package nc.ui.zb.cust;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.zb.pub.FlowManageEventHandler;
import nc.vo.querytemplate.TemplateInfo;
import nc.vo.zb.cust.CubasdocHgVO;
import nc.vo.zb.pub.ZbPuBtnConst;

public class ClientEventHandler extends FlowManageEventHandler {

	public ClientUIQueryDlg queryDialog = null;
	public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}

	protected UIDialog createQueryUI() {
		if (queryDialog == null) {
			TemplateInfo tempinfo = new TemplateInfo();
			tempinfo.setPk_Org(_getCorp().getPk_corp());
			tempinfo.setCurrentCorpPk(_getCorp().getPk_corp());
			tempinfo.setFunNode(getBillUI()._getModuleCode());
			tempinfo.setUserid(getBillUI()._getOperator());
			queryDialog = new ClientUIQueryDlg(getBillUI(), null, tempinfo);
		}
		return queryDialog;
	}
	
	@Override
	protected void onBoElse(int intBtn) throws Exception {

		if(intBtn==ZbPuBtnConst.MODIFY){
			onModifyBill();
		}else if(intBtn == ZbPuBtnConst.Editor){//修改
			   onBoEdit();
		} else{
			super.onBoElse(intBtn);
		}
			
	}
	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {
		// TODO Auto-generated method stub
		super.onBoAdd(bo);
		getBillCardPanel().getHeadItem("vbillno").setEnabled(true);
	}
     
	@Override
	protected String getHeadCondition() {
		return " pk_corp ='"+_getCorp().getPrimaryKey()+"'";
	}

	private void onModifyBill() throws Exception{
		super.onBoEdit();
		setItemEnable(false);
	}
	
	//修订后 设置编辑性
	private void setItemEnable(boolean flag){
		CubasdocHgVO vo = new CubasdocHgVO();
		String[] strs = vo.getAttributeNames();
		for(String str:strs){
			if("ccustmanid".equalsIgnoreCase(str)||"dr".equalsIgnoreCase(str)||"ts".equalsIgnoreCase(str))
				continue;
			getBillCardPanel().getHeadItem(str).setEnabled(flag);
		}
		getBillCardPanel().getHeadItem("ccustmanid").setEnabled(!flag);
	}
	
	protected void onBoCancel() throws Exception {
		setItemEnable(true);
		super.onBoCancel();
	}
	
	@Override
	protected void onBoSave() throws Exception {
		setItemEnable(true);
		super.onBoSave();
	}
	
	protected void onBoEdit() throws Exception {
		super.onBoEdit();
		getBillCardPanel().getHeadItem("vbillno").setEnabled(true);
	}
}
