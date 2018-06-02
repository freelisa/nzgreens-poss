package com.nzgreens.common.enums;

/**
 * 订单状态表
 * Created by sylar on 2018/4/6.
 * @author sylar
 */
public enum OrderStatusEnum implements IEnum {
    PENDING(0,"未处理"),
    SUCCESS(1,"已拒绝");

    private int value;
    private String name;

    OrderStatusEnum(int value, String name) {
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

    public String getText(){
        return name;
    }
}
