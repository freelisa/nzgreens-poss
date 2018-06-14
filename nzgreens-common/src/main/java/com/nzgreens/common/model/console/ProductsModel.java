package com.nzgreens.common.model.console;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ProductsModel implements Serializable {
    private Long id;

    private String title;

    private String sellingPrice;

    private Long gelinProductId;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(String sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public Long getGelinProductId() {
        return gelinProductId;
    }

    public void setGelinProductId(Long gelinProductId) {
        this.gelinProductId = gelinProductId;
    }
}