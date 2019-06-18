/**
 * 
 */
package com.yuyi.model;

import java.io.Serializable;

/**
 *<p>Title:ResultBean </p>
 *<p>Description: </p>
 * @author 李文庆
 * @date 2018年4月8日下午1:08:13
 */

public class ResultBean<T> implements Serializable {
	
	private static final long serialVersionUID = 9088949537486778525L;
	
	public static final int SUCCESS = 0;
	public static final int FAIL = -1;
	
	private String msg = "success";
	private int code = SUCCESS;
	private T data;

	public ResultBean(){
		super();
	}
	
	public ResultBean(T data){
		super();
		this.data=data;
	}
	
	public ResultBean(Throwable e){
		super();
		this.msg = e.toString();
		this.code = FAIL;
	}
	
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
	
	@Override
	public String toString() {
		return "ResultBean [msg=" + msg + ", code=" + code + ", data=" + data + "]";
	}

}
