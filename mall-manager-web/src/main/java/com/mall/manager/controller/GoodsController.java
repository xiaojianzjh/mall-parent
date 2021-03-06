package com.mall.manager.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.mall.entity.Goods;
import com.mall.entity.PageResult;
import com.mall.entity.Result;
import com.mall.page.service.ItemPageService;
import com.mall.pojo.TbGoods;
import com.mall.pojo.TbItem;
import com.mall.search.service.ItemSearchService;
import com.mall.sellergoods.service.GoodsService;

/**
 * controller
 * 
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

	@Reference
	private GoodsService goodsService;
	@Reference
	private ItemSearchService searchService;

	/**
	 * 返回全部列表
	 * 
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbGoods> findAll() {
		return goodsService.findAll();
	}

	/**
	 * 返回全部列表
	 * 
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult findPage(int page, int rows) {
		return goodsService.findPage(page, rows);
	}

	/**
	 * 增加
	 * 
	 * @param goods
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(@RequestBody TbGoods goods) {
		try {
			goodsService.add(goods);
			return new Result(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "增加失败");
		}
	}

	/**
	 * 修改
	 * 
	 * @param goods
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody Goods goods) {
		try {
			goodsService.update(goods);
			return new Result(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "修改失败");
		}
	}

	/**
	 * 获取实体
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/findOne")
	public Goods findOne(Long id) {
		return goodsService.findOne(id);
	}

	/**
	 * 批量删除
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public Result delete(Long[] ids) {
		try {
			goodsService.delete(ids);
			searchService.deleteByGoodsIds(Arrays.asList(ids));
			return new Result(true, "删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "删除失败");
		}
	}

	/**
	 * 查询+分页
	 * 
	 * @param brand
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbGoods goods, int page, int rows) {
		return goodsService.findPage(goods, page, rows);
	}

	/**
	 * 更改状态
	 * 
	 * @param goodsIds
	 *         		商品id
	 * @param status
	 *              状态
	 */
	@RequestMapping("/updateStatus")
	public Result updateStatus(Long[] goodsIds, String status) {
		try {
			goodsService.updateStatus(goodsIds, status);
			//商品审核通过，则导入到solr中，并且生成商品详细页面。
			if ("1".equals(status)) {
				List<TbItem> list = goodsService.findItemListByGoodsIdandStatus(goodsIds, status);
				for (TbItem tbItem : list) {
					System.out.println("id="+ tbItem.getId());
					System.out.println("title = "+tbItem.getTitle());
				}
				searchService.importList(list);
				//静态页生成
				for(Long goodsId:goodsIds){
					itemPageService.genItemHtml(goodsId);
				}	

			}
			
			return new Result(true, "成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "失败");
		}
	}

	@Reference(timeout = 4000)
	private ItemPageService itemPageService;

	/**
	 * 生成静态页（测试）
	 * 
	 * @param goodsId
	 */
	@RequestMapping("/genHtml")
	public void genHtml(Long goodsId) {
		itemPageService.genItemHtml(goodsId);
	}

}
