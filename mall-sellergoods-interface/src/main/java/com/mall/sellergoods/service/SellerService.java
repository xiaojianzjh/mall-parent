package com.mall.sellergoods.service;
import java.util.List;

import com.mall.entity.PageResult;
import com.mall.pojo.TbSeller;

/**
 * �����ӿ�
 * @author Administrator
 *
 */
public interface SellerService {

	/**
	 * ����ȫ���б�
	 * @return
	 */
	public List<TbSeller> findAll();
	
	
	/**
	 * ���ط�ҳ�б�
	 * @return
	 */
	public PageResult findPage(int pageNum,int pageSize);
	
	
	/**
	 * ����
	*/
	public void add(TbSeller seller);
	
	
	/**
	 * �޸�
	 */
	public void update(TbSeller seller);
	

	/**
	 * ����ID��ȡʵ��
	 * @param id
	 * @return
	 */
	public TbSeller findOne(String id);
	
	
	/**
	 * �����û�����ȡʵ��
	 * @param id
	 * @return
	 */
//	public TbSeller findOneByUsername(String name);
	
	
	/**
	 * ����ɾ��
	 * @param ids
	 */
	public void delete(String [] ids);

	/**
	 * ��ҳ
	 * @param pageNum ��ǰҳ ��
	 * @param pageSize ÿҳ��¼��
	 * @return
	 */
	public PageResult findPage(TbSeller seller, int pageNum,int pageSize);
	
	/**
	 * ����״̬
	 * @param id
	 * @param status
	 */
	public void updateStatus(String id,String status);
	
}
