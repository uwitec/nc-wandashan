package nc.bs.ncblk.exception;

import nc.vo.pub.BusinessException;

/**
 * ��̨ҵ���߼����쳣
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
