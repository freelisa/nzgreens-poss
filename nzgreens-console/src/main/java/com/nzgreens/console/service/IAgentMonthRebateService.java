package com.nzgreens.console.service;

import com.nzgreens.common.form.console.AgentMonthRebateForm;
import com.nzgreens.common.form.console.PageSearchForm;
import com.nzgreens.dal.user.example.AgentMonthRebate;

import java.util.List;

/**
 * @Author:helizheng
 * @Date: Created in 2018/4/26 23:44
 */

public interface IAgentMonthRebateService {
    List<AgentMonthRebate> selectForPage(PageSearchForm form) throws Exception;

    AgentMonthRebate selectDetail(Long id) throws Exception;

    void insert(AgentMonthRebateForm form) throws Exception;

    void update(AgentMonthRebateForm form) throws Exception;

    void delete(Long id) throws Exception;
}
