package nc.ui.zb.query.ZbNmny;

import java.util.ArrayList;

import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIRadioButton;
import nc.ui.pub.query.QueryConditionClient;
import nc.vo.pub.query.ConditionVO;

// zhw鹤岗项目 
public class ZbNmnyQueryDlg extends QueryConditionClient {

	// 查询模板新增查询条件
	UIRadioButton m_one = null;
	UIRadioButton m_two = null;
	UIRadioButton m_three = null;

	public ZbNmnyQueryDlg(){
		super();
		getConditionDatas();
		init();
	}
	private void init(){
		changeQueryModelLayout();
	}

	private void changeQueryModelLayout() {
		if (m_one != null && m_two != null && m_three !=null)
			return;

		UILabel label1 = new UILabel("选择分类方式");
		label1.setBounds(30, 65, 100, 25);

		m_one = new UIRadioButton();
		m_one.setBounds(130, 65, 16, 16);
		
		
		UILabel label2 = new UILabel("供应商汇总");
		label2.setBounds(146, 65, 100, 25);

		m_two = new UIRadioButton();
		m_two.setBounds(130, 95, 16, 16);
		
		UILabel label3 = new UILabel("公司汇总");
		label3.setBounds(146, 95, 100, 25);

		m_three = new UIRadioButton();
		m_three.setBounds(130, 125, 16, 16);
		m_three.setSelected(true);
		
		UILabel label4 = new UILabel("品种明细");
		label4.setBounds(146, 125, 100, 25);

		javax.swing.ButtonGroup buttonGroup = new javax.swing.ButtonGroup();
		buttonGroup.add(m_one);
		buttonGroup.add(m_two);
		buttonGroup.add(m_three);

		getUIPanelNormal().add(label1);
		getUIPanelNormal().add(label2);
		getUIPanelNormal().add(label3);
		getUIPanelNormal().add(label4);
		getUIPanelNormal().add(m_one);
		getUIPanelNormal().add(m_two);
		getUIPanelNormal().add(m_three);
	}

	public String checkCondition() {
		String strRet =null;
		ConditionVO[] cons = getConditionVO();
		 ArrayList<String> al = new ArrayList<String>();
			for(ConditionVO con:cons){
				if(con.getFieldCode().equalsIgnoreCase("r.ccustmanid")){
					al.add(con.getFieldCode());
				}
			}
			if(al.size()>1)
				strRet="只能选择一个供应商";
		String sResult = super.checkCondition();
		if (strRet == null || strRet.length() == 0)
			return sResult;
		else
			return strRet + (sResult == null ? "" : sResult);
	}
}
