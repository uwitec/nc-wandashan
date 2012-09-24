package nc.ui.wds.invcl;

import nc.ui.trade.bill.ICardController;
import nc.ui.trade.pub.IVOTreeData;
import nc.ui.trade.treecard.BillTreeCardUI;
import nc.ui.trade.treecard.TreeCardEventHandler;
import nc.ui.wl.pub.LoginInforHelper;
import nc.vo.wds.invcl.checkClassInterface;

public class InvClClientUI extends BillTreeCardUI {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
	/**
	 * 实例化界面编辑前后事件处理, 如果进行事件处理需要重载该方法 创建日期：(2004-1-3 18:13:36)
	 */
	protected nc.ui.trade.card.CardEventHandler createEventHandler() {
		return new Event(this, getUIControl());
	}
}
