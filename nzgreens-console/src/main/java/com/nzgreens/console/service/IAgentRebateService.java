package com.nzgreens.console.service;

import com.nzgreens.common.form.console.AgentRebateForm;
import com.nzgreens.common.model.console.AgentRebateModel;
import com.nzgreens.dal.user.example.AgentRebate;

import java.util.List;

/**
 * @Author:helizheng
 * @Date: Created in 2018/4/26 23:44
 */

public interface IAgentRebateService {
    List<AgentRebateModel> selectForPage(AgentRebateForm form) throws Exception;

    AgentRebate selectDetail(Long id) throws Exception;

    void insert(AgentRebate agentRebate) throws Exception;

    void update(AgentRebate agentRebate) throws Exception;

    void delete(Long id) throws Exception;
}
