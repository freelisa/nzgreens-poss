package com.nzgreens.dal.user.example;

import java.util.Date;

/**
 * @author sylar
 * @Description:
 * @date 2019/8/18 21:32
 */
public class UserOrderCount {
    private Long userId;
    private Long orderCount;
    private Date createTime;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Long orderCount) {
        this.orderCount = orderCount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
