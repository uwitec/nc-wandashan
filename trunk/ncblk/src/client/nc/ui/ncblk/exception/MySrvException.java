package nc.ui.ncblk.exception;

import nc.vo.pub.BusinessException;

/**
 * ������쳣
 * 
 * @author heyq
 * 
 */

public class MySrvException extends BusinessException {
	public MySrvException() {
		super();
	}

	public MySrvException(String str) {
		super(str);
	}
}
