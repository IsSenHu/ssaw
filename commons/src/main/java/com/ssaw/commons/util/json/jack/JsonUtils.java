package com.ssaw.commons.util.json.jack;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 使用jackJson
 *
 * @author HuSen.
 * @date 2018/11/23 13:55.
 */
public class JsonUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * Java对象转为Json格式字符串
     *
     * @param object Java对象
     * @return Json格式字符串
     */
    public static String object2JsonString(Object object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将Json字符串转为Java对象
     *
     * @param text  Json字符串
     * @param clazz class
     * @param <T>   泛型类对象
     * @return Java对象
     */
    public static <T> T jsonString2Object(String text, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(text, clazz);
        } catch (Exception e) {
            return null;
        }
    }
}
