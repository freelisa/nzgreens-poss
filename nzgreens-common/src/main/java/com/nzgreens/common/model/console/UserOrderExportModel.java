package com.nzgreens.common.model.console;

/**
 * @Author:helizheng
 * @Date: Created in 2018/6/3 14:35
 */

public class UserOrderExportModel {
    private Long id;

    private String orderContent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderContent() {
        return orderContent;
    }

    public void setOrderContent(String orderContent) {
        this.orderContent = orderContent;
    }
}
