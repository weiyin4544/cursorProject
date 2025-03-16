package com.mgt.domain.repository;

import com.mgt.domain.entity.PatchNativeGroup;

import java.util.List;
import java.util.Optional;

/**
 * 派接组与原生组关联仓储接口
 */
public interface PatchNativeGroupRepository {
    /**
     * 保存派接组与原生组关联
     *
     * @param patchNativeGroup 派接组与原生组关联
     * @return 保存后的派接组与原生组关联
     */
    PatchNativeGroup save(PatchNativeGroup patchNativeGroup);

    /**
     * 根据ID查询派接组与原生组关联
     *
     * @param id 关联ID
     * @return 派接组与原生组关联
     */
    Optional<PatchNativeGroup> findById(String id);

    /**
     * 根据派接组ID查询关联
     *
     * @param patchGroupId 派接组ID
     * @return 派接组与原生组关联列表
     */
    List<PatchNativeGroup> findByPatchGroupId(String patchGroupId);

    /**
     * 根据原生组ID查询关联
     *
     * @param nativeGroupId 原生组ID
     * @return 派接组与原生组关联列表
     */
    List<PatchNativeGroup> findByNativeGroupId(String nativeGroupId);

    /**
     * 根据派接组ID和原生组ID查询关联
     *
     * @param patchGroupId  派接组ID
     * @param nativeGroupId 原生组ID
     * @return 派接组与原生组关联
     */
    Optional<PatchNativeGroup> findByPatchGroupIdAndNativeGroupId(String patchGroupId, String nativeGroupId);

    /**
     * 根据ID删除派接组与原生组关联
     *
     * @param id 关联ID
     */
    void deleteById(String id);

    /**
     * 根据派接组ID删除关联
     *
     * @param patchGroupId 派接组ID
     */
    void deleteByPatchGroupId(String patchGroupId);

    /**
     * 根据原生组ID删除关联
     *
     * @param nativeGroupId 原生组ID
     */
    void deleteByNativeGroupId(String nativeGroupId);

    /**
     * 根据派接组ID和原生组ID删除关联
     *
     * @param patchGroupId  派接组ID
     * @param nativeGroupId 原生组ID
     */
    void deleteByPatchGroupIdAndNativeGroupId(String patchGroupId, String nativeGroupId);
} 