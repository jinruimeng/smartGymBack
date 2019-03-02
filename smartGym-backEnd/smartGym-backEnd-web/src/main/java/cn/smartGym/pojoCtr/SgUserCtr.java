package cn.smartGym.pojoCtr;

import java.io.Serializable;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;

import common.enums.ErrorCode;
import common.utils.AesCbcUtil;
import common.utils.HttpRequest;
import common.utils.SGResult;
import net.sf.json.JSONObject;

public class SgUserCtr implements Serializable {

	private static final long serialVersionUID = 2741694336785053911L;

	private Long id;

	private String name;

	private String encryptedData;

	private String iv;

	private String code;

	private String wxId;

	private String studentNo;

	private String gender;

	private String campus;

	private String college;

	private String phone;

	private Integer status;

	private Integer authority;

	public String getEncryptedData() {
		return encryptedData;
	}

	public void setEncryptedData(String encryptedData) {
		this.encryptedData = encryptedData;
	}

	public String getIv() {
		return iv;
	}

	public void setIv(String iv) {
		this.iv = iv;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getCampus() {
		return campus;
	}

	public void setCampus(String campus) {
		this.campus = campus;
	}

	public String getCollege() {
		return college;
	}

	public void setCollege(String college) {
		this.college = college;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getAuthority() {
		return authority;
	}

	public void setAuthority(Integer authority) {
		this.authority = authority;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWxId() {
		return wxId;
	}

	public void setWxId(String wxId) {
		this.wxId = wxId;
	}

	public String getStudentNo() {
		return studentNo;
	}

	public void setStudentNo(String studentNo) {
		this.studentNo = studentNo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public SgUserCtr() {
		super();
	}

	/**
	 * 解密用户敏感数据
	 *
	 * @param encryptedData 明文,加密数据
	 * @param iv            加密算法的初始向量
	 * @param code          用户允许登录后，回调内容会带上 code（有效期五分钟），开发者需要将 code
	 *                      发送到开发者服务器后台，使用code 换取 session_key api，将 code 换成 openid 和
	 *                      session_key
	 * @return
	 * @throws Exception
	 */
	public SGResult decodeWxId() throws Exception {
		String encryptedData = this.getEncryptedData();
		String code = this.getCode();
		String iv = this.getIv();
		// 登录凭证不能为空
		if (code == null || code.length() == 0) {
			return SGResult.build(ErrorCode.BAD_REQUEST.getErrorCode(), "code不能为空");
		}

		// 从配置文件中读取小程序唯一标识
		ResourceBundle rs = ResourceBundle.getBundle("conf/resource");
		String wxspAppid = rs.getString("wxspAppid");
		String wxspSecret = rs.getString("wxspSecret");
		String grant_type = rs.getString("grant_type");

		/*
		 * 1、向微信服务器 使用登录凭证 code 获取 session_key 和 openid
		 * 
		 */
		// 请求参数
		String params = "appid=" + wxspAppid + "&secret=" + wxspSecret + "&js_code=" + code + "&grant_type="
				+ grant_type;
		// 发送请求
		String sr = HttpRequest.sendGet("https://api.weixin.qq.com/sns/jscode2session", params);

		// 解析相应内容（转换成json对象）
		JSONObject json = JSONObject.fromObject(sr);

		if (json == null || !json.containsKey("session_key"))
			return SGResult.build(ErrorCode.BUSINESS_EXCEPTION.getErrorCode(), "用户信息解密失败！" + json);

		// 获取会话密钥（session_key）
		String session_key = json.get("session_key").toString();
		// 用户的唯一标识（openId）
		// String openId = (String) json.get("openid");
		// System.out.println("openId: " + openId);

		/*
		 * 2、对encryptedData加密数据进行AES解密
		 * 
		 */
		String result = AesCbcUtil.decrypt(encryptedData, session_key, iv, "UTF-8");
		if (StringUtils.isNotBlank(result)) {
			// 解密成功
			JSONObject userInfoJSON = JSONObject.fromObject(result);

			/*
			 * Map userInfo = new HashMap(); userInfo.put("openId",
			 * userInfoJSON.get("openId")); userInfo.put("nickName",
			 * userInfoJSON.get("nickName")); userInfo.put("gender",
			 * userInfoJSON.get("gender")); userInfo.put("city", userInfoJSON.get("city"));
			 * userInfo.put("province", userInfoJSON.get("province"));
			 * userInfo.put("country", userInfoJSON.get("country"));
			 * userInfo.put("avatarUrl", userInfoJSON.get("avatarUrl"));
			 * userInfo.put("unionId", userInfoJSON.get("unionId"));
			 */

			if (userInfoJSON.containsKey("unionId"))
				return SGResult.ok(userInfoJSON.get("unionId"));
			else
				return SGResult.build(ErrorCode.BUSINESS_EXCEPTION.getErrorCode(), "用户信息解密失败！" + userInfoJSON);
		} else
			return SGResult.build(ErrorCode.BUSINESS_EXCEPTION.getErrorCode(), "用户信息解密失败！");
	}

}