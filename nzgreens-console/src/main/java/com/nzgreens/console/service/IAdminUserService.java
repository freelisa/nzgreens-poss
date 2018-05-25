package com.nzgreens.console.service;


import com.nzgreens.common.exception.CommonException;
import com.nzgreens.common.form.console.AdminUserAddForm;
import com.nzgreens.common.form.console.AdminUserPasswdUpdateForm;
import com.nzgreens.common.form.console.AdminUserSearchForm;
import com.nzgreens.common.form.console.AdminUserUpdateForm;
import com.nzgreens.common.model.console.AdminUserSearchModel;
import com.nzgreens.dal.console.example.AdminUser;

import java.util.List;

/**
 * Created by fei on 16/10/8.
 */
public interface IAdminUserService {

	void signIn(String username, String password, String ip) throws CommonException;

	void signOut();

	AdminUser getCurrentUser();

	/**
	 * 保存用户
	 *
	 * @param adminUserAddForm
	 */
	int insert(AdminUserAddForm adminUserAddForm);

	/**
	 * 查询用户列表
	 *
	 * @param adminUserSearchForm
	 * @return
	 */
	List<AdminUserSearchModel> selectList(AdminUserSearchForm adminUserSearchForm);

	/**
	 * 查询用户详情
	 *
	 * @return
	 */
	AdminUser selectById(Long userId);

	/**
	 * 根据 id 修改详情
	 *
	 * @return
	 * @pdaram adminUserUpdateForm
	 */
	int udpateById(AdminUserUpdateForm adminUserUpdateForm);

	/**
	 * 根据主键删除用户
	 *
	 * @param id 主键
	 * @return
	 */
	int deleteById(Long id);

	/**
	 * 用户密码修改
	 *
	 * @param passwdUpdateForm
	 */
	void updatePasswd(AdminUserPasswdUpdateForm passwdUpdateForm) throws Exception;

	/**
	 * 重置密码
	 *
	 * @param id 登录用户id
	 */
	void updateResetPasswd(Long id);
}
