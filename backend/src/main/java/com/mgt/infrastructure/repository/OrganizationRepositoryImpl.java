package com.mgt.infrastructure.repository;

import com.mgt.domain.entity.Organization;
import com.mgt.domain.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrganizationRepositoryImpl implements OrganizationRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Organization> rowMapper = (rs, rowNum) -> {
        Organization org = new Organization();
        org.setId(rs.getString("id"));
        org.setName(rs.getString("name"));
        org.setCode(rs.getString("code"));
        org.setDescription(rs.getString("description"));
        org.setParentId(rs.getString("parent_id"));
        org.setStatus(rs.getString("status"));
        org.setCreateTime(rs.getTimestamp("create_time").toLocalDateTime());
        org.setUpdateTime(rs.getTimestamp("update_time").toLocalDateTime());
        return org;
    };

    @Override
    public Organization save(Organization organization) {
        if (organization.getId() == null) {
            // 插入
            String sql = "INSERT INTO t_organization (id, name, code, description, parent_id, status, create_time, update_time) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            jdbcTemplate.update(sql,
                    organization.getId(),
                    organization.getName(),
                    organization.getCode(),
                    organization.getDescription(),
                    organization.getParentId(),
                    organization.getStatus(),
                    organization.getCreateTime(),
                    organization.getUpdateTime());
        } else {
            // 更新
            String sql = "UPDATE t_organization SET name = ?, code = ?, description = ?, parent_id = ?, " +
                    "status = ?, update_time = ? WHERE id = ?";
            jdbcTemplate.update(sql,
                    organization.getName(),
                    organization.getCode(),
                    organization.getDescription(),
                    organization.getParentId(),
                    organization.getStatus(),
                    organization.getUpdateTime(),
                    organization.getId());
        }
        return organization;
    }

    @Override
    public Optional<Organization> findById(String id) {
        String sql = "SELECT * FROM t_organization WHERE id = ?";
        List<Organization> results = jdbcTemplate.query(sql, rowMapper, id);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public Optional<Organization> findByCode(String code) {
        String sql = "SELECT * FROM t_organization WHERE code = ?";
        List<Organization> results = jdbcTemplate.query(sql, rowMapper, code);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public List<Organization> findAll() {
        String sql = "SELECT * FROM t_organization";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public List<Organization> findByCondition(Organization condition) {
        StringBuilder sql = new StringBuilder("SELECT * FROM t_organization WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (condition.getName() != null) {
            sql.append(" AND name LIKE ?");
            params.add("%" + condition.getName() + "%");
        }
        if (condition.getCode() != null) {
            sql.append(" AND code LIKE ?");
            params.add("%" + condition.getCode() + "%");
        }
        if (condition.getStatus() != null) {
            sql.append(" AND status = ?");
            params.add(condition.getStatus());
        }

        return jdbcTemplate.query(sql.toString(), rowMapper, params.toArray());
    }

    @Override
    public List<Organization> findTree() {
        // 获取所有组织
        List<Organization> allOrgs = findAll();
        
        // 构建父子关系
        Map<String, List<Organization>> parentMap = allOrgs.stream()
                .collect(Collectors.groupingBy(org -> org.getParentId() == null ? "" : org.getParentId()));
        
        // 从根节点开始构建树
        return buildTree("", parentMap);
    }

    private List<Organization> buildTree(String parentId, Map<String, List<Organization>> parentMap) {
        List<Organization> children = parentMap.get(parentId);
        if (children == null) {
            return new ArrayList<>();
        }
        
        for (Organization org : children) {
            org.setChildren(buildTree(org.getId(), parentMap));
        }
        
        return children;
    }

    @Override
    public void deleteById(String id) {
        String sql = "DELETE FROM t_organization WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean hasChildren(String id) {
        String sql = "SELECT COUNT(*) FROM t_organization WHERE parent_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM t_organization";
        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        return count != null ? count : 0;
    }
} 