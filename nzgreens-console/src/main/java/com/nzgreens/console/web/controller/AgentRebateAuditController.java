package com.nzgreens.console.web.controller;

import com.github.pagehelper.PageInfo;
import com.nzgreens.common.form.console.AgentRebateAuditForm;
import com.nzgreens.common.model.ResultModel;
import com.nzgreens.common.model.console.AgentRebateAuditModel;
import com.nzgreens.console.annotations.Auth;
import com.nzgreens.console.service.IAgentRebateAuditService;
import com.nzgreens.console.service.impl.AgentRebateAuditService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author:helizheng
 * @Date: Created in 2018/5/19 15:08
 */
@Controller
@RequestMapping("agent/rebate/audit")
public class AgentRebateAuditController extends BaseController{
    @Resource
    private IAgentRebateAuditService agentRebateAuditService;

    @RequestMapping("to-list")
    @Auth("AGENT_REBATE_AUDIT_MANAGE")
    public String toList() throws Exception{
        return "agent/agent-rebate-audit-list";
    }

    @RequestMapping("search-list")
    @ResponseBody
    @Auth("AGENT_REBATE_AUDIT_MANAGE")
    public ResultModel selectForPage(AgentRebateAuditForm form) throws Exception{
        ResultModel<PageInfo<AgentRebateAuditModel>> resultModel = new ResultModel<>();
        List<AgentRebateAuditModel> users = agentRebateAuditService.selectAgentRebateAuditForPage(form);
        PageInfo<AgentRebateAuditModel> pageInfo = new PageInfo<>(users);
        resultModel.setData(pageInfo);
        return resultModel;
    }

    @RequestMapping("search-detail")
    @ResponseBody
    @Auth("AGENT_REBATE_AUDIT_MANAGE")
    public ResultModel searchDetail(Long id) throws Exception{
        ResultModel<AgentRebateAuditModel> resultModel = new ResultModel<>();
        AgentRebateAuditModel users = agentRebateAuditService.selectAgentRebateAuditDetail(id);
        resultModel.setData(users);
        return resultModel;
    }

    @RequestMapping("update")
    @ResponseBody
    @Auth("AGENT_REBATE_AUDIT_UPDATE")
    public ResultModel updateStatus(Long id,Integer status,String amount,String remark) throws Exception{
        agentRebateAuditService.updateAgentRebateAuditStatus(id,status,amount,remark);
        return new ResultModel();
    }
}
