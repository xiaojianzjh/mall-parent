package com.mall.manager.controller;
import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.mall.content.service.ContentCategoryService;
import com.mall.entity.PageResult;
import com.mall.entity.Result;
import com.mall.pojo.TbContentCategory;
/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/contentCategory")
public class ContentCategoryController {

	@Reference(check=false)
	private ContentCategoryService contentCategoryService;
	
	/**
	 * ����ȫ���б�
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbContentCategory> findAll(){			
		return contentCategoryService.findAll();
	}
	
	
	/**
	 * ����ȫ���б�
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult  findPage(int page,int rows){			
		return contentCategoryService.findPage(page, rows);
	}
	
	/**
	 * ����
	 * @param contentCategory
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(@RequestBody TbContentCategory contentCategory){
		try {
			contentCategoryService.add(contentCategory);
			return new Result(true, "���ӳɹ�");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "����ʧ��");
		}
	}
	
	/**
	 * �޸�
	 * @param contentCategory
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody TbContentCategory contentCategory){
		try {
			contentCategoryService.update(contentCategory);
			return new Result(true, "�޸ĳɹ�");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "�޸�ʧ��");
		}
	}	
	
	/**
	 * ��ȡʵ��
	 * @param id
	 * @return
	 */
	@RequestMapping("/findOne")
	public TbContentCategory findOne(Long id){
		return contentCategoryService.findOne(id);		
	}
	
	/**
	 * ����ɾ��
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public Result delete(Long [] ids){
		try {
			contentCategoryService.delete(ids);
			return new Result(true, "ɾ���ɹ�"); 
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "ɾ��ʧ��");
		}
	}
	
		/**
	 * ��ѯ+��ҳ
	 * @param brand
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbContentCategory contentCategory, int page, int rows  ){
		return contentCategoryService.findPage(contentCategory, page, rows);		
	}
	
}
