package com.nzgreens.console.service.impl;

import com.github.pagehelper.PageHelper;
import com.nzgreens.common.form.console.ChargeRecordForm;
import com.nzgreens.common.model.console.ChargeRecordModel;
import com.nzgreens.console.service.IChargeRecordService;
import com.nzgreens.dal.user.mapper.SubUserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author:helizheng
 * @Date: Created in 2018/5/19 21:35
 */
@Service
public class ChargeRecordService implements IChargeRecordService {
    @Resource
    private SubUserMapper subUserMapper;

    @Override
    public List<ChargeRecordModel> selectChargeRecordForPage(ChargeRecordForm form) throws Exception {
        PageHelper.startPage(form.getPageNum(),form.getPageSize());
        return subUserMapper.selectChargeRecordForPage(form);
    }
}
