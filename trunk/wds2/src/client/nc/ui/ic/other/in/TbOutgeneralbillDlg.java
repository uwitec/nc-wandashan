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
import nc.vo.pub.AggregatedValueObject;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.wds.w8004040204.TbOutgeneralHVO;
import nc.vo.wds.w8004040210.TbGeneralBVO;

public class TbOutgeneralbillDlg extends BillSourceDLG {
	private SCMQueryConditionDlg m_dlgQry = null;

	public TbOutgeneralbillDlg(String pkField, String pkCorp, String operator,
			String funNode, String queryWhere, String billType,
			String businessType, String templateId, String currentBillType,
			Container parent) {
		super(pkField, pkCorp, operator, funNode, queryWhere, billType,
				businessType, templateId, currentBillType, parent);
		// TODO Auto-generated constructor stub
	}

	public TbOutgeneralbillDlg(Container parent) {
		super(null, null, null, null, "1=1", "0214", null, null, null, parent);
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

			String pk_stordoc = nc.ui.wds.w8000.CommonUnit
					.getStordocName(ClientEnvironment.getInstance().getUser()
							.getPrimaryKey());
			StringBuffer sWhere = new StringBuffer(
					" vbilltype='8' and vbillstatus=1 and srl_pkr=srl_pk and  ");
			if (null != pk_stordoc && !"".equals(pk_stordoc)) {
				sWhere.append(" srl_pkr='" + pk_stordoc + "' and ");
			}
			if (voCons != null && voCons.length > 0 && voCons[0] != null) {
				sWhere.append(m_dlgQry.getWhereSQL(voCons));
			}

			IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
					IUAPQueryBS.class.getName());
			// 查询调拨出库的集合
			ArrayList os = (ArrayList) query.retrieveByClause(
					TbOutgeneralHVO.class, sWhere.toString());
			// 过滤本地表后的集合
			ArrayList ttcs = new ArrayList();
			for (int i = 0; i < os.size(); i++) {
				// Object[] gvo = (Object[]) ttcs.get(i);
				String pwb_sql = "select count(geh_cgeneralhid) from tb_general_h where "
						+ "dr=0 and geh_cgeneralhid='"
						+ ((TbOutgeneralHVO) os.get(i)).getGeneral_pk()
						+ "' and dr=0";
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
							+ ((TbOutgeneralHVO) os.get(i)).getGeneral_pk()
							+ "' ";

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
									if (null != tbgeneralbvo.getGeb_cinvbasid()
											&& !"".equals(tbgeneralbvo
													.getGeb_cinvbasid())) {
										if (tbgeneralbvo.getGeb_cinvbasid()
												.equals(invLisk.get(k))) {
											if (null != tbgeneralbvo
													.getGeb_isclose()) {
												if (!tbgeneralbvo
														.getGeb_isclose()
														.booleanValue()) {
													isallclose = false;
												}
											}
										}
									}
								}
							}
						}
					}

					if (!isallclose) {
						ttcs.add(os.get(i));
					}

				}
			}
			// 创建表头VO
			TbOutgeneralHVO[] tbOutgeneralHVOs = new TbOutgeneralHVO[ttcs
					.size()];
			tbOutgeneralHVOs = (TbOutgeneralHVO[]) ttcs
					.toArray(tbOutgeneralHVOs);
			// 用来判断查询单据模板是否有数据

			// for (int i = 0; i < os.size(); i++) {
			//
			// Object[] gvo = (Object[]) os.get(i);
			//
			// gbhVO[i] = new GeneralBillHeaderVO();
			// gbhVO[i].setVbillcode((String) gvo[0]);
			// if (null != gvo[1]) {
			// gbhVO[i].setDbilldate(new UFDate(gvo[1].toString()));
			// }
			//
			// gbhVO[i].setCwarehouseid((String) gvo[2]);
			// gbhVO[i].setCwhsmanagerid((String) gvo[3]);
			// gbhVO[i].setPk_corp((String) gvo[4]);
			// gbhVO[i].setCdptid((String) gvo[5]);
			// gbhVO[i].setPk_calbody((String) gvo[6]);
			// gbhVO[i].setAttributeValue("cotherwhid", (String) gvo[7]);
			// gbhVO[i].setAttributeValue("cothercorpid", (String) gvo[8]);
			// gbhVO[i].setAttributeValue("cothercalbodyid", (String) gvo[9]);
			// // 获得收发类型
			// if (gvo[11] != null) {
			// gbhVO[i].setCdispatcherid(gvo[11].toString());
			//
			// }
			//
			// gbhVO[i].setCdilivertypeid((String) gvo[12]);
			// gbhVO[i].setCinventoryid((String) gvo[14]);
			// gbhVO[i].setCbizid((String) gvo[15]);
			// gbhVO[i].setCgeneralhid((String) gvo[17]);
			// if (null != gvo[18]) {
			// gbhVO[i]
			// .setFallocflag(Integer.parseInt(gvo[18].toString()));
			// }
			//
			// }

			// //重新填充VO，去除为空的元素
			// for (int i = 0; i < gbhVO.length; i++) {
			//
			// }
			getbillListPanel().setHeaderValueVO(tbOutgeneralHVOs);

			getbillListPanel().getHeadBillModel().execLoadFormula();
		} catch (Exception e) {
			SCMEnv.error("数据加载失败！");
			e.printStackTrace(System.out);
		}
	}
}
