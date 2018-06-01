package com.nzgreens.console.service.impl;

import com.github.pagehelper.PageHelper;
import com.nzgreens.common.enums.AccountLogsTypeEnum;
import com.nzgreens.common.enums.IsValidEnum;
import com.nzgreens.common.enums.UserOrderStatusEnum;
import com.nzgreens.common.enums.UserTypeEnum;
import com.nzgreens.common.exception.ErrorCodes;
import com.nzgreens.common.form.console.UserAddForm;
import com.nzgreens.common.form.console.UserForm;
import com.nzgreens.common.model.console.UsersModel;
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
    @Resource
    private UserAgentMapper userAgentMapper;
    @Resource
    private UserOrderMapper userOrderMapper;

    @Override
    public List<UsersModel> selectUserForPage(UserForm form) throws Exception {
        PageHelper.startPage(form.getPageNum(),form.getPageSize());
        return subUserMapper.selectUserForPage(form);
    }

    @Override
    public List<UsersModel> selectAgentUserForPage(UserForm form) throws Exception {
        PageHelper.startPage(form.getPageNum(),form.getPageSize());
        return subUserMapper.selectAgentUserForPage(form);
    }

    @Override
    public UsersModel selectUserDetail(Long userId) throws Exception {
        Users users = usersMapper.selectByPrimaryKey(userId);

        UsersModel model = new UsersModel();
        BeanUtils.copyProperties(users,model);

        UserAgentExample example = new UserAgentExample();
        example.createCriteria().andUserIdEqualTo(userId);
        List<UserAgent> userAgents = userAgentMapper.selectByExample(example);
        if(CollectionUtils.isNotEmpty(userAgents)){
            model.setAgentUserId(userAgents.get(0).getAgentUserId());
        }
        return model;
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
        if(users.getType().intValue() == UserTypeEnum._AGENT.getValue() && users.getAgentUserId() != null){
            thrown(ErrorCodes.USER_AGENT_SETTING_ERROR);
        }else if(users.getType().intValue() == UserTypeEnum._USER.getValue() && users.getAgentUserId() == null){
            thrown(ErrorCodes.USER_AGENT_NOT_NULL);
        }
        users.setPassword(StringUtil.md5(users.getTelephone()));
        Users user = new Users();
        BeanUtils.copyProperties(users,user);
        user.setNickname(RandomUtil.generateInt(8));
        if(usersMapper.insertSelective(user) < 1){
            thrown(ErrorCodes.UPDATE_ERROR);
        }

        if(users.getType().intValue() == UserTypeEnum._USER.getValue()){
            UserAgent agent = new UserAgent();
            agent.setAgentUserId(users.getAgentUserId());
            agent.setUserId(user.getId());
            if(userAgentMapper.insertSelective(agent) < 1){
                thrown(ErrorCodes.UPDATE_ERROR);
            }
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
        if(users == null){
            thrown(ErrorCodes.ILLEGAL_PARAM);
        }
        if(users.getId() == null){
            thrown(ErrorCodes.ID_ILLEGAL);
        }
        if(users.getType() == null){
            thrown(ErrorCodes.USER_TYPE_ILLEGAL);
        }
        if(users.getBalance() != null && !NumberUtils.isNumber(users.getBalance())){
            thrown(ErrorCodes.USER_BALANCE_ILLEGAL);
        }
        if(users.getType().intValue() == UserTypeEnum._AGENT.getValue() && users.getAgentUserId() != null){
            thrown(ErrorCodes.USER_AGENT_SETTING_ERROR);
        }else if(users.getType().intValue() == UserTypeEnum._USER.getValue() && users.getAgentUserId() == null){
            thrown(ErrorCodes.USER_AGENT_NOT_NULL);
        }
        Users user = new Users();
        BeanUtils.copyProperties(users,user);
        if(usersMapper.updateByPrimaryKeySelective(user) < 1){
            thrown(ErrorCodes.UPDATE_ERROR);
        }

        //查询用户当前类型
        Users usersQuery = usersMapper.selectByPrimaryKey(users.getId());
        if(usersQuery.getType().intValue() == UserTypeEnum._AGENT.getValue() && users.getAgentUserId() != null){
            if(users.getAgentUserId() != UserTypeEnum._AGENT.getValue()){
                thrown(ErrorCodes.USER_AGENT_NOT_UPDATE_TYPE);
            }
        }
        if(usersQuery.getType().intValue() == UserTypeEnum._USER.getValue()){
            //查询该用户是否更换了代理
            UserAgentExample example = new UserAgentExample();
            example.createCriteria().andUserIdEqualTo(users.getId());
            List<UserAgent> userAgents = userAgentMapper.selectByExample(example);
            if(CollectionUtils.isNotEmpty(userAgents)){
                UserAgent oldAgent = userAgents.get(0);
                //没有更换
                if(oldAgent.getAgentUserId().intValue() == users.getAgentUserId().intValue()){
                    return;
                }

                //更换了代理，如果有未处理的订单不让更换
                UserOrderExample orderExample = new UserOrderExample();
                orderExample.createCriteria().andUserIdEqualTo(usersQuery.getId())
                        .andStatusEqualTo((byte) UserOrderStatusEnum._PENDING.getValue());
                if(userOrderMapper.countByExample(orderExample) > 0){
                    thrown(ErrorCodes.USER_ORDER_STATUS_ERROR);
                }
            }

            //修改代理关系
            UserAgent agent = new UserAgent();
            agent.setAgentUserId(users.getAgentUserId());

            if(userAgentMapper.updateByExampleSelective(agent,example) < 1){
                thrown(ErrorCodes.UPDATE_ERROR);
            }
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

        Integer balances = CurrencyUtil.convertYuanToFenResInt(balance);
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
            rebateLog.setAmount(-record.getAmount());
            rebateLog.setAfter(u.getBalance() - record.getAmount());
            rebateLog.setTriggerUserId(systemUser.getId());
            if(accountLogsMapper.insertSelective(rebateLog) < 1){
                thrown(ErrorCodes.UPDATE_ERROR);
            }

            //系统加币
            int row = subUserMapper.reduceBalance(systemUser.getId(), balances);
            if(row < 1){
                thrown(ErrorCodes.SYSTEM_BALANCE_NOT_ENOUGH);
            }

            //系统加币日志
            AccountLogs systemLog = new AccountLogs();
            systemLog.setUserId(systemUser.getId());
            systemLog.setType((byte) AccountLogsTypeEnum._WITHDRAW.getValue());
            systemLog.setRecordId(record.getId());
            systemLog.setBefore(systemUser.getBalance());
            systemLog.setAmount(record.getAmount());
            systemLog.setAfter(systemUser.getBalance() + record.getAmount());
            systemLog.setTriggerUserId(systemUser.getId());
            if(accountLogsMapper.insertSelective(systemLog) < 1){
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

            //系统减币
            int row = subUserMapper.reduceBalance(systemUser.getId(), balances);
            if(row < 1){
                thrown(ErrorCodes.SYSTEM_BALANCE_NOT_ENOUGH);
            }

            //系统减币日志
            AccountLogs systemLog = new AccountLogs();
            systemLog.setUserId(systemUser.getId());
            systemLog.setType((byte) AccountLogsTypeEnum._CHARGE.getValue());
            systemLog.setRecordId(record.getId());
            systemLog.setBefore(systemUser.getBalance());
            systemLog.setAmount(-record.getAmount());
            systemLog.setAfter(systemUser.getBalance() - record.getAmount());
            systemLog.setTriggerUserId(systemUser.getId());
            if(accountLogsMapper.insertSelective(systemLog) < 1){
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
