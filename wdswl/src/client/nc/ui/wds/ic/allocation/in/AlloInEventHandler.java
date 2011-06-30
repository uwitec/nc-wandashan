package nc.ui.wds.ic.allocation.in;

import java.util.ArrayList;
import java.util.List;

import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.scm.util.ObjectUtils;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.wds.ic.pub.InPubClientUI;
import nc.ui.wds.ic.pub.InPubEventHandler;
import nc.ui.wds.pub.print.WdsWlPrintTool;
import nc.ui.wds.w8004040214.buttun0214.ISsButtun;
import nc.vo.ic.pub.TbGeneralBBVO;
import nc.vo.ic.pub.TbGeneralBVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.bill.BillRendererVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.wds.ic.allo.in.AlloInBodyPrintVO;
import nc.vo.wds.ic.allo.in.AlloInHeadPrintVO;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.WdsWlPubTool;


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
	
	
//	打印实现---------------------------------------------------------------
	
//	if (print.selectTemplate() == 1)
//		print.preview();
	
	WdsWlPrintTool print = null;
	private WdsWlPrintTool getPringTool(){
		
		if(print == null){
			print = new WdsWlPrintTool(getBillUI()._getCorp().getPrimaryKey(),getBillUI()._getOperator(),"8004040210",null,"wds",getPringModel());
		}
		return print;
	}
	
	private BillCardPanel printmodel = null;
	private BillCardPanel getPringModel(){
		if(printmodel == null){
			printmodel = new BillCardPanel();
			printmodel.loadTemplet(WdsWlPubConst.PRINT_BILL_TEMPLET);
			printmodel.setTatolRowShow(true);
		}
		return printmodel;
	}
	
	private void dealData() throws Exception{
		HYBillVO bill2 = (HYBillVO)getBufferData().getCurrentVO();
		if(bill2 == null)
			return;
		HYBillVO bill = (HYBillVO)ObjectUtils.serializableClone(bill2);
		TbGeneralBVO[] bodys = (TbGeneralBVO[])bill.getChildrenVO();
		if(bodys == null || bodys.length ==0)
			return;
		List<AlloInBodyPrintVO> lprintbody = new ArrayList<AlloInBodyPrintVO>();
		AlloInBodyPrintVO tmp = null;
		List<TbGeneralBBVO> ltray = null;
		String[] bodynames = bodys[0].getAttributeNames();
		String[] traynames = new TbGeneralBBVO().getAttributeNames();
		for(TbGeneralBVO body:bodys){
			ltray = body.getTrayInfor();
			if(ltray == null || ltray.size() == 0)
				throw new BusinessException("无托盘信息，行号"+WdsWlPubTool.getString_NullAsTrimZeroLen(body.getGeb_crowno()));

			for(TbGeneralBBVO tray:ltray){
				tmp = new AlloInBodyPrintVO();
				for(String name:bodynames){
					tmp.setAttributeValue(name, body.getAttributeValue(name));
				}
				for(String name:traynames){
					tmp.setAttributeValue("t_"+name, tray.getAttributeValue(name));
				}
				lprintbody.add(tmp);
			}
		}
		AlloInBodyPrintVO[] newbodys = null;
 		if(lprintbody.size()>0){
 			newbodys = lprintbody.toArray(new AlloInBodyPrintVO[0]);
 		}
 		
 		bill.setChildrenVO(newbodys);
 		getPringModel().setBillValueVO(bill);
 		getPringModel().execHeadTailLoadFormulas();
 		getPringModel().getBillModel().execLoadFormula();
	}
	
	/**
	 * 按钮m_boPrint点击时执行的动作,如有必要，请覆盖.
	 */
	protected void onBoPrint() throws Exception {
		if(getBufferData().isVOBufferEmpty())
			throw new BusinessException("无数据");
		if(getBufferData().getCurrentRow()< 0)
			throw new BusinessException("请选择数据");
		//		很关键的一步
		dealData();

		HYBillVO bill = null;

		bill = (HYBillVO)getPringModel().getBillValueVO(HYBillVO.class.getName(), 
				AlloInHeadPrintVO.class.getName(), 
				AlloInBodyPrintVO.class.getName());
		if(bill == null){
			getBillUI().showWarningMessage("无数据");
			return;
		}

		getPringTool().priview(bill);
	}
	
	protected void onBoDirectPrint() throws Exception {

		if(getBufferData().isVOBufferEmpty())
			throw new BusinessException("无数据");
		if(getBufferData().getCurrentRow()< 0)
			throw new BusinessException("请选择数据");
		//		很关键的一步
		dealData();

		HYBillVO bill = null;
		bill = (HYBillVO)getPringModel().getBillValueVO(HYBillVO.class.getName(), 
				AlloInHeadPrintVO.class.getName(), 
				AlloInBodyPrintVO.class.getName());
		if(bill == null){
			getBillUI().showWarningMessage("无数据");
			return;
		}

		getPringTool().print(bill);

	}
}