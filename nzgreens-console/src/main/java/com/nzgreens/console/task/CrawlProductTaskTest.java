package com.nzgreens.console.task;

import com.alibaba.fastjson.JSON;
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

            }
        });
    }

    public static void main(String[] args) {
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
            //商品链接
            String linkHref = "http://gelin.nz/index.php?route=product/product&product_id=8035";

            Map<String, String> map = ConvertUrlToMapUtil.URLRequest(linkHref);

            //进入商品详情
            Document productDetail = null;
            try {
                productDetail = con2.url(linkHref).userAgent("Mozilla").get();
            } catch (IOException e1) {
                logger.error("load product detail error："+linkHref,e1);
            }
            if(productDetail == null){
                return;
            }
            Element productContent = productDetail.select("#content").first();

            Element productDesc = productContent.select(".col-sm-8").first();
            //商品title
            String productTitle = productDesc.select("h4 b").first().html();

            //品牌
            Elements detailClasify = productDesc.select("ul.product-detail li");
            Iterator<Element> iterator = detailClasify.iterator();
            String brandHtml = null;
            String brandId = null;
            //TODO 新增库存
            Integer stock = 0;
            while (iterator.hasNext()) {
                Element element = iterator.next();
                String content = element.html();
                if (StringUtils.isNoneBlank(content) && content.contains("品  牌")) {
                    String brandHref = element.select("a").first().attr("herf");
                    brandId = brandHref.substring(brandHref.lastIndexOf("=")+1);
                    brandHtml = element.select("a").first().html();
                } else if (StringUtils.isNoneBlank(content) && content.contains("库 存")) {
                    Pattern p = Pattern.compile("[^0-9]");
                    try {
                        Matcher m = p.matcher(content);
                        stock = Integer.valueOf(m.replaceAll("").trim());
                    } catch (NumberFormatException e1) {
                        stock = 80;
                    }
                }
            }

            //价格
            Element price = productDesc.select("ul.pro-d").first();
            Elements priceEle = price.select("li");
            //重量
            String weight = priceEle.first().html();
            //销售价
            String priceNew = priceEle.select(".price-new-live").html();
            //市场价
            String priceOld = priceEle.select(".price-old-live").html();
            Pattern p = Pattern.compile("([1-9]\\d*\\.?\\d*)|(0\\.\\d*[1-9])");        //得到字符串中的数字
            Matcher m = p.matcher(priceOld);
            Matcher matcher = p.matcher(priceNew);
            if (m.find()){
                System.out.println(m.group());
                priceOld = m.group();
            }
            if (matcher.find()){
                System.out.println(matcher.group());
                priceNew = matcher.group();
            }
            //添加抓取的商品信息
            Elements detailUl = productContent.select("ul.thumbnails");
            //商品图片属性
            Elements image = detailUl.select("a.thumbnail");
            //商品图片路径
            String imageSrc = image.attr("href");
            //商品图片名称
            String imageName = "";
            String downloadImgMain = null;
            if (StringUtils.isNotBlank(imageSrc)) {
                //image/product/大分类ID/小分类ID/产品ID/图片名称
                String suff = imageSrc.substring(imageSrc.lastIndexOf("."));
                imageName = map.get("product_id") + "_0_1" + suff;
                try {
                    // downloadImgMain = download(imageSrc, imageProductIconPath, imageName);
                    System.out.println("download "+imageSrc);
                } catch (Exception e) {
                    logger.error(e.getMessage(),e);
                }
            }
            String categoryParentId = "";
            String categoryId = "";
            String categoryName = "";
            Elements categroys = productDetail.select("ul.breadcrumb li");
            Iterator<Element> cateIterator = categroys.iterator();
            while (cateIterator.hasNext()) {
                Element element = cateIterator.next();
                String href = element.select("a").attr("href");
                if (href.contains("manufacturer_id")) {
                    String brand = href.substring(href.lastIndexOf("manufacturer_id=")+1);
                    if (StringUtils.isNumeric(brand)) {
                        logger.info("当前商品按照--品牌--分类");
                    }
                }
                if (href.contains("path")) {
                    String cate = href.substring(href.lastIndexOf("path=")+1);
                    if (StringUtils.isNumeric(cate)) {
                        logger.info("当前商品按照--大分类--分类");
                        categoryParentId = cate;
                    } else {
                        logger.info("当前商品按照--小分类--分类");
                        cate = href.substring(href.lastIndexOf("path="+cate+"_")+1);
                        if (StringUtils.isNumeric(cate)) {
                            categoryId = cate;
                        }
                        categoryName = element.select("a").html();
                    }
                }

            }

            //查询产品是否存在，存在修改，不存在新增
            ProductsCrawlExample crawlExample = new ProductsCrawlExample();
            crawlExample.createCriteria().andReptileProductIdEqualTo(map.get("product_id"));
            List<ProductsCrawl> productsCrawls =null;
            if(CollectionUtils.isEmpty(productsCrawls)){
                ProductsCrawl crawl = new ProductsCrawl();
                crawl.setReptileProductId(map.get("product_id"));
                crawl.setParentCategoryId(categoryParentId);
                crawl.setCategoryId(categoryId);
                crawl.setCategoryName(categoryName);
                crawl.setBrandId(brandId);
                crawl.setBrandName(brandHtml);
                if(StringUtils.isNotBlank(priceOld)){
                    try {
                        crawl.setCostPrice(CurrencyUtil.convertYuanToFensTask(priceOld));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    crawl.setCostPrice(0L);
                }
                if(StringUtils.isNotBlank(priceNew)){
                    try {
                        crawl.setSellingPrice(CurrencyUtil.convertYuanToFensTask(priceNew));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    crawl.setSellingPrice(0L);
                }

                crawl.setTitle(productTitle);
                //重量处理 重 量： 0.00 克
                if (StringUtils.isNotBlank(weight)) {
                    weight = weight.substring(5, weight.length() - 2);
                }
                crawl.setWeight(weight);
                if (crawl.getCostPrice() == 0
                        || crawl.getSellingPrice() == 0) {
                           /* if (retry) {
                                loadProductByLink(con2,"http://gelin.nz/index.php?route=product/product&"+map.get("product_id"), false);
                                logger.info("当前商品售价为0，重试！-》》"+crawl.getReptileProductId());
                                return;
                            }*/
                }
                StringBuilder buff = new StringBuilder();
                //产品详细图片描述
                Elements detailImgs = productContent.select("div.item-content .tab-content img");
                Iterator<Element> detailImgIter = detailImgs.iterator();
                int detailIndex = 1;
                while (detailImgIter.hasNext()) {
                    Element detailImg = detailImgIter.next();
                    String src = detailImg.attr("src");
                    if (StringUtils.isNotBlank(src)) {
                        String suff = src.substring(src.lastIndexOf("."));
                        String downloadImg = null;
                        try {
                            //downloadImg = download(src, detailImagePath, map.get("product_id") +  "_1_" + detailIndex + suff);
                            System.out.println("download "+src);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if(StringUtils.isNotEmpty(downloadImg)){
                            buff.append(downloadImg);
                            if (detailImgIter.hasNext()) {
                                buff.append(",");
                            }
                        }
                        detailIndex++;
                    }
                }
                crawl.setDetail(buff.toString());
                if(StringUtils.isNotEmpty(downloadImgMain)){
                    crawl.setImage(downloadImgMain);
                }
                crawl.setStock(80);
                logger.info("insert=="+ JSON.toJSONString(crawl));
            } else {
                //修改产品
                ProductsCrawl productsCrawl = productsCrawls.get(0);
                //标题、价格变动了
                Long costPrice = null;
                Long sellPrice = null;
                try {
                    costPrice = StringUtils.isNotBlank(priceOld) ? CurrencyUtil.convertYuanToFensTask(priceOld) : 0;
                    sellPrice = StringUtils.isNotBlank(priceNew) ? CurrencyUtil.convertYuanToFensTask(priceNew) : 0;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(!StringUtils.equals(productTitle,productsCrawl.getTitle())
                        || !StringUtils.equals(String.valueOf(costPrice),String.valueOf(productsCrawl.getCostPrice()))
                        || !StringUtils.equals(String.valueOf(sellPrice),String.valueOf(productsCrawl.getSellingPrice()))){

                    ProductsCrawl crawl = new ProductsCrawl();
                    crawl.setId(productsCrawl.getId());
                    if(StringUtils.isNotBlank(productTitle)){
                        crawl.setTitle(productTitle);
                    }
                    if(costPrice != null && costPrice != 0){
                        crawl.setCostPrice(costPrice);
                    }
                    if(sellPrice != null && sellPrice != 0){
                        crawl.setSellingPrice(sellPrice);
                    }
                    if(costPrice == null || costPrice == 0){
                        crawl.setCostPrice(sellPrice);
                        crawl.setSellingPrice(sellPrice);
                    }
                    if(sellPrice == null || sellPrice == 0){
                        crawl.setCostPrice(costPrice);
                        crawl.setSellingPrice(costPrice);
                    }
                    crawl.setUpdateTime(new Date());
                    System.out.println("productsCrawlMapper update ");

                    if(productsCrawl.getProductId() != null){
                        Products products = new Products();
                        products.setId(productsCrawl.getProductId());
                        if(!StringUtils.equals(productTitle,productsCrawl.getTitle()) && StringUtils.isNotBlank(productTitle)){
                            products.setTitle(productTitle);
                        }
                        if(!StringUtils.equals(String.valueOf(sellPrice),String.valueOf(productsCrawl.getSellingPrice())) && sellPrice != null && sellPrice !=0){
                            products.setCrawlSellingPrice(sellPrice);

                            //价格变动，消息加入价格变动表
                            ProductsPriceChange change = new ProductsPriceChange();
                            change.setProductId(productsCrawl.getProductId());
                            change.setOldSellPrice(productsCrawl.getSellingPrice());
                            change.setNewSellPrice(sellPrice);
                            change.setStatus(ProductsPriceChangeStatusEnum.NOT_READ.getValue());
                            change.setCreateTime(new Date());
                            change.setUpdateTime(new Date());
                            System.out.println("price change");
                        }
                        products.setUpdateTime(new Date());
                        logger.info("update=="+JSON.toJSONString(products));
                    }
                }
            }

        } catch (Exception e) {
            logger.error("CrawlProductTask error data：{}", e.getMessage());
        }
    }
}
