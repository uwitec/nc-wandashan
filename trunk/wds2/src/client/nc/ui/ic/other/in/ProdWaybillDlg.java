package nc.ui.ic.other.in;

import java.awt.Container;
import java.util.ArrayList;
import java.util.List;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.pf.BillSourceDLG;
import nc.ui.scm.pub.query.SCMQueryConditionDlg;
import nc.vo.ic.pub.TbGeneralBVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.wds.w80060406.TbFydnewVO;

public class ProdWaybillDlg extends BillSourceDLG {
	private SCMQueryConditionDlg m_dlgQry = null;

	public ProdWaybillDlg(String pkField, String pkCorp, String operator,
			String funNode, String queryWhere, String billType,
			String businessType, String templateId, String currentBillType,
			Container parent) {
		super(pkField, pkCorp, operator, funNode, queryWhere, billType,
				businessType, templateId, currentBillType, parent);
		// TODO Auto-generated constructor stub
	}

	public ProdWaybillDlg(Container parent) {
		super(null, null, null, null, "1=1", "0202", null, null, null, parent);
	}

	public AggregatedValueObject[] getReturnVOs(String pkCorp, String operator,
			String billType, String currentBillType, String funNode,
			String qrynodekey, java.awt.Container parent) {
		// 得到调用节点的查询对话框
		// funnode 40092010 申请单
		// qrynodekey 40099906 参照单据查询
		// 登录人仓库主键
		String pk_stordoc = "";
		try {
			pk_stordoc = nc.ui.wds.w8000.CommonUnit
					.getStordocName(ClientEnvironment.getInstance().getUser()
							.getPrimaryKey());
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		boolean isTotal = true;
		if (null != pk_stordoc && !"".equals(pk_stordoc)) {
			// 判断是总仓还是分仓
			try {
				isTotal = nc.ui.wds.w8000.CommonUnit
						.getSotckIsTotal(pk_stordoc);
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		m_dlgQry = getQueryDlg(pkCorp, funNode, operator, qrynodekey);

		if (m_dlgQry.showModal() == nc.ui.pub.beans.MessageDialog.ID_OK) {

			initVar(null, pkCorp, operator, null, "null", billType, null, null,
					currentBillType, null, parent);
			if (isTotal) {
				loadHeadData();
			} else {
				loadHeadDataPoints();
			}

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

	/**
	 * 总仓
	 */
	public void loadHeadData() {
		try {
			// 利用产品组传入的条件与当前查询条件获得条件组成主表查询条件
			// 处理dplanbindate的查询条件
			nc.vo.pub.query.ConditionVO[] voCons = m_dlgQry.getConditionVO();

			// 判断是总仓还是分仓
			// boolean isfirst = nc.ui.wds.w8000.CommonUnit
			// .getSotckIsTotal(ClientEnvironment.getInstance().getUser()
			// .getPrimaryKey());
			// 登录人仓库主键
			String pk_stordoc = nc.ui.wds.w8000.CommonUnit
					.getStordocName(ClientEnvironment.getInstance().getUser()
							.getPrimaryKey());
			// 分仓
			// 总仓
			StringBuffer sWhere = new StringBuffer(
					" billtype=0 and fyd_constatus=1 and dr=0 and  ");
			// fyd_pk in "
			// + " (select csourcebillhid from tb_outgeneral_h where
			// vbillstatus=0 and vbilltype=0 )
			if (null != pk_stordoc && !"".equals(pk_stordoc)) {
				sWhere.append(" srl_pkr='" + pk_stordoc + "' and ");
			}
			if (voCons != null && voCons.length > 0 && voCons[0] != null) {
				sWhere.append(m_dlgQry.getWhereSQL(voCons));
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
				String pwb_sql = "select count(geh_cgeneralhid) from tb_general_h where "
						+ "dr=0 and geh_cgeneralhid='"
						+ ((TbFydnewVO) os.get(i)).getFyd_pk() + "' and dr=0";
				ArrayList pwb_count = (ArrayList) query.executeQuery(pwb_sql,
						new ArrayListProcessor());
				int countnum = Integer
						.parseInt((((Object[]) pwb_count.get(0))[0]).toString());
				if (countnum == 0) {
					List invLisk = nc.ui.wds.w8000.CommonUnit
							.getInvbasdoc_Pk(ClientEnvironment.getInstance()
									.getUser().getPrimaryKey());
					String geb_sql = "select count(*) from tb_fydmxnew where fyd_pk ='"
							+ ((TbFydnewVO) os.get(i)).getFyd_pk()
							+ "' and pk_invbasdoc in('";
					for (int k = 0; k < invLisk.size(); k++) {
						if (null != invLisk && invLisk.size() > 0
								&& null != invLisk.get(k)
								&& !"".equals(invLisk.get(k))) {
							geb_sql += invLisk.get(k) + "','";
						}
					}
					geb_sql += "') and dr=0 ";
					ArrayList icgebs = (ArrayList) query.executeQuery(geb_sql,
							new ArrayListProcessor());
					int countnum1 = Integer
							.parseInt((((Object[]) icgebs.get(0))[0])
									.toString());
					if (0 != countnum1) {
						ttcs.add(os.get(i));
					}
				} else {
					// 当前登录的保管员能管理的货品
					List invLisk = nc.ui.wds.w8000.CommonUnit
							.getInvbasdoc_Pk(ClientEnvironment.getInstance()
									.getUser().getPrimaryKey());
					String geb_sql = "select count(*) from tb_fydmxnew where fyd_pk ='"
							+ ((TbFydnewVO) os.get(i)).getFyd_pk()
							+ "' and pk_invbasdoc in('";
					for (int k = 0; k < invLisk.size(); k++) {
						if (null != invLisk && invLisk.size() > 0
								&& null != invLisk.get(k)
								&& !"".equals(invLisk.get(k))) {
							geb_sql += invLisk.get(k) + "','";
						}
					}
					geb_sql += "') and dr=0 ";

					ArrayList icgebs = (ArrayList) query.executeQuery(geb_sql,
							new ArrayListProcessor());
					int countnum1 = Integer
							.parseInt((((Object[]) icgebs.get(0))[0])
									.toString());
					if (0 != countnum1) {
						// 查看所有明细是否全部关闭
						String gebbvosql = " dr=0 and geb_cgeneralhid ='"
								+ ((TbFydnewVO) os.get(i)).getFyd_pk()
								+ "' and geb_cinvbasid in ('";
						for (int k = 0; k < invLisk.size(); k++) {
							if (null != invLisk && invLisk.size() > 0
									&& null != invLisk.get(k)
									&& !"".equals(invLisk.get(k))) {
								gebbvosql += invLisk.get(k) + "','";
							}
						}
						gebbvosql += "') ";
						ArrayList gebbvos = (ArrayList) query.retrieveByClause(
								TbGeneralBVO.class, gebbvosql);
						boolean isallclose = true;
						if (null != gebbvos && gebbvos.size() > 0) {
							for (int j = 0; j < gebbvos.size(); j++) {
								TbGeneralBVO tbgeneralbvo = (TbGeneralBVO) gebbvos
										.get(j);
								for (int k = 0; k < invLisk.size(); k++) {
									if (null != tbgeneralbvo && null != invLisk
											&& invLisk.size() > 0
											&& null != invLisk.get(k)
											&& !"".equals(invLisk.get(k))) {
										if (null != tbgeneralbvo
												.getGeb_cinvbasid()
												&& !"".equals(tbgeneralbvo
														.getGeb_cinvbasid())) {
											if (tbgeneralbvo.getGeb_cinvbasid()
													.equals(invLisk.get(k))) {
												if (null != tbgeneralbvo
														.getGeb_isclose()) {
													if (!tbgeneralbvo
															.getGeb_isclose()
															.booleanValue()) {
														// if (null !=
														// tbgeneralbvo
														// .getGeb_snum()
														// && 0 != tbgeneralbvo
														// .getGeb_snum()
														// .doubleValue()) {
														isallclose = false;
														// }
													}
												}
											}
										}
									}
								}
							}
						} else {
							isallclose = false;
						}

						if (!isallclose) {
							ttcs.add(os.get(i));
						}
					}
				}
			}
			// 创建表头VO
			TbFydnewVO[] tbFydnewVOs = new TbFydnewVO[ttcs.size()];
			tbFydnewVOs = (TbFydnewVO[]) ttcs.toArray(tbFydnewVOs);
			// 用来判断查询单据模板是否有数据

			getbillListPanel().setHeaderValueVO(tbFydnewVOs);

			getbillListPanel().getHeadBillModel().execLoadFormula();
		} catch (Exception e) {
			SCMEnv.error("数据加载失败！");
			e.printStackTrace(System.out);
		}
	}

	/**
	 * 分仓
	 */
	public void loadHeadDataPoints() {
		try {
			// 利用产品组传入的条件与当前查询条件获得条件组成主表查询条件
			// 处理dplanbindate的查询条件
			nc.vo.pub.query.ConditionVO[] voCons = m_dlgQry.getConditionVO();

			// 判断是总仓还是分仓
			// boolean isfirst = nc.ui.wds.w8000.CommonUnit
			// .getSotckIsTotal(ClientEnvironment.getInstance().getUser()
			// .getPrimaryKey());
			// 登录人仓库主键
			String pk_stordoc = nc.ui.wds.w8000.CommonUnit
					.getStordocName(ClientEnvironment.getInstance().getUser()
							.getPrimaryKey());
			// 分仓
			// 总仓
			StringBuffer sWhere = new StringBuffer(
					" billtype=0  and dr=0 and fyd_constatus=1 and ");
			// and fyd_pk in "
			// + " (select csourcebillhid from tb_outgeneral_h where
			// vbillstatus=1 and vbilltype=0 )
			if (null != pk_stordoc && !"".equals(pk_stordoc)) {
				sWhere.append(" srl_pkr='" + pk_stordoc + "' and ");
			}
			if (voCons != null && voCons.length > 0 && voCons[0] != null) {
				sWhere.append(m_dlgQry.getWhereSQL(voCons));
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
				String pwb_sql = "select count(geh_cgeneralhid) from tb_general_h where "
						+ "dr=0 and geh_cgeneralhid='"
						+ ((TbFydnewVO) os.get(i)).getFyd_pk() + "' and dr=0";
				ArrayList pwb_count = (ArrayList) query.executeQuery(pwb_sql,
						new ArrayListProcessor());
				int countnum = Integer
						.parseInt((((Object[]) pwb_count.get(0))[0]).toString());
				if (countnum == 0) {
					ttcs.add(os.get(i));
				} else {
					// 当前登录的保管员能管理的货品
					List invLisk = nc.ui.wds.w8000.CommonUnit
							.getInvbasdoc_Pk(ClientEnvironment.getInstance()
									.getUser().getPrimaryKey());
					// 查看所有明细是否全部关闭
					String gebbvosql = " dr=0 and geb_cgeneralhid ='"
							+ ((TbFydnewVO) os.get(i)).getFyd_pk() + "' ";

					ArrayList gebbvos = (ArrayList) query.retrieveByClause(
							TbGeneralBVO.class, gebbvosql);
					boolean isallclose = true;
					if (null != gebbvos && gebbvos.size() > 0) {
						for (int j = 0; j < gebbvos.size(); j++) {
							TbGeneralBVO tbgeneralbvo = (TbGeneralBVO) gebbvos
									.get(j);
							// for (int k = 0; k < invLisk.size(); k++) {
							if (null != tbgeneralbvo && null != invLisk) {
								if (null != tbgeneralbvo.getGeb_cinvbasid()
										&& !"".equals(tbgeneralbvo
												.getGeb_cinvbasid())) {

									if (null != tbgeneralbvo.getGeb_isclose()) {
										if (!tbgeneralbvo.getGeb_isclose()
												.booleanValue()) {
											isallclose = false;
										}
									}

								}
							}
							// }
						}
					}

					if (!isallclose) {
						ttcs.add(os.get(i));
					}

				}
			}
			// 创建表头VO
			TbFydnewVO[] tbFydnewVOs = new TbFydnewVO[ttcs.size()];
			tbFydnewVOs = (TbFydnewVO[]) ttcs.toArray(tbFydnewVOs);
			// 用来判断查询单据模板是否有数据

			getbillListPanel().setHeaderValueVO(tbFydnewVOs);

			getbillListPanel().getHeadBillModel().execLoadFormula();
		} catch (Exception e) {
			SCMEnv.error("数据加载失败！");
			e.printStackTrace(System.out);
		}
	}
}
