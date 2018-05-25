package com.nzgreens.console.service.impl;

import com.github.pagehelper.PageHelper;
import com.nzgreens.common.form.console.WithdrawRecordForm;
import com.nzgreens.common.model.console.WithdrawRecordModel;
import com.nzgreens.console.service.IWithdrawRecordService;
import com.nzgreens.dal.user.mapper.SubUserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author:helizheng
 * @Date: Created in 2018/5/19 21:35
 */
@Service
public class WithdrawRecordService implements IWithdrawRecordService {
    @Resource
    private SubUserMapper subUserMapper;

    @Override
    public List<WithdrawRecordModel> selectWithdrawRecordForPage(WithdrawRecordForm form) throws Exception {
        PageHelper.startPage(form.getPageNum(),form.getPageSize());
        return subUserMapper.selectWithdrawRecordForPage(form);
    }
}
