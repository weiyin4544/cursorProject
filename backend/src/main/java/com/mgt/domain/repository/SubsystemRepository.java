package com.mgt.domain.repository;

import com.mgt.domain.entity.Subsystem;
import com.mgt.infrastructure.common.PageResult;

import java.util.List;
import java.util.Optional;

/**
 * 子系统仓储接口
 */
public interface SubsystemRepository {
    /**
     * 保存子系统
     *
     * @param subsystem 子系统
     * @return 保存后的子系统
     */
    Subsystem save(Subsystem subsystem);

    /**
     * 根据ID查询子系统
     *
     * @param id 子系统ID
     * @return 子系统
     */
    Optional<Subsystem> findById(String id);

    /**
     * 查询所有子系统
     *
     * @return 子系统列表
     */
    List<Subsystem> findAll();

    /**
     * 根据类型查询子系统
     *
     * @param type 子系统类型
     * @return 子系统列表
     */
    List<Subsystem> findByType(String type);

    /**
     * 根据状态查询子系统
     *
     * @param status 状态
     * @return 子系统列表
     */
    List<Subsystem> findByStatus(String status);

    /**
     * 根据编码查询子系统
     *
     * @param code 子系统编码
     * @return 子系统
     */
    Optional<Subsystem> findByCode(String code);

    /**
     * 根据名称模糊查询子系统
     *
     * @param name 子系统名称
     * @return 子系统列表
     */
    List<Subsystem> findByNameLike(String name);

    /**
     * 根据ID删除子系统
     *
     * @param id 子系统ID
     */
    void deleteById(String id);

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
    PageResult<Subsystem> findByPage(int page, int size, String name, String type, String status);
} 