package wt.seckill.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import wt.seckill.entity.Seckill;

public interface SeckillDao {
	
	// 减库存
	// 返回值为，影响行数 > 1，表示更新的行数
	int reduceNumber(@Param("seckillId") long seckillId, @Param("killTime") Date killTime);
	
	// 通过Id查询秒杀对象
	Seckill queryById(long seckillId);
	
	// 根据偏移量查询秒杀列表
	// 未修改前
//	List<Seckill> queryAll(int offset, int limit);
	// 修改接口, 解决参数mapper xml中找不到参数的情况
	List<Seckill> queryAll(@Param("offset") int offset, @Param("limit") int limit);
	
}
