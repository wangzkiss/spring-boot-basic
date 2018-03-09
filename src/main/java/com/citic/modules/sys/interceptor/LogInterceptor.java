/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.citic.modules.sys.interceptor;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.NamedThreadLocal;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.citic.common.config.Global;
import com.citic.common.service.BaseService;
import com.citic.common.utils.DateUtils;
import com.citic.modules.sys.utils.LogUtils;

/**
 * 日志拦截器
 * @author jeeplus
 * @version 2014-8-19
 */
public class LogInterceptor extends BaseService implements HandlerInterceptor
{
    
    private static final ThreadLocal<Long> startTimeThreadLocal = new NamedThreadLocal<Long>(
            "ThreadLocal StartTime");
    
    private boolean isLog(String url)
    {
        String keys = Global.getConfig("not_print_log_url");
        String keyArray[] = keys.split(",");
        boolean isLog = true;
        for (String key : keyArray)
        {
            if (url.endsWith(key))
            {
                isLog = false;
                break;
            }
        }
        return isLog;
    }
    
    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler) throws Exception
    {
        if (logger.isDebugEnabled() && isLog(request.getRequestURI()))
        {
            long beginTime = System.currentTimeMillis();//1、开始时间  
            startTimeThreadLocal.set(beginTime); //线程绑定变量（该数据只有当前请求的线程可见）  
            logger.debug("开始计时: {}  URI: {}", new SimpleDateFormat(
                    "hh:mm:ss.SSS").format(beginTime), request.getRequestURI());
        }
        return true;
    }
    
    @Override
    public void postHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception
    {
        if (modelAndView != null && isLog(request.getRequestURI()))
        {
            logger.info("ViewName: " + modelAndView.getViewName());
        }
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request,
            HttpServletResponse response, Object handler, Exception ex)
            throws Exception
    {
        if (isLog(request.getRequestURI()))
        {
            // 保存日志
            LogUtils.saveLog(request, handler, ex, null);
            // 打印JVM信息。
            if (logger.isDebugEnabled())
            {
                long beginTime = startTimeThreadLocal.get();//得到线程绑定的局部变量（开始时间）  
                long endTime = System.currentTimeMillis(); //2、结束时间  
                logger.debug("计时结束：{}  耗时：{}  URI: {}  最大内存: {}m  已分配内存: {}m  已分配内存中的剩余空间: {}m  最大可用内存: {}m",
                        new SimpleDateFormat("hh:mm:ss.SSS").format(endTime),
                        DateUtils.formatDateTime(endTime - beginTime),
                        request.getRequestURI(),
                        Runtime.getRuntime().maxMemory() / 1024 / 1024,
                        Runtime.getRuntime().totalMemory() / 1024 / 1024,
                        Runtime.getRuntime().freeMemory() / 1024 / 1024,
                        (Runtime.getRuntime().maxMemory()
                                - Runtime.getRuntime().totalMemory() + Runtime.getRuntime()
                                .freeMemory()) / 1024 / 1024);
            }
            
        }
        
    }
    
}
