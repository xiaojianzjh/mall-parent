package com.mall.sellergoods.service;
import java.util.List;
import java.util.Map;

import com.mall.entity.PageResult;
import com.mall.entity.Specification;
import com.mall.pojo.TbSpecification;

/**
 * �����ӿ�
 * @author Administrator
 *
 */
public interface SpecificationService {

	/**
	 * ����ȫ���б�
	 * @return
	 */
	public List<TbSpecification> findAll();
	
	
	/**
	 * ���ط�ҳ�б�
	 * @return
	 */
	public PageResult findPage(int pageNum,int pageSize);
	
	
	/**
	 * ����
	*/
	public void add(Specification specification);
	
	
	/**
	 * �޸�
	 */
	public void update(Specification specification);
	

	/**
	 * ����ID��ȡʵ��
	 * @param id
	 * @return
	 */
	public Specification findOne(Long id);
	
	
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
	public PageResult findPage(TbSpecification specification, int pageNum,int pageSize);
	
	public List<Map> selectOptionList();
}