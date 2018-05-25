package com.nzgreens.common.form.console;

/**
 * @Author:helizheng
 * @Date: Created in 2018/4/26 23:41
 */

public class AgentRebateForm extends PageSearchForm {
    private Long userId;

    private String mobile;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
