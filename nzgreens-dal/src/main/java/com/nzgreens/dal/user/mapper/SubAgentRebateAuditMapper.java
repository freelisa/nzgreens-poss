package com.nzgreens.dal.user.mapper;

import com.nzgreens.common.form.console.AgentRebateAuditForm;
import com.nzgreens.common.model.console.AgentRebateAuditModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SubAgentRebateAuditMapper {
	List<AgentRebateAuditModel> selectAgentRebateAuditForPage(AgentRebateAuditForm form) throws Exception;

	AgentRebateAuditModel selectAgentRebateAuditDetail(@Param("id") Long id) throws Exception;
}
