package wt.seckill.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import wt.seckill.dto.Exposer;
import wt.seckill.dto.SeckillExecution;
import wt.seckill.entity.Seckill;
import wt.seckill.exception.RepeatKillException;
import wt.seckill.exception.SeckillCloseException;
import wt.seckill.exception.SeckillException;

/**
 * 
 * @author hohoTT ҵ��ӿ� : վ�� "ʹ����" �ĽǶ���ƽӿ� �������� : �����������ȡ���������������(return ����/�쳣)
 * 
 */
public interface SeckillService {

	// ��ѯ������ɱ��¼
	List<Seckill> getSeckillList();

	// ��ѯ������ɱ��¼
	Seckill getById(long seckillId);

	// ��ɱ����ʱ : ������ɱ�ӿڵĵ�ַ
	// �������ϵͳʱ�����ɱʱ��
	Exposer exportSeckillUrl(long seckillId);

	// ִ����ɱ����
	SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
			throws SeckillException, RepeatKillException, SeckillCloseException;

	// ִ����ɱ���� by �洢����
	SeckillExecution executeSeckillProcedure(long seckillId, long userPhone,
			String md5);

}
