package nc.itf.zb.pub;

public interface ILock {
	/**
	 * 返回单据操作员。
	 * @return java.lang.String
	 */
	String getCurUserID();
	/**
	 * 返回本张单据的表头、表体pk,上游单据的表头、表体pk
	 * @return java.util.ArrayList
	 */
	java.util.ArrayList getLockablePKArray();
	/**
	 * 返回上游单据的表头、表体pk
	 * @return java.util.ArrayList
	 */
	java.util.ArrayList getSourceBillPKArray();
	/**
	 * 返回本张单据的表头、表体pk
	 */
	java.util.ArrayList getThisBillPKArray();
}