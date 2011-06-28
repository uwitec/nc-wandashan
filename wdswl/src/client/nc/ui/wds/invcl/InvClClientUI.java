package nc.ui.wds.invcl;

import nc.ui.trade.bill.ICardController;
import nc.ui.trade.pub.IVOTreeData;
import nc.ui.trade.treecard.BillTreeCardUI;
import nc.vo.wds.invcl.checkClassInterface;

public class InvClClientUI extends BillTreeCardUI {

	@Override
	protected IVOTreeData createTableTreeData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected IVOTreeData createTreeData() {
		// TODO Auto-generated method stub
		return new WdsInvClTreeData();
	}

	@Override
	protected ICardController createController() {
		// TODO Auto-generated method stub
		return new InvClCtrl();
	}
	
	public void setDefaultData() throws Exception {
		super.setDefaultData();
		setPanelValue("pk_corp",_getCorp().getPrimaryKey());

		setPanelValue("coperator", _getOperator());
		setPanelValue("dmakedate", _getDate());
	}
	
	public java.lang.Object getUserObject() {
		return new checkClassInterface();
	}

	@Override
	public String getRefBillType() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void setPanelValue(String fieldname,Object oValue){
		getBillCardPanel().getHeadItem(fieldname).setValue(oValue);
	}

	@Override
	protected void initSelfData() {
		// TODO Auto-generated method stub
	
	}

}
