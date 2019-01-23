package com.mall.sellergoods.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mall.entity.PageResult;
import com.mall.mapper.TbSpecificationOptionMapper;
import com.mall.mapper.TbTypeTemplateMapper;
import com.mall.pojo.TbBrand;
import com.mall.pojo.TbSpecification;
import com.mall.pojo.TbSpecificationOption;
import com.mall.pojo.TbSpecificationOptionExample;
import com.mall.pojo.TbTypeTemplate;
import com.mall.pojo.TbTypeTemplateExample;
import com.mall.pojo.TbTypeTemplateExample.Criteria;
import com.mall.sellergoods.service.TypeTemplateService;

/**
 * ����ʵ�ֲ�
 * 
 * @author Administrator
 *
 */
@Service
public class TypeTemplateServiceImpl implements TypeTemplateService {

	@Autowired
	private TbTypeTemplateMapper typeTemplateMapper;

	@Autowired
	private TbSpecificationOptionMapper specificationOptionMapper;

	@Autowired
	private RedisTemplate template;
	/**
	 * ��ѯȫ��
	 */
	@Override
	public List<TbTypeTemplate> findAll() {
		return typeTemplateMapper.selectByExample(null);
	}

	/**
	 * ����ҳ��ѯ
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		Page<TbTypeTemplate> page = (Page<TbTypeTemplate>) typeTemplateMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * ����
	 */
	@Override
	public void add(TbTypeTemplate typeTemplate) {
		typeTemplateMapper.insert(typeTemplate);
	}

	/**
	 * �޸�
	 */
	@Override
	public void update(TbTypeTemplate typeTemplate) {
		typeTemplateMapper.updateByPrimaryKey(typeTemplate);
	}

	/**
	 * ����ID��ȡʵ��
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public TbTypeTemplate findOne(Long id) {
		return typeTemplateMapper.selectByPrimaryKey(id);
	}

	/**
	 * ����ɾ��
	 */
	@Override
	public void delete(Long[] ids) {
		for (Long id : ids) {
			typeTemplateMapper.deleteByPrimaryKey(id);
		}
	}
	/**
	 * @param
	 * �������ķ�ҳ��ѯ��ÿ����ɾ�Ľ����󶼵õ��ã������ʺ�ִ�л��淽��
	 */
	@Override
	public PageResult findPage(TbTypeTemplate typeTemplate, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);

		TbTypeTemplateExample example = new TbTypeTemplateExample();
		Criteria criteria = example.createCriteria();

		if (typeTemplate != null) {
			if (typeTemplate.getName() != null && typeTemplate.getName().length() > 0) {
				criteria.andNameLike("%" + typeTemplate.getName() + "%");
			}
			if (typeTemplate.getSpecIds() != null && typeTemplate.getSpecIds().length() > 0) {
				criteria.andSpecIdsLike("%" + typeTemplate.getSpecIds() + "%");
			}
			if (typeTemplate.getBrandIds() != null && typeTemplate.getBrandIds().length() > 0) {
				criteria.andBrandIdsLike("%" + typeTemplate.getBrandIds() + "%");
			}
			if (typeTemplate.getCustomAttributeItems() != null && typeTemplate.getCustomAttributeItems().length() > 0) {
				criteria.andCustomAttributeItemsLike("%" + typeTemplate.getCustomAttributeItems() + "%");
			}

		}
		//��ѯ
		Page<TbTypeTemplate> page = (Page<TbTypeTemplate>) typeTemplateMapper.selectByExample(example);
		//����Ʒ���б�͹���б�redis
		saveToCache();
		return new PageResult(page.getTotal(), page.getResult());
	}
	@Override
	public List<Map> findSpecList(Long id) {
		// ��ѯģ��
		TbTypeTemplate typeTemplate = typeTemplateMapper.selectByPrimaryKey(id);

		List<Map> list = JSON.parseArray(typeTemplate.getSpecIds(), Map.class);
		for (Map map : list) {
			// ��ѯ���ѡ���б�
			TbSpecificationOptionExample example = new TbSpecificationOptionExample();
			com.mall.pojo.TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
			criteria.andSpecIdEqualTo(new Long((Integer) map.get("id")));
			List<TbSpecificationOption> options = specificationOptionMapper.selectByExample(example);
			map.put("options", options);
		}
		return list;
	}
	public void saveToCache()
	{
		List<TbTypeTemplate> list = findAll();
		for (TbTypeTemplate tbTypeTemplate : list) {
			List<Map> brandList =  JSON.parseArray(tbTypeTemplate.getBrandIds(),Map.class);
			List<Map> specList = findSpecList(tbTypeTemplate.getId());
			template.boundHashOps("brandList").put(tbTypeTemplate.getId(),brandList );
			template.boundHashOps("specList").put(tbTypeTemplate.getId(), specList );
			
		}
	}
}
