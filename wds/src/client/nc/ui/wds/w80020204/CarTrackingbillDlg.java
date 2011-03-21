package nc.ui.wds.w80020204;

import java.awt.Container;
import java.util.ArrayList;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.ic.service.IICPub_GeneralBillBO;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.ResultSetProcessor;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.pf.BillSourceDLG;
import nc.ui.scm.pub.query.SCMQueryConditionDlg;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.wds.w80060406.TbFydnewVO;


public class CarTrackingbillDlg extends BillSourceDLG{
	
	private SCMQueryConditionDlg m_dlgQry = null;

	public CarTrackingbillDlg(String pkField, String pkCorp, String operator,
			String funNode, String queryWhere, String billType,
			String businessType, String templateId, String currentBillType,
			Container parent) {
		super(pkField, pkCorp, operator, funNode, queryWhere, billType,
				businessType, templateId, currentBillType, parent);
		// TODO Auto-generated constructor stub
	}

	public CarTrackingbillDlg(Container parent) {
		super(null, null, null, null, "1=1", "0202", null, null, null, parent);
	}

	public AggregatedValueObject[] getReturnVOs(String pkCorp, String operator,
			String billType, String currentBillType, String funNode,
			String qrynodekey, java.awt.Container parent) {
		// 得到调用节点的查询对话框
		// funnode 40092010 申请单
		// qrynodekey 40099906 参照单据查询
		m_dlgQry = getQueryDlg(pkCorp, funNode, operator, qrynodekey);

		if (m_dlgQry.showModal() == nc.ui.pub.beans.MessageDialog.ID_OK) {

			initVar(null, pkCorp, operator, null, "null", billType, null, null,
					currentBillType, null, parent);
			loadHeadData();
			addBillUI();
			setQueyDlg(m_dlgQry);
			if (showModal() == nc.ui.pub.beans.MessageDialog.ID_OK) {
				// 获取所选VO
				return getRetVos();
			}
		}
		return null;
	}

	private SCMQueryConditionDlg getQueryDlg(String pkCorp, String funNode,
			String operator, String qrynodekey) {
		if (m_dlgQry == null) {
			try {
				m_dlgQry = new nc.ui.scm.pub.query.SCMQueryConditionDlg(this);
				m_dlgQry.setTempletID(pkCorp, funNode, operator, null,
						qrynodekey);
				nc.vo.pub.query.QueryConditionVO[] voaConData = m_dlgQry
						.getConditionDatas();
				// 隐藏常用条件
				m_dlgQry.hideNormal();
				// 设置单据日期
				String sDate = ClientEnvironment.getInstance()
						.getBusinessDate().toString();
				m_dlgQry.setInitDate("head.dapplydate", sDate);

				ArrayList alCorpIDs = new ArrayList();
				alCorpIDs.add(pkCorp);
				// m_dlgQry.initCorpRef("head.pk_corp", pkCorp, alCorpIDs);
				// m_dlgQry.setCorpRefs("head.pk_corp",GenMethod.getDataPowerFieldFromDlgNotByProp(m_dlgQry));

			} catch (Exception e) {
				nc.vo.scm.pub.ctrl.GenMsgCtrl.handleException(e);
			}

		}

		return m_dlgQry;
	}

	public void loadHeadData() {
		try {
			// 利用产品组传入的条件与当前查询条件获得条件组成主表查询条件
			// 处理dplanbindate的查询条件
			nc.vo.pub.query.ConditionVO[] voCons = m_dlgQry.getConditionVO();
			StringBuffer sWhere = new StringBuffer();
			/*StringBuffer sWhere = new StringBuffer(
					" billtype=0 and fyd_pk in "
							+ " (select csourcebillhid from tb_outgeneral_h where vbillstatus=0 and vbilltype=0 ) and    ");*/
			if (voCons != null && voCons.length > 0 && voCons[0] != null) {
				sWhere.append(m_dlgQry.getWhereSQL(voCons));
				sWhere.append(" and fyd_constatus=1");
			}

			IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
					IUAPQueryBS.class.getName());
			// 查询发运单的集合
			ArrayList os = (ArrayList) query.retrieveByClause(TbFydnewVO.class,
					sWhere.toString());
			// 过滤本地表后的集合
			ArrayList ttcs = new ArrayList();
			for (int i = 0; i < os.size(); i++) {
				// Object[] gvo = (Object[]) ttcs.get(i);
				String pwb_sql = "select count(pk_fydnew) from tb_cartracking where "
						+ "dr=0 and pk_fydnew='"
						+ ((TbFydnewVO) os.get(i)).getFyd_pk()+"'" ;
				ArrayList pwb_count = (ArrayList) query.executeQuery(pwb_sql,
						new ArrayListProcessor());
				int countnum = Integer
						.parseInt((((Object[]) pwb_count.get(0))[0]).toString());
				if (countnum == 0) {
					ttcs.add(os.get(i));
				} 
			}
			// 创建表头VO
			TbFydnewVO[] tbFydnewVOs = new TbFydnewVO[ttcs.size()];
			tbFydnewVOs = (TbFydnewVO[]) ttcs.toArray(tbFydnewVOs);
			
			getbillListPanel().setHeaderValueVO(tbFydnewVOs);

			getbillListPanel().getHeadBillModel().execLoadFormula();
		} catch (Exception e) {
			SCMEnv.error("数据加载失败！");
			e.printStackTrace(System.out);
	
	}
		}
}
