package nc.itf.wds.w80060202;

import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;

public interface Iw80060202 {

	/**
	 * ���淢�˵� ����д����¼���еĴ�������
	 * 
	 * @param billVO
	 *            �ۺ�VO
	 * @throws BusinessException
	 */
	public void saveFydVO(AggregatedValueObject billVO)
			throws BusinessException;
	
	public void deleteFydVO(AggregatedValueObject billVO)throws BusinessException;
}
