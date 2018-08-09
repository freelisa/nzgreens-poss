package com.nzgreens.console.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.nzgreens.common.enums.IsValidEnum;
import com.nzgreens.common.enums.OrderHandleStatusEnum;
import com.nzgreens.common.enums.ProductsAuditStatusEnum;
import com.nzgreens.common.enums.ProductsPriceChangeStatusEnum;
import com.nzgreens.common.exception.ErrorCodes;
import com.nzgreens.common.form.console.PageSearchForm;
import com.nzgreens.common.form.console.ProductAddForm;
import com.nzgreens.common.form.console.ProductForm;
import com.nzgreens.common.utils.CurrencyUtil;
import com.nzgreens.console.service.BaseService;
import com.nzgreens.console.service.IProductCrawlService;
import com.nzgreens.console.service.IProductService;
import com.nzgreens.console.util.ConvertUrlToMapUtil;
import com.nzgreens.console.web.common.UploadTempImageUtil;
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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author:helizheng
 * @Date: Created in 2018/4/21 15:23
 */
@Service
public class ProductCrawlService extends BaseService implements IProductCrawlService {
    private static final Logger logger = LoggerFactory.getLogger(ProductCrawlService.class);
    @Resource
    private ProductsCrawlMapper productsCrawlMapper;
    @Resource
    private CoinSettingMapper coinSettingMapper;
    @Resource
    private ProductsMapper productsMapper;
    @Resource
    private ProductsPriceChangeMapper productsPriceChangeMapper;
    @Value("${images.host}")
    private String imageHost;
    @Value("${images.product.icon.path}")
    private String imageProductIconPath;
    @Value("${images.product.detail.path}")
    private String detailImagePath;
    @Value("${images.upload.path}")
    private String uploadPath="d:/新西兰直邮/";


    @Override
    public List<ProductsCrawl> selectProductForPage(PageSearchForm form) throws Exception {
        PageHelper.startPage(form.getPageNum(), form.getPageSize());
        ProductsCrawlExample example = new ProductsCrawlExample();
        example.createCriteria().andStateEqualTo(OrderHandleStatusEnum.WAIT.getValue())
                .andIsValidEqualTo(IsValidEnum.EFFECTIVE.getValue());
        List<ProductsCrawl> products = productsCrawlMapper.selectByExample(example);
        for(int i = 0,len = products.size();i < len;i ++){
            ProductsCrawl pro = products.get(i);
            pro.setImage(imageHost + imageProductIconPath + "/" + pro.getImage());
        }
        return products;
    }

    @Override
    public void update(Long productId,Integer status) throws Exception {
        if (productId == null) {
            thrown(ErrorCodes.ID_ILLEGAL);
        }
        if (status == null) {
            thrown(ErrorCodes.PRODUCT_STATUS_ILLEGAL);
        }
        ProductsCrawl products = productsCrawlMapper.selectByPrimaryKey(productId);
        if(products == null){
            thrown(ErrorCodes.DATA_ILLEGAL);
        }
        if(products.getState() == OrderHandleStatusEnum.SUCCESS.getValue()){
            thrown(ErrorCodes.PRODUCTS_STATUS_ILLEGAL);
        }
        //修改抓取表
        ProductsCrawl crawl = new ProductsCrawl();
        crawl.setId(productId);
        crawl.setState(OrderHandleStatusEnum.SUCCESS.getValue());
        crawl.setUpdateTime(new Date());
        if (productsCrawlMapper.updateByPrimaryKeySelective(crawl) < 1) {
            thrown("update.error");
        }

        //如果是审核通过，新增数据到products表
        if(status == ProductsAuditStatusEnum.SUCCESS.getValue()){
            insertProducts(products);
        }
    }

    @Override
    public void saveProductCrawl(Connection con2, String href, String cateHtml, String cateName, String categoryId, int type) throws Exception {
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
        logger.info("concurrent menu {},concurrent total page {}",temp.select("#content h2").text(),page);
        for (int i = page; i > 0; i--) {
            loadProductByPage(con2, href, i, cateHtml, cateName, categoryId, type);
        }
        for (int i = 1; i <= page; i++) {
            loadProductByPage(con2, href, i, cateHtml, cateName, categoryId, type);
        }

    }

    @Override
    public void saveProductCrawl(Connection con2, Iterator<Element> productHtml, String menu) throws Exception {
        if (productHtml == null) {
            return;
        }
        while (productHtml.hasNext()) {
            Element next = productHtml.next();
            try {
                loadProduct(next, con2, "", "" ,"",1);
            } catch (Exception e) {
                logger.error("load menu product error ,scan to next. "+ menu, e);
            }
        }
    }

    private void loadProductByPage (Connection con2,String href,int i,String cateHtml,String cateName, String categoryId,int type) {
        Document productList = null;
        try {
            productList = con2.url(href + "&page=" + i).get();
        } catch (IOException e1) {}
        if(productList == null){
            return;
        }
        Elements productEle = productList.select(".product-thumb");
        try {
            logger.info("concurrent page info {}",productList.select(".col-sm-6 .text-right").text());
        } catch (Exception e) {

        }
        Iterator<Element> productIter = productEle.iterator();
        while (productIter.hasNext()) {
            Element product = productIter.next();
            try {
                loadProduct(product, con2, cateHtml, cateName, categoryId, type);
            } catch (Exception e) {
                logger.error("load product error, scan to next. "+JSON.toJSONString(product), e);
            }
        }
    }

    private void loadProduct (Element productIter,Connection con2,String cateHtml,String cateName, String categoryId,int type) {
        Element next = productIter;
        Elements link = next.select(".image a");

        //列表内icon图片
        Elements iconImg = next.select(".image img");
        String iconImgSrc = iconImg.attr("src");
        //商品链接
        String linkHref = link.attr("href");

        Map<String, String> map = ConvertUrlToMapUtil.URLRequest(linkHref);

        //进入商品详情
        Document productDetail = null;
        try {
            productDetail = con2.url(linkHref).get();
        } catch (IOException e1) {}
        if(productDetail == null){
            return;
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
            imageName = map.get("product_id") + "_0_1" + suff;
            try {
                downloadImgMain = download(imageSrc, imageProductIconPath, imageName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //type=1全部分类，2=品牌
        String categoryParentId = "";
        String categoryName = cateName;
        if(type == 1){
            categoryParentId = map.get("path");
            if(StringUtils.isNotBlank(categoryParentId)){
                int index = categoryParentId.indexOf("_");
                //如果存在下划线，则是有小分类，不存在就是大分类的产品
                if (index > 0) {
                    categoryParentId = categoryParentId.substring(index + 1);
                    categoryName = cateHtml;
                }
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
                    String downloadImg = null;
                    try {
                        downloadImg = download(src, detailImagePath, map.get("product_id") +  "_1_" + detailIndex + suff);
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
            productsCrawlMapper.insertSelective(crawl);
        }else{
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


        //logger.info("-----------------------链接：{}", linkHref);
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

    private void insertProducts(ProductsCrawl crawl) throws Exception{
        List<CoinSetting> coinSettings = coinSettingMapper.selectByExample(null);
        Long money = null;
        if(CollectionUtils.isEmpty(coinSettings)){
            money = 100L;
        }
        money = coinSettings.get(0).getMoney();

        Products pro = new Products();
        if(StringUtils.isNotEmpty(crawl.getBrandId())){
            pro.setBrandId(Long.valueOf(crawl.getBrandId()));
        }
        if(StringUtils.isNotEmpty(crawl.getCategoryId())){
            pro.setCategoryId(Long.valueOf(crawl.getCategoryId()));
        }

        BigDecimal cost = new BigDecimal(crawl.getCostPrice());
        BigDecimal coseBig = cost.divide(new BigDecimal(money)).multiply(new BigDecimal(coinSettings.get(0).getCoin()))
                .setScale(0, BigDecimal.ROUND_UP);

        BigDecimal sell = new BigDecimal(crawl.getSellingPrice());
        BigDecimal sellBig = sell.divide(new BigDecimal(money)).multiply(new BigDecimal(coinSettings.get(0).getCoin()))
                .setScale(0, BigDecimal.ROUND_UP);
        pro.setCostPrice(coseBig.longValue());
        pro.setSellingPrice(sellBig.longValue());
        StringBuilder buff = new StringBuilder();
        if(StringUtils.isNotEmpty(crawl.getDetail())){
            String[] imgs = crawl.getDetail().split(",");
            buff.append("<div>");
            for(int j = 0,len = imgs.length;j < len;j++){
                //域名+图片地址+图片名称
                buff.append("<p><img src=\"").append(imageHost + detailImagePath + "/" + imgs[j]).append("\" /></p>");
            }
            buff.append("</div>");
        }
        pro.setDetail(buff.toString());
        pro.setImage(crawl.getImage());
        pro.setTitle(crawl.getTitle());
        pro.setParentCategoryId(Long.valueOf(crawl.getParentCategoryId()));
        if(StringUtils.isNotEmpty(crawl.getWeight())){
            String[] split = crawl.getWeight().split("\\.");
            pro.setWeight(Long.valueOf(split[0]));
        }
        pro.setStock(80l);
        pro.setGelinProductId(Long.valueOf(crawl.getReptileProductId()));
        pro.setCreateTime(new Date());
        productsMapper.insertSelective(pro);

        crawl.setProductId(pro.getId());
        crawl.setState(1);
        productsCrawlMapper.updateByPrimaryKeySelective(crawl);
    }
}
