package com.nzgreens.common.enums;

/**
 * 用户订单状态枚举类
 * Created by sylar on 2018/4/6.
 * @author sylar
 */
public enum UserOrderStatusEnum implements IEnum {
    _REFUSED(-1,"订单已拒绝"),
    _PENDING(0,"待处理"),
    _PROCESSED(1,"已处理"),
    _DONE(2,"已上传凭证");

    private int value;
    private String name;

    UserOrderStatusEnum(int value, String name) {
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
