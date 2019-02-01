package com.mall.solrutil;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.mall.mapper.TbItemMapper;
import com.mall.pojo.TbItem;
import com.mall.pojo.TbItemExample;
import com.mall.pojo.TbItemExample.Criteria;

@Component
public class SolrUtil {
	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private SolrTemplate solrTemplate;

	/**
	 * ������Ʒ����
	 */
	public void importItemData(){
		TbItemExample example=new TbItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo("1");//�����
		List<TbItem> itemList = itemMapper.selectByExample(example);
		System.out.println("===��Ʒ�б�===");
		for(TbItem item:itemList){
			System.out.println(item.getTitle());	
			Map map= JSON.parseObject(item.getSpec());
			item.setSpecMap(map);
		}		
		solrTemplate.saveBeans(itemList);
		solrTemplate.commit();
		System.out.println("===����===");

	}	
	public void deleteData()
	{
		Query query=new SimpleQuery("*:*");
		solrTemplate.delete(query);
		solrTemplate.commit();
		System.out.println("ɾ���ɹ�");
	}
	public static void main(String[] args) {
		ApplicationContext context=new ClassPathXmlApplicationContext("classpath*:spring/applicationContext*.xml");
		SolrUtil solrUtil=  (SolrUtil) context.getBean("solrUtil");
		solrUtil.importItemData();
//		solrUtil.deleteData();
	}
}
