package cn.smartGym.aop;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import common.utils.SGResult;

/**
 * 操作日志类
 * 
 * @author MEIM
 *
 */

@Aspect //该标签把LoggerAspect类声明为一个切面
@Component //该标签把LoggerAspect类放到IOC容器中
public class ArchivesLogAspect {

	private static final Logger logger = LoggerFactory.getLogger(ArchivesLogAspect.class);

	/**
     * 定义一个方法，用于声明切入点表达式，方法中一般不需要添加其他代码
     * 使用@Pointcut声明切入点表达式
     * 后面的通知直接使用方法名来引用当前的切点表达式；如果是其他类使用，加上包名即可
     */
	@Pointcut("execution(* cn.smartGym.controller..*.*(..))")
	public void controllerAspect() {
		
	}

	 /**
     * 环绕通知(需要携带类型为ProceedingJoinPoint类型的参数)
     * 环绕通知包含前置、后置、返回、异常通知；ProceedingJoinPoin 类型的参数可以决定是否执行目标方法
     * 且环绕通知必须有返回值，返回值即目标方法的返回值
     * @param joinPoint
     */
	@Around("controllerAspect()")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		Object result = null;
		String MethodName = joinPoint.getSignature().getName();
		try {
			//执行目标方法
			result = joinPoint.proceed();
			return result;
		} catch (Exception e) {
			System.out.println("控制台标准输出异常");
			e.printStackTrace();
			
			System.out.println("=============================================================================");
			//自定义异常格式
			String autoErrorMsg = "[调用方法：" + MethodName + "]，[接收参数：" + Arrays.toString(joinPoint.getArgs()) + "]，发生的异常：" + e;
			System.out.println("控制台自定义输出异常");
			System.out.println(autoErrorMsg);
			
			//输出异常到日志文件，具体异常信息
	        String errorMsg = "";
	        StackTraceElement[] trace = e.getStackTrace();
	        for (StackTraceElement s : trace) {
	        	errorMsg += "\tat " + s + "\r\n";
	        }
	        logger.error("异常摘要：" + autoErrorMsg);
	        logger.error(errorMsg);
			
			return SGResult.build(500, "操作失败！", e);
		}
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//	/**
//	 * 方法调用后触发 , 记录正常操作
//	 * 
//	 * @param joinPoint
//	 * @throws ClassNotFoundException
//	 */
//	@AfterReturning(pointcut="within(cn.smartGym.controller..*) && @annotation(archivesLog)")
//	public void after(JoinPoint joinPoint, ArchivesLog archivesLog) throws ClassNotFoundException {
//		// 用户微信id
//		String wxId = getUSerMsg().getWxId();
//		// 控制器名
//		String targetName = getMethodDesc(joinPoint).getController();
//		// 方法名
//		String methodName = getMethodDesc(joinPoint).getMethod();
//		// 操作说明
//		String operteContent = getMethodDesc(joinPoint).getOperateContent();
//		
//		LogInfo logInfo = new LogInfo();
//		logInfo.setUserWxId(wxId);
//		logInfo.setOperateContent(operteContent);
//		logInfo.setMethod(methodName);
//		logInfo.setController(targetName);
//		loginfoService.insertLog(logInfo);
//	}
//
//	/**
//	 * 发生异常，走此方法
//	 * 
//	 * @param joinPoint
//	 * @param e
//	 */
//	@AfterThrowing(pointcut="within(cn.smartGym.controller..*) && @annotation(archivesLog)", throwing = "e")
//	public void AfterThrowing(JoinPoint joinPoint, ArchivesLog archivesLog, Throwable e) {
//		try {
//			// 用户微信id
//			String wxId = getUSerMsg().getWxId();
//			// 控制器名
//			String targetName = this.getMethodDesc(joinPoint).getController();
//			// 方法名
//			String methodName = this.getMethodDesc(joinPoint).getMethod();
//			// 操作说明
//			String operteContent = getMethodDesc(joinPoint).getOperateContent();
//			//异常消息
//			String exMsg = e.getCause().toString();
//			
//			LogInfo logInfo = new LogInfo();
//			if (exMsg != null) {
//				int type = 2;
//				logInfo.setUserWxId(wxId);
//				logInfo.setOperateContent(operteContent);
//				logInfo.setMethod(methodName);
//				logInfo.setController(targetName);
//				logInfo.setType(type);
//				logInfo.setExceptionMessage(exMsg);
//				loginfoService.insertLog(logInfo);
//			}
//		} catch (Exception e1) {
//			logger.error(e1.getMessage());
//		}
//	}
//
//	/**
//     * 获取 注解中对方法的描述
//     * 
//     * @return
//     * @throws ClassNotFoundException
//     */
//    public LogInfo getMethodDesc(JoinPoint joinPoint) throws ClassNotFoundException {
//        String targetName = joinPoint.getTarget().getClass().getName();
//        String methodName = joinPoint.getSignature().getName();
//        Object[] arguments = joinPoint.getArgs();
//        Class targetClass = Class.forName(targetName);
//        Method[] methods = targetClass.getMethods();
//        String operteContent = "";
//        for (Method method : methods) {
//            if (method.getName().equals(methodName)) {
//                Class[] clazzs = method.getParameterTypes();
//                if (clazzs.length == arguments.length) {
//                	// 操作说明
//                    operteContent = method.getAnnotation(ArchivesLog.class).operateContent();
//                    break;
//                }
//            }
//        }
//        LogInfo logInfo = new LogInfo();
//        logInfo.setController(targetName);
//        logInfo.setMethod(methodName);
//        logInfo.setOperateContent(operteContent);
//        return logInfo;
//    }
//
//	/**
//	 * 得到用户信息
//	 * 
//	 * @return
//	 */
//	public SgUserCtr getUSerMsg() {
//		HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//		// 获取session
//		HttpSession session = req.getSession();
//		SgUserCtr userCtr = (SgUserCtr) session.getAttribute(SESSION_USER);
//		return userCtr;
//	}

}
