package com.nzgreens.common.model.console;

import java.io.Serializable;
import java.util.Date;

public class ProductsPriceChangeModel implements Serializable {
    private Long id;

    private Long productId;

    private String title;

    private String image;

    private Long oldSellPrice;

    private Long newSellPrice;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getOldSellPrice() {
        return oldSellPrice;
    }

    public void setOldSellPrice(Long oldSellPrice) {
        this.oldSellPrice = oldSellPrice;
    }

    public Long getNewSellPrice() {
        return newSellPrice;
    }

    public void setNewSellPrice(Long newSellPrice) {
        this.newSellPrice = newSellPrice;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}