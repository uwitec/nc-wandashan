package nc.bs.hg.pu.pub;

import nc.bs.pub.pf.IQueryData;
import nc.bs.pub.pf.IQueryData2;
import nc.bs.trade.business.HYSuperDMO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;

/*
 * ���ղ�ѯ��Ӧ��DMO����.
 * */

public abstract class HYPubDMOWithQuery extends HYSuperDMO implements IQueryData, IQueryData2 {

	public HYPubDMOWithQuery() {
		super();
	}

	/**
	 * �˴�����˵��Ϊ��������Ĺؼ��ֶν��в�ѯ�ӱ�����ݡ� �������ڣ�(2001-7-9 20:06:40)
	 * 
	 * @return nc.vo.pub.CircularlyAccessibleValueObject
	 * @param key
	 *            java.lang.String
	 */
	public CircularlyAccessibleValueObject[] queryAllBodyData(String key) throws BusinessException {
		return queryAllBodyData(key, null);
	}

	/**
	 * �˴�����˵��Ϊ���ݲ�ѯ������õ�����Լ�����ڹ̻� ������������������估�÷����ж���ĵ�sql��� ������ϲ�ѯ�� �������ڣ�(2001-7-9
	 * 19:58:37)
	 * 
	 * @return nc.vo.pub.AggregatedValueObject
	 * @param whereString
	 *            java.lang.String
	 */
	public CircularlyAccessibleValueObject[] queryAllHeadData(String whereString) throws BusinessException {
		CircularlyAccessibleValueObject[] vos = null;

		vos = queryAllHeadData(getHeadVOClass(), whereString);

		return vos;
	}

	/**
	 * �˴�����˵��Ϊ��������Ĺؼ��ֶ����ѯ�������в�ѯ�ӱ�����ݡ� �������ڣ�(2001-7-9 20:06:40)
	 * 
	 * @return nc.vo.pub.CircularlyAccessibleValueObject
	 * @param key
	 *            java.lang.String
	 */
	public CircularlyAccessibleValueObject[] queryAllBodyData(String key, String whereString) throws BusinessException {
		CircularlyAccessibleValueObject[] retVos = null;
		retVos = queryAllBodyData(getBillType(), getBodyVOClass(), key, whereString);
		return retVos;
	}

	/**
	 * ��������
	 * 
	 * @return twh (2006-10-26 ����09:24:53)<br>
	 */
	protected abstract String getBillType();

	/**
	 * ��ͷVO��
	 * 
	 * @return twh (2006-10-26 ����09:25:03)<br>
	 */
	protected abstract Class getHeadVOClass();

	/**
	 * ����VO��
	 * 
	 * @return twh (2006-10-26 ����09:25:15)<br>
	 */
	protected abstract Class getBodyVOClass();

}
