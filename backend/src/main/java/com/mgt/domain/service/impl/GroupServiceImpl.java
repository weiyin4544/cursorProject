package com.mgt.domain.service.impl;

import com.mgt.domain.entity.*;
import com.mgt.domain.repository.*;
import com.mgt.domain.service.GroupService;
import com.mgt.infrastructure.common.PageResult;
import com.mgt.infrastructure.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 群组服务实现类
 */
@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private PatchNativeGroupRepository patchNativeGroupRepository;

    @Autowired
    private NativeTerminalRepository nativeTerminalRepository;

    @Autowired
    private LocalDispatcherRepository localDispatcherRepository;

    @Autowired
    private TerminalRepository terminalRepository;

    @Autowired
    private DispatcherRepository dispatcherRepository;

    @Override
    @Transactional
    public Group createGroup(Group group) {
        // 检查编码是否已存在
        Optional<Group> existingGroup = groupRepository.findByCode(group.getCode());
        if (existingGroup.isPresent()) {
            throw new BusinessException("群组编码已存在");
        }

        // 验证群组类型
        validateGroupType(group.getType());

        // 设置默认值
        if (group.getId() == null || group.getId().isEmpty()) {
            group.setId(UUID.randomUUID().toString());
        }
        if (group.getCreateTime() == null) {
            group.setCreateTime(LocalDateTime.now());
        }
        if (group.getUpdateTime() == null) {
            group.setUpdateTime(LocalDateTime.now());
        }
        if (group.getStatus() == null || group.getStatus().isEmpty()) {
            group.setStatus("ENABLED");
        }

        return groupRepository.save(group);
    }

    @Override
    @Transactional
    public Group updateGroup(String id, Group group) {
        Group existingGroup = getGroupById(id);
        if (existingGroup == null) {
            throw new BusinessException("群组不存在");
        }

        // 检查编码是否已存在（排除当前ID）
        Optional<Group> groupWithSameCode = groupRepository.findByCode(group.getCode());
        if (groupWithSameCode.isPresent() && !groupWithSameCode.get().getId().equals(id)) {
            throw new BusinessException("群组编码已存在");
        }

        // 不允许修改群组类型
        if (!existingGroup.getType().equals(group.getType())) {
            throw new BusinessException("不允许修改群组类型");
        }

        // 更新属性
        existingGroup.setName(group.getName());
        existingGroup.setCode(group.getCode());
        existingGroup.setOrganizationId(group.getOrganizationId());
        existingGroup.setDescription(group.getDescription());
        if (group.getStatus() != null) {
            existingGroup.setStatus(group.getStatus());
        }
        existingGroup.setUpdateTime(LocalDateTime.now());

        return groupRepository.save(existingGroup);
    }

    @Override
    public Group getGroupById(String id) {
        Optional<Group> group = groupRepository.findById(id);
        if (!group.isPresent()) {
            throw new BusinessException("群组不存在");
        }
        return group.get();
    }

    @Override
    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    @Override
    public List<Group> getGroupsByOrganizationId(String organizationId) {
        return groupRepository.findByOrganizationId(organizationId);
    }

    @Override
    public List<Group> getGroupsByType(String type) {
        validateGroupType(type);
        return groupRepository.findByType(type);
    }

    @Override
    public List<Group> getGroupsByStatus(String status) {
        return groupRepository.findByStatus(status);
    }

    @Override
    @Transactional
    public void deleteGroup(String id) {
        Group group = getGroupById(id);
        if (group == null) {
            throw new BusinessException("群组不存在");
        }

        // 根据群组类型删除关联数据
        String type = group.getType();
        if ("PATCH".equals(type)) {
            // 删除派接组与原生组的关联
            patchNativeGroupRepository.deleteByPatchGroupId(id);
        } else if ("NATIVE".equals(type)) {
            // 删除原生组与终端的关联
            nativeTerminalRepository.deleteByNativeGroupId(id);
            // 删除派接组与原生组的关联
            patchNativeGroupRepository.deleteByNativeGroupId(id);
        } else if ("LOCAL".equals(type)) {
            // 删除本地组与调度员的关联
            localDispatcherRepository.deleteByLocalGroupId(id);
        }

        // 删除群组
        groupRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Group updateGroupStatus(String id, String status) {
        Group group = getGroupById(id);
        if (group == null) {
            throw new BusinessException("群组不存在");
        }

        group.setStatus(status);
        group.setUpdateTime(LocalDateTime.now());
        return groupRepository.save(group);
    }

    @Override
    public boolean checkCodeExists(String code) {
        Optional<Group> group = groupRepository.findByCode(code);
        return group.isPresent();
    }

    @Override
    public boolean checkCodeExistsExcludeId(String code, String excludeId) {
        Optional<Group> group = groupRepository.findByCode(code);
        return group.isPresent() && !group.get().getId().equals(excludeId);
    }

    @Override
    public PageResult<Group> getGroupsByPage(int page, int size, String name, String organizationId, String type, String status) {
        if (type != null && !type.isEmpty()) {
            validateGroupType(type);
        }
        return groupRepository.findByPage(page, size, name, organizationId, type, status);
    }

    @Override
    @Transactional
    public PatchNativeGroup addNativeGroupToPatchGroup(String patchGroupId, String nativeGroupId) {
        // 验证派接组
        Group patchGroup = getGroupById(patchGroupId);
        if (!"PATCH".equals(patchGroup.getType())) {
            throw new BusinessException("指定的群组不是派接组");
        }

        // 验证原生组
        Group nativeGroup = getGroupById(nativeGroupId);
        if (!"NATIVE".equals(nativeGroup.getType())) {
            throw new BusinessException("指定的群组不是原生组");
        }

        // 检查关联是否已存在
        Optional<PatchNativeGroup> existingRelation = patchNativeGroupRepository.findByPatchGroupIdAndNativeGroupId(patchGroupId, nativeGroupId);
        if (existingRelation.isPresent()) {
            throw new BusinessException("该原生组已添加到派接组");
        }

        // 创建关联
        PatchNativeGroup patchNativeGroup = new PatchNativeGroup();
        patchNativeGroup.setId(UUID.randomUUID().toString());
        patchNativeGroup.setPatchGroupId(patchGroupId);
        patchNativeGroup.setNativeGroupId(nativeGroupId);
        patchNativeGroup.setCreateTime(LocalDateTime.now());

        return patchNativeGroupRepository.save(patchNativeGroup);
    }

    @Override
    @Transactional
    public void removeNativeGroupFromPatchGroup(String patchGroupId, String nativeGroupId) {
        // 验证派接组
        Group patchGroup = getGroupById(patchGroupId);
        if (!"PATCH".equals(patchGroup.getType())) {
            throw new BusinessException("指定的群组不是派接组");
        }

        // 验证原生组
        Group nativeGroup = getGroupById(nativeGroupId);
        if (!"NATIVE".equals(nativeGroup.getType())) {
            throw new BusinessException("指定的群组不是原生组");
        }

        // 检查关联是否存在
        Optional<PatchNativeGroup> existingRelation = patchNativeGroupRepository.findByPatchGroupIdAndNativeGroupId(patchGroupId, nativeGroupId);
        if (!existingRelation.isPresent()) {
            throw new BusinessException("该原生组未添加到派接组");
        }

        // 删除关联
        patchNativeGroupRepository.deleteByPatchGroupIdAndNativeGroupId(patchGroupId, nativeGroupId);
    }

    @Override
    public List<Group> getNativeGroupsByPatchGroupId(String patchGroupId) {
        // 验证派接组
        Group patchGroup = getGroupById(patchGroupId);
        if (!"PATCH".equals(patchGroup.getType())) {
            throw new BusinessException("指定的群组不是派接组");
        }

        // 获取关联的原生组ID列表
        List<PatchNativeGroup> relations = patchNativeGroupRepository.findByPatchGroupId(patchGroupId);
        List<String> nativeGroupIds = relations.stream()
                .map(PatchNativeGroup::getNativeGroupId)
                .collect(Collectors.toList());

        // 获取原生组列表
        List<Group> nativeGroups = new ArrayList<>();
        for (String nativeGroupId : nativeGroupIds) {
            Optional<Group> nativeGroup = groupRepository.findById(nativeGroupId);
            nativeGroup.ifPresent(nativeGroups::add);
        }

        return nativeGroups;
    }

    @Override
    public List<Group> getPatchGroupsByNativeGroupId(String nativeGroupId) {
        // 验证原生组
        Group nativeGroup = getGroupById(nativeGroupId);
        if (!"NATIVE".equals(nativeGroup.getType())) {
            throw new BusinessException("指定的群组不是原生组");
        }

        // 获取关联的派接组ID列表
        List<PatchNativeGroup> relations = patchNativeGroupRepository.findByNativeGroupId(nativeGroupId);
        List<String> patchGroupIds = relations.stream()
                .map(PatchNativeGroup::getPatchGroupId)
                .collect(Collectors.toList());

        // 获取派接组列表
        List<Group> patchGroups = new ArrayList<>();
        for (String patchGroupId : patchGroupIds) {
            Optional<Group> patchGroup = groupRepository.findById(patchGroupId);
            patchGroup.ifPresent(patchGroups::add);
        }

        return patchGroups;
    }

    @Override
    @Transactional
    public NativeTerminal addTerminalToNativeGroup(String nativeGroupId, String terminalId) {
        // 验证原生组
        Group nativeGroup = getGroupById(nativeGroupId);
        if (!"NATIVE".equals(nativeGroup.getType())) {
            throw new BusinessException("指定的群组不是原生组");
        }

        // 验证终端
        Optional<Terminal> terminal = terminalRepository.findById(terminalId);
        if (!terminal.isPresent()) {
            throw new BusinessException("终端不存在");
        }

        // 检查关联是否已存在
        Optional<NativeTerminal> existingRelation = nativeTerminalRepository.findByNativeGroupIdAndTerminalId(nativeGroupId, terminalId);
        if (existingRelation.isPresent()) {
            throw new BusinessException("该终端已添加到原生组");
        }

        // 创建关联
        NativeTerminal nativeTerminal = new NativeTerminal();
        nativeTerminal.setId(UUID.randomUUID().toString());
        nativeTerminal.setNativeGroupId(nativeGroupId);
        nativeTerminal.setTerminalId(terminalId);
        nativeTerminal.setCreateTime(LocalDateTime.now());

        return nativeTerminalRepository.save(nativeTerminal);
    }

    @Override
    @Transactional
    public void removeTerminalFromNativeGroup(String nativeGroupId, String terminalId) {
        // 验证原生组
        Group nativeGroup = getGroupById(nativeGroupId);
        if (!"NATIVE".equals(nativeGroup.getType())) {
            throw new BusinessException("指定的群组不是原生组");
        }

        // 验证终端
        Optional<Terminal> terminal = terminalRepository.findById(terminalId);
        if (!terminal.isPresent()) {
            throw new BusinessException("终端不存在");
        }

        // 检查关联是否存在
        Optional<NativeTerminal> existingRelation = nativeTerminalRepository.findByNativeGroupIdAndTerminalId(nativeGroupId, terminalId);
        if (!existingRelation.isPresent()) {
            throw new BusinessException("该终端未添加到原生组");
        }

        // 删除关联
        nativeTerminalRepository.deleteByNativeGroupIdAndTerminalId(nativeGroupId, terminalId);
    }

    @Override
    public List<Terminal> getTerminalsByNativeGroupId(String nativeGroupId) {
        // 验证原生组
        Group nativeGroup = getGroupById(nativeGroupId);
        if (!"NATIVE".equals(nativeGroup.getType())) {
            throw new BusinessException("指定的群组不是原生组");
        }

        // 获取关联的终端ID列表
        List<NativeTerminal> relations = nativeTerminalRepository.findByNativeGroupId(nativeGroupId);
        List<String> terminalIds = relations.stream()
                .map(NativeTerminal::getTerminalId)
                .collect(Collectors.toList());

        // 获取终端列表
        List<Terminal> terminals = new ArrayList<>();
        for (String terminalId : terminalIds) {
            Optional<Terminal> terminal = terminalRepository.findById(terminalId);
            terminal.ifPresent(terminals::add);
        }

        return terminals;
    }

    @Override
    public List<Group> getNativeGroupsByTerminalId(String terminalId) {
        // 验证终端
        Optional<Terminal> terminal = terminalRepository.findById(terminalId);
        if (!terminal.isPresent()) {
            throw new BusinessException("终端不存在");
        }

        // 获取关联的原生组ID列表
        List<NativeTerminal> relations = nativeTerminalRepository.findByTerminalId(terminalId);
        List<String> nativeGroupIds = relations.stream()
                .map(NativeTerminal::getNativeGroupId)
                .collect(Collectors.toList());

        // 获取原生组列表
        List<Group> nativeGroups = new ArrayList<>();
        for (String nativeGroupId : nativeGroupIds) {
            Optional<Group> nativeGroup = groupRepository.findById(nativeGroupId);
            nativeGroup.ifPresent(nativeGroups::add);
        }

        return nativeGroups;
    }

    @Override
    @Transactional
    public LocalDispatcher addDispatcherToLocalGroup(String localGroupId, String dispatcherId) {
        // 验证本地组
        Group localGroup = getGroupById(localGroupId);
        if (!"LOCAL".equals(localGroup.getType())) {
            throw new BusinessException("指定的群组不是本地组");
        }

        // 验证调度员
        Optional<Dispatcher> dispatcher = dispatcherRepository.findById(dispatcherId);
        if (!dispatcher.isPresent()) {
            throw new BusinessException("调度员不存在");
        }

        // 检查关联是否已存在
        Optional<LocalDispatcher> existingRelation = localDispatcherRepository.findByLocalGroupIdAndDispatcherId(localGroupId, dispatcherId);
        if (existingRelation.isPresent()) {
            throw new BusinessException("该调度员已添加到本地组");
        }

        // 创建关联
        LocalDispatcher localDispatcher = new LocalDispatcher();
        localDispatcher.setId(UUID.randomUUID().toString());
        localDispatcher.setLocalGroupId(localGroupId);
        localDispatcher.setDispatcherId(dispatcherId);
        localDispatcher.setCreateTime(LocalDateTime.now());

        return localDispatcherRepository.save(localDispatcher);
    }

    @Override
    @Transactional
    public void removeDispatcherFromLocalGroup(String localGroupId, String dispatcherId) {
        // 验证本地组
        Group localGroup = getGroupById(localGroupId);
        if (!"LOCAL".equals(localGroup.getType())) {
            throw new BusinessException("指定的群组不是本地组");
        }

        // 验证调度员
        Optional<Dispatcher> dispatcher = dispatcherRepository.findById(dispatcherId);
        if (!dispatcher.isPresent()) {
            throw new BusinessException("调度员不存在");
        }

        // 检查关联是否存在
        Optional<LocalDispatcher> existingRelation = localDispatcherRepository.findByLocalGroupIdAndDispatcherId(localGroupId, dispatcherId);
        if (!existingRelation.isPresent()) {
            throw new BusinessException("该调度员未添加到本地组");
        }

        // 删除关联
        localDispatcherRepository.deleteByLocalGroupIdAndDispatcherId(localGroupId, dispatcherId);
    }

    @Override
    public List<Dispatcher> getDispatchersByLocalGroupId(String localGroupId) {
        // 验证本地组
        Group localGroup = getGroupById(localGroupId);
        if (!"LOCAL".equals(localGroup.getType())) {
            throw new BusinessException("指定的群组不是本地组");
        }

        // 获取关联的调度员ID列表
        List<LocalDispatcher> relations = localDispatcherRepository.findByLocalGroupId(localGroupId);
        List<String> dispatcherIds = relations.stream()
                .map(LocalDispatcher::getDispatcherId)
                .collect(Collectors.toList());

        // 获取调度员列表
        List<Dispatcher> dispatchers = new ArrayList<>();
        for (String dispatcherId : dispatcherIds) {
            Optional<Dispatcher> dispatcher = dispatcherRepository.findById(dispatcherId);
            dispatcher.ifPresent(dispatchers::add);
        }

        return dispatchers;
    }

    @Override
    public List<Group> getLocalGroupsByDispatcherId(String dispatcherId) {
        // 验证调度员
        Optional<Dispatcher> dispatcher = dispatcherRepository.findById(dispatcherId);
        if (!dispatcher.isPresent()) {
            throw new BusinessException("调度员不存在");
        }

        // 获取关联的本地组ID列表
        List<LocalDispatcher> relations = localDispatcherRepository.findByDispatcherId(dispatcherId);
        List<String> localGroupIds = relations.stream()
                .map(LocalDispatcher::getLocalGroupId)
                .collect(Collectors.toList());

        // 获取本地组列表
        List<Group> localGroups = new ArrayList<>();
        for (String localGroupId : localGroupIds) {
            Optional<Group> localGroup = groupRepository.findById(localGroupId);
            localGroup.ifPresent(localGroups::add);
        }

        return localGroups;
    }

    /**
     * 验证群组类型
     *
     * @param type 群组类型
     */
    private void validateGroupType(String type) {
        if (type == null || type.isEmpty()) {
            throw new BusinessException("群组类型不能为空");
        }
        if (!("PATCH".equals(type) || "NATIVE".equals(type) || "LOCAL".equals(type))) {
            throw new BusinessException("无效的群组类型，有效值：PATCH, NATIVE, LOCAL");
        }
    }
} 