package com.zhaihuilin.service.impl;

import com.zhaihuilin.dao.MemberRepository;
import com.zhaihuilin.dao.RoleRepository;
import com.zhaihuilin.entity.Member;
import com.zhaihuilin.entity.Role;
import com.zhaihuilin.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaihuilin on 2017/11/15  15:52.
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public Role findRoleById(long id) {
        return roleRepository.findRoleById(id);
    }

    @Override
    public List<Role> findRoleAll() {
        return roleRepository.findAll();
    }

    @Override
    public List<Role> findRoleByName(String name) {
        return roleRepository.findByName(name);
    }

    @Override
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public Role updateRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public boolean deleteRole(long id) {
        Role role= roleRepository.findRoleById(id);
        if (role !=null){
            roleRepository.delete(id);
            return true;
        }
        return false;
    }

    @Override
    public Role setRoleDefault(long id) {
        Role role = roleRepository.findRoleByTheDefaultTrue();
        if(role != null){
            role.setTheDefault(false);
            roleRepository.save(role);
        }
        role = roleRepository.findRoleById(id);
        role.setTheDefault(true);
        return roleRepository.save(role);
    }

    @Override
    public Role getRoleByDefaule() {
        return roleRepository.findRoleByTheDefaultTrue();
    }

    @Override
    public List<Role> findRoleByMember(Member member) {
        List<Member> members = new ArrayList<Member>();
        members.add(member);
        return roleRepository.findRoleByMemberList(members);
    }

    @Override
    public boolean setMemberRole(String username, List<Role> roles) {
        Member member = memberRepository.findMemberByUsername(username);
        if(member != null){
            member.setRoleList(roles);
            member = memberRepository.save(member);
            if(member != null){
                return true;
            }
        }
        return false;
    }
}
