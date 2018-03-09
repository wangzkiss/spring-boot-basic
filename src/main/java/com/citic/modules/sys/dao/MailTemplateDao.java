package com.citic.modules.sys.dao;

import org.apache.ibatis.annotations.Param;

import com.citic.annotation.MyBatisDao;
import com.citic.common.persistence.CrudDao;
import com.citic.modules.sys.entity.MailTemplate;

/**
 * 邮件模板DAO接口
 * @author zhangfeng
 * @version 2016-08-12
 */
@MyBatisDao
public interface MailTemplateDao extends CrudDao<MailTemplate>
{
    public int pitchOn(int mtId);
    
    public int unPitchOn(@Param("mtType") int mtType, @Param("mtId") int mtId);
}