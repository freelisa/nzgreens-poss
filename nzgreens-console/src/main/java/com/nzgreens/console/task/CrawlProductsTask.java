package com.nzgreens.console.task;

import com.nzgreens.console.service.IProductCrawlService;
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
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

/**
 * @author helizheng
 * @create 2017-09-19 16:36
 **/
@Component
@Lazy(false)
public class CrawlProductsTask extends AbstractScheduleTask {
    private static final Logger logger = LoggerFactory.getLogger(CrawlProductsTask.class);
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
    BlockingQueue queue = new ArrayBlockingQueue(100);
    private ThreadPoolExecutor executor = new ThreadPoolExecutor(8,16,3600, TimeUnit.SECONDS,queue);


    @Scheduled(cron = "${CrawlProductTask.cron:0 25 20 * * ?}")
    public void handle() {
        doHandle(this.getClass().getSimpleName(), new InvokerCallback() {
            @Override
            public void invoker() throws Exception {
                try {
                    Connection con2 = getConnection();

                    Document doc = Jsoup.connect("http://www.gelin.nz/").get();
                    Elements lis = doc.select("#supermenu ul li.tlli");
                    Iterator<Element> iter = lis.iterator();

                    while (iter.hasNext()) {
                        Element e = iter.next();
                        String allCategory = e.select("a.tll").html();
                        logger.info("---------------------");
                        logger.info("当前主菜单：{}", allCategory);

                        //查看子菜单
                        Elements categoryDiv = e.select("div.bigdiv .withchild");
                        //查看商品
                        Elements productsDiv = e.select("div.bigdiv .withimage");
                        //查看全部链接
                        Element toplink = e.select("div.bigdiv .linkoftopitem").first();
                        //存在子菜单
                        if (CollectionUtils.isNotEmpty(categoryDiv)) {
                            Iterator<Element> categoryIter = categoryDiv.iterator();
                            while (categoryIter.hasNext()) {
                                Element child = categoryIter.next();
                                //分类名称
                                Elements theparent = child.select("a.theparent");
                                String[] hrefs = theparent.attr("href").split("\\?");
                                logger.info("当前子菜单：{}",theparent.html());
                                //分类子菜单
                                Elements menuList = child.select("ul.child-level li");
                                if (CollectionUtils.isNotEmpty(menuList)) {
                                    logger.info("子菜单有子菜单，进入子菜单");
                                    executor.execute(new ChildMenuRunnable(con2,menuList));
                                } else {
                                    logger.info("子菜单没有子菜单，进入当前菜单");
                                    if (allCategory.equals("首页")) {

                                        logger.info("加载首页");
                                        Document temp = null;
                                        try {
                                            temp = con2.url("http://gelin.nz/index.php").get();
                                        } catch (IOException e1) {}
                                        if(temp == null){
                                            return;
                                        }
                                        executor.execute(new MenuProductsRunnable(con2,temp.select("div.row .product-thumb")));
                                    } else {
                                        executor.execute(new MainMenuRunnable(con2, hrefs[1]));
                                    }
                                }

                            }

                        }
                        //存在商品
                        if (CollectionUtils.isNotEmpty(productsDiv)) {
                            logger.info("当前菜单存在商品");
                            executor.execute(new MenuProductsRunnable(con2, productsDiv));
                        }
                        //存在全部商品链接
                        if (toplink != null) {
                            logger.info("当前菜单存在全部链接");
                            executor.execute(new MenuTopLinkRunnable(con2, toplink));
                        }
                        //单菜单
                        String[] menuHrefs = e.select("a.tll").attr("href").split("\\?");
                        if (menuHrefs != null && menuHrefs.length > 1) {
                            String categoryPath = "http://gelin.nz" + menuHrefs[1];
                            productCrawlService.saveProductCrawl(con2, categoryPath);
                        }
                    }
                    logger.info("---------------------\n");
                } catch (Exception e) {
                    logger.error("CrawlMainCategoryProductTask error data：{}", e);
                }
            }
        });
    }

    public static Connection getConnection() throws IOException {
        Connection con = Jsoup
                .connect("http://gelin.nz/index.php?route=account/login");
        // 获取连接
        con.header("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36");
        // 配置模拟浏览器
        Connection.Response rs = con.execute();
        // 获取响应
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

        con2.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        con2.header("Accept-Encoding", "gzip, deflate, sdch");
        con2.header("Accept-Language", "zh-CN,zh;q=0.8");
        con2.header("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36");
        // 设置cookie和post上面的map数据
        Connection.Response login = con2.ignoreContentType(true).method(Connection.Method.POST)
                .data(datas).cookies(rs.cookies()).execute();
        return con2;
    }

    /**
     * 子菜单爬取
     */
    class ChildMenuRunnable implements Runnable {
        private Connection connection;
        private Elements menuList;
        public ChildMenuRunnable(Connection con2, Elements menuList) {
            this.connection=con2;
            this.menuList = menuList;
        }

        @Override
        public void run() {
            if (CollectionUtils.isNotEmpty(menuList)) {
                menuList.stream().forEach(element -> {
                    String href = element.select("a").attr("href");
                    try {
                        productCrawlService.saveProductCrawl(connection, href);
                    } catch (Exception e) {
                        logger.error("load menu products error："+  element.select("a").html());
                    }

                });
            }
        }
    }

    /**
     * 主菜单爬取
     */
    private class MainMenuRunnable implements Runnable {
        private Connection connection;
        private String href;
        public MainMenuRunnable(Connection con2, String href) {
            this.connection = con2;
            this.href = href;
        }

        @Override
        public void run() {
            try {
                productCrawlService.saveProductCrawl(connection, "http://gelin.nz"+href);
            } catch (Exception e) {
                logger.error("load main menu error",e);
            }
        }
    }

    private class MenuProductsRunnable implements Runnable {
        private Connection connection;
        private Elements productsDiv;
        public MenuProductsRunnable(Connection con2,Elements productsDiv) {
            this.connection = con2;
            this.productsDiv = productsDiv;
        }

        @Override
        public void run() {
            try {
                productCrawlService.saveProductCrawl(connection, productsDiv.iterator(), "");
            } catch (Exception e) {
                logger.error("load menu products error",e);
            }
        }
    }

    private class MenuTopLinkRunnable implements Runnable {
        private Connection connection;
        private Element toplink;
        public MenuTopLinkRunnable(Connection con2,Element toplink) {
            this.connection = con2;
            this.toplink = toplink;
        }

        @Override
        public void run() {
            String href = toplink.select("a").attr("href");
            try {
                productCrawlService.saveProductCrawl(connection, "http://gelin.nz"+href.substring(href.indexOf("\\?")+1));
            } catch (Exception e) {
                logger.error("load menu top link error");
            }
        }
    }
}
