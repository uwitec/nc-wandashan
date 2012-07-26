package nc.vo.dm.so.deal;
import nc.vo.trade.pub.HYBillVO;

public class SoDealBillVO extends HYBillVO {
	public SoDeHeaderVo getHeader(){
		return (SoDeHeaderVo)getParentVO();
	}
	
	public SoDealVO[] getBodyVos(){
		return (SoDealVO[])getChildrenVO();
	}
}
