package com.citic.common.web;
public enum ResultCode {

	OPERATION_SUCCESSED(000, "操作成功"),
	OPERATION_FAILED(001, "操作失败"), 
	OPERATION_ABNORMAL(002, "操作异常"),
	SESSION_INVALID(003, "会话已失效"), 
	OPERATION_NOT_PERMITTED(004, "权限不足"),
	OPERATION_PARAM_ERROR(005, "数据格式错误"), 
	CUSTOM_ERROR(998, "{errmsg}"),
	NONE(999, "无意义数据"), 

	USER_NAME_OR_PASSWORD_EMPTY(101, "帐号或密码为空"),
	USER_DENIED(102, "未授权登录"),
	USER_NAME_OR_PASSWORD_ERROR(103, "账号或密码错误"),
	USER_PASSWORD_ERROR(104, "密码错误"),
	USER_PASSWORD_NEW_SAMEWITH_OLD(105, "新密码和旧密码一样"),
	USER_PASSWORD_EMPTY(106, "密码为空"), 

	USER_NOTEXIST(201, "用户不存在"),
	USER_MOBILE_EXIST(203, "手机号已被使用"), 
	USER_NAME_EMPTY(204, "用户姓名不能为空"), 
	USER_MOBILE_EMPTY(205, "用户手机号不能为空"), 
	
	APP_NOT_EXIST(310,"应用不存在"),
	INTENT_NOT_EXIST(310,"场景不存在"),
	INTENT_HAS_EXIST(310,"场景已经存在"),
	DICT_NOT_EXIST(320,"词库不存在"),
	
	ROLE_NAME_EMPTY(601, "权限名不能为空"),
	ROLE_NAME_EXIST(602, "权限名已存在"), 
	ROLE_NOTEXIST(603, "权限不存在"), 
	
	;

	private Integer code;
	private String msg;
	ResultCode(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	public Integer getResultCode() {
		return code;
	}
	public String getResultMsg() {
		return msg;
	}
	public static String getMsg(Integer code) {
		for(ResultCode val : ResultCode.values()) 
		{
			if(val.getResultCode()==(code)) {
				return val.getResultMsg();
			}
		}
		return ResultCode.NONE.getResultMsg();
	}
 
}
