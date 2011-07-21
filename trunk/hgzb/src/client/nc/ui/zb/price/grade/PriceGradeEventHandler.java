package nc.ui.zb.price.grade;

import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.zb.bill.deal.DealInvPriceBVO;
import nc.vo.zb.bill.deal.DealVendorBillVO;
import nc.vo.zb.price.grade.PriceGradeVO;
import nc.vo.zb.pub.ZbPubTool;

public class PriceGradeEventHandler implements BillEditListener{
	private PriceGradeUI ui = null;
	private ClientLink cl = null;
	public PriceGradeEventHandler(PriceGradeUI ui2){
		ui = ui2;
		cl = new ClientLink(ClientEnvironment.getInstance());
	}
	
	public boolean onOk(){

		if(!ui.getDataBuffer().isAdjust()){
			ui.showErrorMessage("�Ƿ�����");
			return false;
		}
		
		DealVendorBillVO billvo = ui.getDataBuffer().getCurrentVendor();
		
		if(billvo == null){
			ui.showWarningMessage("���ݻ�ȡ�쳣");
			return false;
		}	
		
		UFDouble nheadgrade = PuPubVO.getUFDouble_NullAsZero(ui.getDataUI().getHeadBillModel().getValueAt(ui.getDataBuffer().getVendorSelRow(), "nquotatpoints"));
		billvo.getHeader().setNquotatpoints(nheadgrade);
		
		DealInvPriceBVO[]  bodys = billvo.getBodys();
		int len = bodys.length;
		if(ui.getDataUI().getBodyBillModel().getRowCount()!=len){
			ui.showErrorMessage("�����쳣����ˢ�½������²���");
			return false;
		}
			
		for(int row = 0;row< len;row ++){
			bodys[row].setNadjgrade(PuPubVO.getUFDouble_NullAsZero(ui.getDataUI().getBodyBillModel().getValueAt(row, "nadjgrade")));
		}
		
			
		try {
			PriceGradeVO.validateDataOnOk(billvo, ui.getDataBuffer().getPara().getNmaxquotatpoints());	
			//		ת��̨����

			PriceGradeHelper.doOK(ui, billvo,cl);
		} catch (Exception e) {
			e.printStackTrace();
			ui.showErrorMessage(ZbPubTool.getString_NullAsTrimZeroLen(e.getMessage()));
			ui.showHintMessage(ZbPubTool.getString_NullAsTrimZeroLen(e.getMessage()));
			return false;
		}
		ui.getDataUI().setEnabled(false);
		ui.getDataBuffer().setAdjustFlag(false);
//		ui.setbu
		ui.showHintMessage("�������");
		return true;
	}
	
	private DealVendorBillVO[] getSelVendors(){
		DealVendorBillVO[] bills = ui.getDataBuffer().getVendorInfor(null);

		if(bills == null || bills.length == 0){
			ui.showWarningMessage("��ǰ����Ϊ��");
			return null;
		}

		int[] rows = ui.getDataUI().getHeadTable().getSelectedRows();

		if(rows == null || rows.length == 0){
			ui.showErrorMessage("��ѡ��Ҫ����Ĺ�Ӧ��");
			return null;
		}
		DealVendorBillVO[] vendors = new DealVendorBillVO[rows.length];
		int index = 0;
		for(int row:rows){
			vendors[index] = bills[row];
			index ++;
		}
		return vendors;
	}
	
	
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ�� ��Ӧ�̵÷ּ���
	 * 2011-5-23����12:32:07
	 */
	public void onCol(){
		
//		ȷ��ʱ  �Ա����   �÷ֽ��л�д��   ���ݿ�
		
		DealVendorBillVO[] bills = getSelVendors();
		
		if(bills == null || bills.length == 0){
			ui.showWarningMessage("��ǰ����Ϊ��");
			return;
		}
			
		try {
			PriceGradeVO.validateDataOnCol(bills, ui.getDataBuffer().getPara());
		

//		ת��̨����
		
			PriceGradeHelper.doCol(ui, bills,cl);
		} catch (Exception e) {
			e.printStackTrace();
			ui.showErrorMessage(ZbPubTool.getString_NullAsTrimZeroLen(e.getMessage()));
			ui.showHintMessage(ZbPubTool.getString_NullAsTrimZeroLen(e.getMessage()));
			return;
		}
		ui.showHintMessage("�������");

		ui.freeshData();
		
		int row = ui.getDataBuffer().getVendorSelRow();
		
//		�÷ּ������
		ui.getDataUI().setDataToUI();
		
		ui.getDataUI().getHeadTable().getSelectionModel().setSelectionInterval(row, row);
		ui.getDataBuffer().setVendorSelRow(row);		
	}
	
//	private boolean isedit = false;
	
	public void onAdjust(){
		if(ui.getDataBuffer().isAdjust()){
			ui.showErrorMessage("�Ƿ�����");
			return;
		}
		ui.getDataUI().setEnabled(true);
		ui.getDataBuffer().setAdjustFlag(true);
	}
	public void onCancel(){
		if(!ui.getDataBuffer().isAdjust()){
			ui.showErrorMessage("�Ƿ�����");
			return;
		}
		ui.getDataBuffer().setAdjustFlag(false);
		ui.getDataUI().setEnabled(false);
		int row = ui.getDataBuffer().getVendorSelRow();
		ui.getDataUI().clearDataOnTreeSel();
		ui.getDataUI().setDataToUI();
		ui.getDataUI().getHeadTable().getSelectionModel().setSelectionInterval(row, row);
		ui.getDataBuffer().setVendorSelRow(row);
		
	}

	public void bodyAfterEdit(BillEditEvent e) {
		int row = e.getRow();
		if(row < 0)
			return;
		if(e.getKey().equalsIgnoreCase("nadjgrade")){
			UFDouble nadj = PuPubVO.getUFDouble_NullAsZero(e.getValue());
			UFDouble onadj = PuPubVO.getUFDouble_NullAsZero(e.getOldValue());
			nadj =nadj.sub(onadj);
//			DealVendorBillVO bill = ui.getDataBuffer().getCurrentVendor();
//			bill.getBodys()[row].setNadjgrade(nadj);
			UFDouble nheadgrade = PuPubVO.getUFDouble_NullAsZero(ui.getDataUI().getHeadBillModel().getValueAt(ui.getDataBuffer().getVendorSelRow(), "nquotatpoints"));
//			bill.getHeader().setNquotatpoints(nheadgrade.add(nadj.div(2)));
			int len =ui.getDataUI().getBillListData().getBodyBillModel().getRowCount();
			ui.getDataUI().getHeadBillModel().setValueAt(nheadgrade.add(nadj.div(len)), ui.getDataBuffer().getVendorSelRow(), "nquotatpoints");
			//�����ܷ�
			ui.getDataUI().getHeadBillModel().execEditFormulaByKey(ui.getDataBuffer().getVendorSelRow(), "nquotatpoints");
			
//			DealVendorPriceBVO head = ui.getDataBuffer().getCurrentVendor().getHeader();
//			head.setNquotatpoints(PuPubVO.getUFDouble_NullAsZero(e.getValue()));
//			ui.getDataUI().getHeadBillModel().setValueAt(head.getNqualipoints().add(head.getNquotatpoints()), ui.getDataBuffer().getVendorSelRow(), "nallgrade");
		}	
	}
	
	public void itemRowChange(BillEditEvent e) {
		// TODO Auto-generated method stub
//		ui.getDataUI().vendorPaneHeadRowChange(e.getRow());
		
	}
	
	public void afterEdit(BillEditEvent e) {
//		if(e.getKey().equalsIgnoreCase("nquotatpoints")){
//			DealVendorPriceBVO head = ui.getDataBuffer().getCurrentVendor().getHeader();
//			head.setNquotatpoints(PuPubVO.getUFDouble_NullAsZero(e.getValue()));
//			ui.getDataUI().getHeadBillModel().setValueAt(head.getNqualipoints().add(head.getNquotatpoints()), ui.getDataBuffer().getVendorSelRow(), "nallgrade");
//		}
	}

	public void bodyRowChange(BillEditEvent e) {
		// TODO Auto-generated method stub
		int row = e.getRow();
		if(row<0)
			return;
		if(ui.getDataBuffer().isAdjust()){
			if(ui.showYesNoCancelMessage("�Ƿ񱣴�Թ�Ӧ�̱��۷ֵĵ���?")==UIDialog.ID_YES){
				boolean flag = onOk();
				if(!flag){
					ui.showErrorMessage("��ˢ���������²���");
					return;
				}
			}else{
				ui.getDataUI().setEnabled(false);
				ui.getDataBuffer().setAdjustFlag(false);
			}
		}
		ui.getDataUI().vendorPaneHeadRowChange(e.getRow());
	}
}
