/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.citic.modules.sys.security;

import java.io.Serializable;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.citic.common.config.Global;
import com.citic.common.utils.Encodes;
import com.citic.common.utils.SpringContextHolder;
import com.citic.modules.sys.entity.User;
import com.citic.modules.sys.security.SystemAuthorizingRealm.Principal;
import com.citic.modules.sys.service.SystemService;
import com.citic.modules.sys.utils.UserUtils;

/**
 * 系统安全认证实现类
 * @author jeeplus
 * @version 2014-7-5
 */
@Service
//@DependsOn({"userDao","roleDao","menuDao"})
public class SystemAuthorizingRealm extends AuthorizingRealm {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private SystemService systemService;
	
	 
	/**
	 * 认证回调函数, 登录时调用
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		int onlineNum=getSystemService().getSessionDao().getActiveSessions(false).size();
		String onlineConf=Global.getConfig("online");
		if(!StringUtils.isEmpty(onlineConf))
		{
			int n=Integer.parseInt(onlineConf);
			if(onlineNum>n){
				throw new AuthenticationException("在线人数超过"+n+"人，请稍后登录！");
			}
		}
		 String pwd=String.valueOf(token.getPassword());
		 String uname=token.getUsername();
		logger.info("登录");
		// 校验用户名密码
		User user = getSystemService().getUserByLoginName(uname);
		if (user != null) {
			if(user.getLocked()) {
	            throw new LockedAccountException(); //帐号锁定
	        }
			if(SystemService.validatePassword(pwd,user.getPassword())){
				 AuthenticationInfo authcInfo = new SimpleAuthenticationInfo(new Principal(user, false),pwd, getName());
				 return authcInfo;
			}else{
				 throw new IncorrectCredentialsException();//没找到帐号
			}
		} else {
			  throw new UnknownAccountException();//没找到帐号
		}
	}

	/**
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
			 
			// 添加用户权限
			info.addStringPermission("user");
			// 添加用户角色信息
			 
			return info;
		
	}


	/**
	 * 获取系统业务对象
	 */
	public SystemService getSystemService() {
		if (systemService == null){
			systemService = SpringContextHolder.getBean(SystemService.class);
		}
		return systemService;
	}
	
	/**
	 * 授权用户信息
	 */
	public static class Principal implements Serializable {

		private static final long serialVersionUID = 1L;
		
		private String id; // 编号
		private String loginName; // 登录名
		private String name; // 姓名
		private boolean mobileLogin; // 是否手机登录
		
//		private Map<String, Object> cacheMap;

		public Principal(User user, boolean mobileLogin) {
			this.id = user.getId();
			this.loginName = user.getLoginName();
			this.name = user.getName();
			this.mobileLogin = mobileLogin;
		}

		public String getId() {
			return id;
		}

		public String getLoginName() {
			return loginName;
		}

		public String getName() {
			return name;
		}

		public boolean isMobileLogin() {
			return mobileLogin;
		}

		/**
		 * 获取SESSIONID
		 */
		public String getSessionid() {
			try{
				return (String) UserUtils.getSession().getId();
			}catch (Exception e) {
				return "";
			}
		}
		
		@Override
		public String toString() {
			return id;
		}

	}
}
