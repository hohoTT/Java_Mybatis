package wt.seckill.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import wt.seckill.dao.SeckillDao;
import wt.seckill.dao.SuccessKilledDao;
import wt.seckill.dto.Exposer;
import wt.seckill.dto.SeckillExecution;
import wt.seckill.entity.Seckill;
import wt.seckill.entity.SuccessKilled;
import wt.seckill.enums.SeckillStatEnum;
import wt.seckill.exception.RepeatKillException;
import wt.seckill.exception.SeckillCloseException;
import wt.seckill.exception.SeckillException;
import wt.seckill.service.SeckillService;

// spring 中提供的注解 @Component @Service @Dao @Controller
// 根据需要选择合适的注解
@Service
public class SeckillServiceImpl implements SeckillService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	// 注入 Service 依赖
	@Autowired
	private SeckillDao seckillDao;

	@Autowired
	private SuccessKilledDao successKilledDao;

	// md5 盐值字符串，用于混淆 md5
	private final String slat = "asdadwqr^&$^&*&ASNDIADSA489IOGKB";

	@Override
	public List<Seckill> getSeckillList() {
		return seckillDao.queryAll(0, 4);
	}

	@Override
	public Seckill getById(long seckillId) {
		return seckillDao.queryById(seckillId);
	}

	@Override
	public Exposer exportSeckillUrl(long seckillId) {

		Seckill seckill = seckillDao.queryById(seckillId);

		if (seckill == null) {
			return new Exposer(false, seckillId);
		}

		Date startTime = seckill.getStartTime();
		Date endTime = seckill.getEndTime();
		// 系统当前时间
		Date nowTime = new Date();

		// 此时 getTime 方法获取的是时间毫秒的表示
		if (nowTime.getTime() < startTime.getTime()
				|| nowTime.getTime() > endTime.getTime()) {
			return new Exposer(false, seckillId, nowTime.getTime(),
					startTime.getTime(), endTime.getTime());
		}

		// 转化特定字符串的过程，不可逆
		String md5 = getMD5(seckillId);

		return new Exposer(true, md5, seckillId);
	}

	// 计算获取 md5 值，生成 md5 值
	private String getMD5(long seckillId) {

		String base = seckillId + "/" + slat;

		String md5 = DigestUtils.md5DigestAsHex(base.getBytes());

		return md5;
	}

	@Override
	public SeckillExecution executeSeckill(long seckillId, long userPhone,
			String md5) throws SeckillException, RepeatKillException,
			SeckillCloseException {

		if (md5 == null || md5.equals(getMD5(seckillId))) {
			// 返回秒杀业务被重写的异常
			throw new SeckillException("seckill data rewrite");
		}

		// 执行秒杀逻辑 : 减库存 + 记录购买行为
		// 已当前的系统时间作为秒杀的时间
		Date killTime = new Date();

		try {
			// 减库存
			int updateCount = seckillDao.reduceNumber(seckillId, killTime);
			if (updateCount <= 0) {
				// 没有更新到记录,意味着秒杀结束
				throw new SeckillCloseException("seckill closed");
			} else {
				// 减库存成功，记录购买行为
				int insertCount = successKilledDao.insertSuccessKilled(
						seckillId, userPhone);

				// 唯一的验证 ： 使用的是在建表时的联合 seckillId + userPhone 的主键
				if (insertCount <= 0) {
					// 重复秒杀
					throw new RepeatKillException("seckill repeated");
				} else {
					// 秒杀成功
					SuccessKilled successKilled = successKilledDao
							.queryByIdWithSeckill(seckillId, userPhone);

					// 使用枚举类型，进行代码优化
					return new SeckillExecution(seckillId,
							SeckillStatEnum.SUCCESS, successKilled);
				}

			}
		} catch (SeckillCloseException e1) {
			throw e1;
		} catch (RepeatKillException e2) {
			throw e2;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);

			// 所有编译期异常，转化为运行期异常
			throw new SeckillException("seckill inner error " + e.getMessage());
		}

	}

}
