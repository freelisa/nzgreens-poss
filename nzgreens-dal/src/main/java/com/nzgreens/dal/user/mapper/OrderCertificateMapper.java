package com.nzgreens.dal.user.mapper;

import com.nzgreens.dal.user.example.OrderCertificate;
import com.nzgreens.dal.user.example.OrderCertificateExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OrderCertificateMapper {
    int countByExample(OrderCertificateExample example);

    int deleteByExample(OrderCertificateExample example);

    int deleteByPrimaryKey(Long id);

    int insert(OrderCertificate record);

    int insertSelective(OrderCertificate record);

    List<OrderCertificate> selectByExample(OrderCertificateExample example);

    OrderCertificate selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") OrderCertificate record, @Param("example") OrderCertificateExample example);

    int updateByExample(@Param("record") OrderCertificate record, @Param("example") OrderCertificateExample example);

    int updateByPrimaryKeySelective(OrderCertificate record);

    int updateByPrimaryKey(OrderCertificate record);
}