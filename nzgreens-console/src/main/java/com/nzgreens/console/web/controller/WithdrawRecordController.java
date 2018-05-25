package com.nzgreens.console.web.controller;

import com.github.pagehelper.PageInfo;
import com.nzgreens.common.enums.AccountLogsTypeEnum;
import com.nzgreens.common.form.console.CoinFlowForm;
import com.nzgreens.common.form.console.WithdrawRecordForm;
import com.nzgreens.common.model.ResultModel;
import com.nzgreens.common.model.console.CoinFlowModel;
import com.nzgreens.common.model.console.WithdrawRecordModel;
import com.nzgreens.console.annotations.Auth;
import com.nzgreens.console.service.ICoinFlowService;
import com.nzgreens.console.service.IWithdrawRecordService;
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
@RequestMapping("withdraw/record")
public class WithdrawRecordController extends BaseController {
    @Resource
    private IWithdrawRecordService withdrawRecordService;

    @RequestMapping("to-list")
    @Auth("WITHDRAW_RECORD_MANAGE")
    public String toList() throws Exception{
        return "user/withdraw-record-list";
    }

    @RequestMapping("search-list")
    @ResponseBody
    @Auth("WITHDRAW_RECORD_MANAGE")
    public ResultModel<PageInfo<WithdrawRecordModel>> searchList(WithdrawRecordForm form) throws Exception {
        ResultModel<PageInfo<WithdrawRecordModel>> resultModel = new ResultModel<>();
        List<WithdrawRecordModel> userModels = withdrawRecordService.selectWithdrawRecordForPage(form);
        PageInfo<WithdrawRecordModel> pageInfo = new PageInfo<>(userModels);
        resultModel.setData(pageInfo);
        return resultModel;
    }

}
