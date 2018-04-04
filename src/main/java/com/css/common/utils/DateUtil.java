package com.css.common.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

/**
 * DateUtil.java
 * 
 * 日期时间工具类
 * 
 */
public class DateUtil {
	public static Logger log = Logger.getLogger(DateUtil.class);
	public static final String[] pattern = { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy年MM月dd日",
			"yyyy年MM月dd日 HH:mm:ss" };

	private static String DEFAULT_FORMAT_PATTERN = "yyyy-MM-dd";
	private static SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_FORMAT_PATTERN);

	public static java.util.Date parse(String input) {
		try {
			return DateUtils.parseDate(input, pattern);
		} catch (Exception e) {
			log.debug("日期解析失败！");
		}
		return null;
	}

	public static Calendar parseCalendar(String input) {
		Date date = parse(input);
		if (date == null)
			return null;
		Calendar result = Calendar.getInstance();
		result.setTime(date);
		return result;
	}

	/**
	 * 时间格式转化
	 * 
	 * @param date
	 * @return
	 */
	public static String dateFormat(Date date, String formatPattern) {
		if (DEFAULT_FORMAT_PATTERN.equals(formatPattern)) {
			return dateFormat.format(date);
		}
		DEFAULT_FORMAT_PATTERN = formatPattern;
		dateFormat.applyPattern(formatPattern);
		return dateFormat.format(date);
	}

	/**
	 * 比较两个日期之间的大小
	 * 
	 * @param d1
	 * @param d2
	 * @return 前者大于后者返回true 反之false
	 */
	public static boolean compareDate(Date d1, Date d2) {
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(d1);
		c2.setTime(d2);

		int result = c1.compareTo(c2);
		if (result >= 0)
			return true;
		else
			return false;
	}
}
