package cn.smartGym.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.smartGym.pojo.SmartgymApplications;
import cn.smartGym.pojoCtr.SmartgymApplicationsCtr;
import cn.smartGym.pojoCtr.SmartgymItemsCtr;
import cn.smartGym.service.ApplyService;
import cn.smartGym.service.ItemService;
import common.utils.SGResult;

@Controller
public class ApplyController {

	@Autowired
	private ApplyService applyService;

	@Autowired
	private ItemService itemService;
	/*
	 * @Autowired private SmartgymItemsMapper smartgymItemsMapper;
	 */

	/*
	 * @RequestMapping(value = "/apply/addapply", method = { RequestMethod.POST,
	 * RequestMethod.GET }, consumes = "application/x-www-form-urlencoded")
	 * 
	 * @ResponseBody public SGResult register(SmartgymApplications apply, String
	 * game, String categoty, String item) { // 查询报名项目 SmartgymItemsExample example
	 * = new SmartgymItemsExample(); Criteria criteria = example.createCriteria();
	 * criteria.andGameEqualTo(game); criteria.andCategoryEqualTo(categoty);
	 * criteria.andItemEqualTo(item); // 查询项目ID List<SmartgymItems> list =
	 * smartgymItemsMapper.selectByExample(example); if (list == null ||
	 * list.isEmpty()) return SGResult.build(401, "报名项目有误"); SmartgymItems
	 * itemObject = list.get(0); apply.setItemId(itemObject.getId());
	 * 
	 * //插入数据库 SGResult sgResult = applyService.addApply(apply); return sgResult; }
	 */

	@RequestMapping(value = "/apply/addapply", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded")
	@ResponseBody
	public SGResult addapply(SmartgymApplicationsCtr applyCtr) {
		SmartgymApplications apply = applyService.applyCtrtoDao(applyCtr);
		if (apply == null)
			SGResult.build(401, "报名项目有误！");
		// 插入数据库
		return applyService.addApply(apply);
	}

	@RequestMapping(value = "/apply/applypage", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded;charset=utf-8")
	@ResponseBody
	public SGResult applypage(SmartgymItemsCtr itemsCtr) {
		ArrayList<String> result = itemService.select(itemsCtr);
		if (result == null || result.size() == 0) {
			SGResult.build(401, "无法获取项目！");
		}
		return SGResult.build(200, "获取项目信息成功!", result);
	}
}
