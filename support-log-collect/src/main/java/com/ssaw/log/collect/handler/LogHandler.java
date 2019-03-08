package com.ssaw.log.collect.handler;

import com.ssaw.log.collect.config.KafkaProducerConfig;
import com.ssaw.log.collect.vo.Log;

/**
 * @author HuSen
 * @date 2019/3/7 13:21
 */
public interface LogHandler {

    /**
     * 格式化日志为字符串
     * @param log 日志对象
     * @return 格式化后的字符串
     */
    String formatLog(Log log);

    /**
     * 设置日志的基本信息
     * @param logType 日志类型
     * @param o 数据
     * @return 日志对象
     */
    Log setLogBaseInfo(String logType, Object o);

    /**
     * 设置日志的基本信息
     * @param logType 日志类型
     * @param o 数据
     * @param message 信息
     * @return 日志对象
     */
    Log setLogBaseInfo(String logType, Object o, String message);

    /**
     * 输出日志到kafka
     * @param o 日志主体
     * @param logType 日志类型
     */
    default void log(Object o, String logType) {
        KafkaProducerConfig.logExecutor.sendLog(formatLog(setLogBaseInfo(logType, o)));
    }

    /**
     * 输出日志到kafka
     * @param o 日志主体
     * @param logType 日志类型
     * @param message 信息
     */
    default void log(Object o, String logType, String message) {
        KafkaProducerConfig.logExecutor.sendLog(formatLog(setLogBaseInfo(logType, o, message)));
    }
}