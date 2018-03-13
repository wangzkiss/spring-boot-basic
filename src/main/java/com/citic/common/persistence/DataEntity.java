package com.citic.common.persistence;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

import com.citic.common.utils.IdGen;
import com.citic.modules.sys.entity.User;
import com.citic.modules.sys.utils.UserUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 数据Entity类
 * @author jeeplus
 * @version 2014-05-16
 */
public abstract class DataEntity<T> extends BaseEntity<T>
{
    
    private static final long serialVersionUID = 1L;
    
    protected User createBy; // 创建者
    
    protected Date createTime; // 创建日期
    
    protected User updateBy; // 更新者
    
    protected Date updateTime; // 更新日期
    
    protected String delFlag; // 删除标记（0：正常；1：删除；2：审核）
    
    private int enableFlag;//0 未启用  1 启用  
    
    protected int isAdmin = 0;
    
    @JsonIgnore
    public int getIsAdmin()
    {
         
        return isAdmin;
    }
    
    public void setIsAdmin(int isAdmin)
    {
        this.isAdmin = isAdmin;
    }
    
    @JsonIgnore
    public int getEnableFlag() {
		return enableFlag;
	}

	public void setEnableFlag(int enableFlag) {
		this.enableFlag = enableFlag;
	}

	public DataEntity()
    {
        super();
        this.delFlag = DEL_FLAG_NORMAL;
    }
    
    public DataEntity(String id)
    {
        super(id);
    }
    
    /**
     * 插入之前执行方法，需要手动调用
     */
    @JsonIgnore
    @Override
    public void preInsert()
    {
        // 不限制ID为UUID，调用setIsNewRecord()使用自定义ID
        if (!this.isNewRecord)
        {
            setId(IdGen.uuid());
        }
        User user =UserUtils.getUser();
        if (StringUtils.isNotBlank(user.getId()))
        {
            this.updateBy = user;
            this.createBy = user;
        }
        this.updateTime = new Date();
        this.createTime = this.updateTime;
    }
    
    /**
     * 更新之前执行方法，需要手动调用
     */
    @Override
    public void preUpdate()
    {
        User user =new User();// UserUtils.getUser();
        if (StringUtils.isNotBlank(user.getId()))
        {
            this.updateBy = user;
        }
        this.updateTime = new Date();
    }
    
    @JsonIgnore
    public User getCreateBy()
    {
        return createBy;
    }
    
    public void setCreateBy(User createBy)
    {
        this.createBy = createBy;
    }
    
    @JsonIgnore
    public User getUpdateBy()
    {
        return updateBy;
    }
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public void setUpdateBy(User updateBy)
    {
        this.updateBy = updateBy;
    }
    
    @JsonIgnore
    @Length(min = 1, max = 1)
    public String getDelFlag()
    {
        return delFlag;
    }
    
    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }
}
