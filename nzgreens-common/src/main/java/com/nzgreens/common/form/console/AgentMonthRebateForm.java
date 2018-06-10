package com.nzgreens.common.form.console;

import java.util.Date;

/**
 * @Author:helizheng
 * @Date: Created in 2018/4/26 23:42
 */

public class AgentMonthRebateForm {
    private Long id;

    private String amount;

    /**
     * 代理月返佣（百分比）
     */
    private Long monthRebate;

    private Date createTime;

    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
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
