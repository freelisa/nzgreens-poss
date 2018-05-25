package com.nzgreens.console.service;

import com.nzgreens.common.form.console.PageSearchForm;
import com.nzgreens.common.model.console.ProductCategoryModel;
import com.nzgreens.dal.user.example.ProductBrand;
import com.nzgreens.dal.user.example.ProductCategory;

import java.util.List;

/**
 * @Author:helizheng
 * @Date: Created in 2018/4/26 23:44
 */

public interface IProductCategoryService {
    List<ProductCategory> selectForPage(PageSearchForm form) throws Exception;

    ProductCategoryModel selectDetail(Long id) throws Exception;

    void insert(ProductCategory category) throws Exception;

    void update(ProductCategory category) throws Exception;
}
