package com.citic.modules.sys.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.citic.annotation.Menus;
import com.citic.common.persistence.Page;
import com.citic.common.web.BaseController;
import com.citic.modules.sys.entity.User;
import com.citic.modules.sys.service.SystemService;

@RestController
@RequestMapping(value = "${adminPath}/sys/user")
public class UserController extends BaseController{

    @Autowired
    private SystemService user1Mapper;

 
	/**
	 * 用户列表
	 * @param user
	 * @param request
	 * @param response
	 * @return
	 */
    @Menus("系统管理-用户管理-列表")
	@RequestMapping("/list")
	@ResponseBody
	public JSONObject list(User user, HttpServletRequest request, HttpServletResponse response) {
		Page<User> page = user1Mapper.findUser(new Page<User>(request, response), user); 
		return responseBody(000, page);
	}
  
	/**
	 * 用用户添加
	     officeName;	// 归属部门
		 loginName;// 登录名
		 password;// 密码
		 name;	// 姓名
		 userType;// 用户类型
		 no;		// 工号
		 email;	// 邮箱
		 phone;	// 电话
	 * @return json
	 */
    @Menus("系统管理-用户管理-修改/添加")
	@RequestMapping("/save")
	@ResponseBody
	public JSONObject save(
			@RequestParam(required = true) String officeName,
			@RequestParam(required = true) String loginName,
			@RequestParam(required = true) String password,
			@RequestParam(required = true) String name,
			@RequestParam(required = true) String userType,
			@RequestParam(required = false) String no,
			@RequestParam(required = false) String email,
			@RequestParam(required = false) String phone) {
		User user=new User(officeName, loginName, password, no, name, email, phone, userType);
		user1Mapper.saveUser(user);
		return responseBody(000,user);
	}
	/**
	 * 用户查看
	 * @param id 用户id
	 * @return json
	 */
    @Menus("系统管理-用户管理-查看")
	@RequestMapping("/read")
	@ResponseBody
	public JSONObject read(
			@RequestParam(required = true) String id
			) {
		User user=user1Mapper.getUser(id);
		return responseBody(000,user);
	}
    
	/**
	 * 用户查看
	 * @param id 用户id
	 * @return json
	 */
    @Menus("系统管理-用户管理-删除")
	@RequestMapping("/delete")
	@ResponseBody
	public JSONObject delete(
			@RequestParam(required = true) String id
			) {
		String idArray[] =id.split(",");
		for(String userid : idArray){
			user1Mapper.deleteUser(user1Mapper.getUser(userid));
		}
		return responseBody(000,id);
	}
}