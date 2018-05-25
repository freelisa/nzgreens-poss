package com.nzgreens.dal.user.mapper;

import com.nzgreens.dal.user.example.ProductsCrawl;
import com.nzgreens.dal.user.example.ProductsCrawlExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ProductsCrawlMapper {
    int countByExample(ProductsCrawlExample example);

    int deleteByExample(ProductsCrawlExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ProductsCrawl record);

    int insertSelective(ProductsCrawl record);

    List<ProductsCrawl> selectByExample(ProductsCrawlExample example);

    ProductsCrawl selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ProductsCrawl record, @Param("example") ProductsCrawlExample example);

    int updateByExample(@Param("record") ProductsCrawl record, @Param("example") ProductsCrawlExample example);

    int updateByPrimaryKeySelective(ProductsCrawl record);

    int updateByPrimaryKey(ProductsCrawl record);
}