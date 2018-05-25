package com.nzgreens.console.service.impl;

import com.github.pagehelper.PageHelper;
import com.nzgreens.common.enums.UserTypeEnum;
import com.nzgreens.common.form.console.CoinFlowForm;
import com.nzgreens.common.model.console.CoinFlowModel;
import com.nzgreens.console.service.ICoinFlowService;
import com.nzgreens.dal.user.example.Users;
import com.nzgreens.dal.user.example.UsersExample;
import com.nzgreens.dal.user.mapper.CoinFlowMapper;
import com.nzgreens.dal.user.mapper.UsersMapper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author:helizheng
 * @Date: Created in 2018/4/15 16:02
 */

@Service
public class CoinFlowService implements ICoinFlowService {
    @Resource
    private CoinFlowMapper coinFlowMapper;
    @Resource
    private UsersMapper usersMapper;

    @Override
    public List<CoinFlowModel> selectCoinFlowForPage(CoinFlowForm form) throws Exception {
        if(form.getUserType() != null){
            UsersExample example = new UsersExample();
            example.createCriteria().andTypeEqualTo(UserTypeEnum._SYSTEM.getValue());
            List<Users> users = usersMapper.selectByExample(example);
            if(CollectionUtils.isNotEmpty(users)){
                form.setUserId(users.get(0).getId());
            }
        }
        PageHelper.startPage(form.getPageNum(), form.getPageSize());
        return coinFlowMapper.selectCoinFlowForPage(form);
    }
}
