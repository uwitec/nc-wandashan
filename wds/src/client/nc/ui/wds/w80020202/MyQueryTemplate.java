package nc.ui.wds.w80020202;

import java.awt.Container;
import java.util.ArrayList;

import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.pf.BillSourceDLG;
import nc.ui.scm.pub.query.SCMQueryConditionDlg;
import nc.vo.pub.AggregatedValueObject;

public class MyQueryTemplate  {


	public MyQueryTemplate(Container parent) {
		// TODO Auto-generated constructor stub
		m_parent = parent;
	}
	Container m_parent = null;
	private SCMQueryConditionDlg m_dlgQry = null;
	

	public  SCMQueryConditionDlg getQueryDlg(String pkCorp, String funNode,
			String operator, String qrynodekey) {
		// 得到调用节点的查询对话框
		//m_dlgQry = getQueryDlg(pkCorp, funNode, operator, qrynodekey);

		
		if (m_dlgQry == null) {
			try {
				m_dlgQry = new SCMQueryConditionDlg(m_parent);
				m_dlgQry.setTempletID(pkCorp, funNode, operator, null,
						qrynodekey);
				nc.vo.pub.query.QueryConditionVO[] voaConData = m_dlgQry
						.getConditionDatas();
				// 隐藏常用条件
				m_dlgQry.hideNormal();
				//显示常用条件
//				m_dlgQry.setNormalShow(true);
				//隐藏多单选择按钮
//				m_dlgQry.hideUnitButton();
//				m_dlgQry.setVisible(false); 

				
				//显示打印状态
				m_dlgQry.setShowPrintStatusPanel(true);
				
				// 设置单据日期
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
