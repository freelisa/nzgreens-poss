package com.nzgreens.dal.user.mapper;

import com.nzgreens.dal.user.example.AgentUserShop;
import com.nzgreens.dal.user.example.AgentUserShopExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AgentUserShopMapper {
    int countByExample(AgentUserShopExample example);

    int deleteByExample(AgentUserShopExample example);

    int deleteByPrimaryKey(Long id);

    int insert(AgentUserShop record);

    int insertSelective(AgentUserShop record);

    List<AgentUserShop> selectByExample(AgentUserShopExample example);

    AgentUserShop selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") AgentUserShop record, @Param("example") AgentUserShopExample example);

    int updateByExample(@Param("record") AgentUserShop record, @Param("example") AgentUserShopExample example);

    int updateByPrimaryKeySelective(AgentUserShop record);

    int updateByPrimaryKey(AgentUserShop record);
}