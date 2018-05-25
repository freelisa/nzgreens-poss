package com.nzgreens.common.enums;

/**
 * 用户订单类型枚举
 * Created by sylar on 2018/4/6.
 * @author sylar
 */
public enum UserOrderTypeEnum implements IEnum {
    _AGENT(1,"代理处理订单"),
    _SYSTEM(2,"系统处理订单"),;

    private int value;
    private String name;

    UserOrderTypeEnum(int value, String name) {
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
