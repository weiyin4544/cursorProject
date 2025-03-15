package com.mgt.interfaces.controller;

import com.mgt.domain.entity.Terminal;
import com.mgt.domain.service.TerminalService;
import com.mgt.infrastructure.common.PageResult;
import com.mgt.infrastructure.common.Result;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 终端控制器
 */
@RestController
@RequestMapping("/api/terminals")
public class TerminalController {

    private final TerminalService terminalService;

    public TerminalController(TerminalService terminalService) {
        this.terminalService = terminalService;
    }

    /**
     * 创建终端
     *
     * @param terminal 终端信息
     * @return 创建后的终端
     */
    @PostMapping
    public Result<Terminal> createTerminal(@RequestBody Terminal terminal) {
        return Result.success(terminalService.createTerminal(terminal));
    }

    /**
     * 更新终端
     *
     * @param id       终端ID
     * @param terminal 终端信息
     * @return 更新后的终端
     */
    @PutMapping("/{id}")
    public Result<Terminal> updateTerminal(@PathVariable String id, @RequestBody Terminal terminal) {
        return Result.success(terminalService.updateTerminal(id, terminal));
    }

    /**
     * 根据ID查询终端
     *
     * @param id 终端ID
     * @return 终端信息
     */
    @GetMapping("/{id}")
    public Result<Terminal> getTerminalById(@PathVariable String id) {
        return Result.success(terminalService.getTerminalById(id));
    }

    /**
     * 查询所有终端
     *
     * @return 终端列表
     */
    @GetMapping
    public Result<List<Terminal>> getAllTerminals() {
        return Result.success(terminalService.getAllTerminals());
    }

    /**
     * 根据组织ID查询终端
     *
     * @param organizationId 组织ID
     * @return 终端列表
     */
    @GetMapping("/organization/{organizationId}")
    public Result<List<Terminal>> getTerminalsByOrganizationId(@PathVariable String organizationId) {
        return Result.success(terminalService.getTerminalsByOrganizationId(organizationId));
    }

    /**
     * 根据子系统ID查询终端
     *
     * @param subsystemId 子系统ID
     * @return 终端列表
     */
    @GetMapping("/subsystem/{subsystemId}")
    public Result<List<Terminal>> getTerminalsBySubsystemId(@PathVariable String subsystemId) {
        return Result.success(terminalService.getTerminalsBySubsystemId(subsystemId));
    }

    /**
     * 根据人员ID查询终端
     *
     * @param personId 人员ID
     * @return 终端列表
     */
    @GetMapping("/person/{personId}")
    public Result<List<Terminal>> getTerminalsByPersonId(@PathVariable String personId) {
        return Result.success(terminalService.getTerminalsByPersonId(personId));
    }

    /**
     * 根据状态查询终端
     *
     * @param status 状态
     * @return 终端列表
     */
    @GetMapping("/status/{status}")
    public Result<List<Terminal>> getTerminalsByStatus(@PathVariable String status) {
        return Result.success(terminalService.getTerminalsByStatus(status));
    }

    /**
     * 根据ID删除终端
     *
     * @param id 终端ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteTerminal(@PathVariable String id) {
        terminalService.deleteTerminal(id);
        return Result.success();
    }

    /**
     * 更新终端状态
     *
     * @param id     终端ID
     * @param status 状态
     * @return 更新后的终端
     */
    @PutMapping("/{id}/status")
    public Result<Terminal> updateTerminalStatus(@PathVariable String id, @RequestParam String status) {
        return Result.success(terminalService.updateTerminalStatus(id, status));
    }

    /**
     * 分配终端给人员
     *
     * @param id       终端ID
     * @param personId 人员ID
     * @return 更新后的终端
     */
    @PutMapping("/{id}/assign")
    public Result<Terminal> assignTerminalToPerson(@PathVariable String id, @RequestParam String personId) {
        return Result.success(terminalService.assignTerminalToPerson(id, personId));
    }

    /**
     * 取消分配终端
     *
     * @param id 终端ID
     * @return 更新后的终端
     */
    @PutMapping("/{id}/unassign")
    public Result<Terminal> unassignTerminal(@PathVariable String id) {
        return Result.success(terminalService.unassignTerminal(id));
    }

    /**
     * 检查终端编码是否存在
     *
     * @param code 终端编码
     * @return 是否存在
     */
    @GetMapping("/check-code")
    public Result<Boolean> checkCodeExists(@RequestParam String code) {
        return Result.success(terminalService.checkCodeExists(code));
    }

    /**
     * 检查终端编码是否存在（排除指定ID）
     *
     * @param code     终端编码
     * @param excludeId 排除的ID
     * @return 是否存在
     */
    @GetMapping("/check-code-exclude")
    public Result<Boolean> checkCodeExistsExcludeId(@RequestParam String code, @RequestParam String excludeId) {
        return Result.success(terminalService.checkCodeExistsExcludeId(code, excludeId));
    }

    /**
     * 分页查询终端
     *
     * @param page           页码
     * @param size           每页大小
     * @param name           终端名称
     * @param organizationId 组织ID
     * @param subsystemId    子系统ID
     * @param personId       人员ID
     * @param status         状态
     * @return 终端列表和总数
     */
    @GetMapping("/page")
    public Result<PageResult<Terminal>> getTerminalsByPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String organizationId,
            @RequestParam(required = false) String subsystemId,
            @RequestParam(required = false) String personId,
            @RequestParam(required = false) String status) {
        return Result.success(terminalService.getTerminalsByPage(page, size, name, organizationId, subsystemId, personId, status));
    }
} 