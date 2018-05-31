package com.nzgreens.console.service.impl;

import com.github.pagehelper.PageHelper;
import com.nzgreens.common.enums.IsValidEnum;
import com.nzgreens.common.enums.OrderHandleStatusEnum;
import com.nzgreens.common.enums.ProductsAuditStatusEnum;
import com.nzgreens.common.exception.ErrorCodes;
import com.nzgreens.common.form.console.PageSearchForm;
import com.nzgreens.common.form.console.ProductAddForm;
import com.nzgreens.common.form.console.ProductForm;
import com.nzgreens.console.service.BaseService;
import com.nzgreens.console.service.IProductCrawlService;
import com.nzgreens.console.service.IProductService;
import com.nzgreens.console.web.common.UploadTempImageUtil;
import com.nzgreens.dal.user.example.*;
import com.nzgreens.dal.user.mapper.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Author:helizheng
 * @Date: Created in 2018/4/21 15:23
 */
@Service
public class ProductCrawlService extends BaseService implements IProductCrawlService {
    @Resource
    private ProductsCrawlMapper productsCrawlMapper;
    @Resource
    private CoinSettingMapper coinSettingMapper;
    @Resource
    private ProductsMapper productsMapper;
    @Value("${images.host}")
    private String imageHost;
    @Value("${images.product.icon.path}")
    private String imageProductIconPath;
    @Value("${images.product.detail.path}")
    private String detailImagePath;


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
        pro.setStock(9999l);
        pro.setGelinProductId(Long.valueOf(crawl.getReptileProductId()));
        pro.setCreateTime(new Date());
        productsMapper.insertSelective(pro);

        crawl.setProductId(pro.getId());
        crawl.setState(1);
        productsCrawlMapper.updateByPrimaryKeySelective(crawl);
    }
}
