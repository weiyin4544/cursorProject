package com.mgt.interfaces.controller;

import com.mgt.domain.entity.Organization;
import com.mgt.domain.service.OrganizationService;
import com.mgt.infrastructure.config.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/organization")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;

    @PostMapping
    public Result<Organization> createOrganization(@RequestBody Organization organization) {
        return Result.success(organizationService.createOrganization(organization));
    }

    @PutMapping("/{id}")
    public Result<Organization> updateOrganization(@PathVariable String id, @RequestBody Organization organization) {
        return Result.success(organizationService.updateOrganization(id, organization));
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteOrganization(@PathVariable String id) {
        organizationService.deleteOrganization(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<Organization> getOrganization(@PathVariable String id) {
        return Result.success(organizationService.getOrganization(id));
    }

    @GetMapping("/list")
    public Result<List<Organization>> getOrganizationList(Organization condition) {
        return Result.success(organizationService.getOrganizationList(condition));
    }

    @GetMapping("/tree")
    public Result<List<Organization>> getOrganizationTree() {
        return Result.success(organizationService.getOrganizationTree());
    }
} 