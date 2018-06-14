package com.nzgreens.console.service;

import com.nzgreens.common.form.console.PageSearchForm;
import com.nzgreens.common.form.console.ProductAddForm;
import com.nzgreens.common.form.console.ProductForm;
import com.nzgreens.common.model.console.ProductsModel;
import com.nzgreens.common.model.console.ProductsPriceChangeModel;
import com.nzgreens.dal.user.example.ProductBrand;
import com.nzgreens.dal.user.example.ProductCategory;
import com.nzgreens.dal.user.example.Products;
import com.nzgreens.dal.user.example.ProductsPriceChange;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author:helizheng
 * @Date: Created in 2018/4/21 15:23
 */

public interface IProductService {
    List<Products> selectProductForPage(ProductForm form) throws Exception;

    List<ProductsModel> selectProductExport(ProductForm form) throws Exception;

    void updateProductPriceBatch(List<ProductsModel> list) throws Exception;

    List<ProductsPriceChangeModel> selectProductChangeForPage(PageSearchForm form) throws Exception;

    void updateProductChangeForPage(Long id) throws Exception;

    int selectProductChangeCount() throws Exception;

    ProductAddForm selectProductDetail(Long id) throws Exception;

    List<ProductCategory> selectProductCategory() throws Exception;

    List<ProductBrand> selectProductBrand() throws Exception;

    void insert(ProductAddForm form) throws Exception;

    void update(ProductAddForm form) throws Exception;

    void delete(Long id) throws Exception;

    String uploadImg(MultipartFile multiFile) throws Exception;
}
