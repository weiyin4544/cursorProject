package com.mgt.domain.service.impl;

import com.mgt.domain.entity.PhoneNumber;
import com.mgt.domain.repository.PhoneNumberRepository;
import com.mgt.domain.service.PhoneNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

/**
 * 电话号码服务实现类
 */
@Service
public class PhoneNumberServiceImpl implements PhoneNumberService {

    private final PhoneNumberRepository phoneNumberRepository;

    @Autowired
    public PhoneNumberServiceImpl(PhoneNumberRepository phoneNumberRepository) {
        this.phoneNumberRepository = phoneNumberRepository;
    }

    @Override
    @Transactional
    public PhoneNumber addPhoneNumber(PhoneNumber phoneNumber) {
        // 生成ID
        phoneNumber.setId(UUID.randomUUID().toString());
        // 设置创建时间和更新时间
        LocalDateTime now = LocalDateTime.now();
        phoneNumber.setCreateTime(now);
        phoneNumber.setUpdateTime(now);
        // 如果是主要电话号码，则将该员工的其他电话号码设置为非主要
        if (Boolean.TRUE.equals(phoneNumber.getIsPrimary())) {
            List<PhoneNumber> existingPhoneNumbers = phoneNumberRepository.findByEmployeeId(phoneNumber.getEmployeeId());
            for (PhoneNumber existing : existingPhoneNumbers) {
                if (Boolean.TRUE.equals(existing.getIsPrimary())) {
                    existing.setIsPrimary(false);
                    phoneNumberRepository.save(existing);
                }
            }
        }
        return phoneNumberRepository.save(phoneNumber);
    }

    @Override
    @Transactional
    public PhoneNumber updatePhoneNumber(String id, PhoneNumber phoneNumber) {
        // 查询电话号码是否存在
        Optional<PhoneNumber> existingPhoneNumber = phoneNumberRepository.findById(id);
        if (existingPhoneNumber.isPresent()) {
            PhoneNumber updatedPhoneNumber = existingPhoneNumber.get();
            // 更新电话号码信息
            updatedPhoneNumber.setPhoneNumber(phoneNumber.getPhoneNumber());
            updatedPhoneNumber.setType(phoneNumber.getType());
            // 如果是主要电话号码，则将该员工的其他电话号码设置为非主要
            if (Boolean.TRUE.equals(phoneNumber.getIsPrimary()) && !Boolean.TRUE.equals(updatedPhoneNumber.getIsPrimary())) {
                List<PhoneNumber> existingPhoneNumbers = phoneNumberRepository.findByEmployeeId(updatedPhoneNumber.getEmployeeId());
                for (PhoneNumber existing : existingPhoneNumbers) {
                    if (!existing.getId().equals(id) && Boolean.TRUE.equals(existing.getIsPrimary())) {
                        existing.setIsPrimary(false);
                        phoneNumberRepository.save(existing);
                    }
                }
                updatedPhoneNumber.setIsPrimary(true);
            } else {
                updatedPhoneNumber.setIsPrimary(phoneNumber.getIsPrimary());
            }
            // 更新时间
            updatedPhoneNumber.setUpdateTime(LocalDateTime.now());
            return phoneNumberRepository.save(updatedPhoneNumber);
        }
        throw new NoSuchElementException("PhoneNumber not found with id: " + id);
    }

    @Override
    public Optional<PhoneNumber> getPhoneNumberById(String id) {
        return phoneNumberRepository.findById(id);
    }

    @Override
    public List<PhoneNumber> getPhoneNumbersByEmployeeId(String employeeId) {
        return phoneNumberRepository.findByEmployeeId(employeeId);
    }

    @Override
    public Optional<PhoneNumber> getPrimaryPhoneNumberByEmployeeId(String employeeId) {
        return phoneNumberRepository.findPrimaryByEmployeeId(employeeId);
    }

    @Override
    public List<PhoneNumber> getPhoneNumbersByNumber(String phoneNumber) {
        return phoneNumberRepository.findByPhoneNumber(phoneNumber);
    }

    @Override
    @Transactional
    public void deletePhoneNumber(String id) {
        phoneNumberRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deletePhoneNumbersByEmployeeId(String employeeId) {
        phoneNumberRepository.deleteByEmployeeId(employeeId);
    }

    @Override
    @Transactional
    public PhoneNumber setPrimaryPhoneNumber(String id, String employeeId) {
        // 将该员工的其他电话号码设置为非主要
        List<PhoneNumber> existingPhoneNumbers = phoneNumberRepository.findByEmployeeId(employeeId);
        for (PhoneNumber existing : existingPhoneNumbers) {
            if (!existing.getId().equals(id) && Boolean.TRUE.equals(existing.getIsPrimary())) {
                existing.setIsPrimary(false);
                phoneNumberRepository.save(existing);
            }
        }
        return phoneNumberRepository.setPrimary(id, employeeId);
    }
} 