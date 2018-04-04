package com.css.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

/**
 * IP 相关操作工具类
 * 
 * 
 */
public class IPUtil {

	/**
	 * 获取 客户端 IP 地址<BR>
	 * 如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，
	 * 那么真正的用户端的真实IP则是取X-Forwarded-For中第一个非unknown的有效IP字符串。
	 * 
	 * @param request
	 *            http请求
	 * @return
	 */
	public static String getClientIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}

		// 多个路由时，取第一个非unknown的ip
		final String[] arr = ip.split(",");
		for (final String str : arr) {
			if (!"unknown".equalsIgnoreCase(str)) {
				ip = str;
				break;
			}
		}
		return ip;
	}

	/**
	 * <p>
	 * 判断 IP 是否存在于指定规则中<BR>
	 * 思路如下：
	 * 
	 * <pre>
	 * 1.转换 【*-】 为 【0-】；
	 * 2.转换 【-*】 为 【-255】;
	 * 3.转换 【*】  为 【0-255】;
	 * 4.去除 【[]】 为 【】
	 * 5.提取最大、最小两个IP值;
	 * 6、把 IP 转化为 Long，通过比较大小判断是否在指定规则中。
	 * </pre>
	 * 
	 * <pre>
	 * 用法如下：
	 * <OL>
	 * <LI>
	 * *.*.*.*                          代表 0.0.0.0 至 255.255.255.255
	 * <LI>
	 * 192.168.0.*                      代表 192.168.0.0 至 192.168.0.255 
	 * <LI>
	 * 192.168.0.[0-255]                代表 192.168.0.0 至 192.168.0.255
	 * <LI>
	 * 192.168.*.[0-255]                代表 192.168.0.0 至 192.168.255.255
	 * <LI>
	 * [0-*].[*-20].*.[0-255]           代表 0.0.0.0 至 255.20.255.255
	 * <LI>
	 * [0-255].[0-255].[0-255].[0-255]  代表 0.0.0.0 至 255.255.255.255
	 * </pre>
	 * 
	 * @param pattern
	 *            IP规则
	 * @param ip
	 *            判定IP
	 * @return
	 */
	public static boolean compare(String pattern, String ip) {
		if (StringUtils.isBlank(pattern) || StringUtils.isBlank(ip) || !isIPAddress(ip)) {
			return false;
		}
		// 去除空格
		pattern = pattern.replaceAll("\\s*", "");
		// 替换[- [*- 为0-
		pattern = pattern.replaceAll("\\s*", "");
		pattern = pattern.replaceAll("\\[\\**-", "0-");
		// 替换-] -*] 为-255
		pattern = pattern.replaceAll("-\\**\\]", "-255");
		// 替换* 为0-255
		pattern = pattern.replaceAll("\\*+", "0-255");
		// 替换[
		pattern = pattern.replace("[", "");
		// 替换]
		pattern = pattern.replace("]", "");

		String minIp = pattern.replaceAll("-[^\\.]*", "");
		String maxIp = pattern.replaceAll("[^\\.]*-", "");


		Long ipValue = Long.valueOf(ipPad(ip).replace(".", ""));
		Long minValue = Long.valueOf(ipPad(minIp).replace(".", ""));
		Long maxValue = Long.valueOf(ipPad(maxIp).replace(".", ""));

		if (ipValue >= minValue && ipValue <= maxValue) {
			return true;
		}

		return false;
	}

	public static boolean isIPAddress(String ipaddr) {
		boolean flag = false;
		Pattern pattern = Pattern.compile(
				"\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
		Matcher m = pattern.matcher(ipaddr);
		flag = m.matches();
		return flag;
	}

	/**
	 * 补全 IP 地址<BR>
	 * 把 IP: 1.1.1.1 补全为 IP: 001.001.001.001<BR>
	 * 通过正则替换完成, 思路如下：
	 * 
	 * <pre>
	 * 1.IP 第   一  段,长度为 1、2 位补码；
	 * 2.IP 第 二、三 段,长度为 1、2 位补码；
	 * 3.IP 第   四  段,长度为 1、2 位补码；
	 * </pre>
	 * 
	 * @param ip
	 *            IP地址
	 * @return
	 */
	public static String ipPad(String ip) {
		if (StringUtils.isBlank(ip)) {
			return ip;
		}
		ip = ip.replaceAll("^(\\d)[.]", "00$1.").replaceAll("^(\\d\\d)[.]", "0$1.");
		ip = ip.replaceAll("[.](\\d)[.]", ".00$1.").replaceAll("[.](\\d\\d)[.]", ".0$1.");
		ip = ip.replaceAll("[.](\\d)$", ".00$1").replaceAll("[.](\\d\\d)$", ".0$1");
		return ip;
	}

	/**
	 * 测试
	 *
	 *
	 * @param args
	 * @author wang
	 *
	 * @Apr 14, 2015 3:55:08 PM
	 */
	public static void main(String[] args) {
		boolean flg = false;
		String ip = "192.168.8.255";
		String pattern = null;
		System.out.println(ip + "\t" + isIPAddress(ip));

		pattern = "192.168.[  -*].[-*]";
		System.out.println(ip +  "\t" + pattern + "\t" + compare(pattern, ip));

		pattern = "192.168.[*-].[-*]";
		System.out.println(ip +  "\t" + pattern + "\t" + compare(pattern, ip));

		pattern = "192.168.[0-8].[-*]";
		System.out.println(ip +  "\t" + pattern + "\t" + compare(pattern, ip));

		pattern = "192.168.20.*";
		System.out.println(ip +  "\t" + pattern + "\t" + compare(pattern, ip));
	}
}
