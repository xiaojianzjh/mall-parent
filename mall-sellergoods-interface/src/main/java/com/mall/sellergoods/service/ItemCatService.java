package com.mall.sellergoods.service;
import java.util.List;

import com.mall.entity.PageResult;
import com.mall.pojo.TbItemCat;

/**
 * �����ӿ�
 * @author Administrator
 *
 */
public interface ItemCatService {

	/**
	 * ����ȫ���б�
	 * @return
	 */
	public List<TbItemCat> findAll();
	
	
	/**
	 * ���ط�ҳ�б�
	 * @return
	 */
	public PageResult findPage(int pageNum,int pageSize);
	
	
	/**
	 * ����
	*/
	public void add(TbItemCat itemCat);
	
	
	/**
	 * �޸�
	 */
	public void update(TbItemCat itemCat);
	

	/**
	 * ����ID��ȡʵ��
	 * @param id
	 * @return
	 */
	public TbItemCat findOne(Long id);
	
	
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
	public PageResult findPage(TbItemCat itemCat, int pageNum,int pageSize);
	
	/**
	 * �����ϼ�ID�����б�
	 * @return
	 */
	public List<TbItemCat> findByParentId(Long parentId);
}