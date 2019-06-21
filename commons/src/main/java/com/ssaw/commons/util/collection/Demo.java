package com.ssaw.commons.util.collection;

import java.util.*;

/**
 * @author HuSen
 * create on 2019/6/17 12:03
 */
public class Demo {
    public static void main(String[] args) {
        SortedMap<Integer, List<Object>> treeMap = new TreeMap<>();
        treeMap.put(1, new ArrayList<>());
        treeMap.put(2, new ArrayList<>());
        treeMap.put(3, new ArrayList<>());
        treeMap.put(4, new ArrayList<>());
        System.out.println(treeMap.tailMap(3).firstKey());
    }
}
