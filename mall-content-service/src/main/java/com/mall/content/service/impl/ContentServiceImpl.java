package com.mall.content.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mall.content.service.ContentService;
import com.mall.entity.PageResult;
import com.mall.mapper.TbContentMapper;
import com.mall.pojo.TbContent;
import com.mall.pojo.TbContentExample;
import com.mall.pojo.TbContentExample.Criteria;

/**
 * ����ʵ�ֲ�
 * 
 * @author Administrator
 *
 */
@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private TbContentMapper contentMapper;

	/**
	 * ��ѯȫ��
	 */
	@Override
	public List<TbContent> findAll() {
		return contentMapper.selectByExample(null);
	}

	/**
	 * ����ҳ��ѯ
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		Page<TbContent> page = (Page<TbContent>) contentMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * ����
	 */
	@Override
	public void add(TbContent content) {
		contentMapper.insert(content);
		// �������
		redisTemplate.boundHashOps("content").delete(content.getCategoryId());

	}

	/**
	 * �޸�
	 */
	@Override
	@Transactional
	public void update(TbContent content) {
		//��ѯ�޸�ǰ�ķ���Id
		Long categoryId = contentMapper.selectByPrimaryKey(content.getId()).getCategoryId();
		contentMapper.updateByPrimaryKey(content);
		//�������ID�������޸�,����޸ĺ�ķ���ID�Ļ���
		if(categoryId.longValue()!=content.getCategoryId().longValue()){
			redisTemplate.boundHashOps("content").delete(categoryId);
			redisTemplate.boundHashOps("content").delete(content.getCategoryId());
		}	

	}

	/**
	 * ����ID��ȡʵ��
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public TbContent findOne(Long id) {
		return contentMapper.selectByPrimaryKey(id);
	}

	/**
	 * ����ɾ��
	 */
	@Override
	public void delete(Long[] ids) {
		for (Long id : ids) {
			Long categoryId = contentMapper.selectByPrimaryKey(id).getCategoryId();//������ID
			redisTemplate.boundHashOps("content").delete(categoryId);
			contentMapper.deleteByPrimaryKey(id);
		}
	}

	@Override
	public PageResult findPage(TbContent content, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);

		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();

		if (content != null) {
			if (content.getTitle() != null && content.getTitle().length() > 0) {
				criteria.andTitleLike("%" + content.getTitle() + "%");
			}
			if (content.getUrl() != null && content.getUrl().length() > 0) {
				criteria.andUrlLike("%" + content.getUrl() + "%");
			}
			if (content.getPic() != null && content.getPic().length() > 0) {
				criteria.andPicLike("%" + content.getPic() + "%");
			}
			if (content.getStatus() != null && content.getStatus().length() > 0) {
				criteria.andStatusLike("%" + content.getStatus() + "%");
			}

		}

		Page<TbContent> page = (Page<TbContent>) contentMapper.selectByExample(example);
		return new PageResult(page.getTotal(), page.getResult());
	}

	@Autowired
	private RedisTemplate redisTemplate;

	@Override
	public List<TbContent> findByCategoryId(Long categoryId) {
		List<TbContent> contentList = (List<TbContent>) redisTemplate.boundHashOps("content").get(categoryId);
		if (contentList == null) {
			System.out.println("�����ݿ��ȡ���ݷ��뻺��");
			// ���ݹ�����ID��ѯ����б�
			TbContentExample contentExample = new TbContentExample();
			Criteria criteria2 = contentExample.createCriteria();
			criteria2.andCategoryIdEqualTo(categoryId);
			criteria2.andStatusEqualTo("1");// ����״̬
			contentExample.setOrderByClause("sort_order");// ����
			contentList = contentMapper.selectByExample(contentExample);// ��ȡ����б�
			redisTemplate.boundHashOps("content").put(categoryId, contentList);// ���뻺��
		} else {
			System.out.println("�ӻ����ȡ����");
		}
		return contentList;
	}

}
