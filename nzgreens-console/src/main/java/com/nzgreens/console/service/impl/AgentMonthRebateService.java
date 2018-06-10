package com.nzgreens.console.service.impl;

import com.github.pagehelper.PageHelper;
import com.nzgreens.common.exception.ErrorCodes;
import com.nzgreens.common.form.console.AgentMonthRebateForm;
import com.nzgreens.common.form.console.PageSearchForm;
import com.nzgreens.common.utils.CurrencyUtil;
import com.nzgreens.console.service.BaseService;
import com.nzgreens.console.service.IAgentMonthRebateService;
import com.nzgreens.dal.user.example.AgentMonthRebate;
import com.nzgreens.dal.user.mapper.AgentMonthRebateMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author:helizheng
 * @Date: Created in 2018/4/26 23:46
 */
@Service
public class AgentMonthRebateService extends BaseService implements IAgentMonthRebateService {
    @Resource
    private AgentMonthRebateMapper agentMonthRebateMapper;

    @Override
    public List<AgentMonthRebate> selectForPage(PageSearchForm form) throws Exception {
        PageHelper.startPage(form.getPageNum(),form.getPageSize());
        return agentMonthRebateMapper.selectByExample(null);
    }

    @Override
    public AgentMonthRebate selectDetail(Long id) throws Exception {
        return agentMonthRebateMapper.selectByPrimaryKey(id);
    }

    @Override
    public void insert(AgentMonthRebateForm agentRebate) throws Exception {
        checkForm(agentRebate);
        AgentMonthRebate rebate = new AgentMonthRebate();
        BeanUtils.copyProperties(agentRebate,rebate);
        rebate.setAmount(CurrencyUtil.convertYuanToFen(agentRebate.getAmount()));
        if(agentMonthRebateMapper.insertSelective(rebate) < 1){
            thrown(ErrorCodes.UPDATE_ERROR);
        }
    }

    @Override
    public void update(AgentMonthRebateForm agentRebate) throws Exception {
        checkForm(agentRebate);
        if(agentRebate.getId() == null){
            thrown(ErrorCodes.ID_ILLEGAL);
        }
        AgentMonthRebate rebate = new AgentMonthRebate();
        BeanUtils.copyProperties(agentRebate,rebate);
        rebate.setAmount(CurrencyUtil.convertYuanToFen(agentRebate.getAmount()));
        if(agentMonthRebateMapper.updateByPrimaryKeySelective(rebate) < 1){
            thrown(ErrorCodes.UPDATE_ERROR);
        }
    }

    @Override
    public void delete(Long id) throws Exception {
        if(id == null){
            thrown(ErrorCodes.ID_ILLEGAL);
        }
        if(agentMonthRebateMapper.deleteByPrimaryKey(id) < 1){
            thrown(ErrorCodes.UPDATE_ERROR);
        }
    }

    protected void checkForm(AgentMonthRebateForm agentRebate) throws Exception{
        if(agentRebate == null){
            thrown(ErrorCodes.DATA_ILLEGAL);
        }
        if(StringUtils.isBlank(agentRebate.getAmount())){
            thrown(ErrorCodes.MONTH_REBATE_AMOUNT_ILLEGAL);
        }
        if(agentRebate.getMonthRebate() == null){
            thrown(ErrorCodes.MONTH_REBATE_ILLEGAL);
        }
    }
}
