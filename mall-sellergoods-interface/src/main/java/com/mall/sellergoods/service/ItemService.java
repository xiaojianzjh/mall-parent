package com.mall.sellergoods.service;
import java.util.List;

import com.mall.entity.PageResult;
import com.mall.pojo.TbItem;

/**
 * �����ӿ�
 * @author Administrator
 *
 */
public interface ItemService {

	/**
	 * ����ȫ���б�
	 * @return
	 */
	public List<TbItem> findAll();
	
	
	/**
	 * ���ط�ҳ�б�
	 * @return
	 */
	public PageResult findPage(int pageNum,int pageSize);
	
	
	/**
	 * ����
	*/
	public void add(TbItem item);
	
	
	/**
	 * �޸�
	 */
	public void update(TbItem item);
	

	/**
	 * ����ID��ȡʵ��
	 * @param id
	 * @return
	 */
	public TbItem findOne(Long id);
	
	
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
	public PageResult findPage(TbItem item, int pageNum,int pageSize);
	
}
