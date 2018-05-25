package com.nzgreens.console.service;

import com.nzgreens.common.form.console.PageSearchForm;
import com.nzgreens.common.form.console.ProductFreightForm;
import com.nzgreens.dal.user.example.CoinSetting;
import com.nzgreens.dal.user.example.ProductFreight;

import java.util.List;

/**
 * @Author:helizheng
 * @Date: Created in 2018/4/26 23:44
 */

public interface IProductFreightService {
    List<ProductFreight> selectForPage(PageSearchForm form) throws Exception;

    ProductFreight selectDetail(Long id) throws Exception;

    void insert(ProductFreightForm productFreight) throws Exception;

    void update(ProductFreightForm productFreight) throws Exception;

    void delete(Long id) throws Exception;
}
