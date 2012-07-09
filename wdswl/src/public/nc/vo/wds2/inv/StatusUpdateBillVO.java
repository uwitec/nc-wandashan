package nc.vo.wds2.inv;

import nc.vo.trade.pub.HYBillVO;

public class StatusUpdateBillVO extends HYBillVO {
	
	public StatusUpdateHeadVO getHeader(){
		return (StatusUpdateHeadVO)getParentVO();
	}
	
	public StatusUpdateBodyVO[] getBodys(){
		return (StatusUpdateBodyVO[])getChildrenVO();
	}

}
