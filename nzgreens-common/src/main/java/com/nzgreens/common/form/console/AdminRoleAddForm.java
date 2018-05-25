package com.nzgreens.common.form.console;

import java.io.Serializable;
import java.util.List;

/**
 * 角色新增form
 * Created by z on 2016/10/10.
 */
public class AdminRoleAddForm implements Serializable {
    //角色名
    private String name;
    //权限组合
    private List<Integer> permissionIds;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getPermissionIds() {
        return permissionIds;
    }

    public void setPermissionIds(List<Integer> permissionIds) {
        this.permissionIds = permissionIds;
    }
}
