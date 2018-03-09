/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.citic.modules.sys.dao;

import java.util.List;

import com.citic.annotation.MyBatisDao;
import com.citic.common.persistence.CrudDao;
import com.citic.modules.sys.entity.Dict;

/**
 * 字典DAO接口
 * @author jeeplus
 * @version 2014-05-16
 */
@MyBatisDao
public interface DictDao extends CrudDao<Dict> {

	public List<String> findTypeList(Dict dict);
    public List<Dict> findMetaTypes();
	
}
