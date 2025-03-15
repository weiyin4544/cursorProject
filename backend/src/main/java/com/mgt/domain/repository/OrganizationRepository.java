package com.mgt.domain.repository;

import com.mgt.domain.entity.Organization;
import java.util.List;
import java.util.Optional;

public interface OrganizationRepository {
    // 保存组织
    Organization save(Organization organization);
    
    // 根据ID查询组织
    Optional<Organization> findById(String id);
    
    // 根据编码查询组织
    Optional<Organization> findByCode(String code);
    
    // 查询组织列表
    List<Organization> findAll();
    
    // 根据条件查询组织列表
    List<Organization> findByCondition(Organization condition);
    
    // 查询组织树
    List<Organization> findTree();
    
    // 删除组织
    void deleteById(String id);
    
    // 检查是否存在子组织
    boolean hasChildren(String id);
    
    // 统计组织数量
    long count();
} 