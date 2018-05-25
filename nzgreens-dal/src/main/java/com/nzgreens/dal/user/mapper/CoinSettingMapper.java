package com.nzgreens.dal.user.mapper;

import com.nzgreens.dal.user.example.CoinSetting;
import com.nzgreens.dal.user.example.CoinSettingExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CoinSettingMapper {
    int countByExample(CoinSettingExample example);

    int deleteByExample(CoinSettingExample example);

    int deleteByPrimaryKey(Byte id);

    int insert(CoinSetting record);

    int insertSelective(CoinSetting record);

    List<CoinSetting> selectByExample(CoinSettingExample example);

    CoinSetting selectByPrimaryKey(Byte id);

    int updateByExampleSelective(@Param("record") CoinSetting record, @Param("example") CoinSettingExample example);

    int updateByExample(@Param("record") CoinSetting record, @Param("example") CoinSettingExample example);

    int updateByPrimaryKeySelective(CoinSetting record);

    int updateByPrimaryKey(CoinSetting record);
}