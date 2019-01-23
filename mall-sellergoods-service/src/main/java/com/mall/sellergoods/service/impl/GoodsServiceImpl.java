package com.mall.sellergoods.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mall.entity.Goods;
import com.mall.entity.PageResult;
import com.mall.mapper.TbBrandMapper;
import com.mall.mapper.TbGoodsDescMapper;
import com.mall.mapper.TbGoodsMapper;
import com.mall.mapper.TbItemCatMapper;
import com.mall.mapper.TbItemMapper;
import com.mall.mapper.TbSellerMapper;
import com.mall.pojo.TbBrand;
import com.mall.pojo.TbGoods;
import com.mall.pojo.TbGoodsDesc;
import com.mall.pojo.TbGoodsExample;
import com.mall.pojo.TbGoodsExample.Criteria;
import com.mall.pojo.TbItem;
import com.mall.pojo.TbItemCat;
import com.mall.pojo.TbItemExample;
import com.mall.pojo.TbSeller;
import com.mall.sellergoods.service.GoodsService;

/**
 * ����ʵ�ֲ�
 * 
 * @author Administrator
 *
 */
@Transactional
@Service
public class GoodsServiceImpl implements GoodsService {

	@Autowired
	private TbGoodsMapper goodsMapper;

	@Autowired
	private TbGoodsDescMapper goodsDescMapper;
	
	@Autowired
	private TbItemMapper itemMapper;
	
	@Autowired
	private TbBrandMapper brandMapper;
	
	@Autowired
	private TbItemCatMapper itemCatMapper;
	
	@Autowired
	private TbSellerMapper sellerMapper;

	/**
	 * ��ѯȫ��
	 */
	@Override
	public List<TbGoods> findAll() {
		return goodsMapper.selectByExample(null);
	}

	/**
	 * ����ҳ��ѯ
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		Page<TbGoods> page = (Page<TbGoods>) goodsMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * ����
	 */
	@Override
	public void add(TbGoods goods) {
		goodsMapper.insert(goods);
	}

	/**
	 * �޸�
	 */
	@Override
	public void update(Goods goods) {
		TbGoods tbGoods = goods.getGoods();
		//�޸ĺ���Ҫ�������
		tbGoods.setAuditStatus("0");
		goodsMapper.updateByPrimaryKey(tbGoods);
		TbGoodsDesc goodsDesc = goods.getGoodsDesc();
		goodsDescMapper.updateByPrimaryKey(goodsDesc);
		//ɾ��ԭ����sku�б�
		TbItemExample example = new TbItemExample();
		com.mall.pojo.TbItemExample.Criteria criteria = example.createCriteria();
		criteria.andGoodsIdEqualTo(tbGoods.getId());
		itemMapper.deleteByExample(example);
		
		//����sku�б�
		insertSku(goods);
	}

	/**
	 * ����ID��ȡʵ��
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public Goods findOne(Long id) {
		Goods goods = new Goods();
		TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
		goods.setGoods(tbGoods);
		TbGoodsDesc desc = goodsDescMapper.selectByPrimaryKey(id);
		goods.setGoodsDesc(desc);
		//��ѯSKU��Ʒ�б�
		TbItemExample example=new TbItemExample();
		com.mall.pojo.TbItemExample.Criteria criteria = example.createCriteria();
		criteria.andGoodsIdEqualTo(id);//��ѯ��������ƷID
		List<TbItem> itemList = itemMapper.selectByExample(example);		
		goods.setItemList(itemList);

		return goods;
	}

	/**
	 * ����ɾ��(αɾ������isDelete����Ϊ1����������)
	 */
	@Override
	public void delete(Long[] ids) {
		for (Long id : ids) {
			TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
			tbGoods.setIsDelete("1");
			goodsMapper.updateByPrimaryKey(tbGoods);
		}
	}

	@Override
	public PageResult findPage(TbGoods goods, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);

		TbGoodsExample example = new TbGoodsExample();
		Criteria criteria = example.createCriteria();

		if (goods != null) {
			if (goods.getSellerId() != null && goods.getSellerId().length() > 0) {
				criteria.andSellerIdEqualTo(goods.getSellerId());
			}
			if (goods.getGoodsName() != null && goods.getGoodsName().length() > 0) {
				criteria.andGoodsNameLike("%" + goods.getGoodsName() + "%");
			}
			if (goods.getAuditStatus() != null && goods.getAuditStatus().length() > 0) {
				criteria.andAuditStatusLike("%" + goods.getAuditStatus() + "%");
			}
			if (goods.getIsMarketable() != null && goods.getIsMarketable().length() > 0) {
				criteria.andIsMarketableLike("%" + goods.getIsMarketable() + "%");
			}
			if (goods.getCaption() != null && goods.getCaption().length() > 0) {
				criteria.andCaptionLike("%" + goods.getCaption() + "%");
			}
			if (goods.getSmallPic() != null && goods.getSmallPic().length() > 0) {
				criteria.andSmallPicLike("%" + goods.getSmallPic() + "%");
			}
			if (goods.getIsEnableSpec() != null && goods.getIsEnableSpec().length() > 0) {
				criteria.andIsEnableSpecLike("%" + goods.getIsEnableSpec() + "%");
			}
			if (goods.getIsDelete() != null && goods.getIsDelete().length() > 0) {
				criteria.andIsDeleteLike("%" + goods.getIsDelete() + "%");
			}

		}
		//�ų���ɾ���ļ�¼
		criteria.andIsDeleteIsNull();
		Page<TbGoods> page = (Page<TbGoods>) goodsMapper.selectByExample(example);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * ����
	 */
	@Override
	public void add(Goods goods) {
		goods.getGoods().setAuditStatus("0");		
		goodsMapper.insert(goods.getGoods());	//������Ʒ��
		goods.getGoodsDesc().setGoodsId(goods.getGoods().getId());
		goodsDescMapper.insert(goods.getGoodsDesc());//������Ʒ��չ����
		insertSku(goods);	
	}

	/**
	 * ����sku����
	 * @param goods
	 */
	private void insertSku(Goods goods) {
		if("1".equals(goods.getGoods().getIsEnableSpec())){
			for(TbItem item :goods.getItemList()){
				//����
				String title= goods.getGoods().getGoodsName();
				Map<String,Object> specMap = JSON.parseObject(item.getSpec());
				for(String key:specMap.keySet()){
					title+=" "+ specMap.get(key);
				}
				item.setTitle(title);
				setItemValues(goods,item);
				itemMapper.insert(item);
			}		
		}else{					
			TbItem item=new TbItem();
			item.setTitle(goods.getGoods().getGoodsName());//��ƷKPU+�����������ΪSKU����
			item.setPrice( goods.getGoods().getPrice() );//�۸�			
			item.setStatus("1");//״̬
			item.setIsDefault("1");//�Ƿ�Ĭ��			
			item.setStockCount(99999);//�������
			item.setSpec("{}");			
			setItemValues(goods,item);					
			itemMapper.insert(item);
		} 
	}
	
	private void setItemValues(Goods goods,TbItem item) {
		item.setGoodsId(goods.getGoods().getId());//��ƷSPU���
		item.setSellerId(goods.getGoods().getSellerId());//�̼ұ��
		item.setCategoryid(goods.getGoods().getCategory3Id());//��Ʒ�����ţ�3����
		item.setCreateTime(new Date());//��������
		item.setUpdateTime(new Date());//�޸����� 
		
		//Ʒ������
		TbBrand brand = brandMapper.selectByPrimaryKey(goods.getGoods().getBrandId());
		item.setBrand(brand.getName());
		//��������
		TbItemCat itemCat = itemCatMapper.selectByPrimaryKey(goods.getGoods().getCategory3Id());
		item.setCategory(itemCat.getName());
		
		//�̼�����
		TbSeller seller = sellerMapper.selectByPrimaryKey(goods.getGoods().getSellerId());
		item.setSeller(seller.getNickName());
		
		//ͼƬ��ַ��ȡspu�ĵ�һ��ͼƬ��
		List<Map> imageList = JSON.parseArray(goods.getGoodsDesc().getItemImages(), Map.class) ;
		if(imageList.size()>0){
			item.setImage ( (String)imageList.get(0).get("url"));
		}		
	}

	@Override
	public void updateStatus(Long[] ids, String status) {
		// TODO Auto-generated method stub
		TbGoodsExample example = new TbGoodsExample();
		example.createCriteria().andIdIn(Arrays.asList(ids));
		List<TbGoods> goods = goodsMapper.selectByExample(example);
		for (TbGoods tbGoods : goods) {
			tbGoods.setAuditStatus(status);
			goodsMapper.updateByPrimaryKey(tbGoods);
		}
	}

	@Override
	public List<TbItem> findItemListByGoodsIdandStatus(Long[] goodsIds, String status) {
		// TODO Auto-generated method stub
		
		TbItemExample example = new TbItemExample();
		com.mall.pojo.TbItemExample.Criteria criteria = example.createCriteria().andStatusEqualTo(status);
		criteria.andGoodsIdIn(Arrays.asList(goodsIds));
		List<TbItem> list = itemMapper.selectByExample(example);
		return list;
	}
	

}
