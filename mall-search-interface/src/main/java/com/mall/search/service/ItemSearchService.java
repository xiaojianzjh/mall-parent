package com.mall.search.service;

import java.util.List;
import java.util.Map;

import com.mall.pojo.TbItem;

public interface ItemSearchService {
	/**
	 * ����
	 * @param keywords
	 * @return
	 */
	public Map<String,Object> search(Map searchMap);

	/**
	 * ��������
	 * @param list
	 */
	public void importList(List<TbItem> list);

	/**
	 * ɾ������
	 * @param ids
	 */
	public void deleteByGoodsIds(List goodsIdList);

}
