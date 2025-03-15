package com.mgt.infrastructure.repository;

import com.mgt.domain.entity.Department;
import com.mgt.domain.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 部门仓储实现类
 */
@Repository
public class DepartmentRepositoryImpl implements DepartmentRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Department> rowMapper = new DepartmentRowMapper();

    @Override
    public Department save(Department department) {
        if (department.getId() == null) {
            // 新增部门
            String id = UUID.randomUUID().toString();
            department.setId(id);
            jdbcTemplate.update(
                    "INSERT INTO t_department (id, name, code, description, organization_id, parent_id, manager_id, status, sort, create_time, update_time) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    department.getId(),
                    department.getName(),
                    department.getCode(),
                    department.getDescription(),
                    department.getOrganizationId(),
                    department.getParentId(),
                    department.getManagerId(),
                    department.getStatus(),
                    department.getSort(),
                    department.getCreateTime(),
                    department.getUpdateTime()
            );
        } else {
            // 更新部门
            jdbcTemplate.update(
                    "UPDATE t_department SET name = ?, code = ?, description = ?, organization_id = ?, parent_id = ?, " +
                            "manager_id = ?, status = ?, sort = ?, update_time = ? WHERE id = ?",
                    department.getName(),
                    department.getCode(),
                    department.getDescription(),
                    department.getOrganizationId(),
                    department.getParentId(),
                    department.getManagerId(),
                    department.getStatus(),
                    department.getSort(),
                    department.getUpdateTime(),
                    department.getId()
            );
        }
        return department;
    }

    @Override
    public Optional<Department> findById(String id) {
        List<Department> departments = jdbcTemplate.query(
                "SELECT * FROM t_department WHERE id = ?",
                rowMapper,
                id
        );
        return departments.isEmpty() ? Optional.empty() : Optional.of(departments.get(0));
    }

    @Override
    public Optional<Department> findByCode(String code) {
        List<Department> departments = jdbcTemplate.query(
                "SELECT * FROM t_department WHERE code = ?",
                rowMapper,
                code
        );
        return departments.isEmpty() ? Optional.empty() : Optional.of(departments.get(0));
    }

    @Override
    public List<Department> findAll() {
        return jdbcTemplate.query("SELECT * FROM t_department ORDER BY sort", rowMapper);
    }

    @Override
    public List<Department> findByCondition(Department condition) {
        StringBuilder sql = new StringBuilder("SELECT * FROM t_department WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (StringUtils.hasText(condition.getName())) {
            sql.append(" AND name LIKE ?");
            params.add("%" + condition.getName() + "%");
        }

        if (StringUtils.hasText(condition.getCode())) {
            sql.append(" AND code LIKE ?");
            params.add("%" + condition.getCode() + "%");
        }

        if (StringUtils.hasText(condition.getOrganizationId())) {
            sql.append(" AND organization_id = ?");
            params.add(condition.getOrganizationId());
        }

        if (StringUtils.hasText(condition.getParentId())) {
            sql.append(" AND parent_id = ?");
            params.add(condition.getParentId());
        }

        if (StringUtils.hasText(condition.getManagerId())) {
            sql.append(" AND manager_id = ?");
            params.add(condition.getManagerId());
        }

        if (StringUtils.hasText(condition.getStatus())) {
            sql.append(" AND status = ?");
            params.add(condition.getStatus());
        }

        sql.append(" ORDER BY sort");

        return jdbcTemplate.query(sql.toString(), rowMapper, params.toArray());
    }

    @Override
    public List<Department> findByOrganizationId(String organizationId) {
        return jdbcTemplate.query(
                "SELECT * FROM t_department WHERE organization_id = ? ORDER BY sort",
                rowMapper,
                organizationId
        );
    }

    @Override
    public List<Department> findByParentId(String parentId) {
        String sql;
        Object[] params;

        if (parentId == null) {
            sql = "SELECT * FROM t_department WHERE parent_id IS NULL ORDER BY sort";
            params = new Object[]{};
        } else {
            sql = "SELECT * FROM t_department WHERE parent_id = ? ORDER BY sort";
            params = new Object[]{parentId};
        }

        return jdbcTemplate.query(sql, rowMapper, params);
    }

    @Override
    public List<Department> findTree() {
        List<Department> allDepartments = findAll();
        return buildTree(allDepartments, null);
    }

    @Override
    public List<Department> findTreeByOrganizationId(String organizationId) {
        List<Department> departments = findByOrganizationId(organizationId);
        return buildTree(departments, null);
    }

    private List<Department> buildTree(List<Department> departments, String parentId) {
        return departments.stream()
                .filter(dept -> {
                    if (parentId == null) {
                        return dept.getParentId() == null;
                    } else {
                        return parentId.equals(dept.getParentId());
                    }
                })
                .peek(dept -> {
                    List<Department> children = buildTree(departments, dept.getId());
                    // 这里我们不能直接设置children属性，因为Department类中没有这个属性
                    // 在实际应用中，可以创建一个DepartmentTree类或者使用Map来表示树结构
                })
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(String id) {
        jdbcTemplate.update("DELETE FROM t_department WHERE id = ?", id);
    }

    @Override
    public boolean hasChildren(String id) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM t_department WHERE parent_id = ?",
                Integer.class,
                id
        );
        return count != null && count > 0;
    }

    @Override
    public long count() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM t_department", Long.class);
    }

    /**
     * 部门行映射器
     */
    private static class DepartmentRowMapper implements RowMapper<Department> {
        @Override
        public Department mapRow(ResultSet rs, int rowNum) throws SQLException {
            Department department = new Department();
            department.setId(rs.getString("id"));
            department.setName(rs.getString("name"));
            department.setCode(rs.getString("code"));
            department.setDescription(rs.getString("description"));
            department.setOrganizationId(rs.getString("organization_id"));
            department.setParentId(rs.getString("parent_id"));
            department.setManagerId(rs.getString("manager_id"));
            department.setStatus(rs.getString("status"));
            department.setSort(rs.getInt("sort"));
            department.setCreateTime(rs.getTimestamp("create_time").toLocalDateTime());
            department.setUpdateTime(rs.getTimestamp("update_time").toLocalDateTime());
            return department;
        }
    }
} 