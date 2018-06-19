package com.nzgreens.dal.user.example;

import com.nzgreens.dal.Limit;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AgentRebateAuditExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private Limit limit;

    public AgentRebateAuditExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    public void setLimit(Limit limit) {
        this.limit = limit;
    }

    public Limit getLimit() {
        return this.limit;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andAgentUserIdIsNull() {
            addCriterion("agent_user_id is null");
            return (Criteria) this;
        }

        public Criteria andAgentUserIdIsNotNull() {
            addCriterion("agent_user_id is not null");
            return (Criteria) this;
        }

        public Criteria andAgentUserIdEqualTo(Long value) {
            addCriterion("agent_user_id =", value, "agentUserId");
            return (Criteria) this;
        }

        public Criteria andAgentUserIdNotEqualTo(Long value) {
            addCriterion("agent_user_id <>", value, "agentUserId");
            return (Criteria) this;
        }

        public Criteria andAgentUserIdGreaterThan(Long value) {
            addCriterion("agent_user_id >", value, "agentUserId");
            return (Criteria) this;
        }

        public Criteria andAgentUserIdGreaterThanOrEqualTo(Long value) {
            addCriterion("agent_user_id >=", value, "agentUserId");
            return (Criteria) this;
        }

        public Criteria andAgentUserIdLessThan(Long value) {
            addCriterion("agent_user_id <", value, "agentUserId");
            return (Criteria) this;
        }

        public Criteria andAgentUserIdLessThanOrEqualTo(Long value) {
            addCriterion("agent_user_id <=", value, "agentUserId");
            return (Criteria) this;
        }

        public Criteria andAgentUserIdIn(List<Long> values) {
            addCriterion("agent_user_id in", values, "agentUserId");
            return (Criteria) this;
        }

        public Criteria andAgentUserIdNotIn(List<Long> values) {
            addCriterion("agent_user_id not in", values, "agentUserId");
            return (Criteria) this;
        }

        public Criteria andAgentUserIdBetween(Long value1, Long value2) {
            addCriterion("agent_user_id between", value1, value2, "agentUserId");
            return (Criteria) this;
        }

        public Criteria andAgentUserIdNotBetween(Long value1, Long value2) {
            addCriterion("agent_user_id not between", value1, value2, "agentUserId");
            return (Criteria) this;
        }

        public Criteria andUserOrderIdIsNull() {
            addCriterion("user_order_id is null");
            return (Criteria) this;
        }

        public Criteria andUserOrderIdIsNotNull() {
            addCriterion("user_order_id is not null");
            return (Criteria) this;
        }

        public Criteria andUserOrderIdEqualTo(Long value) {
            addCriterion("user_order_id =", value, "userOrderId");
            return (Criteria) this;
        }

        public Criteria andUserOrderIdNotEqualTo(Long value) {
            addCriterion("user_order_id <>", value, "userOrderId");
            return (Criteria) this;
        }

        public Criteria andUserOrderIdGreaterThan(Long value) {
            addCriterion("user_order_id >", value, "userOrderId");
            return (Criteria) this;
        }

        public Criteria andUserOrderIdGreaterThanOrEqualTo(Long value) {
            addCriterion("user_order_id >=", value, "userOrderId");
            return (Criteria) this;
        }

        public Criteria andUserOrderIdLessThan(Long value) {
            addCriterion("user_order_id <", value, "userOrderId");
            return (Criteria) this;
        }

        public Criteria andUserOrderIdLessThanOrEqualTo(Long value) {
            addCriterion("user_order_id <=", value, "userOrderId");
            return (Criteria) this;
        }

        public Criteria andUserOrderIdIn(List<Long> values) {
            addCriterion("user_order_id in", values, "userOrderId");
            return (Criteria) this;
        }

        public Criteria andUserOrderIdNotIn(List<Long> values) {
            addCriterion("user_order_id not in", values, "userOrderId");
            return (Criteria) this;
        }

        public Criteria andUserOrderIdBetween(Long value1, Long value2) {
            addCriterion("user_order_id between", value1, value2, "userOrderId");
            return (Criteria) this;
        }

        public Criteria andUserOrderIdNotBetween(Long value1, Long value2) {
            addCriterion("user_order_id not between", value1, value2, "userOrderId");
            return (Criteria) this;
        }

        public Criteria andTypeIsNull() {
            addCriterion("type is null");
            return (Criteria) this;
        }

        public Criteria andTypeIsNotNull() {
            addCriterion("type is not null");
            return (Criteria) this;
        }

        public Criteria andTypeEqualTo(Byte value) {
            addCriterion("type =", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotEqualTo(Byte value) {
            addCriterion("type <>", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThan(Byte value) {
            addCriterion("type >", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("type >=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThan(Byte value) {
            addCriterion("type <", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThanOrEqualTo(Byte value) {
            addCriterion("type <=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeIn(List<Byte> values) {
            addCriterion("type in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotIn(List<Byte> values) {
            addCriterion("type not in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeBetween(Byte value1, Byte value2) {
            addCriterion("type between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("type not between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andAmountIsNull() {
            addCriterion("amount is null");
            return (Criteria) this;
        }

        public Criteria andAmountIsNotNull() {
            addCriterion("amount is not null");
            return (Criteria) this;
        }

        public Criteria andAmountEqualTo(Long value) {
            addCriterion("amount =", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountNotEqualTo(Long value) {
            addCriterion("amount <>", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountGreaterThan(Long value) {
            addCriterion("amount >", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountGreaterThanOrEqualTo(Long value) {
            addCriterion("amount >=", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountLessThan(Long value) {
            addCriterion("amount <", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountLessThanOrEqualTo(Long value) {
            addCriterion("amount <=", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountIn(List<Long> values) {
            addCriterion("amount in", values, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountNotIn(List<Long> values) {
            addCriterion("amount not in", values, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountBetween(Long value1, Long value2) {
            addCriterion("amount between", value1, value2, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountNotBetween(Long value1, Long value2) {
            addCriterion("amount not between", value1, value2, "amount");
            return (Criteria) this;
        }

        public Criteria andRebatePriceIsNull() {
            addCriterion("rebate_price is null");
            return (Criteria) this;
        }

        public Criteria andRebatePriceIsNotNull() {
            addCriterion("rebate_price is not null");
            return (Criteria) this;
        }

        public Criteria andRebatePriceEqualTo(Long value) {
            addCriterion("rebate_price =", value, "rebatePrice");
            return (Criteria) this;
        }

        public Criteria andRebatePriceNotEqualTo(Long value) {
            addCriterion("rebate_price <>", value, "rebatePrice");
            return (Criteria) this;
        }

        public Criteria andRebatePriceGreaterThan(Long value) {
            addCriterion("rebate_price >", value, "rebatePrice");
            return (Criteria) this;
        }

        public Criteria andRebatePriceGreaterThanOrEqualTo(Long value) {
            addCriterion("rebate_price >=", value, "rebatePrice");
            return (Criteria) this;
        }

        public Criteria andRebatePriceLessThan(Long value) {
            addCriterion("rebate_price <", value, "rebatePrice");
            return (Criteria) this;
        }

        public Criteria andRebatePriceLessThanOrEqualTo(Long value) {
            addCriterion("rebate_price <=", value, "rebatePrice");
            return (Criteria) this;
        }

        public Criteria andRebatePriceIn(List<Long> values) {
            addCriterion("rebate_price in", values, "rebatePrice");
            return (Criteria) this;
        }

        public Criteria andRebatePriceNotIn(List<Long> values) {
            addCriterion("rebate_price not in", values, "rebatePrice");
            return (Criteria) this;
        }

        public Criteria andRebatePriceBetween(Long value1, Long value2) {
            addCriterion("rebate_price between", value1, value2, "rebatePrice");
            return (Criteria) this;
        }

        public Criteria andRebatePriceNotBetween(Long value1, Long value2) {
            addCriterion("rebate_price not between", value1, value2, "rebatePrice");
            return (Criteria) this;
        }

        public Criteria andActualRebatePriceIsNull() {
            addCriterion("actual_rebate_price is null");
            return (Criteria) this;
        }

        public Criteria andActualRebatePriceIsNotNull() {
            addCriterion("actual_rebate_price is not null");
            return (Criteria) this;
        }

        public Criteria andActualRebatePriceEqualTo(Long value) {
            addCriterion("actual_rebate_price =", value, "actualRebatePrice");
            return (Criteria) this;
        }

        public Criteria andActualRebatePriceNotEqualTo(Long value) {
            addCriterion("actual_rebate_price <>", value, "actualRebatePrice");
            return (Criteria) this;
        }

        public Criteria andActualRebatePriceGreaterThan(Long value) {
            addCriterion("actual_rebate_price >", value, "actualRebatePrice");
            return (Criteria) this;
        }

        public Criteria andActualRebatePriceGreaterThanOrEqualTo(Long value) {
            addCriterion("actual_rebate_price >=", value, "actualRebatePrice");
            return (Criteria) this;
        }

        public Criteria andActualRebatePriceLessThan(Long value) {
            addCriterion("actual_rebate_price <", value, "actualRebatePrice");
            return (Criteria) this;
        }

        public Criteria andActualRebatePriceLessThanOrEqualTo(Long value) {
            addCriterion("actual_rebate_price <=", value, "actualRebatePrice");
            return (Criteria) this;
        }

        public Criteria andActualRebatePriceIn(List<Long> values) {
            addCriterion("actual_rebate_price in", values, "actualRebatePrice");
            return (Criteria) this;
        }

        public Criteria andActualRebatePriceNotIn(List<Long> values) {
            addCriterion("actual_rebate_price not in", values, "actualRebatePrice");
            return (Criteria) this;
        }

        public Criteria andActualRebatePriceBetween(Long value1, Long value2) {
            addCriterion("actual_rebate_price between", value1, value2, "actualRebatePrice");
            return (Criteria) this;
        }

        public Criteria andActualRebatePriceNotBetween(Long value1, Long value2) {
            addCriterion("actual_rebate_price not between", value1, value2, "actualRebatePrice");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("status is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("status is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(Byte value) {
            addCriterion("status =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(Byte value) {
            addCriterion("status <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(Byte value) {
            addCriterion("status >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("status >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(Byte value) {
            addCriterion("status <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(Byte value) {
            addCriterion("status <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<Byte> values) {
            addCriterion("status in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<Byte> values) {
            addCriterion("status not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(Byte value1, Byte value2) {
            addCriterion("status between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(Byte value1, Byte value2) {
            addCriterion("status not between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andRemarkIsNull() {
            addCriterion("remark is null");
            return (Criteria) this;
        }

        public Criteria andRemarkIsNotNull() {
            addCriterion("remark is not null");
            return (Criteria) this;
        }

        public Criteria andRemarkEqualTo(String value) {
            addCriterion("remark =", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotEqualTo(String value) {
            addCriterion("remark <>", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkGreaterThan(String value) {
            addCriterion("remark >", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkGreaterThanOrEqualTo(String value) {
            addCriterion("remark >=", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLessThan(String value) {
            addCriterion("remark <", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLessThanOrEqualTo(String value) {
            addCriterion("remark <=", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLike(String value) {
            addCriterion("remark like", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotLike(String value) {
            addCriterion("remark not like", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkIn(List<String> values) {
            addCriterion("remark in", values, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotIn(List<String> values) {
            addCriterion("remark not in", values, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkBetween(String value1, String value2) {
            addCriterion("remark between", value1, value2, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotBetween(String value1, String value2) {
            addCriterion("remark not between", value1, value2, "remark");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("update_time is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("update_time is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("update_time =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("update_time <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("update_time >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("update_time >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("update_time <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("update_time <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("update_time in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("update_time not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("update_time between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("update_time not between", value1, value2, "updateTime");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}