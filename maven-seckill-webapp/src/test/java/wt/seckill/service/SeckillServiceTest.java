package wt.seckill.service;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import wt.seckill.dto.Exposer;
import wt.seckill.dto.SeckillExecution;
import wt.seckill.entity.Seckill;
import wt.seckill.exception.RepeatKillException;
import wt.seckill.exception.SeckillCloseException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:spring/spring-dao.xml",
		"classpath:spring/spring-service.xml" })
public class SeckillServiceTest {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private SeckillService seckillService;

	@Test
	public void testGetSeckillList() throws Exception {
		List<Seckill> seckills = seckillService.getSeckillList();
		logger.info("seckills={}", seckills);
		System.out.println(seckills);
	}

	@Test
	public void testGetById() throws Exception {
		long id = 1000;
		Seckill seckill = seckillService.getById(id);
		logger.info("seckill={}", seckill);
		System.out.println(seckill);
	}

	@Test
	public void testExportSeckillUrl() throws Exception {
		long id = 1000;
		Exposer exposer = seckillService.exportSeckillUrl(id);
		logger.info("exposer={}", exposer.toString());
		System.out.println("exposer.toString() --- " + exposer.toString());
		/**
		 * exposer.toString() Exposer [exposed=true,
		 * md5=e8a8f1967e5310858fba341f9d02101d, seckillId=1000, now=0, start=0,
		 * end=0]
		 * 
		 */
	}

	@Test
	public void testExecuteSeckill() throws Exception {
		long id = 1000;
		long phone = 18724589276L;

		String md5 = "e8a8f1967e5310858fba341f9d02101d";

		try {
			SeckillExecution seckillExecution = seckillService.executeSeckill(
					id, phone, md5);

			logger.info("seckillExecution={}", seckillExecution);
			System.out.println(seckillExecution.toString());

		} catch (RepeatKillException e) {
			logger.error(e.getMessage());
		} catch (SeckillCloseException e) {
			logger.error(e.getMessage());
		}

	}
	
	
	/**
     * 集成测试代码完整逻辑：秒杀完整流程，可重复执行
     */
    @Test
    public void testSeckillLogic() {

        long id = 1000;
        Exposer exposer = seckillService.exportSeckillUrl(id);
        logger.info("exposer={}",exposer.toString());
        if (exposer.isExposed()) {

            long phone = 18724589299L;
            String md5 = exposer.getMd5();

            try {
                SeckillExecution seckillExecution = seckillService.executeSeckill(id, phone, md5);
                logger.info("result={}",seckillExecution);
            } catch (RepeatKillException e) {
            	logger.error(e.getMessage());
            } catch (SeckillCloseException e) {
            	logger.error(e.getMessage());
            }

        } else {
        	logger.warn("秒杀未开始：{}", exposer.toString());
        }

    }
}
