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
@Component
@Lazy(false)
public class CrawlMainCategoryProductTask extends AbstractScheduleTask {
    private static final Logger logger = LoggerFactory.getLogger(CrawlMainCategoryProductTask.class);
    @Resource
    private ProductBrandMapper productBrandMapper;
    @Resource
    private ProductCategoryMapper productCategoryMapper;
    @Resource
    private ProductsCrawlMapper productsCrawlMapper;
    @Resource
    private ProductsMapper productsMapper;
    @Resource
    private ProductsPriceChangeMapper productsPriceChangeMapper;
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

                                logger.info("---大分类：{}", theparent.html() + "," + hrefs[1]);

                                saveProductCrawl(con2,categoryPath,"",theparent.html(),categoryId,1);
                            }
                        }
                    }
                } catch (Exception e) {
                    logger.error("CrawlMainCategoryProductTask error data：{}", e);
                }
            }
        });
    }

    private void saveProductCrawl(Connection con2,String href,String cateHtml,String cateName,String categoryId,int type) throws Exception{
        //获取小分类
        Document temp = null;
        try {
            temp = con2.url(href).get();
        } catch (IOException e1) {}
        if(temp == null){
            return;
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
                productList = con2.url(href + "&page=" + page).get();
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

                //品牌
                Element brand = productDesc.select("ul.product-detail").first();
                //获取库存
                String[] bradHtml = new String[0];
                try {
                    bradHtml = brand.html().replaceAll("<!--|-->","").split("\n");
                } catch (Exception e1) {

                }
                //TODO 新增库存
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
                Elements brandA = brand.select("a");
                String brandLink = brandA.attr("href");
                String brandHtml = brandA.html();
                //积分
                String point = brand.select("span.get-reward-live").html();

                //价格
                Element price = productDesc.select("ul.pro-d").first();
                Elements priceEle = price.select("li");
                //重量
                String weight = priceEle.first().html();
                //销售价
                String priceNew = priceEle.select(".price-new-live").html();
                //市场价
                String priceOld = priceEle.select(".price-old-live").html();


                //添加抓取的商品信息
                Elements detailUl = productContent.select("ul.thumbnails");
                //商品图片属性
                Elements image = detailUl.select("img");
                //商品图片路径
                String imageSrc = image.attr("src");
                //商品图片名称
                String imageName = "";
                String downloadImgMain = null;
                if (StringUtils.isNotBlank(imageSrc)) {
                    //image/product/大分类ID/小分类ID/产品ID/图片名称
                    String suff = imageSrc.substring(imageSrc.lastIndexOf("."));
                    imageName = map.get("product_id") + "_" + System.currentTimeMillis() + suff;
                    downloadImgMain = download(imageSrc, productIconPath, imageName);
                }
                //type=1全部分类，2=品牌
                String categoryParentId = "";
                String categoryName = cateName;
                if(type == 1){
                    categoryParentId = map.get("path");
                    int index = categoryParentId.indexOf("_");
                    //如果存在下划线，则是有小分类，不存在就是大分类的产品
                    if (index > 0) {
                        categoryParentId = categoryParentId.substring(index + 1);
                        categoryName = cateHtml;
                    }
                }

                //查询产品是否存在，存在修改，不存在新增
                ProductsCrawlExample crawlExample = new ProductsCrawlExample();
                crawlExample.createCriteria().andReptileProductIdEqualTo(map.get("product_id"));
                List<ProductsCrawl> productsCrawls = productsCrawlMapper.selectByExample(crawlExample);
                if(CollectionUtils.isEmpty(productsCrawls)){
                    ProductsCrawl crawl = new ProductsCrawl();
                    crawl.setReptileProductId(map.get("product_id"));
                    crawl.setCategoryId(categoryParentId);
                    crawl.setCategoryName(categoryName);
                    crawl.setBrandId(brandLink.substring(brandLink.lastIndexOf("=") + 1));
                    crawl.setBrandName(brandHtml);
                    if(StringUtils.isNotBlank(priceOld)){
                        crawl.setCostPrice(CurrencyUtil.convertYuanToFensTask(priceOld));
                    }else{
                        crawl.setCostPrice(0L);
                    }
                    if(StringUtils.isNotBlank(priceNew)){
                        crawl.setSellingPrice(CurrencyUtil.convertYuanToFensTask(priceNew));
                    }else{
                        crawl.setSellingPrice(0L);
                    }

                    crawl.setTitle(productTitle);
                    //重量处理 重 量： 0.00 克
                    if (StringUtils.isNotBlank(weight)) {
                        weight = weight.substring(5, weight.length() - 2);
                    }
                    crawl.setWeight(weight);
                    crawl.setParentCategoryId(categoryId);
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
                            String downloadImg = download(src, detailImagePath, map.get("product_id") +  "_" + detailIndex + suff);
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
                    if(stock != null && stock != 0){
                        crawl.setStock(stock);
                    }else{
                        crawl.setStock(999);
                    }
                    productsCrawlMapper.insertSelective(crawl);
                }else{
                    //修改产品
                    ProductsCrawl productsCrawl = productsCrawls.get(0);
                    //标题、价格变动了
                    Long costPrice = StringUtils.isNotBlank(priceOld) ? CurrencyUtil.convertYuanToFensTask(priceOld) : 0;
                    Long sellPrice = StringUtils.isNotBlank(priceNew) ? CurrencyUtil.convertYuanToFensTask(priceNew) : 0;
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
                        productsCrawlMapper.updateByPrimaryKeySelective(crawl);

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
                                productsPriceChangeMapper.insertSelective(change);
                            }
                            products.setUpdateTime(new Date());
                            productsMapper.updateByPrimaryKeySelective(products);
                        }
                    }
                }


                logger.info("-----------------------链接：{}", linkHref);
                logger.info("-----------------------标题：{}", productTitle);
                logger.info("-----------------------品牌：{}", brandLink);
                logger.info("-----------------------品牌：{}", brandHtml);
                logger.info("-----------------------积分：{}", point);
                logger.info("-----------------------图片：{}", imageSrc);
                logger.info("-----------------------重量：{}", weight);
                logger.info("-----------------------市场价：{}", priceOld);
            }
        }
    }

    public static void main(String[] args) throws Exception{
        CrawlMainCategoryProductTask task = new CrawlMainCategoryProductTask();
        String url = "http://gelin.nz/image/catalog/产品/Good%20Health/%E5%A5%BD%E5%81%A5%E5%BA%B7%20%E6%B7%B1%E6%B5%B7%E9%B1%BC%E6%B2%B9%E5%81%A5%E5%BA%B7%E5%8D%AB%E5%A3%AB%201000mg%20400%E7%B2%92/1.jpg";
        task.download(url,"test","123.jpg");
        System.out.println(URLEncoder.encode(url, "utf-8"));
        System.out.println(URLDecoder.decode(url, "utf-8"));
    }

    public String download(String urlString, String savePath, String imageName) throws Exception {
        if(!isContainChinese(urlString)){
            urlString = URLEncoder.encode(urlString, "utf-8");
            urlString = URLDecoder.decode(urlString, "utf-8");
        }else{
            urlString = URLEncoder.encode(urlString, "utf-8");
            urlString = urlString.replace("%3A",":");
            urlString = urlString.replace("%2F","/");
            urlString = urlString.replace("+", "%20");
            urlString = urlString.replace("%25", "%");
        }

        int index = urlString.indexOf("image");
        if (index == -1) {
            return null;
        }
        File targetFile = new File(uploadPath + "/" + savePath);
        try {
            // 创建目标文件
            if (!targetFile.getParentFile().exists()) {
                targetFile.getParentFile().mkdirs();
            }

            // 构造URL
            URL url = new URL(urlString.replace(" ", "%20"));
            // 打开连接
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("User-Agent","Mozilla/5.0 ( compatible ) ");

            con.setRequestProperty("Accept","*/*");
            con.setRequestProperty("Accept-Language", "zh-CN");
            con.setRequestProperty("Charset", "UTF-8");
            //设置请求超时为5s
            con.setConnectTimeout(10 * 1000);
            // 输入流
            InputStream is = con.getInputStream();

            // 1K的数据缓冲
            byte[] bs = new byte[1024];
            // 读取到的数据长度
            int len;
            // 输出的文件流
            OutputStream os = new FileOutputStream(uploadPath + "/" + savePath + "/" + imageName);
            // 开始读取
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            // 完毕，关闭所有链接
            os.close();
            is.close();

            return imageName;
        } catch (Exception e) {
            logger.error("download error：{}" ,e.getMessage());
        }
        return null;
    }

    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }
}