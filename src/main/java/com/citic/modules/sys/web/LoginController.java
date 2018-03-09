/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.citic.modules.sys.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.citic.common.web.BaseController;
import com.citic.modules.sys.entity.User;
import com.citic.modules.sys.service.SystemService;
import com.citic.modules.sys.utils.UserUtils;

/**
 * 登录Controller
 * 
 * @author jeeplus
 * @version 2013-5-31
 */
@Controller
public class LoginController extends BaseController {

	public static void main(String[] args) {
		String pwd = SystemService.entryptPassword("123456");
		boolean f=SystemService.validatePassword("123456", "95b9131f591474b3c442fd7dc76c09618a5dc7635ac55e0c9618d902");
		System.out.println(f);
		boolean f2=SystemService.validatePassword("123456", "1d3d02a732d95a499a0946922225010831d6219fc49b771c68e2f2be");
		System.out.println(f2);
		System.out.println(pwd);
	}

	/**
	 * 管理登录
	 * 
	 * @throws IOException
	 */
	@RequestMapping(value = "${adminPath}/login")
	@ResponseBody
	public JSONObject login(@RequestParam(required = true) String username,
			@RequestParam(required = true) String password, @RequestParam(required = false) Boolean rememberMe)
			throws IOException {
		JSONObject data = new JSONObject();
		try {
			Subject user = SecurityUtils.getSubject();
			// String pwd=SystemService.entryptPassword(password);
			UsernamePasswordToken tocken = new UsernamePasswordToken(username, password);
			rememberMe = rememberMe == null ? false : rememberMe;
			tocken.setRememberMe(rememberMe);
			user.login(tocken);
			User p = UserUtils.getUser();
			data.put("success", true);
			data.put("data", p);
			data.put("msg", "登录成功！");
			data.put("sid", UserUtils.getPrincipal().getSessionid());
		} catch (UnknownAccountException uae) {
			data.put("success", false);
			data.put("msg", "用户不存在！");
		} catch (IncorrectCredentialsException ice) {
			data.put("success", false);
			data.put("msg", "用户密码错误！");
		} catch (LockedAccountException lae) {
			data.put("success", false);
			data.put("msg", "用户已锁定！");
		} catch (ExcessiveAttemptsException eae) {
			data.put("success", false);
			data.put("msg", "用户不允许多次登录！");
		} catch (Exception e) {
			e.printStackTrace();
			data.put("success", false);
			data.put("data", e.getMessage());
			data.put("msg", "登录失败！");
		}
		return data;
	}

	/**
	 * 管理登录
	 * 
	 * @throws IOException
	 */
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

	/**
	 * 首页
	 * 
	 * @throws IOException
	 */
	@RequestMapping(value = "${adminPath}/home")
	public String home(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {

		return "modules/sys/sysHome";

	}
}
