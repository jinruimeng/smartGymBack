package cn.smartGym.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.smartGym.pojo.SmartgymUsers;
import cn.smartGym.pojoCtr.SmartgymUsersCtr;
import cn.smartGym.service.UserService;
import common.utils.AesCbcUtil;
import common.utils.HttpRequest;
import common.utils.SGResult;
import net.sf.json.JSONObject;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/user/register", method = { RequestMethod.POST,
			RequestMethod.GET }, consumes = "application/x-www-form-urlencoded")
	@ResponseBody
	public SGResult register(SmartgymUsersCtr userCtr) {
		//获取用户敏感数据
		/**
	     * 解密用户敏感数据
	     *
	     * @param encryptedData 明文,加密数据
	     * @param iv            加密算法的初始向量
	     * @param code          用户允许登录后，回调内容会带上 code（有效期五分钟），开发者需要将 code 发送到开发者服务器后台，使用code 换取 session_key api，将 code 换成 openid 和 session_key
	     * @return
	     */
//		String encryptedData = userCtr.getEncryptedData();
//		String code = userCtr.getCode();
//		String iv = userCtr.getIv();
//		
//
//        //登录凭证不能为空
//        if (code == null || code.length() == 0) {
//        	return SGResult.build(402, "code不能为空");
//        }
//		
//        //小程序唯一标识   (在微信小程序管理后台获取)
//        String wxspAppid = "xxxxxxxxxxxxxx";
//        //小程序的 app secret (在微信小程序管理后台获取)
//        String wxspSecret = "xxxxxxxxxxxxxx";
//        //授权（必填）
//        String grant_type = "authorization_code";
// 
// 
//        //////////////// 1、向微信服务器 使用登录凭证 code 获取 session_key 和 openid ////////////////
//        //请求参数
//        String params = "appid=" + wxspAppid + "&secret=" + wxspSecret + "&js_code=" + code + "&grant_type=" + grant_type;
//        //发送请求
//        String sr = HttpRequest.sendGet("https://api.weixin.qq.com/sns/jscode2session", params);
//        //解析相应内容（转换成json对象）
//        JSONObject json = JSONObject.fromObject(sr);
//        //获取会话密钥（session_key）
//        String session_key = json.get("session_key").toString();
//        //用户的唯一标识（openid）
////        String openid = (String) json.get("openid");
// 
//        //////////////// 2、对encryptedData加密数据进行AES解密 ////////////////
//        try {
//            String result = AesCbcUtil.decrypt(encryptedData, session_key, iv, "UTF-8");
//            if (null != result && result.length() > 0) {
//            	//解密成功
//                JSONObject userInfoJSON = JSONObject.fromObject(result);
///*                Map userInfo = new HashMap();
//                userInfo.put("openId", userInfoJSON.get("openId"));
//                userInfo.put("nickName", userInfoJSON.get("nickName"));
//                userInfo.put("gender", userInfoJSON.get("gender"));
//                userInfo.put("city", userInfoJSON.get("city"));
//                userInfo.put("province", userInfoJSON.get("province"));
//                userInfo.put("country", userInfoJSON.get("country"));
//                userInfo.put("avatarUrl", userInfoJSON.get("avatarUrl"));
//                userInfo.put("unionId", userInfoJSON.get("unionId"));*/
//                userCtr.setWxid((String)userInfoJSON.get("unionId"));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return SGResult.build(403, "用户信息解密失败");
//        }
		SmartgymUsers userDao = userService.userCtrToDao(userCtr);		
		SGResult sgResult = userService.register(userDao);
		return sgResult;
	}

}
