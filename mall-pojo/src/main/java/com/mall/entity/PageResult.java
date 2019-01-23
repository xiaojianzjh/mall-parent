package com.mall.entity;

import java.io.Serializable;
import java.util.List;

import com.mall.pojo.TbBrand;

public class PageResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long total;
	
	private List list;

	public long getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List getList() {
		return list;
	}

	public PageResult(long total, List list) {
		super();
		this.total = total;
		this.list = list;
	}

	public void setList(List list) {
		this.list = list;
	}
	
	

}
