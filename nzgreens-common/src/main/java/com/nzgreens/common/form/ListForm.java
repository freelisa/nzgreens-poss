package com.nzgreens.common.form;

import java.io.Serializable;

/**
 * Created by fei on 2017/5/28.
 */
public class ListForm extends BaseForm implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer page = 1;

	private Integer pageSize = 10;

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getStart() {
		return (page - 1) * pageSize;
	}


}
