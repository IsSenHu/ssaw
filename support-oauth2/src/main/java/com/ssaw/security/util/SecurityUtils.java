package com.ssaw.security.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author HuSen.
 * @date 2018/12/11 10:03.
 */
public class SecurityUtils {

    /**
     * 构造方法私有
     */
    private SecurityUtils() {}

    /**
     * 获取当前UserDetails
     * @param tClass 类型的Class
     * @param <T> 类型
     * @return 当前UserDetails
     */
    @SuppressWarnings({"SingleStatementInBlock", "unchecked"})
    public static <T extends UserDetails> T getUserDetails(Class<T> tClass) {
        SecurityContext securityContext;
        Authentication authentication;
        Object principal;
        if((securityContext = SecurityContextHolder.getContext()) == null
                || (authentication = securityContext.getAuthentication()) == null || (principal = authentication.getPrincipal()) == null) {
            return null;
        }else {
            if(tClass.isInstance(principal)) {
                return (T) principal;
            }
        }
        return null;
    }
}
