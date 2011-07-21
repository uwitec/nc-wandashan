package nc.ui.zb.comments;

import java.util.ArrayList;
import java.util.List;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillTabbedPane;
import nc.ui.scm.util.ObjectUtils;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.zb.pub.BillRowNo;
import nc.ui.zb.pub.FlowManageEventHandler;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ValidationException;
import nc.vo.querytemplate.TemplateInfo;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.zb.comments.BiEvaluationBodyVO;
import nc.vo.zb.comments.BidEvalHeaderPrintVO;
import nc.vo.zb.comments.BidEvaluationHeaderVO;
import nc.vo.zb.comments.BidSlvendorVO;
import nc.vo.zb.comments.EvaluationBodyPrintVO;
import nc.vo.zb.pub.ZbPuBtnConst;
import nc.vo.zb.pub.ZbPubConst;
import nc.vo.zb.pub.ZbPubTool;

public class ClientEventHandler extends FlowManageEventHandler {
	public ClientUIQueryDlg queryDialog = null;

	private AverageCalDlg m_averageCalDlg = null;
	
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
		super.onBoLineAdd();
		BillRowNo.addLineRowNo(getBillCardPanelWrapper().getBillCardPanel(),ZbPubConst.ZB_EVALUATION_BILLTYPE,"crowno");
	}
	
	@Override
	protected ClientUI getBillManageUI() {
		return (ClientUI) getBillUI();
	}
	
	/**
	 * 动作脚本平台,作废，回收单据号
	 */
	@Override
	protected void onBoDel() throws Exception {
		//
		if (getBufferData().getCurrentVO() == null)
			return;
		
		if (MessageDialog.showYesNoDlg(getBillUI(), "作废", "是否确认作废当前单据?") != UIDialog.ID_YES) {
			return;
		}
		String billcode = (String)getBufferData().getCurrentVO().getParentVO().getAttributeValue("vbillno");
		String pk_billtype =  getBillManageUI().getUIControl().getBillType();
		String pk_corp = _getCorp().getPrimaryKey();
		// 作废时删除所有的子子表信息   未完成
		super.onBoDel();
		//
		returnBillNo(billcode,pk_billtype,pk_corp);
	}
	
	protected void onBoElse(int intBtn) throws Exception {
		if(intBtn==ZbPuBtnConst.FLBTN){
			onAverageCal();
		}else if(intBtn == ZbPuBtnConst.Editor){//修改
			   onBoEdit();
		}else{
			super.onBoElse(intBtn);
		}
	}
	
	private void onAverageCal(){		
		if(getBufferData().isVOBufferEmpty()){
			getBillUI().showWarningMessage("无数据");
			return;
		}
		if(getBufferData().getCurrentRow()<0){
			getBillUI().showWarningMessage("请选择一张单据");
			return;
		}
		
		HYBillVO billvo = (HYBillVO)getBufferData().getCurrentVO();

		try {
			BidEvaluationHeaderVO.validateDataOnCommentsSplitNum(billvo);
		} catch (ValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			getBillUI().showWarningMessage(e.getMessage());
			return;
		}
		
		boolean flag =getAverageCalDlg().setData(billvo);
		if(flag){
			if(getAverageCalDlg().showModal() != UIDialog.ID_OK)
				return;
			BiEvaluationBodyVO[] bodys = getAverageCalDlg().getRetVos();
			getBufferData().getCurrentVO().setChildrenVO(bodys);
			BillTabbedPane tabPane = null;
			if(getBillManageUI().isListPanelSelected()){
				getBillListPanel().getBodyBillModel().setBodyDataVO(bodys);
				getBillListPanel().getBodyBillModel().execLoadFormula();
				tabPane = getBillListPanel().getBodyTabbedPane();
			}else{
				getBillCardPanelWrapper().getBillCardPanel().getBillModel().setBodyDataVO(bodys);
				getBillCardPanelWrapper().getBillCardPanel().getBillModel().execLoadFormula();
				tabPane = getBillCardPanelWrapper().getBillCardPanel().getBodyTabbedPane();
			}
			if(tabPane.getSelectedIndex() == 1){
				tabPane.setSelectedIndex(0);
			}
		}
	}
	
	private AverageCalDlg getAverageCalDlg(){		
		if(m_averageCalDlg == null)
			m_averageCalDlg = new AverageCalDlg(getBillManageUI());		
		return m_averageCalDlg;
	}
	public void onButton(ButtonObject bo) {
		ButtonObject parentBtn = bo.getParent();
		if (parentBtn == null || Integer.parseInt(parentBtn.getTag()) >=100) {
			if (bo.getTag() == null)
				System.out.println("新增按钮必须设置TAG,TAG>100的整数.....");
			int intBtn = Integer.parseInt(bo.getTag());
			if(intBtn!=ZbPuBtnConst.FLBTN)
				   setTabbedPane();
		}else {
			setTabbedPane();
		}
		super.onButton(bo);
	}
	//调整表体业签
	private void setTabbedPane(){
		if (getBillManageUI().isListPanelSelected()) {
			getBillListPanel().getBodyTabbedPane().setSelectedIndex(0);
		} else {
			getBillCardPanel().getBodyTabbedPane().setSelectedIndex(0);
		}
	}
	
//	打印实现---------------------------------------------------------------
	
//	if (print.selectTemplate() == 1)
//		print.preview();
	
	CommentsPrintTool print = null;
	private CommentsPrintTool getPringTool(){
		
		if(print == null){
			print = new CommentsPrintTool(getBillUI()._getCorp().getPrimaryKey(),getBillUI()._getOperator(),"4004090501",null,"pu",getPringModel());
		}
		return print;
	}
	
	private BillCardPanel printmodel = null;
	private BillCardPanel getPringModel(){
		if(printmodel == null){
			printmodel = new BillCardPanel();
			printmodel.loadTemplet("1002AA1000000001KXDV");
			printmodel.setTatolRowShow(true);
		}
		return printmodel;
	}
	
	private void dealData() throws Exception{
		HYBillVO bill2 = (HYBillVO)getBufferData().getCurrentVO();
		if(bill2 == null)
			return;
		HYBillVO bill = (HYBillVO)ObjectUtils.serializableClone(bill2);
		BiEvaluationBodyVO[] bodys = (BiEvaluationBodyVO[])bill.getChildrenVO();
		if(bodys == null || bodys.length ==0)
			return;
		List<EvaluationBodyPrintVO> lbody = new ArrayList<EvaluationBodyPrintVO>();
		EvaluationBodyPrintVO tmp = null;
		BidSlvendorVO[] vendors = null;
		String[] bodynames = bodys[0].getAttributeNames();
		String[] vendornames = new BidSlvendorVO().getAttributeNames();
 		for(BiEvaluationBodyVO body:bodys){
			vendors = body.getBidSlvendorVOs();
			tmp = new EvaluationBodyPrintVO();
			for(String name:bodynames){
				tmp.setAttributeValue(name, body.getAttributeValue(name));
			}
			lbody.add(tmp);
			if(vendors == null || vendors.length ==0){				
				continue;
			}
//			继续补充供应商内容
			for(String name:vendornames){				
				
				if(name.equalsIgnoreCase("cinvbasid"))
					continue;
				if(name.equalsIgnoreCase("cinvmanid"))
					continue;
				if(name.equalsIgnoreCase("nzbnum"))
					tmp.setAttributeValue("vnzbnum", vendors[0].getAttributeValue(name));
				else if(name.equalsIgnoreCase("vdef1")){
					tmp.setAttributeValue("vvdef1", vendors[0].getAttributeValue(name));
				}else if(name.equalsIgnoreCase("vdef2")){
					tmp.setAttributeValue("vvdef2", vendors[0].getAttributeValue(name));
				}else if(name.equalsIgnoreCase("vdef3")){
					tmp.setAttributeValue("vvdef3", vendors[0].getAttributeValue(name));
				}else if(name.equalsIgnoreCase("vdef4")){
					tmp.setAttributeValue("vvdef4", vendors[0].getAttributeValue(name));
				}else if(name.equalsIgnoreCase("vdef5")){
					tmp.setAttributeValue("vvdef5", vendors[0].getAttributeValue(name));
				}else if(name.equalsIgnoreCase("crowno")){
					tmp.setAttributeValue(name, ZbPubTool.getString_NullAsTrimZeroLen(
							body.getAttributeValue(name))
							+":"+ZbPubTool.getString_NullAsTrimZeroLen(vendors[0].getAttributeValue(name)));
				}else
				
				tmp.setAttributeValue(name, vendors[0].getAttributeValue(name));
//				tmp.setAttributeValue(name, vendors[0].getAttributeValue(name));
//				if(name.equalsIgnoreCase("crowno")){
//					tmp.setAttributeValue(name, ZbPubTool.getString_NullAsTrimZeroLen(
//							body.getAttributeValue(name))
//							+":10");
//				}
			}
			
			int index = 0;
			for(BidSlvendorVO vendor:vendors){
				if(index == 0){
					index++;
					continue;
				}
					
				tmp = new EvaluationBodyPrintVO();
				for(String name:vendornames){
					if(name.equalsIgnoreCase("cinvbasid"))
						continue;
					if(name.equalsIgnoreCase("cinvmanid"))
						continue;
					if(name.equalsIgnoreCase("nzbnum"))
						tmp.setAttributeValue("vnzbnum", vendor.getAttributeValue(name));
					else if(name.equalsIgnoreCase("vdef1")){
						tmp.setAttributeValue("vvdef1", vendor.getAttributeValue(name));
					}else if(name.equalsIgnoreCase("vdef2")){
						tmp.setAttributeValue("vvdef2", vendor.getAttributeValue(name));
					}else if(name.equalsIgnoreCase("vdef3")){
						tmp.setAttributeValue("vvdef3", vendor.getAttributeValue(name));
					}else if(name.equalsIgnoreCase("vdef4")){
						tmp.setAttributeValue("vvdef4", vendor.getAttributeValue(name));
					}else if(name.equalsIgnoreCase("vdef5")){
						tmp.setAttributeValue("vvdef5", vendor.getAttributeValue(name));
					}else if(name.equalsIgnoreCase("crowno")){
						tmp.setAttributeValue(name, ZbPubTool.getString_NullAsTrimZeroLen(
								body.getAttributeValue(name))
								+":"+ZbPubTool.getString_NullAsTrimZeroLen(vendor.getAttributeValue(name)));
					}else if(name.equalsIgnoreCase("cevaluationbid")){
						tmp.setAttributeValue("cevaluationbid", vendor.getAttributeValue(name));
					}else  
					tmp.setAttributeValue(name, vendor.getAttributeValue(name));
				}
				lbody.add(tmp);	
				continue;
			}
			
		}
 		if(lbody.size()>0){
 			bill.setChildrenVO(lbody.toArray(new EvaluationBodyPrintVO[0]));
 		}else
 			bill.setChildrenVO(null);
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
//		if(getBillManageUI().isListPanelSelected()){
//			
//		}else{
			bill = (HYBillVO)getPringModel().getBillValueVO(HYBillVO.class.getName(), BidEvalHeaderPrintVO.class.getName(), EvaluationBodyPrintVO.class.getName());
//		}
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
//		if(getBillManageUI().isListPanelSelected()){
//			
//		}else{
			bill = (HYBillVO)getPringModel().getBillValueVO(HYBillVO.class.getName(), BidEvalHeaderPrintVO.class.getName(), EvaluationBodyPrintVO.class.getName());
//		}
		if(bill == null){
			getBillUI().showWarningMessage("无数据");
			return;
		}
			
		getPringTool().print(bill);
	
	}

}
