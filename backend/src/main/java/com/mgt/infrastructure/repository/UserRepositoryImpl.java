package com.mgt.infrastructure.repository;

import com.mgt.domain.entity.User;
import com.mgt.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<User> rowMapper = (rs, rowNum) -> {
        User user = new User();
        user.setId(rs.getString("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setNickname(rs.getString("nickname"));
        user.setEmail(rs.getString("email"));
        user.setPhone(rs.getString("phone"));
        user.setAvatar(rs.getString("avatar"));
        user.setOrganizationId(rs.getString("organization_id"));
        user.setDepartmentId(rs.getString("department_id"));
        user.setStatus(rs.getString("status"));
        
        Timestamp createTime = rs.getTimestamp("create_time");
        if (createTime != null) {
            user.setCreateTime(createTime.toLocalDateTime());
        }
        
        Timestamp updateTime = rs.getTimestamp("update_time");
        if (updateTime != null) {
            user.setUpdateTime(updateTime.toLocalDateTime());
        }
        
        Timestamp lastLoginTime = rs.getTimestamp("last_login_time");
        if (lastLoginTime != null) {
            user.setLastLoginTime(lastLoginTime.toLocalDateTime());
        }
        
        return user;
    };

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            // 插入
            String sql = "INSERT INTO t_user (id, username, password, nickname, email, phone, avatar, " +
                    "organization_id, department_id, status, create_time, update_time, last_login_time) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            jdbcTemplate.update(sql,
                    user.getId(),
                    user.getUsername(),
                    user.getPassword(),
                    user.getNickname(),
                    user.getEmail(),
                    user.getPhone(),
                    user.getAvatar(),
                    user.getOrganizationId(),
                    user.getDepartmentId(),
                    user.getStatus(),
                    Timestamp.valueOf(user.getCreateTime()),
                    Timestamp.valueOf(user.getUpdateTime()),
                    user.getLastLoginTime() != null ? Timestamp.valueOf(user.getLastLoginTime()) : null);
        } else {
            // 更新
            String sql = "UPDATE t_user SET username = ?, password = ?, nickname = ?, email = ?, " +
                    "phone = ?, avatar = ?, organization_id = ?, department_id = ?, status = ?, " +
                    "update_time = ?, last_login_time = ? WHERE id = ?";
            jdbcTemplate.update(sql,
                    user.getUsername(),
                    user.getPassword(),
                    user.getNickname(),
                    user.getEmail(),
                    user.getPhone(),
                    user.getAvatar(),
                    user.getOrganizationId(),
                    user.getDepartmentId(),
                    user.getStatus(),
                    Timestamp.valueOf(user.getUpdateTime()),
                    user.getLastLoginTime() != null ? Timestamp.valueOf(user.getLastLoginTime()) : null,
                    user.getId());
        }
        return user;
    }

    @Override
    public Optional<User> findById(String id) {
        String sql = "SELECT * FROM t_user WHERE id = ?";
        List<User> results = jdbcTemplate.query(sql, rowMapper, id);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM t_user WHERE username = ?";
        List<User> results = jdbcTemplate.query(sql, rowMapper, username);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM t_user WHERE email = ?";
        List<User> results = jdbcTemplate.query(sql, rowMapper, email);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public Optional<User> findByPhone(String phone) {
        String sql = "SELECT * FROM t_user WHERE phone = ?";
        List<User> results = jdbcTemplate.query(sql, rowMapper, phone);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM t_user";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public List<User> findByCondition(User condition, int page, int size) {
        StringBuilder sql = new StringBuilder("SELECT u.*, o.name as organization_name, d.name as department_name FROM t_user u " +
                "LEFT JOIN t_organization o ON u.organization_id = o.id " +
                "LEFT JOIN t_department d ON u.department_id = d.id " +
                "WHERE 1=1");
        List<Object> params = new ArrayList<>();
        
        appendConditions(sql, params, condition);
        
        sql.append(" LIMIT ? OFFSET ?");
        params.add(size);
        params.add((page - 1) * size);
        
        return jdbcTemplate.query(sql.toString(), (rs, rowNum) -> {
            User user = rowMapper.mapRow(rs, rowNum);
            user.setOrganizationName(rs.getString("organization_name"));
            user.setDepartmentName(rs.getString("department_name"));
            return user;
        }, params.toArray());
    }

    @Override
    public long countByCondition(User condition) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM t_user WHERE 1=1");
        List<Object> params = new ArrayList<>();
        
        appendConditions(sql, params, condition);
        
        return jdbcTemplate.queryForObject(sql.toString(), Long.class, params.toArray());
    }

    private void appendConditions(StringBuilder sql, List<Object> params, User condition) {
        if (condition.getUsername() != null) {
            sql.append(" AND username LIKE ?");
            params.add("%" + condition.getUsername() + "%");
        }
        if (condition.getNickname() != null) {
            sql.append(" AND nickname LIKE ?");
            params.add("%" + condition.getNickname() + "%");
        }
        if (condition.getEmail() != null) {
            sql.append(" AND email LIKE ?");
            params.add("%" + condition.getEmail() + "%");
        }
        if (condition.getPhone() != null) {
            sql.append(" AND phone LIKE ?");
            params.add("%" + condition.getPhone() + "%");
        }
        if (condition.getOrganizationId() != null) {
            sql.append(" AND organization_id = ?");
            params.add(condition.getOrganizationId());
        }
        if (condition.getDepartmentId() != null) {
            sql.append(" AND department_id = ?");
            params.add(condition.getDepartmentId());
        }
        if (condition.getStatus() != null) {
            sql.append(" AND status = ?");
            params.add(condition.getStatus());
        }
    }

    @Override
    public void deleteById(String id) {
        String sql = "DELETE FROM t_user WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void updatePassword(String id, String newPassword) {
        String sql = "UPDATE t_user SET password = ?, update_time = ? WHERE id = ?";
        jdbcTemplate.update(sql, newPassword, Timestamp.valueOf(LocalDateTime.now()), id);
    }

    @Override
    public void updateStatus(String id, String status) {
        String sql = "UPDATE t_user SET status = ?, update_time = ? WHERE id = ?";
        jdbcTemplate.update(sql, status, Timestamp.valueOf(LocalDateTime.now()), id);
    }

    @Override
    public void updateLastLoginTime(String id) {
        String sql = "UPDATE t_user SET last_login_time = ? WHERE id = ?";
        jdbcTemplate.update(sql, Timestamp.valueOf(LocalDateTime.now()), id);
    }

    @Override
    public boolean existsByUsername(String username) {
        String sql = "SELECT COUNT(*) FROM t_user WHERE username = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, username);
        return count != null && count > 0;
    }

    @Override
    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM t_user WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    @Override
    public boolean existsByPhone(String phone) {
        String sql = "SELECT COUNT(*) FROM t_user WHERE phone = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, phone);
        return count != null && count > 0;
    }
} 