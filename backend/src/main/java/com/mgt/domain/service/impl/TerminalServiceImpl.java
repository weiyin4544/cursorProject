package com.mgt.domain.service.impl;

import com.mgt.domain.entity.Terminal;
import com.mgt.domain.repository.TerminalRepository;
import com.mgt.domain.service.TerminalService;
import com.mgt.infrastructure.common.PageResult;
import com.mgt.infrastructure.exception.BusinessException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * 终端服务实现
 */
@Service
public class TerminalServiceImpl implements TerminalService {

    private final TerminalRepository terminalRepository;

    public TerminalServiceImpl(TerminalRepository terminalRepository) {
        this.terminalRepository = terminalRepository;
    }

    @Override
    @Transactional
    public Terminal createTerminal(Terminal terminal) {
        // 检查编码是否已存在
        if (checkCodeExists(terminal.getCode())) {
            throw new BusinessException("终端编码已存在");
        }

        // 设置默认值
        terminal.setId(UUID.randomUUID().toString());
        if (!StringUtils.hasText(terminal.getStatus())) {
            terminal.setStatus("OFFLINE");
        }
        LocalDateTime now = LocalDateTime.now();
        terminal.setCreateTime(now);
        terminal.setUpdateTime(now);

        return terminalRepository.save(terminal);
    }

    @Override
    @Transactional
    public Terminal updateTerminal(String id, Terminal terminal) {
        // 检查终端是否存在
        Terminal existingTerminal = getTerminalById(id);

        // 检查编码是否已存在（排除当前ID）
        if (!existingTerminal.getCode().equals(terminal.getCode()) && checkCodeExists(terminal.getCode())) {
            throw new BusinessException("终端编码已存在");
        }

        // 更新属性
        BeanUtils.copyProperties(terminal, existingTerminal, "id", "createTime");
        existingTerminal.setUpdateTime(LocalDateTime.now());

        return terminalRepository.save(existingTerminal);
    }

    @Override
    public Terminal getTerminalById(String id) {
        return terminalRepository.findById(id)
                .orElseThrow(() -> new BusinessException("终端不存在"));
    }

    @Override
    public List<Terminal> getAllTerminals() {
        return terminalRepository.findAll();
    }

    @Override
    public List<Terminal> getTerminalsByOrganizationId(String organizationId) {
        return terminalRepository.findByOrganizationId(organizationId);
    }

    @Override
    public List<Terminal> getTerminalsBySubsystemId(String subsystemId) {
        return terminalRepository.findBySubsystemId(subsystemId);
    }

    @Override
    public List<Terminal> getTerminalsByPersonId(String personId) {
        return terminalRepository.findByPersonId(personId);
    }

    @Override
    public List<Terminal> getTerminalsByStatus(String status) {
        return terminalRepository.findByStatus(status);
    }

    @Override
    @Transactional
    public void deleteTerminal(String id) {
        // 检查终端是否存在
        getTerminalById(id);
        
        // 删除终端
        terminalRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Terminal updateTerminalStatus(String id, String status) {
        // 检查终端是否存在
        Terminal terminal = getTerminalById(id);
        
        // 更新状态
        terminal.setStatus(status);
        terminal.setUpdateTime(LocalDateTime.now());
        
        return terminalRepository.save(terminal);
    }

    @Override
    @Transactional
    public Terminal assignTerminalToPerson(String id, String personId) {
        // 检查终端是否存在
        Terminal terminal = getTerminalById(id);
        
        // 分配终端给人员
        terminal.setPersonId(personId);
        terminal.setUpdateTime(LocalDateTime.now());
        
        return terminalRepository.save(terminal);
    }

    @Override
    @Transactional
    public Terminal unassignTerminal(String id) {
        // 检查终端是否存在
        Terminal terminal = getTerminalById(id);
        
        // 取消分配终端
        terminal.setPersonId(null);
        terminal.setUpdateTime(LocalDateTime.now());
        
        return terminalRepository.save(terminal);
    }

    @Override
    public boolean checkCodeExists(String code) {
        return terminalRepository.findByCode(code).isPresent();
    }

    @Override
    public boolean checkCodeExistsExcludeId(String code, String excludeId) {
        return terminalRepository.findByCode(code)
                .map(terminal -> !terminal.getId().equals(excludeId))
                .orElse(false);
    }

    @Override
    public PageResult<Terminal> getTerminalsByPage(int page, int size, String name, String organizationId, 
                                                 String subsystemId, String personId, String status) {
        return terminalRepository.findByPage(page, size, name, organizationId, subsystemId, personId, status);
    }
} 