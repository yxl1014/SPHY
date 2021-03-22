package com.easyarch.filter;

import com.auth0.jwt.JWTAlgorithmException;
import com.auth0.jwt.JWTVerifier;
import com.easyarch.annotation.LogRecord;
import com.easyarch.annotation.PassToken;
import com.easyarch.dao.Logdao;
import com.easyarch.entity.Log;
import com.easyarch.entity.User;
import com.easyarch.service.Log.LogServie;
import com.easyarch.support.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.Date;


@Component
public class CheckHandle implements HandlerInterceptor {

    @Autowired
    private LogServie logServie;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");// 从 http 请求头中取出 token

        System.out.println(handler);
        // 如果不是映射到方法直接打回
        if (!(handler instanceof HandlerMethod)) {
            return false;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        //检查是否有passtoken注释，有则跳过认证
        if (method.isAnnotationPresent(PassToken.class)) {//获取这个方法是否有这个注释
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
        }

        //检查有没有需要用户权限的注解
        if (method.isAnnotationPresent(LogRecord.class)) {
            LogRecord logRecord = method.getAnnotation(LogRecord.class);
            if (logRecord.required()) {
                // 执行认证
                if (token == null) {
                    throw new RuntimeException("无token，请重新登录");
                }
                // 获取 token 中的 user
                User user;
                user = JWTUtil.unsign(token, User.class);
                if (user == null) {
                    throw new RuntimeException("用户不存在，请重新登录");
                }


                //访问日志
                Log log = new Log();
                log.setLogOp(logRecord.operation());
                log.setLogType(logRecord.type());
                log.setUserId(user.getUser_id());
                log.setCreateTime(new Date().getTime());
                log.setUrl(request.getRequestURI());
                logServie.addLog(log);

                return true;
            }
        }


        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
