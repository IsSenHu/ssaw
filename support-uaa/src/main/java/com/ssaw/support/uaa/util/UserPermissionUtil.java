package com.ssaw.support.uaa.util;

import com.google.common.collect.Sets;
import com.ssaw.support.uaa.model.User;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

/**
 * @author HuSen
 * @date 2019/4/27 14:44
 */
public class UserPermissionUtil {

    /**
     * 权限校验，校验具体的菜单、url或者角色权限
     *
     * @param user    用户
     * @param request HttpServletRequest
     * @return 校验结果
     */
    public static boolean verify(User user, HttpServletRequest request) {
        String url = request.getHeader("x-user-serviceName");
        if (StringUtils.isEmpty(user)) {
            return false;
        }
        Set<String> allPermissionService = user.getAllPermissionService();
        for (String str : allPermissionService) {
            if (url.equalsIgnoreCase(str)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 权限赋值
     *
     * @param user 用户
     */
    public static void permission(User user) {
        if (user.getUsername().equals("admin")) {
            user.setAllPermissionService(Sets.newHashSet("client-service", "provider-service"));
        } else if (user.getUsername().equals("spring")) {
            user.setAllPermissionService(Sets.newHashSet("client-service"));
        } else {
            user.setAllPermissionService(Sets.newHashSet());
        }
    }
}