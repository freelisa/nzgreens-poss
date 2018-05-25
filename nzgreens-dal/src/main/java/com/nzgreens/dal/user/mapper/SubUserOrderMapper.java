package com.nzgreens.dal.user.mapper;

import com.nzgreens.common.form.console.UserOrderForm;
import com.nzgreens.common.model.console.UserOrderModel;

import java.util.List;

public interface SubUserOrderMapper {
    List<UserOrderModel> selectUserOrderForPage(UserOrderForm form) throws Exception;
}