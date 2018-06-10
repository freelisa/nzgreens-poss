package com.nzgreens.console.service.impl;

import com.github.pagehelper.PageHelper;
import com.nzgreens.common.enums.AccountLogsTypeEnum;
import com.nzgreens.common.enums.AgentRebateAuditStatusEnum;
import com.nzgreens.common.enums.AgentRebateTypeEnum;
import com.nzgreens.common.exception.ErrorCodes;
import com.nzgreens.common.form.console.AgentRebateAuditForm;
import com.nzgreens.common.model.console.AgentRebateAuditModel;
import com.nzgreens.common.utils.CurrencyUtil;
import com.nzgreens.console.service.BaseService;
import com.nzgreens.console.service.IAgentRebateAuditService;
import com.nzgreens.dal.user.example.*;
import com.nzgreens.dal.user.mapper.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author:helizheng
 * @Date: Created in 2018/5/19 18:35
 */
@Service
public class AgentRebateAuditService extends BaseService implements IAgentRebateAuditService {
    @Resource
    private SubAgentRebateAuditMapper subAgentRebateAuditMapper;
    @Resource
    private AgentRebateAuditMapper agentRebateAuditMapper;
    @Resource
    private SubUserMapper subUserMapper;
    @Resource
    private AccountLogsMapper accountLogsMapper;

    @Override
    public List<AgentRebateAuditModel> selectAgentRebateAuditForPage(AgentRebateAuditForm form) throws Exception {
        PageHelper.startPage(form.getPageNum(),form.getPageSize());
        return subAgentRebateAuditMapper.selectAgentRebateAuditForPage(form);
    }

    @Override
    public AgentRebateAuditModel selectAgentRebateAuditDetail(Long id) throws Exception {
        AgentRebateAuditModel auditModel = subAgentRebateAuditMapper.selectAgentRebateAuditDetail(id);
        auditModel.setTypeDesc(AgentRebateTypeEnum.getName(auditModel.getType().intValue()));
//        auditModel.setRebatePrice(CurrencyUtil.convertFenToYuan());
//        auditModel.setActualRebatePrice();
        return auditModel;
    }

    @Override
    public void updateAgentRebateAuditStatus(Long id, Integer status,String amount) throws Exception {
        if(id == null){
            thrown(ErrorCodes.ID_ILLEGAL);
        }
        if(status == null){
            thrown(ErrorCodes.REBATE_STATUS_ILLEGAL);
        }
        if(StringUtils.isBlank(amount)){
            thrown(ErrorCodes.REBATE_AMOUNT_ILLEGAL);
        }
        BigDecimal s = new BigDecimal(amount);
        if(s.compareTo(new BigDecimal(0)) == 0){
            thrown(ErrorCodes.REBATE_AMOUNT_ILLEGAL);
        }
        AgentRebateAudit agentRebateAudit = agentRebateAuditMapper.selectByPrimaryKey(id);
        int auditStatus = agentRebateAudit.getStatus();
        if(auditStatus == AgentRebateAuditStatusEnum._DONE.getValue()
                || auditStatus == AgentRebateAuditStatusEnum._REFUSED.getValue()){
            thrown(ErrorCodes.REBATE_STATUS_HANDLE_SUCC);
        }
        AgentRebateAuditExample example = new AgentRebateAuditExample();
        example.createCriteria().andIdEqualTo(id)
                .andStatusEqualTo((byte)AgentRebateAuditStatusEnum._PENDING.getValue());

        AgentRebateAudit audit = new AgentRebateAudit();
        audit.setStatus(status.byteValue());
        audit.setActualRebatePrice(CurrencyUtil.convertYuanToFen(s.toString()));
        if(agentRebateAuditMapper.updateByExampleSelective(audit,example) < 1){
            thrown(ErrorCodes.UPDATE_ERROR);
        }

        if(status == AgentRebateAuditStatusEnum._DONE.getValue()){
            handleRebate(agentRebateAudit,audit.getActualRebatePrice());
        }
    }

    private void handleRebate(AgentRebateAudit audit,Long rebatePrice) throws Exception{
        subUserMapper.addBalance(audit.getAgentUserId(),rebatePrice.intValue());

        //代理
        Users users = checkUser(audit.getAgentUserId());
        //系统
        Users sysUser = selectSystemUser();
        //代理返佣日志
        AccountLogs rebateLog = new AccountLogs();
        rebateLog.setUserId(audit.getAgentUserId());
        rebateLog.setType((byte) AccountLogsTypeEnum._ORDER_REBATE.getValue());
        rebateLog.setRecordId(audit.getUserOrderId());
        rebateLog.setBefore(users.getBalance());
        rebateLog.setAmount(rebatePrice);
        rebateLog.setAfter(rebateLog.getBefore() + rebateLog.getAmount());
        rebateLog.setTriggerUserId(sysUser.getId());
        if(accountLogsMapper.insertSelective(rebateLog) < 1){
            thrown(ErrorCodes.UPDATE_ERROR);
        }

        //系统减币
        int row = subUserMapper.reduceBalance(sysUser.getId(), rebatePrice.intValue());
        if(row < 1){
            thrown(ErrorCodes.SYSTEM_BALANCE_NOT_ENOUGH);
        }

        //系统减币日志
        AccountLogs systemLog = new AccountLogs();
        systemLog.setUserId(sysUser.getId());
        systemLog.setType((byte) AccountLogsTypeEnum._ORDER_REBATE.getValue());
        systemLog.setRecordId(audit.getUserOrderId());
        systemLog.setBefore(sysUser.getBalance());
        systemLog.setAmount(-rebatePrice);
        systemLog.setAfter(sysUser.getBalance() - rebatePrice);
        systemLog.setTriggerUserId(sysUser.getId());
        if(accountLogsMapper.insertSelective(systemLog) < 1){
            thrown(ErrorCodes.UPDATE_ERROR);
        }
    }
}
