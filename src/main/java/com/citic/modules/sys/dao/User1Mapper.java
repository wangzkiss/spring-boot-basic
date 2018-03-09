package com.citic.modules.sys.dao;

import java.util.List;

import com.citic.annotation.MyBatisDao;
import com.citic.modules.sys.entity.UserEntity;
@MyBatisDao
public interface User1Mapper {
	
	List<UserEntity> getAll();
	
	UserEntity getOne(Long id);

	void insert(UserEntity user);

	void update(UserEntity user);

	void delete(Long id);

}