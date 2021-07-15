package jsz.dk.signmanagement.aop;

import com.alibaba.fastjson.JSON;
import jsz.dk.signmanagement.common.annotations.OperationLogDetail;
import jsz.dk.signmanagement.common.entity.CustomException;
import jsz.dk.signmanagement.common.entity.OperationLog;
import jsz.dk.signmanagement.utils.IPUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @ProjectName: sign-management
 * @Package: jsz.dk.signmanagement.aop
 * @ClassName: LogAspect
 * @Author: Strawberry
 * @Description:
 * @Date: 2021/07/12 16:11
 */
@Aspect
@Component
@Slf4j
public class LogAspect {

    @Pointcut("@annotation(jsz.dk.signmanagement.common.annotations.OperationLogDetail)")
    public void operationLog(){}

    @Around("operationLog()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable{
        Object res = null;
        long time = System.currentTimeMillis();
        try {
            res = joinPoint.proceed();
            time = System.currentTimeMillis() - time;
            return res;
        }finally {
            try{
                addOperationLog(joinPoint, res, time);
            }catch (Exception e){
                log.warn("LogAspect 操作失败：{}",e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void addOperationLog(JoinPoint joinPoint, Object res, long time){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        OperationLog operationLog = new OperationLog();
        operationLog.setRunTime(time);
        operationLog.setReturnValue(JSON.toJSONString(res));
        operationLog.setId(UUID.randomUUID().toString());
        operationLog.setArgs(JSON.toJSONString(joinPoint.getArgs()));
        operationLog.setCreateTime(new Date());
        operationLog.setMethod(signature.getDeclaringTypeName() + "." + signature.getName());
        operationLog.setUserId("#{currentUserId}");
        operationLog.setUserName("#{currentUserName}");
        OperationLogDetail annotation = signature.getMethod().getAnnotation(OperationLogDetail.class);
        if (annotation != null){
            operationLog.setLevel(annotation.level());
            operationLog.setDescribe(getDetail(((MethodSignature) joinPoint.getSignature()).getParameterNames(), joinPoint.getArgs(), annotation));
            operationLog.setOperationType(annotation.operationType().getValue());
            operationLog.setOperationUnit(annotation.operationUnit().getValue());
        }
        log.info("记录日志：{}",operationLog.toString());
    }

    private String getDetail(String[] argName, Object[] args, OperationLogDetail annotation){
        Map<Object, Object> map = new HashMap<>(4);
        for (int i = 0; i < argName.length; i++){
            map.put(argName[i], args[i]);
        }

        String detail = annotation.detail();
        try {
            detail = "'" + "#{currentUserName}" + "'=>" + annotation.detail();
            for (Map.Entry<Object, Object> entry : map.entrySet()){
                Object k = entry.getKey();
                Object v = entry.getValue();
                detail = detail.replace("{{" + k + "}}", JSON.toJSONString(v));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return detail;
    }

    @SuppressWarnings("unused")
    @Before("operationLog()")
    public void doBeforeAdvice(JoinPoint joinPoint) {
        ServletRequestAttributes attr=(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attr != null;
        HttpServletRequest request =attr.getRequest();
        String clientIp = IPUtil.getIpAddr(request);
        String userAgent = request.getHeader("user-agent");
        String origin = request.getHeader("origin");
        String referer = request.getHeader("referer");
        String method = request.getMethod();
        String remoteUser = request.getRemoteUser();
        String requestUri = request.getRequestURI();
        log.info("收到业务请求: client-ip:{}, request-uri:{}, method:{}, origin:{}, referer:{}, remote-user:{}, user-agent:{}",clientIp,requestUri,method,origin,referer,remoteUser,userAgent);
    }

    /**
     * 处理完请求，返回内容
     */
    @AfterReturning(returning = "ret", pointcut = "operationLog()")
    public void doAfterReturning(Object ret){
        log.info("方法的返回值：{}",ret);
    }

    /**
     * 后置异常通知
     */
    @SuppressWarnings("unused")
    @AfterThrowing(throwing = "ce",pointcut = "operationLog()")
    public void throwException(CustomException ce){
        log.info("请求业务失败: {}-{}",ce.getTag(),ce.getMsg());
    }
}
