package nc.itf.wds.w8000;

import nc.vo.pub.AggregatedValueObject;

public interface Iw8000 {
	public AggregatedValueObject saveBD(AggregatedValueObject billVO, Object userObj )throws Exception ;
	public AggregatedValueObject deleteBD(AggregatedValueObject billVO, Object userObj) throws Exception;
	
}
