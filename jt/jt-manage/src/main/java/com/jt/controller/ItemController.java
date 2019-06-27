package com.jt.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.service.ItemService;
import com.jt.vo.EasyUIData;
import com.jt.vo.SysResult;
@RestController
@RequestMapping("/item")
public class ItemController {
	@Autowired
	private ItemService itemService;
	//查询商品列表信息 分页查询
	//http://localhost:8091/item/query?page=1&rows=20
	@RequestMapping("/query")
	public EasyUIData findItemByPage(Integer page,Integer rows) {
		return itemService.findItemByPage(page, rows);
	}
	@RequestMapping("/save")
	public SysResult saveItem(Item item,ItemDesc itemDesc) {
		//实现数据新增
		try {
			itemService.saveItem(item,itemDesc);
			return SysResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.fail();
		}
	}
	//修改商品
	@RequestMapping("/update")
	public SysResult updateItem(Item item,ItemDesc itemDesc) {
		try {
			itemService.updateItem(item, itemDesc);
			return SysResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.fail("商品修改失败");
		}
	}
	/**
	 * 1.删除商品信息
	 * url="/item/delete"
	 * params = {"ids":ids}
	 * @return
	 */
	@RequestMapping("/delete")
	public SysResult deleteItem(Long[] ids) {
		try {
			itemService.deleteItem(ids);
			return SysResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.fail();
		}
	}
	//实现商品下架
	@RequestMapping("/instock")
	public SysResult instock(Long[] ids) {
		try {
			//int Status=1;上架
			int Status=2;//下架
			itemService.updateStatus(ids,Status);
			return SysResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.fail();
		}
	}
	//实现商品上架
		@RequestMapping("/reshelf")
		public SysResult reshelf(Long[] ids) {
			try {
				//int Status=2;上架
				int Status=1;//上架
				itemService.updateStatus(ids,Status);
				return SysResult.ok();
			} catch (Exception e) {
				e.printStackTrace();
				return SysResult.fail();
			}
		}
	//根据itemid查询商品详情信息
		@RequestMapping("/query/item/desc/{itemId}")
		public SysResult findItemDescById(@PathVariable Long itemId) {
			try {
				ItemDesc itemDesc=itemService.findItemDescById(itemId);
				return SysResult.ok(itemDesc);
			} catch (Exception e) {
				e.printStackTrace();
				return SysResult.fail();
			}
		}
}
