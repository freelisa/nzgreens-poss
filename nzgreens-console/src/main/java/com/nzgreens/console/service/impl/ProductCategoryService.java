package com.nzgreens.console.service.impl;

import com.github.pagehelper.PageHelper;
import com.nzgreens.common.exception.ErrorCodes;
import com.nzgreens.common.form.console.PageSearchForm;
import com.nzgreens.common.model.console.ProductCategoryModel;
import com.nzgreens.common.utils.BeanMapUtil;
import com.nzgreens.common.utils.CollectionUtil;
import com.nzgreens.console.service.BaseService;
import com.nzgreens.console.service.IProductCategoryService;
import com.nzgreens.dal.Limit;
import com.nzgreens.dal.user.example.ProductCategory;
import com.nzgreens.dal.user.example.ProductCategoryExample;
import com.nzgreens.dal.user.mapper.ProductCategoryMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author:helizheng
 * @Date: Created in 2018/5/19 22:05
 */
@Service
public class ProductCategoryService extends BaseService implements IProductCategoryService {
    @Resource
    private ProductCategoryMapper productCategoryMapper;

    @Override
    public List<ProductCategory> selectForPage(PageSearchForm form) throws Exception {
        PageHelper.startPage(form.getPageNum(),form.getPageSize());
        List<ProductCategory> productCategories = productCategoryMapper.selectByExample(null);
        return productCategories;
    }

    @Override
    public ProductCategoryModel selectDetail(Long id) throws Exception {
        ProductCategoryModel model = new ProductCategoryModel();
        ProductCategory productCategory = productCategoryMapper.selectByPrimaryKey(id);
        BeanUtils.copyProperties(productCategory,model);
        if(productCategory.getParentId() != null){
            model.setParentName(productCategoryMapper.selectByPrimaryKey(productCategory.getParentId()).getName());
        }
        return model;
    }

    @Override
    public void insert(ProductCategory category) throws Exception {
        if(category == null){
            thrown(ErrorCodes.PRODUCT_CATEGORY_NAME_ILLEGAL);
        }
        if(StringUtils.isBlank(category.getName())){
            thrown(ErrorCodes.PRODUCT_CATEGORY_NAME_ILLEGAL);
        }
        if(category.getIsValid() == null){
            thrown(ErrorCodes.PRODUCT_STATUS_ILLEGAL);
        }

        ProductCategoryExample example = new ProductCategoryExample();
        example.setOrderByClause(" id desc");
        example.setLimit(new Limit(0,1));
        List<ProductCategory> productCategories = productCategoryMapper.selectByExample(example);
        Long id = 10000L;
        if(CollectionUtils.isNotEmpty(productCategories)){
            Long productId = productCategories.get(0).getId();
            if(productId > 10000){
                id = productId + 1;
            }else{
                id = id + productCategories.get(0).getId();
            }
        }
        category.setId(id);
        if(productCategoryMapper.insertSelective(category) < 1){
            thrown(ErrorCodes.UPDATE_ERROR);
        }
    }

    @Override
    public void update(ProductCategory category) throws Exception {
        if(category == null){
            thrown(ErrorCodes.PRODUCT_CATEGORY_NAME_ILLEGAL);
        }
        if(category.getId() == null){
            thrown(ErrorCodes.ID_ILLEGAL);
        }
        if(StringUtils.isBlank(category.getName())){
            thrown(ErrorCodes.PRODUCT_CATEGORY_NAME_ILLEGAL);
        }
        if(category.getIsValid() == null){
            thrown(ErrorCodes.PRODUCT_STATUS_ILLEGAL);
        }
        if(productCategoryMapper.updateByPrimaryKeySelective(category) < 1){
            thrown(ErrorCodes.UPDATE_ERROR);
        }
    }
}
