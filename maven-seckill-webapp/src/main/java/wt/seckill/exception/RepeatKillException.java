package wt.seckill.exception;

/**
 * 
 * @author hohoTT �ظ���ɱ�쳣(�������쳣)
 * 
 */
public class RepeatKillException extends RuntimeException {

	public RepeatKillException(String message) {
		super(message);
	}

	public RepeatKillException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
