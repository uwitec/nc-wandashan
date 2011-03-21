package nc.itf.wds.w80060208;

import nc.vo.pub.AggregatedValueObject;
import nc.vo.wds.w80060406.TbFydnewVO;
import nc.vo.wds.w80060604.SoSaleVO;



public interface Iw80060208   {
	public int saveAndCommit80060208(AggregatedValueObject billVO, Object userObj)throws Exception ;
	public AggregatedValueObject saveBD80060208(AggregatedValueObject billVO, Object userObj )throws Exception ;
	public AggregatedValueObject deleteBD80060208(AggregatedValueObject billVO, Object userObj) throws Exception;
	public AggregatedValueObject prinDB80060208(TbFydnewVO tbFydnewVO,SoSaleVO ob ) throws Exception;
}
