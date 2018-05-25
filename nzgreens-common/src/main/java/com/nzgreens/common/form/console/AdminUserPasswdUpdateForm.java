package com.nzgreens.common.form.console;

import java.io.Serializable;

/**
 * 用户修改密码form
 * Created by za on 2016/10/11.
 */
public class AdminUserPasswdUpdateForm implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    //原密码
    private String oldPassword;
    //新密码
    private String newPassword;
    //确认密码
    private String newPasswordConfirm;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPasswordConfirm() {
        return newPasswordConfirm;
    }

    public void setNewPasswordConfirm(String newPasswordConfirm) {
        this.newPasswordConfirm = newPasswordConfirm;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
