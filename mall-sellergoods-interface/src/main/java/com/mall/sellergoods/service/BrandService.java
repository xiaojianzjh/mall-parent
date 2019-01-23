package com.mall.sellergoods.service;

import java.util.List;
import java.util.Map;

import com.mall.entity.PageResult;
import com.mall.pojo.TbBrand;

public interface BrandService {
	
	/**
	 * ����ȫ���б�
	 * @return
	 */
	public List<TbBrand> findAll();

	/**
	 * ���ط�ҳ�б�
	 * @return
	 */
	public PageResult findPage(int pageNum, int pageSize);

	/**
	 * ����
	*/
	public void add(TbBrand brand);

	/**
	 * �޸�
	 */
	public void update(TbBrand brand);

	/**
	 * ����ID��ȡʵ��
	 * @param id
	 * @return
	 */
	public TbBrand findOne(Long id);
	
	/**
	 * ����ɾ��
	 * @param ids
	 */
	public void delete(Long [] ids);
	
	/**
	 * ��ҳ
	 * @param pageNum ��ǰҳ ��
	 * @param pageSize ÿҳ��¼��
	 * @return
	 */
	public PageResult findPage(TbBrand brand, int pageNum,int pageSize);

	public List<Map> selectOptionList();

}
