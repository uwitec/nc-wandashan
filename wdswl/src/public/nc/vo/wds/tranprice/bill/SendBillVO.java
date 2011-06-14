package nc.vo.wds.tranprice.bill;

import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.trade.pub.HYBillVO;

/**
 * 
 * @author Administrator �˷Ѻ��㵥 �ۺ�vo
 */
public class SendBillVO extends HYBillVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SendBillHeaderVO getHeaderVO() {
		return (SendBillHeaderVO) getParentVO();
	}

	public SendBillBodyVO[] getBodyVos() {
		return (SendBillBodyVO[]) getChildrenVO();
	}

	private UFBoolean issale = UFBoolean.FALSE;// true:���۳��� false:ת�ֲֳ���

	private UFDate m_logDate = null;// ��ǰ��¼���� �˷Ѻ���ʱ��Ҫ

	public UFDate getM_logDate() {
		return m_logDate;
	}

	public void setM_logDate(UFDate date) {
		m_logDate = date;
	}

	public UFBoolean isSale() {
		return issale;
	}

	public void setIsSale(UFBoolean bsale) {
		issale = bsale;
	}

	/**
	 * ���ü���ʱУ������
	 * 
	 * @throws ValidationException
	 */
	public void validationOnCal() throws ValidationException {
	}

	public static int HAND_COLTYPE = 0;// �ֹ�
	public static int DS_COLTYPE = 1;// �ֹ���
	public static int XS_COLTYPE = 2;// ����

	public static int DS_PRICEUNIT = 0;
	public static int XS_PRICEUNIT = 1;
	public static int YYFW_ALL = 0;// Ӧ�÷�Χ ȫ�� ���� ת�ֲ�
	public static int YYFW_SALE = 1;
	public static int YYFW_ZFC = 2;

	public static String[] priceInfor_fieldNames = new String[] { "cpricehid",
			"cpriceid", "nprice", "nadjustprice", "iadjusttype", "ncolmny",
			"nadjustmny", "nmny", "ngl", "ipriceunit" };
}
