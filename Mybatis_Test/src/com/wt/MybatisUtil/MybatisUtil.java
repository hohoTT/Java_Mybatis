package com.wt.MybatisUtil;

import java.io.InputStream;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.wt.entity.test1.Test;

public class MybatisUtil {

	public static SqlSessionFactory getFactory() {
		// �����ļ�������
		String resource = "conf.xml";
		
		// ʹ��������������ļ��ļ���
		InputStream inputStream =  Test.class.getClassLoader().getResourceAsStream(resource);
		
		SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);
	
		return factory;
	}
	
}
