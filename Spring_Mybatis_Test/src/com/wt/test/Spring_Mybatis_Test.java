package com.wt.test;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.wt.entities.User;
import com.wt.mapper.UserMapper;

@RunWith(SpringJUnit4ClassRunner.class) // 使用 Spring 的测试框架
@ContextConfiguration("/beans.xml") // 加载 Spring 的配置文件 beans.xml
public class Spring_Mybatis_Test {
	
	@Autowired
	private UserMapper userMapper;
	
	@Test
	public void testAdd(){
		
		User user = new User(-1, "testA", new Date(), 10006);
		
		userMapper.save(user);
	}
	
	
}
