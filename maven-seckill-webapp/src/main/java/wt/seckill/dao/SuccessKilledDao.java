package wt.seckill.dao;

import wt.seckill.entity.SuccessKilled;

public interface SuccessKilledDao {
	
	// ���빺����ϸ
	// �ɹ����ظ� -- ���ṹ�й� (ʹ�����������ķ�ʽ����)
	// ����ֵΪ���������
	int insertSuccessKilled(long seckillId, long userPhone);
	
	// ����id��ѯSuccessKilled��Я����ɱ��Ʒ����ʵ��
	SuccessKilled queryByIdWithSeckill(long seckillId);

}
