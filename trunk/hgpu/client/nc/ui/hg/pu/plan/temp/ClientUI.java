package nc.ui.hg.pu.plan.temp;

import java.awt.Component;
import java.awt.Container;
import java.util.Vector;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import nc.ui.hg.pu.pub.PlanPubClientUI;
import nc.ui.pub.beans.UITabbedPane;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.hg.pu.plan.temp.PlanInventoryVO;
import nc.vo.hg.pu.plan.temp.PlanOtherTempVO;
import nc.vo.hg.pu.pub.HgPubConst;
import nc.vo.pub.CircularlyAccessibleValueObject;
/**
 * ��ʱ�ƻ�(֧���Զ�����)
 * @author zhw
 * 
 */
public class ClientUI extends PlanPubClientUI implements ChangeListener {

	public ClientUI() {
		super();
	}
	

	@Override
	protected ManageEventHandler createEventHandler() {
		return new ClientEventHandler(this, getUIControl());
	}
	
	@Override
	protected void initSelfData() {
		
		// ����ҳǩ�л�����
		UITabbedPane m_CardUITabbedPane = getBillCardPanel().getBodyTabbedPane();
		UITabbedPane m_ListUITabbedPane = getUITabbedPane(getBillListPanel());
		m_CardUITabbedPane.addChangeListener(this);
		m_ListUITabbedPane.addChangeListener(this);
		
		getBillCardPanel().setBodyMenuShow(false);//���ñ���Ĳ˵�������
		super.initSelfData();
	}
	
	protected nc.ui.pub.beans.UITabbedPane getUITabbedPane(Component c) {
		if (c instanceof UITabbedPane)
			return (UITabbedPane) c;
		if (c instanceof Container) {
			Component[] comps = ((Container) c).getComponents();
			for (int i = 0; i < comps.length; i++) {
				Component cc = getUITabbedPane(comps[i]);
				if (cc instanceof UITabbedPane)
					return (UITabbedPane) cc;
			}
		}
		return null;
	}

	@Override
	protected AbstractManageController createController() {
		return new ClientController();
	}

	@Override
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {

	}

	@Override
	protected String getBillNo() throws Exception {
		return HYPubBO_Client.getBillNo(HgPubConst.PLAN_TEMP_BILLTYPE,
				_getCorp().getPrimaryKey(), null, null);
	}

	@Override
	protected BusinessDelegator createBusinessDelegator() {
		return new ClientBusinessDelegator(this);
	}
	@Override
	public void setDefaultData() throws Exception {		
		 setHeadItemValue("pk_billtype", HgPubConst.PLAN_TEMP_BILLTYPE);
		
		super.setDefaultData();
	}


	//ҳǩ�л��¼�
	public void stateChanged(ChangeEvent e) {
		
		if(getBufferData().getCurrentVO()==null)
			return;
		
		if (isListPanelSelected()) {// //��Ƭҳǩ�б�ҳǩ
			if ("inv".equals(getBillListPanel().getBodyTabbedPane().getSelectedTableCode())) {
				PlanInventoryVO[] invos =((PlanOtherTempVO)(getBufferData().getCurrentVO().getChildrenVO()[0])).getInvvos();
				getBillListPanel().getBodyBillModel().setBodyDataVO(invos);//����������	
				Vector v = getBillListPanel().getBodyBillModel().getBillModelData();
				getBillListPanel().getBodyBillModel().setBillModelData(v);
				getBillListPanel().getBodyBillModel().execLoadFormula();
			}
		} else {// ��Ƭҳǩ
			if ("inv".equals(getBillCardPanel().getBodyTabbedPane().getSelectedTableCode())) {
				PlanInventoryVO[] invos =((PlanOtherTempVO)(getBufferData().getCurrentVO().getChildrenVO()[0])).getInvvos();
				getBillCardPanel().getBillModel().setBodyDataVO(invos);//����������	
				Vector v = getBillCardPanel().getBillModel().getBillModelData();
				getBillCardPanel().getBillModel().setBillModelData(v);
				getBillCardPanel().getBillModel().execLoadFormula();
			}
		}
	}
	
	@Override
	public void bodyRowChange(BillEditEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == getBillListPanel().getParentListPanel().getTable()) {
			getBillListPanel().getBodyTabbedPane().setSelectedIndex(0);
		}
		super.bodyRowChange(e);
		
	}
}
