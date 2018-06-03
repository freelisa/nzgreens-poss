package com.nzgreens.console.service;

import com.nzgreens.common.form.console.PageSearchForm;
import com.nzgreens.common.form.console.UserOrderExportForm;
import com.nzgreens.common.form.console.UserOrderForm;
import com.nzgreens.common.model.console.OrdersModel;
import com.nzgreens.common.model.console.UserOrderExportModel;
import com.nzgreens.common.model.console.UserOrderModel;
import com.nzgreens.dal.user.example.OrderCertificate;
import com.nzgreens.dal.user.example.Orders;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author:helizheng
 * @Date: Created in 2018/5/2 21:47
 */

public interface IUserOrderService {
    List<UserOrderModel> selectUserOrderForPage(UserOrderForm form) throws Exception;

    List<UserOrderExportModel> selectUserOrderExportExcel(UserOrderExportForm form) throws Exception;

    List<UserOrderExportModel> selectUserOrderExportExcelV2(UserOrderExportForm form) throws Exception;

    List<OrdersModel> selectOrdersForPage(String orderNumber, PageSearchForm page) throws Exception;

    List<OrderCertificate> selectOrderCertForPage(String orderNumber,PageSearchForm page) throws Exception;

    UserOrderModel selectUserOrderDetail(Long id) throws Exception;

    void insertOrderCert(OrderCertificate orderCertificate) throws Exception;

    void updateOrderStatus(Long orderId,Integer status) throws Exception;

    void updateLogisticsNumber(Long orderId,String logisticsNumber) throws Exception;

    void deleteOrderCert(Long id) throws Exception;

    String uploadImg(MultipartFile[] multiFile,String orderNumer) throws Exception;
}
