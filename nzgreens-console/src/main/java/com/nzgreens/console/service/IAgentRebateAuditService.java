package com.nzgreens.console.service;

import com.nzgreens.common.form.console.AgentRebateAuditForm;
import com.nzgreens.common.model.console.AgentRebateAuditModel;

import java.util.List;

/**
 * @Author:helizheng
 * @Date: Created in 2018/5/19 18:35
 */

public interface IAgentRebateAuditService {
    List<AgentRebateAuditModel> selectAgentRebateAuditForPage(AgentRebateAuditForm form) throws Exception;

    AgentRebateAuditModel selectAgentRebateAuditDetail(Long id) throws Exception;

    void updateAgentRebateAuditStatus(Long id,Integer status,String amount,String remark) throws Exception;
}
