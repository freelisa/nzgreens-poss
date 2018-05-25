package com.nzgreens.common.form.console;

/**
 * @Author:helizheng
 * @Date: Created in 2018/5/19 15:10
 */

public class AgentRebateAuditForm extends PageSearchForm {
    private Long id;

    private String mobile;

    private String orderNumber;

    private Integer type;

    private Integer status;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
