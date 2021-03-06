-- 数据库初始化脚本

-- 创建数据库
CREATE DATABASE seckill;
-- 使用数据库
use seckill;
-- 创建秒杀库存表
CREATE TABLE seckill(
	seckill_id bigint NOT NULL AUTO_INCREMENT COMMENT='商品库存id',
	name varchar(120) NOT NULL COMMENT='商品名称',
	number int NOT NULL COMMENT='库存数量',
	start_time timestamp NOT NULL COMMENT='秒杀开启时间',
	end_time timestamp NOT NULL COMMENT='秒杀结束时间',
	create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT='创建时间',
	PRIMARY KEY (seckill_id),
	key idx_start_time(start_time),
	key idx_end_time(end_time),
	key idx_create_time(create_time)
)ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf-8 COMMENT='秒杀库存表';

-- 初始化数据
insert into 
	seckill(name, number, start_time, end_time)
values 
	('1000元描述iPhone6', 100, '2015-11-01 00:00:00', '2015-11-02 00:00:00'),
	('500元描述iPad2', 200, '2015-11-01 00:00:00', '2015-11-02 00:00:00'),
	('300元描述小米4', 300, '2015-11-01 00:00:00', '2015-11-02 00:00:00'),
	('200元描述红米note', 400, '2015-11-01 00:00:00', '2015-11-02 00:00:00');

	
-- 秒杀成功明细表
-- 用户登录认证相关的信息
CREATE TABLE success_killed(
	'seckill_id' bigint NOT NULL COMMENT='秒杀商品id',
	'user_phone' bigint NOT NULL COMMENT='用户手机号',
	'state' tinyint NOT NULL DEFAULT -1 COMMENT='状态标示 : -1：无效，0：成功，1：已付款，2已发货',
	'create_time' timestamp NOT NULL COMMENT='创建时间',
	PRIMARY KEY (seckill_id, user_phone),	/*联合主键*/
	key idx_create_time(create_time)		
)ENGINE=InnoDB DEFAULT CHARSET=utf-8 COMMENT='秒杀成功明细表';
	

-- 连接数据库

-----------------------------------------------------------------------

-- 成功时的语句

CREATE TABLE seckill(
	seckill_id bigint NOT NULL AUTO_INCREMENT,
	name varchar(120) NOT NULL,
	number int NOT NULL,
	start_time timestamp NOT NULL,
	end_time timestamp NOT NULL,
	create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (seckill_id),
	key idx_start_time(start_time),
	key idx_end_time(end_time),
	key idx_create_time(create_time)
)ENGINE=InnoDB AUTO_INCREMENT=1000 COMMENT='秒杀库存表';

insert into 
	seckill(name, number, start_time, end_time)
values 
	('1000元描述iPhone6', 100, '2015-11-01 00:00:00', '2015-11-02 00:00:00'),
	('500元描述iPad2', 200, '2015-11-01 00:00:00', '2015-11-02 00:00:00'),
	('300元描述小米4', 300, '2015-11-01 00:00:00', '2015-11-02 00:00:00'),
	('200元描述红米note', 400, '2015-11-01 00:00:00', '2015-11-02 00:00:00');

CREATE TABLE success_killed(
	seckill_id bigint NOT NULL,
	user_phone bigint NOT NULL,
	state tinyint NOT NULL DEFAULT -1 ,
	create_time timestamp NOT NULL ,
	PRIMARY KEY (seckill_id, user_phone),	/*联合主键*/
	key idx_create_time(create_time)		
)ENGINE=InnoDB COMMENT='秒杀成功明细表';


-- 为什么手写DDL
-- 记录每次上线的DDL修改
-- 上线时的版本v1.1

ALTER TABLE seckill
DROP INDEX idx_create_time,
add INDEX idx_c_s(start_time, create_time)


-- 上线新的版本v1.2
-- 新的 DDL


