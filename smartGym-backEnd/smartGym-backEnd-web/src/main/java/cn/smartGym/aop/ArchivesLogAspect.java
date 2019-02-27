package cn.smartGym.aop;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.gson.Gson;

import cn.smartGym.pojo.SgUser;
import cn.smartGym.service.UserService;
import common.enums.ErrorCode;
import common.utils.LogAopUtils;
import common.utils.SGResult;

/**
 * 操作日志类
 * 
 * @author MEIM
 *
 */

@Aspect // 该标签把LoggerAspect类声明为一个切面
@Component // 该标签把LoggerAspect类放到IOC容器中
public class ArchivesLogAspect {

	private static final Logger logger = LoggerFactory.getLogger(ArchivesLogAspect.class);

	@Autowired
	UserService userService;
//	@Autowired
//	private JedisClient jedisClient;
//	@Value("${SESSION_EXPIRE}")
//	private Integer SESSION_EXPIRE;

	private String userWxId = null; // 请求者微信号
	private String userName = null; // 请求者
	private String studentNo = null; // 请求者学号
	private String requestPath = null; // 请求地址
	private String requestMethod = null; // 请求方式
	private Map<?, ?> inputParamMap = null; // 传入参数
	private Map<String, Object> outputParamMap = null; // 存放输出结果
	private long startTimeMillis = 0; // 开始时间
	private long endTimeMillis = 0; // 结束时间
	private long errorTimeMillis = 0; // 结束时间
	private StringBuffer sb = null;

	/**
	 * 定义一个方法，用于声明切入点表达式，方法中一般不需要添加其他代码 使用@Pointcut声明切入点表达式
	 * 后面的通知直接使用方法名来引用当前的切点表达式；如果是其他类使用，加上包名即可
	 */
	@Pointcut("execution(* cn.smartGym.controller..*.*(..))")
	public void controllerAspect() {

	}

	/**
	 * 
	 * @Title：doBeforeInServiceLayer
	 * @Description: 方法调用前触发 记录开始时间
	 * @param joinPoint
	 * @throws Throwable
	 */
	@Before("controllerAspect()")
	public void doBeforeInServiceLayer(JoinPoint joinPoint) throws Exception {
		RequestAttributes ra = RequestContextHolder.getRequestAttributes();
		ServletRequestAttributes sra = (ServletRequestAttributes) ra;
		HttpServletRequest request = sra.getRequest();
		requestPath = request.getRequestURL().toString();
		requestMethod = request.getMethod();
		startTimeMillis = System.currentTimeMillis(); // 记录方法开始执行的时间
		inputParamMap = request.getParameterMap();

		if (inputParamMap.containsKey("wxId"))
			userWxId = ((String[]) inputParamMap.get("wxId"))[0];
		else if (inputParamMap.containsKey("userWxId"))
			userWxId = ((String[]) inputParamMap.get("userWxId"))[0];

		if (!StringUtils.isBlank(userWxId)) {
			SgUser sgUser = new SgUser();
			sgUser.setWxId(userWxId);
			SGResult result = userService.getUserByDtail(sgUser);
			if (result.isOK()) {
				SgUser reUser = (SgUser) result.getData();
				if (reUser != null) {
					userName = reUser.getName();
					studentNo = reUser.getStudentNo();
				}
			}
/*			// 先去redis中查看是否有用户的信息，如果没有，则去数据库中查看
			String sgUserSignInString = jedisClient.get(userWxId);
			if (!StringUtils.isBlank(sgUserSignInString)) {
				sgUser = JsonUtils.jsonToPojo(sgUserSignInString, SgUser.class);
				userName = sgUser.getName();
				studentNo = sgUser.getStudentNo();
			} else {
				// 如果缓存中无信息，去数据库中查找用户信息
				sgUser.setWxId(userWxId);
				sgUser = (SgUser) userService.getUserByDtail(sgUser).getData();
				if (sgUser != null) {
					userName = sgUser.getName();
					studentNo = sgUser.getStudentNo();
					// 把用户信息写入redis，key：wxId value：用户信息
					jedisClient.set("wxId:" + userWxId, JsonUtils.objectToJson(sgUser));
					// 设置Session的过期时间
					jedisClient.expire("wxId:" + userWxId, SESSION_EXPIRE);
				}
			}*/
		}
		// 打印请求内容
		logger.info("====================请求内容开始====================");
		logger.info("请求者微信号:" + userWxId);
		logger.info("请求者姓名:" + userName);
		logger.info("请求者学号:" + studentNo);
		logger.info("请求地址:" + requestPath);
		logger.info("请求方式:" + requestMethod);
		logger.info("请求参数:" + new Gson().toJson(inputParamMap));
		logger.info("请求类方法:" + joinPoint.getSignature());
		logger.info("请求类方法参数:" + Arrays.toString(joinPoint.getArgs()));

		Object[] args = joinPoint.getArgs();
		String classType = joinPoint.getTarget().getClass().getName();
		Class<?> clazz = Class.forName(classType);
		String clazzName = clazz.getName();
		String methodName = joinPoint.getSignature().getName(); // 获取方法名称
		// 获取参数名称和值
		sb = LogAopUtils.getNameAndArgs(this.getClass(), clazzName, methodName, args);
		logger.info("请求类方法参数名称和值：" + sb);
		logger.info("====================请求内容结束====================");
	}

	/**
	 * 环绕通知(需要携带类型为ProceedingJoinPoint类型的参数) 环绕通知包含前置、后置、返回、异常通知；ProceedingJoinPoin
	 * 类型的参数可以决定是否执行目标方法 且环绕通知必须有返回值，返回值即目标方法的返回值
	 * 
	 * @param joinPoint
	 */
	@Around("controllerAspect()")
	public Object around(ProceedingJoinPoint joinPoint) throws Exception {
		// 执行目标方法
		try {
			Object result = joinPoint.proceed();
			outputParamMap = new HashMap<String, Object>();
			outputParamMap.put("result", result);
			logger.info("====================返回内容开始====================");
			logger.info("方法执行结果:" + new Gson().toJson(outputParamMap));
			endTimeMillis = System.currentTimeMillis(); // 记录方法执行完成的时间
			logger.info("方法执行所用时间:" + (endTimeMillis - startTimeMillis) + "ms");
			logger.info("====================返回内容结束====================");
			return result;
		} catch (Throwable e) {
			errorTimeMillis = System.currentTimeMillis(); // 记录方法发生异常的时间
			logger.error("====================异常内容开始====================");
			logger.error("方法开始执行时间:" + startTimeMillis);
			logger.error("请求者微信号:" + userWxId);
			logger.error("请求者姓名:" + userName);
			logger.error("请求者学号:" + studentNo);
			logger.error("请求地址:" + requestPath);
			logger.error("请求方式:" + requestMethod);
			logger.error("请求参数:" + new Gson().toJson(inputParamMap));
			logger.error("请求类方法:" + joinPoint.getSignature());
			logger.error("请求类方法参数:" + Arrays.toString(joinPoint.getArgs()));
			logger.error("请求类方法参数名称和值：" + sb);
			logger.error("方法发生异常时间:" + errorTimeMillis);
			logger.error("方法发生的异常：" + e);
			// 输出异常到日志文件，具体异常信息
			String errorMsg = "";
			StackTraceElement[] trace = e.getStackTrace();
			for (StackTraceElement s : trace) {
				errorMsg += "\tat " + s + "\r\n";
			}
			logger.error(errorMsg);
			logger.error("====================异常内容结束====================");

			System.out.println("====================标准异常输出开始====================");
			e.printStackTrace();
			System.out.println("====================标准异常输出结束====================");
			// 自定义异常格式
			String autoErrorMsg = "[调用方法：" + joinPoint.getSignature() + "]，[接收参数："
					+ Arrays.toString(joinPoint.getArgs()) + "]，发生的异常：" + e;
			System.out.println("====================自定义异常输出开始====================");
			System.out.println(autoErrorMsg);
			System.out.println("====================自定义异常输出结束====================");

			return SGResult.build(ErrorCode.SYSTEM_EXCEPTION.getErrorCode(), "操作失败！", e);
		}
	}

	@After("controllerAspect()")
	public void after(JoinPoint joinPoint) throws Exception {
		// 重置各参数
		userWxId = null; // 请求者微信号
		userName = null; // 请求者
		studentNo = null; // 请求者学号
		requestPath = null; // 请求地址
		requestMethod = null; // 请求方式
		inputParamMap = null;// 传入参数
		outputParamMap = null; // 存放输出结果
		startTimeMillis = 0; // 开始时间
		endTimeMillis = 0; // 结束时间
		errorTimeMillis = 0; // 结束时间
		sb = null;
	}
}
