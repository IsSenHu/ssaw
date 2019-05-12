package com.ssaw.support.uaa.model;

import lombok.Data;

import java.util.Set;

/**
 * @author HuSen
 * @date 2019/4/27 14:46
 */
@Data
public class User {
    private Integer id;
    private String username;
    private Set<String> allPermissionService;
}