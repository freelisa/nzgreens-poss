package com.nzgreens.console.web.controller;

import com.github.pagehelper.PageInfo;
import com.nzgreens.common.form.console.AdminUserAddForm;
import com.nzgreens.common.form.console.AdminUserPasswdUpdateForm;
import com.nzgreens.common.form.console.AdminUserSearchForm;
import com.nzgreens.common.form.console.AdminUserUpdateForm;
import com.nzgreens.common.model.ResultModel;
import com.nzgreens.common.model.console.AdminUserSearchModel;
import com.nzgreens.console.annotations.Auth;
import com.nzgreens.console.service.IAdminRoleService;
import com.nzgreens.console.service.IAdminUserService;
import com.nzgreens.dal.console.example.AdminRole;
import com.nzgreens.dal.console.example.AdminUser;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author za
 * @version V1.0
 */
@Controller
@RequestMapping("/adminUser")
public class AdminUserController {

	@Resource
	private IAdminUserService adminUserService;
	@Resource
	private IAdminRoleService adminRoleService;

	@RequestMapping("/to-add")
	@Auth("ADMIN_USER_ADD")
	public String toAdd(Model model) {
		initUserInfo(model);
		return "system/adminuser-add";
	}

	@RequestMapping("/insert")
	@ResponseBody
	@Auth("ADMIN_USER_ADD")
	public ResultModel<Integer> insert(AdminUserAddForm adminUserAddForm) {
		ResultModel<Integer> resultModel = new ResultModel<>();
		Integer row = this.adminUserService.insert(adminUserAddForm);
		resultModel.setData(row);
		return resultModel;
	}

	@RequestMapping("/to-list")
	@Auth("ADMIN_USER_MANAGE")
	public String toList(Model model) {
		initUserInfo(model);
		return "system/adminuser-list";
	}

	@RequestMapping("/search-list")
	@ResponseBody
	@Auth("ADMIN_USER_MANAGE")
	public ResultModel<PageInfo<AdminUserSearchModel>> searchUserList(AdminUserSearchForm adminUserSearchForm) {
		ResultModel<PageInfo<AdminUserSearchModel>> resultModel = new ResultModel<>();
		List<AdminUserSearchModel> adminUserSearchModels = this.adminUserService.selectList(adminUserSearchForm);
		PageInfo<AdminUserSearchModel> pageInfo = new PageInfo<>(adminUserSearchModels);
		resultModel.setData(pageInfo);
		return resultModel;
	}

	@RequestMapping("/search-detail")
	@ResponseBody
	@Auth("ADMIN_USER_UPDATE")
	public ResultModel<AdminUser> searchDetail(Long userId) {
		ResultModel<AdminUser> resultModel = new ResultModel<>();
		AdminUser adminUser = this.adminUserService.selectById(userId);
		resultModel.setData(adminUser);
		return resultModel;
	}

	@RequestMapping("/update")
	@ResponseBody
	@Auth("ADMIN_USER_UPDATE")
	public ResultModel<Integer> saveUpdate(AdminUserUpdateForm adminUserUpdateForm) {
		ResultModel<Integer> resultModel = new ResultModel<>();
		int row = this.adminUserService.udpateById(adminUserUpdateForm);
		resultModel.setData(row);
		return resultModel;
	}

	@RequestMapping("/delete")
	@ResponseBody
	@Auth("ADMIN_USER_DELETE")
	public ResultModel<Integer> deleteUser(Long userId) {
		ResultModel<Integer> resultModel = new ResultModel<>();
		int row = this.adminUserService.deleteById(userId);
		resultModel.setData(row);
		return resultModel;
	}

	@RequestMapping("/updatePasswd")
	@ResponseBody
	public ResultModel updatePasswd(AdminUserPasswdUpdateForm passwdUpdateForm) throws Exception {
		ResultModel resultModel = new ResultModel();
		Long userId = (Long) SecurityUtils.getSubject().getPrincipal();
		passwdUpdateForm.setId(userId);
		this.adminUserService.updatePasswd(passwdUpdateForm);
		return resultModel;
	}

	@RequestMapping("/updateResetPasswd")
	@ResponseBody
	@Auth("ADMIN_USER_PASSWORD_RESET")
	public ResultModel updateResetPasswd(Long userId) {
		ResultModel resultModel = new ResultModel();
		this.adminUserService.updateResetPasswd(userId);
		return resultModel;
	}

	//初始化用户信息
	private void initUserInfo(Model model) {
		List<AdminRole> adminRoles = this.adminRoleService.selectAll();
		model.addAttribute("adminRoles", adminRoles);
	}

}
