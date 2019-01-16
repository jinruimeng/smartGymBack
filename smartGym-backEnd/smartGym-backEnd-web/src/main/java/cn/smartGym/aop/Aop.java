package cn.smartGym.aop;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import common.utils.SGResult;

@Aspect
@Component
public class Aop {

	@Pointcut("execution(* cn.smartGym.controller..*.*(..))")
	public void controller() {
	}

	@Around("controller()")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		String MethodName = joinPoint.getSignature().getName();
		Object result = null;
		try {
			result = joinPoint.proceed();
			return result;
		} catch (Exception e) {
			String exception = "调用的方法：" + MethodName + "，传递参数：" + Arrays.toString(joinPoint.getArgs()) + "，方法发生异常：" + e;
			e.printStackTrace();
			System.out.println(exception);
			return SGResult.build(500, "操作失败！", e);
		}
	}
}
