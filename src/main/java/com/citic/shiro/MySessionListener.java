package com.citic.shiro;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MySessionListener implements SessionListener {

 
	private Logger log=LoggerFactory.getLogger(getClass());
	@Override
	public void onExpiration(Session arg0) {
		log.info("回话过期！");
	}

	@Override
	public void onStart(Session arg0) {
		log.info("回话创建！");
	}

	@Override
	public void onStop(Session arg0) {
		log.info("回话退出！");
	}

}
