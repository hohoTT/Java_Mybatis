package com.wt.entity.CRUD_Test;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;

import com.wt.MybatisUtil.MybatisUtil;
import com.wt.entity.test1.Users;

public class CRUD_Test {
	
	@Test
	public void testAdd(){
		SqlSessionFactory factory = MybatisUtil.getFactory();
		
		// 默认的提交为手动的提交，即按照普通的factory.openSession()做的话
		// 插入的数据并没有进行数据表的插入操作，需要进行手动的打开
		SqlSession session = factory.openSession();
		
		String statement = "com.wt.entity.CRUD_Test.usersMapper" + ".addUser";
		
		// 表示的为受影响的行数，此时执行时显示的结果应该为 1 
		int insert = session.insert(statement, new Users(-1, "hohoTT", 21));
		
		// 手动设置session的提交，即向数据表中执行一条插入的操作
		session.commit();
		
		session.close();
		
		System.out.println("insert ---- " + insert);
	} 
	
	@Test
	public void testUpdate(){
		SqlSessionFactory factory = MybatisUtil.getFactory();
		
		// 默认的提交为手动的提交，即按照普通的factory.openSession()做的话
		// 插入的数据并没有进行数据表的插入操作，需要进行手动的打开
		
		// 第二种方法，设置自动提交为true
		SqlSession session = factory.openSession(true);
		
		String statement = "com.wt.entity.CRUD_Test.usersMapper" + ".updateUser";

		int update = session.update(statement, new Users(4, "hoho", 25));
		
		// 第一种方法：手动设置session的提交，即向数据表中执行一条插入的操作
//		session.commit();
		
		session.close();
		
		System.out.println("update --- " + update);
	} 
	
	@Test
	public void testDelete(){
		SqlSessionFactory factory = MybatisUtil.getFactory();
		
		// 默认的提交为手动的提交，即按照普通的factory.openSession()做的话
		// 插入的数据并没有进行数据表的插入操作，需要进行手动的打开
		
		// 第二种方法，设置自动提交为true
		SqlSession session = factory.openSession(true);
		
		String statement = "com.wt.entity.CRUD_Test.usersMapper" + ".deleteUser";

		int delete = session.delete(statement, 4);
				
		// 第一种方法：手动设置session的提交，即向数据表中执行一条插入的操作
//		session.commit();
		
		session.close();
		
		System.out.println("delete --- " + delete);
	} 

	@Test
	public void testGetUser(){
		SqlSessionFactory factory = MybatisUtil.getFactory();
		
		// 默认的提交为手动的提交，即按照普通的factory.openSession()做的话
		// 插入的数据并没有进行数据表的插入操作，需要进行手动的打开
		
		// 第二种方法，设置自动提交为true
		SqlSession session = factory.openSession(true);
		
		String statement = "com.wt.entity.CRUD_Test.usersMapper" + ".getUser";

		Users users = session.selectOne(statement, 1);
		
		session.close();
		
		System.out.println(users);
	} 
	
}
