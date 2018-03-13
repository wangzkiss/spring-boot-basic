package com.citic.shiro;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import com.alibaba.fastjson.JSONObject;
import com.citic.common.config.Global;
import com.citic.modules.sys.security.SystemAuthorizingRealm.Principal;
import com.citic.modules.sys.utils.UserUtils;

@Configuration
public class CoreFilter implements Filter {
	private Logger log=LoggerFactory.getLogger(getClass());
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		 
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request=(HttpServletRequest) req;
		HttpServletResponse response=(HttpServletResponse)res;
		
		String origin=request.getHeader("Origin");
		String webUrl="*";
		if(null!=origin&& origin.contains(":"))
		{
			log.info(origin);
			origin=origin.substring(0, origin.lastIndexOf(":"));
			webUrl=origin+":"+Global.getConfig("web_port");
		}
		response.setHeader("Access-Control-Allow-Origin", webUrl);
		response.setHeader("Access-Control-Allow-Credentials","true");
		response.setHeader("Access-Control-Allow-Headers", " Origin, X-Requested-With, Content-Type, Accept, Connection, User-Agent, Cookie");
		String url=request.getRequestURI();
		log.info(url);
		if(url.endsWith("/login") || url.contains("job")){
			chain.doFilter(request, response);
		 }else{
			 Principal p= UserUtils.getPrincipal();
			 if (p==null){
				 JSONObject data=new JSONObject();
				 data.put("code", "102");
				 data.put("msg", "未授权登录！");
				 response.setCharacterEncoding("utf-8");
				 response.getWriter().print(data.toJSONString());
			 }else{
				 chain.doFilter(request, response);
			 }
		 }
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
