package com.nzgreens.common.enums;

/**
 * 用户状态枚举类
 * Created by sylar on 2018/4/6.
 * @author sylar
 */
public enum IsValidEnum implements IEnum {
    INVALID(0,"无效"),
    EFFECTIVE(1,"有效");

    private int value;
    private String name;

    IsValidEnum(int value, String name) {
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
