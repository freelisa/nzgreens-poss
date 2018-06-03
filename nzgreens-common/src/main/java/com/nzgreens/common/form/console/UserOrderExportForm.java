package com.nzgreens.common.form.console;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @Author:helizheng
 * @Date: Created in 2018/6/3 16:51
 */

public class UserOrderExportForm {
    private String orderIdsExport;

    private List<Long> ids;

    private Long orderNumberExport;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

    private Integer status;

    public String getOrderIdsExport() {
        return orderIdsExport;
    }

    public void setOrderIdsExport(String orderIdsExport) {
        this.orderIdsExport = orderIdsExport;
    }

    public Long getOrderNumberExport() {
        return orderNumberExport;
    }

    public void setOrderNumberExport(Long orderNumberExport) {
        this.orderNumberExport = orderNumberExport;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }
}
