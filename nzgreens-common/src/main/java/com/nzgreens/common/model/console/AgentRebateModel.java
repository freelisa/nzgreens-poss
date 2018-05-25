package com.nzgreens.common.model.console;

import java.util.Date;

/**
 * @Author:helizheng
 * @Date: Created in 2018/4/26 23:42
 */

public class AgentRebateModel {
    private Long id;

    private Long userId;

    private String mobile;

    /**
     * 订单返佣(百分比 80 = 80%)
     */
    private Long orderRebate;

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

    public Long getOrderRebate() {
        return orderRebate;
    }

    public void setOrderRebate(Long orderRebate) {
        this.orderRebate = orderRebate;
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
