package com.wt.entity.test2;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.wt.MybatisUtil.MybatisUtil;
import com.wt.entities.Order;

public class Test {
	
	public static void main(String[] args) {
		SqlSessionFactory factory = MybatisUtil.getFactory();
		
		SqlSession session = factory.openSession();
		
		String statement = "com.wt.entity.test2.orderMapper" + ".getOrder";
		
		Order order = session.selectOne(statement, 2);
		
		System.out.println("order --- " + order);
	}

}
