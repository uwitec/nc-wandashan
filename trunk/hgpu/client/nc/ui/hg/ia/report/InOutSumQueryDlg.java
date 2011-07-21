package nc.ui.hg.ia.report;

import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIRadioButton;
import nc.ui.pub.query.QueryConditionClient;
import nc.vo.pub.lang.UFBoolean;

// zhf �׸���Ŀ   �ƻ������ѯ�Ի�����
public class InOutSumQueryDlg extends QueryConditionClient {

	// ��ѯģ��������ѯ����
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

		UILabel label1 = new UILabel("ѡ���ѯ����");
		label1.setBounds(30, 65, 100, 25);

		m_rbyear = new UIRadioButton();
		m_rbyear.setBounds(130, 65, 16, 16);
		m_rbyear.setSelected(true);
		
		UILabel label2 = new UILabel("���ܲ�ѯ");
		label2.setBounds(146, 65, 100, 25);

		m_rbtemp = new UIRadioButton();
		m_rbtemp.setBounds(130, 95, 16, 16);
		
		UILabel label3 = new UILabel("��ϸ��ѯ");
		label3.setBounds(146, 95, 100, 25);

//		m_rbmny = new UIRadioButton();
//		m_rbmny.setBounds(130, 125, 16, 16);
//		
//		UILabel label4 = new UILabel("ר���ʽ�ƻ�");
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
	 * @˵�������׸ڿ�ҵ���Ƿ������ܲ�ѯ
	 * 2011-6-20����03:49:36
	 * @return
	 */
	public UFBoolean isinvcl(){
		if(m_rbyear.isSelected())
			return UFBoolean.TRUE;
		else
			return UFBoolean.FALSE;
	}

}
