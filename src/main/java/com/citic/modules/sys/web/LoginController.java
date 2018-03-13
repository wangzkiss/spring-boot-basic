/**
 * Copyright &copy; "201"5-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.citic.modules.sys.web;

import java.io.IOException;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.citic.annotation.Menus;
import com.citic.common.web.BaseController;
import com.citic.modules.sys.entity.User;
import com.citic.modules.sys.utils.UserUtils;

/**
 * 登录Controller
 * 
 * @author jeeplus
 * @version "201"3-5-31
 */
@Controller
public class LoginController extends BaseController {

 
	/**
	 * 管理登录
	 * 
	 * @throws IOException
	 */
	@Menus("系统管理-用户登录")
	@RequestMapping(value = "${adminPath}/login")
	@ResponseBody
	public JSONObject login(@RequestParam(required = true) String username,
			@RequestParam(required = true) String password, @RequestParam(required = false) Boolean rememberMe)
			throws IOException {
		try {
			Subject user = SecurityUtils.getSubject();
			// String pwd=SystemService.entryptPassword(password);
			UsernamePasswordToken tocken = new UsernamePasswordToken(username, password);
			rememberMe = rememberMe == null ? false : rememberMe;
			tocken.setRememberMe(rememberMe);
			user.login(tocken);
			User p = UserUtils.getUser();
			return responseBody("000", p);
		} catch (UnknownAccountException uae) {
			return responseBody("201", uae.getMessage());
		} catch (IncorrectCredentialsException ice) {
			return responseBody("103", ice.getMessage());
		} catch (LockedAccountException lae) {
			return responseBody("103", lae.getMessage());
		} catch (ExcessiveAttemptsException eae) {
			return responseBody("103", eae.getMessage());
		} catch (Exception e) {
			return responseBody("001", e.getMessage());
		}
	}

	/**
	 * 管理退出
	 * 
	 * @throws IOException
	 */
	@Menus("系统管理-用户退出")
	@RequestMapping(value = "${adminPath}/logout")
	@ResponseBody
	public JSONObject logout() throws IOException {
		JSONObject data = new JSONObject();
		Subject user = SecurityUtils.getSubject();
		user.logout();
		data.put("success", true);
		data.put("msg", "成功退出！");
		return data;
	}

}
