package com.nzgreens.console.service;

import com.nzgreens.common.form.console.PageSearchForm;
import com.nzgreens.dal.user.example.ProductBrand;

import java.util.List;

/**
 * @Author:helizheng
 * @Date: Created in 2018/4/26 23:44
 */

public interface IProductBrandService {
    List<ProductBrand> selectForPage(PageSearchForm form) throws Exception;

    ProductBrand selectDetail(Long id) throws Exception;

    void insert(ProductBrand brand) throws Exception;

    void update(ProductBrand brand) throws Exception;
}
