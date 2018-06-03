package com.nzgreens.common.form.console;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Author:helizheng
 * @Date: Created in 2018/5/2 21:48
 */

public class UserOrderForm extends PageSearchForm {
    private Long userId;

    private String mobile;

    private Long orderNumber;

    private Integer deliveryMode;

    private Integer type;

    private Integer status;

    private Integer agentStatus;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String endTime;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Long getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Long orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Integer getDeliveryMode() {
        return deliveryMode;
    }

    public void setDeliveryMode(Integer deliveryMode) {
        this.deliveryMode = deliveryMode;
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

    public Integer getAgentStatus() {
        return agentStatus;
    }

    public void setAgentStatus(Integer agentStatus) {
        this.agentStatus = agentStatus;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
