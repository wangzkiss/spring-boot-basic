/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.citic.common.web;

import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.alibaba.fastjson.JSONObject;
import com.citic.common.mapper.JsonMapper;
import com.citic.common.utils.DateUtils;
import com.google.common.collect.Maps;

/**
 * 控制器支持类
 * 
 * @author jeeplus
 * @version 2013-3-23
 */
public abstract class BaseController {

	/**
	 * 日志对象
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 管理基础路径
	 */
	@Value("${adminPath}")
	protected String adminPath;

	/**
	 * 前端基础路径
	 */
	@Value("${frontPath}")
	protected String frontPath;

	/**
	 * 前端URL后缀
	 */
	@Value("${urlSuffix}")
	protected String urlSuffix;

	
	private static  Map <String,String> uniqueKeys =Maps.newHashMap();
	static {
		uniqueKeys.put("conn_name", "连接名称不能重复！");
		uniqueKeys.put("udb", "库信息不能重复！");
		uniqueKeys.put("sourcename", "源名称不能重复！");
		uniqueKeys.put("unique_source","源信息不能重复！");
		uniqueKeys.put("sourceproname","属性名称不能重复！");
		uniqueKeys.put("sourceproindex", "属性序号不能重复！");
		uniqueKeys.put("storeproname", "存储名称不能重复！");
		uniqueKeys.put("unique_store", "存储信息不能重复！");
		uniqueKeys.put("storeproname", "存储属性不能重复！");
		uniqueKeys.put("storeproindex", "存储属性序号不能重复！");
		uniqueKeys.put("consumer_name_u", "消费信息不能重复！");
		uniqueKeys.put("consumer_name_s", "消费信息不能重复！");
		uniqueKeys.put("sys_user_login_name", "登录名不能重复！");
		uniqueKeys.put("idx_group_name", "名称不能重复！");
		uniqueKeys.put("idx_task_name", "名称不能重复！");
		uniqueKeys.put("agent_name", "名称不能重复！");
	 }
	
	/**
	 * 全局参数验证异常处理
	 * 
	 * @param red
	 * @param e
	 * @return
	 */
	@ExceptionHandler(value = Exception.class)
	@ResponseBody
	JSONObject defaultErrorHandler(HttpServletRequest red, Exception e) {
		JSONObject data = new JSONObject();
		String msg = "服务器繁忙，稍后重试！";
		String code = "500";
		if (e instanceof NoHandlerFoundException) {
			msg = "页面不存在！";
			code = "404";
		} else if (e instanceof AuthenticationException) {
			msg = "无权限访问！";
			code = "403";
		} else if (e instanceof BindException) {
			msg = "";
			for (FieldError error : ((BindException) e).getFieldErrors()) {
				msg += error.getDefaultMessage() + ";";
			}
		} else if (e instanceof MissingServletRequestParameterException) {
			msg = "缺少参数" + ((MissingServletRequestParameterException) e).getParameterName();
			code = "005";
		} else if (e instanceof MethodArgumentTypeMismatchException) {
			code = "005";
			msg = ((MethodArgumentTypeMismatchException) e).getName() + "参数类型不合法，需要类型为"
					+ ((MethodArgumentTypeMismatchException) e).getRequiredType().getCanonicalName();
		} else if (e instanceof DuplicateKeyException) {
			String errorKey=e.getCause().getMessage();
			//Duplicate entry 'DB2-192.168.2.26-test-1' for key 'udb'
			if (errorKey != null && errorKey.contains("Duplicate")) {
				int end=errorKey.lastIndexOf("'");
				int start=errorKey.lastIndexOf(" ")+2;
				String uniqueKey=errorKey.substring(start,end);
				String s=uniqueKeys.get(uniqueKey);
				msg = "操作失败,"+s;
			} else{
				msg = "操作失败,数据存在重复！详情请看唯一索引：" + e.getCause().getMessage();
			}
			code = "001";
		}
		data.put("msg", msg);
		data.put("code", code);
		logger.error("异常：", e);
		return data;
	}
	 

	/**
	 * 客户端返回JSON字符串
	 * 
	 * @param response
	 * @param object
	 * @return
	 */
	protected String renderString(HttpServletResponse response, Object object) {
		return renderString(response, JsonMapper.toJsonString(object));
	}

	/**
	 * 客户端返回字符串
	 * 
	 * @param response
	 * @param string
	 * @return
	 */
	protected String renderString(HttpServletResponse response, String string) {
		try {
			response.reset();
			response.setContentType("application/json");
			response.setCharacterEncoding("utf-8");
			response.getWriter().print(string);
			return null;
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * 
	 * @param code
	 *            错误代号 000：操作成功 001：操作失败 002：操作异常 003：会话已失效 004：权限不足 005：数据格式错误
	 *            998：{errmsg} 999：无意义数据 101：帐号或密码为空 102：未授权登录 103：账号或密码错误
	 *            104：密码错误 105：新密码和旧密码一样 106：密码为空 201：用户不存在 203：手机号已被使用
	 *            204：用户姓名不能为空 205：用户手机号不能为空 310：应用不存在 310：场景不存在 310：场景已经存在
	 *            320：词库不存在 601：权限名不能为空 602：权限名已存在 603：权限不存在
	 * @param data
	 *            返回数据
	 * @return
	 */
	protected JSONObject responseBody(String code, Object data) {
		JSONObject res = new JSONObject();
		res.put("code", code);
		res.put("msg", ResultCode.getMsg(code) + "!");
		if (null != data) {
			res.put("data", data);
		}
		return res;
	}


	/**
	 * 自定义描述
	 * @param code
	 * @param msg
	 * @param data
	 * @return
	 */
	protected JSONObject responseBody(String code, Object msg, Object data) {
		JSONObject res = new JSONObject();
		res.put("code", code);
		res.put("msg", msg);
		if (null != data) {
			res.put("data", data);
		}
		return res;
	}

	/**
	 * 初始化数据绑定 1. 将所有传递进来的String进行HTML编码，防止XSS攻击 2. 将字段中Date类型转换为String类型
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		// String类型转换，将所有传递进来的String进行HTML编码，防止XSS攻击
		// 设置接受数据list的大小
		binder.setAutoGrowNestedPaths(true);
		binder.setAutoGrowCollectionLimit(1024 * 10);
		binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				setValue(text == null ? null : StringEscapeUtils.escapeHtml4(text.trim()));
			}

			@Override
			public String getAsText() {
				Object value = getValue();
				return value != null ? value.toString() : "";
			}
		});
		// Date 类型转换
		binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				setValue(DateUtils.parseDate(text));
			}
			// @Override
			// public String getAsText() {
			// Object value = getValue();
			// return value != null ? DateUtils.formatDateTime((Date)value) :
			// "";
			// }
		});
	}

}
