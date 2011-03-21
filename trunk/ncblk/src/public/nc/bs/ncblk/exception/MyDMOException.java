package nc.bs.ncblk.exception;

import nc.vo.pub.BusinessException;

public class MyDMOException extends BusinessException {
	public MyDMOException() {
		super();
	}

	public MyDMOException(String str) {
		super(str);
	}
}
