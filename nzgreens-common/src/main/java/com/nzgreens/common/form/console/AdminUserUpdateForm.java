package com.nzgreens.common.form.console;

import java.io.Serializable;

/**
 * 用户新增form
 * Created by za on 2016/10/11.
 */
public class AdminUserUpdateForm implements Serializable {
    private static final long serialVersionUID = 1L;
    //用户id
    private Long id;
    //渠道id
    private Long channelId;
    //角色id
    private Long roleId;
    //电话号码
    private String mobile;
    //真实姓名
    private String realName;
    //市场代码
    private String market;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

}
