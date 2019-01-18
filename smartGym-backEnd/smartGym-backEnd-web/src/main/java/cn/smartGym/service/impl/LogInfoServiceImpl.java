package cn.smartGym.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.smartGym.mapper.LogInfoMapper;
import cn.smartGym.pojo.LogInfo;
import cn.smartGym.service.LogInfoService;

@Service
public class LogInfoServiceImpl implements LogInfoService {

	@Autowired
	private LogInfoMapper logInfoMapper;
	
	@Override
	public void insertLog(LogInfo logInfo) {
		logInfoMapper.insert(logInfo);
	}

}
