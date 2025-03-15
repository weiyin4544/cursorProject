package com.mgt.domain.repository;

import com.mgt.domain.entity.PhoneNumber;

import java.util.List;
import java.util.Optional;

/**
 * 电话号码仓储接口
 */
public interface PhoneNumberRepository {
    /**
     * 保存电话号码
     *
     * @param phoneNumber 电话号码实体
     * @return 保存后的电话号码实体
     */
    PhoneNumber save(PhoneNumber phoneNumber);

    /**
     * 根据ID查询电话号码
     *
     * @param id 电话号码ID
     * @return 电话号码实体
     */
    Optional<PhoneNumber> findById(String id);

    /**
     * 根据员工ID查询电话号码列表
     *
     * @param employeeId 员工ID
     * @return 电话号码列表
     */
    List<PhoneNumber> findByEmployeeId(String employeeId);

    /**
     * 根据员工ID查询主要电话号码
     *
     * @param employeeId 员工ID
     * @return 主要电话号码
     */
    Optional<PhoneNumber> findPrimaryByEmployeeId(String employeeId);

    /**
     * 根据电话号码查询
     *
     * @param phoneNumber 电话号码
     * @return 电话号码实体列表
     */
    List<PhoneNumber> findByPhoneNumber(String phoneNumber);

    /**
     * 根据ID删除电话号码
     *
     * @param id 电话号码ID
     */
    void deleteById(String id);

    /**
     * 根据员工ID删除所有电话号码
     *
     * @param employeeId 员工ID
     */
    void deleteByEmployeeId(String employeeId);

    /**
     * 设置主要电话号码
     *
     * @param id 电话号码ID
     * @param employeeId 员工ID
     * @return 更新后的电话号码实体
     */
    PhoneNumber setPrimary(String id, String employeeId);
} 