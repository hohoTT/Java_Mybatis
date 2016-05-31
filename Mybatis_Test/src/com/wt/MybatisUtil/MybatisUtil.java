package com.wt.MybatisUtil;

import java.io.InputStream;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.wt.entity.test1.Test;

public class MybatisUtil {

	public static SqlSessionFactory getFactory() {
		// 配置文件的名字
		String resource = "conf.xml";
		
		// 使用类加载器进行文件的加载
		InputStream inputStream =  Test.class.getClassLoader().getResourceAsStream(resource);
		
		SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);
	
		return factory;
	}
	
}
