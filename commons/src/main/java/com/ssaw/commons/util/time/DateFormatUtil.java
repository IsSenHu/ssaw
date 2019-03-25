package com.ssaw.commons.util.time;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;

/**
 * 日期工具类
 * @author HuSen
 * @date 2019/3/22 13:29
 */
public class DateFormatUtil {

    private static final DateTimeFormatter LOCAL_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final DateTimeFormatter LOCAL_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * LocalDate 格式化
     * @param localDate localDate
     * @return 格式化后的字符串
     */
    public static String localDateFormat(LocalDate localDate) {
        if (Objects.isNull(localDate)) {
            return "";
        }
        return LOCAL_DATE_FORMATTER.format(localDate);
    }

    /**
     * LocalDate 按指定格式进行格式化
     * @param localDate localDate
     * @param format format
     * @return 格式化后的字符串
     */
    public static String localDateFormat(LocalDate localDate, String format) {
        if (Objects.isNull(localDate) || Objects.isNull(format) || format.trim().length() == 0) {
            return "";
        }
        return formatString(localDate, format);
    }

    /**
     * LocalDateTime 格式化
     * @param localDateTime localDateTime
     * @return 格式化后的字符串
     */
    public static String localDateTimeFormat(LocalDateTime localDateTime) {
        if (Objects.isNull(localDateTime)) {
            return "";
        }
        return LOCAL_DATE_TIME_FORMATTER.format(localDateTime);
    }

    /**
     * LocalDateTime 按指定格式进行格式化
     * @param localDateTime localDateTime
     * @param format format
     * @return 格式化后的字符串
     */
    public static String localDateTimeFormat(LocalDateTime localDateTime, String format) {
        if (Objects.isNull(localDateTime) || Objects.isNull(format) || format.trim().length() == 0) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return formatter.format(localDateTime);
    }

    /**
     * 获取当前秒数
     * @param localDateTime localDateTime
     * @return 当前秒数
     */
    public static Long getSecond(LocalDateTime localDateTime) {
        if (Objects.isNull(localDateTime)) {
            return null;
        }
        return localDateTime.toEpochSecond(ZoneOffset.of("+8"));
    }

    /**
     * 获取当前秒数
     * @param localDate localDate
     * @return 当前秒数
     */
    public static Long getSecond(LocalDate localDate) {
        if (Objects.isNull(localDate)) {
            return null;
        }
        return localDate.atStartOfDay().toEpochSecond(ZoneOffset.of("+8"));
    }

    /**
     * 获取当前毫秒数
     * @param localDateTime localDateTime
     * @return 当前毫秒数
     */
    public static Long getMilliSecond(LocalDateTime localDateTime) {
        if (Objects.isNull(localDateTime)) {
            return null;
        }
        return localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    /**
     * 获取当前毫秒数
     * @param localDate localDate
     * @return 当前毫秒数
     */
    public static Long getMilliSecond(LocalDate localDate) {
        if (Objects.isNull(localDate)) {
            return null;
        }
        return localDate.atStartOfDay().toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    /**
     * 字符串转LocalDateTime
     * @param str 字符串
     * @return LocalDateTime
     */
    public static LocalDateTime getLocalDateTime(String str) {
        if (Objects.isNull(str) || str.trim().length() == 0) {
            return null;
        }
        return LocalDateTime.parse(str, LOCAL_DATE_TIME_FORMATTER);
    }

    /**
     * 根据指定格式转LocalDateTime
     * @param str 字符串
     * @param format 格式
     * @return LocalDateTime
     */
    public static LocalDateTime getLocalDateTime(String str, String format) {
        DateTimeFormatter formatter = getDateTimeFormatter(str, format);
        if (formatter == null) {
            return null;
        }
        return LocalDateTime.parse(str, formatter);
    }

    /**
     * 字符串转LocalDate
     * @param str 字符串
     * @return LocalDate
     */
    public static LocalDate getLocalDate(String str) {
        if (Objects.isNull(str) || str.trim().length() == 0) {
            return null;
        }
        return LocalDate.parse(str, LOCAL_DATE_FORMATTER);
    }

    /**
     * 根据指定格式转LocalDate
     * @param str 字符串
     * @param format 格式
     * @return LocalDate
     */
    public static LocalDate getLocalDate(String str, String format) {
        DateTimeFormatter formatter = getDateTimeFormatter(str, format);
        if (formatter == null) {
            return null;
        }
        return LocalDate.parse(str, formatter);
    }

    /**
     * Date 转 LocalDateTime
     * @param date Date
     * @return LocalDateTime
     */
    private static LocalDateTime dateToLocalDateTime(Date date) {
        if (Objects.isNull(date)) {
            return null;
        }
        return date.toInstant().atOffset(ZoneOffset.of("+8")).toLocalDateTime();
    }

    /**
     * LocalDateTime 转 Date
     * @param localDateTime LocalDateTime
     * @return Date
     */
    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        if (Objects.isNull(localDateTime)) {
            return null;
        }
        return Date.from(localDateTime.toInstant(ZoneOffset.of("+8")));
    }

    private static String formatString(LocalDate localDate, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return formatter.format(localDate);
    }

    private static DateTimeFormatter getDateTimeFormatter(String str, String format) {
        if (Objects.isNull(str) || str.trim().length() == 0 || Objects.isNull(format) || format.trim().length() == 0) {
            return null;
        }
        return DateTimeFormatter.ofPattern(format);
    }
}