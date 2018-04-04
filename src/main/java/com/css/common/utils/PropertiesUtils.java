package com.css.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Properties;

public class PropertiesUtils {

	public static final Logger log = Logger.getLogger(PropertiesUtils.class);

	private static final String FTP_CONFIG = "sys_config.properties";

	private PropertiesUtils() {
	}

	public static String getSysParam(String key) {
		return getSysParam(key, null);
	}

	public static String getSysParam(String key, String defaultValue) {
		return getParam(key, defaultValue, FTP_CONFIG);
	}

	public static String getParam(String key, String defaultValue, String config) {
		Properties ps = new Properties();
		String value = defaultValue;
		if (StringUtils.isBlank(key) || StringUtils.isBlank(config)) {
			return value;
		}
		try {
			// 加载配置文件
			ps.load(PropertiesUtils.class.getClassLoader().getResourceAsStream(config));
			value = ps.getProperty(key, defaultValue);
		} catch (IOException e) {
			log.error(e);
		}
		return value;
	}
	
	public static void main(String[] args) throws IOException {
		String value = null;
		value = PropertiesUtils.getSysParam("ftp.host");
		value = PropertiesUtils.getParam("ftp.host","1234","application.properties");
		System.out.println(value);
	}

}
