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

	// �е��������ݿ�����ӳ�
	private final JedisPool jedisPool;

	public RedisDao(String ip, int port) {
		jedisPool = new JedisPool(ip, port);
	}

	// Seckill.class Ϊ����ֽ������
	private RuntimeSchema<Seckill> schema = RuntimeSchema
			.createFrom(Seckill.class);

	// ͨ�� redis �õ�������Ҫ�� seckill ����
	public Seckill getSeckill(long seckillId) {
		// redis �������߼�
		try {
			// �����õ����ǵ� jedis ����
			Jedis jedis = jedisPool.getResource();

			try {
				// ���� key
				String key = "seckill:" + seckillId;

				// ��û��ʵ���ڲ������л�����
				// get -> byte[] -> �����л� -> Object(Seckill)
				// �����Զ�������л��ķ�ʽ�������ǵĶ���ת��Ϊ����������
				// Ȼ�󴫵ݸ� redis ��������
				// protostuff �� pojo
				byte[] bytes = jedis.get(key.getBytes());

				// �����л�ȡ��
				if (bytes != null) {
					// �����ն���
					Seckill seckill = schema.newMessage();

					// seckill �������л�
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

	// �����ֻ�����û�����ǵ� seckill ���� ʱ��ȥ put һ������
	public String putSeckill(Seckill seckill) {

		// set Object(seckill) -> ���л� -> byte[]
		try {
			// �����õ����ǵ� jedis ����
			Jedis jedis = jedisPool.getResource();
			try {
				String key = "seckill:" + seckill.getSeckillId();
				byte[] bytes = ProtostuffIOUtil
						.toByteArray(seckill, schema, LinkedBuffer
								.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));

				// ��ʱ����
				// ��Ϊ����һСʱ
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
