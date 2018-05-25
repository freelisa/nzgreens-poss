package com.nzgreens.common.model.console;

/**
 * @Author:helizheng
 * @Date: Created in 2018/5/19 23:52
 */

public class UserOrderPriceSumModel {
    private Long userId;

    private Long price;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }
}
