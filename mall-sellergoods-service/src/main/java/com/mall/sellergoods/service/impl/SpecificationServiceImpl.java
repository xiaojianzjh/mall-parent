package com.mall.sellergoods.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mall.entity.PageResult;
import com.mall.entity.Specification;
import com.mall.mapper.TbSpecificationMapper;
import com.mall.mapper.TbSpecificationOptionMapper;
import com.mall.pojo.TbSpecification;
import com.mall.pojo.TbSpecificationExample;
import com.mall.pojo.TbSpecificationExample.Criteria;
import com.mall.pojo.TbSpecificationOption;
import com.mall.pojo.TbSpecificationOptionExample;
import com.mall.sellergoods.service.SpecificationService;

/**
 * ����ʵ�ֲ�
 * 
 * @author Administrator
 *
 */
@Transactional
@Service
public class SpecificationServiceImpl implements SpecificationService {

	@Autowired
	private TbSpecificationMapper specificationMapper;

	@Autowired
	private TbSpecificationOptionMapper tbSpecificationOptionMapper;

	/**
	 * ��ѯȫ��
	 */
	@Override
	public List<TbSpecification> findAll() {
		return specificationMapper.selectByExample(null);
	}

	/**
	 * ����ҳ��ѯ
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		Page<TbSpecification> page = (Page<TbSpecification>) specificationMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * ����
	 */
	@Override
	public void add(Specification specification) {
		// ������
		specificationMapper.insert(specification.getSpecification());
		List<TbSpecificationOption> list = specification.getSpecificationOptionList();
		// ������ѡ��
		for (int i = 0; list != null && i < list.size(); i++) {
			TbSpecificationOption tbSpecificationOption = list.get(i);
			tbSpecificationOption.setSpecId(specification.getSpecification().getId());
			tbSpecificationOptionMapper.insert(tbSpecificationOption);
		}

	}

	/**
	 * �޸�
	 */
	@Override
	public void update(Specification specification) {
		// specificationMapper.updateByPrimaryKey(specification);
		specificationMapper.updateByPrimaryKey(specification.getSpecification());
		List<TbSpecificationOption> list = specification.getSpecificationOptionList();
		for (int i = 0; list!=null&&i < list.size(); i++) {
			TbSpecificationOption option = list.get(i);
			if(option.getId()==null)
			{
				option.setSpecId(specification.getSpecification().getId());
				tbSpecificationOptionMapper.insert(option);
				System.out.println(option.getOptionName()+"ѡ������������������");
			}else {
				tbSpecificationOptionMapper.updateByPrimaryKey(option);
				System.out.println(option.getOptionName()+"ѡ����¡�����������");
			}
		}
		
	}

	/**
	 * ����ID��ȡʵ��
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public Specification findOne(Long id) {
		
		Specification specification = new Specification();
		//��ȡ���
		TbSpecification tbspecification = specificationMapper.selectByPrimaryKey(id);
		//��ȡ���ѡ��
		TbSpecificationOptionExample example = new TbSpecificationOptionExample();
		com.mall.pojo.TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
		criteria.andSpecIdEqualTo(id);
		List<TbSpecificationOption> tbSpecificationOption = tbSpecificationOptionMapper.selectByExample(example);
		//��װspecification
		specification.setSpecification(tbspecification);
		specification.setSpecificationOptionList(tbSpecificationOption);
		
		return specification;
		
	}

	/**
	 * ����ɾ��
	 */
	@Override
	public void delete(Long[] ids) {
		for (Long id : ids) {
			specificationMapper.deleteByPrimaryKey(id);
		}
	}

	@Override
	public PageResult findPage(TbSpecification specification, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);

		TbSpecificationExample example = new TbSpecificationExample();
		Criteria criteria = example.createCriteria();

		if (specification != null) {
			if (specification.getSpecName() != null && specification.getSpecName().length() > 0) {
				criteria.andSpecNameLike("%" + specification.getSpecName() + "%");
			}

		}

		Page<TbSpecification> page = (Page<TbSpecification>) specificationMapper.selectByExample(example);
		return new PageResult(page.getTotal(), page.getResult());
	}

	@Override
	public List<Map> selectOptionList() {
		// TODO Auto-generated method stub
		return specificationMapper.selectOptionList();
	}

}
