package com.nzgreens.common.enums;

/**
 * 收货方式
 * Created by sylar on 2018/4/6.
 * @author sylar
 */
public enum DeliveryModeEnum implements IEnum {
    _SELF(1,"自收/直接购买"),
    _DELIVERY(2,"代收/合并订单");

    private int value;
    private String name;

    DeliveryModeEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    @Override
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
