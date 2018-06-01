package com.nzgreens.console.web.controller;

import com.github.pagehelper.PageInfo;
import com.nzgreens.common.form.console.PageSearchForm;
import com.nzgreens.common.form.console.UserOrderForm;
import com.nzgreens.common.model.ResultModel;
import com.nzgreens.common.model.console.OrdersModel;
import com.nzgreens.common.model.console.UserOrderModel;
import com.nzgreens.console.annotations.Auth;
import com.nzgreens.console.service.IUserOrderService;
import com.nzgreens.dal.user.example.OrderCertificate;
import com.nzgreens.dal.user.example.Orders;
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
 * @Date: Created in 2018/5/2 21:44
 */
@Controller
@RequestMapping("user/order")
public class UserOrderController extends BaseController {
    @Resource
    private IUserOrderService userOrderService;

    @RequestMapping("to-list")
    @Auth("USER_ORDER_MANAGE")
    public String toList() throws Exception{
        return "order/user-order-list";
    }

    @RequestMapping("to-detail")
    @Auth("USER_ORDER_MANAGE")
    public String toDetail(Model model,Long id,String orderNumber) throws Exception{
        model.addAttribute("orderId",id);
        model.addAttribute("orderNumber",orderNumber);
        return "order/user-order-detail";
    }

    @RequestMapping("search-list")
    @ResponseBody
    @Auth("USER_ORDER_MANAGE")
    public ResultModel selectUserOrderForPage(UserOrderForm form) throws Exception{
        ResultModel result = new ResultModel();
        List<UserOrderModel> userOrderModels = userOrderService.selectUserOrderForPage(form);
        PageInfo<UserOrderModel> pageInfo = new PageInfo<>(userOrderModels);
        result.setData(pageInfo);
        return result;
    }

    @RequestMapping("detail/search-list")
    @ResponseBody
    @Auth("USER_ORDER_MANAGE")
    public ResultModel selectUserOrderDetailForPage(String orderNumber, PageSearchForm page) throws Exception{
        ResultModel result = new ResultModel();
        List<OrdersModel> orders = userOrderService.selectOrdersForPage(orderNumber, page);
        PageInfo<OrdersModel> pageInfo = new PageInfo<>(orders);
        result.setData(pageInfo);
        return result;
    }

    @RequestMapping("cert/search-list")
    @ResponseBody
    @Auth("USER_ORDER_MANAGE")
    public ResultModel selectUserOrderCertForPage(String orderNumber, PageSearchForm page) throws Exception{
        ResultModel result = new ResultModel();
        List<OrderCertificate> orderCertificates = userOrderService.selectOrderCertForPage(orderNumber, page);
        PageInfo<OrderCertificate> pageInfo = new PageInfo<>(orderCertificates);
        result.setData(pageInfo);
        return result;
    }

    @RequestMapping("search-detail")
    @ResponseBody
    @Auth("USER_ORDER_MANAGE")
    public ResultModel searchDetail(Long id) throws Exception{
        ResultModel<UserOrderModel> resultModel = new ResultModel<>();
        UserOrderModel products = userOrderService.selectUserOrderDetail(id);
        resultModel.setData(products);
        return resultModel;
    }

    @RequestMapping("update")
    @ResponseBody
    @Auth("USER_ORDER_UPDATE")
    public ResultModel updateUserOrderStatus(Long orderId,Integer status) throws Exception{
        userOrderService.updateOrderStatus(orderId,status);
        return new ResultModel();
    }

    @RequestMapping("update/logisticsNumber")
    @ResponseBody
    @Auth("USER_ORDER_UPDATE")
    public ResultModel updateUserLogisticsNumber(Long orderId,String logisticsNumber) throws Exception{
        userOrderService.updateLogisticsNumber(orderId,logisticsNumber);
        return new ResultModel();
    }

    @RequestMapping("insert/cert")
    @ResponseBody
    @Auth("USER_ORDER_UPDATE")
    public ResultModel insertUserOrderCert(OrderCertificate orderCertificate) throws Exception{
        userOrderService.insertOrderCert(orderCertificate);
        return new ResultModel();
    }

    @RequestMapping("delete/cert")
    @ResponseBody
    @Auth("USER_ORDER_UPDATE")
    public ResultModel deleteUserOrderCert(Long id) throws Exception{
        userOrderService.deleteOrderCert(id);
        return new ResultModel();
    }

    @RequestMapping(value = "upload")
    @ResponseBody
    @Auth("USER_ORDER_UPDATE")
    public ResultModel<String> upload(@RequestParam(value = "file") MultipartFile[] multiFile,String orderNumber) throws Exception{
        ResultModel<String> resultModel = new ResultModel<>();
        resultModel.setData(userOrderService.uploadImg(multiFile,orderNumber));
        return resultModel;
    }
}
