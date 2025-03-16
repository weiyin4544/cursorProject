package com.mgt.interfaces.controller;

import com.mgt.domain.entity.Dispatcher;
import com.mgt.domain.entity.Group;
import com.mgt.domain.entity.LocalDispatcher;
import com.mgt.domain.entity.NativeTerminal;
import com.mgt.domain.entity.PatchNativeGroup;
import com.mgt.domain.entity.Terminal;
import com.mgt.domain.service.GroupService;
import com.mgt.infrastructure.common.PageResult;
import com.mgt.infrastructure.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 群组控制器
 */
@RestController
@RequestMapping("/api/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

    /**
     * 创建群组
     *
     * @param group 群组信息
     * @return 创建结果
     */
    @PostMapping
    public Result<Group> createGroup(@RequestBody Group group) {
        return Result.success(groupService.createGroup(group));
    }

    /**
     * 更新群组
     *
     * @param id    群组ID
     * @param group 群组信息
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public Result<Group> updateGroup(@PathVariable String id, @RequestBody Group group) {
        return Result.success(groupService.updateGroup(id, group));
    }

    /**
     * 根据ID获取群组
     *
     * @param id 群组ID
     * @return 群组信息
     */
    @GetMapping("/{id}")
    public Result<Group> getGroupById(@PathVariable String id) {
        return Result.success(groupService.getGroupById(id));
    }

    /**
     * 获取所有群组
     *
     * @return 群组列表
     */
    @GetMapping
    public Result<List<Group>> getAllGroups() {
        return Result.success(groupService.getAllGroups());
    }

    /**
     * 根据组织ID获取群组列表
     *
     * @param organizationId 组织ID
     * @return 群组列表
     */
    @GetMapping("/organization/{organizationId}")
    public Result<List<Group>> getGroupsByOrganizationId(@PathVariable String organizationId) {
        return Result.success(groupService.getGroupsByOrganizationId(organizationId));
    }

    /**
     * 根据类型获取群组列表
     *
     * @param type 群组类型
     * @return 群组列表
     */
    @GetMapping("/type/{type}")
    public Result<List<Group>> getGroupsByType(@PathVariable String type) {
        return Result.success(groupService.getGroupsByType(type));
    }

    /**
     * 根据状态获取群组列表
     *
     * @param status 状态
     * @return 群组列表
     */
    @GetMapping("/status/{status}")
    public Result<List<Group>> getGroupsByStatus(@PathVariable String status) {
        return Result.success(groupService.getGroupsByStatus(status));
    }

    /**
     * 删除群组
     *
     * @param id 群组ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteGroup(@PathVariable String id) {
        groupService.deleteGroup(id);
        return Result.success();
    }

    /**
     * 更新群组状态
     *
     * @param id     群组ID
     * @param status 状态
     * @return 更新结果
     */
    @PutMapping("/{id}/status")
    public Result<Group> updateGroupStatus(@PathVariable String id, @RequestParam String status) {
        return Result.success(groupService.updateGroupStatus(id, status));
    }

    /**
     * 检查群组编码是否存在
     *
     * @param code 群组编码
     * @return 检查结果
     */
    @GetMapping("/check-code")
    public Result<Boolean> checkCodeExists(@RequestParam String code) {
        return Result.success(groupService.checkCodeExists(code));
    }

    /**
     * 检查群组编码是否存在（排除指定ID）
     *
     * @param code     群组编码
     * @param excludeId 排除的ID
     * @return 检查结果
     */
    @GetMapping("/check-code-exclude")
    public Result<Boolean> checkCodeExistsExcludeId(@RequestParam String code, @RequestParam String excludeId) {
        return Result.success(groupService.checkCodeExistsExcludeId(code, excludeId));
    }

    /**
     * 分页查询群组
     *
     * @param page           页码
     * @param size           每页大小
     * @param name           群组名称
     * @param organizationId 组织ID
     * @param type           群组类型
     * @param status         状态
     * @return 分页结果
     */
    @GetMapping("/page")
    public Result<PageResult<Group>> getGroupsByPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String organizationId,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status) {
        return Result.success(groupService.getGroupsByPage(page, size, name, organizationId, type, status));
    }

    /**
     * 添加原生组到派接组
     *
     * @param patchGroupId  派接组ID
     * @param nativeGroupId 原生组ID
     * @return 关联信息
     */
    @PostMapping("/patch/{patchGroupId}/native/{nativeGroupId}")
    public Result<PatchNativeGroup> addNativeGroupToPatchGroup(
            @PathVariable String patchGroupId,
            @PathVariable String nativeGroupId) {
        return Result.success(groupService.addNativeGroupToPatchGroup(patchGroupId, nativeGroupId));
    }

    /**
     * 从派接组移除原生组
     *
     * @param patchGroupId  派接组ID
     * @param nativeGroupId 原生组ID
     * @return 移除结果
     */
    @DeleteMapping("/patch/{patchGroupId}/native/{nativeGroupId}")
    public Result<Void> removeNativeGroupFromPatchGroup(
            @PathVariable String patchGroupId,
            @PathVariable String nativeGroupId) {
        groupService.removeNativeGroupFromPatchGroup(patchGroupId, nativeGroupId);
        return Result.success();
    }

    /**
     * 获取派接组下的原生组列表
     *
     * @param patchGroupId 派接组ID
     * @return 原生组列表
     */
    @GetMapping("/patch/{patchGroupId}/natives")
    public Result<List<Group>> getNativeGroupsByPatchGroupId(@PathVariable String patchGroupId) {
        return Result.success(groupService.getNativeGroupsByPatchGroupId(patchGroupId));
    }

    /**
     * 获取原生组所属的派接组列表
     *
     * @param nativeGroupId 原生组ID
     * @return 派接组列表
     */
    @GetMapping("/native/{nativeGroupId}/patches")
    public Result<List<Group>> getPatchGroupsByNativeGroupId(@PathVariable String nativeGroupId) {
        return Result.success(groupService.getPatchGroupsByNativeGroupId(nativeGroupId));
    }

    /**
     * 添加终端到原生组
     *
     * @param nativeGroupId 原生组ID
     * @param terminalId    终端ID
     * @return 关联信息
     */
    @PostMapping("/native/{nativeGroupId}/terminal/{terminalId}")
    public Result<NativeTerminal> addTerminalToNativeGroup(
            @PathVariable String nativeGroupId,
            @PathVariable String terminalId) {
        return Result.success(groupService.addTerminalToNativeGroup(nativeGroupId, terminalId));
    }

    /**
     * 从原生组移除终端
     *
     * @param nativeGroupId 原生组ID
     * @param terminalId    终端ID
     * @return 移除结果
     */
    @DeleteMapping("/native/{nativeGroupId}/terminal/{terminalId}")
    public Result<Void> removeTerminalFromNativeGroup(
            @PathVariable String nativeGroupId,
            @PathVariable String terminalId) {
        groupService.removeTerminalFromNativeGroup(nativeGroupId, terminalId);
        return Result.success();
    }

    /**
     * 获取原生组下的终端列表
     *
     * @param nativeGroupId 原生组ID
     * @return 终端列表
     */
    @GetMapping("/native/{nativeGroupId}/terminals")
    public Result<List<Terminal>> getTerminalsByNativeGroupId(@PathVariable String nativeGroupId) {
        return Result.success(groupService.getTerminalsByNativeGroupId(nativeGroupId));
    }

    /**
     * 获取终端所属的原生组列表
     *
     * @param terminalId 终端ID
     * @return 原生组列表
     */
    @GetMapping("/terminal/{terminalId}/natives")
    public Result<List<Group>> getNativeGroupsByTerminalId(@PathVariable String terminalId) {
        return Result.success(groupService.getNativeGroupsByTerminalId(terminalId));
    }

    /**
     * 添加调度员到本地组
     *
     * @param localGroupId 本地组ID
     * @param dispatcherId 调度员ID
     * @return 关联信息
     */
    @PostMapping("/local/{localGroupId}/dispatcher/{dispatcherId}")
    public Result<LocalDispatcher> addDispatcherToLocalGroup(
            @PathVariable String localGroupId,
            @PathVariable String dispatcherId) {
        return Result.success(groupService.addDispatcherToLocalGroup(localGroupId, dispatcherId));
    }

    /**
     * 从本地组移除调度员
     *
     * @param localGroupId 本地组ID
     * @param dispatcherId 调度员ID
     * @return 移除结果
     */
    @DeleteMapping("/local/{localGroupId}/dispatcher/{dispatcherId}")
    public Result<Void> removeDispatcherFromLocalGroup(
            @PathVariable String localGroupId,
            @PathVariable String dispatcherId) {
        groupService.removeDispatcherFromLocalGroup(localGroupId, dispatcherId);
        return Result.success();
    }

    /**
     * 获取本地组下的调度员列表
     *
     * @param localGroupId 本地组ID
     * @return 调度员列表
     */
    @GetMapping("/local/{localGroupId}/dispatchers")
    public Result<List<Dispatcher>> getDispatchersByLocalGroupId(@PathVariable String localGroupId) {
        return Result.success(groupService.getDispatchersByLocalGroupId(localGroupId));
    }

    /**
     * 获取调度员所属的本地组列表
     *
     * @param dispatcherId 调度员ID
     * @return 本地组列表
     */
    @GetMapping("/dispatcher/{dispatcherId}/locals")
    public Result<List<Group>> getLocalGroupsByDispatcherId(@PathVariable String dispatcherId) {
        return Result.success(groupService.getLocalGroupsByDispatcherId(dispatcherId));
    }

    /**
     * 批量添加终端到原生组
     *
     * @param nativeGroupId 原生组ID
     * @param terminalIds   终端ID列表
     * @return 添加结果
     */
    @PostMapping("/native/{nativeGroupId}/terminals")
    public Result<Void> batchAddTerminalsToNativeGroup(
            @PathVariable String nativeGroupId,
            @RequestBody List<String> terminalIds) {
        for (String terminalId : terminalIds) {
            groupService.addTerminalToNativeGroup(nativeGroupId, terminalId);
        }
        return Result.success();
    }

    /**
     * 批量添加原生组到派接组
     *
     * @param patchGroupId   派接组ID
     * @param nativeGroupIds 原生组ID列表
     * @return 添加结果
     */
    @PostMapping("/patch/{patchGroupId}/natives")
    public Result<Void> batchAddNativeGroupsToPatchGroup(
            @PathVariable String patchGroupId,
            @RequestBody List<String> nativeGroupIds) {
        for (String nativeGroupId : nativeGroupIds) {
            groupService.addNativeGroupToPatchGroup(patchGroupId, nativeGroupId);
        }
        return Result.success();
    }

    /**
     * 批量添加调度员到本地组
     *
     * @param localGroupId  本地组ID
     * @param dispatcherIds 调度员ID列表
     * @return 添加结果
     */
    @PostMapping("/local/{localGroupId}/dispatchers")
    public Result<Void> batchAddDispatchersToLocalGroup(
            @PathVariable String localGroupId,
            @RequestBody List<String> dispatcherIds) {
        for (String dispatcherId : dispatcherIds) {
            groupService.addDispatcherToLocalGroup(localGroupId, dispatcherId);
        }
        return Result.success();
    }
} 