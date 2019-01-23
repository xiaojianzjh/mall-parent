package com.mall.sellergoods.service;
import java.util.List;

import com.mall.entity.PageResult;
import com.mall.pojo.TbGoodsDesc;

/**
 * �����ӿ�
 * @author Administrator
 *
 */
public interface GoodsDescService {

	/**
	 * ����ȫ���б�
	 * @return
	 */
	public List<TbGoodsDesc> findAll();
	
	
	/**
	 * ���ط�ҳ�б�
	 * @return
	 */
	public PageResult findPage(int pageNum,int pageSize);
	
	
	/**
	 * ����
	*/
	public void add(TbGoodsDesc goodsDesc);
	
	
	/**
	 * �޸�
	 */
	public void update(TbGoodsDesc goodsDesc);
	

	/**
	 * ����ID��ȡʵ��
	 * @param id
	 * @return
	 */
	public TbGoodsDesc findOne(Long id);
	
	
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
	public PageResult findPage(TbGoodsDesc goodsDesc, int pageNum,int pageSize);
	
}
