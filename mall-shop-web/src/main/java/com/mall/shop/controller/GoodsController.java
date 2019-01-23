package com.mall.shop.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.mall.entity.Goods;
import com.mall.entity.PageResult;
import com.mall.entity.Result;
import com.mall.pojo.TbGoods;
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
	public Result add(@RequestBody Goods goods) {
		// ��ȡ��¼��
		String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
		goods.getGoods().setSellerId(sellerId);// �����̼�ID
		System.out.println("sellerId===" + sellerId);
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
			//У���Ƿ��ǵ�ǰ�̼ҵ�id		
			Goods goods2 = goodsService.findOne(goods.getGoods().getId());
			//��ȡ��ǰ��¼���̼�ID
			String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
			//������ݹ������̼�ID�����ǵ�ǰ��¼���û���ID,�����ڷǷ�����
			if(!goods2.getGoods().getSellerId().equals(sellerId) ||  !goods.getGoods().getSellerId().equals(sellerId) ){
				return new Result(false, "�����Ƿ�");		
			}
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

		System.out.println("goods="+goods);
		// ��ȡ�̼�ID
		String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
		// ��Ӳ�ѯ����
		goods.setSellerId(sellerId);

		return goodsService.findPage(goods, page, rows);
	}

}
