package com.nzgreens.common.form.console;

import java.io.Serializable;

/**
 * Created by z on 2016/10/10.
 */
public class PageSearchForm implements Serializable {
    private static final long serialVersionUID = 1L;
    //每页的数据大小
    private Integer pageSize = 10;
    //第几页
    private Integer pageNum = 1;

    //是否统计
    private Boolean count = true;

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Boolean getCount() {
        return count;
    }

    public void setCount(Boolean count) {
        this.count = count;
    }
}
