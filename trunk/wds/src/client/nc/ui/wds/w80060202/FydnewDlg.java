package nc.ui.wds.w80060202;

import java.awt.Container;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.ListSelectionEvent;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.pf.BillSourceDLG;
import nc.ui.scm.pub.query.SCMQueryConditionDlg;
import nc.ui.wds.w80060202.MyClientUI;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.wds.w80060401.TbShipentryVO;
import nc.vo.wds.w80060406.TbFydmxnewVO;
import nc.vo.wds.w80060406.TbFydnewVO;

public class FydnewDlg extends BillSourceDLG {
	Container m_parent = null;

	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		if (getbillListPanel().getHeadTable().getSelectedRowCount() > 0) {
			List<TbFydmxnewVO> fydmxList = new ArrayList<TbFydmxnewVO>();
			TbFydmxnewVO[] fydmx = null;
			// 获取所选择行的Id
			Object o = getbillListPanel().getHeadBillModel().getValueAt(
					getbillListPanel().getHeadTable().getSelectedRow(),
					"fyd_pk");
			// 判断是否为空
			if (o != null && o != "" && null != this.fydmxVO
					&& this.fydmxVO.length > 0) {
				String csaleid = o.toString();
				for (int i = 0; i < this.fydmxVO.length; i++) {
					String saleid = this.fydmxVO[i].getFyd_pk();
					if (csaleid.equals(saleid)) {
						// 如果所选择的行id不为空 根据它去缓存里查询出子表Vo 进行显示
						fydmxList.add(this.fydmxVO[i]);
					}
				}
				fydmx = new TbFydmxnewVO[fydmxList.size()];
				fydmx = fydmxList.toArray(fydmx);
				getbillListPanel().setBodyValueVO(fydmx);
			}
		}

		// getbillListPanel().getHeadBillModel().execLoadFormula();
		getbillListPanel().getBodyBillModel().execLoadFormula();
	}

	public void onOk() {
		if (getbillListPanel().getHeadBillModel().getRowCount() > 0) {
			AggregatedValueObject[] selectedBillVOs = getbillListPanel()
					.getMultiSelectedVOs(
							nc.vo.trade.pub.HYBillVO.class.getName(),
							TbFydnewVO.class.getName(),
							TbFydmxnewVO.class.getName());
			retBillVo = selectedBillVOs.length > 0 ? selectedBillVOs[0] : null;
			retBillVos = selectedBillVOs;
			if (retBillVos != null && retBillVos.length == 1) {
				selectedBillVOs[0].setChildrenVO(null);
				this.closeOK();
			}else{
				((MyClientUI) m_parent).showWarningMessage("请选择一条记录进行制单!");
				return;
			}

		}else{
			((MyClientUI) m_parent).showWarningMessage("请选择一条记录进行制单!");
			return;
		}
		
	}

	private SCMQueryConditionDlg m_dlgQry = null;

	public TbFydnewVO[] fydVO;
	public TbFydmxnewVO[] fydmxVO;

	public TbFydnewVO[] getFydVO() {
		return fydVO;
	}

	public void setFydVO(TbFydnewVO[] fydVO) {
		this.fydVO = fydVO;
	}

	public TbFydmxnewVO[] getFydmxVO() {
		return fydmxVO;
	}

	public void setFydmxVO(TbFydmxnewVO[] fydmxVO) {
		this.fydmxVO = fydmxVO;
	}

	public FydnewDlg(String pkField, String pkCorp, String operator,
			String funNode, String queryWhere, String billType,
			String businessType, String templateId, String currentBillType,
			Container parent) {
		super(pkField, pkCorp, operator, funNode, queryWhere, billType,
				businessType, templateId, currentBillType, parent);
		// TODO Auto-generated constructor stub
	}

	public FydnewDlg(Container parent) {
		super(null, null, null, null, "1=1", "0202", null, null, null, parent);
		m_parent = parent;
		init();
	}

	private void init() {
		getbillListPanel().getHeadTable().getSelectionModel()
				.addListSelectionListener(this);
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
			// 获取查询条件
			nc.vo.pub.query.ConditionVO[] voCons = m_dlgQry.getConditionVO();

			StringBuffer sWhere = new StringBuffer(
					" dr = 0 and billtype=0 and vbillstatus = -1 and fyd_approstate = 1");
			if (voCons != null && voCons.length > 0 && voCons[0] != null) {
				sWhere.append(" and " + m_dlgQry.getWhereSQL(voCons));
			}
			int num = 0;
			// 表体VO
			List<TbFydmxnewVO> fydmxList = new ArrayList<TbFydmxnewVO>();
			TbFydmxnewVO[] fydmx = null;
			// 获取访问数据库对象
			IUAPQueryBS IUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance()
					.lookup(IUAPQueryBS.class.getName());

			ArrayList list = (ArrayList) IUAPQueryBS.retrieveByClause(
					TbFydnewVO.class, sWhere.toString());
			// 表头VO
			TbFydnewVO[] fyd = new TbFydnewVO[list.size()];
			fyd = (TbFydnewVO[]) list.toArray(fyd);

			if (null != fyd && fyd.length > 0) {
				for (int i = 0; i < fyd.length; i++) {
					TbFydnewVO fydvo = fyd[i];
					String mxWhere = " dr = 0 and fyd_pk= '"
							+ fydvo.getFyd_pk() + "'";
					ArrayList mxlist = (ArrayList) IUAPQueryBS
							.retrieveByClause(TbFydmxnewVO.class, mxWhere);
					fydmx = new TbFydmxnewVO[mxlist.size()];
					fydmx = (TbFydmxnewVO[]) mxlist.toArray(fydmx);
					if (null != fydmx && fydmx.length > 0) {
						for (int j = 0; j < fydmx.length; j++) {
							fydmxList.add(fydmx[j]);
						}
					}

				}
			}
			//			
			this.setFydVO(fyd);
			if (null != fydmxList && fydmxList.size() > 0) {
				TbFydmxnewVO[] fydmxvo = new TbFydmxnewVO[fydmxList.size()];
				fydmxvo = fydmxList.toArray(fydmxvo);
				this.setFydmxVO(fydmxvo);
			}
			getbillListPanel().getBillListData().setHeaderValueVO(fyd);

			getbillListPanel().getHeadBillModel().execLoadFormula();
		} catch (Exception e) {
			SCMEnv.error("数据加载失败！");
			e.printStackTrace(System.out);
		}
	}

}
