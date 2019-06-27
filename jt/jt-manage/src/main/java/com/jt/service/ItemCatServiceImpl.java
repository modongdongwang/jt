package com.jt.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.jt.mapper.ItemCatMapper;
import com.jt.pojo.ItemCat;
import com.jt.util.ObjectMapperUtil;
import com.jt.util.RedisService;
import com.jt.vo.EasyUITree;


import redis.clients.jedis.ShardedJedis;

@Service
public  class ItemCatServiceImpl implements ItemCatService{
	@Autowired
	private ItemCatMapper itemCatMapper;
//	@Autowired
//	private Jedis jedis;
	/*@Autowired
	private ShardedJedis jedis;*/
	@Autowired
	private RedisService jedis;
	@Override
	public String findItemCatNameById(Long itemCatId) {
		
		 String name=itemCatMapper.selectById(itemCatId).getName();
		return name;
	}
	/**
	 * 1.根据parentId查询数据库记录返回itemCatList集合
	 * 2.将itemCatList集合中的数据按照指定的格式封装为
	 * List<EasyUITree>
	 */

	public List<EasyUITree> findItemCatNameByParendId(Long parentId) {
		List<ItemCat> carList=findItemCatList(parentId);
		List<EasyUITree> treeList = new ArrayList<>();
		//遍历集合数据,实现数据的转化
		for(ItemCat itemCat:carList) {
			EasyUITree uiTree = new EasyUITree();
			uiTree.setId(itemCat.getId());
			uiTree.setText(itemCat.getName());
			String state=itemCat.getIsParent()?"closed":"open";
			//如果是父级则closed不是则open
			uiTree.setState(state);
			treeList.add(uiTree);
		}
		return treeList;
	}
	private List<ItemCat> findItemCatList(Long parentId) {
		QueryWrapper<ItemCat> queryWrapper = new QueryWrapper<ItemCat>();
		queryWrapper.eq("parent_id", parentId);
		return itemCatMapper.selectList(queryWrapper);
	}
	
	@Override
	public List<EasyUITree> findItemCatByCache(Long parentId) {
		String key = "ITEM_CAT_"+parentId;
		System.out.println(parentId);
		String result = jedis.get(key);
		List<EasyUITree> treeList = new ArrayList<>();
		if(StringUtils.isEmpty(result)) {
			//如果为null,查询数据库
			treeList = findItemCatNameByParendId(parentId);
			//将数据转化为json
			String json = ObjectMapperUtil.toJSON(treeList);
			jedis.setex(key, 7*24*3600, json);
			System.out.println("业务查询数据库.......");
			
		}else {
			//表示缓存中有数据
			treeList = ObjectMapperUtil.toObject(result,treeList.getClass() );
			System.out.println("查询缓存数据");
		}
		return treeList;
	}


	
}
