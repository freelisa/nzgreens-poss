package com.nzgreens.console.task;

import com.nzgreens.console.service.IProductCrawlService;
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
import java.util.*;

/**
 * @author helizheng
 * @create 2017-09-19 16:36
 **/
@Component
@Lazy(false)
public class CrawlNewsProductTask extends AbstractScheduleTask {
    private static final Logger logger = LoggerFactory.getLogger(CrawlNewsProductTask.class);
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


    @Scheduled(cron = "${CrawlProductTask.cron:0 0 3 * * ?}")
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
                        Elements linkTops = e.select("div.bigdiv .linkoftopitem");
                        Iterator<Element> linktopIter = linkTops.iterator();
                        while (linktopIter.hasNext()) {
                            Element linkTop = linktopIter.next();
                            Elements theparent = linkTop.select("a");
                            String[] hrefs = theparent.attr("href").split("\\?");

                            String categoryPath = "http://gelin.nz" + theparent.attr("href");
                            logger.info("---大分类：{}", theparent.html() + "," + hrefs[1]);

                            productCrawlService.saveProductCrawl(con2,categoryPath,"","","",1);
                        }
                    }
                } catch (Exception e) {
                    logger.error("CrawlNewsProductTask error data：{}", e);
                }
            }
        });
    }
}
