package com.zhaihuilin.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * 角色
 * Created by zhaihuilin on 2017/11/15  15:45.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class Role implements Serializable {

    /**
     * 角色Id
     */
    @Id
    @GeneratedValue
    private long id;

    /**
     * 角色编码 暂时无用方便后期扩展
     */
    private String roleCode;

    /**
     * 角色名称
     */
    @NonNull
    private String name;

    /**
     * 默认角色
     */
    private boolean theDefault;

    @ManyToMany(mappedBy = "roleList")
    @JsonBackReference
    private List<Member> memberList;

    @ManyToMany(mappedBy = "roleList",fetch = FetchType.EAGER)
    @JsonBackReference
    private List<Permission> permissionList;
}
