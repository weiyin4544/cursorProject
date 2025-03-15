package com.mgt.domain.service;

import com.mgt.domain.entity.Subsystem;
import com.mgt.infrastructure.common.PageResult;

import java.util.List;

/**
 * 子系统服务接口
 */
public interface SubsystemService {
    /**
     * 创建子系统
     *
     * @param subsystem 子系统信息
     * @return 创建后的子系统
     */
    Subsystem createSubsystem(Subsystem subsystem);

    /**
     * 更新子系统
     *
     * @param id        子系统ID
     * @param subsystem 子系统信息
     * @return 更新后的子系统
     */
    Subsystem updateSubsystem(String id, Subsystem subsystem);

    /**
     * 根据ID查询子系统
     *
     * @param id 子系统ID
     * @return 子系统信息
     */
    Subsystem getSubsystemById(String id);

    /**
     * 查询所有子系统
     *
     * @return 子系统列表
     */
    List<Subsystem> getAllSubsystems();

    /**
     * 根据类型查询子系统
     *
     * @param type 子系统类型
     * @return 子系统列表
     */
    List<Subsystem> getSubsystemsByType(String type);

    /**
     * 根据状态查询子系统
     *
     * @param status 状态
     * @return 子系统列表
     */
    List<Subsystem> getSubsystemsByStatus(String status);

    /**
     * 根据ID删除子系统
     *
     * @param id 子系统ID
     */
    void deleteSubsystem(String id);

    /**
     * 更新子系统状态
     *
     * @param id     子系统ID
     * @param status 状态
     * @return 更新后的子系统
     */
    Subsystem updateSubsystemStatus(String id, String status);

    /**
     * 检查子系统编码是否存在
     *
     * @param code 子系统编码
     * @return 是否存在
     */
    boolean checkCodeExists(String code);

    /**
     * 检查子系统编码是否存在（排除指定ID）
     *
     * @param code     子系统编码
     * @param excludeId 排除的ID
     * @return 是否存在
     */
    boolean checkCodeExistsExcludeId(String code, String excludeId);

    /**
     * 分页查询子系统
     *
     * @param page     页码
     * @param size     每页大小
     * @param name     子系统名称
     * @param type     子系统类型
     * @param status   状态
     * @return 子系统列表和总数
     */
    PageResult<Subsystem> getSubsystemsByPage(int page, int size, String name, String type, String status);
} 