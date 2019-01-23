package com.mall.entity;

import java.io.Serializable;
import java.util.List;

import com.mall.pojo.TbGoods;
import com.mall.pojo.TbGoodsDesc;
import com.mall.pojo.TbItem;

public class Goods implements Serializable {
	private TbGoods goods;// ��ƷSPU
	private TbGoodsDesc goodsDesc;// ��Ʒ��չ
	private List<TbItem> itemList;// ��ƷSKU�б�
	// getter and setter����......
	public TbGoods getGoods() {
		return goods;
	}

	public void setGoods(TbGoods goods) {
		this.goods = goods;
	}

	public TbGoodsDesc getGoodsDesc() {
		return goodsDesc;
	}

	public void setGoodsDesc(TbGoodsDesc goodsDesc) {
		this.goodsDesc = goodsDesc;
	}

	public List<TbItem> getItemList() {
		return itemList;
	}

	public void setItemList(List<TbItem> itemList) {
		this.itemList = itemList;
	}

}
