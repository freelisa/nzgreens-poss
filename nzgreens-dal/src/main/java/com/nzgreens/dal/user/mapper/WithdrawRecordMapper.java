package com.nzgreens.dal.user.mapper;

import com.nzgreens.dal.user.example.WithdrawRecord;
import com.nzgreens.dal.user.example.WithdrawRecordExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface WithdrawRecordMapper {
    int countByExample(WithdrawRecordExample example);

    int deleteByExample(WithdrawRecordExample example);

    int deleteByPrimaryKey(Long id);

    int insert(WithdrawRecord record);

    int insertSelective(WithdrawRecord record);

    List<WithdrawRecord> selectByExample(WithdrawRecordExample example);

    WithdrawRecord selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") WithdrawRecord record, @Param("example") WithdrawRecordExample example);

    int updateByExample(@Param("record") WithdrawRecord record, @Param("example") WithdrawRecordExample example);

    int updateByPrimaryKeySelective(WithdrawRecord record);

    int updateByPrimaryKey(WithdrawRecord record);
}