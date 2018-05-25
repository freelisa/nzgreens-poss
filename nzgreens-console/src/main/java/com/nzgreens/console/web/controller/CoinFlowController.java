package com.nzgreens.console.web.controller;

import com.github.pagehelper.PageInfo;
import com.nzgreens.common.enums.AccountLogsTypeEnum;
import com.nzgreens.common.form.console.CoinFlowForm;
import com.nzgreens.common.model.ResultModel;
import com.nzgreens.common.model.console.CoinFlowModel;
import com.nzgreens.console.annotations.Auth;
import com.nzgreens.console.service.ICoinFlowService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author:helizheng
 * @Date: Created in 2018/4/20 1:23
 */
@Controller
@RequestMapping("coin/flow")
public class CoinFlowController extends BaseController {
    @Resource
    private ICoinFlowService coinFlowService;

    @RequestMapping("to-list")
    @Auth("COIN_FLOW_MANAGE")
    public String toList(Model model) throws Exception{
        model.addAttribute("accountLogsTypeEnum", AccountLogsTypeEnum.values());
        return "user/coin-flow-list";
    }


    @RequestMapping("search-list")
    @ResponseBody
    @Auth("COIN_FLOW_MANAGE")
    public ResultModel<PageInfo<CoinFlowModel>> searchList(CoinFlowForm form) throws Exception {
        ResultModel<PageInfo<CoinFlowModel>> resultModel = new ResultModel<>();
        List<CoinFlowModel> userModels = coinFlowService.selectCoinFlowForPage(form);
        PageInfo<CoinFlowModel> pageInfo = new PageInfo<>(userModels);
        resultModel.setData(pageInfo);
        return resultModel;
    }

}
