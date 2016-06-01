package com.wt.Mybatis.storedProcedure;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.wt.MybatisUtil.MybatisUtil;
import com.wt.entities.Classes;
import com.wt.entities.Order;

public class Test {
	
	public static void main(String[] args) {
		SqlSessionFactory factory = MybatisUtil.getFactory();
		
		SqlSession session = factory.openSession();
		
		String statement = "com.wt.Mybatis.storedProcedure.userMapper" + ".getUserCount";
		
		Map<String, Integer> parameterMap = new HashMap<String, Integer>();

		parameterMap.put("sexId", 1);
		parameterMap.put("userCount", -1);
		
		session.selectOne(statement, parameterMap);
		
		Integer result = parameterMap.get("userCount");
		
		session.close();
		
		System.out.println("result --- " + result);
	}

}
