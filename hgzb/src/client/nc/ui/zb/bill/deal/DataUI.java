package nc.ui.zb.bill.deal;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillTabbedPane;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.zb.bill.deal.DealVendorPriceBVO;
import nc.vo.zb.pub.ZbPubConst;

public class DataUI extends BillTabbedPane {

	private BillDealUI ui = null;
	private ClientLink cl = null;
	private BillListPanel m_invpane = null;
	private BillListPanel m_vendorpane = null;
	public DataUI(BillDealUI ui){
		super();
		cl = new ClientLink(ClientEnvironment.getInstance());
		this.ui = ui;
		initListener();
		initData();
	}
	private void initListener(){
		getInvPane().getHeadTable().getSelectionModel().addListSelectionListener(new InvPaneHeadListSelListener());
//		getVendorPane().getHeadTable().getSelectionModel().addListSelectionListener(new VendorPaneHeadListSelListener());
//		getInvPane().getHeadBillModel().addSortRelaObjectListener2(new IBillRelaSortListener2(){
//
//			public Object[] getRelaSortObjectArray() {
//				// TODO Auto-generated method stub
//				return ui.getDataBuffer().getCurrAllInvs();
//			}
//			
//		});
//		getVendorPane().getHeadBillModel().addSortRelaObjectListener2(new IBillRelaSortListener2(){
//
//			public Object[] getRelaSortObjectArray() {
//				// TODO Auto-generated method stub
//				return ui.getDataBuffer().getCurrAllVendors();
//			}
//			
//		});
	}
	
	
	
	public BillListPanel getInvPane(){
		if(m_invpane == null){
			m_invpane =  new BillListPanel();
			m_invpane.loadTemplet(ZbPubConst.ZB_BILL_BILLTYPE_INV, null, cl.getUser(), cl.getCorp());
			m_invpane.getHeadTable().removeSortListener();//不允许排序
			m_invpane.getBodyTable().removeSortListener();
		}
		return m_invpane;
	}
	
	public BillListPanel getVendorPane(){
		if(m_vendorpane == null){
			m_vendorpane =  new BillListPanel();
			m_vendorpane.loadTemplet(ZbPubConst.ZB_BILL_BILLTYPE_VENDOR, null, cl.getUser(), cl.getCorp());
			m_vendorpane.getHeadTable().removeSortListener();
			m_vendorpane.getBodyTable().removeSortListener();
			m_vendorpane.setEnabled(true);
		}
		return m_vendorpane;
	}

	private void initData(){
		cl = new ClientLink(ClientEnvironment.getInstance());
		addTab("投标供应商", getVendorPane());
		addTab("招标品种", getInvPane());		
	}
//	public void clearData(){
//		clearDataOnTreeSel();
//	}
	
	public void setDataToUI(){
//		供应商页签数据设置
		CircularlyAccessibleValueObject[] heads  =(CircularlyAccessibleValueObject[]) ui.getDataBuffer().getCurrAllVendors();
		getVendorPane().getHeadBillModel().setBodyDataVO(heads);
		if(heads!=null){
			int len = heads.length;
			for(int i=0;i<len;i++){
				if(PuPubVO.getString_TrimZeroLenAsNull(heads[i].getAttributeValue("ccustbasid"))==null){
					String[] formulas = new String[]{
							"vendorname->getColvalue(bd_cubasdochg,custname,ccubasdochgid,ccustmanid)",
							"vendorcode->getColvalue(bd_cubasdochg,vbillno,ccubasdochgid,ccustmanid)"
							};
					getVendorPane().getHeadBillModel().execFormulas(i, formulas);
				}else{
					String[] formulas = new String[]{
							"vendorname->getColvalue(bd_cubasdoc,custname,pk_cubasdoc,ccustbasid)",
							"vendorcode->getColvalue(bd_cubasdoc,custcode,pk_cubasdoc,ccustbasid)"
							};
					getVendorPane().getHeadBillModel().execFormulas(i, formulas);
				}
			}
		}
		getVendorPane().getHeadBillModel().execLoadFormula();
//		品种页签数据设置
        getInvPane().getHeadBillModel().setBodyDataVO(ui.getDataBuffer().getCurrAllInvs());
        getInvPane().getHeadBillModel().execLoadFormula();
		
	}
	
	public void clearDataOnTreeSel(){
		getVendorPane().getHeadBillModel().clearBodyData();
		getInvPane().getHeadBillModel().clearBodyData();
		getVendorPane().getBodyBillModel().clearBodyData();
		getInvPane().getBodyBillModel().clearBodyData();
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
			if(row < 0)
				return;
			getVendorPane().getBodyBillModel().setBodyDataVO(ui.getDataBuffer().getCurrPricesOnSelVendor());
			getVendorPane().getBodyBillModel().execLoadFormula();
		}
		
//	}
	
	/**
	 * 
	 * @author zhf 品种页签表头行变换事件处理
	 *
	 */
	class InvPaneHeadListSelListener implements ListSelectionListener{

		public void valueChanged(ListSelectionEvent e) {
			// TODO Auto-generated method stub
			int row = getInvPane().getHeadTable().getSelectedRow();
			ui.getDataBuffer().setInvSelRow(row);
			if(row < 0)
				return;
			
			//供应商数据设置
			DealVendorPriceBVO[] heads  = ui.getDataBuffer().getCurrPricesOnSelInv();
				getInvPane().getBodyBillModel().setBodyDataVO(heads);
			if(heads!=null){
				int len = heads.length;
				for(int i=0;i<len;i++){
					if(PuPubVO.getString_TrimZeroLenAsNull(heads[i].getAttributeValue("ccustbasid"))==null){
						String[] formulas = new String[]{
								"vendorname->getColvalue(bd_cubasdochg,custname,ccubasdochgid,ccustmanid)",
								"vendorcode->getColvalue(bd_cubasdochg,vbillno,ccubasdochgid,ccustmanid)"
								};
						getInvPane().getBodyBillModel().execFormulas(i, formulas);
					}else{
						String[] formulas = new String[]{
								"vendorname->getColvalue(bd_cubasdoc,custname,pk_cubasdoc,ccustbasid)",
								"vendorcode->getColvalue(bd_cubasdoc,custcode,pk_cubasdoc,ccustbasid)"
								};
						getInvPane().getBodyBillModel().execFormulas(i, formulas);
					}
				}
			}
			getInvPane().getBodyBillModel().execLoadFormula();
		}
		
	}
}
