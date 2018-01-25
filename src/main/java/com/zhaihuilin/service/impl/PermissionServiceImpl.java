package com.zhaihuilin.service.impl;

import com.zhaihuilin.dao.PermissionRepository;
import com.zhaihuilin.entity.Permission;
import com.zhaihuilin.service.PermissionService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zhaihuilin on 2017/11/16  9:19.
 */
@Service
@Log4j
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    /**
     * 新增
     * @param permission  权限
     * @return
     */
    @Override
    public Permission savePerission(Permission permission) {
        return permissionRepository.save(permission);
    }

    /**
     * 编辑
     * @param permission 权限
     * @return
     */
    @Override
    public Permission updatePerission(Permission permission) {
        return permissionRepository.save(permission);
    }

    /**
     * 根据权限编号 进行查询
     * @param id   权限编号
     * @return
     */
    @Override
    public Permission findPerissionByPId(long id) {
        return permissionRepository.findOne(id);
    }

    /**
     * 获取所有的权限信息
     * @return
     */
    @Override
    public List<Permission> findPermissions() {
        return permissionRepository.findAll();
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @Override
    public boolean deletePermission(long id) {
       Permission permission= permissionRepository.findOne(id);
       if (permission !=null){
           permissionRepository.delete(id);
           return true;
       }
        return false;
    }
}
