package jsz.dk.signmanagement.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import jsz.dk.signmanagement.common.annotations.NeedLogin;
import jsz.dk.signmanagement.common.entity.Consts;
import jsz.dk.signmanagement.common.entity.CustomException;
import jsz.dk.signmanagement.common.entity.ResponseParent;
import jsz.dk.signmanagement.entity.User;
import jsz.dk.signmanagement.enums.CacheEnum;
import jsz.dk.signmanagement.enums.ResponseCode;
import jsz.dk.signmanagement.utils.RedisUtil;
import jsz.dk.signmanagement.utils.Tools;
import org.apache.ibatis.plugin.Intercepts;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @ProjectName: sign-management
 * @Package: jsz.dk.signmanagement.interceptor
 * @ClassName: LoginIntercept
 * @Author: Strawberry
 * @Description:
 * @Date: 2021/07/15 17:31
 */
@Component
public class LoginIntercept implements HandlerInterceptor {
    private final static String TAG = "[LoginIntercept]";
    @Resource
    private RedisUtil redisUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /*
          isAssignableFrom() 判定此 Class 对象所表示的类或接口与指定的 Class 参数所表示的类或接口是否相同，或是否是其超类或超接口
          isAssignableFrom 与instanceof 区别
          isAssignableFrom()方法是判断是否为某个类的父类
          instanceof 关键字是判断是否某个类的子类
         */
        if(handler.getClass().isAssignableFrom(HandlerMethod.class)){
            //HandlerMethod 封装方法定义相关的信息,如类,方法,参数等
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            NeedLogin needLogin = getTagAnnotation(method, NeedLogin.class);
            if(needLogin != null){
                //登录校验
                if(needLogin.login() && !isLogin(request, CacheEnum.LOGIN)){
                    responseOut(response, ResponseParent.fail(ResponseCode.UN_AUTHORIZED));
                    return false;
                }

                //google校验
                if(needLogin.google() && !isGoogle(request,CacheEnum.GOOGLE)){
                    responseOut(response,ResponseParent.fail(ResponseCode.AUTH_GOOGLE_NOT_FOUND));
                    return false;
                }
            }

        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    /**
     * 检查是否 登录或者google验证
     * @param request
     * @param cacheEnum
     * @return
     */
    public boolean isGoogle(HttpServletRequest request,CacheEnum cacheEnum){
        String tokenKey = Tools.getTokenKey(request,cacheEnum);
        String googleStatus = String.valueOf(redisUtil.get(tokenKey));
        if(googleStatus != null && googleStatus.equalsIgnoreCase(Consts.SUCCESS)){
            return true;
        }
        return false;
    }

    /**
     * 是否登录
     * @param request
     * @param cacheEnum
     * @return
     */
    public boolean isLogin(HttpServletRequest request,CacheEnum cacheEnum) throws CustomException {
        String tokenKey = Tools.getTokenKey(request,cacheEnum);
        try {
            return redisUtil.get(tokenKey) != null;
        }catch (Exception e){
            throw new CustomException(TAG,"redis连接失败：(e)"+e.getMessage());
        }
    }


    /**
     * 获取目标注解
     * 如果方法上有注解就返回方法上的注解配置，否则类上的
     * @param method
     * @param annotationClass
     * @param <A>
     * @return
     */
    public <A extends Annotation> A getTagAnnotation(Method method, Class<A> annotationClass) {
        // 获取方法中是否包含注解
        Annotation methodAnnotate = method.getAnnotation(annotationClass);
        //获取 类中是否包含注解，也就是controller 是否有注解
        Annotation classAnnotate = method.getDeclaringClass().getAnnotation(annotationClass);
        return (A) (methodAnnotate!= null?methodAnnotate:classAnnotate);
    }

    /**
     * 回写给客户端
     */
    private void responseOut(HttpServletResponse response, ResponseParent<?> responseParent) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null ;
        String json = JSONObject.toJSON(responseParent).toString();
        out = response.getWriter();
        out.append(json);
    }
}
