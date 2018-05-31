package com.nzgreens.common.enums;

/**
 * 订单状态表
 * Created by sylar on 2018/4/6.
 * @author sylar
 */
public enum ProductsAuditStatusEnum implements IEnum {
    FAIL(0,"拒绝"),
    SUCCESS(1,"通过");

    private int value;
    private String name;

    ProductsAuditStatusEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return name;
    }
}
