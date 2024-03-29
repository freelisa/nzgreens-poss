package com.nzgreens.dal.user.example;

import java.io.Serializable;
import java.util.Date;

public class AgentMonthRebate implements Serializable {
    private Long id;

    private Long amount;

    private Long monthRebate;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getMonthRebate() {
        return monthRebate;
    }

    public void setMonthRebate(Long monthRebate) {
        this.monthRebate = monthRebate;
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