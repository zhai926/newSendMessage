package com.zhaihuilin.service;

import com.zhaihuilin.entity.Member;

import java.util.List;

/**
 * Created by zhaihuilin on 2017/11/14  9:37.
 */
public interface MemberService {

    //根据编号进行查询
    public Member findMemberByid(String id);

    //新增
    public Member saveMember(Member member);

    //编辑
    public Member updateMember(Member member);

    //根据邮箱进行查询
    public Member findMemeberByemail(String email);

    //根据用户名行查询
    public Member findMemberByusername(String username);

    //获取所有
    public List<Member> findMemebers();

    //判断邮箱是否存在
    public boolean  existMemberByemail(String email);

    //判断用户名是否存在
    public boolean  existMemberByusername(String username);

}
