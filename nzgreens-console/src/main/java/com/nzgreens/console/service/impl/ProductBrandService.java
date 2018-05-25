package com.nzgreens.console.service.impl;

import com.github.pagehelper.PageHelper;
import com.nzgreens.common.exception.ErrorCodes;
import com.nzgreens.common.form.console.PageSearchForm;
import com.nzgreens.console.service.BaseService;
import com.nzgreens.console.service.IProductBrandService;
import com.nzgreens.dal.Limit;
import com.nzgreens.dal.user.example.ProductBrand;
import com.nzgreens.dal.user.example.ProductBrandExample;
import com.nzgreens.dal.user.mapper.ProductBrandMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author:helizheng
 * @Date: Created in 2018/5/19 22:05
 */
@Service
public class ProductBrandService extends BaseService implements IProductBrandService {
    @Resource
    private ProductBrandMapper productBrandMapper;

    @Override
    public List<ProductBrand> selectForPage(PageSearchForm form) throws Exception {
        PageHelper.startPage(form.getPageNum(),form.getPageSize());
        return productBrandMapper.selectByExample(null);
    }

    @Override
    public ProductBrand selectDetail(Long id) throws Exception {
        return productBrandMapper.selectByPrimaryKey(id);
    }

    @Override
    public void insert(ProductBrand brand) throws Exception {
        if(brand == null){
            thrown(ErrorCodes.PRODUCT_BRAND_NAME_ILLEGAL);
        }
        if(StringUtils.isBlank(brand.getName())){
            thrown(ErrorCodes.PRODUCT_BRAND_NAME_ILLEGAL);
        }
        if(brand.getIsValid() == null){
            thrown(ErrorCodes.PRODUCT_STATUS_ILLEGAL);
        }
        ProductBrandExample example = new ProductBrandExample();
        example.setOrderByClause(" id desc");
        example.setLimit(new Limit(0,1));
        List<ProductBrand> productBrands = productBrandMapper.selectByExample(example);
        Long id = 10000L;
        if(CollectionUtils.isNotEmpty(productBrands)){
            Long productId = productBrands.get(0).getId();
            if(productId > 10000){
                id = productId + 1;
            }else{
                id = id + productBrands.get(0).getId();
            }
        }
        brand.setId(id);
        if(productBrandMapper.insertSelective(brand) < 1){
            thrown(ErrorCodes.UPDATE_ERROR);
        }
    }

    @Override
    public void update(ProductBrand brand) throws Exception {
        if(brand == null){
            thrown(ErrorCodes.PRODUCT_BRAND_NAME_ILLEGAL);
        }
        if(brand.getId() == null){
            thrown(ErrorCodes.ID_ILLEGAL);
        }
        if(StringUtils.isBlank(brand.getName())){
            thrown(ErrorCodes.PRODUCT_BRAND_NAME_ILLEGAL);
        }
        if(brand.getIsValid() == null){
            thrown(ErrorCodes.PRODUCT_STATUS_ILLEGAL);
        }
        if(productBrandMapper.updateByPrimaryKeySelective(brand) < 1){
            thrown(ErrorCodes.UPDATE_ERROR);
        }
    }
}
