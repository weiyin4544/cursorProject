package com.mgt.domain.repository;

import com.mgt.domain.entity.DispatcherSubsystem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 调度员与子系统关联仓储接口
 */
@Repository
public interface DispatcherSubsystemRepository extends JpaRepository<DispatcherSubsystem, String> {

    /**
     * 根据调度员ID查询关联记录
     *
     * @param dispatcherId 调度员ID
     * @return 关联记录列表
     */
    List<DispatcherSubsystem> findByDispatcherId(String dispatcherId);

    /**
     * 根据子系统ID查询关联记录
     *
     * @param subsystemId 子系统ID
     * @return 关联记录列表
     */
    List<DispatcherSubsystem> findBySubsystemId(String subsystemId);

    /**
     * 根据调度员ID和子系统ID查询关联记录
     *
     * @param dispatcherId 调度员ID
     * @param subsystemId 子系统ID
     * @return 关联记录
     */
    DispatcherSubsystem findByDispatcherIdAndSubsystemId(String dispatcherId, String subsystemId);

    /**
     * 根据调度员ID删除关联记录
     *
     * @param dispatcherId 调度员ID
     */
    void deleteByDispatcherId(String dispatcherId);

    /**
     * 根据子系统ID删除关联记录
     *
     * @param subsystemId 子系统ID
     */
    void deleteBySubsystemId(String subsystemId);

    /**
     * 根据调度员ID和子系统ID删除关联记录
     *
     * @param dispatcherId 调度员ID
     * @param subsystemId 子系统ID
     */
    void deleteByDispatcherIdAndSubsystemId(String dispatcherId, String subsystemId);

    /**
     * 检查调度员是否关联了指定子系统
     *
     * @param dispatcherId 调度员ID
     * @param subsystemId 子系统ID
     * @return 是否存在关联
     */
    boolean existsByDispatcherIdAndSubsystemId(String dispatcherId, String subsystemId);

    /**
     * 统计调度员关联的子系统数量
     *
     * @param dispatcherId 调度员ID
     * @return 关联数量
     */
    long countByDispatcherId(String dispatcherId);

    /**
     * 统计子系统关联的调度员数量
     *
     * @param subsystemId 子系统ID
     * @return 关联数量
     */
    long countBySubsystemId(String subsystemId);
} 