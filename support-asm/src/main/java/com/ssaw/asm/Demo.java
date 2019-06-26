package com.ssaw.asm;

import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.Type;

import java.io.IOException;

/**
 * @author HuSen
 * create on 2019/6/25 17:02
 */
public class Demo {
    public static void main(String[] args) throws IOException {
        ClassReader cr = new ClassReader(Type.getInternalName(HelloWorld.class));
    }
}
