package com.nzgreens.dal.user.example;

import java.io.Serializable;
import java.util.Date;

public class ProductFreight implements Serializable {
    private Long id;

    private Long productWeight;

    private Long freight;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductWeight() {
        return productWeight;
    }

    public void setProductWeight(Long productWeight) {
        this.productWeight = productWeight;
    }

    public Long getFreight() {
        return freight;
    }

    public void setFreight(Long freight) {
        this.freight = freight;
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