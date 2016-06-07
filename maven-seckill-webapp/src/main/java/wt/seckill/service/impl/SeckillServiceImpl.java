package wt.seckill.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import wt.seckill.dao.SeckillDao;
import wt.seckill.dao.SuccessKilledDao;
import wt.seckill.dao.cache.RedisDao;
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

	@Autowired
	private RedisDao redisDao;

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

		// 优化点 : 缓存优化
		/**
		 * get from cache
		 * 
		 * if null get db else put cache
		 * 
		 * locgoin
		 */

		// 优化点 ： 使用 redis 进行 seckill 的获取
		// 业务在超时的基础上维护一致性
		// 1. 访问 redis 进行 seckill 对象的获取
		Seckill seckill = redisDao.getSeckill(seckillId);

		if (seckill == null) {
			// 此时 redis 缓存中没有 seckill 对象
			// 2. 那么此时就要通过数据库的访问进行获取
			seckill = seckillDao.queryById(seckillId);

			// 如果数据库中不存在
			if (seckill == null) {
				return new Exposer(false, seckillId);
			} else {
				// 如果数据库中查到数据，seckill 对象存在
				// 3. 将查询得到的seckill 对象 放入到 redis 缓存中
				redisDao.putSeckill(seckill);
			}

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

	/**
	 * 使用注解控制事务的优点: 1. 开发团队达成一致约定, 明确标注事务方法的编程风格. 2. 保证事务方法的执行时间尽可能短, 不要穿插其他网络操作
	 * RPC/HTTP 请求或者玻璃到事务方法外部. 3. 不是所有的方法都需要事务. 如只有一条查询修改操作，只读操作不需要事务控制 如一些查询的
	 * service.只有一条修改操作的 service.
	 */
	@Override
	@Transactional
	public SeckillExecution executeSeckill(long seckillId, long userPhone,
			String md5) throws SeckillException, RepeatKillException,
			SeckillCloseException {

		if (md5 == null || !md5.equals(getMD5(seckillId))) {
			// 返回秒杀业务被重写的异常
			throw new SeckillException("seckill data rewrite");
		}

		// 执行秒杀逻辑 : 减库存 + 记录购买行为
		// 已当前的系统时间作为秒杀的时间
		Date killTime = new Date();

		try {
			// 减库存成功，记录购买行为
			int insertCount = successKilledDao.insertSuccessKilled(seckillId,
					userPhone);

			// 唯一的验证 ： 使用的是在建表时的联合 seckillId + userPhone 的主键
			if (insertCount <= 0) {
				// 重复秒杀
				throw new RepeatKillException("seckill repeated");
			} else {
				// 减库存
				// 热点商品竞争
				int updateCount = seckillDao.reduceNumber(seckillId, killTime);
				if (updateCount <= 0) {
					// 没有更新到记录,意味着秒杀结束
					// 进行 rollback 操作
					throw new SeckillCloseException("seckill closed");
				} else {
					// 秒杀成功
					// 此时进行 commit 操作
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
