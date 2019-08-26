package com.nzgreens.dal.user.mapper;

import com.nzgreens.dal.user.example.UserOrderCount;
import com.nzgreens.dal.user.example.UserOrderExample;

import java.util.List;

public interface UserOrderCountMapper {

    List<UserOrderCount> countByExamples(UserOrderExample example);

}