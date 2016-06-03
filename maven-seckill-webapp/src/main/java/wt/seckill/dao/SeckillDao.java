package wt.seckill.dao;

import java.util.Date;
import java.util.List;

import wt.seckill.entity.Seckill;

public interface SeckillDao {
	
	// �����
	// ����ֵΪ��Ӱ������ > 1����ʾ���µ�����
	int reduceNumber(long seckillId, Date killTime);
	
	// ͨ��Id��ѯ��ɱ����
	Seckill queryById(long seckillId);
	
	// ����ƫ������ѯ��ɱ�б�
	List<Seckill> queryAll(int offset, int limit);
	
}
