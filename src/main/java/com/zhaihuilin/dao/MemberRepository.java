package com.zhaihuilin.dao;

import com.zhaihuilin.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * 持久层
 * Created by zhaihuilin on 2017/11/14  9:34.
 */
@Repository
public interface MemberRepository extends JpaRepository<Member,Long>,JpaSpecificationExecutor<Member> {
    //根据编号进行查询
    public  Member  findMemberById(String id);

    //根据邮箱进行查询
    public  Member  findMemberByEmail(String email);

    //根据用户名进行查询
    public  Member  findMemberByUsername(String username);
}
