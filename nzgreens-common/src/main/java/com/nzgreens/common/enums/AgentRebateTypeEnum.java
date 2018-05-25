package com.nzgreens.common.enums;

/**
 * 代理返佣类型枚举
 * Created by sylar on 2018/4/6.
 * @author sylar
 */
public enum AgentRebateTypeEnum implements IEnum {
    _ORDER(1,"订单返佣"),
    _MONTH(2,"月返佣"),;

    private int value;
    private String name;

    AgentRebateTypeEnum(int value, String name) {
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

    public static String getName(Integer value){
        for(AgentRebateTypeEnum enums :  AgentRebateTypeEnum.values()){
            if(enums.value == value){
                return enums.name;
            }
        }
        return null;
    }
}
