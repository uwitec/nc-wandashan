package nc.itf.zb.pub;

public interface ILock {
	/**
	 * ���ص��ݲ���Ա��
	 * @return java.lang.String
	 */
	String getCurUserID();
	/**
	 * ���ر��ŵ��ݵı�ͷ������pk,���ε��ݵı�ͷ������pk
	 * @return java.util.ArrayList
	 */
	java.util.ArrayList getLockablePKArray();
	/**
	 * �������ε��ݵı�ͷ������pk
	 * @return java.util.ArrayList
	 */
	java.util.ArrayList getSourceBillPKArray();
	/**
	 * ���ر��ŵ��ݵı�ͷ������pk
	 */
	java.util.ArrayList getThisBillPKArray();
}