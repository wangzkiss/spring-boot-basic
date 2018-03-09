package com.citic.modules.sys.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.citic.common.persistence.Page;
import com.citic.common.web.BaseController;
import com.citic.modules.sys.entity.User;
import com.citic.modules.sys.service.SystemService;

@RestController
@RequestMapping(value = "${adminPath}/sys/user")
public class UserController extends BaseController{

    @Autowired
    private SystemService user1Mapper;

 
	
	@RequestMapping("/list")
	@ResponseBody
	public JSONObject list(User user, HttpServletRequest request, HttpServletResponse response) {
		Page<User> page = user1Mapper.findUser(new Page<User>(request, response), user); 
		return responseBody(000, page);
	}
	
  
 
    
    
    
}