package com.zhaihuilin.controller;

import com.zhaihuilin.entity.Permission;
import com.zhaihuilin.entity.RequestState;
import com.zhaihuilin.entity.ReturnMessages;
import com.zhaihuilin.entity.Role;
import com.zhaihuilin.service.PermissionService;
import com.zhaihuilin.service.RoleService;
import com.zhaihuilin.util.StringUtils;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaihuilin on 2017/11/15  17:49.
 */
@RestController
@Log4j
@RequestMapping(value ="/Role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;



    /**
     * 添加角色
     * @param name
     * @param roleCode
     * @param pIds
     * @param theDefault
     * @return
     */
    @RequestMapping(value = "/saveRole")
    public ReturnMessages saveRole(
            @RequestParam(name = "name")String name,
            @RequestParam(name = "roleCode")String roleCode,
            @RequestParam(name = "pIds",required = false)String[] pIds,
            @RequestParam(name = "default" ,required = false,defaultValue = "false") boolean theDefault
    ) {
        ReturnMessages returnMessages = null;
        if (!StringUtils.isNotEmpty(name)) {
            return new ReturnMessages(RequestState.ERROR, "角色名不能为空！", null);
        }
        if (!StringUtils.isNotEmpty(roleCode)) {
            return new ReturnMessages(RequestState.ERROR, "角色编码不能为空！", null);
        }
        Role role = new Role();
        List<Permission> permissions = new ArrayList<Permission>();
        if (pIds != null && pIds.length > 0) {
            for (String pId : pIds) {
                Permission permission = permissionService.findPerissionByPId(Integer.parseInt(pId));
                permissions.add(permission);//加载
            }
        }
        role.setName(name);
        role.setRoleCode(roleCode);
        if (permissions != null && permissions.size() > 0) {
            role.setPermissionList(permissions);
        }
        role = roleService.saveRole(role);
        if (pIds != null) {
            List<Permission> permissionList = role.getPermissionList();
            if (permissionList != null && permissionList.size() > 0) {
                for (Permission permission : permissionList) {
                    List<Role> roles = permission.getRoleList();
                    int index = roles.indexOf(role);
                    if (index >= 0) {
                        roles.remove(index);
                        permission.setRoleList(roles);
                        permission = permissionService.updatePerission(permission);
                    }
                }
            }
            for (String pId : pIds) {
                Permission permission = permissionService.findPerissionByPId(Integer.parseInt(pId));
                List<Role> roles = permission.getRoleList();
                if (roles.indexOf(role) < 0) {
                    roles.add(role);
                    permission.setRoleList(roles);
                    permission = permissionService.updatePerission(permission);
                }
            }
          }
            if(role != null){
                if(theDefault){
                    roleService.setRoleDefault(role.getId());
                }
                return new ReturnMessages(RequestState.SUCCESS,"创建角色成功.",role);
            }else{
                return new ReturnMessages(RequestState.ERROR,"创建角色失败.",null);
            }
    }

    /**
     * 编辑角色
     * @param id 角色ID
     * @param name 角色名称
     * @param roleCode 角色身份标识(ROLE:ADMIN:SUPER属于超级管理员无限配置权限)
     * @param pIds 所有权限
     * @param theDefault 是否为默认角色
     * @return
     */
    @RequestMapping(value ="/updateRole")
    public ReturnMessages updateRole(
            @RequestParam(name = "id")String id,
            @RequestParam(name = "name",required = false)String name,
            @RequestParam(name = "roleCode",required = false)String roleCode,
            @RequestParam(name = "pIds",required = false)String[] pIds,
            @RequestParam(name = "default" ,required = false,defaultValue = "false") boolean theDefault
    ){
        Role role = roleService.findRoleById(Integer.parseInt(id));
        List<Permission> permissions = new ArrayList<Permission>();
        if(role == null){
            return new ReturnMessages(RequestState.ERROR,"未查到该角色.",null);
        }
        if(StringUtils.isNotEmpty(name)){
            role.setName(name);
        }
        if(StringUtils.isNotEmpty(roleCode)){
            role.setRoleCode(roleCode);
        }
        role = roleService.saveRole(role);
        if(pIds!= null){
            List<Permission> permissionList = role.getPermissionList();
            if(permissionList != null && permissionList.size() > 0){
                for (Permission permission:permissionList
                        ) {
                    List<Role> roles = permission.getRoleList();
                    int index = roles.indexOf(role);
                    if(index >= 0){
                        roles.remove(index);
                        permission.setRoleList(roles);
                        permission = permissionService.updatePerission(permission);
                    }
                }
            }
            for (String pId : pIds){
                Permission permission = permissionService.findPerissionByPId(Integer.parseInt(pId));
                List<Role> roles = permission.getRoleList();
                if(roles.indexOf(role) < 0){
                    roles.add(role);
                    permission.setRoleList(roles);
                    permission = permissionService.updatePerission(permission);
                }
            }
        }
        if(role != null){
            if(theDefault){
                roleService.setRoleDefault(role.getId());
            }
            return new ReturnMessages(RequestState.SUCCESS,"角色编辑成功.",role);
        }else{
            return new ReturnMessages(RequestState.ERROR,"角色编辑失败.",role);
        }
    }

    /**
     * 根据 角色编号进行查询
     * @param id
     * @return
     */
    @RequestMapping(value = "/findRoleByid")
    public  ReturnMessages findRoleByid(
        @RequestParam(value = "id",defaultValue = "",required = true) String id
    ){

        if (!StringUtils.isNotEmpty(id)){
             new  ReturnMessages(RequestState.ERROR,"编号不能为空！",null);
        }
        Role role=roleService.findRoleById(Integer.parseInt(id));
        if (role !=null){
            return new ReturnMessages(RequestState.SUCCESS,"查询到数据！",role);
        }else {
            return new ReturnMessages(RequestState.ERROR,"暂无数据！",null);
        }
   }

    /**
     * 设置 默认角色
     * @param id
     * @return
     */
    @RequestMapping(value = "/updateDefRole")
   public  ReturnMessages updateDefRole(
           @RequestParam(value = "id",defaultValue = "",required = true) String id
        ){
           if (!StringUtils.isNotEmpty(id)){
               new  ReturnMessages(RequestState.ERROR,"编号不能为空！",null);
           }
           Role role= roleService.setRoleDefault(Integer.parseInt(id));
           if (role !=null){
               return new ReturnMessages(RequestState.SUCCESS,"设置默认成功！",role);
           }else {
               return new ReturnMessages(RequestState.ERROR,"设置默认失败！",null);
           }
        }


}
