package com.nzgreens.dal.user.mapper;

import com.nzgreens.dal.user.example.ProductsCrawl;

import java.util.List;

public interface ProductTaskMapper {
    List<ProductsCrawl> queryProductIsExists() throws Exception;
}