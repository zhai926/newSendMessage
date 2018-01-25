package com.zhaihuilin.dao;

import com.zhaihuilin.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by zhaihuilin on 2017/11/15  17:50.
 */
@Repository
public interface PermissionRepository extends JpaRepository<Permission,Long> {

    /**
     * 获取所有的权限
     * @return
     */
    public List<Permission> findAllByOldIsNull();
}
