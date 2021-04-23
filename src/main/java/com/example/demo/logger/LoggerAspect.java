package com.example.demo.logger;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
//@Component
//@Aspect
public class LoggerAspect {
	
	/*****************************************************************
	 * 
	 * [POINTCUT]
	 * 		"execution(* com..controller.*Controller.*(..))"
	 * 
	 * 		* 						: 모든 리턴 타입
	 * 		com..controller 		: 패키지 시작이 com, 끝이 contoller
	 * 		*Controller 			: Contoller로 끝나는 모든 클래스
	 * 		* 						: 모든 메서드
	 * 		(..) 					: 모든 파라미터
	 * 
	 * [주의] ProceedingJoinPoint is only supported for @Around advice
	 * 
	 */
	@Around("execution(* com..controller.*Controller.*(..)) or"
			+ "execution(* com..service.*Service.*(..)) or"
			+ "execution(* com..mapper.*Mapper.*(..))")
	public Object logPrint(ProceedingJoinPoint joinPoint) throws Throwable{
		String type="";
		String name = joinPoint.getSignature().getDeclaringTypeName();
		if(name.indexOf("Controller") > -1) {
			type = "@@@@@ [Controller] ";
		}else if(name.indexOf("Service") > -1) {
			type = "@@@@@ [Service] ";
		}else if(name.indexOf("Mapper") > -1) {
			type = "@@@@@ [Mapper] ";
		}
		log.debug(type+name+"."+joinPoint.getSignature().getName()+"()");
		return joinPoint.proceed();
	}

}

