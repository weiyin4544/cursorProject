package com.mgt.interfaces.controller;

import com.mgt.domain.entity.Subsystem;
import com.mgt.domain.service.SubsystemService;
import com.mgt.infrastructure.common.PageResult;
import com.mgt.infrastructure.common.Result;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 子系统控制器
 */
@RestController
@RequestMapping("/api/subsystems")
public class SubsystemController {

    private final SubsystemService subsystemService;

    public SubsystemController(SubsystemService subsystemService) {
        this.subsystemService = subsystemService;
    }

    /**
     * 创建子系统
     *
     * @param subsystem 子系统信息
     * @return 创建后的子系统
     */
    @PostMapping
    public Result<Subsystem> createSubsystem(@RequestBody Subsystem subsystem) {
        return Result.success(subsystemService.createSubsystem(subsystem));
    }

    /**
     * 更新子系统
     *
     * @param id        子系统ID
     * @param subsystem 子系统信息
     * @return 更新后的子系统
     */
    @PutMapping("/{id}")
    public Result<Subsystem> updateSubsystem(@PathVariable String id, @RequestBody Subsystem subsystem) {
        return Result.success(subsystemService.updateSubsystem(id, subsystem));
    }

    /**
     * 根据ID查询子系统
     *
     * @param id 子系统ID
     * @return 子系统信息
     */
    @GetMapping("/{id}")
    public Result<Subsystem> getSubsystemById(@PathVariable String id) {
        return Result.success(subsystemService.getSubsystemById(id));
    }

    /**
     * 查询所有子系统
     *
     * @return 子系统列表
     */
    @GetMapping
    public Result<List<Subsystem>> getAllSubsystems() {
        return Result.success(subsystemService.getAllSubsystems());
    }

    /**
     * 根据类型查询子系统
     *
     * @param type 子系统类型
     * @return 子系统列表
     */
    @GetMapping("/type/{type}")
    public Result<List<Subsystem>> getSubsystemsByType(@PathVariable String type) {
        return Result.success(subsystemService.getSubsystemsByType(type));
    }

    /**
     * 根据状态查询子系统
     *
     * @param status 状态
     * @return 子系统列表
     */
    @GetMapping("/status/{status}")
    public Result<List<Subsystem>> getSubsystemsByStatus(@PathVariable String status) {
        return Result.success(subsystemService.getSubsystemsByStatus(status));
    }

    /**
     * 根据ID删除子系统
     *
     * @param id 子系统ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteSubsystem(@PathVariable String id) {
        subsystemService.deleteSubsystem(id);
        return Result.success();
    }

    /**
     * 更新子系统状态
     *
     * @param id     子系统ID
     * @param status 状态
     * @return 更新后的子系统
     */
    @PutMapping("/{id}/status")
    public Result<Subsystem> updateSubsystemStatus(@PathVariable String id, @RequestParam String status) {
        return Result.success(subsystemService.updateSubsystemStatus(id, status));
    }

    /**
     * 检查子系统编码是否存在
     *
     * @param code 子系统编码
     * @return 是否存在
     */
    @GetMapping("/check-code")
    public Result<Boolean> checkCodeExists(@RequestParam String code) {
        return Result.success(subsystemService.checkCodeExists(code));
    }

    /**
     * 检查子系统编码是否存在（排除指定ID）
     *
     * @param code     子系统编码
     * @param excludeId 排除的ID
     * @return 是否存在
     */
    @GetMapping("/check-code-exclude")
    public Result<Boolean> checkCodeExistsExcludeId(@RequestParam String code, @RequestParam String excludeId) {
        return Result.success(subsystemService.checkCodeExistsExcludeId(code, excludeId));
    }

    /**
     * 分页查询子系统
     *
     * @param page     页码
     * @param size     每页大小
     * @param name     子系统名称
     * @param type     子系统类型
     * @param status   状态
     * @return 子系统列表和总数
     */
    @GetMapping("/page")
    public Result<PageResult<Subsystem>> getSubsystemsByPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status) {
        return Result.success(subsystemService.getSubsystemsByPage(page, size, name, type, status));
    }
} 