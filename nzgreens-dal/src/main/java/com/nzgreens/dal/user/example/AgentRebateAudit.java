package com.nzgreens.dal.user.example;

import java.io.Serializable;
import java.util.Date;

public class AgentRebateAudit implements Serializable {
    private Long id;

    private Long agentUserId;

    private Long userOrderId;

    private Byte type;

    private Long rebatePrice;

    private Long actualRebatePrice;

    private Byte status;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

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

    public Long getUserOrderId() {
        return userOrderId;
    }

    public void setUserOrderId(Long userOrderId) {
        this.userOrderId = userOrderId;
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
}