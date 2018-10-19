package com.nzgreens.console.service.impl;

import com.github.pagehelper.PageHelper;
import com.nzgreens.common.exception.ErrorCodes;
import com.nzgreens.common.form.console.PageSearchForm;
import com.nzgreens.common.form.console.ProductFreightForm;
import com.nzgreens.common.utils.CurrencyUtil;
import com.nzgreens.console.service.BaseService;
import com.nzgreens.console.service.ICoinSettingService;
import com.nzgreens.console.service.IProductFreightService;
import com.nzgreens.dal.user.example.CoinSetting;
import com.nzgreens.dal.user.example.ProductFreight;
import com.nzgreens.dal.user.mapper.CoinSettingMapper;
import com.nzgreens.dal.user.mapper.ProductFreightMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author:helizheng
 * @Date: Created in 2018/4/26 23:46
 */
@Service
public class ProductFreightService extends BaseService implements IProductFreightService {
    @Resource
    private ProductFreightMapper productFreightMapper;

    @Override
    public List<ProductFreight> selectForPage(PageSearchForm form) throws Exception {
        PageHelper.startPage(form.getPageNum(),form.getPageSize());
        return productFreightMapper.selectByExample(null);
    }

    @Override
    public ProductFreight selectDetail(Long id) throws Exception {
        return productFreightMapper.selectByPrimaryKey(id);
    }

    @Override
    public void insert(ProductFreightForm productFreight) throws Exception {
        checkForm(productFreight);
        ProductFreight freight = new ProductFreight();
        freight.setFreight(CurrencyUtil.convertYuanToFen(productFreight.getFreight()));
        freight.setMinFreight(CurrencyUtil.convertYuanToFen(productFreight.getMinFreight()));
        freight.setProductWeight(productFreight.getProductWeight());
        if(productFreightMapper.insertSelective(freight) < 1){
            thrown(ErrorCodes.UPDATE_ERROR);
        }
    }

    @Override
    public void update(ProductFreightForm productFreight) throws Exception {
        checkForm(productFreight);
        if(productFreight.getId() == null){
            thrown(ErrorCodes.ID_ILLEGAL);
        }
        ProductFreight freight = new ProductFreight();
        freight.setId(productFreight.getId());
        freight.setFreight(CurrencyUtil.convertYuanToFen(productFreight.getFreight()));
        freight.setMinFreight(CurrencyUtil.convertYuanToFen(productFreight.getMinFreight()));
        freight.setProductWeight(productFreight.getProductWeight());
        if(productFreightMapper.updateByPrimaryKeySelective(freight) < 1){
            thrown(ErrorCodes.UPDATE_ERROR);
        }
    }

    @Override
    public void delete(Long id) throws Exception {
        if(id == null){
            thrown(ErrorCodes.ID_ILLEGAL);
        }
        if(productFreightMapper.deleteByPrimaryKey(id) < 1){
            thrown(ErrorCodes.UPDATE_ERROR);
        }
    }

    protected void checkForm(ProductFreightForm productFreight) throws Exception{
        if(productFreight == null){
            thrown(ErrorCodes.DATA_ILLEGAL);
        }
        if(StringUtils.isBlank(productFreight.getFreight())){
            thrown(ErrorCodes.FREIGHT_ILLEGAL);
        }
        if(productFreight.getProductWeight() == null){
            thrown(ErrorCodes.PRODUCT_WEIGHT_ILLEGAL);
        }
        if(StringUtils.isBlank(productFreight.getMinFreight())){
            thrown(ErrorCodes.FREIGHT_ILLEGAL);
        }
    }
}
