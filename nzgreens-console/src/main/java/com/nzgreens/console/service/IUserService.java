package com.nzgreens.console.service;

import com.nzgreens.common.form.console.UserAddForm;
import com.nzgreens.common.form.console.UserForm;
import com.nzgreens.common.model.console.UsersModel;
import com.nzgreens.dal.user.example.Users;

import java.util.List;

/**
 * @Author:helizheng
 * @Date: Created in 2018/4/25 23:44
 */

public interface IUserService {
    List<UsersModel> selectUserForPage(UserForm form) throws Exception;

    List<UsersModel> selectAgentUserForPage(UserForm form) throws Exception;

    UsersModel selectUserDetail(Long userId) throws Exception;

    List<Users> searchAgentList() throws Exception;

    void insertUser(UserAddForm users) throws Exception;

    void updateResetPassword(Long userId) throws Exception;

    void updateUser(UserAddForm users) throws Exception;

    void updateUserBalance(Long userId,String balance,String remark) throws Exception;

    void deleteUser(Long userId) throws Exception;
}
