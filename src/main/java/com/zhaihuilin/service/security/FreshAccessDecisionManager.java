package com.zhaihuilin.service.security;


import com.zhaihuilin.entity.FreshGranteAuthority;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

/**
 * 3333
 * 权限判断
 * Created by SunHaiyang on 2017/8/4.
 */
@Service
public class FreshAccessDecisionManager implements AccessDecisionManager {
    /**
     * 判断是否拥有权限的决策方法，
     * @param authentication    包含当前用户信息    FreshDetailsService 中循环添加到 GrantedAuthority 对象中的权限信息集合.
     * @param o     当前正在请求的受保护对象           包含客户端发起的请求的requset信息，可转换为 HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();
     * @param collection     当前正在访问的受保护对象的配置属性   如一个角色列表  FreshMetadataSourceService的getAttributes(Object object)这个方法返回的结果，此方法是为了判定用户请求的url 是否在权限表中，如果在权限表中，则返回给 decide 方法，用来判定用户是否有此权限。如果不在权限表中则放行。
     * @throws AccessDeniedException
     * @throws InsufficientAuthenticationException
     */
    @Override
    public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> collection) throws AccessDeniedException, InsufficientAuthenticationException {
        HttpServletRequest request = ((FilterInvocation)o).getHttpRequest();
        if(permitAll(request)){ //验证是否是公开接口
            return;
        }
        if (collection ==null ||collection.size()<0){
             return;
        }

        String needRole;
        String url,method,roleCode;
        for (ConfigAttribute attribute : collection) {
            needRole= attribute.getAttribute();
            System.out.println("获取的角色------："+ needRole);
            for (GrantedAuthority grantedAuthority:authentication.getAuthorities()){
                if (grantedAuthority instanceof FreshGranteAuthority){
                    //将 grantedAuthority 装换成 自己封装的类  便于获取 属性
                    FreshGranteAuthority freshGranteAuthority = (FreshGranteAuthority)grantedAuthority;
                    url = freshGranteAuthority.getUrl();
                    method = freshGranteAuthority.getMethod();
                    roleCode = freshGranteAuthority.getAuthority();//获取权限字符串
                    if(roleCode.equals("ROLE:USER:GHOST")){
                        if(matchers("/memberInfo/saveMe",request)||matchers("/member/updateMe",request)){
                            return;
                        }
                    }
                    if(roleCode.equals("ROLE:ADMIN:SUPER")){
                        return;
                    }
                    if(matchers(url,request) || matchers("/error",request)){
                        if(method.toUpperCase().equals(request.getMethod()) || method.toUpperCase().equals("ALL")){
                            return;
                        }
                    }
                }else if(grantedAuthority.getAuthority().equals("ROLE_ANONYMOUS")){
                    if(matchers("/member/register",request)){
                        return;
                    }
                }
            }

        }
        throw new AccessDeniedException("no right");

    }

    /**
     * 判断是公开接口
     * @param request
     * @return
     */
    private boolean permitAll(HttpServletRequest request){
        String[] urls = {
                "/error",
                "/member/login",
                "/logout",
                "/memberInfo/find",
                "/member/find",
                "/member/sendCode",
                "/code",
                "/SmsCode/sendSms",
                "/member/updatePwd",
                "/Role/saveRole",
                "/member/updateMember",
                "/permission/savePermission",
                "/Role/updateRole",
                "/memberInfo/findMyRole"
        };
        for (String url : urls){
            if (matchers(url,request)){
                return true;
            }
        }
        return false;
    }

    /**
     * 校验url是否有效
     * @param url 拦截地址
     * @param request 访问消息
     * @return
     */
    private boolean matchers(String url, HttpServletRequest request){
        if(url == null || url.isEmpty()){
            return false;
        }
        AntPathRequestMatcher matcher = new AntPathRequestMatcher(url);
        if(matcher.matches(request)){
            return true;
        }
        return false;
    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return false;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
