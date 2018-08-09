package com.nzgreens.common.model.console;

/**
 * @Author:helizheng
 * @Date: Created in 2018/6/3 14:35
 */

public class UserOrderExportModel {
    private Long id;

    private Double orderPrice;

    private String mobile;

    private String orderContent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(Double orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getOrderContent() {
        return orderContent;
    }

    public void setOrderContent(String orderContent) {
        this.orderContent = orderContent;
    }
}
