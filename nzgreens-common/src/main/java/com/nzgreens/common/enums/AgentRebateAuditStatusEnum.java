package com.nzgreens.common.enums;

/**
 * 代理返佣审核状态枚举类
 * Created by sylar on 2018/4/6.
 * @author sylar
 */
public enum AgentRebateAuditStatusEnum implements IEnum {
    _PENDING(0,"未审核"),
    _DONE(1,"已通过"),
    _REFUSED(2,"已拒绝");

    private int value;
    private String name;

    AgentRebateAuditStatusEnum(int value, String name) {
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
        for(AgentRebateAuditStatusEnum enums :  AgentRebateAuditStatusEnum.values()){
            if(enums.value == value){
                return enums.name;
            }
        }
        return null;
    }
}
