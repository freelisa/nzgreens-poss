package com.nzgreens.dal.user.mapper;

import com.nzgreens.dal.user.example.ProductFreight;
import com.nzgreens.dal.user.example.ProductFreightExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ProductFreightMapper {
    int countByExample(ProductFreightExample example);

    int deleteByExample(ProductFreightExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ProductFreight record);

    int insertSelective(ProductFreight record);

    List<ProductFreight> selectByExample(ProductFreightExample example);

    ProductFreight selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ProductFreight record, @Param("example") ProductFreightExample example);

    int updateByExample(@Param("record") ProductFreight record, @Param("example") ProductFreightExample example);

    int updateByPrimaryKeySelective(ProductFreight record);

    int updateByPrimaryKey(ProductFreight record);
}