package nc.ui.zb.bidfloor;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.zb.pub.BillRowNo;
import nc.ui.zb.pub.FlowManageEventHandler;
import nc.vo.pub.BusinessException;
import nc.vo.querytemplate.TemplateInfo;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.zb.pub.ZbPuBtnConst;
import nc.vo.zb.pub.ZbPubConst;

public class ClientEventHandler extends FlowManageEventHandler {

	public ClientUIQueryDlg queryDialog = null;
	private ViewDetailDlg m_viewDetailDlg = null;

	public ClientEventHandler(ClientUI clientUI, IControllerBase control) {
		super(clientUI, control);
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
	protected String getHeadCondition() {
		return " pk_corp = '" + _getCorp().getPrimaryKey() + "'  ";
	}

	@Override
	protected void onBoLineAdd() throws Exception {
		// TODO Auto-generated method stub
		super.onBoLineAdd();
		BillRowNo.addLineRowNo(getBillCardPanelWrapper().getBillCardPanel(),
				ZbPubConst.ZB_BIDFLOOR_BILLTYPE, "crowno");
	}
	
	protected void onBoElse(int intBtn) throws Exception {
		if(intBtn==ZbPuBtnConst.View){
			onViewBill();
		}else if(intBtn == ZbPuBtnConst.Editor){//修改
			   onBoEdit();
		}else{
			super.onBoElse(intBtn);
		}
	}
	
	protected void onViewBill() throws Exception {
		
		String cbiddingid = PuPubVO.getString_TrimZeroLenAsNull(
				getBillCardPanelWrapper().getBillCardPanel().getHeadItem("cbiddingid").getValueObject());
		int row =getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
		
		if(row<0)
			row=0;
		String cinvmanid = PuPubVO.getString_TrimZeroLenAsNull(
				getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(row,"cinvmanid"));
		
		if(cbiddingid == null )
			throw new BusinessException("没有标书信息");
		
		if(cinvmanid == null )
			throw new BusinessException("没有品种信息");
		String str = cbiddingid+"&"+cinvmanid;
		getViewDetailDlg(str).showModal();
	}
	
	private ViewDetailDlg getViewDetailDlg(String str) throws Exception{
	    m_viewDetailDlg = new ViewDetailDlg(getClientUI(),str);
		return m_viewDetailDlg;
	}
	
	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {
		// TODO Auto-generated method stub
		super.onBoAdd( bo);
		getClientUI().map.clear();//清空map
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("cbiddingid").setEnabled(true);//标段可编辑
		
	}
	
	protected ClientUI getClientUI() {
		return (ClientUI) getBillManageUI();
	}
	
	/**
	 * 修改时标段不可重选
	 */
	protected void onBoEdit() throws Exception {
		
		super.onBoEdit();
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("cbiddingid").setEnabled(false);
	}
}
