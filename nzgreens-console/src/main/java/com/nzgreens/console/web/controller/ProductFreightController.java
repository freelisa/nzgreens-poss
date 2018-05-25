package com.nzgreens.console.web.controller;

import com.github.pagehelper.PageInfo;
import com.nzgreens.common.form.console.AgentRebateForm;
import com.nzgreens.common.form.console.PageSearchForm;
import com.nzgreens.common.form.console.ProductFreightForm;
import com.nzgreens.common.model.ResultModel;
import com.nzgreens.common.model.console.AgentRebateModel;
import com.nzgreens.console.annotations.Auth;
import com.nzgreens.console.service.IAgentRebateService;
import com.nzgreens.console.service.IProductFreightService;
import com.nzgreens.console.service.IUserService;
import com.nzgreens.dal.user.example.AgentRebate;
import com.nzgreens.dal.user.example.ProductFreight;
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
@RequestMapping("product/freight")
public class ProductFreightController {
    @Resource
    private IProductFreightService productFreightService;

    @RequestMapping("to-list")
    @Auth("PRODUCT_FREIGHT_MANAGE")
    public String toList() throws Exception{
        return "product/product-freight-list";
    }

    @RequestMapping("search-list")
    @Auth("PRODUCT_FREIGHT_MANAGE")
    @ResponseBody
    public ResultModel selectForPage(PageSearchForm form) throws Exception{
        ResultModel<PageInfo<ProductFreight>> resultModel = new ResultModel<>();
        List<ProductFreight> agents = productFreightService.selectForPage(form);
        PageInfo<ProductFreight> pageInfo = new PageInfo<>(agents);
        resultModel.setData(pageInfo);
        return resultModel;
    }

    @RequestMapping("search-detail")
    @ResponseBody
    @Auth("PRODUCT_FREIGHT_MANAGE")
    public ResultModel searchDetai(Long id) throws Exception{
        ResultModel<ProductFreight> resultModel = new ResultModel<>();
        ProductFreight users = productFreightService.selectDetail(id);
        resultModel.setData(users);
        return resultModel;
    }

    @RequestMapping("insert")
    @ResponseBody
    @Auth("PRODUCT_FREIGHT_UPDATE")
    public ResultModel insert(ProductFreightForm agentRebate) throws Exception{
        productFreightService.insert(agentRebate);
        return new ResultModel();
    }

    @RequestMapping("update")
    @ResponseBody
    @Auth("PRODUCT_FREIGHT_UPDATE")
    public ResultModel update(ProductFreightForm agentRebate) throws Exception{
        productFreightService.update(agentRebate);
        return new ResultModel();
    }

    @RequestMapping("delete")
    @ResponseBody
    @Auth("PRODUCT_FREIGHT_UPDATE")
    public ResultModel delete(Long id) throws Exception{
        productFreightService.delete(id);
        return new ResultModel();
    }
}
