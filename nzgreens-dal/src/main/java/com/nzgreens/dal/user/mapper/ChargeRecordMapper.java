package com.nzgreens.dal.user.mapper;

import com.nzgreens.dal.user.example.ChargeRecord;
import com.nzgreens.dal.user.example.ChargeRecordExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ChargeRecordMapper {
    int countByExample(ChargeRecordExample example);

    int deleteByExample(ChargeRecordExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ChargeRecord record);

    int insertSelective(ChargeRecord record);

    List<ChargeRecord> selectByExample(ChargeRecordExample example);

    ChargeRecord selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ChargeRecord record, @Param("example") ChargeRecordExample example);

    int updateByExample(@Param("record") ChargeRecord record, @Param("example") ChargeRecordExample example);

    int updateByPrimaryKeySelective(ChargeRecord record);

    int updateByPrimaryKey(ChargeRecord record);
}