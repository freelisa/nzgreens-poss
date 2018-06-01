package com.nzgreens.common.form.console;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Author:helizheng
 * @Date: Created in 2018/4/25 23:46
 */

public class UserForm extends PageSearchForm {
    private Long userId;

    private String mobile;

    private Integer type;

    private Integer isValid;

    private String startTime;

    private String endTime;

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getIsValid() {
        return isValid;
    }

    public void setIsValid(Integer isValid) {
        this.isValid = isValid;
    }
}
