package nc.ui.ncblk.exception;

import nc.vo.pub.BusinessException;

/**
 * 前台 业务逻辑处理异常
 * 
 * @author heyq
 * 
 */

public class MyUiBeanException extends BusinessException {

	public MyUiBeanException() {
		super();
	}

	public MyUiBeanException(String str) {
		super(str);
	}
}
