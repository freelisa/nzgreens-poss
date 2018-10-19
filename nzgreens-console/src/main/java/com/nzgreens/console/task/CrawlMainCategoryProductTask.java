package com.nzgreens.console.task;

import com.nzgreens.common.enums.ProductsPriceChangeStatusEnum;
import com.nzgreens.common.utils.CurrencyUtil;
import com.nzgreens.console.service.IProductCrawlService;
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
@Lazy(false)
public class CrawlMainCategoryProductTask extends AbstractScheduleTask {
    private static final Logger logger = LoggerFactory.getLogger(CrawlMainCategoryProductTask.class);
    @Resource
    private IProductCrawlService productCrawlService;
    @Value("${images.host}")
    private String imagePath;
    @Value("${images.upload.path}")
    private String uploadPath="d:/新西兰直邮/";
    @Value("${images.product.icon.path}")
    private String productIconPath;
    @Value("${images.product.detail.path}")
    private String detailImagePath;


    //@Scheduled(cron = "${CrawlProductTask.cron:0 40 15 * * ?}")
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
                                Elements theparent = categoryDivChild.select("a.theparent");//分类名称
                                String[] hrefs = theparent.attr("href").split("\\?");
                                //保存大分类
                                String categoryPath = theparent.attr("href");
                                String categoryId = categoryPath.substring(categoryPath.lastIndexOf("=") + 1);

                                logger.info("---按照分类统计抓取：{}", theparent.html() + "," + hrefs[1]);

                                //productCrawlService.saveProductCrawl(con2,categoryPath,"",theparent.html(),categoryId,1);
                                productCrawlService.saveProductCrawl(con2,categoryPath);
                            }
                        }
                    }
                } catch (Exception e) {
                    logger.error("CrawlMainCategoryProductTask error data：{}", e);
                }
            }
        });
    }

}
