package com.mall.search.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.stax2.validation.Validatable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.GroupOptions;
import org.springframework.data.solr.core.query.HighlightOptions;
import org.springframework.data.solr.core.query.HighlightQuery;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleFilterQuery;
import org.springframework.data.solr.core.query.SimpleHighlightQuery;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.GroupEntry;
import org.springframework.data.solr.core.query.result.GroupPage;
import org.springframework.data.solr.core.query.result.GroupResult;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.data.solr.core.query.result.ScoredPage;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.mall.pojo.TbItem;
import com.mall.search.service.ItemSearchService;

@Service(timeout = 3000)
public class ItemSearchServiceImpl implements ItemSearchService {
	@Autowired
	private SolrTemplate solrTemplate;

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> search(Map searchMap) {
		// Map<String, Object> map = new HashMap<>();
		// Query query = new SimpleQuery();
		// // ��Ӳ�ѯ����
		// Criteria criteria = new
		// Criteria("item_keywords").is(searchMap.get("keywords"));
		// query.addCriteria(criteria);
		// ScoredPage<TbItem> page = solrTemplate.queryForPage(query, TbItem.class);
		// map.put("rows", page.getContent());
		Map map = new HashMap<>();
		map.putAll(searchList(searchMap));
		// ���ҷ���
		List categoryList = searchCategoryList(searchMap);
		map.put("categoryList", categoryList);
		// 3.��ѯƷ�ƺ͹���б�
		// if (categoryList.size() > 0) {
		// map.putAll(searchBrandAndSpecList((String) categoryList.get(0)));
		// }
		// 3.��ѯƷ�ƺ͹���б�
		String categoryName = (String) searchMap.get("category");
		// �ж����������ѯ�Ļ��ǵ�����м��ѯ��
		if (!"".equals(categoryName)) {
			// ����з�������
			map.putAll(searchBrandAndSpecList(categoryName));
		} else {
			// ���û�з������ƣ����յ�һ����ѯ
			if (categoryList.size() > 0) {
				map.putAll(searchBrandAndSpecList((String) categoryList.get(0)));
			}
		}
		return map;
	}

	@Autowired
	private RedisTemplate redisTemplate;

	/**
	 * ��ѯƷ�ƺ͹���б�
	 * 
	 * @param category
	 *            ��������
	 * @return
	 */
	private Map searchBrandAndSpecList(String category) {
		Map map = new HashMap();
		Long typeId = (Long) redisTemplate.boundHashOps("itemCat").get(category);// ��ȡģ��ID
		if (typeId != null) {
			// ����ģ��ID��ѯƷ���б�
			List brandList = (List) redisTemplate.boundHashOps("brandList").get(typeId);
			map.put("brandList", brandList);// ����ֵ���Ʒ���б�
			// ����ģ��ID��ѯ����б�
			List specList = (List) redisTemplate.boundHashOps("specList").get(typeId);
			map.put("specList", specList);
		}
		return map;
	}

	/**
	 * ���ݹؼ��������б� ������ʾ
	 * 
	 * @param keywords
	 * @return
	 */
	private Map searchList(Map searchMap) {
		Map map = new HashMap();
		// ������ѯ
		HighlightQuery query = new SimpleHighlightQuery();
		HighlightOptions highlightOptions = new HighlightOptions().addField("item_title");// ���ø�������
		highlightOptions.setSimplePrefix("<em style='color:red'>");// ����ǰ׺
		highlightOptions.setSimplePostfix("</em>");// ������׺
		query.setHighlightOptions(highlightOptions);// ���ø���ѡ��
		// ���չؼ��ֲ�ѯ
		String keywords = (String) searchMap.get("keywords");
		// �ո����ؼ����м��пո�������Ч
		if (keywords != null && !keywords.equals(""))
			keywords = keywords.replace(" ", "");
		System.out.println("keywords=" + keywords);
		System.out.println("oldkeywords" + (String) searchMap.get("keywords"));
		Criteria criteria = new Criteria("item_keywords").is(keywords);
		query.addCriteria(criteria);
		// ����ɸѡ
		// if (!"".equals(searchMap.get("category"))) {
		// SimpleFilterQuery simpleFilterQuery = new SimpleFilterQuery();
		// Criteria category_criteria = new
		// Criteria("item_category").is(searchMap.get("category"));
		// simpleFilterQuery.addCriteria(category_criteria);
		// query.addFilterQuery(simpleFilterQuery);
		// }
		// ������ɸѡ
		if (!"".equals(searchMap.get("category"))) {
			Criteria filterCriteria = new Criteria("item_category").is(searchMap.get("category"));
			SimpleFilterQuery filterQuery = new SimpleFilterQuery(filterCriteria);
			query.addFilterQuery(filterQuery);
		}

		// ��Ʒ��ɸѡ
		if (!"".equals(searchMap.get("brand"))) {
			Criteria filterCriteria = new Criteria("item_brand").is(searchMap.get("brand"));
			SimpleFilterQuery filterQuery = new SimpleFilterQuery(filterCriteria);
			query.addFilterQuery(filterQuery);
		}
		// ���չ��ɸѡ
		if (searchMap.get("spec") != null) {
			Map<String, String> specMap = (Map) searchMap.get("spec");
			for (String key : specMap.keySet()) {
				Criteria filterCriteria = new Criteria("item_spec_" + key).is(specMap.get(key));
				SimpleFilterQuery filterQuery = new SimpleFilterQuery(filterCriteria);
				query.addFilterQuery(filterQuery);
			}
		}
		// ���۸�ɸѡ.....
		if (searchMap.get("price") != null && !"".equals(searchMap.get("price"))) {
			String[] price = ((String) searchMap.get("price")).split("-");
			if (!price[0].equals("0")) {
				// ���������㲻����0
				Criteria filterCriteria = new Criteria("item_price").greaterThanEqual(price[0]);
				SimpleFilterQuery filterQuery = new SimpleFilterQuery(filterCriteria);
				query.addFilterQuery(filterQuery);
			}
			if (!price[1].equals("*")) {
				// ��������յ㲻����*
				Criteria filterCriteria = new Criteria("item_price").lessThanEqual(price[1]);
				SimpleFilterQuery filterQuery = new SimpleFilterQuery(filterCriteria);
				query.addFilterQuery(filterQuery);
			}
		}
		// 1.7����
		String sortValue = (String) searchMap.get("sort");// ASC DESC
		String sortField = (String) searchMap.get("sortField");// �����ֶ�
		if (sortValue != null && !sortValue.equals("")) {
			if (sortValue.equals("ASC")) {
				Sort sort = new Sort(Sort.Direction.ASC, "item_" + sortField);
				query.addSort(sort);
			}
			if (sortValue.equals("DESC")) {
				Sort sort = new Sort(Sort.Direction.DESC, "item_" + sortField);
				query.addSort(sort);
			}
		}
		// ��ҳ��ѯ
		Integer pageNo = (Integer) searchMap.get("pageNo");// ��ȡҳ��
		if (pageNo == null) {
			pageNo = 1;// Ĭ�ϵ�һҳ
		}
		Integer pageSize = (Integer) searchMap.get("pageSize");// ÿҳ��¼��
		if (pageSize == null) {
			pageSize = 20;// Ĭ��20
		}
		query.setOffset((pageNo - 1) * pageSize);
		query.setRows(pageSize);

		HighlightPage<TbItem> page = solrTemplate.queryForHighlightPage(query, TbItem.class);
		for (HighlightEntry<TbItem> h : page.getHighlighted()) {// ѭ��������ڼ���
			TbItem item = h.getEntity();// ��ȡԭʵ����
			if (h.getHighlights().size() > 0 && h.getHighlights().get(0).getSnipplets().size() > 0) {
				item.setTitle(h.getHighlights().get(0).getSnipplets().get(0));// ���ø����Ľ��
			}
		}
		map.put("rows", page.getContent());
		map.put("totalPages", page.getTotalPages());// ������ҳ��
		map.put("total", page.getTotalElements());// �����ܼ�¼��
		return map;
	}

	/**
	 * ��ѯ�����б�
	 * 
	 * @param searchMap
	 * @return
	 */
	private List searchCategoryList(Map searchMap) {
		List<String> list = new ArrayList();
		Query query = new SimpleQuery();
		// ���չؼ��ֲ�ѯ
		Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
		query.addCriteria(criteria);
		// ���÷���ѡ��
		GroupOptions groupOptions = new GroupOptions().addGroupByField("item_category");
		query.setGroupOptions(groupOptions);
		// �õ�����ҳ
		GroupPage<TbItem> page = solrTemplate.queryForGroupPage(query, TbItem.class);
		// �����еõ���������
		GroupResult<TbItem> groupResult = page.getGroupResult("item_category");
		// �õ����������ҳ
		Page<GroupEntry<TbItem>> groupEntries = groupResult.getGroupEntries();
		// �õ�������ڼ���
		List<GroupEntry<TbItem>> content = groupEntries.getContent();
		for (GroupEntry<TbItem> entry : content) {
			list.add(entry.getGroupValue());// �������������Ʒ�װ������ֵ��
		}
		return list;
	}

	@Override
	public void importList(List<TbItem> list) {
		// TODO Auto-generated method stub
		
		//����
		for(TbItem item:list){
			Map map= JSON.parseObject(item.getSpec());
			item.setSpecMap(map);
//			for (Object key : map.keySet()) {
//				Object value = map.get(key);
//				System.out.println("key="+key);
//				System.out.println("value="+value);
//			}
		}	
		solrTemplate.saveBeans(list);
		solrTemplate.commit();
	}

	@Override
	public void deleteByGoodsIds(List goodsIdList) {
		System.out.println("ɾ����ƷID"+goodsIdList);
		Query query=new SimpleQuery();		
		Criteria criteria=new Criteria("item_goodsid").in(goodsIdList);
		query.addCriteria(criteria);
		solrTemplate.delete(query);
		solrTemplate.commit();
	}
	

}
