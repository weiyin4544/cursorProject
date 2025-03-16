package com.mgt.interfaces.controller;

import com.mgt.domain.entity.Dispatcher;
import com.mgt.domain.service.DispatcherService;
import com.mgt.infrastructure.common.PageResult;
import com.mgt.infrastructure.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 调度员控制器
 */
@RestController
@RequestMapping("/api/dispatchers")
public class DispatcherController {

    @Autowired
    private DispatcherService dispatcherService;

    /**
     * 创建调度员
     *
     * @param dispatcher 调度员信息
     * @return 创建结果
     */
    @PostMapping
    public Result<Dispatcher> createDispatcher(@RequestBody Dispatcher dispatcher) {
        return Result.success(dispatcherService.createDispatcher(dispatcher));
    }

    /**
     * 更新调度员
     *
     * @param id         调度员ID
     * @param dispatcher 调度员信息
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public Result<Dispatcher> updateDispatcher(@PathVariable String id, @RequestBody Dispatcher dispatcher) {
        return Result.success(dispatcherService.updateDispatcher(id, dispatcher));
    }

    /**
     * 根据ID获取调度员
     *
     * @param id 调度员ID
     * @return 调度员信息
     */
    @GetMapping("/{id}")
    public Result<Dispatcher> getDispatcherById(@PathVariable String id) {
        return Result.success(dispatcherService.getDispatcherById(id));
    }

    /**
     * 获取所有调度员
     *
     * @return 调度员列表
     */
    @GetMapping
    public Result<List<Dispatcher>> getAllDispatchers() {
        return Result.success(dispatcherService.getAllDispatchers());
    }

    /**
     * 根据组织ID获取调度员列表
     *
     * @param organizationId 组织ID
     * @return 调度员列表
     */
    @GetMapping("/organization/{organizationId}")
    public Result<List<Dispatcher>> getDispatchersByOrganizationId(@PathVariable String organizationId) {
        return Result.success(dispatcherService.getDispatchersByOrganizationId(organizationId));
    }

    /**
     * 根据用户ID获取调度员
     *
     * @param userId 用户ID
     * @return 调度员信息
     */
    @GetMapping("/user/{userId}")
    public Result<Dispatcher> getDispatcherByUserId(@PathVariable String userId) {
        return Result.success(dispatcherService.getDispatcherByUserId(userId));
    }

    /**
     * 根据状态获取调度员列表
     *
     * @param status 状态
     * @return 调度员列表
     */
    @GetMapping("/status/{status}")
    public Result<List<Dispatcher>> getDispatchersByStatus(@PathVariable String status) {
        return Result.success(dispatcherService.getDispatchersByStatus(status));
    }

    /**
     * 删除调度员
     *
     * @param id 调度员ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteDispatcher(@PathVariable String id) {
        dispatcherService.deleteDispatcher(id);
        return Result.success();
    }

    /**
     * 更新调度员状态
     *
     * @param id     调度员ID
     * @param status 状态
     * @return 更新结果
     */
    @PutMapping("/{id}/status")
    public Result<Dispatcher> updateDispatcherStatus(@PathVariable String id, @RequestParam String status) {
        return Result.success(dispatcherService.updateDispatcherStatus(id, status));
    }

    /**
     * 检查编码是否存在
     *
     * @param code 编码
     * @return 检查结果
     */
    @GetMapping("/check-code")
    public Result<Boolean> checkCodeExists(@RequestParam String code) {
        return Result.success(dispatcherService.checkCodeExists(code));
    }

    /**
     * 检查编码是否存在（排除指定ID）
     *
     * @param code     编码
     * @param excludeId 排除的ID
     * @return 检查结果
     */
    @GetMapping("/check-code-exclude")
    public Result<Boolean> checkCodeExistsExcludeId(@RequestParam String code, @RequestParam String excludeId) {
        return Result.success(dispatcherService.checkCodeExistsExcludeId(code, excludeId));
    }

    /**
     * 分页查询调度员
     *
     * @param page           页码
     * @param size           每页大小
     * @param name           名称（模糊匹配）
     * @param organizationId 组织ID
     * @param status         状态
     * @return 分页结果
     */
    @GetMapping("/page")
    public Result<PageResult<Dispatcher>> getDispatchersByPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String organizationId,
            @RequestParam(required = false) String status) {
        Page<Dispatcher> dispatcherPage = dispatcherService.getDispatchersByPage(page, size, name, organizationId, status);
        return Result.success(new PageResult<>(
                dispatcherPage.getContent(),
                dispatcherPage.getTotalElements(),
                dispatcherPage.getTotalPages(),
                dispatcherPage.getNumber(),
                dispatcherPage.getSize()
        ));
    }

    /**
     * 为调度员分配子系统
     *
     * @param dispatcherId 调度员ID
     * @param subsystemIds 子系统ID列表
     * @return 分配结果
     */
    @PostMapping("/{dispatcherId}/subsystems")
    public Result<Void> assignSubsystems(@PathVariable String dispatcherId, @RequestBody List<String> subsystemIds) {
        dispatcherService.assignSubsystems(dispatcherId, subsystemIds);
        return Result.success();
    }

    /**
     * 移除调度员的子系统分配
     *
     * @param dispatcherId 调度员ID
     * @param subsystemId  子系统ID
     * @return 移除结果
     */
    @DeleteMapping("/{dispatcherId}/subsystems/{subsystemId}")
    public Result<Void> removeSubsystem(@PathVariable String dispatcherId, @PathVariable String subsystemId) {
        dispatcherService.removeSubsystem(dispatcherId, subsystemId);
        return Result.success();
    }

    /**
     * 获取调度员已分配的子系统ID列表
     *
     * @param dispatcherId 调度员ID
     * @return 子系统ID列表
     */
    @GetMapping("/{dispatcherId}/subsystems")
    public Result<List<String>> getAssignedSubsystemIds(@PathVariable String dispatcherId) {
        return Result.success(dispatcherService.getAssignedSubsystemIds(dispatcherId));
    }

    /**
     * 检查调度员是否已分配指定子系统
     *
     * @param dispatcherId 调度员ID
     * @param subsystemId  子系统ID
     * @return 是否已分配
     */
    @GetMapping("/{dispatcherId}/subsystems/{subsystemId}/check")
    public Result<Boolean> isSubsystemAssigned(@PathVariable String dispatcherId, @PathVariable String subsystemId) {
        return Result.success(dispatcherService.isSubsystemAssigned(dispatcherId, subsystemId));
    }
} 