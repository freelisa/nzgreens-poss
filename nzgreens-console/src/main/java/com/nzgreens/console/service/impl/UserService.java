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
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

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
    private UserOrderCountMapper userOrderCountMapper;
    @Resource
    private UserOrderMapper userOrderMapper;
    @Override
    public List<UsersModel> selectUserForPage(UserForm form) throws Exception {
        PageHelper.startPage(form.getPageNum(),form.getPageSize());
        List<UsersModel> usersModels = subUserMapper.selectUserForPage(form);
        if (CollectionUtils.isEmpty(usersModels)) {
            return usersModels;
        }
        List<Long> agentIds = usersModels.stream().filter(usersModel -> UserTypeEnum._AGENT.getValue() == usersModel.getType())
                .map(UsersModel::getId).collect(Collectors.toList());
        UserOrderExample userOrderExample = new UserOrderExample();
        userOrderExample.createCriteria().andUserIdIn(usersModels.stream().map(UsersModel::getId).collect(Collectors.toList()))
                .andStatusIn(Arrays.asList(new Byte("0"),new Byte("1"),new Byte("2")));
        userOrderExample.setOrderByClause("create_time desc");
        List<UserOrderCount> orderList = userOrderCountMapper.countByExamples(userOrderExample);
        if (CollectionUtils.isNotEmpty(orderList)) {
            Map<Long, List<UserOrderCount>> map = orderList.stream().collect(Collectors.groupingBy(UserOrderCount::getUserId));
            try {

                usersModels.forEach(usersModel -> {
                    List<UserOrderCount> countList = map.getOrDefault(usersModel.getId(), Collections.emptyList());
                    if (CollectionUtils.isNotEmpty(countList)) {
                        usersModel.setTotalOrderCount(countList.get(0).getOrderCount());
                        usersModel.setLastOrderTime(countList.get(0).getCreateTime());
                    }

                });
            } catch (Exception e) {

            }
        }
        if (CollectionUtils.isEmpty(agentIds)) {
            return usersModels;
        }
        UserAgentExample agentExample = new UserAgentExample();
        agentExample.createCriteria().andAgentUserIdIn(agentIds);
        List<UserAgent> userAgentList = userAgentMapper.selectByExample(agentExample);
        if (CollectionUtils.isEmpty(userAgentList)) {
            return usersModels;
        }
        UsersExample usersExample = new UsersExample();
        usersExample.createCriteria().andIdIn(userAgentList.stream().map(UserAgent::getUserId)
                .collect(Collectors.toList())).andBalanceLessThan(0L);
        List<Users> usersList = usersMapper.selectByExample(usersExample);
        if (CollectionUtils.isEmpty(usersList)) {
            return usersModels;
        }
        try {
            Map<Long, List<UserAgent>> agentMap = userAgentList.stream().collect(Collectors.groupingBy(UserAgent::getAgentUserId, Collectors.toList()));
            Map<Long, Long> userBalanceMap = usersList.stream().collect(Collectors.groupingBy(Users::getId, Collectors.summingLong(Users::getBalance)));
            agentMap.forEach((k,v)->{
                UsersModel usersModel = usersModels.stream().filter(a->a.getId().equals(k)).findFirst().get();
                Long balance = v.stream().map(user->
                        userBalanceMap.getOrDefault(user.getUserId(), 0L))
                        .reduce(0L,(v1,v2)->v1+v2);
                usersModel.setTotalBalance(usersModel.getBalance() + balance);
            });
        } catch (Exception e) {
            usersModels.forEach(user->user.setTotalBalance(-1L));
        }

        return usersModels;
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
        example.setOrderByClause(" telephone");
        return usersMapper.selectByExample(example);
    }

    @Override
    public void insertUser(UserAddForm users) throws Exception {
        checkUserForm(users);

        UsersExample example = new UsersExample();
        example.createCriteria().andTelephoneEqualTo(users.getTelephone());
        if(usersMapper.countByExample(example) > 0){
            thrown(ErrorCodes.USER_MOBILE_EXIST_ERROR);
        }

        if(users.getType().intValue() == UserTypeEnum._AGENT.getValue() && users.getAgentUserId() != null){
            thrown(ErrorCodes.USER_AGENT_SETTING_ERROR);
        }else if(users.getType().intValue() == UserTypeEnum._USER.getValue() && users.getAgentUserId() == null){
            thrown(ErrorCodes.USER_AGENT_NOT_NULL);
        }
        users.setPassword(StringUtil.md5(users.getTelephone()));
        Users user = new Users();
        BeanUtils.copyProperties(users,user);
        user.setNickname(RandomUtil.generateInt(8));
        user.setLastOrderNumber(Long.valueOf(users.getTelephone() + "0"));
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

        UsersExample usersExample = new UsersExample();
        usersExample.createCriteria().andIdNotEqualTo(users.getId()).andTelephoneEqualTo(users.getTelephone());
        if(usersMapper.countByExample(usersExample) > 0){
            thrown(ErrorCodes.USER_MOBILE_EXIST_ERROR);
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
    public void updateUserBalance(Long userId,String balance,String remark) throws Exception {
        if(userId == null){
            thrown(ErrorCodes.ID_ILLEGAL);
        }
        if(StringUtils.isBlank(balance)){
            thrown(ErrorCodes.USER_BALANCE_ILLEGAL);
        }
        if(StringUtils.equals(balance,"0")){
            thrown(ErrorCodes.USER_BALANCE_ILLEGAL);
        }
        if(StringUtils.isBlank(remark)){
            thrown(ErrorCodes.USER_BALANCE_REMARK_ILLEGAL);
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
            record.setRemark(remark);
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
            record.setRemark(remark);
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
        if(!StringUtils.isNumeric(users.getTelephone())){
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
