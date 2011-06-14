package nc.ui.wds.tranprice.bill;

import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIRadioButton;
import nc.ui.pub.query.QueryConditionClient;
import nc.vo.pub.lang.UFBoolean;

// 装车对话框
public class SendBillRefQryDlg extends QueryConditionClient {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public SendBillRefQryDlg(ToftPanel parent){
		super(parent);
		getConditionDatas();
		init();
	}


	// 查询模板新增查询条件
	UIRadioButton m_rbsale = null;
	UIRadioButton m_rbtrans = null;
//	UIRadioButton m_rball = null;


	public UFBoolean isSale(){
		if(m_rbsale.isSelected())
			return UFBoolean.TRUE;
		else 
			return UFBoolean.FALSE;
	}
	private void init(){
		changeQueryModelLayout();
	}
//	public void initData() {
//		UFDate billdate = ClientEnvironment.getInstance().getDate();
////		setDefaultValue("h.dbilldate",null,PuPubVO.getString_TrimZeroLenAsNull(billdate.toString()));
//		super.initData();
//	}
	private void changeQueryModelLayout() {
		if (m_rbsale != null && m_rbtrans != null && m_rbtrans !=null)
			return;

		UILabel label1 = new UILabel("选择发货类型");
		label1.setBounds(30, 65, 100, 25);

		m_rbsale = new UIRadioButton();
		m_rbsale.setBounds(130, 65, 16, 16);
		m_rbsale.setSelected(true);
		
		UILabel label2 = new UILabel("销售发货");
		label2.setBounds(146, 65, 100, 25);

		m_rbtrans = new UIRadioButton();
		m_rbtrans.setBounds(130, 95, 16, 16);
		
		UILabel label3 = new UILabel("转分仓发货");
		label3.setBounds(146, 95, 100, 25);

//		m_rball = new UIRadioButton();
//		m_rball.setBounds(130, 125, 16, 16);
//		
//		UILabel label4 = new UILabel("全部");
//		label4.setBounds(146, 125, 100, 25);

		javax.swing.ButtonGroup buttonGroup = new javax.swing.ButtonGroup();
		buttonGroup.add(m_rbsale);
		buttonGroup.add(m_rbtrans);
//		buttonGroup.add(m_rball);

		getUIPanelNormal().add(label1);
		getUIPanelNormal().add(label2);
		getUIPanelNormal().add(label3);
//		getUIPanelNormal().add(label4);
		getUIPanelNormal().add(m_rbsale);
		getUIPanelNormal().add(m_rbtrans);
//		getUIPanelNormal().add(m_rball);
	}


}
