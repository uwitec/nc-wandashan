package nc.vo.wds.ic.allo.in.close;

import nc.vo.trade.pub.HYBillVO;

public class AlloCloseBillVO extends HYBillVO {

	public AlloCloseHVO getHead() {
		return (AlloCloseHVO)getParentVO();
	}
	public void setHead(AlloCloseHVO head) {
		setParentVO(head);
	}
	public AlloCloseBVO[] getBodys() {
		return (AlloCloseBVO[])getChildrenVO();
	}
	public void setBodys(AlloCloseBVO[] bodys) {
		setChildrenVO(bodys);
	}
	
}
