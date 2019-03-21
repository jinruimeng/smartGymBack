package cn.smartGym.service.impl;

import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.smartGym.mapper.RemarkMapper;
import cn.smartGym.pojo.Remark;
import cn.smartGym.service.RemarkService;
import common.enums.ErrorCode;
import common.utils.IDUtils;
import common.utils.SGResult;

@Service
public class RemarkServiceImpl implements RemarkService {

	@Autowired
	private RemarkMapper remarkMapper;

	private HashMap<String, Integer> time = new HashMap<String, Integer>();
	private HashMap<String, Date> lastDates = new HashMap<String, Date>();

	@Override
	public SGResult addRemark(Remark remark) {
		String wxId = remark.getWxId();
		Date now = new Date();
		if (wxId == null)
			return SGResult.build(ErrorCode.BAD_REQUEST.getErrorCode(), "不能匿名反馈！");

		// 判断该用户上次添加反馈时间，一天之内不能反馈超过三次
		if (time.get(wxId) != null) {
			Date lastDate = (Date) lastDates.get(wxId);
			long between = now.getTime() - lastDate.getTime();
			if (between > (24 * 3600000)) {
				time.put(wxId, 0);
			} else if (time.get(wxId) >= 3)
				return SGResult.build(ErrorCode.BAD_REQUEST.getErrorCode(), "一天内不能反馈超过3次！");
		}

		remark.setId(IDUtils.genId());
		remark.setCreated(now);
		remark.setUpdated(now);
		remarkMapper.insert(remark);

		if (time.get(wxId) == null || time.get(wxId) == 0)
			time.put(wxId, 1);
		else
			time.put(wxId, time.get(wxId) + 1);
		lastDates.put(wxId, now);

		return SGResult.ok("添加反馈成功！");
	}

}
