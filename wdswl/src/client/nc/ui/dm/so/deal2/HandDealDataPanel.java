package nc.ui.dm.so.deal2;

import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillTabbedPane;
import nc.vo.dm.so.deal.SoDealVO;
import nc.vo.dm.so.deal2.SoDealBillVO;
import nc.vo.dm.so.deal2.StoreInvNumVO;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.WdsWlPubTool;

public class HandDealDataPanel extends BillTabbedPane {

	private HandDealDLG ui = null;
	private ClientLink cl = null;
	private BillListPanel m_custpane = null;
	private BillListPanel m_dealpane = null;
	public HandDealDataPanel(HandDealDLG ui){
		super();
		cl = new ClientLink(ClientEnvironment.getInstance());
		this.ui = ui;
		initListener();
		initData();
	}
	private void initListener(){
		getCustPane().addHeadEditListener(new CustPaneHeadListSelListener());
		getDealPane().addHeadEditListener(new DealPaneHeadListSelListener());
	}

	public BillListPanel getCustPane(){
		if(m_custpane == null){
			m_custpane =  new BillListPanel();
			m_custpane.loadTemplet(WdsWlPubConst.WDS4_2_1, null, cl.getUser(), cl.getCorp());
			m_custpane.getHeadTable().removeSortListener();//����������
			m_custpane.getBodyTable().removeSortListener();
		}
		return m_custpane;
	}

	public BillListPanel getDealPane(){
		if(m_dealpane == null){
			m_dealpane =  new BillListPanel();
			m_dealpane.loadTemplet(WdsWlPubConst.WDS4_2_2, null, cl.getUser(), cl.getCorp());
			m_dealpane.getHeadTable().removeSortListener();
			m_dealpane.getBodyTable().removeSortListener();
			m_dealpane.setEnabled(true);
		}
		return m_dealpane;
	}

	private void initData(){
		addTab("���Ž���", getCustPane());
		addTab("�ͻ���������Ϣ", getDealPane());		
	}

	public void setDataToUI(){
		clearPanel();
		//		�ͻ�ҳǩ��������
		if(!ui.getBuffer().isCustEmpty()){
			SoDealBillVO[] bills = ui.getBuffer().getLcust().toArray(new SoDealBillVO[0]);
			getCustPane().getHeadBillModel().setBodyDataVO(WdsWlPubTool.getParentVOFromAggBillVo(bills, SoDealHealper.class));
			getCustPane().getBodyBillModel().setBodyDataVO(bills[0].getChildrenVO());
			getCustPane().getHeadBillModel().execLoadFormula();
			getCustPane().getBodyBillModel().execLoadFormula();
		}


		//		��������ҳǩ��������
		if(!ui.getBuffer().isNumEmpty()){
			StoreInvNumVO[] nums = ui.getBuffer().getLnum().toArray(new StoreInvNumVO[0]);
			getDealPane().getHeadBillModel().setBodyDataVO(nums);
			getDealPane().getBodyBillModel().setBodyDataVO(nums[0].getLdeal().toArray(new SoDealVO[0]));
			getDealPane().getHeadBillModel().execLoadFormula();
			getDealPane().getBodyBillModel().execLoadFormula();
		}		
	}

	public void clearPanel(){
		getCustPane().getHeadBillModel().clearBodyData();
		getCustPane().getBodyBillModel().clearBodyData();
		getDealPane().getHeadBillModel().clearBodyData();
		getDealPane().getBodyBillModel().clearBodyData();
	}

	/**
	 * 
	 * @author zhf ��Ӧ��ҳǩ��ͷ�б任�¼�����
	 *
	 */
	class CustPaneHeadListSelListener implements BillEditListener{

		public void afterEdit(BillEditEvent e) {
			// TODO Auto-generated method stub

		}

		public void bodyRowChange(BillEditEvent e) {
			// TODO Auto-generated method stub
			int row = e.getRow();
			ui.getBuffer().setCustSelRow(row);
			getCustPane().getBodyBillModel().clearBodyData();
			if(row<0)
				return;
			getCustPane().getBodyBillModel().setBodyDataVO(ui.getBuffer().getCurrBodysForCust());
			getCustPane().getBodyBillModel().execLoadFormula();
		}


	}

	/**
	 * 
	 * @author zhf Ʒ��ҳǩ��ͷ�б任�¼�����
	 *
	 */
	class DealPaneHeadListSelListener implements BillEditListener{

		public void afterEdit(BillEditEvent e) {
			// TODO Auto-generated method stub

		}

		public void bodyRowChange(BillEditEvent e) {
			// TODO Auto-generated method stub
			int row = e.getRow();
			ui.getBuffer().setNumSelRow(row);
			getDealPane().getBodyBillModel().clearBodyData();
			if(row<0)
				return;
			getDealPane().getBodyBillModel().setBodyDataVO(ui.getBuffer().getCurrBodysForDeal());
			getDealPane().getBodyBillModel().execLoadFormula();
		}
	}
}
