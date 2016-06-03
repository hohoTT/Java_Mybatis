package wt.seckill.dao;

import wt.seckill.entity.SuccessKilled;

public interface SuccessKilledDao {
	
	// 插入购买明细
	// 可过滤重复 -- 与表结构有关 (使用联合主键的方式建表)
	// 返回值为插入的行数
	int insertSuccessKilled(long seckillId, long userPhone);
	
	// 根据id查询SuccessKilled并携带秒杀产品对象实体
	SuccessKilled queryByIdWithSeckill(long seckillId);

}
