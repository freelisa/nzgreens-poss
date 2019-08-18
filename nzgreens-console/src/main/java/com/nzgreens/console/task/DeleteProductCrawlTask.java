package com.nzgreens.console.task;

import com.nzgreens.dal.user.mapper.SubProductsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;

/**
 * 删除待审核对应的抓取表数据
 * @author helizheng
 * @create 2017-09-19 16:36
 **/
@Component
@Lazy(false)
public class DeleteProductCrawlTask extends AbstractScheduleTask {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource
	private SubProductsMapper subProductsMapper;

	@Scheduled(cron = "${DeleteProductCrawlTask.cron:0 30 0 * * ?}")
	public void handle() {
		doHandle(this.getClass().getSimpleName(), new InvokerCallback() {
			@Override
			public void invoker() throws Exception {
			try {
				subProductsMapper.deleteProductCrawlBatch();
			} catch (Exception e) {
				logger.error("DeleteProductCrawlTask error data" , e);
			}
			}
		});
	}
}
