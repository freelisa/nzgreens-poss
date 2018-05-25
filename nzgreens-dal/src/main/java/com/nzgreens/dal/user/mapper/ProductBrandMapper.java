package com.nzgreens.dal.user.mapper;

import com.nzgreens.dal.user.example.ProductBrand;
import com.nzgreens.dal.user.example.ProductBrandExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ProductBrandMapper {
    int countByExample(ProductBrandExample example);

    int deleteByExample(ProductBrandExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ProductBrand record);

    int insertSelective(ProductBrand record);

    List<ProductBrand> selectByExample(ProductBrandExample example);

    ProductBrand selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ProductBrand record, @Param("example") ProductBrandExample example);

    int updateByExample(@Param("record") ProductBrand record, @Param("example") ProductBrandExample example);

    int updateByPrimaryKeySelective(ProductBrand record);

    int updateByPrimaryKey(ProductBrand record);
}