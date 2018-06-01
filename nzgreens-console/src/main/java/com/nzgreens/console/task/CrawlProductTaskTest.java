package com.nzgreens.console.task;

import com.nzgreens.common.enums.ProductsPriceChangeStatusEnum;
import com.nzgreens.common.utils.CurrencyUtil;
import com.nzgreens.console.util.ConvertUrlToMapUtil;
import com.nzgreens.dal.user.example.*;
import com.nzgreens.dal.user.mapper.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author helizheng
 * @create 2017-09-19 16:36
 **/
//@Component
//@Lazy(false)
public class CrawlProductTaskTest extends AbstractScheduleTask {
    private static final Logger logger = LoggerFactory.getLogger(CrawlProductTaskTest.class);
    @Resource
    private ProductsCrawlMapper productsCrawlMapper;
    @Value("${images.host}")
    private String imagePath;
    @Value("${images.upload.path}")
    private String uploadPath="d:/新西兰直邮/";
    @Value("${images.product.icon.path}")
    private String productIconPath;
    @Value("${images.product.detail.path}")
    private String detailImagePath;


    //@Scheduled(cron = "${CrawlProductTask.cron:30 50 1 * * ?}")
    public void handle() {
        doHandle(this.getClass().getSimpleName(), new InvokerCallback() {
            @Override
            public void invoker() throws Exception {
                try {
                    Connection con = Jsoup
                            .connect("http://gelin.nz/index.php?route=account/login");// 获取连接
                    con.header("User-Agent",
                            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36");// 配置模拟浏览器
                    Connection.Response rs = con.execute();// 获取响应
                    // 获取，cooking和表单属性，下面map存放post时的数据
                    Map<String, String> datas = new HashMap<>();
                    datas.put("email", "brucewang@gelin.nz");
                    datas.put("password", "gelinbruce");
                    /**
                     * 第二次请求，post表单数据，以及cookie信息
                     *
                     * **/
                    Connection con2 = Jsoup
                            .connect("http://gelin.nz/index.php?route=account/login");
                    con2.header("User-Agent",
                            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36");
                    // 设置cookie和post上面的map数据
                    Connection.Response login = con2.ignoreContentType(true).method(Connection.Method.POST)
                            .data(datas).cookies(rs.cookies()).execute();

                    Document doc = Jsoup.connect("http://www.gelin.nz/").get();
                    Elements lis = doc.select("#supermenu ul li.tlli");
                    Iterator<Element> iter = lis.iterator();

                    while (iter.hasNext()) {
                        Element e = iter.next();
                        Elements categoryDiv = e.select("div.bigdiv .withchild");
                        String allCategory = e.select("a.tll").html();
                        if (StringUtils.equals(allCategory, "全部分类")) {
                            Iterator<Element> categoryIter = categoryDiv.iterator();
                            while (categoryIter.hasNext()) {
                                Element categoryDivChild = categoryIter.next();

                                Elements childLevel = categoryDivChild.select("ul.child-level li a");
                                Iterator<Element> childLevelIter = childLevel.iterator();
                                while (childLevelIter.hasNext()) {
                                    Element child = childLevelIter.next();

                                    //获取小分类
                                    Document temp = null;
                                    try {
                                        temp = con2.url(child.attr("href")).get();
                                    } catch (IOException e1) {}
                                    if(temp == null){
                                        continue;
                                    }
                                    Elements last = temp.select("ul.pagination");
                                    int page = 1;
                                    if (last.size() > 0) {
                                        Element lastLi = last.select("li a").last();
                                        String lastHref = lastLi.attr("href");
                                        page = Integer.valueOf(lastHref.substring(lastHref.lastIndexOf("=") + 1));
                                    }

                                    for (int i = 1; i <= page; i++) {
                                        Document productList = null;
                                        try {
                                            productList = con2.url(child.attr("href") + "&page=" + page).get();
                                        } catch (IOException e1) {}
                                        if(productList == null){
                                            continue;
                                        }
                                        Elements productEle = productList.select(".product-thumb");
                                        Iterator<Element> productIter = productEle.iterator();
                                        while (productIter.hasNext()) {
                                            Element next = productIter.next();
                                            Elements link = next.select(".image a");
                                            //商品链接
                                            String linkHref = link.attr("href");

                                            Map<String, String> map = ConvertUrlToMapUtil.URLRequest(linkHref);

                                            //进入商品详情
                                            Document productDetail = null;
                                            try {
                                                productDetail = con2.url(linkHref).get();
                                            } catch (IOException e1) {}
                                            if(productDetail == null){
                                                continue;
                                            }
                                            Element productContent = productDetail.select("#content").first();

                                            Element productDesc = productContent.select(".col-sm-8").first();
                                            //商品title
                                            String productTitle = productDesc.select("h4 b").first().html();

                                            //价格
                                            Element price = productDesc.select("ul.pro-d").first();
                                            Elements priceEle = price.select("li");
                                            //销售价
                                            String priceNew = priceEle.select(".price-new-live").html();
                                            //市场价
                                            String priceOld = priceEle.select(".price-old-live").html();

                                            //查询产品是否存在，存在修改，不存在新增
                                            ProductsCrawlExample crawlExample = new ProductsCrawlExample();
                                            crawlExample.createCriteria().andReptileProductIdEqualTo(map.get("product_id"));
                                            List<ProductsCrawl> productsCrawls = productsCrawlMapper.selectByExample(crawlExample);
                                            if(CollectionUtils.isNotEmpty(productsCrawls)){
                                                //修改产品
                                                ProductsCrawl productsCrawl = productsCrawls.get(0);
                                                //标题、价格变动了
                                                Long costPrice = StringUtils.isNotBlank(priceOld) ? CurrencyUtil.convertYuanToFensTask(priceOld) : 0;
                                                Long sellPrice = StringUtils.isNotBlank(priceNew) ? CurrencyUtil.convertYuanToFensTask(priceNew) : 0;
                                                ProductsCrawl crawl = new ProductsCrawl();
                                                crawl.setId(productsCrawl.getId());

                                                //品牌
                                                Element brand = productDesc.select("ul.product-detail").first();
                                                //获取库存
                                                String[] bradHtml = new String[0];
                                                try {
                                                    bradHtml = brand.html().replaceAll("<!--|-->","").split("\n");
                                                } catch (Exception e1) {

                                                }
                                                Integer stock = 0;
                                                if (bradHtml != null && bradHtml.length > 0) {
                                                    for (String s : bradHtml) {
                                                        if (StringUtils.isNotBlank(s) && s.contains("库 存")) {
                                                            Pattern p = Pattern.compile("[^0-9]");
                                                            try {
                                                                Matcher m = p.matcher(s);
                                                                stock = Integer.valueOf(m.replaceAll("").trim());
                                                            } catch (NumberFormatException e1) {

                                                            }

                                                        }
                                                    }
                                                }
//                                                if(costPrice != null && costPrice != 0){
//                                                    crawl.setCostPrice(costPrice);
//                                                }
//                                                if(sellPrice != null && sellPrice != 0){
//                                                    crawl.setSellingPrice(sellPrice);
//                                                }
//                                                if(costPrice == null || costPrice == 0){
//                                                    crawl.setCostPrice(sellPrice);
//                                                    crawl.setSellingPrice(sellPrice);
//                                                }
//                                                if(sellPrice == null || sellPrice == 0){
//                                                    crawl.setCostPrice(costPrice);
//                                                    crawl.setSellingPrice(costPrice);
//                                                }
                                                if(stock != null && stock != 0){
                                                    crawl.setStock(stock);
                                                }else{
                                                    crawl.setStock(999);
                                                }
                                                crawl.setUpdateTime(new Date());
                                                productsCrawlMapper.updateByPrimaryKeySelective(crawl);
                                                logger.info("-----------------------链接：{}", linkHref);
                                                logger.info("-----------------------标题：{}", productTitle);
                                                logger.info("-----------------------原价：{}", costPrice);
                                                logger.info("-----------------------售价：{}", sellPrice);
                                                logger.info("-----------------------库存：{}", stock);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    logger.error("CrawlProductTask error data：{}", e.getMessage());
                }
            }
        });
    }
}
