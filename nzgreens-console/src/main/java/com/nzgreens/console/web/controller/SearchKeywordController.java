package com.nzgreens.console.web.controller;

import com.github.pagehelper.PageInfo;
import com.nzgreens.common.form.console.CoinSettingForm;
import com.nzgreens.common.form.console.PageSearchForm;
import com.nzgreens.common.model.ResultModel;
import com.nzgreens.console.annotations.Auth;
import com.nzgreens.console.service.ICoinSettingService;
import com.nzgreens.console.service.ISearchKeywordService;
import com.nzgreens.dal.user.example.CoinSetting;
import com.nzgreens.dal.user.example.SearchKeyword;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author:helizheng
 * @Date: Created in 2018/4/25 23:30
 */
@Controller
@RequestMapping("search/keyword")
public class SearchKeywordController {
    @Resource
    private ISearchKeywordService searchKeywordService;

    @RequestMapping("to-list")
    @Auth("SEARCH_KEYWORD_MANAGE")
    public String toList() throws Exception{
        return "product/search-keyword-list";
    }

    @RequestMapping("search-list")
    @Auth("SEARCH_KEYWORD_MANAGE")
    @ResponseBody
    public ResultModel selectForPage(PageSearchForm form,String keyword) throws Exception{
        ResultModel<PageInfo<SearchKeyword>> resultModel = new ResultModel<>();
        List<SearchKeyword> searchKeyword = searchKeywordService.selectForPage(form,keyword);
        PageInfo<SearchKeyword> pageInfo = new PageInfo<>(searchKeyword);
        resultModel.setData(pageInfo);
        return resultModel;
    }

    @RequestMapping("search-detail")
    @ResponseBody
    @Auth("SEARCH_KEYWORD_MANAGE")
    public ResultModel searchDetai(Long id) throws Exception{
        ResultModel<SearchKeyword> resultModel = new ResultModel<>();
        SearchKeyword users = searchKeywordService.selectDetail(id);
        resultModel.setData(users);
        return resultModel;
    }

    @RequestMapping("insert")
    @ResponseBody
    @Auth("SEARCH_KEYWORD_UPDATE")
    public ResultModel insert(SearchKeyword searchKeyword) throws Exception{
        searchKeywordService.insert(searchKeyword);
        return new ResultModel();
    }

    @RequestMapping("update")
    @ResponseBody
    @Auth("SEARCH_KEYWORD_UPDATE")
    public ResultModel update(SearchKeyword searchKeyword) throws Exception{
        searchKeywordService.update(searchKeyword);
        return new ResultModel();
    }

    @RequestMapping("delete")
    @ResponseBody
    @Auth("SEARCH_KEYWORD_UPDATE")
    public ResultModel delete(Long id) throws Exception{
        searchKeywordService.delete(id);
        return new ResultModel();
    }
}
