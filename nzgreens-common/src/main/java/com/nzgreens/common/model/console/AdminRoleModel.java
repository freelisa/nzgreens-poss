package com.nzgreens.common.model.console;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 角色信息Model
 * @author za
 * @version V1.0
 * @date 2016年10月12日11:08:58
 */
public class AdminRoleModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private String permissions;

    private Date createTime;

    private Date updateTime;

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

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public List<Integer> getPermissionIds() {
        return permissionIds;
    }

    public void setPermissionIds(List<Integer> permissionIds) {
        this.permissionIds = permissionIds;
    }
}
