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

// spring ���ṩ��ע�� @Component @Service @Dao @Controller
// ������Ҫѡ����ʵ�ע��
@Service
public class SeckillServiceImpl implements SeckillService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	// ע�� Service ����
	@Autowired
	private SeckillDao seckillDao;

	@Autowired
	private SuccessKilledDao successKilledDao;

	@Autowired
	private RedisDao redisDao;

	// md5 ��ֵ�ַ��������ڻ��� md5
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

		// �Ż��� : �����Ż�
		/**
		 * get from cache
		 * 
		 * if null get db else put cache
		 * 
		 * locgoin
		 */

		// �Ż��� �� ʹ�� redis ���� seckill �Ļ�ȡ
		// ҵ���ڳ�ʱ�Ļ�����ά��һ����
		// 1. ���� redis ���� seckill ����Ļ�ȡ
		Seckill seckill = redisDao.getSeckill(seckillId);

		if (seckill == null) {
			// ��ʱ redis ������û�� seckill ����
			// 2. ��ô��ʱ��Ҫͨ�����ݿ�ķ��ʽ��л�ȡ
			seckill = seckillDao.queryById(seckillId);

			// ������ݿ��в�����
			if (seckill == null) {
				return new Exposer(false, seckillId);
			} else {
				// ������ݿ��в鵽���ݣ�seckill �������
				// 3. ����ѯ�õ���seckill ���� ���뵽 redis ������
				redisDao.putSeckill(seckill);
			}

		}

		Date startTime = seckill.getStartTime();
		Date endTime = seckill.getEndTime();
		// ϵͳ��ǰʱ��
		Date nowTime = new Date();

		// ��ʱ getTime ������ȡ����ʱ�����ı�ʾ
		if (nowTime.getTime() < startTime.getTime()
				|| nowTime.getTime() > endTime.getTime()) {
			return new Exposer(false, seckillId, nowTime.getTime(),
					startTime.getTime(), endTime.getTime());
		}

		// ת���ض��ַ����Ĺ��̣�������
		String md5 = getMD5(seckillId);

		return new Exposer(true, md5, seckillId);
	}

	// �����ȡ md5 ֵ������ md5 ֵ
	private String getMD5(long seckillId) {

		String base = seckillId + "/" + slat;

		String md5 = DigestUtils.md5DigestAsHex(base.getBytes());

		return md5;
	}

	/**
	 * ʹ��ע�����������ŵ�: 1. �����ŶӴ��һ��Լ��, ��ȷ��ע���񷽷��ı�̷��. 2. ��֤���񷽷���ִ��ʱ�価���ܶ�, ��Ҫ���������������
	 * RPC/HTTP ������߲��������񷽷��ⲿ. 3. �������еķ�������Ҫ����. ��ֻ��һ����ѯ�޸Ĳ�����ֻ����������Ҫ������� ��һЩ��ѯ��
	 * service.ֻ��һ���޸Ĳ����� service.
	 */
	@Override
	@Transactional
	public SeckillExecution executeSeckill(long seckillId, long userPhone,
			String md5) throws SeckillException, RepeatKillException,
			SeckillCloseException {

		if (md5 == null || !md5.equals(getMD5(seckillId))) {
			// ������ɱҵ����д���쳣
			throw new SeckillException("seckill data rewrite");
		}

		// ִ����ɱ�߼� : ����� + ��¼������Ϊ
		// �ѵ�ǰ��ϵͳʱ����Ϊ��ɱ��ʱ��
		Date killTime = new Date();

		try {
			// �����ɹ�����¼������Ϊ
			int insertCount = successKilledDao.insertSuccessKilled(seckillId,
					userPhone);

			// Ψһ����֤ �� ʹ�õ����ڽ���ʱ������ seckillId + userPhone ������
			if (insertCount <= 0) {
				// �ظ���ɱ
				throw new RepeatKillException("seckill repeated");
			} else {
				// �����
				// �ȵ���Ʒ����
				int updateCount = seckillDao.reduceNumber(seckillId, killTime);
				if (updateCount <= 0) {
					// û�и��µ���¼,��ζ����ɱ����
					// ���� rollback ����
					throw new SeckillCloseException("seckill closed");
				} else {
					// ��ɱ�ɹ�
					// ��ʱ���� commit ����
					SuccessKilled successKilled = successKilledDao
							.queryByIdWithSeckill(seckillId, userPhone);

					// ʹ��ö�����ͣ����д����Ż�
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

			// ���б������쳣��ת��Ϊ�������쳣
			throw new SeckillException("seckill inner error " + e.getMessage());
		}

	}

}
