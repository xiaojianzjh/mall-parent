package com.mall.page.service.impl;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import com.alibaba.dubbo.config.annotation.Service;
import com.mall.mapper.TbGoodsDescMapper;
import com.mall.mapper.TbGoodsMapper;
import com.mall.mapper.TbItemCatMapper;
import com.mall.mapper.TbItemMapper;
import com.mall.page.service.ItemPageService;
import com.mall.pojo.TbGoods;
import com.mall.pojo.TbGoodsDesc;
import com.mall.pojo.TbItem;
import com.mall.pojo.TbItemExample;
import com.mall.pojo.TbItemExample.Criteria;

import freemarker.template.Configuration;
import freemarker.template.Template;
@Service
public class ItemPageServiceImpl implements ItemPageService {

	@Value("${pagedir}")
	private String pagedir;
	
	@Autowired
	private FreeMarkerConfig freeMarkerConfig;
	
	@Autowired
	private TbGoodsMapper goodsMapper;
	
	@Autowired
	private TbGoodsDescMapper goodsDescMapper;
	
	@Autowired
	private ServletContext context;
	
	@Autowired
	private TbItemCatMapper itemCatMapper;

	@Autowired
	private TbItemMapper itemMapper;

	@Override
	public boolean genItemHtml(Long goodsId){				
		try {
			Configuration configuration = freeMarkerConfig.getConfiguration();
//			System.out.println( ContextLoader.getCurrentWebApplicationContext().getServletContext());
//			System.out.println(context);
			configuration.setServletContextForTemplateLoading( context, "/WEB-INF/");
			configuration.setDefaultEncoding("UTF-8");
			Template template = configuration.getTemplate("item.ftl");
			Map dataModel=new HashMap<>();			
			//1.加载商品表数据
			TbGoods goods = goodsMapper.selectByPrimaryKey(goodsId);
			dataModel.put("goods", goods);			
			//2.加载商品扩展表数据			
			TbGoodsDesc goodsDesc = goodsDescMapper.selectByPrimaryKey(goodsId);
			dataModel.put("goodsDesc", goodsDesc);				
			//3.加载商品分类
			//3.商品分类
			String itemCat1 = itemCatMapper.selectByPrimaryKey(goods.getCategory1Id()).getName();
			String itemCat2 = itemCatMapper.selectByPrimaryKey(goods.getCategory2Id()).getName();
			String itemCat3 = itemCatMapper.selectByPrimaryKey(goods.getCategory3Id()).getName();
			dataModel.put("itemCat1", itemCat1);
			dataModel.put("itemCat2", itemCat2);
			dataModel.put("itemCat3", itemCat3);
			
			//4.SKU列表			
			TbItemExample example=new TbItemExample();
			Criteria criteria = example.createCriteria();
			criteria.andStatusEqualTo("1");//状态为有效
			criteria.andGoodsIdEqualTo(goodsId);//指定SPU ID
			example.setOrderByClause("is_default desc");//按照状态降序，保证第一个为默认			
			List<TbItem> itemList = itemMapper.selectByExample(example);		
			dataModel.put("itemList", itemList);

			
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pagedir+goodsId+".html"), "UTF-8"));
			template.process(dataModel, writer);
			writer.close();
			return true;			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
