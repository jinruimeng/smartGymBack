package cn.smartGym.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.smartGym.mapper.RemarkMapper;
import cn.smartGym.pojo.Remark;
import cn.smartGym.service.RemarkService;
import common.enums.ErrorCode;
import common.utils.SGResult;

@Service
public class RemarkServiceImpl implements RemarkService {

	@Autowired
	private RemarkMapper remarkMapper;
	@Override
	public SGResult addRemark(Remark remark) {
		try {
			remarkMapper.insert(remark);
			return SGResult.build(200, "添加反馈成功！");
		} catch (Exception e) {
			return SGResult.build(ErrorCode.BAD_REQUEST.getErrorCode(), "添加反馈失败！");
		}
	}


}
