package com.nzgreens.console.service.impl;

import com.github.pagehelper.PageHelper;
import com.nzgreens.common.exception.ErrorCodes;
import com.nzgreens.common.form.console.CoinSettingForm;
import com.nzgreens.common.form.console.PageSearchForm;
import com.nzgreens.common.utils.CurrencyUtil;
import com.nzgreens.console.service.BaseService;
import com.nzgreens.console.service.ICoinSettingService;
import com.nzgreens.dal.user.example.CoinSetting;
import com.nzgreens.dal.user.mapper.CoinSettingMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author:helizheng
 * @Date: Created in 2018/4/26 23:46
 */
@Service
public class CoinSettingService extends BaseService implements ICoinSettingService {
    @Resource
    private CoinSettingMapper coinSettingMapper;

    @Override
    public List<CoinSetting> selectForPage(PageSearchForm form) throws Exception {
        PageHelper.startPage(form.getPageNum(),form.getPageSize());
        return coinSettingMapper.selectByExample(null);
    }

    @Override
    public CoinSetting selectDetail(Long id) throws Exception {
        return coinSettingMapper.selectByPrimaryKey(id.byteValue());
    }

    @Override
    public void insert(CoinSettingForm coinSetting) throws Exception {
        checkForm(coinSetting);
        CoinSetting setting = new CoinSetting();
        setting.setMoney(CurrencyUtil.convertYuanToFen(coinSetting.getMoney()));
        setting.setCoin(coinSetting.getCoin());
        if(coinSettingMapper.insertSelective(setting) < 1){
            thrown(ErrorCodes.UPDATE_ERROR);
        }
    }

    @Override
    public void update(CoinSettingForm coinSetting) throws Exception {
        checkForm(coinSetting);
        if(coinSetting.getId() == null){
            thrown(ErrorCodes.ID_ILLEGAL);
        }
        CoinSetting setting = new CoinSetting();
        setting.setId(coinSetting.getId());
        setting.setMoney(CurrencyUtil.convertYuanToFen(coinSetting.getMoney()));
        setting.setCoin(coinSetting.getCoin());
        if(coinSettingMapper.updateByPrimaryKeySelective(setting) < 1){
            thrown(ErrorCodes.UPDATE_ERROR);
        }
    }

    @Override
    public void delete(Long id) throws Exception {
        if(id == null){
            thrown(ErrorCodes.ID_ILLEGAL);
        }
        if(coinSettingMapper.deleteByPrimaryKey(id.byteValue()) < 1){
            thrown(ErrorCodes.UPDATE_ERROR);
        }
    }

    protected void checkForm(CoinSettingForm coinSetting) throws Exception{
        if(coinSetting == null){
            thrown(ErrorCodes.DATA_ILLEGAL);
        }
        if(StringUtils.isBlank(coinSetting.getMoney())){
            thrown(ErrorCodes.COIN_MONEY_ILLEGAL);
        }
        if(coinSetting.getCoin() == null){
            thrown(ErrorCodes.COIN_ILLEGAL);
        }
    }
}
