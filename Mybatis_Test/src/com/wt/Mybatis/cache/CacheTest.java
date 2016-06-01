package com.wt.Mybatis.cache;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;

import com.wt.MybatisUtil.MybatisUtil;
import com.wt.entities.CUser;

/**
 * 
 * @author hohoTT
 * 
 * 测试 Mybatis 缓存
 * 1. 清空一级缓存的情况，一级缓存是一个 session 级的缓存
 * 	   <1>. session.clearCache();
 * 	   <2>. CRID 操作
 * 	   <3>. 使用不同的session对象
 * 
 * 2. 二级缓存的使用 ： 是一个映射文件级的缓存
 * 
 */
public class CacheTest {

	@Test
	public void testCacheOne() {
		
		SqlSessionFactory factory = MybatisUtil.getFactory();
		
		SqlSession session = factory.openSession();
		
		String statement = "com.wt.Mybatis.cache.userMapper" + ".getUser";
		
		CUser cUser1 = session.selectOne(statement, 1);
	
		System.out.println("cuser1 --- " + cUser1);
		

		System.out.println("-------------------------");
		
		// 清空缓存的第一种情况，此时会执行两次sql语句的查询操作
//		session.clearCache();
		
		// 清空缓存的第二种情况，即执行CRUD的操作
		// 此时会执行两次sql语句的查询操作
//		session.update("com.wt.Mybatis.cache.userMapper.updateUser", new CUser(1, "hohoTT", 21));
//		session.commit();
		
		// 清空缓存的第三种情况，即关闭session再进行重新开启，这样就不是同一个session对象
		// 此时会执行两次sql语句的查询操作
		session.close();
		session = factory.openSession();
		
		// 此时使用 Mybatis 中的一级缓存
		// 两次只执行一次 sql 的查询
		CUser cUser2 = session.selectOne(statement, 1);
		
		System.out.println("cuser2 --- " + cUser2);

		session.close();
	}

	
	@Test
	public void testCacheTwo() {
		
		SqlSessionFactory factory = MybatisUtil.getFactory();
		
		SqlSession session1 = factory.openSession();
		SqlSession session2 = factory.openSession();
		
		String statement = "com.wt.Mybatis.cache.userMapper" + ".getUser";
		
		CUser cUser1 = session1.selectOne(statement, 1);
		session1.commit();
		System.out.println(cUser1);
		
		
		CUser cUser2 = session2.selectOne(statement, 1);
		session2.commit();
		System.out.println(cUser2);
		
	}
}







