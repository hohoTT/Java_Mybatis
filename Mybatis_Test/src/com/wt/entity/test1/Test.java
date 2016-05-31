package com.wt.entity.test1;

import java.io.InputStream;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class Test {
	
	public static void main(String[] args) {
		
		// 配置文件的名字
		String resource = "conf.xml";
		
		// 使用类加载器进行文件的加载
		InputStream inputStream =  Test.class.getClassLoader().getResourceAsStream(resource);
		
		SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);
		
		SqlSession session = factory.openSession();
		
		String statement = "com.wt.entity.test1.usersMapper" + ".getUser";
		
		Users users = session.selectOne(statement, 2);
	
		System.out.println(users);
	}

}
