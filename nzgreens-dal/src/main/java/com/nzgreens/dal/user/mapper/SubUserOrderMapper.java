package com.nzgreens.dal.user.mapper;

import com.nzgreens.common.form.console.UserOrderExportForm;
import com.nzgreens.common.form.console.UserOrderForm;
import com.nzgreens.common.model.console.OrdersModel;
import com.nzgreens.common.model.console.UserOrderModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SubUserOrderMapper {
    List<UserOrderModel> selectUserOrderForPage(UserOrderForm form) throws Exception;

    List<UserOrderModel> selectUserOrderExportForPage(UserOrderExportForm form) throws Exception;

    List<OrdersModel> selectOrdersForPage(@Param("orderNumber") String orderNumber) throws Exception;
}