package com.zhaihuilin.service;

import com.zhaihuilin.entity.Member;
import com.zhaihuilin.entity.Role;

import java.util.List;

/**
 * Created by zhaihuilin on 2017/11/15  15:50.
 */
public interface RoleService {

    /**
     * 根据id查询角色
     * @param id
     * @return
     */
    public Role findRoleById(long id);

    public List<Role> findRoleAll();

    /**
     * 根据角色名查询
     * @param name 角色名
     * @return
     */
    public List<Role> findRoleByName(String name);

    /**
     * 保存角色
     * @param role
     * @return
     */
    public Role saveRole (Role role);

    /**
     * 更新角色
     * @param role
     * @return
     */
    public Role updateRole(Role role);

    /**
     * 删除角色
     * @param id
     * @return
     */
    public boolean deleteRole(long id);

    /**
     * 设定为默认角色
     * @param id
     * @return
     */
    public Role setRoleDefault(long id);

    /**
     * 获取默认角色
     * @return
     */
    public Role getRoleByDefaule();


    /**
     * 通过用户查询角色
     * @param member
     * @return
     */
    public List<Role> findRoleByMember(Member member);

    /**
     * 赋予用户权限
     * @param username
     * @param roles
     * @return
     */
    public boolean setMemberRole(String username, List<Role> roles);
}
