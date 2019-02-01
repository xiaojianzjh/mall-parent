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
	 * 导入商品数据
	 */
	public void importItemData(){
		TbItemExample example=new TbItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo("1");//已审核
		List<TbItem> itemList = itemMapper.selectByExample(example);
		System.out.println("===商品列表===");
		for(TbItem item:itemList){
			System.out.println(item.getTitle());	
			Map map= JSON.parseObject(item.getSpec());
			item.setSpecMap(map);
		}		
		solrTemplate.saveBeans(itemList);
		solrTemplate.commit();
		System.out.println("===结束===");

	}	
	public void deleteData()
	{
		Query query=new SimpleQuery("*:*");
		solrTemplate.delete(query);
		solrTemplate.commit();
		System.out.println("删除成功");
	}
	public static void main(String[] args) {
		ApplicationContext context=new ClassPathXmlApplicationContext("classpath*:spring/applicationContext*.xml");
		SolrUtil solrUtil=  (SolrUtil) context.getBean("solrUtil");
		solrUtil.importItemData();
//		solrUtil.deleteData();
	}
}
