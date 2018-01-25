package com.zhaihuilin.controller;


import com.zhaihuilin.entity.Permission;
import com.zhaihuilin.entity.RequestState;
import com.zhaihuilin.entity.ReturnMessages;
import com.zhaihuilin.service.PermissionService;
import com.zhaihuilin.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 权限controller
 * Created by SunHaiyang on 2017/9/23.
 */
@RestController
@RequestMapping(value = "/permission")
public class PermissionController {
    @Autowired
    private PermissionService permissionService;


    /**
     * 通过ID查询权限
     * @param id 权限ID[可空]
     * @return
     */
    @RequestMapping(value = "/find")
    public ReturnMessages findPermissionById(
            @RequestParam(name = "id",required = false,defaultValue = "-1")String id
    ){
        if (StringUtils.isNotEmpty(id)){
            Permission permission = permissionService.findPerissionByPId(Integer.parseInt(id));
            if(permission != null){
                return new ReturnMessages(RequestState.SUCCESS,"查询成功.",permission);
            }else{
                return new ReturnMessages(RequestState.ERROR,"查询失败.",null);
            }
        }else{
            List<Permission> permissions = permissionService.findPermissions();
            if(permissions.size() > 0){
                return new ReturnMessages(RequestState.SUCCESS,"查询成功.",permissions);
            }else {
                return new ReturnMessages(RequestState.ERROR,"查询失败.",null);
            }
        }
    }

    /**
     * 添加权限
     * @param url 链接地址
     * @param method 链接方式
     * @param description 描述
     * @param oldId 父类[可空]
     * @param name 组件名称[可空] - vue路由用
     * @param path 组件地址[可空] - vue路由用
     * @param component 组件[可空] - vue路由用
     * @return
     */
    @RequestMapping(value = "/savePermission")
    public ReturnMessages savePermission(
            @RequestParam(name = "url")String url,
            @RequestParam(name = "method")String method,
            @RequestParam(name = "description")String description,
            @RequestParam(name = "oldId" ,required = false,defaultValue = "-1")String oldId,
            @RequestParam(name = "name",required = false)String name,
            @RequestParam(name = "path",required = false)String path,
            @RequestParam(name = "component",required = false)String component
    ){
        Permission permission = new Permission();
        Permission oldPermission = null;
        permission.setUrl(url);
        permission.setMethod(method);
        permission.setDescription(description);
        if(StringUtils.isNotEmpty(oldId)){
            oldPermission = permissionService.findPerissionByPId(Integer.parseInt(oldId));
        }
        if (oldPermission != null){
            permission.setOld(oldPermission);
        }
        if(StringUtils.isNotEmpty(name)){
            permission.setName(name);
        }
        if(StringUtils.isNotEmpty(path)){
            permission.setPath(path);
        }
        if(StringUtils.isNotEmpty(component)){
            permission.setComponent(component);
        }
        permission = permissionService.savePerission(permission);
        if(permission != null){
            return new ReturnMessages(RequestState.SUCCESS,"添加权限成功.",permission);
        }else{
            return new ReturnMessages(RequestState.ERROR,"添加权限失败.",null);
        }
    }

    /**
     * 修改权限
     * @param id 权限ID
     * @param url 链接地址[可空]
     * @param method 链接模式[可空]
     * @param description 描述[可空]
     * @param oldId 父类ID[可空]
     * @param name 组件名称[可空] - vue路由用
     * @param path 组件地址[可空] - vue路由用
     * @param component 组件[可空] - vue路由用
     * @return
     */
    @RequestMapping(value = "/updatePermission")
    public ReturnMessages updatePermission(
            @RequestParam(name = "id")String id,
            @RequestParam(name = "url",required = false)String url,
            @RequestParam(name = "method",required = false)String method,
            @RequestParam(name = "description",required = false)String description,
            @RequestParam(name = "oldId" ,required = false,defaultValue = "-1")String oldId,
            @RequestParam(name = "name",required = false)String name,
            @RequestParam(name = "path",required = false)String path,
            @RequestParam(name = "component",required = false)String component
    ){
        Permission permission = null;
        Permission oldPermission = null;
        permission = permissionService.findPerissionByPId(Integer.parseInt(id));
        if(permission == null){
            return new ReturnMessages(RequestState.ERROR,"未查询到修改权限.",null);
        }
        if (StringUtils.isNotEmpty(url)){
            permission.setUrl(url);
        }
        if(StringUtils.isNotEmpty(method)){
            permission.setMethod(method);
        }
        if(StringUtils.isNotEmpty(description)){
            permission.setDescription(description);
        }
        if(StringUtils.isNotEmpty(name)){
            permission.setName(name);
        }
        if(StringUtils.isNotEmpty(path)){
            permission.setPath(path);
        }
        if(StringUtils.isNotEmpty(component)){
            permission.setComponent(component);
        }
        if(StringUtils.isNotEmpty(oldId)){
            oldPermission = permissionService.findPerissionByPId(Integer.parseInt(oldId));
        }
        if(oldPermission != null){
            permission.setOld(oldPermission);
        }
        permission = permissionService.updatePerission(permission);
        if(permission != null){
            return new ReturnMessages(RequestState.SUCCESS,"修改权限成功.",permission);
        }else{
            return new ReturnMessages(RequestState.ERROR,"修改权限失败.",null);
        }
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @RequestMapping(value = "/deletePermission")
    public ReturnMessages deletePermission(
            @RequestParam(name = "id")String id
    ){
        if (permissionService.deletePermission(Integer.parseInt(id))){
            return new ReturnMessages(RequestState.SUCCESS,"删除成功.",true);
        }else{
            return new ReturnMessages(RequestState.ERROR,"删除失败.",false);
        }
    }


}
