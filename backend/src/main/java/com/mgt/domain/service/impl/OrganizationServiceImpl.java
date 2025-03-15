package com.mgt.domain.service.impl;

import com.mgt.domain.entity.Organization;
import com.mgt.domain.repository.OrganizationRepository;
import com.mgt.domain.service.OrganizationService;
import com.mgt.infrastructure.config.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;

    @Override
    @Transactional
    public Organization createOrganization(Organization organization) {
        // 检查编码是否存在
        if (isCodeExists(organization.getCode())) {
            throw new BusinessException("组织编码已存在");
        }
        
        // 设置基本信息
        organization.setId(UUID.randomUUID().toString());
        organization.setCreateTime(LocalDateTime.now());
        organization.setUpdateTime(LocalDateTime.now());
        
        // 如果有父组织,检查父组织是否存在
        if (organization.getParentId() != null) {
            organizationRepository.findById(organization.getParentId())
                .orElseThrow(() -> new BusinessException("父组织不存在"));
        }
        
        return organizationRepository.save(organization);
    }

    @Override
    @Transactional
    public Organization updateOrganization(String id, Organization organization) {
        // 检查组织是否存在
        Organization existingOrg = organizationRepository.findById(id)
            .orElseThrow(() -> new BusinessException("组织不存在"));
        
        // 如果修改了编码,检查新编码是否存在
        if (!existingOrg.getCode().equals(organization.getCode()) && isCodeExists(organization.getCode())) {
            throw new BusinessException("组织编码已存在");
        }
        
        // 如果修改了父组织,检查父组织是否存在
        if (organization.getParentId() != null && !organization.getParentId().equals(existingOrg.getParentId())) {
            organizationRepository.findById(organization.getParentId())
                .orElseThrow(() -> new BusinessException("父组织不存在"));
        }
        
        // 更新基本信息
        organization.setId(id);
        organization.setUpdateTime(LocalDateTime.now());
        organization.setCreateTime(existingOrg.getCreateTime());
        
        return organizationRepository.save(organization);
    }

    @Override
    @Transactional
    public void deleteOrganization(String id) {
        // 检查组织是否存在
        organizationRepository.findById(id)
            .orElseThrow(() -> new BusinessException("组织不存在"));
        
        // 检查是否存在子组织
        if (organizationRepository.hasChildren(id)) {
            throw new BusinessException("存在子组织,不能删除");
        }
        
        organizationRepository.deleteById(id);
    }

    @Override
    public Organization getOrganization(String id) {
        return organizationRepository.findById(id)
            .orElseThrow(() -> new BusinessException("组织不存在"));
    }

    @Override
    public List<Organization> getOrganizationList(Organization condition) {
        return organizationRepository.findByCondition(condition);
    }

    @Override
    public List<Organization> getOrganizationTree() {
        return organizationRepository.findTree();
    }

    @Override
    public boolean isCodeExists(String code) {
        return organizationRepository.findByCode(code).isPresent();
    }
} 