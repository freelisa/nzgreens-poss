package com.nzgreens.console.web.controller;

import com.github.pagehelper.PageInfo;
import com.nzgreens.common.form.console.PageSearchForm;
import com.nzgreens.common.form.console.UserOrderExportForm;
import com.nzgreens.common.form.console.UserOrderForm;
import com.nzgreens.common.model.ResultModel;
import com.nzgreens.common.model.console.OrdersModel;
import com.nzgreens.common.model.console.UserOrderExportModel;
import com.nzgreens.common.model.console.UserOrderModel;
import com.nzgreens.console.annotations.Auth;
import com.nzgreens.console.service.IAgentRebateAuditService;
import com.nzgreens.console.service.IAgentRebateService;
import com.nzgreens.console.service.IUserOrderService;
import com.nzgreens.console.service.impl.AgentRebateAuditService;
import com.nzgreens.console.util.exceltool.ExcelUtils;
import com.nzgreens.console.util.exceltool.JsGridReportBase;
import com.nzgreens.console.util.exceltool.TableData;
import com.nzgreens.dal.user.example.AgentRebateAudit;
import com.nzgreens.dal.user.example.AgentRebateAuditExample;
import com.nzgreens.dal.user.example.OrderCertificate;
import com.nzgreens.dal.user.example.Orders;
import com.nzgreens.dal.user.mapper.AgentRebateAuditMapper;
import com.nzgreens.dal.user.mapper.AgentRebateMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
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
    @Resource
    private IAgentRebateAuditService agentRebateAuditService;
    @Resource
    private AgentRebateAuditMapper agentRebateAuditMapper;
    @RequestMapping("to-list")
    @Auth("USER_ORDER_MANAGE")
    public String toList() throws Exception{
        return "order/user-order-list";
    }

    @RequestMapping("to-detail")
    @Auth("USER_ORDER_UPDATE")
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
    @Auth("USER_ORDER_MANAGE_UPDATE_ORDER")
    public ResultModel updateUserOrderStatus(Long orderId,Integer status) throws Exception{
        userOrderService.updateOrderStatus(orderId,status);
        return new ResultModel();
    }
    @RequestMapping("onceAudit")
    @ResponseBody
    @Auth("USER_ORDER_MANAGE_UPDATE_ORDER")
    public ResultModel onceAudit(Long orderId) throws Exception{
        try {
            userOrderService.updateOrderStatus(orderId,2);
        } catch (Exception e) {

        }
        AgentRebateAuditExample example = new AgentRebateAuditExample();
        example.createCriteria().andUserOrderIdEqualTo(orderId);
        try {
            AgentRebateAudit agentRebateAudit = agentRebateAuditMapper.selectByExample(example).get(0);
            if (agentRebateAudit == null) {
                ResultModel resultModel = new ResultModel();
                resultModel.setSuccess(false);
                resultModel.setErrorInfo("系统一键处理失败，请刷新页面后手动处理！");
                return resultModel;
            }
            if (agentRebateAudit.getStatus() != 0) {
                throw new RuntimeException();
            }
            agentRebateAuditService.updateAgentRebateAuditStatus(agentRebateAudit.getId(), 1,
                    String.format("%.2f", Double.valueOf(agentRebateAudit.getRebatePrice())/100), "系统自动确认");
        } catch (Exception e) {
            throw e;
        }
        return new ResultModel();
    }
    @RequestMapping("update/logisticsNumber")
    @ResponseBody
    @Auth("USER_ORDER_MANAGE_UPDATE_ORDER_NUMBER")
    public ResultModel updateUserLogisticsNumber(Long orderId,String logisticsNumber) throws Exception{
        userOrderService.updateLogisticsNumber(orderId,logisticsNumber);
        return new ResultModel();
    }

    @RequestMapping("insert/cert")
    @ResponseBody
    @Auth("USER_ORDER_MANAGE_UPDATE_CERTIFICATE")
    public ResultModel insertUserOrderCert(OrderCertificate orderCertificate) throws Exception{
        userOrderService.insertOrderCert(orderCertificate);
        return new ResultModel();
    }

    @RequestMapping("delete/cert")
    @ResponseBody
    @Auth("USER_ORDER_MANAGE_UPDATE_CERTIFICATE")
    public ResultModel deleteUserOrderCert(Long id) throws Exception{
        userOrderService.deleteOrderCert(id);
        return new ResultModel();
    }

    @RequestMapping(value = "upload")
    @ResponseBody
    @Auth("USER_ORDER_MANAGE_UPDATE_CERTIFICATE")
    public ResultModel<String> upload(@RequestParam(value = "file") MultipartFile[] multiFile,String orderNumber) throws Exception{
        ResultModel<String> resultModel = new ResultModel<>();
        resultModel.setData(userOrderService.uploadImg(multiFile,orderNumber));
        return resultModel;
    }

    @RequestMapping("export")
    @ResponseBody
    @Auth("USER_ORDER_EXPORT_MANAGE")
    public void selectUserOrderExportExcel(HttpServletRequest request, HttpServletResponse response,UserOrderExportForm form) throws Exception{
        String[] headers = {"序号","金额","账号","订单内容"};
        List<UserOrderExportModel> exportModels = userOrderService.selectUserOrderExportExcelV2(form);

        String[] fields = new String[]{"id","orderPrice","mobile","orderContent"};
        TableData td = ExcelUtils.createTableData(exportModels, ExcelUtils.createTableHeader(headers), fields);
        JsGridReportBase report = new JsGridReportBase(request, response);
        report.exportToExcel("订单列表", "admin", td,null);
    }
}
