package com.nzgreens.common.form.console;

import java.io.Serializable;

/**
 * 用户查询form
 * Created by za on 2016/10/11.
 */
public class AdminUserSearchForm extends PageSearchForm implements Serializable {
    private static final long serialVersionUID = 1L;
    //用户名
    private String keyword;
    //角色id
    private Long roleId;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
