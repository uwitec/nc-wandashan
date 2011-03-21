package nc.itf.wds.w80060408;

import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.wds.w80060401.TbShipentryBVO;
import nc.vo.wds.w80060406.TbFydmxnewVO;


public interface Iw80060408 {
	
	public void updateFydVO(AggregatedValueObject billVO) throws BusinessException;
	

}
