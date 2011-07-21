package nc.ui.hg.ia.report;

import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIRadioButton;
import nc.ui.pub.query.QueryConditionClient;
import nc.vo.pub.lang.UFBoolean;

// zhf 鹤岗项目   计划处理查询对话框类
public class InOutSumQueryDlg extends QueryConditionClient {

	// 查询模板新增查询条件
	UIRadioButton m_rbyear = null;
	UIRadioButton m_rbtemp = null;
//	UIRadioButton m_rbmny = null;

	public InOutSumQueryDlg(){
		super();
		getConditionDatas();
		init();
	}
	private void init(){
		changeQueryModelLayout();
	}
	public void initData() {
		
//		setDefaultValue("accountdate",null,ClientEnvironment.getInstance().getAccountMonth());
		super.initData();
	}
	private void changeQueryModelLayout() {
		if ( m_rbtemp != null && m_rbyear !=null)
			return;

		UILabel label1 = new UILabel("选择查询类型");
		label1.setBounds(30, 65, 100, 25);

		m_rbyear = new UIRadioButton();
		m_rbyear.setBounds(130, 65, 16, 16);
		m_rbyear.setSelected(true);
		
		UILabel label2 = new UILabel("汇总查询");
		label2.setBounds(146, 65, 100, 25);

		m_rbtemp = new UIRadioButton();
		m_rbtemp.setBounds(130, 95, 16, 16);
		
		UILabel label3 = new UILabel("明细查询");
		label3.setBounds(146, 95, 100, 25);

//		m_rbmny = new UIRadioButton();
//		m_rbmny.setBounds(130, 125, 16, 16);
//		
//		UILabel label4 = new UILabel("专项资金计划");
//		label4.setBounds(146, 125, 100, 25);

		javax.swing.ButtonGroup buttonGroup = new javax.swing.ButtonGroup();
		buttonGroup.add(m_rbyear);
		buttonGroup.add(m_rbtemp);
//		buttonGroup.add(m_rbmny);

		getUIPanelNormal().add(label1);
		getUIPanelNormal().add(label2);
		getUIPanelNormal().add(label3);
//		getUIPanelNormal().add(label4);
		getUIPanelNormal().add(m_rbyear);
		getUIPanelNormal().add(m_rbtemp);
//		getUIPanelNormal().add(m_rbmny);
	}
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）是否大类汇总查询
	 * 2011-6-20下午03:49:36
	 * @return
	 */
	public UFBoolean isinvcl(){
		if(m_rbyear.isSelected())
			return UFBoolean.TRUE;
		else
			return UFBoolean.FALSE;
	}

}
