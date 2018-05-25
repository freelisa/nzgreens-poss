package com.nzgreens.console.service.impl;

import com.github.pagehelper.PageHelper;
import com.nzgreens.common.form.console.AdminRoleAddForm;
import com.nzgreens.common.form.console.AdminRoleSearchForm;
import com.nzgreens.common.form.console.AdminRoleUpdateForm;
import com.nzgreens.common.utils.BeanMapUtil;
import com.nzgreens.console.permission.BitCode;
import com.nzgreens.console.permission.PermissionConfig;
import com.nzgreens.console.service.IAdminRoleService;
import com.nzgreens.dal.console.example.AdminRole;
import com.nzgreens.dal.console.example.AdminRoleExample;
import com.nzgreens.dal.console.mapper.AdminRoleMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by z on 2016/10/10.
 */
@Service
public class AdminRoleService implements IAdminRoleService {

	@Resource
	private PermissionConfig permissionConfig;

	@Resource
	private AdminRoleMapper adminRoleMapper;

	@Override
	public List<AdminRole> selectList(AdminRoleSearchForm adminRoleSearchForm) {
		AdminRoleExample adminRoleExample = new AdminRoleExample();
		if (StringUtils.isNotBlank(adminRoleSearchForm.getName())) {
			adminRoleExample.createCriteria().andNameLike(adminRoleSearchForm.getName() + "%");
		}
		PageHelper.startPage(adminRoleSearchForm.getPageNum(), adminRoleSearchForm.getPageSize());
		List<AdminRole> adminRoles = adminRoleMapper.selectByExample(adminRoleExample);
		return adminRoles;
	}

	@Override
	public Integer insert(AdminRoleAddForm adminRoleAddForm) {
		//检查角色名是否已经存在
		if (!selectByCheckRoleName(adminRoleAddForm.getName())) {
			AdminRole adminRole = new AdminRole();
			BeanMapUtil.copy(adminRoleAddForm, adminRole);
			adminRole.setCreateTime(new Date());
			adminRole.setUpdateTime(new Date());
			if (CollectionUtils.isNotEmpty((adminRoleAddForm.getPermissionIds()))) {
				adminRole.setPermissions(permissionConfig.getPermissionCode(adminRoleAddForm.getPermissionIds()).getCode());
			}
			return adminRoleMapper.insert(adminRole);
		}
		return 0;
	}

	@Override
	public List<AdminRole> selectAll() {
		return adminRoleMapper.selectByExample(null);
	}

	@Override
	public AdminRole selectById(Long roleId) {
		if (roleId == null || roleId == 0) {
			return null;
		}

		AdminRole role = adminRoleMapper.selectByPrimaryKey(roleId);
		role.setPermissionCodes(permissionConfig.getPermissionCodes(new BitCode(role.getPermissions())));
		return role;
	}

	@Override
	public Integer updateById(AdminRoleUpdateForm adminRoleUpdateForm) {
		AdminRole dataAdminRole = adminRoleMapper.selectByPrimaryKey(adminRoleUpdateForm.getId());
		boolean flg = true;
		if (!dataAdminRole.getName().equals(adminRoleUpdateForm.getName())) {
			flg = selectByCheckRoleName(adminRoleUpdateForm.getName());
		}
		if (!flg) {
			return 0;
		}
		AdminRole adminRole = new AdminRole();
		BeanMapUtil.copy(adminRoleUpdateForm, adminRole);
		adminRole.setUpdateTime(new Date());
		if (CollectionUtils.isNotEmpty((adminRoleUpdateForm.getPermissionIds()))) {
			adminRole.setPermissions(permissionConfig.getPermissionCode(adminRoleUpdateForm.getPermissionIds()).getCode());
		}
		return adminRoleMapper.updateByPrimaryKeySelective(adminRole);
	}

	@Override
	public Integer deleteById(Long roleId) {
		return adminRoleMapper.deleteByPrimaryKey(roleId);
	}

	@Override
	public boolean selectByCheckRoleName(String roleName) {
		AdminRoleExample adminRoleExample = new AdminRoleExample();
		adminRoleExample.createCriteria().andNameEqualTo(roleName);
		int count = adminRoleMapper.countByExample(adminRoleExample);
		return count > 0;
	}
}
