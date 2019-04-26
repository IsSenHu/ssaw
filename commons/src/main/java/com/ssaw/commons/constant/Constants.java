package com.ssaw.commons.constant;

/**
 * @author HuSen.
 * @date 2018/11/27 19:33.
 */
public class Constants {

    /**
     * 响应状态码
     */
    public static class ResultCodes {
        /**
         * 成功
         */
        public static final int SUCCESS = 0;

        /**
         * 服务器内部错误
         */
        public static final int ERROR = 5000;

        /**
         * 参数错误
         * */
        public static final int PARAM_ERROR = 5001;

        /**
         * 数据不存在
         */
        public static final int DATA_NOT_EXIST = 5002;

        /**
         * 数据已存在
         */
        public static final int DATA_EXIST = 5003;

        /**
         * 禁止
         */
        public static final int FORBIDDEN = 403;
    }
}
