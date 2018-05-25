package com.nzgreens.console.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 定时器抽象类
 *
 * @author hcf
 * @version V1.0
 * @date 2016/8/27 13:50
 */
public abstract class AbstractScheduleTask {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(AbstractScheduleTask.class);

    protected interface InvokerCallback {
        void invoker() throws Exception;
    }

    public void doHandle(String clz, InvokerCallback call) {
        LOGGER.info(clz + " task start!");
        try {
            call.invoker();
        } catch (Exception e) {
            LOGGER.error(clz + " excuted was error!  ", e);
        }
        LOGGER.info(clz + " task end!");
    }
}
