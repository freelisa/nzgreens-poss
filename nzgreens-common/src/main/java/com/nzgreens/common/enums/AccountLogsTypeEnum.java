package com.nzgreens.common.enums;

/**
 * 账户流水类型
 * Created by sylar on 2018/4/6.
 * @author sylar
 */
public enum AccountLogsTypeEnum implements IEnum{
    _CHARGE(1,"用户/代理充值"),
    _ORDER_REBATE(2,"订单代理返佣"),
    _ORDER(3,"用户/代理下单"),
    _REFUND(4,"用户/代理退款"),
    _MONTH_REBATE(5,"代理月返佣"),
    _WITHDRAW(6,"提现"),
    _ORDER_REFUSED(7,"订单拒绝"),;

    private int value;
    private String name;

    AccountLogsTypeEnum(int value, String name) {
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
