package com.mgt.domain.repository;

import com.mgt.domain.entity.Dispatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 调度员仓储接口
 */
@Repository
public interface DispatcherRepository extends JpaRepository<Dispatcher, String> {

    /**
     * 根据组织ID查询调度员列表
     *
     * @param organizationId 组织ID
     * @return 调度员列表
     */
    List<Dispatcher> findByOrganizationId(String organizationId);

    /**
     * 根据用户ID查询调度员
     *
     * @param userId 用户ID
     * @return 调度员
     */
    Optional<Dispatcher> findByUserId(String userId);

    /**
     * 根据状态查询调度员列表
     *
     * @param status 状态
     * @return 调度员列表
     */
    List<Dispatcher> findByStatus(String status);

    /**
     * 根据编码查询调度员
     *
     * @param code 编码
     * @return 调度员
     */
    Optional<Dispatcher> findByCode(String code);

    /**
     * 检查编码是否存在
     *
     * @param code 编码
     * @return 是否存在
     */
    boolean existsByCode(String code);

    /**
     * 检查编码是否存在（排除指定ID）
     *
     * @param code 编码
     * @param id   ID
     * @return 是否存在
     */
    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END FROM Dispatcher d WHERE d.code = :code AND d.id != :id")
    boolean existsByCodeAndIdNot(@Param("code") String code, @Param("id") String id);

    /**
     * 根据名称模糊查询调度员列表
     *
     * @param name 名称
     * @return 调度员列表
     */
    List<Dispatcher> findByNameContaining(String name);

    /**
     * 分页查询调度员
     *
     * @param name           名称（模糊匹配）
     * @param organizationId 组织ID
     * @param status         状态
     * @param pageable       分页参数
     * @return 分页结果
     */
    @Query("SELECT d FROM Dispatcher d WHERE " +
            "(:name IS NULL OR d.name LIKE %:name%) AND " +
            "(:organizationId IS NULL OR d.organizationId = :organizationId) AND " +
            "(:status IS NULL OR d.status = :status)")
    Page<Dispatcher> findByConditions(
            @Param("name") String name,
            @Param("organizationId") String organizationId,
            @Param("status") String status,
            Pageable pageable);
}