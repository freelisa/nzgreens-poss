package com.nzgreens.dal.user.mapper;

import com.nzgreens.dal.user.example.SearchKeyword;
import com.nzgreens.dal.user.example.SearchKeywordExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SearchKeywordMapper {
    int countByExample(SearchKeywordExample example);

    int deleteByExample(SearchKeywordExample example);

    int deleteByPrimaryKey(Long id);

    int insert(SearchKeyword record);

    int insertSelective(SearchKeyword record);

    List<SearchKeyword> selectByExample(SearchKeywordExample example);

    SearchKeyword selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") SearchKeyword record, @Param("example") SearchKeywordExample example);

    int updateByExample(@Param("record") SearchKeyword record, @Param("example") SearchKeywordExample example);

    int updateByPrimaryKeySelective(SearchKeyword record);

    int updateByPrimaryKey(SearchKeyword record);
}