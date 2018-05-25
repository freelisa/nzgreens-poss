package com.nzgreens.common.form.console;

import java.util.List;

/**
 * 角色新增form
 * Created by z on 2016/10/10.
 */
public class AdminRoleUpdateForm {
    //角色id
    private Long id;
    //角色名
    private String name;
    //权限组合
    private List<Integer> permissionIds;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
