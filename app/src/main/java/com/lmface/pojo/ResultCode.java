package com.lmface.pojo;

public class ResultCode {

	int code=10000;
	String msg="";

	public ResultCode(){}
	
	public ResultCode(int code,String msg){
		this.code=code;
		this.msg=msg;
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	@Override
	public String toString() {
		return "ResultCode [code=" + code + ", msg=" + msg + "]";
	}
	
	
}
