//package kr.or.komca.foundation.jwt.temp;
//
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.springframework.stereotype.Component;
//
//@Aspect
//@Component
//public class TempLogging {
//	private static final Logger log = LoggerFactory.getLogger(SecurityExceptionHandlerAspect.class);
//
//	@Around("within(kr.or.komca.foundation.jwt.global.exception.handler.SecurityExceptionHandler)")
//	public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
//		String methodName = joinPoint.getSignature().getName();
//
//		log.info("Started executing {} method", methodName);
//		try {
//			Object result = joinPoint.proceed();
//			log.info("Completed executing {} method", methodName);
//			return result;
//		} catch (Throwable e) {
//			log.error("Error in {} method: {}", methodName, e.getMessage());
//			throw e;
//		}
//	}private static final Logger log = LoggerFactory.getLogger(SecurityExceptionHandlerAspect.class);
//
//	@Around("within(kr.or.komca.foundation.jwt.global.exception.handler.SecurityExceptionHandler)")
//	public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
//		String methodName = joinPoint.getSignature().getName();
//
//		log.info("Started executing {} method", methodName);
//		try {
//			Object result = joinPoint.proceed();
//			log.info("Completed executing {} method", methodName);
//			return result;
//		} catch (Throwable e) {
//			log.error("Error in {} method: {}", methodName, e.getMessage());
//			throw e;
//		}
//	}private static final Logger log = LoggerFactory.getLogger(SecurityExceptionHandlerAspect.class);
//
//	@Around("within(kr.or.komca.foundation.jwt.global.exception.handler.SecurityExceptionHandler)")
//	public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
//		String methodName = joinPoint.getSignature().getName();
//
//		log.info("Started executing {} method", methodName);
//		try {
//			Object result = joinPoint.proceed();
//			log.info("Completed executing {} method", methodName);
//			return result;
//		} catch (Throwable e) {
//			log.error("Error in {} method: {}", methodName, e.getMessage());
//			throw e;
//		}
//	}private static final Logger log = LoggerFactory.getLogger(SecurityExceptionHandlerAspect.class);
//
//	@Around("within(kr.or.komca.foundation.jwt.global.exception.handler.SecurityExceptionHandler)")
//	public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
//		String methodName = joinPoint.getSignature().getName();
//
//		log.info("Started executing {} method", methodName);
//		try {
//			Object result = joinPoint.proceed();
//			log.info("Completed executing {} method", methodName);
//			return result;
//		} catch (Throwable e) {
//			log.error("Error in {} method: {}", methodName, e.getMessage());
//			throw e;
//		}
//	}
//}
