package cn.smartGym.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.smartGym.mapper.SmartgymUsersMapper;
import cn.smartGym.pojo.SmartgymUsers;
import cn.smartGym.pojo.SmartgymUsersExample;
import cn.smartGym.pojo.SmartgymUsersExample.Criteria;
import cn.smartGym.pojoCtr.SmartgymUsersCtr;
import cn.smartGym.service.UserService;
import common.utils.IDUtils;
import common.utils.SGResult;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private SmartgymUsersMapper smartgymUsersMapper;

	@Override
	public SGResult checkData(String param, int type) {
		// 根据不同的type生成不同的查询条件
		SmartgymUsersExample example = new SmartgymUsersExample();
		Criteria criteria = example.createCriteria();
		// 1：学号 2：用户名 3：手机号
		if (type == 1) {
			criteria.andStudentnoEqualTo(param);
		} else if (type == 2) {
			criteria.andUsernameEqualTo(param);
		} else if (type == 3) {
			criteria.andPhoneEqualTo(param);
		} else {
			return SGResult.build(400, "数据类型错误");
		}
		// 执行查询
		List<SmartgymUsers> list = smartgymUsersMapper.selectByExample(example);
		// 判断结果中是否包含数据
		if (list != null && list.size() > 0) {
			// 如果有数据返回false
			return SGResult.ok(false);
		}
		// 如果没有数据返回true
		return SGResult.ok(true);
	}

	@Override
	public SGResult register(SmartgymUsers user) {
		// 数据有效性检验
		if (StringUtils.isBlank(user.getStudentno()) || StringUtils.isBlank(user.getUsername())
				|| StringUtils.isBlank(user.getPhone()) || StringUtils.isBlank(user.getWxid()))
			return SGResult.build(401, "用户数据不完整，注册失败");
		// 1：学号 2：用户名 3：手机号
		SGResult result = checkData(user.getStudentno(), 1);
		if (!(boolean) result.getData()) {
			return SGResult.build(400, "此学号已经被注册");
		}
		result = checkData(user.getPhone(), 3);
		if (!(boolean) result.getData()) {
			return SGResult.build(400, "此手机号已经被占用");
		}
//		result = checkData(user.getUsername(), 2);
//		if (!(boolean) result.getData()) {
//			return SGResult.build(400, "此用户名已经被占用");
//		}

		// 补全pojo属性
		user.setAuthority(0); // 0是普通用户
		user.setCreated(new Date());
		user.setUpdated(new Date());
		user.setStatus(1); // 0是删除，1是正常
		user.setId(IDUtils.genId());
		// 把用户数据插入数据库
		smartgymUsersMapper.insert(user);
		// 返回添加成功
		return SGResult.build(200, "注册成功");
	}

	@Override
	public SmartgymUsers userCtrToDao(SmartgymUsersCtr userCtr) {
		SmartgymUsers userDao = new SmartgymUsers();

		userDao.setPhone(userCtr.getPhone());
		userDao.setStudentno(userCtr.getPhone());
		userDao.setWxid(userCtr.getWxid());
		userDao.setUsername(userCtr.getUsername());
		switch (userCtr.getCampus()) {
		case "四平路校区":
			userDao.setCampus(0);
			break;
		case "嘉定校区":
			userDao.setCampus(1);
			break;
		case "沪西校区":
			userDao.setCampus(2);
			break;
		case "沪北校区":
			userDao.setCampus(3);
			break;
		default:
			userDao.setCampus(0);
		}
		switch (userCtr.getCollege()) {
		case "建筑与城市规划学院":
			userDao.setCollege(1);
			break;
		case "土木工程学院":
			userDao.setCollege(2);
			break;
		case "机械与能源工程学院":
			userDao.setCollege(3);
			break;
		case "经济与管理学院":
			userDao.setCollege(4);
			break;
		case "环境科学与工程学院":
			userDao.setCollege(5);
			break;
		case "材料科学与工程学院":
			userDao.setCollege(6);
			break;
		case "电子与信息工程学院":
			userDao.setCollege(7);
			break;
		case "人文学院":
			userDao.setCollege(8);
			break;
		case "外国语学院":
			userDao.setCollege(9);
			break;
		case "法学院":
			userDao.setCollege(10);
			break;
		case "马克思主义学院":
			userDao.setCollege(11);
			break;
		case "政治与国际关系学院":
			userDao.setCollege(12);
			break;
		case "理学部":
			userDao.setCollege(13);
			break;
		case "海洋与地球科学学院":
			userDao.setCollege(14);
			break;
		case "航空航天与力学学院":
			userDao.setCollege(15);
			break;
		case "数学科学学院":
			userDao.setCollege(16);
			break;
		case "物理科学与工程学院":
			userDao.setCollege(17);
			break;
		case "化学科学与工程学院":
			userDao.setCollege(18);
			break;
		case "汽车学院":
			userDao.setCollege(19);
			break;
		case "交通运输工程学院":
			userDao.setCollege(20);
			break;
		case "软件学院":
			userDao.setCollege(21);
			break;
		case "测绘与地理信息学院":
			userDao.setCollege(22);
			break;
		case "生命科学与技术学院":
			userDao.setCollege(23);
			break;
		case "医学院":
			userDao.setCollege(24);
			break;
		case "设计创意学院":
			userDao.setCollege(25);
			break;
		case "口腔医学院":
			userDao.setCollege(26);
			break;
		case "艺术与传媒学院":
			userDao.setCollege(27);
			break;
		case "体育教学部":
			userDao.setCollege(28);
			break;
		case "铁道与城市轨道交通研究院":
			userDao.setCollege(29);
			break;
		case "女子学院":
			userDao.setCollege(30);
			break;
		case "职业技术教育学院":
			userDao.setCollege(31);
			break;
		case "国际文化交流学院":
			userDao.setCollege(32);
			break;
		case "中德学院":
			userDao.setCollege(33);
			break;
		case "中法工程和管理学院":
			userDao.setCollege(34);
			break;
		case "中德工程学院":
			userDao.setCollege(35);
			break;
		case "中意学院":
			userDao.setCollege(36);
			break;
		case "环境与可持续发展学院":
			userDao.setCollege(37);
			break;
		case "中芬中心":
			userDao.setCollege(38);
			break;
		case "中西学院":
			userDao.setCollege(39);
			break;
		case "新农村发展研究院":
			userDao.setCollege(40);
			break;
		case "国际足球学院":
			userDao.setCollege(41);
			break;
		case "上海国际知识产权学院":
			userDao.setCollege(42);
			break;
		case "创新创业学院":
			userDao.setCollege(43);
			break;
		default:
			userDao.setCollege(0);
		}
		switch (userCtr.getGender()) {
		case "男":
			userDao.setGender(0);
			break;
		case "女":
			userDao.setGender(1);
			break;
		case "未填写":
			userDao.setGender(2);
			break;
		default:
			userDao.setGender(2);
		}

		return userDao;
	}

	@Override
	public SmartgymUsersCtr userDaoToCtr(SmartgymUsers userDao) {
		SmartgymUsersCtr userCtr = new SmartgymUsersCtr();
		
		userCtr.setPhone(userDao.getPhone());
		userCtr.setStudentno(userDao.getPhone());
		userCtr.setWxid(userDao.getWxid());
		userCtr.setUsername(userDao.getUsername());
		
		switch (userDao.getCampus()) {
		case 0:
			userCtr.setCampus("四平路校区");
			break;
		case 1:
			userCtr.setCampus("嘉定校区");
			break;
		case 2:
			userCtr.setCampus("沪西校区");
			break;
		case 3:
			userCtr.setCampus("沪北校区");
			break;
		default:
			userCtr.setCampus("四平路校区");
		}
		switch (userDao.getCollege()) {
		case 1:
			userCtr.setCollege("建筑与城市规划学院");
			break;
		case 2:
			userCtr.setCollege("土木工程学院");
			break;
		case 3:
			userCtr.setCollege("机械与能源工程学院");
			break;
		case 4:
			userCtr.setCollege("经济与管理学院");
			break;
		case 5:
			userCtr.setCollege("环境科学与工程学院");
			break;
		case 6:
			userCtr.setCollege("材料科学与工程学院");
			break;
		case 7:
			userCtr.setCollege("电子与信息工程学院");
			break;
		case 8:
			userCtr.setCollege("人文学院");
			break;
		case 9:
			userCtr.setCollege("外国语学院");
			break;
		case 10:
			userCtr.setCollege("法学院");
			break;
		case 11:
			userCtr.setCollege("马克思主义学院");
			break;
		case 12:
			userCtr.setCollege("政治与国际关系学院");
			break;
		case 13:
			userCtr.setCollege("理学部");
			break;
		case 14:
			userCtr.setCollege("海洋与地球科学学院");
			break;
		case 15:
			userCtr.setCollege("航空航天与力学学院");
			break;
		case 16:
			userCtr.setCollege("数学科学学院");
			break;
		case 17:
			userCtr.setCollege("物理科学与工程学院");
			break;
		case 18:
			userCtr.setCollege("化学科学与工程学院");
			break;
		case 19:
			userCtr.setCollege("汽车学院");
			break;
		case 20:
			userCtr.setCollege("交通运输工程学院");
			break;
		case 21:
			userCtr.setCollege("软件学院");
			break;
		case 22:
			userCtr.setCollege("测绘与地理信息学院");
			break;
		case 23:
			userCtr.setCollege("生命科学与技术学院");
			break;
		case 24:
			userCtr.setCollege("医学院");
			break;
		case 25:
			userCtr.setCollege("设计创意学院");
			break;
		case 26:
			userCtr.setCollege("口腔医学院");
			break;
		case 27:
			userCtr.setCollege("艺术与传媒学院");
			break;
		case 28:
			userCtr.setCollege("体育教学部");
			break;
		case 29:
			userCtr.setCollege("铁道与城市轨道交通研究院");
			break;
		case 30:
			userCtr.setCollege("女子学院");
			break;
		case 31:
			userCtr.setCollege("职业技术教育学院");
			break;
		case 32:
			userCtr.setCollege("国际文化交流学院");
			break;
		case 33:
			userCtr.setCollege("中德学院");
			break;
		case 34:
			userCtr.setCollege("中法工程和管理学院");
			break;
		case 35:
			userCtr.setCollege("中德工程学院");
			break;
		case 36:
			userCtr.setCollege("中意学院");
			break;
		case 37:
			userCtr.setCollege("环境与可持续发展学院");
			break;
		case 38:
			userCtr.setCollege("中芬中心");
			break;
		case 39:
			userCtr.setCollege("中西学院");
			break;
		case 40:
			userCtr.setCollege("新农村发展研究院");
			break;
		case 41:
			userCtr.setCollege("国际足球学院");
			break;
		case 42:
			userCtr.setCollege("上海国际知识产权学院");
			break;
		case 43:
			userCtr.setCollege("创新创业学院");
			break;
		default:
			userCtr.setCollege("未填写");
		}
		switch (userDao.getGender()) {
		case 0:
			userCtr.setGender("男");
			break;
		case 1:
			userCtr.setGender("女");
			break;
		case 2:
			userCtr.setGender("未填写");
			break;
		default:
			userCtr.setGender("未填写");
		}
		
		return userCtr;
	}

}
