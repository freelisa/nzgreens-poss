package com.nzgreens.common.enums;

/**
 * 订单状态表
 * Created by sylar on 2018/4/6.
 * @author sylar
 */
public enum OrderHandleStatusEnum implements IEnum {
    WAIT(0,"未处理"),
    SUCCESS(1,"已处理");

    private int value;
    private String name;

    OrderHandleStatusEnum(int value, String name) {
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
