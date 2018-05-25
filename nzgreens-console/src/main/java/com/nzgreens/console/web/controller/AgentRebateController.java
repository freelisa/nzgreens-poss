package com.nzgreens.console.web.controller;

import com.github.pagehelper.PageInfo;
import com.nzgreens.common.form.console.AgentRebateForm;
import com.nzgreens.common.model.ResultModel;
import com.nzgreens.common.model.console.AgentRebateModel;
import com.nzgreens.console.annotations.Auth;
import com.nzgreens.console.service.IAgentRebateService;
import com.nzgreens.console.service.IUserService;
import com.nzgreens.dal.user.example.AgentRebate;
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
@RequestMapping("agent/rebate")
public class AgentRebateController {
    @Resource
    private IAgentRebateService agentRebateService;
    @Resource
    private IUserService userService;

    @RequestMapping("to-list")
    @Auth("AGENT_REBATE_MANAGE")
    public String toList(Model model) throws Exception{
        model.addAttribute("agentList",userService.searchAgentList());
        return "agent/agent-rebate-list";
    }

    @RequestMapping("search-list")
    @Auth("AGENT_REBATE_MANAGE")
    @ResponseBody
    public ResultModel selectForPage(AgentRebateForm form) throws Exception{
        ResultModel<PageInfo<AgentRebateModel>> resultModel = new ResultModel<>();
        List<AgentRebateModel> agents = agentRebateService.selectForPage(form);
        PageInfo<AgentRebateModel> pageInfo = new PageInfo<>(agents);
        resultModel.setData(pageInfo);
        return resultModel;
    }

    @RequestMapping("search-detail")
    @ResponseBody
    @Auth("AGENT_REBATE_MANAGE")
    public ResultModel searchDetai(Long id) throws Exception{
        ResultModel<AgentRebate> resultModel = new ResultModel<>();
        AgentRebate users = agentRebateService.selectDetail(id);
        resultModel.setData(users);
        return resultModel;
    }

    @RequestMapping("insert")
    @ResponseBody
    @Auth("AGENT_REBATE_UPDATE")
    public ResultModel insert(AgentRebate agentRebate) throws Exception{
        agentRebateService.insert(agentRebate);
        return new ResultModel();
    }

    @RequestMapping("update")
    @ResponseBody
    @Auth("AGENT_REBATE_UPDATE")
    public ResultModel update(AgentRebate agentRebate) throws Exception{
        agentRebateService.update(agentRebate);
        return new ResultModel();
    }

    @RequestMapping("delete")
    @ResponseBody
    @Auth("AGENT_REBATE_UPDATE")
    public ResultModel delete(Long id) throws Exception{
        agentRebateService.delete(id);
        return new ResultModel();
    }
}
