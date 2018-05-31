package com.nzgreens.common.enums;

/**
 * 价格变动消息状态
 */
public enum ProductsPriceChangeStatusEnum implements IEnum {
    NOT_READ(0,"未读"),
    READED(1,"已读");

    private int value;
    private String name;

    ProductsPriceChangeStatusEnum(int value, String name) {
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
