package wt.seckill.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import wt.seckill.dto.Exposer;
import wt.seckill.dto.SeckillExecution;
import wt.seckill.entity.Seckill;

/**
 * 
 * @author hohoTT
 * ҵ��ӿ� : վ�� "ʹ����" �ĽǶ���ƽӿ�
 * �������� : �����������ȡ���������������(return ����/�쳣)
 *
 */
public interface SeckillService {
	
	// ��ѯ������ɱ��¼
	List<Seckill> getSeckillList();
	
	// ��ѯ������ɱ��¼
	Seckill getById(long SeckillId);
	
	// ��ɱ����ʱ : ������ɱ�ӿڵĵ�ַ
	// �������ϵͳʱ�����ɱʱ��
	Exposer exportSeckillUrl(long SeckillId);
	
	// ִ����ɱ����
	SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
		throws Exception;

}



