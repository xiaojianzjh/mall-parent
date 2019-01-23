package com.mall.sellergoods.service;
import java.util.List;

import com.mall.entity.PageResult;
import com.mall.pojo.TbSpecificationOption;

/**
 * �����ӿ�
 * @author Administrator
 *
 */
public interface SpecificationOptionService {

	/**
	 * ����ȫ���б�
	 * @return
	 */
	public List<TbSpecificationOption> findAll();
	
	
	/**
	 * ���ط�ҳ�б�
	 * @return
	 */
	public PageResult findPage(int pageNum,int pageSize);
	
	
	/**
	 * ����
	*/
	public void add(TbSpecificationOption specificationOption);
	
	
	/**
	 * �޸�
	 */
	public void update(TbSpecificationOption specificationOption);
	

	/**
	 * ����ID��ȡʵ��
	 * @param id
	 * @return
	 */
	public TbSpecificationOption findOne(Long id);
	
	
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
	public PageResult findPage(TbSpecificationOption specificationOption, int pageNum,int pageSize);
	
}
