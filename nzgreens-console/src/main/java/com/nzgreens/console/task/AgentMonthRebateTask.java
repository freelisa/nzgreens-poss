package com.nzgreens.console.task;

import com.alibaba.fastjson.JSON;
import com.nzgreens.common.enums.AgentRebateAuditStatusEnum;
import com.nzgreens.common.enums.AgentRebateTypeEnum;
import com.nzgreens.common.enums.UserTypeEnum;
import com.nzgreens.common.model.console.UserOrderPriceSumModel;
import com.nzgreens.common.utils.DateUtil;
import com.nzgreens.dal.user.example.*;
import com.nzgreens.dal.user.mapper.*;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

/**
 * @Author:helizheng
 * @Date: Created in 2018/5/19 23:37
 */
@Component
@Lazy(false)
public class AgentMonthRebateTask extends AbstractScheduleTask {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private AgentMonthRebateMapper agentMonthRebateMapper;
    @Resource
    private AgentRebateAuditMapper agentRebateAuditMapper;
    @Resource
    private SubProductsMapper subProductsMapper;
    private static final BigDecimal HUNDRED = new BigDecimal("100");

    //@Scheduled(cron = "${AgentMonthRebateTask.cron:0 0 1 1 * ?}")
    @Scheduled(cron = "${AgentMonthRebateTask.cron:0 41 18 * * ?}")
    public void handle() {
        doHandle(this.getClass().getSimpleName(), new InvokerCallback() {
            @Override
            public void invoker() throws Exception {
                try {
                    Calendar calendar1 = Calendar.getInstance();
                    calendar1.add(Calendar.MONTH,-1);
                    calendar1.set(Calendar.DAY_OF_MONTH,1);
                    String startTime = DateUtil.format(calendar1.getTime());
                    startTime += " 00:00:00";
                    Calendar calendar2 = Calendar.getInstance();
                    calendar2.add(Calendar.MONTH,-1);
                    calendar2.set(Calendar.DAY_OF_MONTH,calendar2.getActualMaximum(Calendar.DAY_OF_MONTH));
                    String endTime = DateUtil.format(calendar2.getTime());
                    endTime += " 23:59:59";
                    List<UserOrderPriceSumModel> userOrderPriceSumModels = subProductsMapper.selectAgentMonthRebate(startTime, endTime);
                    if(CollectionUtils.isEmpty(userOrderPriceSumModels)){
                        return;
                    }
                    for(UserOrderPriceSumModel model : userOrderPriceSumModels){
                        AgentMonthRebateExample example = new AgentMonthRebateExample();
                        example.createCriteria().andAmountLessThanOrEqualTo(model.getPrice());
                        example.setOrderByClause(" amount desc");
                        List<AgentMonthRebate> agentRebates = agentMonthRebateMapper.selectByExample(example);
                        if(CollectionUtils.isNotEmpty(agentRebates)){
                            AgentMonthRebate agentRebate = agentRebates.get(0);
                            BigDecimal rebateDec = new BigDecimal(agentRebate.getMonthRebate());
                            BigDecimal priceDec = new BigDecimal(model.getPrice());
                            BigDecimal divide = rebateDec.multiply(priceDec).divide(HUNDRED)
                                    .setScale(0,BigDecimal.ROUND_HALF_UP);

                            AgentRebateAudit audit = new AgentRebateAudit();
                            audit.setAgentUserId(model.getUserId());
                            audit.setType((byte) AgentRebateTypeEnum._MONTH.getValue());
                            audit.setRebatePrice(divide.longValue());
                            audit.setStatus((byte) AgentRebateAuditStatusEnum._PENDING.getValue());
                            agentRebateAuditMapper.insertSelective(audit);

                            logger.info("date: {} ~ {},month rebate set: {},agent month rebate: {}", startTime,endTime,agentRebate.getMonthRebate(),JSON.toJSONString(audit));
                        }
                    }
                } catch (Exception e) {
                    logger.error("AgentMonthRebateTask error data" , e);
                }
            }
        });
    }

    public static void main(String[] args) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(Calendar.MONTH,-1);
        calendar1.set(Calendar.DAY_OF_MONTH,1);
        String startTime = DateUtil.format(calendar1.getTime());
        startTime += " 00:00:00";
        Calendar calendar2 = Calendar.getInstance();
        calendar2.add(Calendar.MONTH,-1);
        calendar2.set(Calendar.DAY_OF_MONTH,calendar2.getActualMaximum(Calendar.DAY_OF_MONTH));
        String endTime = DateUtil.format(calendar2.getTime());
        endTime += " 23:59:59";

        System.out.println(startTime + "_" + endTime);
    }
}
