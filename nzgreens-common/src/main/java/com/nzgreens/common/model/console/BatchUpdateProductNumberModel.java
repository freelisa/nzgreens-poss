package com.nzgreens.common.model.console;

/**
 * @Author:helizheng
 * @Date: Created in 2018/5/29 22:06
 */

public class BatchUpdateProductNumberModel {
    private Integer number;

    private Long productId;

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
