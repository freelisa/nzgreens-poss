package com.nzgreens.console.service;

import com.nzgreens.common.form.console.CoinFlowForm;
import com.nzgreens.common.model.console.CoinFlowModel;

import java.util.List;

/**
 * @Author:helizheng
 * @Date: Created in 2018/4/15 16:00
 */

public interface ICoinFlowService {
    List<CoinFlowModel> selectCoinFlowForPage(CoinFlowForm form) throws Exception;
}
