package com.nzgreens.common.enums;

/**
 * 用户类型枚举类
 * Created by sylar on 2018/4/6.
 * @author sylar
 */
public enum UserTypeEnum implements IEnum {
    _SYSTEM(0,"系统"),
    _USER(1,"用户"),
    _AGENT(2,"代理"),;

    private int value;
    private String name;

    UserTypeEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getText() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
