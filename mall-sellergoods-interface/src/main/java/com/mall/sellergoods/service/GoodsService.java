package com.mall.sellergoods.service;
import java.util.List;

import com.mall.entity.Goods;
import com.mall.entity.PageResult;
import com.mall.pojo.TbGoods;
import com.mall.pojo.TbItem;

/**
 * �����ӿ�
 * @author Administrator
 *
 */
public interface GoodsService {

	/**
	 * ����ȫ���б�
	 * @return
	 */
	public List<TbGoods> findAll();
	
	
	/**
	 * ���ط�ҳ�б�
	 * @return
	 */
	public PageResult findPage(int pageNum,int pageSize);
	
	
	/**
	 * ����
	*/
	public void add(TbGoods goods);
	
	
	/**
	 * �޸�
	 */
	public void update(Goods goods);
	

	/**
	 * ����ID��ȡʵ��
	 * @param id
	 * @return
	 */
	public Goods findOne(Long id);
	
	
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
	public PageResult findPage(TbGoods goods, int pageNum,int pageSize);
	
	/**
	 * ����
	*/
	public void add(Goods goods);

	public void updateStatus(Long[] id,String status);
	
	/**
	 * ������ƷID��״̬��ѯItem����Ϣ  
	 * @param goodsId
	 * @param status
	 * @return
	 */
	public List<TbItem> findItemListByGoodsIdandStatus(Long[] goodsIds, String status );

}
