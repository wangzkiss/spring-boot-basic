package com.citic.shiro;

import java.util.Set;

import javax.servlet.DispatcherType;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import com.citic.common.config.Global;
import com.citic.common.security.shiro.session.CacheSessionDAO;
import com.citic.common.security.shiro.session.SessionManager;
import com.citic.common.utils.IdGen;
import com.citic.modules.sys.security.SystemAuthorizingRealm;
import com.google.common.collect.Sets;

@Configuration
public class ShiroConfig {

	@Bean
	public FilterRegistrationBean reguistFilter() {
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		filterRegistrationBean.setFilter(new DelegatingFilterProxy("shirofilter"));
		filterRegistrationBean.setEnabled(true);
		filterRegistrationBean.addUrlPatterns("/*");
		filterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST);
		return filterRegistrationBean;
	}

	@Bean("shirofilter")
	public ShiroFilterFactoryBean shirofilter(SecurityManager securityManager) {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean.setSecurityManager(securityManager);
		return shiroFilterFactoryBean;
	}

	@Bean("securityManager")
	public SecurityManager securityManager() {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setRealm(new SystemAuthorizingRealm());
		securityManager.setCacheManager(cacheManager());
		securityManager.setSessionManager(sessionManager());
		return securityManager;
	}

	private SessionManager sessionManager() {
		SessionManager sessionManager = new SessionManager();
		sessionManager.setCacheManager(cacheManager());
		String timeout = Global.getConfig("session.sessionTimeout");
		timeout = StringUtils.isEmpty(timeout) ? "1800000" : timeout;
		String timeoutclean = Global.getConfig("session.sessionTimeoutClean");
		timeoutclean = StringUtils.isEmpty(timeoutclean) ?"1800000" : timeoutclean;
		sessionManager.setGlobalSessionTimeout(Long.parseLong(timeout));
		sessionManager.setSessionValidationInterval(Long.parseLong(timeout));
		sessionManager.setDeleteInvalidSessions(false);
		sessionManager.setSessionValidationSchedulerEnabled(false);
		sessionManager.setSessionIdCookie(new SimpleCookie("citic.session.id"));
		sessionManager.setSessionIdCookieEnabled(true);
		sessionManager.setSessionDAO(sessionDAO());
		Set<SessionListener>  listeners=Sets.newHashSet();
		listeners.add(new MySessionListener());
		sessionManager.setSessionListeners(listeners);
		return sessionManager;
	}

	 
	@Bean("sessionDAO")
	public SessionDAO sessionDAO() {
		CacheSessionDAO cacheSessionDAO=new CacheSessionDAO();
		cacheSessionDAO.setCacheManager(cacheManager());
		cacheSessionDAO.setSessionIdGenerator(new IdGen());
		cacheSessionDAO.setActiveSessionsCacheName("activeSessionsCache");
		return cacheSessionDAO;
	}

	@Bean("cacheManager")
	public  CacheManager cacheManager() { // TODO Auto-generated method stub
		EhCacheManager cacheManager =new EhCacheManager();
		cacheManager.setCacheManagerConfigFile("classpath:ehcache-local.xml");
		return cacheManager;
	}

 

}
