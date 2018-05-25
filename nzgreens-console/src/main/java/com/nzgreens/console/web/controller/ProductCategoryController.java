package com.nzgreens.console.web.controller;

import com.github.pagehelper.PageInfo;
import com.nzgreens.common.form.console.AgentRebateForm;
import com.nzgreens.common.form.console.PageSearchForm;
import com.nzgreens.common.model.ResultModel;
import com.nzgreens.common.model.console.AgentRebateModel;
import com.nzgreens.common.model.console.ProductCategoryModel;
import com.nzgreens.console.annotations.Auth;
import com.nzgreens.console.service.IAgentRebateService;
import com.nzgreens.console.service.IProductCategoryService;
import com.nzgreens.console.service.IProductService;
import com.nzgreens.console.service.IUserService;
import com.nzgreens.dal.user.example.AgentRebate;
import com.nzgreens.dal.user.example.ProductCategory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author:helizheng
 * @Date: Created in 2018/4/25 23:30
 */
@Controller
@RequestMapping("product/category")
public class ProductCategoryController {
    @Resource
    private IProductCategoryService productCategoryService;
    @Resource
    private IProductService productService;

    @RequestMapping("to-list")
    @Auth("PRODUCT_CATEGORY_MANAGE")
    public String toList(Model model) throws Exception{
        model.addAttribute("categoryList", productService.selectProductCategory());
        return "product/product-category-list";
    }

    @RequestMapping("search-list")
    @Auth("PRODUCT_CATEGORY_MANAGE")
    @ResponseBody
    public ResultModel selectForPage(PageSearchForm form) throws Exception{
        ResultModel<PageInfo<ProductCategory>> resultModel = new ResultModel<>();
        List<ProductCategory> agents = productCategoryService.selectForPage(form);
        PageInfo<ProductCategory> pageInfo = new PageInfo<>(agents);
        resultModel.setData(pageInfo);
        return resultModel;
    }

    @RequestMapping("search-detail")
    @ResponseBody
    @Auth("PRODUCT_CATEGORY_MANAGE")
    public ResultModel searchDetai(Long id) throws Exception{
        ResultModel<ProductCategoryModel> resultModel = new ResultModel<>();
        ProductCategoryModel users = productCategoryService.selectDetail(id);
        resultModel.setData(users);
        return resultModel;
    }

    @RequestMapping("insert")
    @ResponseBody
    @Auth("PRODUCT_CATEGORY_UPDATE")
    public ResultModel insert(ProductCategory category) throws Exception{
        productCategoryService.insert(category);
        return new ResultModel();
    }

    @RequestMapping("update")
    @ResponseBody
    @Auth("PRODUCT_CATEGORY_UPDATE")
    public ResultModel update(ProductCategory category) throws Exception{
        productCategoryService.update(category);
        return new ResultModel();
    }
}
