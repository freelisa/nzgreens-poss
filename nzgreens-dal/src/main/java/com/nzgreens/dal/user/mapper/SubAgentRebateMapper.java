package com.nzgreens.dal.user.mapper;

import com.nzgreens.common.form.console.AgentRebateForm;
import com.nzgreens.common.model.console.AgentRebateModel;

import java.util.List;

public interface SubAgentRebateMapper {
    List<AgentRebateModel> selectForPage(AgentRebateForm form) throws Exception;
}