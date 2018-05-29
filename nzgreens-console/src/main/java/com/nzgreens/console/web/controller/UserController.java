package com.nzgreens.console.web.controller;

import com.github.pagehelper.PageInfo;
import com.nzgreens.common.form.console.UserAddForm;
import com.nzgreens.common.form.console.UserForm;
import com.nzgreens.common.model.ResultModel;
import com.nzgreens.common.model.console.UsersModel;
import com.nzgreens.console.annotations.Auth;
import com.nzgreens.console.service.IUserService;
import com.nzgreens.dal.user.example.Users;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author:helizheng
 * @Date: Created in 2018/4/26 0:38
 */
@Controller
@RequestMapping("user")
public class UserController extends BaseController {
    @Resource
    private IUserService userService;

    @RequestMapping("to-list")
    @Auth("USER_MANAGE")
    public String toList(Model model) throws Exception{
        model.addAttribute("agentList",userService.searchAgentList());
        return "user/user-list";
    }

    @RequestMapping("search-list")
    @ResponseBody
    @Auth("USER_MANAGE")
    public ResultModel selectUserForPage(UserForm form) throws Exception{
        ResultModel<PageInfo<Users>> resultModel = new ResultModel<>();
        List<Users> users = userService.selectUserForPage(form);
        PageInfo<Users> pageInfo = new PageInfo<>(users);
        resultModel.setData(pageInfo);
        return resultModel;
    }

    @RequestMapping("search-detail")
    @ResponseBody
    @Auth("USER_MANAGE")
    public ResultModel searchDetail(Long userId) throws Exception{
        ResultModel<UsersModel> resultModel = new ResultModel<>();
        UsersModel users = userService.selectUserDetail(userId);
        resultModel.setData(users);
        return resultModel;
    }

    @RequestMapping("search/agent-list")
    @ResponseBody
    @Auth("USER_MANAGE")
    public ResultModel searchAgentList() throws Exception{
        ResultModel<List<Users>> resultModel = new ResultModel<>();
        List<Users> users = userService.searchAgentList();
        resultModel.setData(users);
        return resultModel;
    }

    @RequestMapping("insert")
    @ResponseBody
    @Auth("USER_UPDATE")
    public ResultModel insert(UserAddForm users) throws Exception{
        userService.insertUser(users);
        return new ResultModel();
    }

    @RequestMapping("update")
    @ResponseBody
    @Auth("USER_UPDATE")
    public ResultModel update(UserAddForm users) throws Exception{
        userService.updateUser(users);
        return new ResultModel();
    }

    @RequestMapping("update/balance")
    @ResponseBody
    @Auth("USER_UPDATE")
    public ResultModel updateBalance(Long userId,String balance) throws Exception{
        userService.updateUserBalance(userId,balance);
        return new ResultModel();
    }

    @RequestMapping("reset")
    @ResponseBody
    @Auth("USER_UPDATE")
    public ResultModel reset(Long userId) throws Exception{
        userService.updateResetPassword(userId);
        return new ResultModel();
    }

    @RequestMapping("delete")
    @ResponseBody
    @Auth("USER_UPDATE")
    public ResultModel delete(Long userId) throws Exception{
        userService.deleteUser(userId);
        return new ResultModel();
    }
}
