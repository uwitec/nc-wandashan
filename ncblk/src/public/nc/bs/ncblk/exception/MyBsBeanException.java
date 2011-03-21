package nc.bs.ncblk.exception;

import nc.vo.pub.BusinessException;

/**
 * 后台业务逻辑层异常
 * 
 * @author heyq
 * 
 */
public class MyBsBeanException extends BusinessException {

	public MyBsBeanException() {
		super();
	}

	public MyBsBeanException(String str) {
		super(str);
	}
}
