package wt.seckill.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import wt.seckill.dto.Exposer;
import wt.seckill.dto.SeckillExecution;
import wt.seckill.entity.Seckill;

/**
 * 
 * @author hohoTT
 * 业务接口 : 站在 "使用者" 的角度设计接口
 * 三个方面 : 方法定义粒度、参数、返回类型(return 类型/异常)
 *
 */
public interface SeckillService {
	
	// 查询所有秒杀记录
	List<Seckill> getSeckillList();
	
	// 查询单个秒杀记录
	Seckill getById(long SeckillId);
	
	// 秒杀开启时 : 出入秒杀接口的地址
	// 否则输出系统时间和秒杀时间
	Exposer exportSeckillUrl(long SeckillId);
	
	// 执行秒杀操作
	SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
		throws Exception;

}



