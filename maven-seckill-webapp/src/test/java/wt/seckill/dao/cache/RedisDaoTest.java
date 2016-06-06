package wt.seckill.dao.cache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import wt.seckill.dao.SeckillDao;
import wt.seckill.entity.Seckill;

@RunWith(SpringJUnit4ClassRunner.class)
//告诉 junit spring 的配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class RedisDaoTest {
	// 秒杀 ID
	private long id = 1003;
	
	@Autowired
	private RedisDao redisDao;
	
	@Autowired
	private SeckillDao seckillDao;
	
	@Test
	public void testSeckill() throws Exception {
		// 测试 RedisDao 其中的 get 与 put 方法
		 
		Seckill seckill = redisDao.getSeckill(id);
		
		if(seckill == null){
			seckill = seckillDao.queryById(id);
			if(seckill != null){
				String result = redisDao.putSeckill(seckill);
				System.out.println("result --- " + result);
				
				seckill = redisDao.getSeckill(id);
				
				System.out.println("seckill --- " + seckill);
			}
		}
		
	}
	
}
