package com.nzgreens.console.service.impl;

import com.github.pagehelper.PageHelper;
import com.nzgreens.common.exception.ErrorCodes;
import com.nzgreens.common.form.console.CoinSettingForm;
import com.nzgreens.common.form.console.PageSearchForm;
import com.nzgreens.common.utils.CurrencyUtil;
import com.nzgreens.console.service.BaseService;
import com.nzgreens.console.service.ICoinSettingService;
import com.nzgreens.console.service.ISearchKeywordService;
import com.nzgreens.dal.user.example.CoinSetting;
import com.nzgreens.dal.user.example.SearchKeyword;
import com.nzgreens.dal.user.example.SearchKeywordExample;
import com.nzgreens.dal.user.mapper.CoinSettingMapper;
import com.nzgreens.dal.user.mapper.SearchKeywordMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author:helizheng
 * @Date: Created in 2018/4/26 23:46
 */
@Service
public class SearchKeywordService extends BaseService implements ISearchKeywordService {
    @Resource
    private SearchKeywordMapper searchKeywordMapper;

    @Override
    public List<SearchKeyword> selectForPage(PageSearchForm form,String keyWord) throws Exception {
        PageHelper.startPage(form.getPageNum(),form.getPageSize());
        SearchKeywordExample example = new SearchKeywordExample();
        if(StringUtils.isNotBlank(keyWord)){
            example.createCriteria().andKeywordEqualTo(keyWord);
        }
        return searchKeywordMapper.selectByExample(example);
    }

    @Override
    public SearchKeyword selectDetail(Long id) throws Exception {
        return searchKeywordMapper.selectByPrimaryKey(id);
    }

    @Override
    public void insert(SearchKeyword searchKeyword) throws Exception {
        checkForm(searchKeyword);
        if(searchKeywordMapper.insertSelective(searchKeyword) < 1){
            thrown(ErrorCodes.UPDATE_ERROR);
        }
    }

    @Override
    public void update(SearchKeyword searchKeyword) throws Exception {
        checkForm(searchKeyword);
        if(searchKeyword.getId() == null){
            thrown(ErrorCodes.ID_ILLEGAL);
        }
        if(searchKeywordMapper.updateByPrimaryKeySelective(searchKeyword) < 1){
            thrown(ErrorCodes.UPDATE_ERROR);
        }
    }

    @Override
    public void delete(Long id) throws Exception {
        if(id == null){
            thrown(ErrorCodes.ID_ILLEGAL);
        }
        if(searchKeywordMapper.deleteByPrimaryKey(id) < 1){
            thrown(ErrorCodes.UPDATE_ERROR);
        }
    }

    protected void checkForm(SearchKeyword searchKeyword) throws Exception{
        if(searchKeyword == null){
            thrown(ErrorCodes.DATA_ILLEGAL);
        }
        if(StringUtils.isBlank(searchKeyword.getKeyword())){
            thrown(ErrorCodes.SEARCH_KEYWORD_ILLEGAL);
        }
    }
}
