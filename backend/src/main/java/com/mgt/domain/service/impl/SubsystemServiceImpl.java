package com.mgt.domain.service.impl;

import com.mgt.domain.entity.Subsystem;
import com.mgt.domain.repository.SubsystemRepository;
import com.mgt.domain.service.SubsystemService;
import com.mgt.infrastructure.common.PageResult;
import com.mgt.infrastructure.exception.BusinessException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * 子系统服务实现
 */
@Service
public class SubsystemServiceImpl implements SubsystemService {

    private final SubsystemRepository subsystemRepository;

    public SubsystemServiceImpl(SubsystemRepository subsystemRepository) {
        this.subsystemRepository = subsystemRepository;
    }

    @Override
    @Transactional
    public Subsystem createSubsystem(Subsystem subsystem) {
        // 检查编码是否已存在
        if (checkCodeExists(subsystem.getCode())) {
            throw new BusinessException("子系统编码已存在");
        }

        // 设置默认值
        subsystem.setId(UUID.randomUUID().toString());
        if (!StringUtils.hasText(subsystem.getStatus())) {
            subsystem.setStatus("ACTIVE");
        }
        LocalDateTime now = LocalDateTime.now();
        subsystem.setCreateTime(now);
        subsystem.setUpdateTime(now);

        return subsystemRepository.save(subsystem);
    }

    @Override
    @Transactional
    public Subsystem updateSubsystem(String id, Subsystem subsystem) {
        // 检查子系统是否存在
        Subsystem existingSubsystem = getSubsystemById(id);

        // 检查编码是否已存在（排除当前ID）
        if (!existingSubsystem.getCode().equals(subsystem.getCode()) && checkCodeExists(subsystem.getCode())) {
            throw new BusinessException("子系统编码已存在");
        }

        // 更新属性
        BeanUtils.copyProperties(subsystem, existingSubsystem, "id", "createTime");
        existingSubsystem.setUpdateTime(LocalDateTime.now());

        return subsystemRepository.save(existingSubsystem);
    }

    @Override
    public Subsystem getSubsystemById(String id) {
        return subsystemRepository.findById(id)
                .orElseThrow(() -> new BusinessException("子系统不存在"));
    }

    @Override
    public List<Subsystem> getAllSubsystems() {
        return subsystemRepository.findAll();
    }

    @Override
    public List<Subsystem> getSubsystemsByType(String type) {
        return subsystemRepository.findByType(type);
    }

    @Override
    public List<Subsystem> getSubsystemsByStatus(String status) {
        return subsystemRepository.findByStatus(status);
    }

    @Override
    @Transactional
    public void deleteSubsystem(String id) {
        // 检查子系统是否存在
        getSubsystemById(id);
        
        // 删除子系统
        subsystemRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Subsystem updateSubsystemStatus(String id, String status) {
        // 检查子系统是否存在
        Subsystem subsystem = getSubsystemById(id);
        
        // 更新状态
        subsystem.setStatus(status);
        subsystem.setUpdateTime(LocalDateTime.now());
        
        return subsystemRepository.save(subsystem);
    }

    @Override
    public boolean checkCodeExists(String code) {
        return subsystemRepository.findByCode(code).isPresent();
    }

    @Override
    public boolean checkCodeExistsExcludeId(String code, String excludeId) {
        return subsystemRepository.findByCode(code)
                .map(subsystem -> !subsystem.getId().equals(excludeId))
                .orElse(false);
    }

    @Override
    public PageResult<Subsystem> getSubsystemsByPage(int page, int size, String name, String type, String status) {
        return subsystemRepository.findByPage(page, size, name, type, status);
    }
} 