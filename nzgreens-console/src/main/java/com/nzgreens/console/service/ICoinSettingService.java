package com.nzgreens.console.service;

import com.nzgreens.common.form.console.CoinSettingForm;
import com.nzgreens.common.form.console.PageSearchForm;
import com.nzgreens.dal.user.example.CoinSetting;

import java.util.List;

/**
 * @Author:helizheng
 * @Date: Created in 2018/4/26 23:44
 */

public interface ICoinSettingService {
    List<CoinSetting> selectForPage(PageSearchForm form) throws Exception;

    CoinSetting selectDetail(Long id) throws Exception;

    void insert(CoinSettingForm coinSetting) throws Exception;

    void update(CoinSettingForm coinSetting) throws Exception;

    void delete(Long id) throws Exception;
}
