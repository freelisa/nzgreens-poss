package com.nzgreens.dal.user.mapper;

import com.nzgreens.dal.user.example.UserAgent;
import com.nzgreens.dal.user.example.UserAgentExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserAgentMapper {
    int countByExample(UserAgentExample example);

    int deleteByExample(UserAgentExample example);

    int deleteByPrimaryKey(Long id);

    int insert(UserAgent record);

    int insertSelective(UserAgent record);

    List<UserAgent> selectByExample(UserAgentExample example);

    UserAgent selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") UserAgent record, @Param("example") UserAgentExample example);

    int updateByExample(@Param("record") UserAgent record, @Param("example") UserAgentExample example);

    int updateByPrimaryKeySelective(UserAgent record);

    int updateByPrimaryKey(UserAgent record);
}