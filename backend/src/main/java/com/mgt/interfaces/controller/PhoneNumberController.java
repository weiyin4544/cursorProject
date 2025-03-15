package com.mgt.interfaces.controller;

import com.mgt.domain.entity.PhoneNumber;
import com.mgt.domain.service.PhoneNumberService;
import com.mgt.interfaces.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * 电话号码控制器
 */
@RestController
@RequestMapping("/api/phone-numbers")
public class PhoneNumberController {

    private final PhoneNumberService phoneNumberService;

    @Autowired
    public PhoneNumberController(PhoneNumberService phoneNumberService) {
        this.phoneNumberService = phoneNumberService;
    }

    /**
     * 添加电话号码
     *
     * @param phoneNumber 电话号码实体
     * @return 添加结果
     */
    @PostMapping
    public Result<PhoneNumber> addPhoneNumber(@RequestBody PhoneNumber phoneNumber) {
        try {
            PhoneNumber addedPhoneNumber = phoneNumberService.addPhoneNumber(phoneNumber);
            return Result.success(addedPhoneNumber);
        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }
    }

    /**
     * 更新电话号码
     *
     * @param id 电话号码ID
     * @param phoneNumber 电话号码实体
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public Result<PhoneNumber> updatePhoneNumber(@PathVariable String id, @RequestBody PhoneNumber phoneNumber) {
        try {
            PhoneNumber updatedPhoneNumber = phoneNumberService.updatePhoneNumber(id, phoneNumber);
            return Result.success(updatedPhoneNumber);
        } catch (NoSuchElementException e) {
            return Result.failure(e.getMessage());
        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }
    }

    /**
     * 根据ID查询电话号码
     *
     * @param id 电话号码ID
     * @return 查询结果
     */
    @GetMapping("/{id}")
    public Result<PhoneNumber> getPhoneNumberById(@PathVariable String id) {
        try {
            Optional<PhoneNumber> phoneNumber = phoneNumberService.getPhoneNumberById(id);
            return phoneNumber.map(Result::success)
                    .orElseGet(() -> Result.failure("PhoneNumber not found with id: " + id));
        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }
    }

    /**
     * 根据员工ID查询电话号码列表
     *
     * @param employeeId 员工ID
     * @return 查询结果
     */
    @GetMapping("/employee/{employeeId}")
    public Result<List<PhoneNumber>> getPhoneNumbersByEmployeeId(@PathVariable String employeeId) {
        try {
            List<PhoneNumber> phoneNumbers = phoneNumberService.getPhoneNumbersByEmployeeId(employeeId);
            return Result.success(phoneNumbers);
        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }
    }

    /**
     * 根据员工ID查询主要电话号码
     *
     * @param employeeId 员工ID
     * @return 查询结果
     */
    @GetMapping("/employee/{employeeId}/primary")
    public Result<PhoneNumber> getPrimaryPhoneNumberByEmployeeId(@PathVariable String employeeId) {
        try {
            Optional<PhoneNumber> phoneNumber = phoneNumberService.getPrimaryPhoneNumberByEmployeeId(employeeId);
            return phoneNumber.map(Result::success)
                    .orElseGet(() -> Result.failure("Primary phone number not found for employee: " + employeeId));
        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }
    }

    /**
     * 根据电话号码查询
     *
     * @param phoneNumber 电话号码
     * @return 查询结果
     */
    @GetMapping("/search")
    public Result<List<PhoneNumber>> getPhoneNumbersByNumber(@RequestParam String phoneNumber) {
        try {
            List<PhoneNumber> phoneNumbers = phoneNumberService.getPhoneNumbersByNumber(phoneNumber);
            return Result.success(phoneNumbers);
        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }
    }

    /**
     * 根据ID删除电话号码
     *
     * @param id 电话号码ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> deletePhoneNumber(@PathVariable String id) {
        try {
            phoneNumberService.deletePhoneNumber(id);
            return Result.success();
        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }
    }

    /**
     * 根据员工ID删除所有电话号码
     *
     * @param employeeId 员工ID
     * @return 删除结果
     */
    @DeleteMapping("/employee/{employeeId}")
    public Result<Void> deletePhoneNumbersByEmployeeId(@PathVariable String employeeId) {
        try {
            phoneNumberService.deletePhoneNumbersByEmployeeId(employeeId);
            return Result.success();
        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }
    }

    /**
     * 设置主要电话号码
     *
     * @param id 电话号码ID
     * @param employeeId 员工ID
     * @return 设置结果
     */
    @PutMapping("/{id}/primary")
    public Result<PhoneNumber> setPrimaryPhoneNumber(@PathVariable String id, @RequestParam String employeeId) {
        try {
            PhoneNumber phoneNumber = phoneNumberService.setPrimaryPhoneNumber(id, employeeId);
            return Result.success(phoneNumber);
        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }
    }
} 