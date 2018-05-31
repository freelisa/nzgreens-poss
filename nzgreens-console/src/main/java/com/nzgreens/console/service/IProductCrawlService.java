package com.nzgreens.console.service;

import com.nzgreens.common.form.console.PageSearchForm;
import com.nzgreens.common.form.console.ProductAddForm;
import com.nzgreens.common.form.console.ProductForm;
import com.nzgreens.dal.user.example.ProductBrand;
import com.nzgreens.dal.user.example.ProductCategory;
import com.nzgreens.dal.user.example.Products;
import com.nzgreens.dal.user.example.ProductsCrawl;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author:helizheng
 * @Date: Created in 2018/4/21 15:23
 */

public interface IProductCrawlService {
    List<ProductsCrawl> selectProductForPage(PageSearchForm form) throws Exception;

    void update(Long productId,Integer status) throws Exception;
}
