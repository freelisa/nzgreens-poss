package com.nzgreens.console.service;

import com.nzgreens.common.form.console.WithdrawRecordForm;
import com.nzgreens.common.model.console.WithdrawRecordModel;

import java.util.List;

/**
 * @Author:helizheng
 * @Date: Created in 2018/4/15 16:00
 */

public interface IWithdrawRecordService {
    List<WithdrawRecordModel> selectWithdrawRecordForPage(WithdrawRecordForm form) throws Exception;
}
