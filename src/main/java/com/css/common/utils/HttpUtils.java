package com.css.common.utils;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpUtils {

	private static Logger logger = LoggerFactory.getLogger(HttpUtils.class);
	
	private static final String USER_AGENT = "User-Agent";
	
	private HttpUtils(){
		
	}

	
	/**
	 * 获取浏览器类型
	 * 
	 * Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.18)
	 * Gecko/20110614 Firefox/3.6.18 Mozilla/5.0 (Linux; U; Android 2.3.4;
	 * zh-cn; ME722 Build/4.5.3-109_MS2-5) AppleWebKit/533.1 (KHTML, like Gecko)
	 * Version/4.0 Mobile Safari/533.1 Mozilla/5.0 (iPad; U; CPU OS 4_3_3 like
	 * Mac OS X; zh-cn) AppleWebKit/533.17.9 (KHTML, like Gecko) Version/5.0.2
	 * Mobile/8J2 Safari/6533.18.5 MQQBrowser/2.5 Mozilla/5.0 (iPad; U; CPU OS
	 * 4_3_3 like Mac OS X; zh-cn) AppleWebKit/533.17.9 (KHTML, like Gecko)
	 * Mobile/8J2 Safari/7534.48.3 Mozilla/5.0 (iPhone; U; CPU iPhone OS 4_3_1
	 * like Mac OS X; zh-cn) AppleWebKit/533.17.9 (KHTML, like Gecko)
	 * Version/5.0.2 Mobile/8G4 Safari/6533.18.5 IUC(U;iOS
	 * 4.3.1;Zh-cn;320*480;)/UCWEB8.5.1.178/41/800 Mozilla/5.0 (iPad; U; CPU OS
	 * 4_3 like Mac OS X; zh-cn Model:iPad2,2) AppleWebKit/533.17.9 (KHTML, like
	 * Gecko) Version/5.0.2 Mobile/8C148 Safari/6533.18.5 JUC (Linux; U; 2.3.7;
	 * zh-cn; MB525; 480*854) UCWEB7.9.4.145/139/800 Mozilla/5.0 (Linux; U;
	 * Android 2.3.7; zh-cn; MB525 Build/MIUI) AppleWebKit/533.1 (KHTML, like
	 * Gecko) Version/4.0 Mobile Safari/533.1 Mozilla/4.0 (compatible; MSIE 8.0;
	 * Windows NT 6.1; WOW64; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR
	 * 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; InfoPath.3; .NET4.0C;
	 * .NET4.0E) Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.19 (KHTML,
	 * like Gecko) Chrome/18.0.1025.162 Safari/535.19 android pad版 ?
	 * 
	 * @return
	 */
	public static String getUserAgent() {
		HttpServletRequest request = Struts2Utils.getRequest();
		if (null == request) {
			return "";
		}
		logger.debug("user_agent:" + request.getHeader(USER_AGENT));
		return request.getHeader(USER_AGENT);
	}
	
	/**
	 * 判断是否是ie浏览器
	 * @return
	 */
	public static boolean isMSIE() {
		boolean flg = false;
		String agent = getUserAgent();

		flg = (agent != null && agent.toLowerCase().indexOf("msie") >= 0)
				|| (agent != null && agent.toLowerCase().indexOf("Gecko") >= 0 && agent.toLowerCase().indexOf("rv:11") >= 0);
		return flg;

	}
}
