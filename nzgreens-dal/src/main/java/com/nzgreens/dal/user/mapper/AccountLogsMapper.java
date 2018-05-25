package com.nzgreens.dal.user.mapper;

import com.nzgreens.dal.user.example.AccountLogs;
import com.nzgreens.dal.user.example.AccountLogsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AccountLogsMapper {
    int countByExample(AccountLogsExample example);

    int deleteByExample(AccountLogsExample example);

    int deleteByPrimaryKey(Long id);

    int insert(AccountLogs record);

    int insertSelective(AccountLogs record);

    List<AccountLogs> selectByExample(AccountLogsExample example);

    AccountLogs selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") AccountLogs record, @Param("example") AccountLogsExample example);

    int updateByExample(@Param("record") AccountLogs record, @Param("example") AccountLogsExample example);

    int updateByPrimaryKeySelective(AccountLogs record);

    int updateByPrimaryKey(AccountLogs record);
}