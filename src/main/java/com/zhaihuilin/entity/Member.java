package com.zhaihuilin.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by zhaihuilin on 2017/11/14  9:20.
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Member implements Serializable,UserDetails {

    @Id
    @GenericGenerator(name = "sys_uid",strategy = "com.zhaihuilin.util.KeyGeneratorUtils",parameters = {
            @Parameter(name = "k",value = "M")
    })
    @GeneratedValue(generator = "sys_uid")
    private  String  id;

    //用户名
    @NonNull
    private  String  username;
    //昵称
    private  String  nickname;

    //邮箱验证码
    private  String  emailCode;

    @NonNull
    //密码
    private  String  password;

    @NonNull
    //邮箱
    private  String  email;

    //电话号码
    private  String  smscode;

    //用户状态
    private  String  state;

    /**
     * 是否过期
     */
    private boolean isAccountNonExpired = true;

    /**
     * 是否锁定
     */
    private boolean isAccountNonLocked = true;

    /**
     * 密码是否过期
     */
    private boolean isCredentialsNonExpired = true;

    /**
     * 账户是否激活
     */
    private boolean isEnabled = true;

    /**
     * 所拥有的权限
     */
    @ManyToMany(fetch = FetchType.EAGER,cascade = {})
    @JoinTable(name = "member_role",joinColumns ={
            @JoinColumn(name = "member_id")
    },inverseJoinColumns = {
            @JoinColumn(name = "role_id")
    })
    @JsonManagedReference
    private List<Role> roleList;

    public Member( String username, String nickname, String password, String email, String smscode, String state) {
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.smscode = smscode;
        this.state = state;
    }

    public Member(String username, String nickname, String password, String email, String state) {
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.state = state;
    }

    /**
     * 权限分配
     */
    @Transient
    private Set<FreshGranteAuthority> authorities = new HashSet<FreshGranteAuthority>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }
}
