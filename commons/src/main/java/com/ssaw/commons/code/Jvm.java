package com.ssaw.commons.code;


import sun.misc.Launcher;

/**
 * @author HuSen
 * @date 2019/6/4 16:43
 */
public class Jvm {

    public static void main(String[] args) {
        Launcher launcher = Launcher.getLauncher();
        ClassLoader loader = launcher.getClassLoader();
    }
}