package nc.ui.wds.invcl;

import nc.ui.trade.bill.ICardController;
import nc.ui.trade.pub.IVOTreeData;
import nc.ui.trade.treecard.BillTreeCardUI;
import nc.ui.wl.pub.LoginInforHelper;
import nc.vo.wds.invcl.checkClassInterface;

public class InvClClientUI extends BillTreeCardUI {

	@Override
	protected IVOTreeData createTableTreeData() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private String cwarehouseid;//登陆人员所属仓库
	
	public InvClClientUI(){
		super();
		init();
	}
	private void init(){
		getBillTreeData().modifyRootNodeShowName("存货分类");
		LoginInforHelper login = new LoginInforHelper();
		try {
			cwarehouseid = login.getCwhid(_getOperator());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			cwarehouseid = null;
		}
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
		setPanelValue("cwarehouseid", cwarehouseid);
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
