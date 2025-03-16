package com.mgt.domain.service;

import com.mgt.domain.entity.Dispatcher;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 调度员服务接口
 */
public interface DispatcherService {

    /**
     * 创建调度员
     *
     * @param dispatcher 调度员信息
     * @return 创建后的调度员
     */
    Dispatcher createDispatcher(Dispatcher dispatcher);

    /**
     * 更新调度员
     *
     * @param id         调度员ID
     * @param dispatcher 调度员信息
     * @return 更新后的调度员
     */
    Dispatcher updateDispatcher(String id, Dispatcher dispatcher);

    /**
     * 根据ID获取调度员
     *
     * @param id 调度员ID
     * @return 调度员信息
     */
    Dispatcher getDispatcherById(String id);

    /**
     * 获取所有调度员
     *
     * @return 调度员列表
     */
    List<Dispatcher> getAllDispatchers();

    /**
     * 根据组织ID获取调度员列表
     *
     * @param organizationId 组织ID
     * @return 调度员列表
     */
    List<Dispatcher> getDispatchersByOrganizationId(String organizationId);

    /**
     * 根据用户ID获取调度员
     *
     * @param userId 用户ID
     * @return 调度员信息
     */
    Dispatcher getDispatcherByUserId(String userId);

    /**
     * 根据状态获取调度员列表
     *
     * @param status 状态
     * @return 调度员列表
     */
    List<Dispatcher> getDispatchersByStatus(String status);

    /**
     * 删除调度员
     *
     * @param id 调度员ID
     */
    void deleteDispatcher(String id);

    /**
     * 更新调度员状态
     *
     * @param id     调度员ID
     * @param status 状态
     * @return 更新后的调度员
     */
    Dispatcher updateDispatcherStatus(String id, String status);

    /**
     * 检查编码是否存在
     *
     * @param code 编码
     * @return 是否存在
     */
    boolean checkCodeExists(String code);

    /**
     * 检查编码是否存在（排除指定ID）
     *
     * @param code     编码
     * @param excludeId 排除的ID
     * @return 是否存在
     */
    boolean checkCodeExistsExcludeId(String code, String excludeId);

    /**
     * 分页查询调度员
     *
     * @param page           页码
     * @param size           每页大小
     * @param name           名称（模糊匹配）
     * @param organizationId 组织ID
     * @param status         状态
     * @return 分页结果
     */
    Page<Dispatcher> getDispatchersByPage(int page, int size, String name, String organizationId, String status);

    /**
     * 为调度员分配子系统
     *
     * @param dispatcherId 调度员ID
     * @param subsystemIds 子系统ID列表
     */
    void assignSubsystems(String dispatcherId, List<String> subsystemIds);

    /**
     * 移除调度员的子系统分配
     *
     * @param dispatcherId 调度员ID
     * @param subsystemId  子系统ID
     */
    void removeSubsystem(String dispatcherId, String subsystemId);

    /**
     * 获取调度员已分配的子系统ID列表
     *
     * @param dispatcherId 调度员ID
     * @return 子系统ID列表
     */
    List<String> getAssignedSubsystemIds(String dispatcherId);

    /**
     * 检查调度员是否已分配指定子系统
     *
     * @param dispatcherId 调度员ID
     * @param subsystemId  子系统ID
     * @return 是否已分配
     */
    boolean isSubsystemAssigned(String dispatcherId, String subsystemId);
} 