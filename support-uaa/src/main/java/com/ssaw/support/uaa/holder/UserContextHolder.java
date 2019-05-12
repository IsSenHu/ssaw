package com.ssaw.support.uaa.holder;


import com.ssaw.support.uaa.model.User;

/**
 * @author HuSen
 * @date 2019/4/27 15:01
 */
public class UserContextHolder {
    private static ThreadLocal<User> context = new InheritableThreadLocal<>();

    public static User currentUser() {
        return context.get();
    }

    public static void set(User user) {
        context.set(user);
    }

    public static void shutdown() {
        context.remove();
    }
}