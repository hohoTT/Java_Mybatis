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
		
		// Ĭ�ϵ��ύΪ�ֶ����ύ����������ͨ��factory.openSession()���Ļ�
		// ��������ݲ�û�н������ݱ�Ĳ����������Ҫ�����ֶ��Ĵ�
		SqlSession session = factory.openSession();
		
		String statement = "com.wt.entity.CRUD_Test.usersMapper" + ".addUser";
		
		// ��ʾ��Ϊ��Ӱ�����������ʱִ��ʱ��ʾ�Ľ��Ӧ��Ϊ 1 
		int insert = session.insert(statement, new Users(-1, "hohoTT", 21));
		
		// �ֶ�����session���ύ���������ݱ���ִ��һ������Ĳ���
		session.commit();
		
		session.close();
		
		System.out.println("insert ---- " + insert);
	} 
	
	@Test
	public void testUpdate(){
		SqlSessionFactory factory = MybatisUtil.getFactory();
		
		// Ĭ�ϵ��ύΪ�ֶ����ύ����������ͨ��factory.openSession()���Ļ�
		// ��������ݲ�û�н������ݱ�Ĳ����������Ҫ�����ֶ��Ĵ�
		
		// �ڶ��ַ����������Զ��ύΪtrue
		SqlSession session = factory.openSession(true);
		
		String statement = "com.wt.entity.CRUD_Test.usersMapper" + ".updateUser";

		int update = session.update(statement, new Users(4, "hoho", 25));
		
		// ��һ�ַ������ֶ�����session���ύ���������ݱ���ִ��һ������Ĳ���
//		session.commit();
		
		session.close();
		
		System.out.println("update --- " + update);
	} 
	
	@Test
	public void testDelete(){
		SqlSessionFactory factory = MybatisUtil.getFactory();
		
		// Ĭ�ϵ��ύΪ�ֶ����ύ����������ͨ��factory.openSession()���Ļ�
		// ��������ݲ�û�н������ݱ�Ĳ����������Ҫ�����ֶ��Ĵ�
		
		// �ڶ��ַ����������Զ��ύΪtrue
		SqlSession session = factory.openSession(true);
		
		String statement = "com.wt.entity.CRUD_Test.usersMapper" + ".deleteUser";

		int delete = session.delete(statement, 4);
				
		// ��һ�ַ������ֶ�����session���ύ���������ݱ���ִ��һ������Ĳ���
//		session.commit();
		
		session.close();
		
		System.out.println("delete --- " + delete);
	} 

	@Test
	public void testGetUser(){
		SqlSessionFactory factory = MybatisUtil.getFactory();
		
		// Ĭ�ϵ��ύΪ�ֶ����ύ����������ͨ��factory.openSession()���Ļ�
		// ��������ݲ�û�н������ݱ�Ĳ����������Ҫ�����ֶ��Ĵ�
		
		// �ڶ��ַ����������Զ��ύΪtrue
		SqlSession session = factory.openSession(true);
		
		String statement = "com.wt.entity.CRUD_Test.usersMapper" + ".getUser";

		Users users = session.selectOne(statement, 1);
		
		session.close();
		
		System.out.println(users);
	} 
	
}
