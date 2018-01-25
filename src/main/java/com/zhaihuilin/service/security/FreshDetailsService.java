package com.zhaihuilin.service.security;


import com.zhaihuilin.entity.Member;
import com.zhaihuilin.entity.Role;
import com.zhaihuilin.entity.SysUser;
import com.zhaihuilin.service.MemberService;
import com.zhaihuilin.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**.
 * 1112
 * Created by SunHaiyang on 2017/8/4.
 */
@Service
public class FreshDetailsService implements UserDetailsService {
    @Autowired
    MemberService memberService;

    @Autowired
    RoleService roleService;

    /**
     * 1111
     * 登录时获取用户权限,并提交需验证的账号密码
     * @param s
     * @return User
     * @throws UsernameNotFoundException 用户不存在
     */
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        try {
            Member member = memberService.findMemberByusername(s);
            List<Role> roles = roleService.findRoleByMember(member);
            return new SysUser(member,roles);
        }catch (NullPointerException e){
            throw new UsernameNotFoundException("username not exist : " + s);
        }
    }
}
