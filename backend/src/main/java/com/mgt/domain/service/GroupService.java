package com.mgt.domain.service;

import com.mgt.domain.entity.Group;
import com.mgt.domain.entity.NativeTerminal;
import com.mgt.domain.entity.PatchNativeGroup;
import com.mgt.domain.entity.LocalDispatcher;
import com.mgt.infrastructure.common.PageResult;

import java.util.List;

/**
 * 群组服务接口
 */
public interface GroupService {
    /**
     * 创建群组
     *
     * @param group 群组信息
     * @return 创建后的群组
     */
    Group createGroup(Group group);

    /**
     * 更新群组
     *
     * @param id    群组ID
     * @param group 群组信息
     * @return 更新后的群组
     */
    Group updateGroup(String id, Group group);

    /**
     * 根据ID查询群组
     *
     * @param id 群组ID
     * @return 群组信息
     */
    Group getGroupById(String id);

    /**
     * 查询所有群组
     *
     * @return 群组列表
     */
    List<Group> getAllGroups();

    /**
     * 根据组织ID查询群组
     *
     * @param organizationId 组织ID
     * @return 群组列表
     */
    List<Group> getGroupsByOrganizationId(String organizationId);

    /**
     * 根据类型查询群组
     *
     * @param type 群组类型
     * @return 群组列表
     */
    List<Group> getGroupsByType(String type);

    /**
     * 根据状态查询群组
     *
     * @param status 状态
     * @return 群组列表
     */
    List<Group> getGroupsByStatus(String status);

    /**
     * 根据ID删除群组
     *
     * @param id 群组ID
     */
    void deleteGroup(String id);

    /**
     * 更新群组状态
     *
     * @param id     群组ID
     * @param status 状态
     * @return 更新后的群组
     */
    Group updateGroupStatus(String id, String status);

    /**
     * 检查群组编码是否存在
     *
     * @param code 群组编码
     * @return 是否存在
     */
    boolean checkCodeExists(String code);

    /**
     * 检查群组编码是否存在（排除指定ID）
     *
     * @param code     群组编码
     * @param excludeId 排除的ID
     * @return 是否存在
     */
    boolean checkCodeExistsExcludeId(String code, String excludeId);

    /**
     * 分页查询群组
     *
     * @param page           页码
     * @param size           每页大小
     * @param name           群组名称
     * @param organizationId 组织ID
     * @param type           群组类型
     * @param status         状态
     * @return 群组列表和总数
     */
    PageResult<Group> getGroupsByPage(int page, int size, String name, String organizationId, 
                                     String type, String status);

    /**
     * 添加原生组到派接组
     *
     * @param patchGroupId  派接组ID
     * @param nativeGroupId 原生组ID
     * @return 关联信息
     */
    PatchNativeGroup addNativeGroupToPatchGroup(String patchGroupId, String nativeGroupId);

    /**
     * 从派接组移除原生组
     *
     * @param patchGroupId  派接组ID
     * @param nativeGroupId 原生组ID
     */
    void removeNativeGroupFromPatchGroup(String patchGroupId, String nativeGroupId);

    /**
     * 获取派接组下的原生组列表
     *
     * @param patchGroupId 派接组ID
     * @return 原生组列表
     */
    List<Group> getNativeGroupsByPatchGroupId(String patchGroupId);

    /**
     * 获取原生组所属的派接组列表
     *
     * @param nativeGroupId 原生组ID
     * @return 派接组列表
     */
    List<Group> getPatchGroupsByNativeGroupId(String nativeGroupId);

    /**
     * 添加终端到原生组
     *
     * @param nativeGroupId 原生组ID
     * @param terminalId    终端ID
     * @return 关联信息
     */
    NativeTerminal addTerminalToNativeGroup(String nativeGroupId, String terminalId);

    /**
     * 从原生组移除终端
     *
     * @param nativeGroupId 原生组ID
     * @param terminalId    终端ID
     */
    void removeTerminalFromNativeGroup(String nativeGroupId, String terminalId);

    /**
     * 获取原生组下的终端列表
     *
     * @param nativeGroupId 原生组ID
     * @return 终端列表
     */
    List<com.mgt.domain.entity.Terminal> getTerminalsByNativeGroupId(String nativeGroupId);

    /**
     * 获取终端所属的原生组列表
     *
     * @param terminalId 终端ID
     * @return 原生组列表
     */
    List<Group> getNativeGroupsByTerminalId(String terminalId);

    /**
     * 添加调度员到本地组
     *
     * @param localGroupId 本地组ID
     * @param dispatcherId 调度员ID
     * @return 关联信息
     */
    LocalDispatcher addDispatcherToLocalGroup(String localGroupId, String dispatcherId);

    /**
     * 从本地组移除调度员
     *
     * @param localGroupId 本地组ID
     * @param dispatcherId 调度员ID
     */
    void removeDispatcherFromLocalGroup(String localGroupId, String dispatcherId);

    /**
     * 获取本地组下的调度员列表
     *
     * @param localGroupId 本地组ID
     * @return 调度员列表
     */
    List<com.mgt.domain.entity.Dispatcher> getDispatchersByLocalGroupId(String localGroupId);

    /**
     * 获取调度员所属的本地组列表
     *
     * @param dispatcherId 调度员ID
     * @return 本地组列表
     */
    List<Group> getLocalGroupsByDispatcherId(String dispatcherId);
} 