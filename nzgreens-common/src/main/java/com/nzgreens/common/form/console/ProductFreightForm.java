package com.nzgreens.common.form.console;

public class ProductFreightForm {
    private Long id;

    private Long productWeight;

    private String freight;

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

    public String getFreight() {
        return freight;
    }

    public void setFreight(String freight) {
        this.freight = freight;
    }
}