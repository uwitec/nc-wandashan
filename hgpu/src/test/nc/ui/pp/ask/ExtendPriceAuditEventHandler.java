package nc.ui.pp.ask;

import nc.ui.hg.pu.pub.LongTimeTask;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.pp.ask.IAskAndQuote;
import nc.vo.pp.ask.PriceauditHeaderVO;
import nc.vo.pp.ask.PriceauditMergeVO;

// add by  zhw  2011-06-17  �׸���Ŀ    У��۸��������Ƿ��Ѿ����ɺ�ͬ
public class ExtendPriceAuditEventHandler extends PriceAuditEventHandler {
	
	public ExtendPriceAuditEventHandler(BillManageUI billUI,
			IControllerBase control) {
		super(billUI, control);
		// TODO Auto-generated constructor stub
	}
	
	protected void onBoElse(int intBtn) throws Exception{
		if (intBtn == IAskAndQuote.GenPurOrder) {
			PriceauditMergeVO billVO = null;
			billVO = (PriceauditMergeVO) getBillUI().getVOFromUI();
			// ���²�ѯȡts
			PriceauditHeaderVO head =(PriceauditHeaderVO)billVO.getParentVO();
			String str = head.getPrimaryKey();
			Class[] ParameterTypes = new Class[]{String.class};
			Object[] ParameterValues = new Object[]{str};
			LongTimeTask.callRemoteService("pu","nc.bs.pp.ask.HgPriceAuditBO", "isComplete", ParameterTypes, ParameterValues, 2);
		}
		super.onBoElse(intBtn);
	}
	
}
