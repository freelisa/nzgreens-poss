package com.nzgreens.console.service;

import com.nzgreens.common.form.console.ChargeRecordForm;
import com.nzgreens.common.model.console.ChargeRecordModel;

import java.util.List;

/**
 * @Author:helizheng
 * @Date: Created in 2018/4/15 16:00
 */

public interface IChargeRecordService {
    List<ChargeRecordModel> selectChargeRecordForPage(ChargeRecordForm form) throws Exception;
}
