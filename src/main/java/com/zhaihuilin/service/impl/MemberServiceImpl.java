package com.zhaihuilin.service.impl;

import com.zhaihuilin.dao.MemberRepository;
import com.zhaihuilin.entity.Member;
import com.zhaihuilin.service.MemberService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by zhaihuilin on 2017/11/14  9:43.
 */
@Service
@Log4j
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberRepository memberRepository;


    @Override
    public Member findMemberByid(String id) {
        return memberRepository.findMemberById(id);
    }

    @Transactional
    @Override
    public Member saveMember(Member member) {
        return memberRepository.save(member);
    }

    @Transactional
    @Override
    public Member updateMember(Member member) {
        return memberRepository.save(member);
    }

    @Override
    public Member findMemeberByemail(String email) {
        return memberRepository.findMemberByEmail(email);
    }

    @Override
    public Member findMemberByusername(String username) {
        return memberRepository.findMemberByUsername(username);
    }

    @Override
    public List<Member> findMemebers() {
        return memberRepository.findAll();
    }

    @Override
    public boolean existMemberByemail(String email) {
        Member member=findMemeberByemail(email);
        if (member !=null){
            return  true;
        }
        return false;
    }

    @Override
    public boolean existMemberByusername(String username) {
        Member member=findMemberByusername(username);
        if (member !=null){
            return  true;
        }
        return false;
    }
}
