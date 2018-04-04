package com.css.common.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.css.common.utils.JsonMapper;
import com.css.common.utils.Pagination;
import com.css.common.utils.Result;
import com.css.common.utils.Struts2Utils;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

public abstract class BaseAction extends ActionSupport implements Preparable {
	private static final long serialVersionUID = 5698596827388131338L;

	protected Logger logger = LoggerFactory.getLogger(getClass());

	/** *ajax请求响应,全局跳转配置 */
	private final String ajax = "global_ajax";

	/** * 分页对象 */
	protected Pagination page = new Pagination();
	//********************** DWZ 前端UI页面消息盒子内置参数 begin **********************//
	/** * 分页类型, 默认 "navTab".(navTab, dialog, dwz) */
	private String pageType = "navTab";
	/** *数据状态(DWZ UI 界面初始时配置) */
	private String statusCode = "";
	/** *提示信息 */
	private String message = "";
	/** *返回页面的ID */
	private String navTabId = "";
	/** *定义页面的ID,作用于其他页面引用 */
	private String rel = "";
	/** *消息盒子的回调函数类型 (closeCurrent, forward, forwardConfirm, other) */
	private String callbackType = "";
	/** *回调函数类型为：forward 时的链接地址 */
	private String forwardUrl = "";
	/** *回调函数类型为：forwardConfirm 时的提示信息 */
	private String confirmMsg = "";

//	private String ftpUrl = FTPConStant.FTP_URL_NONE;
	private String ftpUrl="";

	/** *专用于 error 消息时, 页面关闭范围(dialog, navTab, all) */
	private String closeCurrent = "dialog";
	//********************** DWZ 前端UI页面消息盒子内置参数 end **********************//

	//********************** DWZ 弹出层的刷新的参数 end **********************//
	/**刷新的按钮的ID*/
	private String refreshTagId;

	public String getRefreshTagId() {
	    return refreshTagId;
	}

	public void setRefreshTagId(String refreshTagId) {
	    this.refreshTagId = refreshTagId;
	}
	//********************** DWZ 弹出层的刷新的参数 end **********************//

	//-- CRUD Action函数 --//
	/**
	 * Action函数, 显示Entity列表.
	 * 建议return SUCCESS.
	 */
	public abstract String list() throws Exception;

	/**
	 * Action函数,显示新增或修改Entity界面.
	 * 建议return SUCCESS.
	 */
	@Override
	public abstract String input() throws Exception;

	/**
	 * Action函数,新增或修改Entity. 
	 * 建议return 自定义 JSON 数据.
	 */
	public abstract String save() throws Exception;

	/**
	 * Action函数,删除Entity.
	 * 建议return 自定义 JSON 数据.
	 */
	public abstract String delete() throws Exception;
	
	//-- Preparable函数 --//
	/**
	 * 实现空的prepare()函数,屏蔽了所有Action函数都会执行的公共的二次绑定.
	 */
	public void prepare() throws Exception {}

	/**
	 * 在input()前执行二次绑定.
	 */
	public abstract void prepareInput() throws Exception ;

	/**
	 * 在save()前执行二次绑定.
	 */
	public abstract void prepareSave() throws Exception ;

	//********************** 内置对象 **********************//
	/**
	 * 获取消息对象值
	 * @return
	 */
	public Map<String, String> getMessageBox(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("statusCode", statusCode);
		map.put("message", message);
		map.put("navTabId", navTabId);
		map.put("rel", rel);
		map.put("callbackType", callbackType);
		map.put("forwardUrl", forwardUrl);
		map.put("confirmMsg", confirmMsg);
		map.put("closeCurrent", closeCurrent);
		map.put("refreshTagId", refreshTagId);
		return map;
	}
	
	/**
	 * 返回ajax响应消息(用于 UI ajax 上传返回,一般json响应可用 success/error/info 方法动态返回)
	 * @param message 提醒消息
	 * @return 返回 DWZ UI 全局配置 jsp 页面
	 */
	protected String ajax(String message){
		setStatusCode("200");
		setMessage(message);
		return ajax;
	}

	/**
	 * 返回ajax响应消息(用于 UI ajax 上传返回,一般json响应可用 success/error/info 方法动态返回)
	 * @param result 业务处理结果
	 * @return 返回 DWZ UI 全局配置 jsp 页面
	 */
	protected String ajax(Result result) {
		setMessage(result.getMessage());
		if (result.getStatus()) {
			setStatusCode("200");
		} else {
			setStatusCode("300");
		}
		return ajax;
	}
	
	/**
	 * 返回正常的提醒消息
	 * 
	 * @param message
	 *                提醒消息
	 * @return 返回 DWZ UI 提醒消息
	 */
	protected String success(String message){
		setStatusCode("200");
		setMessage(message);
		Map<String, String> messageBox = getMessageBox();
		Struts2Utils.renderJson(messageBox);
		return null;
	}

	/**
	 * 返回异常的提醒消息
	 * @param message 提醒消息
	 * @return 返回 DWZ UI 提醒消息
	 */
	protected String error(String message){
		setStatusCode("300");
		setMessage(message);
		Map<String, String> messageBox = getMessageBox();
		Struts2Utils.renderJson(messageBox);
		return null;
	}

	/**
	 * 根据业务处理结果,返回提醒消息
	 * @param result 业务处理结果
	 * @return 返回 DWZ UI 提醒消息
	 */
	protected String info(Result result) {
		if(result.getStatus()){
			return success(result.getMessage());
		} else {
			return error(result.getMessage());
		}
	}

	/**
	 * json 转化工具对象
	 * @return
	 */
	protected JsonMapper getJsonMapper() {
		return new JsonMapper();
	}

	/**
	 * 把对象转化为 json 串
	 * 
	 * @param object
	 * @return
	 */
	public String toJSON(Object object) {
		return getJsonMapper().toJson(object);
	}

	/**
	 * 把对象转化为 json 串
	 * 
	 * @param object
	 * @return
	 */
	public String toJSON(String prefix, Object object) {
		return getJsonMapper().toJsonP(prefix, object);
	}

	/**
	 * 获取 request
	 * 
	 * @return
	 */
	public static HttpServletRequest getRequest(){
		return ServletActionContext.getRequest();
	}
	
	/**
	 * 获取 response
	 * @return
	 */
	public static HttpServletResponse getResponse(){
		return ServletActionContext.getResponse();
	}
	
	/**
	 * 获取 session
	 * @return
	 */
	public static HttpSession getSession(){
		return getRequest().getSession();
	}
	
	/**
	 * 获取 web 应用中 application 对象
	 * @return
	 */
	public static ServletContext getApplication(){
		return getServletContext();
	}
	
	/**
	 * 获取 web 应用中 application 对象
	 * @return
	 */
	public static ServletContext getServletContext(){
		return ServletActionContext.getServletContext();
	}
	
	/**
	 * 获取系统硬盘的绝对路径
	 * @return 
	 */
	public static String getRealPath(){
		return getServletContext().getRealPath("/");
	}
	
	/**
	 * 获取系统硬盘的绝对路径
	 * @return 
	 */
	public static String getRealPath(String absolutePath){
		return getServletContext().getRealPath(absolutePath);
	}
	
	/**
	 * 获取应用上下文路径(web应用访问名)
	 * @return
	 */
	public static String getContextPath(){
		return getRequest().getContextPath();
	}

	//********************** getter and setter method **********************//
	public Pagination getPage() {
		return page;
	}

	public void setPage(Pagination page) {
		this.page = page;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getNavTabId() {
		return navTabId;
	}

	public void setNavTabId(String navTabId) {
		this.navTabId = navTabId;
	}

	public String getRel() {
		return rel;
	}

	public void setRel(String rel) {
		this.rel = rel;
	}

	public String getCallbackType() {
		return callbackType;
	}

	public void setCallbackType(String callbackType) {
		this.callbackType = callbackType;
	}

	public String getForwardUrl() {
		return forwardUrl;
	}

	public void setForwardUrl(String forwardUrl) {
		this.forwardUrl = forwardUrl;
	}

	public String getPageType() {
		return pageType;
	}

	public void setPageType(String pageType) {
		this.pageType = pageType;
	}

	public String getConfirmMsg() {
		return confirmMsg;
	}

	public void setConfirmMsg(String confirmMsg) {
		this.confirmMsg = confirmMsg;
	}

	public String getCloseCurrent() {
	    return closeCurrent;
	}

	public void setCloseCurrent(String closeCurrent) {
	    this.closeCurrent = closeCurrent;
	}

	public String getFtpUrl() {
		return ftpUrl;
	}
}
