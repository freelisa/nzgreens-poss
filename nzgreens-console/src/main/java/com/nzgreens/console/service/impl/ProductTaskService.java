package com.nzgreens.console.service.impl;

import com.nzgreens.console.service.BaseService;
import com.nzgreens.console.service.IProductTaskService;
import com.nzgreens.dal.user.example.ProductsCrawl;
import com.nzgreens.dal.user.mapper.ProductTaskMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author:helizheng
 * @Date: Created in 2018/4/20 1:06
 */
@Service
public class ProductTaskService extends BaseService implements IProductTaskService {
    @Resource
    private ProductTaskMapper productTaskMapper;

    @Override
    public List<ProductsCrawl> queryProductIsExists() throws Exception {
        return productTaskMapper.queryProductIsExists();
    }
}
