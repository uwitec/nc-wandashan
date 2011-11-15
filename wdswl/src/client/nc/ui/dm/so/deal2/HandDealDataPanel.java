package nc.ui.dm.so.deal2;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillTabbedPane;
import nc.vo.dm.so.deal.SoDealVO;
import nc.vo.dm.so.deal2.SoDealBillVO;
import nc.vo.dm.so.deal2.SoDealHeaderVo;
import nc.vo.dm.so.deal2.StoreInvNumVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.WdsWlPubTool;

public class HandDealDataPanel extends BillTabbedPane implements BillEditListener,ChangeListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8752884896489833550L;
	
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
		getDealPane().addBodyEditListener(this);
		this.addChangeListener(this);
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
		addTab("�ͻ���������Ϣ", getCustPane());
		addTab("���Ž���", getDealPane());		
	}

	public void setDataToUI(){
		clearPanel();
		//		�ͻ�ҳǩ��������
		setCustDataToUI();
		//		��������ҳǩ��������
		setDealDataToUI();	
		updateUI();
	}
	
	public void setCustDataToUI(){
		if(!ui.getBuffer().isCustEmpty()){
			SoDealBillVO[] bills = ui.getBuffer().getLcust().toArray(new SoDealBillVO[0]);
			ui.getBuffer().setCustSelRow(0);
			getCustPane().getHeadBillModel().setBodyDataVO(WdsWlPubTool.getParentVOFromAggBillVo(bills, SoDealHeaderVo.class));
			getCustPane().getBodyBillModel().setBodyDataVO(bills[0].getChildrenVO());
			getCustPane().getHeadBillModel().execLoadFormula();
			getCustPane().getBodyBillModel().execLoadFormula();
		}
	}
	public void setDealDataToUI(){
		if(!ui.getBuffer().isNumEmpty()){
			StoreInvNumVO[] nums = ui.getBuffer().getLnum().toArray(new StoreInvNumVO[0]);
			ui.getBuffer().setNumSelRow(0);
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
//  -------------�����¼�-------------------------------
	private boolean ischange = false;//�Ƿ�����˰�������
	public void afterEdit(BillEditEvent e) {
		// TODO Auto-generated method stub
		String key = e.getKey();
		if(key.equalsIgnoreCase("nassnum")){//���Ÿ����� ������  ��Ӧ�䶯
			SoDealVO deal = (SoDealVO)getDealPane().getBodyBillModel().getBodyValueRowVO(e.getRow(), SoDealVO.class.getName());
			//У�飬���ܳ��ƻ�����
			UFDouble nnumber = PuPubVO.getUFDouble_NullAsZero(deal.getNnumber());//�ƻ�������
			UFDouble nnum = PuPubVO.getUFDouble_NullAsZero(deal.getNnum());//���ΰ�������
			UFDouble ntaldcnum = PuPubVO.getUFDouble_NullAsZero(deal.getNtaldcnum());//�ۼư�������
			if(nnumber.sub(ntaldcnum).sub(nnum).doubleValue()<0){
				MessageDialog.showWarningDlg(this, "����", "���ܳ��ƻ�δ��������");
				getDealPane().getBodyBillModel().setValueAt(e.getOldValue(), e.getRow(), key);
				return ;
			}
			//У�鲻�ܳ����ܵĿ�����
			SoDealVO [] bodys =(SoDealVO []) getDealPane().getBodyBillModel().getBodyValueVOs(SoDealVO.class.getName());
			UFDouble	curNum =new UFDouble(0);
			for(SoDealVO body:bodys){
				curNum = curNum.add(PuPubVO.getUFDouble_NullAsZero(body.getNassnum()));
			}
			UFDouble curTotalNum = PuPubVO.getUFDouble_NullAsZero(getDealPane().getHeadBillModel().getValueAt(ui.getBuffer().getNumSelRow(), "nassnum"));
			if(curNum.sub(curTotalNum).doubleValue()>0){
				MessageDialog.showWarningDlg(this, "����", "���ܳ�����ǰ������");
				getDealPane().getBodyBillModel().setValueAt(e.getOldValue(), e.getRow(), key);
				return ;
			}
//			ͬ������
			SoDealHealper.synData(ui.getBuffer().getLcust(), deal);
			ischange = true;
		}
	}
	public void bodyRowChange(BillEditEvent e) {
		
	}
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		int index = this.getSelectedIndex();
		if(index ==0 && ischange){
//			ͬ������
			setCustDataToUI();
			getCustPane().updateUI();
			ischange = false;
		}
	}
}
