package com.nzgreens.console.web.controller;

import com.github.pagehelper.PageInfo;
import com.nzgreens.common.form.console.AgentRebateForm;
import com.nzgreens.common.form.console.PageSearchForm;
import com.nzgreens.common.model.ResultModel;
import com.nzgreens.common.model.console.AgentRebateModel;
import com.nzgreens.console.annotations.Auth;
import com.nzgreens.console.service.IAgentRebateService;
import com.nzgreens.console.service.IProductBrandService;
import com.nzgreens.console.service.IUserService;
import com.nzgreens.dal.user.example.AgentRebate;
import com.nzgreens.dal.user.example.ProductBrand;
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
@RequestMapping("product/brand")
public class ProductBrandController {
    @Resource
    private IProductBrandService productBrandService;

    @RequestMapping("to-list")
    @Auth("PRODUCT_BRAND_MANAGE")
    public String toList() throws Exception{
        return "product/product-brand-list";
    }

    @RequestMapping("search-list")
    @Auth("PRODUCT_BRAND_MANAGE")
    @ResponseBody
    public ResultModel selectForPage(PageSearchForm form) throws Exception{
        ResultModel<PageInfo<ProductBrand>> resultModel = new ResultModel<>();
        List<ProductBrand> agents = productBrandService.selectForPage(form);
        PageInfo<ProductBrand> pageInfo = new PageInfo<>(agents);
        resultModel.setData(pageInfo);
        return resultModel;
    }

    @RequestMapping("search-detail")
    @ResponseBody
    @Auth("PRODUCT_BRAND_MANAGE")
    public ResultModel searchDetai(Long id) throws Exception{
        ResultModel<ProductBrand> resultModel = new ResultModel<>();
        ProductBrand users = productBrandService.selectDetail(id);
        resultModel.setData(users);
        return resultModel;
    }

    @RequestMapping("insert")
    @ResponseBody
    @Auth("PRODUCT_BRAND_UPDATE")
    public ResultModel insert(ProductBrand brand) throws Exception{
        productBrandService.insert(brand);
        return new ResultModel();
    }

    @RequestMapping("update")
    @ResponseBody
    @Auth("PRODUCT_BRAND_UPDATE")
    public ResultModel update(ProductBrand brand) throws Exception{
        productBrandService.update(brand);
        return new ResultModel();
    }
}
