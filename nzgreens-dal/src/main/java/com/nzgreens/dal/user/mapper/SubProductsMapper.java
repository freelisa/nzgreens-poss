package com.nzgreens.dal.user.mapper;

import com.nzgreens.common.form.console.ProductForm;
import com.nzgreens.common.model.console.UserOrderPriceSumModel;
import com.nzgreens.dal.user.example.Products;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SubProductsMapper {
    List<Products> selectProductForPage(ProductForm form) throws Exception;

    /**
     * 查询代理月返佣
     * @param startTime
     * @param endTime
     * @return
     * @throws Exception
     */
    List<UserOrderPriceSumModel> selectAgentMonthRebate(@Param(value = "startTime") String startTime,@Param(value = "endTime") String endTime) throws Exception;
}