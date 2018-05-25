package com.nzgreens.console.service;

import com.nzgreens.dal.user.example.ProductsCrawl;

import java.util.List;

/**
 * @Author:helizheng
 * @Date: Created in 2018/4/20 1:05
 */

public interface IProductTaskService {
    List<ProductsCrawl> queryProductIsExists() throws Exception;
}
