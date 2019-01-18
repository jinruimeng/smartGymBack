package cn.smartGym.aop;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import cn.smartGym.annotation.ArchivesLog;
import cn.smartGym.pojo.LogInfo;
import cn.smartGym.pojoctr.request.SgUserCtr;
import cn.smartGym.service.LogInfoService;

/**
 * 操作日志类
 * 
 * @author MEIM
 *
 */

@Aspect
@Component
public class ArchivesLogAspect {

	@Autowired
	private LogInfoService loginfoService;

	@Value("${SESSION_USER}")
	private String SESSION_USER;

	private static final Logger logger = LoggerFactory.getLogger(ArchivesLog.class);

//	@Pointcut("@annotation(ArchivesLog)")
//	public void controllerAspect() {
//		// System.out.println("切入点...");
//	}

	/**
	 * 方法调用后触发 , 记录正常操作
	 * 
	 * @param joinPoint
	 * @throws ClassNotFoundException
	 */
//	@AfterReturning("controllerAspect()")
	@AfterReturning(pointcut="within(cn.smartGym.controller..*) && @annotation(archivesLog)")
	public void after(JoinPoint joinPoint, ArchivesLog archivesLog) throws ClassNotFoundException {
		// 用户微信id
		String wxId = getUSerMsg().getWxId();
		// 控制器名
		String targetName = getMethodDesc(joinPoint).getController();
		// 方法名
		String methodName = getMethodDesc(joinPoint).getMethod();
		// 操作说明
		String operteContent = getMethodDesc(joinPoint).getOperateContent();
		LogInfo logInfo = new LogInfo();
		logInfo.setUserWxId(wxId);
		logInfo.setOperateContent(operteContent);
		logInfo.setMethod(methodName);
		logInfo.setController(targetName);
		loginfoService.insertLog(logInfo);
	}

	/**
	 * 发生异常，走此方法
	 * 
	 * @param joinPoint
	 * @param e
	 */
//	@AfterThrowing(pointcut = "controllerAspect()", throwing = "e")
	@AfterThrowing(pointcut="within(cn.smartGym.controller..*) && @annotation(archivesLog)", throwing = "e")
	public void AfterThrowing(JoinPoint joinPoint, ArchivesLog archivesLog, Throwable e) {
		try {
			System.out.println("发生了异常");
			System.out.println(e);
			System.out.println("-------------");
			LogInfo logInfo = new LogInfo();
			logInfo.setExceptionMessage(e.toString());
			System.out.println(logInfo.getExceptionMessage());
			
			loginfoService.insertLog(logInfo);
			
			// 用户微信id
			String wxId = getUSerMsg().getWxId();
			// 控制器名
			String targetName = this.getMethodDesc(joinPoint).getController();
			// 方法名
			String methodName = this.getMethodDesc(joinPoint).getMethod();
			
			System.out.println(methodName);
			
			// 操作说明
			String operteContent = getMethodDesc(joinPoint).getOperateContent();
			System.out.println(operteContent);
			
			
			
			System.out.println(e + "++++++++");
			
			String exMsg = e.getCause().toString();
			System.out.println(exMsg);
			
			if (exMsg != null) {
				int type = 2;
				logInfo.setUserWxId(wxId);
				logInfo.setOperateContent(operteContent);
				logInfo.setMethod(methodName);
				logInfo.setController(targetName);
				logInfo.setType(type);
				logInfo.setExceptionMessage(exMsg);
				loginfoService.insertLog(logInfo);
			}
		} catch (Exception e1) {
//			logger.error(e1.getMessage());
			System.out.println("又出错了。。。。。");
		}
	}

	/**
     * 获取 注解中对方法的描述
     * 
     * @return
     * @throws ClassNotFoundException
     */
    public LogInfo getMethodDesc(JoinPoint joinPoint) throws ClassNotFoundException {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        String operteContent = "";
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                	// 操作说明
                    operteContent = method.getAnnotation(ArchivesLog.class).operateContent();
                    break;
                }
            }
        }
        LogInfo logInfo = new LogInfo();
        logInfo.setController(targetName);
        logInfo.setMethod(methodName);
        logInfo.setOperateContent(operteContent);
        return logInfo;
    }

	/**
	 * 得到用户信息
	 * 
	 * @return
	 */
	public SgUserCtr getUSerMsg() {
		HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		// 获取session
		HttpSession session = req.getSession();
		SgUserCtr userCtr = (SgUserCtr) session.getAttribute(SESSION_USER);
		return userCtr;
	}

}
