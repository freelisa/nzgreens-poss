package com.nzgreens.console.service.impl;

import com.github.pagehelper.PageHelper;
import com.nzgreens.common.exception.CommonException;
import com.nzgreens.common.form.console.AdminUserAddForm;
import com.nzgreens.common.form.console.AdminUserPasswdUpdateForm;
import com.nzgreens.common.form.console.AdminUserSearchForm;
import com.nzgreens.common.form.console.AdminUserUpdateForm;
import com.nzgreens.common.model.console.AdminUserSearchModel;
import com.nzgreens.common.utils.BeanMapUtil;
import com.nzgreens.console.permission.PermissionConfig;
import com.nzgreens.console.realm.LoginRealm;
import com.nzgreens.console.service.IAdminUserService;
import com.nzgreens.dal.console.example.AdminUser;
import com.nzgreens.dal.console.mapper.AdminUserMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.subject.Subject;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by fei on 16/10/8.
 */
@Service
public class AdminUserService implements IAdminUserService {
    @Resource
    protected LoginRealm loginRealm;
    @Resource
    private AdminUserMapper adminUserMapper;
    @Resource
    protected PermissionConfig permissionConfig;
    @Resource
    private MessageSource messageSource;

    /**
     * 登录
     *
     * @param username
     * @param password
     * @param ip
     * @throws Exception
     */
    public void signIn(String username, String password, String ip) throws CommonException {
        try {
            AuthenticationToken token = new UsernamePasswordToken(username,
                    password);
            Subject subject = SecurityUtils.getSubject();
            subject.login(token);
            loginRealm.clearCache();
        } catch (DisabledAccountException de) {
            thrown("user.disabled");
        } catch (UnknownAccountException ue) {
            thrown("user.not.exist");
        } catch (IncorrectCredentialsException ie) {
            thrown("password.wrong");
        } catch (AuthenticationException ae) {
            thrown("login.failed");
        }
    }

    /**
     * 退出登录。
     */
    public void signOut() {
        loginRealm.clearCache();
        SecurityUtils.getSubject().logout();
    }

    /**
     * 获取当前登录用户。
     *
     * @return 返回当前登录用户。
     */
    @Transactional(readOnly = true)
    public AdminUser getCurrentUser() {
        try {
            Long userId = (Long) SecurityUtils.getSubject().getPrincipal();
            return adminUserMapper.selectByPrimaryKey(userId);
        } catch (Exception e) {
            throw new UnauthenticatedException("获取当前登录用户时发生异常。", e);
        }
    }

    @Override
    public int insert(AdminUserAddForm adminUserAddForm) {
        AdminUser adminUser = new AdminUser();
        BeanMapUtil.copy(adminUserAddForm, adminUser);
        adminUser.setCreateTime(new Date());
        adminUser.setUpdateTime(new Date());
        adminUser.setPassword(loginRealm.encryptPassword(adminUser.getPassword()));
        adminUser.setStatus(0);
        return adminUserMapper.insert(adminUser);
    }

    @Override
    public List<AdminUserSearchModel> selectList(AdminUserSearchForm adminUserSearchForm) {
        PageHelper.startPage(adminUserSearchForm.getPageNum(),adminUserSearchForm.getPageSize());
        return adminUserMapper.selectList(adminUserSearchForm);
    }

    @Override
    public AdminUser selectById(Long userId) {
        return adminUserMapper.selectByPrimaryKey(userId);
    }

    @Override
    public int udpateById(AdminUserUpdateForm adminUserUpdateForm) {
        AdminUser adminUser = new AdminUser();
        BeanMapUtil.copy(adminUserUpdateForm,adminUser);
        adminUser.setUpdateTime(new Date());
        return adminUserMapper.updateByPrimaryKeySelective(adminUser);
    }

    @Override
    public int deleteById(Long id) {
        return adminUserMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void updatePasswd(AdminUserPasswdUpdateForm passwdUpdateForm) throws CommonException {
        if(!StringUtils.equals(passwdUpdateForm.getNewPassword(),passwdUpdateForm.getNewPasswordConfirm())){
            thrown("confirm.passwd.not");
        }
        AdminUser adminUser = adminUserMapper.selectByPrimaryKey(passwdUpdateForm.getId());
        if(adminUser==null){
            thrown("user.not.exist");
        }
        if(!StringUtils.equals(loginRealm.encryptPassword(passwdUpdateForm.getOldPassword()),adminUser.getPassword())){
            thrown("user.passwd.wrong");
        }
        adminUser.setPassword(loginRealm.encryptPassword(passwdUpdateForm.getNewPassword()));
        adminUserMapper.updateByPrimaryKeySelective(adminUser);
    }

    @Override
    public void updateResetPasswd(Long id){
        AdminUser adminUser = new AdminUser();
        adminUser.setId(id);
        adminUser.setPassword(loginRealm.encryptPassword("123456"));
        adminUserMapper.updateByPrimaryKeySelective(adminUser);
    }

    /**
     * 抛出异常
     *
     * @param code
     * @param args
     * @throws CommonException
     */
    protected void thrown(String code, String... args) throws CommonException {
        throw new CommonException(code, messageSource.getMessage(code, args, Locale.SIMPLIFIED_CHINESE));
    }


}
