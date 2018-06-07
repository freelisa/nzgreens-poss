package com.nzgreens.console.web.controller;

import com.github.pagehelper.PageInfo;
import com.nzgreens.common.enums.IsValidEnum;
import com.nzgreens.common.form.console.PageSearchForm;
import com.nzgreens.common.form.console.ProductAddForm;
import com.nzgreens.common.form.console.ProductForm;
import com.nzgreens.common.model.ResultModel;
import com.nzgreens.common.model.console.ProductsPriceChangeModel;
import com.nzgreens.common.utils.CurrencyUtil;
import com.nzgreens.console.annotations.Auth;
import com.nzgreens.console.service.IProductService;
import com.nzgreens.dal.user.example.Products;
import com.nzgreens.dal.user.example.ProductsPriceChange;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author:helizheng
 * @Date: Created in 2018/4/21 15:21
 */
@Controller
@RequestMapping("product")
public class ProductController extends BaseController {
    @Resource
    private IProductService productService;

    @RequestMapping("to-list")
    @Auth("PRODUCT_MANAGE")
    public String toList(Model model) throws Exception{
        model.addAttribute("categoryList", productService.selectProductCategory());
        model.addAttribute("brandList", productService.selectProductBrand());
        return "product/product-list";
    }

    @RequestMapping("search-list")
    @ResponseBody
    @Auth("PRODUCT_MANAGE")
    public ResultModel searchList(ProductForm form) throws Exception{
        ResultModel<PageInfo<Products>> resultModel = new ResultModel<>();
        form.setIsValid(IsValidEnum.EFFECTIVE.getValue());
        List<Products> products = productService.selectProductForPage(form);
        PageInfo<Products> pageInfo = new PageInfo<>(products);
        resultModel.setData(pageInfo);
        return resultModel;
    }

    @RequestMapping("audit/to-list")
    @Auth("PRODUCT_AUDIT_MANAGE")
    public String toAuditList(Model model) throws Exception{
        model.addAttribute("categoryList", productService.selectProductCategory());
        model.addAttribute("brandList", productService.selectProductBrand());
        return "product/product-audit-list";
    }

    @RequestMapping("audit/search-list")
    @ResponseBody
    @Auth("PRODUCT_AUDIT_MANAGE")
    public ResultModel searchAuditList(ProductForm form) throws Exception{
        ResultModel<PageInfo<Products>> resultModel = new ResultModel<>();
        form.setIsValid(IsValidEnum.INVALID.getValue());
        List<Products> products = productService.selectProductForPage(form);
        PageInfo<Products> pageInfo = new PageInfo<>(products);
        resultModel.setData(pageInfo);
        return resultModel;
    }

    @RequestMapping("change/to-list")
    @Auth("PRODUCT_PRICE_CHANGE_MANAGE")
    public String toChangeList(Model model) throws Exception{
        return "product/product-price-change-list";
    }

    @RequestMapping("change/search-list")
    @ResponseBody
    @Auth("PRODUCT_PRICE_CHANGE_MANAGE")
    public ResultModel searchChangeList(PageSearchForm form) throws Exception{
        ResultModel<PageInfo<ProductsPriceChangeModel>> resultModel = new ResultModel<>();
        List<ProductsPriceChangeModel> products = productService.selectProductChangeForPage(form);
        PageInfo<ProductsPriceChangeModel> pageInfo = new PageInfo<>(products);
        resultModel.setData(pageInfo);
        return resultModel;
    }

    @RequestMapping("change/update")
    @ResponseBody
    @Auth("PRODUCT_PRICE_CHANGE_MANAGE")
    public ResultModel update(Long id) throws Exception{
        productService.updateProductChangeForPage(id);
        return new ResultModel();
    }

    @RequestMapping("change/count")
    @ResponseBody
    @Auth("PRODUCT_PRICE_CHANGE_MANAGE")
    public ResultModel searchChangeCount() throws Exception{
        ResultModel resultModel = new ResultModel<>();
        resultModel.setData( productService.selectProductChangeCount());
        return resultModel;
    }

    @RequestMapping("search-detail")
    @ResponseBody
    @Auth("PRODUCT_MANAGE")
    public ResultModel searchDetail(Long id) throws Exception{
        ResultModel<ProductAddForm> resultModel = new ResultModel<>();
        ProductAddForm products = productService.selectProductDetail(id);
        resultModel.setData(products);
        return resultModel;
    }

    @RequestMapping("insert")
    @ResponseBody
    @Auth("PRODUCT_UPDATE")
    public ResultModel insert(ProductAddForm form) throws Exception{
        productService.insert(form);
        return new ResultModel();
    }

    @RequestMapping("update")
    @ResponseBody
    @Auth("PRODUCT_UPDATE")
    public ResultModel update(ProductAddForm form) throws Exception{
        productService.update(form);
        return new ResultModel();
    }

    @RequestMapping("delete")
    @ResponseBody
    @Auth("PRODUCT_UPDATE")
    public ResultModel delete(Long id) throws Exception{
        productService.delete(id);
        return new ResultModel();
    }

    @RequestMapping(value = "upload")
    @ResponseBody
    @Auth("PRODUCT_UPDATE")
    public ResultModel<String> upload(@RequestParam(value = "file") MultipartFile multiFile) throws Exception{
        ResultModel<String> resultModel = new ResultModel<>();
        resultModel.setData(productService.uploadImg(multiFile));
        return resultModel;
    }
}
