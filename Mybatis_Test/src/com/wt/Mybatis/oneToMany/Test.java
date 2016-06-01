package com.wt.Mybatis.oneToMany;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.wt.MybatisUtil.MybatisUtil;
import com.wt.entities.Classes;
import com.wt.entities.Order;

public class Test {
	
	public static void main(String[] args) {
		SqlSessionFactory factory = MybatisUtil.getFactory();
		
		SqlSession session = factory.openSession();
		
//		String statement = "com.wt.Mybatis.oneToMany.ClassMapper" + ".getClass";
//		
//		Classes classes = session.selectOne(statement, 1);

		String statement = "com.wt.Mybatis.oneToMany.ClassMapper" + ".getClass2";
		
		Classes classes = session.selectOne(statement, 1);
		
		session.close();
		
		System.out.println("classes --- " + classes);
	}

}
