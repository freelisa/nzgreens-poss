package com.nzgreens.dal.user.mapper;

import com.nzgreens.dal.user.example.AgentMonthRebate;
import com.nzgreens.dal.user.example.AgentMonthRebateExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AgentMonthRebateMapper {
    int countByExample(AgentMonthRebateExample example);

    int deleteByExample(AgentMonthRebateExample example);

    int deleteByPrimaryKey(Long id);

    int insert(AgentMonthRebate record);

    int insertSelective(AgentMonthRebate record);

    List<AgentMonthRebate> selectByExample(AgentMonthRebateExample example);

    AgentMonthRebate selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") AgentMonthRebate record, @Param("example") AgentMonthRebateExample example);

    int updateByExample(@Param("record") AgentMonthRebate record, @Param("example") AgentMonthRebateExample example);

    int updateByPrimaryKeySelective(AgentMonthRebate record);

    int updateByPrimaryKey(AgentMonthRebate record);
}