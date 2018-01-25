package com.zhaihuilin.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Service;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 *
 * AbstractSecurityInterceptor 拦截请求进行权限鉴定
 * Created by SunHaiyang on 2017/8/4.
 */
@Service
public class FreshFilterSecurityInterceptor extends AbstractSecurityInterceptor implements Filter {

    @Autowired
    FreshMetadataSourceService metadataSourceService;

    @Autowired
    public void setAccessDecisionManage(FreshAccessDecisionManager freshAccessDecisionManager){
        super.setAccessDecisionManager(freshAccessDecisionManager);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    private static HttpServletRequest getSessionId(ServletRequest request){
        HttpServletRequest httpRequest=(HttpServletRequest)request;
        return httpRequest;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest servletRequest = getSessionId(request);
        FilterInvocation invocation = new FilterInvocation(request,response,chain);
        invoke(invocation);
    }

    /**
     *里面调用MyInvocationSecurityMetadataSource的getAttributes(Object object)这个方法获取fi对应的所有权限
     //再调用MyAccessDecisionManager的decide方法来校验用户的权限是否足够
     * @param filterInvocation   有一个被拦截的 url
     * @throws IOException
     * @throws ServletException
     */
    public void invoke(FilterInvocation filterInvocation) throws IOException, ServletException {
        InterceptorStatusToken token = super.beforeInvocation(filterInvocation);
        try {
            filterInvocation.getChain().doFilter(filterInvocation.getHttpRequest(),filterInvocation.getHttpResponse());
        }finally {
            super.afterInvocation(token,null);
        }
    }


    @Override
    public void destroy() {

    }

    @Override
    public Class<?> getSecureObjectClass() {
        return FilterInvocation.class;
    }

    @Override
    public SecurityMetadataSource obtainSecurityMetadataSource() {
        return this.metadataSourceService;
    }
}
