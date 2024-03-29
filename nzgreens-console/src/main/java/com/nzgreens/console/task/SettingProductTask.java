package com.nzgreens.console.task;

import com.nzgreens.console.service.IProductTaskService;
import com.nzgreens.dal.user.example.CoinSetting;
import com.nzgreens.dal.user.example.Products;
import com.nzgreens.dal.user.example.ProductsCrawl;
import com.nzgreens.dal.user.example.ProductsExample;
import com.nzgreens.dal.user.mapper.CoinSettingMapper;
import com.nzgreens.dal.user.mapper.ProductsCrawlMapper;
import com.nzgreens.dal.user.mapper.ProductsMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author helizheng
 * @create 2017-09-19 16:36
 **/
@Component
@Lazy(false)
public class SettingProductTask extends AbstractScheduleTask {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	private IProductTaskService productTaskService;
	@Resource
	private ProductsMapper productsMapper;
	@Resource
	private ProductsCrawlMapper productsCrawlMapper;
	@Resource
	private CoinSettingMapper coinSettingMapper;
	@Value("${images.host}")
	private String imagePath;
	@Value("${images.product.detail.path}")
	private String detailImagePath;

	@Scheduled(cron = "${SettingProductTask.cron:0 0 1 * * ?}")
	public void handle() {
		doHandle(this.getClass().getSimpleName(), new InvokerCallback() {
			@Override
			public void invoker() throws Exception {
				settingProducts();
			}
		});
	}

	public void settingProducts() {
		try {
			List<ProductsCrawl> productsCrawls = productTaskService.queryProductIsExists();
			logger.info("SettingProductTask productsCrawls size : {}",productsCrawls.size());
			if(CollectionUtils.isNotEmpty(productsCrawls)){

				List<CoinSetting> coinSettings = coinSettingMapper.selectByExample(null);
				Long money = null;
				if(CollectionUtils.isEmpty(coinSettings)){
					money = 1L;
				}
				//RMB 分-> 金币
				money = new BigDecimal(coinSettings.get(0).getMoney()).divide(
						new BigDecimal(coinSettings.get(0).getCoin() == null ? 1L :coinSettings.get(0).getCoin()))
								.setScale(BigDecimal.ROUND_UP).longValue();

				ProductsExample example = new ProductsExample();
				for(int i = 0,length = productsCrawls.size();i < length;i++){
					ProductsCrawl crawl = productsCrawls.get(i);
					//logger.info("ProductsCrawl is {}", JSON.toJSONString(crawl));
					//查询格林商品是否存在商品表，存在修改缩略图和详情，不存在新增
					example.clear();
					example.createCriteria().andGelinProductIdEqualTo(Long.valueOf(crawl.getReptileProductId()));
					List<Products> products = productsMapper.selectByExample(example);
					if(CollectionUtils.isEmpty(products)){
						Products pro = new Products();
						if(StringUtils.isNotEmpty(crawl.getBrandId())){
							pro.setBrandId(Long.valueOf(crawl.getBrandId()));
						}
						if(StringUtils.isNotEmpty(crawl.getCategoryId())){
							pro.setCategoryId(Long.valueOf(crawl.getCategoryId()));
						}

						BigDecimal sell = new BigDecimal(crawl.getSellingPrice());
						BigDecimal cost = new BigDecimal(crawl.getCostPrice());
						BigDecimal costBig = null;
						if(crawl.getCostPrice() == null){
							costBig = new BigDecimal("0");
							logger.error("crawl product cost price null!!----->"+crawl.getReptileProductId());
						}else{
							costBig = cost.divide(new BigDecimal(money)).setScale(0, BigDecimal.ROUND_UP);
						}
						BigDecimal sellBig = null;
						if(crawl.getSellingPrice() == null){
							sellBig = new BigDecimal("0");
							logger.error("crawl product sell price null!!----->"+crawl.getReptileProductId());
						}else{
							sellBig = sell.divide(new BigDecimal(money)).setScale(0, BigDecimal.ROUND_UP);
						}
						pro.setCostPrice(costBig.longValue());
						pro.setSellingPrice(sellBig.longValue());
						StringBuilder buff = new StringBuilder();
						if(StringUtils.isNotEmpty(crawl.getDetail())){
							String[] imgs = crawl.getDetail().split(",");
							buff.append("<div>");
							for(int j = 0,len = imgs.length;j < len;j++){
								//域名+图片地址+图片名称
								buff.append("<p><img src=\"").append(imagePath + detailImagePath + "/" + imgs[j]).append("\" /></p>");
							}
							buff.append("</div>");
						}
						pro.setDetail(buff.toString());
						if(StringUtils.isNotBlank(crawl.getImage())){
							pro.setImage(crawl.getImage());
						}
						pro.setTitle(crawl.getTitle());
						if(StringUtils.isNotBlank(crawl.getParentCategoryId())){
							pro.setParentCategoryId(Long.valueOf(crawl.getParentCategoryId()));
						}
						if(StringUtils.isNotEmpty(crawl.getWeight())){
							String[] split = crawl.getWeight().split("\\.");
							pro.setWeight(Long.valueOf(split[0]));
						}
						if(crawl.getStock() == null){
							pro.setStock(80L);
						}else{
							pro.setStock(crawl.getStock().longValue());
						}
						pro.setGelinProductId(Long.valueOf(crawl.getReptileProductId()));
						//设置商品未生效
						pro.setIsValid(0);
						productsMapper.insertSelective(pro);

						crawl.setProductId(pro.getId());
						crawl.setState(1);
						productsCrawlMapper.updateByPrimaryKeySelective(crawl);
					}else{
						Products proModel = products.get(0);
						proModel.setTitle(crawl.getTitle());
						//无效更新detail,image
						if (Integer.valueOf(0).equals(proModel.getIsValid())) {
							StringBuilder buff = new StringBuilder();
							if(StringUtils.isNotEmpty(crawl.getDetail())){
								String[] imgs = crawl.getDetail().split(",");
								buff.append("<div>");
								for(int j = 0,len = imgs.length;j < len;j++){
									//域名+图片地址+图片名称
									buff.append("<p><img src=\"").append(imagePath + detailImagePath + "/" + imgs[j]).append("\" /></p>");
								}
								buff.append("</div>");
							}
							proModel.setDetail(buff.toString());
							if(StringUtils.isNotBlank(crawl.getImage())){
								proModel.setImage(crawl.getImage());
							}
						}
						productsMapper.updateByPrimaryKeySelective(proModel);

						crawl.setProductId(proModel.getId());
						crawl.setState(1);
						productsCrawlMapper.updateByPrimaryKeySelective(crawl);
					}

				}
			}
		} catch (Exception e) {
			logger.error("SettingProductTask error data" , e);
		}
	}
}
