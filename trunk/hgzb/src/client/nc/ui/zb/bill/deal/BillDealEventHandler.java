package nc.ui.zb.bill.deal;

import java.util.List;

import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillModel;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.zb.bill.deal.DealVendorBillVO;
import nc.vo.zb.pub.ZbPubTool;

public class BillDealEventHandler implements BillEditListener{
	private BillDealUI ui = null;
	private ClientLink cl = null;
	public BillDealEventHandler(BillDealUI ui2){
		ui = ui2;
		cl = new ClientLink(ClientEnvironment.getInstance());
	}
	public void onOk(){
		List<DealVendorBillVO> ldata = ui.getDataBuffer().getSelVendorInfor();
		if(ldata == null || ldata.size() ==0){
			ui.showHintMessage("本次评标无供应商入围");
			return;
		}
		
		int vendornum = ui.getDataBuffer().getIVendorNum();
		int num = ldata.size();
		String error = "";
		if(vendornum>num){
			error = "本次入围供应商数量小于标书设置的[入围供应商数量]";
		}else if(vendornum<num){
			error = "本次入围供应商数量超过标书设置的[入围供应商数量]";
		}
		if(PuPubVO.getString_TrimZeroLenAsNull(error)!=null){
			ui.showErrorMessage(error);
			return;
		}
			
		
//		转后台处理
		try {
			BillDealHelper.doOK(ui, ldata,cl);
		} catch (Exception e) {
			e.printStackTrace();
			ui.showErrorMessage(ZbPubTool.getString_NullAsTrimZeroLen(e.getMessage()));
			ui.showHintMessage(ZbPubTool.getString_NullAsTrimZeroLen(e.getMessage()));
			return;
		}
//		前台数据重构
		ui.getTreeUI().deleteNodeFromTree();
		ui.getDataUI().clearDataOnTreeSel();
		ui.getDataBuffer().clearOnTreeSel();	
	}
	
	public void onAllSel(){
		DealVendorBillVO[] vos = ui.getDataBuffer().getVendorInfor(ui.getDataBuffer().getCurrentBidding());
		if(vos == null || vos.length == 0){
			ui.showHintMessage("无供应商数据");
			return;
		}
		ui.getDataBuffer().clearSelVendorInfor();

		for(DealVendorBillVO vo:vos){
			ui.getDataBuffer().addSelVendor(vo);
		}

		int rowcount = ui.getDataUI().getVendorPane().getHeadTable().getRowCount();
		for(int i=0;i<rowcount;i++){
			ui.getDataUI().getVendorPane().getHeadBillModel().setValueAt(UFBoolean.TRUE, i, "bsel");
		}
	}
	public void onNoSel(){
		int rowcount =  ui.getDataUI().getVendorPane().getHeadTable().getRowCount();
		if(rowcount <= 0)
			return;
		for(int i=0;i<rowcount;i++){
			ui.getDataUI().getVendorPane().getHeadBillModel().setValueAt(UFBoolean.FALSE, i, "bsel");
		}
		ui.getDataBuffer().clearSelVendorInfor();
	}

	private BillModel getVendorHeadPane(){
		return ui.getDataUI().getVendorPane().getHeadBillModel();
	}
	public void afterEdit(BillEditEvent e) {
		int row = e.getRow();
		if(row < 0)
			return;
		if(e.getKey().equalsIgnoreCase("bsel")){
			UFBoolean bsel = PuPubVO.getUFBoolean_NullAs(getVendorHeadPane().getValueAt(row, "bsel"), UFBoolean.FALSE);
			ui.getDataBuffer().setVendorSelRow(row);//zhf add  防止用户未选择行直接勾选 选择框 情况
			if(bsel.booleanValue()){
				ui.getDataBuffer().addSelVendor(null);
			}else{
				ui.getDataBuffer().removeSelVendor(null);
			}   
		}	
	}

	public void bodyRowChange(BillEditEvent e) {
		// TODO Auto-generated method stub
		ui.getDataUI().vendorPaneHeadRowChange(e.getRow());
	}
}
