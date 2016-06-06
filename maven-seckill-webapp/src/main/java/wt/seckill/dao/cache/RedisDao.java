package wt.seckill.dao.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import wt.seckill.entity.Seckill;

public class RedisDao {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	// 有点类似数据库的连接池
	private final JedisPool jedisPool;

	public RedisDao(String ip, int port) {
		jedisPool = new JedisPool(ip, port);
	}

	// Seckill.class 为类的字节码对象
	private RuntimeSchema<Seckill> schema = RuntimeSchema
			.createFrom(Seckill.class);

	// 通过 redis 拿到我们需要的 seckill 对象
	public Seckill getSeckill(long seckillId) {
		// redis 操作的逻辑
		try {
			// 首先拿到我们的 jedis 对象
			Jedis jedis = jedisPool.getResource();

			try {
				// 构造 key
				String key = "seckill:" + seckillId;

				// 并没有实现内部的序列化操作
				// get -> byte[] -> 反序列化 -> Object(Seckill)
				// 采用自定义的序列化的方式，把我们的对象转化为二进制数据
				// 然后传递给 redis 缓存起来
				// protostuff ： pojo
				byte[] bytes = jedis.get(key.getBytes());

				// 缓存中获取的
				if (bytes != null) {
					// 创建空对象
					Seckill seckill = schema.newMessage();

					// seckill 被反序列化
					ProtostuffIOUtil.mergeFrom(bytes, seckill, schema);

					return seckill;
				}

			} finally {
				jedis.close();
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return null;
	}

	// 当发现缓存中没有我们的 seckill 对象 时，去 put 一个对象
	public String putSeckill(Seckill seckill) {

		// set Object(seckill) -> 序列化 -> byte[]
		try {
			// 首先拿到我们的 jedis 对象
			Jedis jedis = jedisPool.getResource();
			try {
				String key = "seckill:" + seckill.getSeckillId();
				byte[] bytes = ProtostuffIOUtil
						.toByteArray(seckill, schema, LinkedBuffer
								.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));

				// 超时缓存
				// 设为缓存一小时
				int timeout = 60 * 60;

				String result = jedis.setex(key.getBytes(), timeout, bytes);

				return result;
			} finally {
				jedis.close();
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return null;
	}
}
