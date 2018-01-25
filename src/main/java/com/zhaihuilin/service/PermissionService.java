package com.zhaihuilin.service;

import com.zhaihuilin.entity.Permission;

import java.util.List;

/**
 * Created by zhaihuilin on 2017/11/16  9:13.
 */
public interface PermissionService {

    /**
     * 新增
     * @param permission  权限
     * @return  Permission
     */
    public Permission savePerission(Permission permission);

    /**
     * 编辑
     * @param permission 权限
     * @return  Permission
     */
    public Permission updatePerission(Permission permission);

    /**
     * 根据 权限编号查询 权限
     * @param id   权限编号
     * @return Permission
     */
    public Permission findPerissionByPId(long id);

    /**
     * 获取所有的权限
     * @return  List<Permission>
     */
    public List<Permission> findPermissions();

    /**
     * 删除
     * @param id
     * @return
     */
    public boolean deletePermission(long id);
}
