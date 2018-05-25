package com.nzgreens.console.service;

import com.nzgreens.common.enums.UserTypeEnum;
import com.nzgreens.common.exception.CommonException;
import com.nzgreens.common.exception.ErrorCodes;
import com.nzgreens.dal.user.example.Users;
import com.nzgreens.dal.user.example.UsersExample;
import com.nzgreens.dal.user.mapper.UsersMapper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.MessageSource;

import javax.annotation.Resource;
import java.util.List;
import java.util.Locale;

/**
 * Created by longgang on 2017/6/20.
 */
public class BaseService {

    @Resource
    protected MessageSource messageSource;
    @Resource
    protected UsersMapper usersMapper;

    /**
     * 检查用户
     *
     * @param userId
     * @return
     * @throws Exception
     */
    protected Users checkUser(Long userId) throws Exception {
        if (userId == null || userId <= 0) {
            thrown(ErrorCodes.ID_ILLEGAL);
        }

        Users user = usersMapper.selectByPrimaryKey(userId);
        if (user == null) {
            thrown(ErrorCodes.USER_NOT_EXIST_ILLEGAL);
        }
        return user;
    }

    /**
     * 查询系统用户
     * @return
     */
    protected Users selectSystemUser(){
        //查询系统
        UsersExample userExample = new UsersExample();
        userExample.createCriteria().andTypeEqualTo(UserTypeEnum._SYSTEM.getValue());
        List<Users> systemUsers = usersMapper.selectByExample(userExample);
        Users systemUser = null;
        if(CollectionUtils.isNotEmpty(systemUsers)){
            systemUser = systemUsers.get(0);
        }
        return systemUser;
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
