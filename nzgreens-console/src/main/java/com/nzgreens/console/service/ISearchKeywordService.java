package com.nzgreens.console.service;

import com.nzgreens.common.form.console.PageSearchForm;
import com.nzgreens.dal.user.example.SearchKeyword;

import java.util.List;

/**
 * @Author:helizheng
 * @Date: Created in 2018/4/26 23:44
 */

public interface ISearchKeywordService {
    List<SearchKeyword> selectForPage(PageSearchForm form,String keyWord) throws Exception;

    SearchKeyword selectDetail(Long id) throws Exception;

    void insert(SearchKeyword searchKeyword) throws Exception;

    void update(SearchKeyword searchKeyword) throws Exception;

    void delete(Long id) throws Exception;
}
