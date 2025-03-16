package com.mgt.domain.service.impl;

import com.mgt.domain.entity.Dispatcher;
import com.mgt.domain.entity.DispatcherSubsystem;
import com.mgt.domain.repository.DispatcherRepository;
import com.mgt.domain.repository.DispatcherSubsystemRepository;
import com.mgt.domain.service.DispatcherService;
import com.mgt.infrastructure.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 调度员服务实现类
 */
@Service
public class DispatcherServiceImpl implements DispatcherService {

    @Autowired
    private DispatcherRepository dispatcherRepository;

    @Autowired
    private DispatcherSubsystemRepository dispatcherSubsystemRepository;

    @Override
    @Transactional
    public Dispatcher createDispatcher(Dispatcher dispatcher) {
        // 检查编码是否已存在
        if (dispatcherRepository.existsByCode(dispatcher.getCode())) {
            throw new BusinessException("调度员编码已存在");
        }

        // 设置默认值
        if (dispatcher.getId() == null || dispatcher.getId().isEmpty()) {
            dispatcher.setId(UUID.randomUUID().toString());
        }
        if (dispatcher.getCreateTime() == null) {
            dispatcher.setCreateTime(LocalDateTime.now());
        }
        if (dispatcher.getUpdateTime() == null) {
            dispatcher.setUpdateTime(LocalDateTime.now());
        }
        if (dispatcher.getStatus() == null || dispatcher.getStatus().isEmpty()) {
            dispatcher.setStatus("ACTIVE");
        }

        return dispatcherRepository.save(dispatcher);
    }

    @Override
    @Transactional
    public Dispatcher updateDispatcher(String id, Dispatcher dispatcher) {
        Dispatcher existingDispatcher = getDispatcherById(id);
        if (existingDispatcher == null) {
            throw new BusinessException("调度员不存在");
        }

        // 检查编码是否已存在（排除当前ID）
        if (!existingDispatcher.getCode().equals(dispatcher.getCode()) && 
            dispatcherRepository.existsByCodeAndIdNot(dispatcher.getCode(), id)) {
            throw new BusinessException("调度员编码已存在");
        }

        // 更新属性
        existingDispatcher.setName(dispatcher.getName());
        existingDispatcher.setCode(dispatcher.getCode());
        existingDispatcher.setOrganizationId(dispatcher.getOrganizationId());
        existingDispatcher.setUserId(dispatcher.getUserId());
        if (dispatcher.getStatus() != null) {
            existingDispatcher.setStatus(dispatcher.getStatus());
        }
        existingDispatcher.setUpdateTime(LocalDateTime.now());

        return dispatcherRepository.save(existingDispatcher);
    }

    @Override
    public Dispatcher getDispatcherById(String id) {
        return dispatcherRepository.findById(id)
                .orElseThrow(() -> new BusinessException("调度员不存在"));
    }

    @Override
    public List<Dispatcher> getAllDispatchers() {
        return dispatcherRepository.findAll();
    }

    @Override
    public List<Dispatcher> getDispatchersByOrganizationId(String organizationId) {
        return dispatcherRepository.findByOrganizationId(organizationId);
    }

    @Override
    public Dispatcher getDispatcherByUserId(String userId) {
        return dispatcherRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException("未找到与该用户关联的调度员"));
    }

    @Override
    public List<Dispatcher> getDispatchersByStatus(String status) {
        return dispatcherRepository.findByStatus(status);
    }

    @Override
    @Transactional
    public void deleteDispatcher(String id) {
        Dispatcher dispatcher = getDispatcherById(id);
        if (dispatcher == null) {
            throw new BusinessException("调度员不存在");
        }

        // 删除关联的子系统记录
        dispatcherSubsystemRepository.deleteByDispatcherId(id);
        
        // 删除调度员
        dispatcherRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Dispatcher updateDispatcherStatus(String id, String status) {
        Dispatcher dispatcher = getDispatcherById(id);
        if (dispatcher == null) {
            throw new BusinessException("调度员不存在");
        }

        dispatcher.setStatus(status);
        dispatcher.setUpdateTime(LocalDateTime.now());
        return dispatcherRepository.save(dispatcher);
    }

    @Override
    public boolean checkCodeExists(String code) {
        return dispatcherRepository.existsByCode(code);
    }

    @Override
    public boolean checkCodeExistsExcludeId(String code, String excludeId) {
        return dispatcherRepository.existsByCodeAndIdNot(code, excludeId);
    }

    @Override
    public Page<Dispatcher> getDispatchersByPage(int page, int size, String name, String organizationId, String status) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        return dispatcherRepository.findByConditions(
                StringUtils.hasText(name) ? name : null,
                StringUtils.hasText(organizationId) ? organizationId : null,
                StringUtils.hasText(status) ? status : null,
                pageable);
    }

    @Override
    @Transactional
    public void assignSubsystems(String dispatcherId, List<String> subsystemIds) {
        // 验证调度员是否存在
        Dispatcher dispatcher = getDispatcherById(dispatcherId);
        if (dispatcher == null) {
            throw new BusinessException("调度员不存在");
        }

        // 获取当前已分配的子系统ID列表
        List<String> currentSubsystemIds = getAssignedSubsystemIds(dispatcherId);

        // 需要新增的子系统ID
        List<String> toAddSubsystemIds = new ArrayList<>(subsystemIds);
        toAddSubsystemIds.removeAll(currentSubsystemIds);

        // 需要删除的子系统ID
        List<String> toRemoveSubsystemIds = new ArrayList<>(currentSubsystemIds);
        toRemoveSubsystemIds.removeAll(subsystemIds);

        // 删除不再需要的关联
        for (String subsystemId : toRemoveSubsystemIds) {
            dispatcherSubsystemRepository.deleteByDispatcherIdAndSubsystemId(dispatcherId, subsystemId);
        }

        // 添加新的关联
        for (String subsystemId : toAddSubsystemIds) {
            DispatcherSubsystem dispatcherSubsystem = new DispatcherSubsystem();
            dispatcherSubsystem.setId(UUID.randomUUID().toString());
            dispatcherSubsystem.setDispatcherId(dispatcherId);
            dispatcherSubsystem.setSubsystemId(subsystemId);
            dispatcherSubsystem.setCreateTime(LocalDateTime.now());
            dispatcherSubsystemRepository.save(dispatcherSubsystem);
        }
    }

    @Override
    @Transactional
    public void removeSubsystem(String dispatcherId, String subsystemId) {
        // 验证调度员是否存在
        Dispatcher dispatcher = getDispatcherById(dispatcherId);
        if (dispatcher == null) {
            throw new BusinessException("调度员不存在");
        }

        // 检查关联是否存在
        if (!isSubsystemAssigned(dispatcherId, subsystemId)) {
            throw new BusinessException("调度员未分配该子系统");
        }

        // 删除关联
        dispatcherSubsystemRepository.deleteByDispatcherIdAndSubsystemId(dispatcherId, subsystemId);
    }

    @Override
    public List<String> getAssignedSubsystemIds(String dispatcherId) {
        List<DispatcherSubsystem> dispatcherSubsystems = dispatcherSubsystemRepository.findByDispatcherId(dispatcherId);
        return dispatcherSubsystems.stream()
                .map(DispatcherSubsystem::getSubsystemId)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isSubsystemAssigned(String dispatcherId, String subsystemId) {
        return dispatcherSubsystemRepository.existsByDispatcherIdAndSubsystemId(dispatcherId, subsystemId);
    }
}