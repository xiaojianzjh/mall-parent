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
	 * ����ȫ���б�
	 * 
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbGoods> findAll() {
		return goodsService.findAll();
	}

	/**
	 * ����ȫ���б�
	 * 
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult findPage(int page, int rows) {
		return goodsService.findPage(page, rows);
	}

	/**
	 * ����
	 * 
	 * @param goods
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(@RequestBody TbGoods goods) {
		try {
			goodsService.add(goods);
			return new Result(true, "���ӳɹ�");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "����ʧ��");
		}
	}

	/**
	 * �޸�
	 * 
	 * @param goods
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody Goods goods) {
		try {
			goodsService.update(goods);
			return new Result(true, "�޸ĳɹ�");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "�޸�ʧ��");
		}
	}

	/**
	 * ��ȡʵ��
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/findOne")
	public Goods findOne(Long id) {
		return goodsService.findOne(id);
	}

	/**
	 * ����ɾ��
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public Result delete(Long[] ids) {
		try {
			goodsService.delete(ids);
			searchService.deleteByGoodsIds(Arrays.asList(ids));
			return new Result(true, "ɾ���ɹ�");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "ɾ��ʧ��");
		}
	}

	/**
	 * ��ѯ+��ҳ
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
	 * ����״̬
	 * 
	 * @param goodsIds
	 *         		��Ʒid
	 * @param status
	 *              ״̬
	 */
	@RequestMapping("/updateStatus")
	public Result updateStatus(Long[] goodsIds, String status) {
		try {
			goodsService.updateStatus(goodsIds, status);
			//��Ʒ���ͨ�������뵽solr�У�����������Ʒ��ϸҳ�档
			if ("1".equals(status)) {
				List<TbItem> list = goodsService.findItemListByGoodsIdandStatus(goodsIds, status);
				for (TbItem tbItem : list) {
					System.out.println("id="+ tbItem.getId());
					System.out.println("title = "+tbItem.getTitle());
				}
				searchService.importList(list);
				//��̬ҳ����
				for(Long goodsId:goodsIds){
					itemPageService.genItemHtml(goodsId);
				}	

			}
			
			return new Result(true, "�ɹ�");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "ʧ��");
		}
	}

	@Reference(timeout = 4000)
	private ItemPageService itemPageService;

	/**
	 * ���ɾ�̬ҳ�����ԣ�
	 * 
	 * @param goodsId
	 */
	@RequestMapping("/genHtml")
	public void genHtml(Long goodsId) {
		itemPageService.genItemHtml(goodsId);
	}

}
