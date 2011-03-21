package nc.itf.wds.w80060406;

import java.text.ParseException;

import nc.vo.pub.BusinessException;
import nc.vo.wds.w80060406.TbFydmxnewVO;

public interface Iw80060406 {

	/**
	 * �ƻ�����еĲ�ѯ��ϸ��ť���� ����������ѯ�����е�ֵ ���ز����ϸ����
	 * 
	 * @param strWhere
	 *            ��ѯ����
	 * @param stock
	 *            ����վ
	 * @param begindate
	 *            ��ʼʱ��
	 * @param enddate
	 *            ����ʱ��
	 * @param stockr
	 *            ����վ
	 * @return
	 * @throws BusinessException
	 * @throws ParseException
	 */
	public abstract TbFydmxnewVO[] queryFydmxnewVO(String strWhere,
			String stock, String begindate, String enddate, String stockr)
			throws BusinessException, ParseException;

}
