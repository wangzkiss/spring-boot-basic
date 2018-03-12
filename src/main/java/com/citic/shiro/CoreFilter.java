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
		String url=request.getRequestURI();
		log.info(url);
		if(url.endsWith("/login") || url.contains("job")){
			chain.doFilter(request, response);
		 }else{
			 Principal p= UserUtils.getPrincipal();
			 if (p==null){
				 JSONObject data=new JSONObject();
				 data.put("code", 102);
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
