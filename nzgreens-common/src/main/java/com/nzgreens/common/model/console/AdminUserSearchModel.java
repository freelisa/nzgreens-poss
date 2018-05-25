package com.nzgreens.common.model.console;

import java.io.Serializable;
import java.util.Date;

/**
 * 后台用户查询model
 * Created by z on 2016/10/13.
 */
public class AdminUserSearchModel implements Serializable {
    private static final long serialVersionUID = 1L;

    //主键id
    private Long id;
    //用户名
    private String username;
    //角色名
    private String roleName;
    //手机号码
    private String mobile;
    //真实姓名
    private String realName;
    //创建时间
    private Date createTime;
    //状态
    private Integer status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
