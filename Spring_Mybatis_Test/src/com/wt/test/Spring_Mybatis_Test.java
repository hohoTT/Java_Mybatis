package com.wt.test;

import java.util.Date;
import java.util.List;

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
	
	@Test
	public void testUpdate(){
		
		User user = new User(1, "hohoTT", new Date(), 666);
		
		userMapper.update(user);
	}
	
	@Test
	public void testDelete(){
		userMapper.delete(2);
	}
	
	@Test
	public void testFindById(){
		User user = userMapper.findById(1);
		System.out.println(user);
	}
	
	@Test
	public void testFindAll(){
		List<User> users = userMapper.findAll();
		System.out.println(users);
	}
}
