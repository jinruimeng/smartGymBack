package cn.smartGym.service;

import cn.smartGym.pojo.Information;
import common.utils.SGResult;

/**
 * 通知/活动消息服务层
 * 
 * @author Ruimeng Jin
 *
 */
public interface InformService {

	void addInform(Information Information);

	void hardDeleteInformation();

	SGResult deleteInformById(Long... ids);

	void updateInform(Information Information);

	SGResult getInformList(Integer type);
	
	SGResult getInformById(Long id);
}
