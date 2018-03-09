package com.citic.modules.sys.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.citic.modules.sys.dao.User1Mapper;
import com.citic.modules.sys.entity.UserEntity;

@RestController
public class UserController {

    @Autowired
    private User1Mapper user1Mapper;

 
	
	@RequestMapping("/getUsers")
	public List<UserEntity> getUsers() {
		List<UserEntity> users=user1Mapper.getAll();
		return users;
	}
	
  
 
    
    @RequestMapping(value="/delete/{id}")
    public void delete(@PathVariable("id") Long id) {
        user1Mapper.delete(id);
    }
    
    
}