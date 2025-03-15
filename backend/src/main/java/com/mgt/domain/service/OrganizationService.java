package com.mgt.domain.service;

import com.mgt.domain.entity.Organization;
import java.util.List;

public interface OrganizationService {
    // 创建组织
    Organization createOrganization(Organization organization);
    
    // 更新组织
    Organization updateOrganization(String id, Organization organization);
    
    // 删除组织
    void deleteOrganization(String id);
    
    // 获取组织详情
    Organization getOrganization(String id);
    
    // 获取组织列表
    List<Organization> getOrganizationList(Organization condition);
    
    // 获取组织树
    List<Organization> getOrganizationTree();
    
    // 检查组织编码是否存在
    boolean isCodeExists(String code);
} 