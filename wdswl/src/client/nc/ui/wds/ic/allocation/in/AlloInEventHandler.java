package nc.ui.wds.ic.allocation.in;

import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.wds.ic.pub.InPubClientUI;
import nc.ui.wds.ic.pub.InPubEventHandler;
import nc.ui.wds.w8004040214.buttun0214.ISsButtun;
import nc.vo.pub.bill.BillRendererVO;
import nc.vo.wl.pub.WdsWlPubConst;


public class AlloInEventHandler extends InPubEventHandler {
	

	public AlloInEventHandler(InPubClientUI billUI, IControllerBase control) {
		super(billUI, control);
	}
	
	protected void onBoElse(int intBtn) throws Exception {
		super.onBoElse(intBtn);
		switch (intBtn) {
			case ISsButtun.Zdtp:
				onZdtp();
				break;
			case ISsButtun.Ckmx:
				onCkmx();
				break;
			case ISsButtun.Zdrk:
				onZdrk();
				break;
			case nc.ui.wds.w80020206.buttun0206.ISsButtun.Qxqz:
				onQxqz();
				break;
			case nc.ui.wds.w80020206.buttun0206.ISsButtun.Qzqr:
				onQzqr();
				break;
			case  nc.ui.wds.w80020206.buttun0206.ISsButtun.Ref4I:
				onBillRef();
				break;
		}
	}
	@Override
	protected UIDialog createQueryUI() {
		return new QueryDIG(getBillUI(), null, _getCorp().getPk_corp(), getBillUI().getModuleCode(), getBillUI()._getOperator(), null		
		);
	}


	@Override
	protected String getHeadCondition() {
		StringBuffer strWhere = new StringBuffer();
		return   strWhere.append( "  geh_cbilltypecode").append(" ='"+WdsWlPubConst.BILLTYPE_ALLO_IN+"'").toString();
	
	}

	public void showZeroLikeNull(boolean bShowZeroLikeNull) {
		BillRendererVO vo = getBillCardPanelWrapper().getBillCardPanel()
				.getBodyPanel().getRendererVO();
		vo.setShowZeroLikeNull(bShowZeroLikeNull);
		getBillCardPanelWrapper().getBillCardPanel().getBodyPanel()
				.setShowFlags(vo);
	}
}