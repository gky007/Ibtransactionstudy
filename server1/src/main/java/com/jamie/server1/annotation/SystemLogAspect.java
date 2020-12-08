package com.jamie.server1.annotation;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSON;
import com.jamie.server1.entity.SystemLog;
import com.jamie.server1.service.SystemLogService;
import javassist.*;
import javassist.bytecode.AttributeInfo;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


/**
 * @author jamie
 * @version 1.0.0
 * @date 2020/12/8 16:54
 * @description 切点类
 */
@Aspect
@Component
public class SystemLogAspect {

    //注入Service用于把日志保存数据库
    @Resource  //这里我用resource注解，一般用的是@Autowired，他们的区别如有时间我会在后面的博客中来写
    private SystemLogService systemLogService;

    private static final Logger logger = LoggerFactory.getLogger(SystemLogAspect.class);

    private Object result;

    //Controller层切点
    @Pointcut("execution (* com.jamie.server1.controller.*.*(..))")
    public void controllerAspect() {
    }

    /**
     * 前置通知 用于拦截Controller层记录用户的操作
     *
     * @param joinPoint 切点
     */
    @Before("controllerAspect()")
    public void doBefore(JoinPoint joinPoint) {
        System.out.println("==========执行controller前置通知===============");
        if (logger.isInfoEnabled()) {
            logger.info("before " + joinPoint);
        }
    }

    //配置controller环绕通知,使用在方法aspect()上注册的切入点
    @Around("controllerAspect()")
    public Object doAround(JoinPoint joinPoint) {
        System.out.println("==========开始执行controller环绕通知===============");
        long start = System.currentTimeMillis();
        Object proceed = null;
        try {
            proceed = ((ProceedingJoinPoint) joinPoint).proceed();
            long end = System.currentTimeMillis();
            if (logger.isInfoEnabled()) {
                logger.info("around " + joinPoint + "\tUse time : " + (end - start) + " ms!");
            }
            System.out.println("==========结束执行controller环绕通知===============");
        } catch (Throwable e) {
            long end = System.currentTimeMillis();
            if (logger.isInfoEnabled()) {
                logger.info("around " + joinPoint + "\tUse time : " + (end - start) + " ms with exception : " + e.getMessage());
            }
        }
        return this.result = proceed;
    }

    /**
     * 后置通知 用于拦截Controller层记录用户的操作
     *
     * @param joinPoint 切点
     */
    @After("controllerAspect()")
    public void after(JoinPoint joinPoint) {

       /* HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();  */
        //读取session中的用户
        // User user = (User) session.getAttribute("user");
        //请求的IP
//        String ip = request.getRemoteAddr();
//        User user = new User();
//        user.setId(1);
//        user.setName("张三");
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        //IP地址
        String ipAddr = getRemoteHost(request);
        try {

            String targetName = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            Object[] arguments = joinPoint.getArgs();
            Class targetClass = Class.forName(targetName);
            Method[] methods = targetClass.getMethods();
            String operationType = "";
            String operationName = "";
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    Class[] clazzs = method.getParameterTypes();
                    if (clazzs.length == arguments.length) {
                        operationType = method.getAnnotation(GkyLog.class).operationType();
                        operationName = method.getAnnotation(GkyLog.class).operationName();
                        break;
                    }
                }
            }
            String params = JSON.toJSONString(getFieldsName(this.getClass(), targetName, methodName, arguments));
            //*========控制台输出=========*//
            System.out.println("=====controller后置通知开始=====");
            System.out.println("请求方法:" + (targetName + "." + methodName + "()"));
            System.out.println("操作类型:" + operationType);
            System.out.println("方法描述:" + operationName);
//            System.out.println("请求人:" + user.getName());
            System.out.println("请求人:" + "localhost");
            System.out.println("请求IP:" + ipAddr);
            System.out.println("请求参数:" + params);
            //*========数据库日志=========*//
            SystemLog log = new SystemLog();
            log.setId(UUID.randomUUID().toString().replace("-", ""));
            log.setDescription(operationName);
            log.setOperationType(operationType);
            log.setMethod(targetName + "." + methodName + "()");
            log.setLogType((long) 0);
            log.setRequestIp(ipAddr);
//            log.setRequestIp(ip);
            log.setExceptionCode(null);
            log.setExceptionDetail(null);
            log.setParams(params);
            log.setResultMap(JSON.toJSONString(result));
//            log.setCreateBy(user.getName());
            log.setCreateBy("Test007");
            log.setCreateDate(new Date());
            //保存数据库
            systemLogService.insert(log);
            System.out.println("=====controller后置通知结束=====");
        } catch (Exception e) {
            //记录本地异常日志
            logger.error("==后置通知异常==");
            logger.error("异常信息:{}", e.getMessage());
        }
    }

    //配置后置返回通知,使用在方法aspect()上注册的切入点
    @AfterReturning("controllerAspect()")
    public void afterReturn(JoinPoint joinPoint) {
        System.out.println("=====执行controller后置返回通知=====");
        if (logger.isInfoEnabled()) {
            logger.info("afterReturn " + joinPoint);
        }
    }

    /**
     * 异常通知 用于拦截记录异常日志
     *
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(pointcut = "controllerAspect()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e) throws NotFoundException {
        /*HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        //读取session中的用户
        User user = (User) session.getAttribute(WebConstants.CURRENT_USER);
        //获取请求ip
        String ip = request.getRemoteAddr(); */
        //获取用户请求方法的参数并序列化为JSON格式字符串

//        User user = new User();
//        user.setId(1);
//        user.setName("张三");
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        //IP地址
        String ipAddr = getRemoteHost(request);

//        String params = "";
//        if (joinPoint.getArgs() != null && joinPoint.getArgs().length > 0) {
//            for (int i = 0; i < joinPoint.getArgs().length; i++) {
//                params += JSON.toJSONString(joinPoint.getArgs()[i]) + ";";
//            }
//        }
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        String params = JSON.toJSONString(getFieldsName(this.getClass(), targetName, methodName, arguments));
        try {
            Class targetClass = Class.forName(targetName);
            Method[] methods = targetClass.getMethods();
            String operationType = "";
            String operationName = "";
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    Class[] clazzs = method.getParameterTypes();
                    if (clazzs.length == arguments.length) {
                        operationType = method.getAnnotation(GkyLog.class).operationType();
                        operationName = method.getAnnotation(GkyLog.class).operationName();
                        break;
                    }
                }
            }
            /*========控制台输出=========*/
            System.out.println("=====异常通知开始=====");
            System.out.println("异常代码:" + e.getClass().getName());
            System.out.println("异常信息:" + e.getMessage());
            System.out.println("异常方法:" + (targetName + "." + methodName + "()"));
            System.out.println("操作类型:" + operationType);
            System.out.println("方法描述:" + operationName);
//            System.out.println("请求人:" + user.getName());
            System.out.println("请求人:" + "localhost");
            System.out.println("请求IP:" + ipAddr);
            System.out.println("请求参数:" + params);
            /*==========数据库日志=========*/
            SystemLog log = new SystemLog();
            log.setId(UUID.randomUUID().toString().replace("-", ""));
            log.setDescription(operationName);
            log.setOperationType(operationType);
            log.setExceptionCode(e.getClass().getName());
            log.setLogType((long) 1);
            log.setExceptionDetail(e.getMessage());
            log.setMethod(targetName + "." + methodName + "()");
            log.setParams(params);
            log.setResultMap(JSON.toJSONString(result));
//            log.setCreateBy(user.getName());
            log.setCreateBy("Test007");
            log.setCreateDate(new Date());
            log.setRequestIp(ipAddr);
            //保存数据库
            systemLogService.insert(log);
            System.out.println("=====异常通知结束=====");
        } catch (Exception ex) {
            //记录本地异常日志
            logger.error("==异常通知异常==");
            logger.error("异常信息:{}", ex.getMessage());
        }
        /*==========记录本地异常日志==========*/
        logger.error("异常方法:{}异常代码:{}异常信息:{}参数:{}", joinPoint.getTarget().getClass().getName() + joinPoint.getSignature().getName(), e.getClass().getName(), e.getMessage(), params);
    }

    /**
     * 获取接口入参变量及变量名
     * @param aClass aClass
     * @param targetName targetName
     * @param methodName methodName
     * @param arguments arguments
     * @throws NotFoundException NotFoundException
     * @return Map Map
     */
    private Map<String, Object> getFieldsName(Class<? extends SystemLogAspect> aClass, String targetName, String methodName, Object[] arguments) throws NotFoundException {
        Map<String, Object> map = new HashMap<String, Object>();
        ClassPool pool = ClassPool.getDefault();
        ClassClassPath classPath = new ClassClassPath(aClass);
        pool.insertClassPath(classPath);
        CtClass cc = pool.get(targetName);
        CtMethod cm = cc.getDeclaredMethod(methodName);
        MethodInfo methodInfo = cm.getMethodInfo();
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
        LocalVariableAttribute attribute = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
        if (attribute == null) {
            throw new IllegalArgumentException();
        }
        int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
        for (int i = 0; i < cm.getParameterTypes().length; i++) {
            map.put(attribute.variableName(i + pos), arguments[i]);
        }
        return map;
    }

    /**
     * 获取目标主机的ip
     * @param request
     * @return
     */
    private String getRemoteHost(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }
}