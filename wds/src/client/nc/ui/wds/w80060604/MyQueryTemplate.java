package nc.ui.wds.w80060604;

import java.awt.Container;
import java.util.ArrayList;

import nc.itf.scm.so.so090.IcoopwithQuery;
import nc.ui.ic.pub.report.IcReportFormula;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.pf.BillSourceDLG;
import nc.ui.scm.pub.query.SCMQueryConditionDlg;
import nc.ui.wds.w8000.CommonUnit;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.query.ConditionVO;

public class MyQueryTemplate  {


	public MyQueryTemplate(Container parent) {
		// TODO Auto-generated constructor stub
		m_parent = parent;
	}
	Container m_parent = null;
	private SCMQueryConditionDlg m_dlgQry = null;
	

	public  SCMQueryConditionDlg getQueryDlg(String pkCorp, String funNode,
			String operator, String qrynodekey) {
		// �õ����ýڵ�Ĳ�ѯ�Ի���
		//m_dlgQry = getQueryDlg(pkCorp, funNode, operator, qrynodekey);

		if (m_dlgQry == null) {
			try {
				m_dlgQry = new SCMQueryConditionDlg(m_parent);
				m_dlgQry.setTempletID(pkCorp, funNode, operator, null,
						qrynodekey);
				nc.vo.pub.query.QueryConditionVO[] voaConData = m_dlgQry
						.getConditionDatas();
				String pk = CommonUnit.getStordocName(ClientEnvironment
						.getInstance().getUser().getPrimaryKey());
				m_dlgQry.setConditionEditable("so_sale.tmpstock", CommonUnit.getSotckIsTotal(pk));
				// ���س�������
//				m_dlgQry.hideNormal();
//				m_dlgQry.setNormalShow(true);
//				m_dlgQry.set
				//��ʾ��������
//				m_dlgQry.setNormalShow(true);
				//���ض൥ѡ��ť
//				m_dlgQry.hideUnitButton();
//				m_dlgQry.setVisible(false); 
				
				//��ʾ��ӡ״̬
				m_dlgQry.setShowPrintStatusPanel(true);
				
				// ���õ�������
				String sDate = ClientEnvironment.getInstance()
						.getBusinessDate().toString();
				m_dlgQry.setInitDate("head.dapplydate", sDate);

				ArrayList alCorpIDs = new ArrayList();
				alCorpIDs.add(pkCorp);
			} catch (Exception e) {
				nc.vo.scm.pub.ctrl.GenMsgCtrl.handleException(e);
			}

		}
		return m_dlgQry;
	}
	

}
