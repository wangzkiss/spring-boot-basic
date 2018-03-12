/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.citic.modules.sys.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.citic.annotation.Menus;
import com.citic.common.persistence.Page;
import com.citic.common.web.BaseController;
import com.citic.modules.sys.entity.Log;
import com.citic.modules.sys.service.LogService;

/**
 * 日志Controller
 * @author jeeplus
 * @version 2013-6-2
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/log")
public class LogController extends BaseController {

	@Autowired
	private LogService logService;
	
	@Menus("系统管理-操作日志-列表")
	@RequestMapping(value = {"list", ""})
	@ResponseBody
	public JSONObject list(Log log, HttpServletRequest request, HttpServletResponse response) {
        Page<Log> page = logService.findPage(new Page<Log>(request, response), log); 
    	return responseBody(000, page);
	}

	/**
	 * 批量删除
	 */
	@Menus("系统管理-操作日志-删除")
	@RequestMapping(value = "delete")
	@ResponseBody
	public JSONObject deleteAll(@RequestParam(required = true) String  id) {
		String idArray[] =id.split(",");
		for(String i : idArray){
			logService.delete(logService.get(i));
		}
		return responseBody(000,id);
	}
	
	/**
	 * 全部清空
	 */
	@Menus("系统管理-操作日志-清空")
	@RequestMapping(value = "empty")
	@ResponseBody
	public JSONObject   empty() {
		logService.empty();
		return responseBody(000,"");
	}
}
