package com.nzgreens.console.service.impl;

import com.github.pagehelper.PageHelper;
import com.nzgreens.common.enums.IsValidEnum;
import com.nzgreens.common.enums.ProductsPriceChangeStatusEnum;
import com.nzgreens.common.exception.ErrorCodes;
import com.nzgreens.common.form.console.PageSearchForm;
import com.nzgreens.common.form.console.ProductAddForm;
import com.nzgreens.common.form.console.ProductForm;
import com.nzgreens.common.model.console.ProductsModel;
import com.nzgreens.common.model.console.ProductsPriceChangeModel;
import com.nzgreens.common.utils.BeanMapUtil;
import com.nzgreens.common.utils.CurrencyUtil;
import com.nzgreens.console.service.BaseService;
import com.nzgreens.console.service.IProductService;
import com.nzgreens.console.util.CosUtils;
import com.nzgreens.console.web.common.UploadTempImageUtil;
import com.nzgreens.dal.user.example.*;
import com.nzgreens.dal.user.mapper.*;
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
import java.util.Date;
import java.util.List;

/**
 * @Author:helizheng
 * @Date: Created in 2018/4/21 15:23
 */
@Service
public class ProductService extends BaseService implements IProductService {
    @Resource
    private ProductsMapper productsMapper;
    @Resource
    private SubProductsMapper subProductsMapper;
    @Resource
    private ProductCategoryMapper productCategoryMapper;
    @Resource
    private ProductBrandMapper productBrandMapper;
    @Resource
    private ProductsPriceChangeMapper productsPriceChangeMapper;
    @Value("${images.host}")
    private String imageHost;
    @Value("${images.product.detail.path}")
    private String imageProductDetailPath;
    @Value("${images.product.icon.path}")
    private String imageProductIconPath;
    @Value("${images.upload.path}")
    private String imageRoot;
    @Resource
    private CosUtils cosUtils;

    @Override
    public List<Products> selectProductForPage(ProductForm form) throws Exception {
        //查询是否是父分类
        if(form.getCategoryId() != null){
            ProductCategoryExample example = new ProductCategoryExample();
            example.createCriteria().andParentIdEqualTo(form.getCategoryId());
            if(productCategoryMapper.countByExample(example) > 0){
                form.setParentCategoryId(form.getCategoryId());
                form.setCategoryId(null);
            }
        }
        PageHelper.startPage(form.getPageNum(), form.getPageSize());
        List<Products> products = subProductsMapper.selectProductForPage(form);
        for(int i = 0,len = products.size();i < len;i ++){
            Products pro = products.get(i);
            pro.setImage(imageHost + imageProductIconPath + "/" + pro.getImage());
            if(pro.getCrawlSellingPrice() == null){
                pro.setCrawlSellingPrice(0L);
            }
        }
        return products;
    }

    @Override
    public List<ProductsModel> selectProductExport(ProductForm form) throws Exception {
        //查询是否是父分类
        if(form.getCategoryId() != null){
            ProductCategoryExample example = new ProductCategoryExample();
            example.createCriteria().andParentIdEqualTo(form.getCategoryId());
            if(productCategoryMapper.countByExample(example) > 0){
                form.setParentCategoryId(form.getCategoryId());
                form.setCategoryId(null);
            }
        }
        List<Products> products = subProductsMapper.selectProductForPage(form);

        return BeanMapUtil.collectionToList(products,ProductsModel.class);
    }

    @Override
    public void updateProductPriceBatch(List<ProductsModel> list) throws Exception {
        subProductsMapper.updateProductPriceBatch(list);
    }

    @Override
    public List<ProductsPriceChangeModel> selectProductChangeForPage(PageSearchForm form) throws Exception {
        PageHelper.startPage(form.getPageNum(), form.getPageSize());
        List<ProductsPriceChangeModel> changeModels = subProductsMapper.selectProductChangeForPage(form);
        for(int i = 0,len = changeModels.size();i < len;i ++){
            ProductsPriceChangeModel pro = changeModels.get(i);
            pro.setImage(imageHost + imageProductIconPath + "/" + pro.getImage());
        }
        return changeModels;
    }

    @Override
    public void updateProductChangeForPage(Long id) throws Exception {
        ProductsPriceChange change = new ProductsPriceChange();
        change.setStatus(ProductsPriceChangeStatusEnum.READED.getValue());
        change.setUpdateTime(new Date());

        ProductsPriceChangeExample changeExample = new ProductsPriceChangeExample();
        changeExample.createCriteria().andIdEqualTo(id).andStatusEqualTo(ProductsPriceChangeStatusEnum.NOT_READ.getValue());
        if(productsPriceChangeMapper.updateByExampleSelective(change,changeExample) < 1){
            thrown(ErrorCodes.UPDATE_ERROR);
        }
    }

    @Override
    public int selectProductChangeCount() throws Exception {
        return subProductsMapper.selectProductChangeCount();
    }

    @Override
    public ProductAddForm selectProductDetail(Long id) throws Exception {
        Products products = productsMapper.selectByPrimaryKey(id);
        ProductAddForm form = new ProductAddForm();
        BeanUtils.copyProperties(products,form);
        form.setCostPrice(String.valueOf(products.getCostPrice()));
        form.setSellingPrice(String.valueOf(products.getSellingPrice()));
        form.setImage(imageHost + imageProductIconPath + "/" + form.getImage());
        return form;
    }

    @Override
    public List<ProductCategory> selectProductCategory() throws Exception {
        return productCategoryMapper.selectByExample(null);
    }

    @Override
    public List<ProductBrand> selectProductBrand() throws Exception {
        return productBrandMapper.selectByExample(null);
    }

    @Override
    public void insert(ProductAddForm form) throws Exception {
        if (form == null) {
            thrown(ErrorCodes.DATA_ILLEGAL);
        }
        if (StringUtils.isEmpty(form.getTitle())) {
            thrown(ErrorCodes.TITLE_ILLEGAL);
        }
        if (form.getWeight() == null) {
            thrown(ErrorCodes.WEIGHT_ILLEGAL);
        }
        if (form.getCostPrice() == null) {
            thrown(ErrorCodes.COST_PRICE_ILLEGAL);
        }
        if (form.getSellingPrice() == null) {
            thrown(ErrorCodes.SELL_PRICE_ILLEGAL);
        }
        if (form.getStock() == null) {
            thrown(ErrorCodes.STOCK_ILLEGAL);
        }
        if (StringUtils.isBlank(form.getImage())) {
            thrown(ErrorCodes.PRODUCT_IMAGE_ILLEGAL);
        }
        if (form.getIsValid() == null) {
            thrown(ErrorCodes.PRODUCTS_IS_VALID_ILLEGAL);
        }
        form.setDetail(html_Base64ToImg(form.getDetail()));
        form.setImage(form.getImage().substring(form.getImage().lastIndexOf("/") + 1));
        Products products = new Products();
        BeanUtils.copyProperties(form,products);
        products.setCostPrice(CurrencyUtil.convertYuanToFen(form.getCostPrice()));
        products.setSellingPrice(CurrencyUtil.convertYuanToFen(form.getSellingPrice()));
        if (productsMapper.insertSelective(products) < 1) {
            thrown("update.error");
        }
    }

    @Override
    public void update(ProductAddForm form) throws Exception {
        if (form == null) {
            thrown(ErrorCodes.DATA_ILLEGAL);
        }
        if (form.getId() == null) {
            thrown(ErrorCodes.ID_ILLEGAL);
        }
        if (StringUtils.isEmpty(form.getTitle())) {
            thrown(ErrorCodes.TITLE_ILLEGAL);
        }
        if (form.getWeight() == null) {
            thrown(ErrorCodes.WEIGHT_ILLEGAL);
        }
        if (form.getCostPrice() == null) {
            thrown(ErrorCodes.COST_PRICE_ILLEGAL);
        }
        if (form.getSellingPrice() == null) {
            thrown(ErrorCodes.SELL_PRICE_ILLEGAL);
        }
        if (form.getStock() == null) {
            thrown(ErrorCodes.STOCK_ILLEGAL);
        }
        if (form.getIsValid() == null) {
            thrown(ErrorCodes.PRODUCTS_IS_VALID_ILLEGAL);
        }
        if(StringUtils.isNotEmpty(form.getDetail())){
            form.setDetail(html_Base64ToImg(form.getDetail()));
        }
        if(StringUtils.isNotBlank(form.getImage())){
            form.setImage(form.getImage().substring(form.getImage().lastIndexOf("/") + 1));
        }
        Products products = new Products();
        BeanUtils.copyProperties(form,products);
        products.setCostPrice(CurrencyUtil.convertYuanToFen(form.getCostPrice()));
        products.setSellingPrice(CurrencyUtil.convertYuanToFen(form.getSellingPrice()));
        if (productsMapper.updateByPrimaryKeySelective(products) < 1) {
            thrown("update.error");
        }
    }

    @Override
    public void delete(Long id) throws Exception {
        if (id == null) {
            thrown(ErrorCodes.ID_ILLEGAL);
        }
        Products products = new Products();
        products.setId(id);
        products.setIsValid(IsValidEnum.INVALID.getValue());
        if (productsMapper.updateByPrimaryKeySelective(products) < 1) {
            thrown("update.error");
        }
    }

    /**
     * 处理把base64转化成img
     *
     * @param html
     * @return
     * @throws Exception
     */
    private String html_Base64ToImg(String html) throws Exception {
        Document doc = Jsoup.parse(html, "utf-8");
        Elements imgs = doc.getElementsByTag("img");
        for (Element img : imgs) {
            String src = img.attr("src");
            if (src.startsWith("data:image")) {
                img.attr("src", imageHost + imageProductDetailPath + "/" + UploadTempImageUtil.processImage(src, imageProductDetailPath));
            }
        }
        return doc.getElementsByTag("body").html();
    }

    @Override
    public String uploadImg(MultipartFile multiFile) throws Exception {
        if (multiFile != null) {
            String imagePath = imageProductIconPath;
            File file = UploadTempImageUtil.processImage(multiFile, imagePath);
            // 返回路径
            imagePath = imagePath + "/" + file.getName();
            cosUtils.upload(file.getAbsolutePath(),"/data/upload/"+imagePath);
            return imageHost + imagePath;
        }
        return "";
    }
}
