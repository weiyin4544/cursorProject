package com.mgt.domain.service;

import com.mgt.domain.entity.Terminal;
import com.mgt.infrastructure.common.PageResult;

import java.util.List;

/**
 * 终端服务接口
 */
public interface TerminalService {
    /**
     * 创建终端
     *
     * @param terminal 终端信息
     * @return 创建后的终端
     */
    Terminal createTerminal(Terminal terminal);

    /**
     * 更新终端
     *
     * @param id       终端ID
     * @param terminal 终端信息
     * @return 更新后的终端
     */
    Terminal updateTerminal(String id, Terminal terminal);

    /**
     * 根据ID查询终端
     *
     * @param id 终端ID
     * @return 终端信息
     */
    Terminal getTerminalById(String id);

    /**
     * 查询所有终端
     *
     * @return 终端列表
     */
    List<Terminal> getAllTerminals();

    /**
     * 根据组织ID查询终端
     *
     * @param organizationId 组织ID
     * @return 终端列表
     */
    List<Terminal> getTerminalsByOrganizationId(String organizationId);

    /**
     * 根据子系统ID查询终端
     *
     * @param subsystemId 子系统ID
     * @return 终端列表
     */
    List<Terminal> getTerminalsBySubsystemId(String subsystemId);

    /**
     * 根据人员ID查询终端
     *
     * @param personId 人员ID
     * @return 终端列表
     */
    List<Terminal> getTerminalsByPersonId(String personId);

    /**
     * 根据状态查询终端
     *
     * @param status 状态
     * @return 终端列表
     */
    List<Terminal> getTerminalsByStatus(String status);

    /**
     * 根据ID删除终端
     *
     * @param id 终端ID
     */
    void deleteTerminal(String id);

    /**
     * 更新终端状态
     *
     * @param id     终端ID
     * @param status 状态
     * @return 更新后的终端
     */
    Terminal updateTerminalStatus(String id, String status);

    /**
     * 分配终端给人员
     *
     * @param id       终端ID
     * @param personId 人员ID
     * @return 更新后的终端
     */
    Terminal assignTerminalToPerson(String id, String personId);

    /**
     * 取消分配终端
     *
     * @param id 终端ID
     * @return 更新后的终端
     */
    Terminal unassignTerminal(String id);

    /**
     * 检查终端编码是否存在
     *
     * @param code 终端编码
     * @return 是否存在
     */
    boolean checkCodeExists(String code);

    /**
     * 检查终端编码是否存在（排除指定ID）
     *
     * @param code     终端编码
     * @param excludeId 排除的ID
     * @return 是否存在
     */
    boolean checkCodeExistsExcludeId(String code, String excludeId);

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
    PageResult<Terminal> getTerminalsByPage(int page, int size, String name, String organizationId, 
                                           String subsystemId, String personId, String status);
} 