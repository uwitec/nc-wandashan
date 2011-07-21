package nc.ui.zb.bidding.view;

import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.zb.pub.FlowManageEventHandler;
import nc.vo.pub.BusinessException;
import nc.vo.querytemplate.TemplateInfo;
import nc.vo.zb.pub.ZbPubConst;
import nc.vo.zb.pub.ZbPubTool;

public class ViewEventHandler extends FlowManageEventHandler {
	public ViewUIQueryDlg queryDialog = null;

	public ViewEventHandler(ViewBiddingUI clientUI, IControllerBase control) {
		super(clientUI, control);
	}
  private boolean flag1 =true;
	public ViewEventHandler(ViewBiddingUI viewBiddingUI,
			AbstractManageController control, boolean flag) {
		super(viewBiddingUI, control);
		flag1=flag;
	}

	protected UIDialog createQueryUI() {
		if (queryDialog == null) {
			TemplateInfo tempinfo = new TemplateInfo();
			tempinfo.setPk_Org(_getCorp().getPk_corp());
			tempinfo.setCurrentCorpPk(_getCorp().getPk_corp());
			tempinfo.setFunNode(getBillUI()._getModuleCode());
			tempinfo.setUserid(getBillUI()._getOperator());
			queryDialog = new ViewUIQueryDlg(getBillUI(), null, tempinfo);
		}
		return queryDialog;
	}

	@Override
	protected String getHeadCondition() {
		String sql =" zb_bidding_h.pk_corp = '" + _getCorp().getPrimaryKey()
		+ "'  and  zb_bidding_h.pk_billtype = '" +ZbPubConst.ZB_BIDDING_BILLTYPE
		+ "' and  isnull(zb_bidding_h.dr,0)=0";
		String s=null;
		try {
			s = ZbPubTool.getParam();
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(s!=null &&!"".equals(s))
		   sql = sql+" and zb_bidding_h.reserve1 = '"+s+"'";
		return sql;
	}
	
	protected void onBoQuery() throws Exception {
      if(!flag1){
    	  super.onBoQuery();
      }else{
    	  ((ViewBiddingUI)getBillManageUI()).showHintMessage("供应商不可用查询");
      }
	}
}
