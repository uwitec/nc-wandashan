package nc.ui.wds.ic.allocation.in;
import java.util.ArrayList;
import java.util.List;

import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.scm.util.ObjectUtils;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.wds.ic.pub.InPubEventHandler;
import nc.ui.wds.ic.pub.MutiInPubClientUI;
import nc.ui.wds.pub.print.WdsWlPrintTool;
import nc.ui.wds.w8004040214.buttun0214.ISsButtun;
import nc.ui.wl.pub.BeforeSaveValudate;
import nc.uif.pub.exception.UifException;
import nc.vo.ic.pub.TbGeneralBBVO;
import nc.vo.ic.pub.TbGeneralBVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.bill.BillRendererVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.wds.ic.allo.in.AlloInBodyPrintVO;
import nc.vo.wds.ic.allo.in.AlloInHeadPrintVO;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.WdsWlPubTool;

public class AlloInEventHandler extends InPubEventHandler {
	public AlloInEventHandler(MutiInPubClientUI billUI, IControllerBase control) {
		super(billUI, control);
	}
	
	protected void onBoElse(int intBtn) throws Exception {
		super.onBoElse(intBtn);
		switch (intBtn) {
			case ISsButtun.Zdtp:

				//��� ���ΨһУ��
				BeforeSaveValudate.beforeSaveBodyUnique(getBillCardPanelWrapper().getBillCardPanel().getBillTable(),
						getBillCardPanelWrapper().getBillCardPanel().getBillModel(),
						new String[]{"invcode","geb_vbatchcode"},
						new String[]{"�������","���κ�"});

				valuteRowNum();

			//	onZdtp();
				break;
			case ISsButtun.Ckmx:
			//	onCkmx();
				break;
			case ISsButtun.Zdrk:

				//��� ���ΨһУ��
				BeforeSaveValudate.beforeSaveBodyUnique(getBillCardPanelWrapper().getBillCardPanel().getBillTable(),
						getBillCardPanelWrapper().getBillCardPanel().getBillModel(),
						new String[]{"invcode","geb_vbatchcode"},
						new String[]{"�������","���κ�"});

				valuteRowNum();
			//	setTrayCatNUll();

				//onZdrk();
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
	protected void setRefData(AggregatedValueObject[] vos) throws Exception {
		// TODO Auto-generated method stub
		super.setRefData(vos);
		//��� ���յ����ֿ�Ϊ�� ����Ĭ�ϲֿ�Ϊ��ǰ����Ա�ֿ�
		setInitWarehouse("geh_cwarehouseid");
	}
	
//	/**
//	 * 
//	 * @���ߣ�mlr
//	 * @˵�������ɽ������Ŀ 
//	 *       �Զ����ǰ�趨������ϢΪ��    
//	 * @ʱ�䣺2011-7-26����12:58:02
//	 */
//	private void setTrayCatNUll() {		
//	  if(getBillInPubUI()==null){
//		  return;
//	  }
//	//  getBillInPubUI().setTrayInfor(null);
//	}

	@Override
	protected UIDialog createQueryUI() {
		return new QueryDIG(getBillUI(), null, _getCorp().getPk_corp(), getBillUI().getModuleCode(), getBillUI()._getOperator(), null		
		);
	}
	/**
	 * 
	 * @����  mlr 
	 * @˵�������ɽ������Ŀ
	 *       У������кŲ������ظ� 
	 * @ʱ�䣺2011-7-26����12:46:08
	 */
    public void valuteRowNum()throws Exception{  	
       BeforeSaveValudate.FieldBodyUnique(getBodyRowCount(),getBillCardPanelWrapper().getBillCardPanel().getBillModel(), "geb_crowno", "�����к�");
    }

	@Override
	protected String getHeadCondition() {
		StringBuffer strWhere = new StringBuffer();
		strWhere.append( "  geh_cbilltypecode");
		strWhere.append(" ='"+WdsWlPubConst.BILLTYPE_ALLO_IN+"'");
		strWhere.append(" and pk_corp = '"+_getCorp().getPrimaryKey()+"'");
		
		return   strWhere.toString();	
	
	}

	public void showZeroLikeNull(boolean bShowZeroLikeNull) {
		BillRendererVO vo = getBillCardPanelWrapper().getBillCardPanel()
				.getBodyPanel().getRendererVO();
		vo.setShowZeroLikeNull(bShowZeroLikeNull);
		getBillCardPanelWrapper().getBillCardPanel().getBodyPanel()
				.setShowFlags(vo);
	}
	
	
//	��ӡʵ��---------------------------------------------------------------
	
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
				throw new BusinessException("��������Ϣ���к�"+WdsWlPubTool.getString_NullAsTrimZeroLen(body.getGeb_crowno()));

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
	 * ��ťm_boPrint���ʱִ�еĶ���,���б�Ҫ���븲��.
	 */
//	protected void onBoPrint() throws Exception {
//		if(getBufferData().isVOBufferEmpty())
//			throw new BusinessException("������");
//		if(getBufferData().getCurrentRow()< 0)
//			throw new BusinessException("��ѡ������");
//		//		�ܹؼ���һ��
//		dealData();
//
//		HYBillVO bill = null;
//
//		bill = (HYBillVO)getPringModel().getBillValueVO(HYBillVO.class.getName(), 
//				AlloInHeadPrintVO.class.getName(), 
//				AlloInBodyPrintVO.class.getName());
//		if(bill == null){
//			getBillUI().showWarningMessage("������");
//			return;
//		}
//
//		getPringTool().priview(bill);
//	}
	
	@Override  //xjx  add 
	protected void onBoPrint() throws Exception {
		//��������б���棬ʹ��ListPanelPRTS����Դ
		if( getBillManageUI().isListPanelSelected() ){
			nc.ui.pub.print.IDataSource dataSource = new MyListDbTyDateSource(getBillUI()
					._getModuleCode(),((BillManageUI) getBillUI()).getBillListPanel());
			nc.ui.pub.print.PrintEntry print = new nc.ui.pub.print.PrintEntry(null,
					dataSource);
			print.setTemplateID(getBillUI()._getCorp().getPrimaryKey(), getBillUI()
					._getModuleCode(), getBillUI()._getOperator(), getBillUI()
					.getBusinessType(), getBillUI().getNodeKey());
			if (print.selectTemplate() == 1)
				print.preview();
		}else{
		final nc.ui.pub.print.IDataSource dataSource = new MyDbTyDateSource(
				getBillUI()._getModuleCode(), getBillCardPanelWrapper()
						.getBillCardPanel());
		final nc.ui.pub.print.PrintEntry print = new nc.ui.pub.print.PrintEntry(
				null, dataSource);
		print.setTemplateID(getBillUI()._getCorp().getPrimaryKey(),
				getBillUI()._getModuleCode(), getBillUI()._getOperator(),
				getBillUI().getBusinessType(), getBillUI().getNodeKey());
		if (print.selectTemplate() == 1)
			print.preview();
		// ��������Դ��֧�����̴�ӡ
		}
	//	super.onBoPrint();
		Integer iprintcount = PuPubVO.getInteger_NullAs(getBufferData()
				.getCurrentVO().getParentVO().getAttributeValue(
						"cdt_pk"), 0);
		iprintcount = iprintcount + 1;
		getBufferData().getCurrentVO().getParentVO().setAttributeValue(
				"iprintcount", iprintcount);
		try {
			HYPubBO_Client.update((SuperVO) getBufferData().getCurrentVO()
					.getParentVO());
			onBoRefresh();
		} catch (final UifException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		   
	    }
	}
	
	protected void onBoDirectPrint() throws Exception {

		if(getBufferData().isVOBufferEmpty())
			throw new BusinessException("������");
		if(getBufferData().getCurrentRow()< 0)
			throw new BusinessException("��ѡ������");
		//		�ܹؼ���һ��
		dealData();

		HYBillVO bill = null;
		bill = (HYBillVO)getPringModel().getBillValueVO(HYBillVO.class.getName(), 
				AlloInHeadPrintVO.class.getName(), 
				AlloInBodyPrintVO.class.getName());
		if(bill == null){
			getBillUI().showWarningMessage("������");
			return;
		}

		getPringTool().print(bill);

	}
//	@Override
//	protected void onBoLineCopy() throws Exception {
//		int selectedRow = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
//		UFDouble num = PuPubVO.getUFDouble_NullAsZero(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(selectedRow, "geb_anum"));
//		if(num.doubleValue()>0){
//			throw new BusinessException("�Ѽ�����,���в����ٸ���");
//		}
//		super.onBoLineCopy();
//	}

	@Override
	protected String getBillType() {
		return WdsWlPubConst.BILLTYPE_ALLO_IN;
	}
	

}