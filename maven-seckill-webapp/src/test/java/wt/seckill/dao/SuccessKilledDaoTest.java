package wt.seckill.dao;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import wt.seckill.entity.Seckill;
import wt.seckill.entity.SuccessKilled;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessKilledDaoTest {
	
	// 依赖注入的注释
	@Resource
	private SuccessKilledDao successKilledDao;
	
	@Test
	public void testInsertSuccessKilled() throws Exception {
		/*
		 *  第一次执行： insertCount = 1
		 *  第一次执行： insertCount = 0
		 */
		
		long id = 1001L;
		long phone = 18362597866L;
		
		int insertCount = successKilledDao.insertSuccessKilled(id, phone);
	
		System.out.println("insertCount --- " + insertCount);
	}

	@Test
	public void testQueryByIdWithSeckill() throws Exception {
		long id = 1000L;
		long phone = 18362597845L;
		
		SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(id, phone);
	
		System.out.println(successKilled);
		System.out.println(successKilled.getSeckill());
	}
}
