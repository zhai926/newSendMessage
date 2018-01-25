package com.zhaihuilin.util;

import org.springframework.security.core.context.SecurityContextImpl;

import javax.servlet.http.HttpServletRequest;

/**
 * SecurityContextImpl
 * HttpRequestUtils 工具类 用来获取当前的登录用户
 * Created by zhaihuilin on 2017/11/15  11:47.
 */
public class HttpRequestUtils {

    /**
     * 获取当前登录名
     * @param request
     * @return
     */
    public static  String getUsername(HttpServletRequest request){
        SecurityContextImpl  securityContext = (SecurityContextImpl) request.getSession().getAttribute("SPRING_SECURITY_CONTEXT");
        if (securityContext ==null){
            return null;
        }
        String username=securityContext.getAuthentication().getName();
        return username;
    }
}
