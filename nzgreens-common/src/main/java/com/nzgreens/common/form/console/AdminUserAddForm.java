package com.nzgreens.common.form.console;

import java.io.Serializable;

/**
 * 用户新增form
 * Created by za on 2016/10/11.
 */
public class AdminUserAddForm implements Serializable {
    private static final long serialVersionUID = 1L;
    //用户名
    private String username;
    //密码
    private String password;
    //确认密码
    private String confirmpPassword;
    //渠道id
    private Long channelId;
    //角色id
    private Long roleId;
    //电话号码
    private String mobile;
    //真实姓名
    private String realName;
    //状态
    private Integer status;
    //应用市场
    private String market;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmpPassword() {
        return confirmpPassword;
    }

    public void setConfirmpPassword(String confirmpPassword) {
        this.confirmpPassword = confirmpPassword;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }
}
