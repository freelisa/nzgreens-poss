package com.nzgreens.dal.user.mapper;

import com.nzgreens.common.form.console.PageSearchForm;
import com.nzgreens.common.form.console.ProductForm;
import com.nzgreens.common.model.console.ProductsModel;
import com.nzgreens.common.model.console.ProductsPriceChangeModel;
import com.nzgreens.common.model.console.UserOrderPriceSumModel;
import com.nzgreens.dal.user.example.Products;
import com.nzgreens.dal.user.example.ProductsPriceChange;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SubProductsMapper {
    List<Products> selectProductForPage(ProductForm form) throws Exception;

    int selectProductChangeCount() throws Exception;

    List<ProductsPriceChangeModel> selectProductChangeForPage(PageSearchForm form) throws Exception;

    /**
     * 查询代理月返佣
     * @param startTime
     * @param endTime
     * @return
     * @throws Exception
     */
    List<UserOrderPriceSumModel> selectAgentMonthRebate(@Param(value = "startTime") String startTime,@Param(value = "endTime") String endTime) throws Exception;

    /**
     * 批量修改商品价格
     * @param list
     * @throws Exception
     */
    void updateProductPriceBatch(List<ProductsModel> list) throws Exception;

    /**
     * 删除待审核对应的抓取表数据
     * @throws Exception
     */
    void deleteProductCrawlBatch() throws Exception;
}