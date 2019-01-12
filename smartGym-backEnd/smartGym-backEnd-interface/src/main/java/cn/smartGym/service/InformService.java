package cn.smartGym.service;

import cn.smartGym.pojo.Information;
import common.utils.SGResult;

/**
 * 通知/活动消息服务层
 * @author Ruimeng Jin
 *
 */
public interface InformService {

	SGResult addInform(Information Information);
	
	SGResult updateInform(Information Information);
	
	SGResult deleteInformById(Long id);
	
	SGResult getInformList(Integer type);
	
	SGResult getInformById(Long id);
}
