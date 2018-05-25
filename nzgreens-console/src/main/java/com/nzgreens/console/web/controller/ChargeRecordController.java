package com.nzgreens.console.web.controller;

import com.github.pagehelper.PageInfo;
import com.nzgreens.common.enums.AccountLogsTypeEnum;
import com.nzgreens.common.form.console.ChargeRecordForm;
import com.nzgreens.common.form.console.CoinFlowForm;
import com.nzgreens.common.model.ResultModel;
import com.nzgreens.common.model.console.ChargeRecordModel;
import com.nzgreens.common.model.console.CoinFlowModel;
import com.nzgreens.console.annotations.Auth;
import com.nzgreens.console.service.IChargeRecordService;
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
@RequestMapping("charge/record")
public class ChargeRecordController extends BaseController {
    @Resource
    private IChargeRecordService chargeRecordService;

    @RequestMapping("to-list")
    @Auth("CHARGE_RECORD_MANAGE")
    public String toList() throws Exception{
        return "user/charge-record-list";
    }


    @RequestMapping("search-list")
    @ResponseBody
    @Auth("CHARGE_RECORD_MANAGE")
    public ResultModel<PageInfo<ChargeRecordModel>> searchList(ChargeRecordForm form) throws Exception {
        ResultModel<PageInfo<ChargeRecordModel>> resultModel = new ResultModel<>();
        List<ChargeRecordModel> userModels = chargeRecordService.selectChargeRecordForPage(form);
        PageInfo<ChargeRecordModel> pageInfo = new PageInfo<>(userModels);
        resultModel.setData(pageInfo);
        return resultModel;
    }

}
