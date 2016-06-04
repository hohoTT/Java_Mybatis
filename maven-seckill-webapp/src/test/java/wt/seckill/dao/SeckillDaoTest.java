package wt.seckill.dao;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import wt.seckill.entity.Seckill;

/**
 * 
 * @author hohoTT
 * ���� spring �� junit �����ϣ�junit ����ʱ����springIOC���� -- @RunWith
 * spring-test,junit
 */
@RunWith(SpringJUnit4ClassRunner.class)
// ���� junit spring �������ļ�
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {
	
	// ע�� Dao ʵ��������
	@Resource
	private SeckillDao seckillDao;
	
	@Test
	public void testQueryById() throws Exception {
		long id = 1000;
		Seckill seckill = seckillDao.queryById(id);
		System.out.println(seckill.getName());
		System.out.println(seckill);
	}
	
	@Test
	public void testQueryAll() throws Exception {
		// java û�б����βεļ�¼
		// ���� �� queryAll(int offset, int limit) - > queryAll(arg0, arg1)
		
		
		List<Seckill> seckills = seckillDao.queryAll(0, 100);
		
		for (Seckill seckill : seckills) {
			System.out.println(seckill);
		}
		
	}
	
	@Test
	public void testReduceNumber() throws Exception {
		Date killTime = new Date();
		
		int updateCount = seckillDao.reduceNumber(1000, killTime);
		
		System.out.println("updateCount ---- " + updateCount);
	}

}
