package com.ssaw.support.uaa.interceptor;

import com.alibaba.fastjson.JSON;
import com.ssaw.support.uaa.holder.UserContextHolder;
import com.ssaw.support.uaa.model.User;
import com.ssaw.support.uaa.util.UserPermissionUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author HuSen
 * @date 2019/4/27 14:59
 */
public class UserContextInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user = getUser(request);
        UserPermissionUtil.permission(user);
        if (!UserPermissionUtil.verify(user, request)) {
            response.setHeader("Content-Type", "application/json");
            String jsonString = JSON.toJSONString("no permission access service, please check");
            response.getWriter().write(jsonString);
            response.getWriter().flush();
            response.getWriter().close();
            throw new RuntimeException("no permission access service, please check");
        }
        UserContextHolder.set(user);
        return true;
    }

    private User getUser(HttpServletRequest request) {
        User user = new User();
        user.setId(Integer.valueOf(request.getHeader("x-user-id")));
        user.setUsername(request.getHeader("x-user-name"));
        return user;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContextHolder.shutdown();
    }
}