package com.nzgreens.dal.user.mapper;

import com.nzgreens.dal.user.example.AgentProducts;
import com.nzgreens.dal.user.example.AgentProductsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AgentProductsMapper {
    int countByExample(AgentProductsExample example);

    int deleteByExample(AgentProductsExample example);

    int deleteByPrimaryKey(Long id);

    int insert(AgentProducts record);

    int insertSelective(AgentProducts record);

    List<AgentProducts> selectByExample(AgentProductsExample example);

    AgentProducts selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") AgentProducts record, @Param("example") AgentProductsExample example);

    int updateByExample(@Param("record") AgentProducts record, @Param("example") AgentProductsExample example);

    int updateByPrimaryKeySelective(AgentProducts record);

    int updateByPrimaryKey(AgentProducts record);
}