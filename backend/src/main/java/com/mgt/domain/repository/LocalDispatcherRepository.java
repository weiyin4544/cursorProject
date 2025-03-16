package com.mgt.domain.repository;

import com.mgt.domain.entity.LocalDispatcher;

import java.util.List;
import java.util.Optional;

/**
 * 本地组与调度员关联仓储接口
 */
public interface LocalDispatcherRepository {
    /**
     * 保存本地组与调度员关联
     *
     * @param localDispatcher 本地组与调度员关联
     * @return 保存后的本地组与调度员关联
     */
    LocalDispatcher save(LocalDispatcher localDispatcher);

    /**
     * 根据ID查询本地组与调度员关联
     *
     * @param id 关联ID
     * @return 本地组与调度员关联
     */
    Optional<LocalDispatcher> findById(String id);

    /**
     * 根据本地组ID查询关联
     *
     * @param localGroupId 本地组ID
     * @return 本地组与调度员关联列表
     */
    List<LocalDispatcher> findByLocalGroupId(String localGroupId);

    /**
     * 根据调度员ID查询关联
     *
     * @param dispatcherId 调度员ID
     * @return 本地组与调度员关联列表
     */
    List<LocalDispatcher> findByDispatcherId(String dispatcherId);

    /**
     * 根据本地组ID和调度员ID查询关联
     *
     * @param localGroupId 本地组ID
     * @param dispatcherId 调度员ID
     * @return 本地组与调度员关联
     */
    Optional<LocalDispatcher> findByLocalGroupIdAndDispatcherId(String localGroupId, String dispatcherId);

    /**
     * 根据ID删除本地组与调度员关联
     *
     * @param id 关联ID
     */
    void deleteById(String id);

    /**
     * 根据本地组ID删除关联
     *
     * @param localGroupId 本地组ID
     */
    void deleteByLocalGroupId(String localGroupId);

    /**
     * 根据调度员ID删除关联
     *
     * @param dispatcherId 调度员ID
     */
    void deleteByDispatcherId(String dispatcherId);

    /**
     * 根据本地组ID和调度员ID删除关联
     *
     * @param localGroupId 本地组ID
     * @param dispatcherId 调度员ID
     */
    void deleteByLocalGroupIdAndDispatcherId(String localGroupId, String dispatcherId);
} 