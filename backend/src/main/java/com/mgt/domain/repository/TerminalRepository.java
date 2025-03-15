package com.mgt.domain.repository;

import com.mgt.domain.entity.Terminal;
import com.mgt.infrastructure.common.PageResult;

import java.util.List;
import java.util.Optional;

/**
 * 终端仓储接口
 */
public interface TerminalRepository {
    /**
     * 保存终端
     *
     * @param terminal 终端
     * @return 保存后的终端
     */
    Terminal save(Terminal terminal);

    /**
     * 根据ID查询终端
     *
     * @param id 终端ID
     * @return 终端
     */
    Optional<Terminal> findById(String id);

    /**
     * 查询所有终端
     *
     * @return 终端列表
     */
    List<Terminal> findAll();

    /**
     * 根据组织ID查询终端
     *
     * @param organizationId 组织ID
     * @return 终端列表
     */
    List<Terminal> findByOrganizationId(String organizationId);

    /**
     * 根据子系统ID查询终端
     *
     * @param subsystemId 子系统ID
     * @return 终端列表
     */
    List<Terminal> findBySubsystemId(String subsystemId);

    /**
     * 根据人员ID查询终端
     *
     * @param personId 人员ID
     * @return 终端列表
     */
    List<Terminal> findByPersonId(String personId);

    /**
     * 根据状态查询终端
     *
     * @param status 状态
     * @return 终端列表
     */
    List<Terminal> findByStatus(String status);

    /**
     * 根据编码查询终端
     *
     * @param code 终端编码
     * @return 终端
     */
    Optional<Terminal> findByCode(String code);

    /**
     * 根据名称模糊查询终端
     *
     * @param name 终端名称
     * @return 终端列表
     */
    List<Terminal> findByNameLike(String name);

    /**
     * 根据ID删除终端
     *
     * @param id 终端ID
     */
    void deleteById(String id);

    /**
     * 分页查询终端
     *
     * @param page           页码
     * @param size           每页大小
     * @param name           终端名称
     * @param organizationId 组织ID
     * @param subsystemId    子系统ID
     * @param personId       人员ID
     * @param status         状态
     * @return 终端列表和总数
     */
    PageResult<Terminal> findByPage(int page, int size, String name, String organizationId, 
                                   String subsystemId, String personId, String status);
} 