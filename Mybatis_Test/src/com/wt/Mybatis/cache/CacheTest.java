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
 * ���� Mybatis ����
 * 1. ���һ������������һ��������һ�� session ���Ļ���
 * 	   <1>. session.clearCache();
 * 	   <2>. CRID ����
 * 	   <3>. ʹ�ò�ͬ��session����
 * 
 * 2. ���������ʹ�� �� ��һ��ӳ���ļ����Ļ���
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
		
		// ��ջ���ĵ�һ���������ʱ��ִ������sql���Ĳ�ѯ����
//		session.clearCache();
		
		// ��ջ���ĵڶ����������ִ��CRUD�Ĳ���
		// ��ʱ��ִ������sql���Ĳ�ѯ����
//		session.update("com.wt.Mybatis.cache.userMapper.updateUser", new CUser(1, "hohoTT", 21));
//		session.commit();
		
		// ��ջ���ĵ�������������ر�session�ٽ������¿����������Ͳ���ͬһ��session����
		// ��ʱ��ִ������sql���Ĳ�ѯ����
		session.close();
		session = factory.openSession();
		
		// ��ʱʹ�� Mybatis �е�һ������
		// ����ִֻ��һ�� sql �Ĳ�ѯ
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







