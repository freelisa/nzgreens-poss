package com.nzgreens.console.web.controller;

import com.github.pagehelper.PageInfo;
import com.nzgreens.common.form.console.AgentMonthRebateForm;
import com.nzgreens.common.form.console.AgentRebateForm;
import com.nzgreens.common.model.ResultModel;
import com.nzgreens.console.annotations.Auth;
import com.nzgreens.console.service.IAgentMonthRebateService;
import com.nzgreens.console.service.IUserService;
import com.nzgreens.dal.user.example.AgentMonthRebate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author:helizheng
 * @Date: Created in 2018/4/25 23:30
 */
@Controller
@RequestMapping("agent/month/rebate")
public class AgentMonthRebateController {
    @Resource
    private IAgentMonthRebateService agentMonthRebateService;
    @Resource
    private IUserService userService;

    @RequestMapping("to-list")
    @Auth("AGENT_MONTH_REBATE_MANAGE")
    public String toList(Model model) throws Exception{
        model.addAttribute("agentList",userService.searchAgentList());
        return "agent/agent-month-rebate-list";
    }

    @RequestMapping("search-list")
    @Auth("AGENT_MONTH_REBATE_MANAGE")
    @ResponseBody
    public ResultModel selectForPage(AgentRebateForm form) throws Exception{
        ResultModel<PageInfo<AgentMonthRebate>> resultModel = new ResultModel<>();
        List<AgentMonthRebate> agents = agentMonthRebateService.selectForPage(form);
        PageInfo<AgentMonthRebate> pageInfo = new PageInfo<>(agents);
        resultModel.setData(pageInfo);
        return resultModel;
    }

    @RequestMapping("search-detail")
    @ResponseBody
    @Auth("AGENT_MONTH_REBATE_MANAGE")
    public ResultModel searchDetai(Long id) throws Exception{
        ResultModel<AgentMonthRebate> resultModel = new ResultModel<>();
        AgentMonthRebate users = agentMonthRebateService.selectDetail(id);
        resultModel.setData(users);
        return resultModel;
    }

    @RequestMapping("insert")
    @ResponseBody
    @Auth("AGENT_MONTH_REBATE_UPDATE")
    public ResultModel insert(AgentMonthRebateForm agentRebate) throws Exception{
        agentMonthRebateService.insert(agentRebate);
        return new ResultModel();
    }

    @RequestMapping("update")
    @ResponseBody
    @Auth("AGENT_MONTH_REBATE_UPDATE")
    public ResultModel update(AgentMonthRebateForm agentRebate) throws Exception{
        agentMonthRebateService.update(agentRebate);
        return new ResultModel();
    }

    @RequestMapping("delete")
    @ResponseBody
    @Auth("AGENT_MONTH_REBATE_UPDATE")
    public ResultModel delete(Long id) throws Exception{
        agentMonthRebateService.delete(id);
        return new ResultModel();
    }
}
