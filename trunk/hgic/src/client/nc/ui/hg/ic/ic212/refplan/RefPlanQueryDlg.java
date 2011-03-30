package nc.ui.hg.ic.ic212.refplan;

import java.awt.Container;
import nc.ui.hg.pu.pub.LongTimeTask;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIRadioButton;
import nc.vo.hg.pu.pub.HgPubConst;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.to.pub.Log;

/**
 * ��������:��������ⵥ����������
 * 
 * ����:���� 
 * 
 *  ��������:(2004-5-15 10:48:03)
 * 
 *  �޸ļ�¼������:
 * 
 *  �޸���:
 */
public class RefPlanQueryDlg extends nc.ui.scm.pub.query.SCMQueryConditionDlg implements
nc.ui.pub.pf.IinitQueryData{
	
//	// ��ѯģ��������ѯ����
	private UIRadioButton m_rbyear = null;
	private UIRadioButton m_rbtemp = null;
	private UIRadioButton m_rbmny = null;
	
	public  String getPlanType(){
		if(m_rbyear.isSelected()){
			return HgPubConst.PLAN_MONTH_BILLTYPE;
		}else if(m_rbtemp.isSelected()){
			return HgPubConst.PLAN_TEMP_BILLTYPE;
		}else if(m_rbmny.isSelected())
			return HgPubConst.PLAN_MNY_BILLTYPE;
		else
			return null;
	}

  public RefPlanQueryDlg(
      Container container, String pkCorp, String operator, String funNode, String businessType, String currentBillType, String sourceBilltype, Object userObj) {
    super(container);
    try {
      initData(pkCorp, operator,funNode, businessType,currentBillType, sourceBilltype,userObj);
    }
  
    catch (Exception e) {
      nc.vo.scm.pub.SCMEnv.out(e);
    }
    this.setNormalShow(true);
    changeQueryModelLayout();
     this.hideUnitButton();
  }
  /**
   * �˴����뷽��˵����
   * �������ڣ�(2001-12-1 12:19:04)
   */
  public void setTempletID(String pkCorp, String funNode,String operator, java.lang.String businessType, java.lang.String qrynodekey) 
  {
    super.setTempletID(pkCorp, funNode, operator, businessType,qrynodekey);

    nc.vo.pub.query.QueryConditionVO[] voaConData = getConditionDatas();
//  ���س�������
//    hideNormal();
    setInitDate("h.dbilldate", ClientEnvironment.getInstance().getDate().toString());
  }

  /**
   * �˴����뷽��˵���� �������ڣ�(2001-12-1 12:19:04)
   */
  public void initData(java.lang.String pkCorp, java.lang.String operator,
      java.lang.String funNode, java.lang.String businessType,
      java.lang.String currentBillType, java.lang.String sourceBilltype,
      java.lang.Object userObj) throws java.lang.Exception {

    String querynodekey = "5Xref5A";

    setTempletID(pkCorp, funNode, operator, businessType,querynodekey);
  }

  /**
   * ���"ȷ��"��ť���� �������ڣ�(2004-2-20 10:02:48)
   * @param where 
   * @return 
   */
  public Object onQuery(String where) {
	  String con = getWhereSQL();
	  if(PuPubVO.getString_TrimZeroLenAsNull(con)!=null)
		  where = where + " and "+con;
	  Class[] Types = new Class[]{String.class};
	  Object[] Values = new Object[]{where};
	  try {
		  Object o = LongTimeTask.callRemoteService("scmpub", "nc.bs.hg.scm.pub.HgScmPubBO", "queryPlanBillForTO5X", Types, Values, 2);

		  return o;
	  } catch (Exception e) {
		  Log.error(e);
		  MessageDialog.showWarningDlg(this, null, e.getMessage());
		  return null;
	  }
  }
  
//  
//  
  private void changeQueryModelLayout() {
		if (m_rbmny != null && m_rbtemp != null && m_rbyear !=null)
			return;

		UILabel label1 = new UILabel("ѡ��ƻ�����");
		label1.setBounds(30, 65, 100, 25);

		m_rbyear = new UIRadioButton();
		m_rbyear.setBounds(130, 65, 16, 16);
		m_rbyear.setSelected(true);
		
		UILabel label2 = new UILabel("�·����üƻ�");
		label2.setBounds(146, 65, 100, 25);

		m_rbtemp = new UIRadioButton();
		m_rbtemp.setBounds(130, 95, 16, 16);
		
		UILabel label3 = new UILabel("��ʱ����ƻ�");
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
