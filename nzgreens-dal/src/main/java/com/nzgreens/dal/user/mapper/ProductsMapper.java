package com.nzgreens.dal.user.mapper;

import com.nzgreens.dal.user.example.Products;
import com.nzgreens.dal.user.example.ProductsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ProductsMapper {
    int countByExample(ProductsExample example);

    int deleteByExample(ProductsExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Products record);

    int insertSelective(Products record);

    List<Products> selectByExampleWithBLOBs(ProductsExample example);

    List<Products> selectByExample(ProductsExample example);

    Products selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Products record, @Param("example") ProductsExample example);

    int updateByExampleWithBLOBs(@Param("record") Products record, @Param("example") ProductsExample example);

    int updateByExample(@Param("record") Products record, @Param("example") ProductsExample example);

    int updateByPrimaryKeySelective(Products record);

    int updateByPrimaryKeyWithBLOBs(Products record);

    int updateByPrimaryKey(Products record);
}