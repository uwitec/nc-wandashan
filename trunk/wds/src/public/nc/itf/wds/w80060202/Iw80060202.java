package nc.itf.wds.w80060202;

import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;

public interface Iw80060202 {

	/**
	 * 保存发运单 并回写发运录入中的待发运量
	 * 
	 * @param billVO
	 *            聚合VO
	 * @throws BusinessException
	 */
	public void saveFydVO(AggregatedValueObject billVO)
			throws BusinessException;
	
	public void deleteFydVO(AggregatedValueObject billVO)throws BusinessException;
}
