package com.mgt.domain.service;

import com.mgt.domain.entity.PhoneNumber;

import java.util.List;
import java.util.Optional;

/**
 * 电话号码服务接口
 */
public interface PhoneNumberService {
    /**
     * 添加电话号码
     *
     * @param phoneNumber 电话号码实体
     * @return 添加后的电话号码实体
     */
    PhoneNumber addPhoneNumber(PhoneNumber phoneNumber);

    /**
     * 更新电话号码
     *
     * @param id 电话号码ID
     * @param phoneNumber 电话号码实体
     * @return 更新后的电话号码实体
     */
    PhoneNumber updatePhoneNumber(String id, PhoneNumber phoneNumber);

    /**
     * 根据ID查询电话号码
     *
     * @param id 电话号码ID
     * @return 电话号码实体
     */
    Optional<PhoneNumber> getPhoneNumberById(String id);

    /**
     * 根据员工ID查询电话号码列表
     *
     * @param employeeId 员工ID
     * @return 电话号码列表
     */
    List<PhoneNumber> getPhoneNumbersByEmployeeId(String employeeId);

    /**
     * 根据员工ID查询主要电话号码
     *
     * @param employeeId 员工ID
     * @return 主要电话号码
     */
    Optional<PhoneNumber> getPrimaryPhoneNumberByEmployeeId(String employeeId);

    /**
     * 根据电话号码查询
     *
     * @param phoneNumber 电话号码
     * @return 电话号码实体列表
     */
    List<PhoneNumber> getPhoneNumbersByNumber(String phoneNumber);

    /**
     * 根据ID删除电话号码
     *
     * @param id 电话号码ID
     */
    void deletePhoneNumber(String id);

    /**
     * 根据员工ID删除所有电话号码
     *
     * @param employeeId 员工ID
     */
    void deletePhoneNumbersByEmployeeId(String employeeId);

    /**
     * 设置主要电话号码
     *
     * @param id 电话号码ID
     * @param employeeId 员工ID
     * @return 更新后的电话号码实体
     */
    PhoneNumber setPrimaryPhoneNumber(String id, String employeeId);
} 