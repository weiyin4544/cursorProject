package com.mgt.domain.repository;

import com.mgt.domain.entity.NativeTerminal;

import java.util.List;
import java.util.Optional;

/**
 * 原生组与终端关联仓储接口
 */
public interface NativeTerminalRepository {
    /**
     * 保存原生组与终端关联
     *
     * @param nativeTerminal 原生组与终端关联
     * @return 保存后的原生组与终端关联
     */
    NativeTerminal save(NativeTerminal nativeTerminal);

    /**
     * 根据ID查询原生组与终端关联
     *
     * @param id 关联ID
     * @return 原生组与终端关联
     */
    Optional<NativeTerminal> findById(String id);

    /**
     * 根据原生组ID查询关联
     *
     * @param nativeGroupId 原生组ID
     * @return 原生组与终端关联列表
     */
    List<NativeTerminal> findByNativeGroupId(String nativeGroupId);

    /**
     * 根据终端ID查询关联
     *
     * @param terminalId 终端ID
     * @return 原生组与终端关联列表
     */
    List<NativeTerminal> findByTerminalId(String terminalId);

    /**
     * 根据原生组ID和终端ID查询关联
     *
     * @param nativeGroupId 原生组ID
     * @param terminalId    终端ID
     * @return 原生组与终端关联
     */
    Optional<NativeTerminal> findByNativeGroupIdAndTerminalId(String nativeGroupId, String terminalId);

    /**
     * 根据ID删除原生组与终端关联
     *
     * @param id 关联ID
     */
    void deleteById(String id);

    /**
     * 根据原生组ID删除关联
     *
     * @param nativeGroupId 原生组ID
     */
    void deleteByNativeGroupId(String nativeGroupId);

    /**
     * 根据终端ID删除关联
     *
     * @param terminalId 终端ID
     */
    void deleteByTerminalId(String terminalId);

    /**
     * 根据原生组ID和终端ID删除关联
     *
     * @param nativeGroupId 原生组ID
     * @param terminalId    终端ID
     */
    void deleteByNativeGroupIdAndTerminalId(String nativeGroupId, String terminalId);
} 