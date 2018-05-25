package com.nzgreens.console.service.impl;

import com.github.pagehelper.PageHelper;
import com.nzgreens.common.exception.ErrorCodes;
import com.nzgreens.common.form.console.AgentRebateForm;
import com.nzgreens.common.model.console.AgentRebateModel;
import com.nzgreens.console.service.BaseService;
import com.nzgreens.console.service.IAgentRebateService;
import com.nzgreens.dal.user.example.AgentRebate;
import com.nzgreens.dal.user.mapper.AgentRebateMapper;
import com.nzgreens.dal.user.mapper.SubAgentRebateMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author:helizheng
 * @Date: Created in 2018/4/26 23:46
 */
@Service
public class AgentRebateService extends BaseService implements IAgentRebateService {
    @Resource
    private AgentRebateMapper agentRebateMapper;
    @Resource
    private SubAgentRebateMapper subAgentRebateMapper;

    @Override
    public List<AgentRebateModel> selectForPage(AgentRebateForm form) throws Exception {
        PageHelper.startPage(form.getPageNum(),form.getPageSize());
        return subAgentRebateMapper.selectForPage(form);
    }

    @Override
    public AgentRebate selectDetail(Long id) throws Exception {
        return agentRebateMapper.selectByPrimaryKey(id);
    }

    @Override
    public void insert(AgentRebate agentRebate) throws Exception {
        checkForm(agentRebate);
        if(agentRebateMapper.insertSelective(agentRebate) < 1){
            thrown(ErrorCodes.UPDATE_ERROR);
        }
    }

    @Override
    public void update(AgentRebate agentRebate) throws Exception {
        checkForm(agentRebate);
        if(agentRebate.getId() == null){
            thrown(ErrorCodes.ID_ILLEGAL);
        }
        if(agentRebateMapper.updateByPrimaryKeySelective(agentRebate) < 1){
            thrown(ErrorCodes.UPDATE_ERROR);
        }
    }

    @Override
    public void delete(Long id) throws Exception {
        if(id == null){
            thrown(ErrorCodes.ID_ILLEGAL);
        }
        if(agentRebateMapper.deleteByPrimaryKey(id) < 1){
            thrown(ErrorCodes.UPDATE_ERROR);
        }
    }

    protected void checkForm(AgentRebate agentRebate) throws Exception{
        if(agentRebate == null){
            thrown(ErrorCodes.DATA_ILLEGAL);
        }
        if(agentRebate.getAgentUserId() == null){
            thrown(ErrorCodes.AGENT_USER_ID_ILLEGAL);
        }
        if(agentRebate.getOrderRebate() == null){
            thrown(ErrorCodes.ORDER_REBATE_ILLEGAL);
        }
        if(agentRebate.getMonthRebate() == null){
            thrown(ErrorCodes.MONTH_REBATE_ILLEGAL);
        }
    }
}
