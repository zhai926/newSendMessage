package com.zhaihuilin.dao;

import com.zhaihuilin.entity.Member;
import com.zhaihuilin.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by zhaihuilin on 2017/11/15  15:48.
 */
public interface RoleRepository extends JpaRepository<Role,Long> {

    /**
     * 根据角色id查询角色
     * @param id
     * @return
     */
    public Role findRoleById(long id);

    /**
     * 根据用户名查询
     * @param name
     * @return
     */
    public List<Role> findByName(String name);


    /**
     * 设定默认角色
     * @param id
     * @return
     */
    @Query(value = "update Role r set r.theDefault = true where r.id = ?1")
    public Role setRoleDefault(long id);

    /**
     * 查询默认角色
     * @return
     */
    public Role findRoleByTheDefaultTrue();


    /**
     * 通过用户查询角色
     * @return
     */
    public List<Role> findRoleByMemberList(List<Member> members);
}
