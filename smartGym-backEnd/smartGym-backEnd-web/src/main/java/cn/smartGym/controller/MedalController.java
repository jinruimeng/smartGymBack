package cn.smartGym.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.smartGym.pojo.Medal;
import cn.smartGym.pojoCtr.MedalCtr;
import cn.smartGym.service.MedalService;
import cn.smartGym.utils.ConversionUtils;
import common.utils.SGResult;

@Controller
public class MedalController {
	
    @Autowired
    private MedalService medalService;
    
    @RequestMapping(value = "/smartgym/medal/getMedal", method = {RequestMethod.POST,
            RequestMethod.GET}, consumes = "application/x-www-form-urlencoded;charset=utf-8")
    @ResponseBody
    public SGResult getMedal(MedalCtr medalCtr) throws Exception {
    	List<Medal> medals = medalService.getMedalsByDetails(ConversionUtils.medalCtrToDao(medalCtr));
        return SGResult.ok("查询成功！", ConversionUtils.medalDaoListToCtrList(medals));
    }
}
