package com.nzgreens.console.web.controller;

import com.github.pagehelper.PageInfo;
import com.nzgreens.common.form.console.AdminRoleAddForm;
import com.nzgreens.common.form.console.AdminRoleSearchForm;
import com.nzgreens.common.form.console.AdminRoleUpdateForm;
import com.nzgreens.common.model.ResultModel;
import com.nzgreens.common.model.console.AdminRoleModel;
import com.nzgreens.common.utils.BeanMapUtil;
import com.nzgreens.console.annotations.Auth;
import com.nzgreens.console.permission.BitCode;
import com.nzgreens.console.permission.PermissionConfig;
import com.nzgreens.console.permission.PermissionGroup;
import com.nzgreens.console.service.IAdminRoleService;
import com.nzgreens.dal.console.example.AdminRole;
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
@RequestMapping("/adminRole")
public class AdminRoleController {

    @Resource
    private IAdminRoleService adminRoleService;

    @Resource
    private PermissionConfig permissionConfig;

    @RequestMapping("/to-list")
    @Auth("ROLE_MANAGE")
    public String roleList(Model model){
        //获取所有权限分组
        List<PermissionGroup> permissionGroups = permissionConfig.getPermissionGroups();
        model.addAttribute("permissionGroups",permissionGroups);
        return "system/role-list";
    }

    @RequestMapping("/search-list")
    @ResponseBody
    @Auth("ROLE_MANAGE")
    public ResultModel<PageInfo<AdminRole>> searchRoleList(AdminRoleSearchForm adminRoleForm){
        List<AdminRole> adminRoles = this.adminRoleService.selectList(adminRoleForm);
        PageInfo<AdminRole> pageInfo = new PageInfo<>(adminRoles);
        ResultModel<PageInfo<AdminRole>> resultModel = new ResultModel<>();
        resultModel.setData(pageInfo);
        return resultModel;
    }

    @RequestMapping("/search-detail")
    @ResponseBody
    @Auth("ROLE_UPDATE")
    public ResultModel<AdminRoleModel> searchRoleDetail(Long roleId){
        ResultModel<AdminRoleModel> resultModel = new ResultModel<>();
        AdminRole adminRole = this.adminRoleService.selectById(roleId);
        if(adminRole!=null){
            AdminRoleModel adminRoleModel = new AdminRoleModel();
            BeanMapUtil.copy(adminRole,adminRoleModel);
            BitCode bitCode = new BitCode(adminRoleModel.getPermissions());
            adminRoleModel.setPermissionIds(this.permissionConfig.getPermissionIds(bitCode));
            resultModel.setData(adminRoleModel);
        }
        return resultModel;
    }

    @RequestMapping("to-add")
    @Auth("ROLE_ADD")
    public String roleAdd(Model model){
        //获取所有权限分组
        List<PermissionGroup> permissionGroups = permissionConfig.getPermissionGroups();
        model.addAttribute("permissionGroups",permissionGroups);
        return "system/role-add";
    }

    @RequestMapping("insert")
    @ResponseBody
    @Auth("ROLE_ADD")
    public ResultModel<Integer> insertRole(AdminRoleAddForm adminRoleAddForm){
        ResultModel<Integer> resultModel = new ResultModel<>();
        int row = this.adminRoleService.insert(adminRoleAddForm);
        if(row<=0){
            resultModel.setSuccess(false);
        }
        resultModel.setData(row);
        return resultModel;
    }

    @RequestMapping("/update")
    @ResponseBody
    @Auth("ROLE_UPDATE")
    public ResultModel<Integer> updateRole(AdminRoleUpdateForm adminRoleUpdateForm){
        ResultModel<Integer> resultModel = new ResultModel<>();
        Integer result = this.adminRoleService.updateById(adminRoleUpdateForm);
        resultModel.setData(result);
        return resultModel;
    }

    @RequestMapping("/delete")
    @ResponseBody
    @Auth("ROLE_DELETE")
    public ResultModel<Integer> deleteRole(Long roleId){
        ResultModel<Integer> resultModel = new ResultModel<>();
        Integer result = this.adminRoleService.deleteById(roleId);
        resultModel.setData(result);
        return resultModel;
    }
}
