package com.citic.mapper;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.citic.modules.sys.entity.User;
import com.citic.modules.sys.service.SystemService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class User1MapperTest {

	@Autowired
	private SystemService userMapper;
 
	@Test
	public void testQuery() throws Exception {
		List<User> users = userMapper.findUser(new User());
		if(users==null || users.size()==0){
			System.out.println("is null");
		}else{
			System.out.println(users.size());
		}
	}
	
}