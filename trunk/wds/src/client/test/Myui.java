package test;

import java.util.ArrayList;

import nc.bs.framework.common.NCLocator;
import nc.bs.ic.mm.pub.GenMethod;
import nc.itf.uap.sfapp.IBillcodeRuleService;
import nc.ui.ic.pub.bill.GeneralBillClientUI;
import nc.ui.pub.bill.BillEditEvent;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.billcodemanage.BillCodeObjValueVO;

public class Myui extends GeneralBillClientUI {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void afterBillEdit(BillEditEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void afterBillItemSelChg(int row, int col) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean beforeBillItemEdit(BillEditEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void beforeBillItemSelChg(int row, int col) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean checkVO(GeneralBillVO voBill) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void initPanel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void selectBillOnListPanel(int billIndex) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void setButtonsStatus(int billMode) {
		// TODO Auto-generated method stub
		
	}
	//回存ERP
	@Override
	protected void saveNewBill(GeneralBillVO voNewBill) throws Exception {
		// TODO Auto-generated method stub
		GeneralBillHeaderVO gvo=new GeneralBillHeaderVO();
		
		GeneralBillItemVO[] gvos=new GeneralBillItemVO[1];
		GeneralBillVO voTempBill=new GeneralBillVO();
		voTempBill.setParentVO(gvo);
		voTempBill.setChildrenVO(gvos);
		ArrayList alPK;
		//回写方法
		alPK = (ArrayList) nc.ui.pub.pf.PfUtilClient.processAction("SAVE",
					m_sBillTypeCode/*单据类型*/, m_sLogDate, voTempBill);
		//
		super.saveNewBill(voNewBill);
	}
	public void test1(){
		//nc.ui.to.to103.ApplicationDisposePanel.onFast();
		
		//
	
	}
	//获得单据号
	public String getBillCode(String billtype, String pkcorp, String gcbm, String operator) throws BusinessException {
		String scddh=null;
		try {
			BillCodeObjValueVO vo = new BillCodeObjValueVO();
			String[] names = { "库存组织", "操作员" };
			String[] values = new String[] { gcbm, operator };
			vo.setAttributeValue(names, values);
			scddh = getBillCode(billtype, pkcorp,vo);
		}
		catch (Exception e) {
			GenMethod.throwBusiException(e);
		}
		return scddh;
	}
	private String getBillCode(String billtype, String pkcorp,nc.vo.pub.billcodemanage.BillCodeObjValueVO billVO) throws BusinessException {
		String djh = null;
		try {
			IBillcodeRuleService bo = (IBillcodeRuleService)NCLocator.getInstance().lookup(IBillcodeRuleService.class.getName());
			djh = bo.getBillCode_RequiresNew(billtype, pkcorp, null, billVO);
			
		}
		catch (Exception e) {
			GenMethod.throwBusiException(e);
		}
		return djh;
	}

}
