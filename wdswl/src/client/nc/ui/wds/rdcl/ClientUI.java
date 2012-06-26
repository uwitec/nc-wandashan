package nc.ui.wds.rdcl;

import nc.ui.trade.bill.ICardController;
import nc.ui.trade.pub.IVOTreeData;
import nc.ui.trade.treecard.BillTreeCardUI;
import nc.vo.wds.rdcl.checkClassInterface;

public class ClientUI extends BillTreeCardUI {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected IVOTreeData createTableTreeData() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public ClientUI(){
		super();
		init();
	}
	private void init(){
		getBillTreeData().modifyRootNodeShowName("收发类别");
	}

	@Override
	protected IVOTreeData createTreeData() {
		return new ClientTreeCardData();
	}

	@Override
	protected ICardController createController() {
		return new ClientController();
	}
	
	public void setDefaultData() throws Exception {
		super.setDefaultData();
		setPanelValue("pk_corp",_getCorp().getPrimaryKey());
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
