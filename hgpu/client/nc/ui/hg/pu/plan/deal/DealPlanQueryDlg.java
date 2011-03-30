package nc.ui.hg.pu.plan.deal;

import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIRadioButton;
import nc.ui.pub.query.QueryConditionClient;
import nc.vo.pub.lang.UFDate;
import nc.vo.scm.pu.PuPubVO;

// zhf �׸���Ŀ   �ƻ������ѯ�Ի�����
public class DealPlanQueryDlg extends QueryConditionClient {

	// ��ѯģ��������ѯ����
	UIRadioButton m_rbyear = null;
	UIRadioButton m_rbtemp = null;
	UIRadioButton m_rbmny = null;

	public DealPlanQueryDlg(){
		super();
		getConditionDatas();
		init();
	}
	private void init(){
		changeQueryModelLayout();
	}
	public void initData() {
		UFDate billdate = ClientEnvironment.getInstance().getDate();
		setDefaultValue("h.dbilldate",null,PuPubVO.getString_TrimZeroLenAsNull(billdate.toString()));
		super.initData();
	}
	private void changeQueryModelLayout() {
		if (m_rbmny != null && m_rbtemp != null && m_rbyear !=null)
			return;

		UILabel label1 = new UILabel("ѡ��ƻ�����");
		label1.setBounds(30, 65, 100, 25);

		m_rbyear = new UIRadioButton();
		m_rbyear.setBounds(130, 65, 16, 16);
		m_rbyear.setSelected(true);
		
		UILabel label2 = new UILabel("��ȼƻ�");
		label2.setBounds(146, 65, 100, 25);

		m_rbtemp = new UIRadioButton();
		m_rbtemp.setBounds(130, 95, 16, 16);
		
		UILabel label3 = new UILabel("��ʱ�ƻ�");
		label3.setBounds(146, 95, 100, 25);

		m_rbmny = new UIRadioButton();
		m_rbmny.setBounds(130, 125, 16, 16);
		
		UILabel label4 = new UILabel("ר���ʽ�ƻ�");
		label4.setBounds(146, 125, 100, 25);

		javax.swing.ButtonGroup buttonGroup = new javax.swing.ButtonGroup();
		buttonGroup.add(m_rbyear);
		buttonGroup.add(m_rbtemp);
		buttonGroup.add(m_rbmny);

		getUIPanelNormal().add(label1);
		getUIPanelNormal().add(label2);
		getUIPanelNormal().add(label3);
		getUIPanelNormal().add(label4);
		getUIPanelNormal().add(m_rbyear);
		getUIPanelNormal().add(m_rbtemp);
		getUIPanelNormal().add(m_rbmny);
	}

}
