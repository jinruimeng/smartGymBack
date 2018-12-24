package cn.smartGym.service;

import cn.smartGym.pojo.SmartgymUsers;
import common.utils.SGResult;

public interface UserService {
	SGResult checkData(String param, int type);
	SGResult register(SmartgymUsers user);
}
