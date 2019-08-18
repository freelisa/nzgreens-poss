package com.nzgreens.console.task;

import com.nzgreens.console.service.IProductCrawlService;
import com.nzgreens.dal.user.example.*;
import com.nzgreens.dal.user.mapper.*;
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
import java.util.*;

/**
 * @author helizheng
 * @create 2017-09-19 16:36
 **/
@Component
@Lazy(false)
public class CrawlProductTask extends AbstractScheduleTask {
    private static final Logger logger = LoggerFactory.getLogger(CrawlProductTask.class);
    @Resource
    private ProductBrandMapper productBrandMapper;
    @Resource
    private ProductCategoryMapper productCategoryMapper;
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


    @Scheduled(cron = "${CrawlProductTask.cron:0 0 23 * * ?}")
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

                                ProductCategory categoryModel = productCategoryMapper.selectByPrimaryKey(Long.valueOf(categoryId));
                                if (categoryModel == null) {
                                    ProductCategory category = new ProductCategory();
                                    category.setId(Long.valueOf(categoryId));
                                    category.setName(theparent.html());
                                    category.setCreateTime(new Date());
                                    productCategoryMapper.insertSelective(category);
                                }else{
                                    if(!StringUtils.equals(theparent.html(),categoryModel.getName())){
                                        ProductCategory category = new ProductCategory();
                                        category.setId(categoryModel.getId());
                                        category.setName(theparent.html());
                                        category.setUpdateTime(new Date());
                                        productCategoryMapper.updateByPrimaryKeySelective(category);
                                    }
                                }
                                logger.info("---大分类：{}", theparent.html() + "," + hrefs[1]);


                                Elements childLevel = categoryDivChild.select("ul.child-level li a");
                                Iterator<Element> childLevelIter = childLevel.iterator();
                                while (childLevelIter.hasNext()) {
                                    Element child = childLevelIter.next();
                                    String[] childHrefs = child.attr("href").split("\\?");

                                    //保存小分类
                                    String categoryChildPath = child.attr("href");
                                    String categoryChildId = categoryChildPath.substring(categoryChildPath.lastIndexOf("_") + 1);

                                    ProductCategory categoryChildModel = productCategoryMapper.selectByPrimaryKey(Long.valueOf(categoryChildId));
                                    if (categoryChildModel == null) {
                                        ProductCategory categoryChild = new ProductCategory();
                                        categoryChild.setId(Long.valueOf(categoryChildId));
                                        categoryChild.setParentId(Long.valueOf(categoryId));
                                        categoryChild.setName(child.html());
                                        categoryChild.setCreateTime(new Date());
                                        productCategoryMapper.insertSelective(categoryChild);

                                        logger.info("--------小分类：{}", child.html() + "," + childHrefs[1]);
                                    }else{
                                        if(!StringUtils.equals(child.html(),categoryChildModel.getName())){
                                            ProductCategory categoryChild = new ProductCategory();
                                            categoryChild.setId(categoryChildModel.getId());
                                            categoryChild.setName(child.html());
                                            categoryChild.setUpdateTime(new Date());
                                            productCategoryMapper.updateByPrimaryKeySelective(categoryChild);
                                        }
                                    }
                                    //productCrawlService.saveProductCrawl(con2,categoryChildPath,child.html(),theparent.html(),categoryId,1);
                                    productCrawlService.saveProductCrawl(con2, categoryChildPath);
                                }
                            }
                        } else if (StringUtils.equals(allCategory, "品牌专区")) {
                            Iterator<Element> categoryIter = categoryDiv.iterator();
                            while (categoryIter.hasNext()) {
                                Element categoryDivChild = categoryIter.next();
                                Elements theparent = categoryDivChild.select("a.theparent");//分类名称
                                String[] hrefs = theparent.attr("href").split("\\?");

                                //保存大分类
                                String brandPath = theparent.attr("href");
                                String brandId = brandPath.substring(brandPath.lastIndexOf("=") + 1);

                                ProductBrand productBrand = productBrandMapper.selectByPrimaryKey(Long.valueOf(brandId));
                                if (productBrand == null) {
                                    ProductBrand brand = new ProductBrand();
                                    brand.setId(Long.valueOf(brandId));
                                    brand.setName(theparent.html());
                                    brand.setCreateTime(new Date());
                                    productBrandMapper.insertSelective(brand);
                                    logger.info("---大分类：{}",theparent.html() + "," + hrefs[1]);
                                }else{
                                    if(!StringUtils.equals(theparent.html(),productBrand.getName())){
                                        ProductBrand brand = new ProductBrand();
                                        brand.setId(Long.valueOf(brandId));
                                        brand.setName(theparent.html());
                                        brand.setUpdateTime(new Date());
                                        productBrandMapper.updateByPrimaryKeySelective(brand);
                                    }
                                }
                                productCrawlService.saveProductCrawl(con2,brandPath);
                            }
                        }
                    }
                } catch (Exception e) {
                    logger.error("CrawlProductTask error data：{}", e);
                }
            }
        });
    }
}
