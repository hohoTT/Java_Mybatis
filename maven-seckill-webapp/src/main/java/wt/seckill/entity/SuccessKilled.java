package wt.seckill.entity;

import java.util.Date;

public class SuccessKilled {

	private long seckillId;
	private long userPhone;
	private short state;
	private Date createTime;

	// 变通: 多对一的复合属性
	// 其实就是 多对一 的关系
	// 一个成功秒杀的表中可以对应多个相同的 seckill 表中被秒杀的物品
	private Seckill seckill;

	public SuccessKilled() {
	}

	public SuccessKilled(long seckillId, long userPhone, short state,
			Date createTime, Seckill seckill) {
		super();
		this.seckillId = seckillId;
		this.userPhone = userPhone;
		this.state = state;
		this.createTime = createTime;
		this.seckill = seckill;
	}

	public long getSeckillId() {
		return seckillId;
	}

	public void setSeckillId(long seckillId) {
		this.seckillId = seckillId;
	}

	public long getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(long userPhone) {
		this.userPhone = userPhone;
	}

	public short getState() {
		return state;
	}

	public void setState(short state) {
		this.state = state;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Seckill getSeckill() {
		return seckill;
	}

	public void setSeckill(Seckill seckill) {
		this.seckill = seckill;
	}

	@Override
	public String toString() {
		return "SuccessKilled [seckillId=" + seckillId + ", userPhone="
				+ userPhone + ", state=" + state + ", createTime=" + createTime
				+ ", seckill=" + seckill + "]";
	}

}
