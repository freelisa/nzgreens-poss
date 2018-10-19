package com.nzgreens.console.web.controller;

import com.github.pagehelper.PageInfo;
import com.nzgreens.common.form.console.PageSearchForm;
import com.nzgreens.common.form.console.ProductAddForm;
import com.nzgreens.common.form.console.ProductForm;
import com.nzgreens.common.model.ResultModel;
import com.nzgreens.console.annotations.Auth;
import com.nzgreens.console.service.IProductCrawlService;
import com.nzgreens.console.service.IProductService;
import com.nzgreens.console.task.CrawlProductsTask;
import com.nzgreens.console.task.SettingProductTask;
import com.nzgreens.dal.user.example.Products;
import com.nzgreens.dal.user.example.ProductsCrawl;
import com.nzgreens.dal.user.example.ProductsCrawlExample;
import com.nzgreens.dal.user.mapper.ProductsCrawlMapper;
import org.apache.commons.collections.CollectionUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("product/crawl")
public class ProductCrawlController extends BaseController {
    @Resource
    private IProductCrawlService productCrawlService;
    @Resource
    private IProductService productService;
    @Autowired
    private ProductsCrawlMapper productsCrawlMapper;
    @Autowired
    private SettingProductTask settingProductTask;

    @RequestMapping("to-list")
    @Auth("PRODUCT_CRAWL_MANAGE")
    public String toList(Model model) throws Exception{
        model.addAttribute("categoryList", productService.selectProductCategory());
        model.addAttribute("brandList", productService.selectProductBrand());
        return "product/product-crawl-audit-list";
    }

    @RequestMapping("search-list")
    @ResponseBody
    @Auth("PRODUCT_CRAWL_MANAGE")
    public ResultModel searchList(PageSearchForm form) throws Exception{
        ResultModel<PageInfo<ProductsCrawl>> resultModel = new ResultModel<>();
        List<ProductsCrawl> products = productCrawlService.selectProductForPage(form);
        PageInfo<ProductsCrawl> pageInfo = new PageInfo<>(products);
        resultModel.setData(pageInfo);
        return resultModel;
    }

    @RequestMapping("update")
    @ResponseBody
    @Auth("PRODUCT_CRAWL_UPDATE")
    public ResultModel update(Long productId,Integer status) throws Exception{
        productCrawlService.update(productId,status);
        return new ResultModel();
    }

    @RequestMapping("insert")
    @ResponseBody
    @Auth("PRODUCT_UPDATE")
    public ResultModel insert(Long startProductId,Long endProductId) throws Exception{
        if (startProductId != null && endProductId == null) {
            endProductId = startProductId;
        }
        if (startProductId == null && endProductId != null) {
            startProductId = endProductId;
        }
        if (endProductId != null && startProductId != null && endProductId.compareTo(startProductId) >= 0) {
            Connection connection = CrawlProductsTask.getConnection();
            for (long i = startProductId; i <= endProductId ; i++) {
                ProductsCrawlExample productsCrawlExample = new ProductsCrawlExample();
                productsCrawlExample.createCriteria().andReptileProductIdEqualTo(startProductId+"");
                List<ProductsCrawl> list = productsCrawlMapper.selectByExample(productsCrawlExample);
                if (CollectionUtils.isEmpty(list)) {
                    productCrawlService.loadProduct("http://gelin.nz/index.php?route=product/product&product_id="+i, connection);
                }
            }
        }
        settingProductTask.settingProducts();
        return new ResultModel();
    }
}
