package com.nzgreens.dal.user.mapper;

import com.nzgreens.dal.user.example.AgentRebateAudit;
import com.nzgreens.dal.user.example.AgentRebateAuditExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AgentRebateAuditMapper {
    int countByExample(AgentRebateAuditExample example);

    int deleteByExample(AgentRebateAuditExample example);

    int deleteByPrimaryKey(Long id);

    int insert(AgentRebateAudit record);

    int insertSelective(AgentRebateAudit record);

    List<AgentRebateAudit> selectByExample(AgentRebateAuditExample example);

    AgentRebateAudit selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") AgentRebateAudit record, @Param("example") AgentRebateAuditExample example);

    int updateByExample(@Param("record") AgentRebateAudit record, @Param("example") AgentRebateAuditExample example);

    int updateByPrimaryKeySelective(AgentRebateAudit record);

    int updateByPrimaryKey(AgentRebateAudit record);
}