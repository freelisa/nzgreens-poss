package com.nzgreens.dal.user.mapper;

import com.nzgreens.common.form.console.CoinFlowForm;
import com.nzgreens.common.model.console.CoinFlowModel;
import java.util.List;

/**
 * @Author:helizheng
 * @Date: Created in 2018/4/15 15:35
 */

public interface CoinFlowMapper {
    /**
     * 查询流水
     * @param form
     * @return
     */
    List<CoinFlowModel> selectCoinFlowForPage(CoinFlowForm form);
}
