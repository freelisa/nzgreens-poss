package com.nzgreens.common.model.console;

import java.util.Date;

/**
 * @Author:helizheng
 * @Date: Created in 2018/5/19 18:25
 */

public class AgentRebateAuditModel {
    private Long id;

    private Long agentUserId;

    private String mobile;

    private Long userOrderId;

    private Long orderNumber;

    private Byte type;

    private String typeDesc;

    private Long rebatePrice;

    private Long actualRebatePrice;

    private Byte status;

    private Date createTime;

    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAgentUserId() {
        return agentUserId;
    }

    public void setAgentUserId(Long agentUserId) {
        this.agentUserId = agentUserId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Long getUserOrderId() {
        return userOrderId;
    }

    public void setUserOrderId(Long userOrderId) {
        this.userOrderId = userOrderId;
    }

    public Long getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Long orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Long getRebatePrice() {
        return rebatePrice;
    }

    public void setRebatePrice(Long rebatePrice) {
        this.rebatePrice = rebatePrice;
    }

    public Long getActualRebatePrice() {
        return actualRebatePrice;
    }

    public void setActualRebatePrice(Long actualRebatePrice) {
        this.actualRebatePrice = actualRebatePrice;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }
}
