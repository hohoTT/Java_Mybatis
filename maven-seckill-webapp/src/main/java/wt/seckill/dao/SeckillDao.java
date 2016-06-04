package wt.seckill.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import wt.seckill.entity.Seckill;

public interface SeckillDao {
	
	// �����
	// ����ֵΪ��Ӱ������ > 1����ʾ���µ�����
	int reduceNumber(@Param("seckillId") long seckillId, @Param("killTime") Date killTime);
	
	// ͨ��Id��ѯ��ɱ����
	Seckill queryById(long seckillId);
	
	// ����ƫ������ѯ��ɱ�б�
	// δ�޸�ǰ
//	List<Seckill> queryAll(int offset, int limit);
	// �޸Ľӿ�, �������mapper xml���Ҳ������������
	List<Seckill> queryAll(@Param("offset") int offset, @Param("limit") int limit);
	
}
