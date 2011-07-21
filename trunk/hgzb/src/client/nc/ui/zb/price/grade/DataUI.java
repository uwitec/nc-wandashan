package nc.ui.zb.price.grade;

import javax.swing.ListSelectionModel;

import nc.ui.pub.bill.BillListPanel;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.zb.pub.ZbPubConst;

public class DataUI extends BillListPanel {

	private PriceGradeUI ui = null;
	
	public DataUI(PriceGradeUI ui,String sUser,String corpid){
		super();
		loadTemplet(ZbPubConst.ZB_PRICE_GRADE, null, sUser, corpid);
		this.ui = ui;
		getHeadTable().removeSortListener();
		getBodyTable().removeSortListener();
		getHeadTable().getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
//		initListener();
	}
//	private void initListener(){
//		getBodyScrollPane("zb_bidding_b").addEditListener(ui.getHandler());
//		getParentListPanel().addEditListener(ui.getHandler());
////		getHeadBillModel().addSortRelaObjectListener2(new IBillRelaSortListener2(){
////
////			public Object[] getRelaSortObjectArray() {
////				return ui.getDataBuffer().getCurrAllVendors();
////			}
////			
////		});
//	}
	
	public void setDataToUI(){
//		供应商页签数据设置
		CircularlyAccessibleValueObject[] heads  =(CircularlyAccessibleValueObject[]) ui.getDataBuffer().getCurrAllVendors();
		getHeadBillModel().setBodyDataVO(heads);
		if(heads!=null){
			int len = heads.length;
			for(int i=0;i<len;i++){
				if(PuPubVO.getString_TrimZeroLenAsNull(heads[i].getAttributeValue("ccustbasid"))==null){
					String[] formulas = new String[]{
							"vendorname->getColvalue(bd_cubasdochg,custname,ccubasdochgid,ccustmanid)",
							"vendorcode->getColvalue(bd_cubasdochg,vbillno,ccubasdochgid,ccustmanid)"
							};
					getHeadBillModel().execFormulas(i, formulas);
				}else{
					String[] formulas = new String[]{
							"vendorname->getColvalue(bd_cubasdoc,custname,pk_cubasdoc,ccustbasid)",
							"vendorcode->getColvalue(bd_cubasdoc,custcode,pk_cubasdoc,ccustbasid)"
							};
					getHeadBillModel().execFormulas(i, formulas);
				}
			}
		}
		getHeadBillModel().execLoadFormula();
	}
	
	public void clearDataOnTreeSel(){
		getHeadBillModel().clearBodyData();
		getBodyBillModel().clearBodyData();
	}
	
	/**
	 * 
	 * @author zhf 供应商页签表头行变换事件处理
	 *
	 */
//	class VendorPaneHeadListSelListener implements ListSelectionListener{

		public void vendorPaneHeadRowChange(int row) {
			// TODO Auto-generated method stub
//			int row = getVendorPane().getHeadTable().getSelectedRow();
			ui.getDataBuffer().setVendorSelRow(row);
			ui.setButtonState();
			if(row < 0)
				return;
			getBodyBillModel().setBodyDataVO(ui.getDataBuffer().getCurrPricesOnSelVendor());
			getBodyBillModel().execLoadFormula();
		}
}
