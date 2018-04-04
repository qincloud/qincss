package com.css.common.utils;

/**
 * 用于 业务层 数据处理结束后, 返回到 控制层 做响应处理
 * 
 *
 */
public class Result {

	/** * 结果状态(true:正常,false:异常) */
	private Boolean status;
	/** * 结果消息 */
	private String message;
	/* 数据 */
	private Object obj;

	public Result() {
		super();
	}

	public Result(boolean status, String message) {
		super();
		this.status = status;
		this.message = message;
	}

	public Boolean getStatus() {
		return status;
	}

	public Result setStatus(boolean status) {
		this.status = status;
		return this;
	}

	public String getMessage() {
		return message;
	}

	public Result setMessage(String message) {
		this.message = message;
		return this;
	}

	public Object getObj() {
		return obj;
	}

	public Result setObj(Object obj) {
		this.obj = obj;
		return this;
	}

	@Override
	public String toString() {
		return "Result [status=" + status + ", message=" + message + ", obj=" + obj + "]";
	}

}
