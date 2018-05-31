package com.nzgreens.dal.user.mapper;

import com.nzgreens.dal.user.example.ProductsPriceChange;
import com.nzgreens.dal.user.example.ProductsPriceChangeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ProductsPriceChangeMapper {
    int countByExample(ProductsPriceChangeExample example);

    int deleteByExample(ProductsPriceChangeExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ProductsPriceChange record);

    int insertSelective(ProductsPriceChange record);

    List<ProductsPriceChange> selectByExample(ProductsPriceChangeExample example);

    ProductsPriceChange selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ProductsPriceChange record, @Param("example") ProductsPriceChangeExample example);

    int updateByExample(@Param("record") ProductsPriceChange record, @Param("example") ProductsPriceChangeExample example);

    int updateByPrimaryKeySelective(ProductsPriceChange record);

    int updateByPrimaryKey(ProductsPriceChange record);
}