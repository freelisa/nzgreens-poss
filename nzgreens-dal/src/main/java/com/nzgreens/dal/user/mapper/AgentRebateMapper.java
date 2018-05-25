package com.nzgreens.dal.user.mapper;

import com.nzgreens.dal.user.example.AgentRebate;
import com.nzgreens.dal.user.example.AgentRebateExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AgentRebateMapper {
    int countByExample(AgentRebateExample example);

    int deleteByExample(AgentRebateExample example);

    int deleteByPrimaryKey(Long id);

    int insert(AgentRebate record);

    int insertSelective(AgentRebate record);

    List<AgentRebate> selectByExample(AgentRebateExample example);

    AgentRebate selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") AgentRebate record, @Param("example") AgentRebateExample example);

    int updateByExample(@Param("record") AgentRebate record, @Param("example") AgentRebateExample example);

    int updateByPrimaryKeySelective(AgentRebate record);

    int updateByPrimaryKey(AgentRebate record);
}