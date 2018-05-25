package com.nzgreens.console.service;


import com.nzgreens.common.form.console.AdminRoleAddForm;
import com.nzgreens.common.form.console.AdminRoleSearchForm;
import com.nzgreens.common.form.console.AdminRoleUpdateForm;
import com.nzgreens.dal.console.example.AdminRole;

import java.util.List;

/**
 * Created by z on 2016/10/10.
 */
public interface IAdminRoleService {

    /**
     * 根据条件查询角色列表
     * @return
     */
    List<AdminRole> selectList(AdminRoleSearchForm adminRoleSearchForm);

    /**
     * 保存角色
     * @param adminRoleAddForm
     */
    Integer insert(AdminRoleAddForm adminRoleAddForm);

    /**
     * 查询所有角色
     * @return
     */
    List<AdminRole> selectAll();

    /**
     * 根据id查询角色详情
     * @param roleId 角色id
     * @return 角色对象
     */
    AdminRole selectById(Long roleId);

    /**
     * 根据ID修改角色信息
     * @param adminRoleUpdateForm 对应表单对象
     * @return 修改影响的行数
     */
    Integer updateById(AdminRoleUpdateForm adminRoleUpdateForm);

    /**
     * 删除角色id
     * @param roleId 角色id
     * @return 删除角色行数
     */
    Integer deleteById(Long roleId);

    /**
     * 检查角色名是否存在
     * @return
     */
    boolean selectByCheckRoleName(String roleName);
}
