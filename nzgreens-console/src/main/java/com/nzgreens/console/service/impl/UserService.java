package com.nzgreens.console.service.impl;

import com.github.pagehelper.PageHelper;
import com.nzgreens.common.enums.AccountLogsTypeEnum;
import com.nzgreens.common.enums.IsValidEnum;
import com.nzgreens.common.enums.UserTypeEnum;
import com.nzgreens.common.exception.ErrorCodes;
import com.nzgreens.common.form.console.UserAddForm;
import com.nzgreens.common.form.console.UserForm;
import com.nzgreens.common.utils.CurrencyUtil;
import com.nzgreens.common.utils.RandomUtil;
import com.nzgreens.common.utils.StringUtil;
import com.nzgreens.console.service.BaseService;
import com.nzgreens.console.service.IUserService;
import com.nzgreens.dal.user.example.*;
import com.nzgreens.dal.user.mapper.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author:helizheng
 * @Date: Created in 2018/4/25 23:48
 */
@Service
public class UserService extends BaseService implements IUserService {
    @Resource
    private UsersMapper usersMapper;
    @Resource
    private SubUserMapper subUserMapper;
    @Resource
    private ChargeRecordMapper chargeRecordMapper;
    @Resource
    private WithdrawRecordMapper withdrawRecordMapper;
    @Resource
    private AccountLogsMapper accountLogsMapper;

    @Override
    public List<Users> selectUserForPage(UserForm form) throws Exception {
        PageHelper.startPage(form.getPageNum(),form.getPageSize());
        UsersExample example = new UsersExample();
        UsersExample.Criteria criteria = example.createCriteria();
        if(form != null){
            if(form.getUserId() != null){
                criteria.andIdEqualTo(form.getUserId());
            }
            if(StringUtils.isNotBlank(form.getMobile())){
                criteria.andTelephoneEqualTo(form.getMobile());
            }
            if(form.getType() != null){
                criteria.andTypeEqualTo(form.getType());
            }
            if(form.getIsValid() != null){
                criteria.andIsValidEqualTo(form.getIsValid());
            }
            if(form.getStartTime() != null){
                criteria.andCreateTimeGreaterThanOrEqualTo(form.getStartTime());
            }
            if(form.getEndTime() != null){
                criteria.andCreateTimeLessThanOrEqualTo(form.getEndTime());
            }
        }
        example.setOrderByClause(" id desc");
        return usersMapper.selectByExample(example);
    }

    @Override
    public Users selectUserDetail(Long userId) throws Exception {
        return usersMapper.selectByPrimaryKey(userId);
    }

    @Override
    public List<Users> searchAgentList() throws Exception {
        UsersExample example = new UsersExample();
        example.createCriteria().andTypeEqualTo(UserTypeEnum._AGENT.getValue())
                .andIsValidEqualTo(IsValidEnum.EFFECTIVE.getValue());
        return usersMapper.selectByExample(example);
    }

    @Override
    public void insertUser(UserAddForm users) throws Exception {
        checkUserForm(users);
        users.setPassword(StringUtil.md5(users.getTelephone()));
        Users user = new Users();
        BeanUtils.copyProperties(users,user);
        user.setNickname(RandomUtil.generateInt(8));
        if(usersMapper.insertSelective(user) < 1){
            thrown(ErrorCodes.UPDATE_ERROR);
        }
    }

    @Override
    public void updateResetPassword(Long userId) throws Exception {
        if(userId == null){
            thrown(ErrorCodes.ID_ILLEGAL);
        }
        Users u = usersMapper.selectByPrimaryKey(userId);
        if(u == null){
            thrown(ErrorCodes.USER_NOT_EXIST_ILLEGAL);
        }
        Users users = new Users();
        users.setId(userId);
        users.setPassword(StringUtil.md5(u.getTelephone()));
        if(usersMapper.updateByPrimaryKeySelective(users) < 1){
            thrown(ErrorCodes.UPDATE_ERROR);
        }
    }

    @Override
    public void updateUser(UserAddForm users) throws Exception {
        checkUserForm(users);
        if(users.getId() == null){
            thrown(ErrorCodes.ID_ILLEGAL);
        }
        Users user = new Users();
        BeanUtils.copyProperties(users,user);
        if(usersMapper.updateByPrimaryKeySelective(user) < 1){
            thrown(ErrorCodes.UPDATE_ERROR);
        }
    }

    @Override
    public void updateUserBalance(Long userId,String balance) throws Exception {
        if(userId == null){
            thrown(ErrorCodes.ID_ILLEGAL);
        }
        if(StringUtils.isBlank(balance)){
            thrown(ErrorCodes.USER_BALANCE_ILLEGAL);
        }
        if(StringUtils.equals(balance,"0")){
            thrown(ErrorCodes.USER_BALANCE_ILLEGAL);
        }
        Users u = usersMapper.selectByPrimaryKey(userId);
        if(u == null){
            thrown(ErrorCodes.USER_NOT_EXIST_ILLEGAL);
        }

        //查询系统
        Users systemUser = selectSystemUser();

        Integer balances = Integer.valueOf(balance);
        if(balances < 0){
            int balanceAbs = Math.abs(balances);
            if(balanceAbs > u.getBalance()){
                thrown(ErrorCodes.BALANCE_NOT_ENOUGH);
            }else{
                int row = subUserMapper.reduceBalance(userId, balanceAbs);
                if(row < 1){
                    thrown(ErrorCodes.BALANCE_NOT_ENOUGH);
                }
            }
            //提现
            WithdrawRecord record = new WithdrawRecord();
            record.setAmount(Long.valueOf(balanceAbs));
            record.setUserAgentId(systemUser.getId());
            record.setUserId(userId);
            withdrawRecordMapper.insertSelective(record);
            //日志
            AccountLogs rebateLog = new AccountLogs();
            rebateLog.setUserId(userId);
            rebateLog.setType((byte) AccountLogsTypeEnum._WITHDRAW.getValue());
            rebateLog.setRecordId(record.getId());
            rebateLog.setBefore(u.getBalance());
            rebateLog.setAmount(record.getAmount());
            rebateLog.setAfter(u.getBalance() + record.getAmount());
            rebateLog.setTriggerUserId(systemUser.getId());
            if(accountLogsMapper.insertSelective(rebateLog) < 1){
                thrown(ErrorCodes.UPDATE_ERROR);
            }
        }else{
            subUserMapper.addBalance(userId,balances);
            //充值
            ChargeRecord record = new ChargeRecord();
            record.setAmount(balances.longValue());
            record.setUserAgentId(systemUser.getId());
            record.setUserId(userId);
            chargeRecordMapper.insertSelective(record);
            //日志
            AccountLogs rebateLog = new AccountLogs();
            rebateLog.setUserId(userId);
            rebateLog.setType((byte) AccountLogsTypeEnum._CHARGE.getValue());
            rebateLog.setRecordId(record.getId());
            rebateLog.setBefore(u.getBalance());
            rebateLog.setAmount(record.getAmount());
            rebateLog.setAfter(u.getBalance() + record.getAmount());
            rebateLog.setTriggerUserId(systemUser.getId());
            if(accountLogsMapper.insertSelective(rebateLog) < 1){
                thrown(ErrorCodes.UPDATE_ERROR);
            }
        }
    }

    @Override
    public void deleteUser(Long userId) throws Exception {
        if(userId == null){
            thrown(ErrorCodes.ID_ILLEGAL);
        }
        Users users = new Users();
        users.setId(userId);
        users.setIsValid(IsValidEnum.INVALID.getValue());
        if(usersMapper.updateByPrimaryKeySelective(users) < 1){
            thrown(ErrorCodes.UPDATE_ERROR);
        }
    }

    protected void checkUserForm(UserAddForm users) throws Exception{
        if(users == null){
            thrown(ErrorCodes.ILLEGAL_PARAM);
        }
        if(StringUtils.isBlank(users.getTelephone())){
            thrown(ErrorCodes.USER_MOBILE_ILLEGAL);
        }
        if(!StringUtil.isMobileNum(users.getTelephone())){
            thrown(ErrorCodes.USER_MOBILE_ERROR);
        }
        if(users.getType() == null){
            thrown(ErrorCodes.USER_TYPE_ILLEGAL);
        }
        if(users.getBalance() != null && !NumberUtils.isNumber(users.getBalance())){
            thrown(ErrorCodes.USER_BALANCE_ILLEGAL);
        }
    }
}
