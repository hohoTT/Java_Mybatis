package wt.seckill.dao;

import java.util.Date;
import java.util.List;

import wt.seckill.entity.Seckill;

public interface SeckillDao {
	
	// 减库存
	// 返回值为，影响行数 > 1，表示更新的行数
	int reduceNumber(long seckillId, Date killTime);
	
	// 通过Id查询秒杀对象
	Seckill queryById(long seckillId);
	
	// 根据偏移量查询秒杀列表
	List<Seckill> queryAll(int offset, int limit);
	
}
