package com.wt.Mybatis.likeSelect;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.wt.MybatisUtil.MybatisUtil;
import com.wt.entities.Classes;
import com.wt.entities.ConditionUser;
import com.wt.entities.Order;
import com.wt.entity.test1.Users;

public class Test {
	
	public static void main(String[] args) {
		SqlSessionFactory factory = MybatisUtil.getFactory();
		
		SqlSession session = factory.openSession();
		
		String statement = "com.wt.Mybatis.likeSelect.userMapper" + ".getUser";
		
		String name = "o";
		
		ConditionUser conditionUser = new ConditionUser("%" + name + "%", 13, 18);
		
		List<Users> Users = session.selectList(statement, conditionUser);
		
		session.close();
		
		System.out.println("Users --- " + Users);
	}

}
