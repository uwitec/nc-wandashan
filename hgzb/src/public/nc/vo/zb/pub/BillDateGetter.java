package nc.vo.zb.pub;

import nc.ui.pub.ServerTimeProxy;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;

public class BillDateGetter {
	
	private BillDateGetter(){}

	/**
	 * ��������
	 * 
	 */
	public static UFDate getApproveDate() {
		UFDate date = new UFDate(getServerTime().getMillis());
		return date;
	}

	/**
	 * ��������
	 * 
	 */
	public static UFDate getBillDate() {
		UFDate date = new UFDate(getServerTime().getMillis());
		return date;
	}

	/**
	 * �Ƶ�����
	 * 
	 */
	public static UFDate getMakeDate() {
		UFDate date = new UFDate(getServerTime().getMillis());
		return date;
	}

	/**
	 * �������¼�ķ���������ʱ��
	 * 
	 */
	public static UFDateTime getServerTime() {
		return ServerTimeProxy.getInstance().getServerTime();
	}

}
