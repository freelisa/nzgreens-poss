package com.nzgreens.console.web.controller;

import com.github.pagehelper.PageInfo;
import com.nzgreens.common.form.console.AgentRebateForm;
import com.nzgreens.common.form.console.CoinSettingForm;
import com.nzgreens.common.form.console.PageSearchForm;
import com.nzgreens.common.model.ResultModel;
import com.nzgreens.common.model.console.AgentRebateModel;
import com.nzgreens.console.annotations.Auth;
import com.nzgreens.console.service.IAgentRebateService;
import com.nzgreens.console.service.ICoinSettingService;
import com.nzgreens.console.service.IUserService;
import com.nzgreens.dal.user.example.AgentRebate;
import com.nzgreens.dal.user.example.CoinSetting;
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
@RequestMapping("coin/setting")
public class CoinSettingController {
    @Resource
    private ICoinSettingService coinSettingService;

    @RequestMapping("to-list")
    @Auth("COIN_SETTING_MANAGE")
    public String toList() throws Exception{
        return "product/coin-setting-list";
    }

    @RequestMapping("search-list")
    @Auth("COIN_SETTING_MANAGE")
    @ResponseBody
    public ResultModel selectForPage(PageSearchForm form) throws Exception{
        ResultModel<PageInfo<CoinSetting>> resultModel = new ResultModel<>();
        List<CoinSetting> coinSettings = coinSettingService.selectForPage(form);
        PageInfo<CoinSetting> pageInfo = new PageInfo<>(coinSettings);
        resultModel.setData(pageInfo);
        return resultModel;
    }

    @RequestMapping("search-detail")
    @ResponseBody
    @Auth("COIN_SETTING_MANAGE")
    public ResultModel searchDetai(Long id) throws Exception{
        ResultModel<CoinSetting> resultModel = new ResultModel<>();
        CoinSetting users = coinSettingService.selectDetail(id);
        resultModel.setData(users);
        return resultModel;
    }

    @RequestMapping("insert")
    @ResponseBody
    @Auth("COIN_SETTING_UPDATE")
    public ResultModel insert(CoinSettingForm coinSetting) throws Exception{
        coinSettingService.insert(coinSetting);
        return new ResultModel();
    }

    @RequestMapping("update")
    @ResponseBody
    @Auth("COIN_SETTING_UPDATE")
    public ResultModel update(CoinSettingForm coinSetting) throws Exception{
        coinSettingService.update(coinSetting);
        return new ResultModel();
    }

    @RequestMapping("delete")
    @ResponseBody
    @Auth("COIN_SETTING_UPDATE")
    public ResultModel delete(Long id) throws Exception{
        coinSettingService.delete(id);
        return new ResultModel();
    }
}
