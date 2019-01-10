package cn.smartGym.service;

import cn.smartGym.pojo.SmartgymInform;
import common.utils.SGResult;

/**
 * 通知/活动消息服务层
 * @author Ruimeng Jin
 *
 */
public interface InformService {

	SGResult addInform(SmartgymInform smartgymInform);
	
	SGResult updateInform(SmartgymInform smartgymInform);
	
	SGResult deleteInformById(Long id);
	
	SGResult getInformList(Integer type);
	
	SGResult getInformById(Long id);
}
