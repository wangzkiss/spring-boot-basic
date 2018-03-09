package com.citic.modules.sys.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.citic.common.persistence.Page;
import com.citic.common.service.CrudService;
import com.citic.modules.sys.dao.MailTemplateDao;
import com.citic.modules.sys.entity.MailTemplate;

/**
 * 邮件模板Service
 * @author zhangfeng
 * @version 2016-08-12
 */
@Service
@Transactional(readOnly = true)
public class MailTemplateService
        extends CrudService<MailTemplateDao, MailTemplate>
{
    
    public MailTemplate get(String id)
    {
        return super.get(id);
    }
    
    public List<MailTemplate> findList(MailTemplate mailTemplate)
    {
        return super.findList(mailTemplate);
    }
    
    public Page<MailTemplate> findPage(Page<MailTemplate> page,
            MailTemplate mailTemplate)
    {
        return super.findPage(page, mailTemplate);
    }
    
    @Transactional(readOnly = false)
    public void save(MailTemplate mailTemplate)
    {
        super.save(mailTemplate);
    }
    
    @Transactional(readOnly = false)
    public void delete(MailTemplate mailTemplate)
    {
        super.delete(mailTemplate);
    }
    
    @Transactional(readOnly = false)
    public void pitchOn(int mtType, int mtId)
    {
        dao.unPitchOn(mtType, mtId);
        dao.pitchOn(mtId);
    }
}