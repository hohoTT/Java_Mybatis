package wt.seckill.dto;

import wt.seckill.entity.SuccessKilled;
import wt.seckill.enums.SeckillStatEnum;

/**
 * 
 * @author hohoTT ��װ��ɱִ�к�Ľ��
 * 
 */
public class SeckillExecution {

	private long seckillId;

	// ��ɱִ�н��״̬
	private int state;

	// ״̬��ʾ
	private String stateInfo;

	// ��ɱ�ɹ�����
	private SuccessKilled successKilled;

	// ʹ��ö�����ͣ����д����Ż�
	public SeckillExecution(long seckillId, SeckillStatEnum statEnum,
			SuccessKilled successKilled) {
		super();
		this.seckillId = seckillId;
		this.state = statEnum.getState();
		this.stateInfo = statEnum.getStateInfo();
		this.successKilled = successKilled;
	}

	// ʹ��ö�����ͣ����д����Ż�
	public SeckillExecution(long seckillId, SeckillStatEnum statEnum) {
		super();
		this.seckillId = seckillId;
		this.state = statEnum.getState();
		this.stateInfo = statEnum.getStateInfo();
	}

	public long getSeckillId() {
		return seckillId;
	}

	public void setSeckillId(long seckillId) {
		this.seckillId = seckillId;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getStateInfo() {
		return stateInfo;
	}

	public void setStateInfo(String stateInfo) {
		this.stateInfo = stateInfo;
	}

	public SuccessKilled getSuccessKilled() {
		return successKilled;
	}

	public void setSuccessKilled(SuccessKilled successKilled) {
		this.successKilled = successKilled;
	}

	@Override
	public String toString() {
		return "SeckillExecution [seckillId=" + seckillId + ", state=" + state
				+ ", stateInfo=" + stateInfo + ", successKilled="
				+ successKilled + "]";
	}

}
