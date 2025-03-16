package com.mgt.domain.repository;

import com.mgt.domain.entity.Group;
import com.mgt.infrastructure.common.PageResult;

import java.util.List;
import java.util.Optional;

/**
 * 群组仓储接口
 */
public interface GroupRepository {
    /**
     * 保存群组
     *
     * @param group 群组
     * @return 保存后的群组
     */
    Group save(Group group);

    /**
     * 根据ID查询群组
     *
     * @param id 群组ID
     * @return 群组
     */
    Optional<Group> findById(String id);

    /**
     * 查询所有群组
     *
     * @return 群组列表
     */
    List<Group> findAll();

    /**
     * 根据组织ID查询群组
     *
     * @param organizationId 组织ID
     * @return 群组列表
     */
    List<Group> findByOrganizationId(String organizationId);

    /**
     * 根据类型查询群组
     *
     * @param type 群组类型
     * @return 群组列表
     */
    List<Group> findByType(String type);

    /**
     * 根据状态查询群组
     *
     * @param status 状态
     * @return 群组列表
     */
    List<Group> findByStatus(String status);

    /**
     * 根据编码查询群组
     *
     * @param code 群组编码
     * @return 群组
     */
    Optional<Group> findByCode(String code);

    /**
     * 根据名称模糊查询群组
     *
     * @param name 群组名称
     * @return 群组列表
     */
    List<Group> findByNameLike(String name);

    /**
     * 根据ID删除群组
     *
     * @param id 群组ID
     */
    void deleteById(String id);

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
    PageResult<Group> findByPage(int page, int size, String name, String organizationId, 
                                String type, String status);
} 