package com.nzgreens.common.model.console;

import org.omg.CORBA.INTERNAL;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author:helizheng
 * @Date: Created in 2018/5/2 21:48
 */

public class UserOrderModel implements Serializable {
    private Long id;

    private Long userId;

    private String mobile;

    private String orderNumber;

    private Integer deliveryMode;

    private String address;

    private String contact;

    private String telephone;

    private Long productPrice;

    private Long freight;

    private Long price;

    private Integer type;

    private Integer status;

    private String logisticsNumber;

    private Long rebateId;
    /**
     * 返佣类型
     */
    private Integer rebateType;
    /**
     * 返佣金额
     */
    private Long rebatePrice;
    /**
     * 真实返佣金额
     */
    private Long actualRebatePrice;
    /**
     * 返佣状态
     */
    private Integer rebateStatus;
    /**
     * 返佣时间
     */
    private Date rebateCreateTime;
    /**
     * 返佣审核时间
     */
    private Date rebateUpdateTime;
    /**
     * 返佣备注
     */
    private String rebateRemark;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Integer getDeliveryMode() {
        return deliveryMode;
    }

    public void setDeliveryMode(Integer deliveryMode) {
        this.deliveryMode = deliveryMode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Long getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Long productPrice) {
        this.productPrice = productPrice;
    }

    public Long getFreight() {
        return freight;
    }

    public void setFreight(Long freight) {
        this.freight = freight;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getLogisticsNumber() {
        return logisticsNumber;
    }

    public void setLogisticsNumber(String logisticsNumber) {
        this.logisticsNumber = logisticsNumber;
    }

    public Integer getRebateType() {
        return rebateType;
    }

    public void setRebateType(Integer rebateType) {
        this.rebateType = rebateType;
    }

    public Long getRebatePrice() {
        return rebatePrice;
    }

    public void setRebatePrice(Long rebatePrice) {
        this.rebatePrice = rebatePrice;
    }

    public Long getActualRebatePrice() {
        return actualRebatePrice;
    }

    public void setActualRebatePrice(Long actualRebatePrice) {
        this.actualRebatePrice = actualRebatePrice;
    }

    public Integer getRebateStatus() {
        return rebateStatus;
    }

    public void setRebateStatus(Integer rebateStatus) {
        this.rebateStatus = rebateStatus;
    }

    public Date getRebateCreateTime() {
        return rebateCreateTime;
    }

    public void setRebateCreateTime(Date rebateCreateTime) {
        this.rebateCreateTime = rebateCreateTime;
    }

    public Date getRebateUpdateTime() {
        return rebateUpdateTime;
    }

    public void setRebateUpdateTime(Date rebateUpdateTime) {
        this.rebateUpdateTime = rebateUpdateTime;
    }

    public Long getRebateId() {
        return rebateId;
    }

    public void setRebateId(Long rebateId) {
        this.rebateId = rebateId;
    }

    public String getRebateRemark() {
        return rebateRemark;
    }

    public void setRebateRemark(String rebateRemark) {
        this.rebateRemark = rebateRemark;
    }
}
