package com.jt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jt.anno.Cache_Find;
import com.jt.enu.KEY_ENUM;
import com.jt.service.ItemCatService;
import com.jt.vo.EasyUITree;

@RestController
@RequestMapping("/item/cat")
public class ItemCatController {
	@Autowired
	private ItemCatService itemCatService;
	//实现根据id查询商品分类信息
	/**
	 * 1.用户发起post请求携带了itemCatId=560
	 * 2.servlet request response
	 * @return
	 */
	@RequestMapping("/queryItemName")
	public String findItemCatNameById(Long itemCatId) {
		return itemCatService.findItemCatNameById(itemCatId);
		
	}
	//查询全部数据的商品分类信息
	//需要获取任意类型的参数,为指定的参数赋值
	//@RequestParam value/name 
	@RequestMapping("/list")
	@Cache_Find(key="ITEM_CAT_",keyType = KEY_ENUM.AUTO)
	public List<EasyUITree> findItemCatByParentId(@RequestParam(value="id",defaultValue = "0" )Long parentId){
		//Long parentId =0L;//查询一级商品分类信息
		//return itemCatService.findItemCatByCache(parentId);
		return itemCatService.findItemCatNameByParendId(parentId);
		
	}
//	
}
