/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.citic.modules.sys.dao;


import com.citic.annotation.MyBatisDao;
import com.citic.common.persistence.CrudDao;
import com.citic.modules.sys.entity.Log;

/**
 * 日志DAO接口
 * @author jeeplus
 * @version 2014-05-16
 */
@MyBatisDao
public interface LogDao extends CrudDao<Log> {

	public void empty();
}
