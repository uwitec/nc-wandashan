package nc.ui.dm.db.deal;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIRadioButton;
import nc.ui.pub.query.QueryConditionClient;
public class DbDealQryDlg extends QueryConditionClient {
	private static final long serialVersionUID = 1L;
	
	UIRadioButton m_rbclose = null;
	UIRadioButton m_rbopen = null;
	
	public DbDealQryDlg() {
		super();
		getConditionDatas();
		init();
	}
	public void init(){
		//changeQueryModelLayout();
		setDefaultValue("h.dbilldate",null,ClientEnvironment.getInstance().getDate().toString());
	}
	
//	private void changeQueryModelLayout() {
//		if ( m_rbopen != null && m_rbclose !=null)
//			return;
//
//		UILabel label1 = new UILabel("选择订单类型");
//		label1.setBounds(30, 65, 100, 25);
//
//		m_rbopen = new UIRadioButton();
//		m_rbopen.setBounds(130, 65, 16, 16);
//		m_rbopen.setSelected(true);
//		
//		UILabel label2 = new UILabel("未关闭");
//		label2.setBounds(146, 65, 100, 25);
//
//		m_rbclose = new UIRadioButton();
//		m_rbclose.setBounds(130, 95, 16, 16);
//		
//		UILabel label3 = new UILabel("已关闭");
//		label3.setBounds(146, 95, 100, 25);
//
//		javax.swing.ButtonGroup buttonGroup = new javax.swing.ButtonGroup();
//		buttonGroup.add(m_rbopen);
//		buttonGroup.add(m_rbclose);
//		
//
//		getUIPanelNormal().add(label1);
//		getUIPanelNormal().add(label2);
//		getUIPanelNormal().add(label3);
////		getUIPanelNormal().add(label4);
//		getUIPanelNormal().add(m_rbopen);
//		getUIPanelNormal().add(m_rbclose);		
//	}

}
