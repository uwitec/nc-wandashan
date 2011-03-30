package nc.ui.wds.w8004040208;

import java.awt.Container;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ListSelectionEvent;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.pf.BillSourceDLG;
import nc.ui.scm.pub.query.SCMQueryConditionDlg;
import nc.ui.trade.button.IBillButton;
import nc.ui.wds.w8000.CommonUnit;
import nc.ui.wds.w8004040204.ssButtun.ISsButtun;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.wds.w80021030.TbQycgjh2VO;
import nc.vo.wds.w80021030.TbQycgjhVO;

public class CgqyDlg extends BillSourceDLG {
	Container m_parent = null;
	private List pkList = null;
	private boolean isStock = false;

	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		if (getbillListPanel().getHeadTable().getSelectedRowCount() > 0) {
			List<TbQycgjh2VO> qmxList = new ArrayList<TbQycgjh2VO>();
			TbQycgjh2VO[] qmx = null;
			// 获取所选择行的Id
			Object o = getbillListPanel().getHeadBillModel().getValueAt(
					getbillListPanel().getHeadTable().getSelectedRow(),
					"pk_qycgjh");
			// 判断是否为空
			if (o != null && o != "" && null != this.qmxVO
					&& this.qmxVO.length > 0) {
				String pk = o.toString();
				for (int i = 0; i < this.qmxVO.length; i++) {
					String qpk = this.qmxVO[i].getPk_qycgjh();
					if (pk.equals(qpk)) {
						// 如果所选择的行id不为空 根据它去缓存里查询出子表Vo 进行显示
						qmxList.add(this.qmxVO[i]);
					}
				}
				qmx = new TbQycgjh2VO[qmxList.size()];
				qmx = qmxList.toArray(qmx);
				getbillListPanel().setBodyValueVO(qmx);
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
							TbQycgjhVO.class.getName(),
							TbQycgjh2VO.class.getName());
			retBillVo = selectedBillVOs.length > 0 ? selectedBillVOs[0] : null;
			retBillVos = selectedBillVOs;
			if (null == retBillVos || retBillVos.length == 1) {
				this.closeOK();
			} else {
				((MyClientUI) m_parent).showWarningMessage("请选择一条记录进行制单!");
				return;
			}
		} else {
			((MyClientUI) m_parent).showWarningMessage("请选择一条记录进行制单!");
			return;
		}

	}

	private SCMQueryConditionDlg m_dlgQry = null;

	public TbQycgjhVO[] qVO;
	public TbQycgjh2VO[] qmxVO;

	public TbQycgjhVO[] getQVO() {
		return qVO;
	}

	public void setQVO(TbQycgjhVO[] qvo) {
		qVO = qvo;
	}

	public TbQycgjh2VO[] getQmxVO() {
		return qmxVO;
	}

	public void setQmxVO(TbQycgjh2VO[] qmxVO) {
		this.qmxVO = qmxVO;
	}

	public CgqyDlg(String pkField, String pkCorp, String operator,
			String funNode, String queryWhere, String billType,
			String businessType, String templateId, String currentBillType,
			Container parent) {
		super(pkField, pkCorp, operator, funNode, queryWhere, billType,
				businessType, templateId, currentBillType, parent);
		// TODO Auto-generated constructor stub
	}

	public CgqyDlg(Container parent, List pkList, boolean isStock) {
		super(null, null, null, null, "1=1", "4278", null, null, null, parent);
		m_parent = parent;
		this.pkList = pkList;
		this.isStock = isStock;
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
				((MyClientUI) m_parent).getButtonManager().getButton(
						ISsButtun.fzgn).setEnabled(true);
				((MyClientUI) m_parent).getButtonManager().getButton(
						IBillButton.Line).setVisible(false);
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
				// m_dlgQry.setNormalShow(true);
				// m_dlgQry.hideUnitButton();
				// m_dlgQry.setVisible(false);
				// m_dlgQry.getUIPanelNormal().
				// 显示打印状态
				// m_dlgQry.setShowPrintStatusPanel(true);
				// PanelUI panelUI = new PanelUI();
				// Component com = new Component();
				// m_dlgQry.setFieldRef("打印", new JComboBox());
				// m_dlgQry.setValueRef("打印", new JComboBox());

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

			StringBuffer sWhere = new StringBuffer(" dr = 0 ");
			// 获取当前登录者查询人员绑定表 是属于哪个仓库，
			String pk_stock = CommonUnit.getStordocName(ClientEnvironment
					.getInstance().getUser().getPrimaryKey());
			if (null != pk_stock && !"".equals(pk_stock)) {
				sWhere.append(" and qycgjh_ck = '" + pk_stock + "' ");
			}
			if (voCons != null && voCons.length > 0 && voCons[0] != null) {
				sWhere.append(" and " + m_dlgQry.getWhereSQL(voCons));
			}
			// 表体VO
			List<TbQycgjh2VO> qmxList = new ArrayList<TbQycgjh2VO>();
			TbQycgjh2VO[] qmx = null;

			// 获取访问数据库对象
			IUAPQueryBS IUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance()
					.lookup(IUAPQueryBS.class.getName());

			ArrayList list = (ArrayList) IUAPQueryBS.retrieveByClause(
					TbQycgjhVO.class, sWhere.toString());
			if (null != list && list.size() > 0) {
				// 表头VO
				TbQycgjhVO[] qvo = new TbQycgjhVO[list.size()];
				qvo = (TbQycgjhVO[]) list.toArray(qvo);

				List<TbQycgjhVO> qyVOList = new ArrayList<TbQycgjhVO>();

				for (int j = 0; j < list.size(); j++) {
					TbQycgjhVO qycgvo = (TbQycgjhVO) list.get(j);
					String mxWhere = " dr = 0 and pk_qycgjh= '"
							+ qycgvo.getPk_qycgjh() + "'";
					ArrayList mxlist = (ArrayList) IUAPQueryBS
							.retrieveByClause(TbQycgjh2VO.class, mxWhere);
					boolean isData = false;
					// 判断总仓还是分仓
					if (isStock) {
						if (null != mxlist && mxlist.size() > 0
								&& null != pkList && pkList.size() > 0) {
							boolean isPkEqer = false;
							for (int i = 0; i < mxlist.size(); i++) {
								TbQycgjh2VO tmpqy = (TbQycgjh2VO) mxlist.get(i);

								for (int z = 0; z < pkList.size(); z++) {
									if (pkList.get(z).equals(
											tmpqy.getQycgjh2_chzj())) {
										isPkEqer = true;

										ArrayList outbList = (ArrayList) CommonUnit
												.getOutGeneralBVO(tmpqy
														.getQycgjh2_chzj(),
														tmpqy.getPk_qycgjh2());
										if (null != outbList
												&& outbList.size() > 0) {
											isData = true;
											break;
										}
										if (null == tmpqy.getQycgjh2_fsl()
												|| tmpqy.getQycgjh2_fsl()
														.toDouble()
														.doubleValue() <= 0)
											break;
										qmxList.add(tmpqy);
										break;
									}
								}
							}
							if (!isData && isPkEqer)
								qyVOList.add(qycgvo);
						}
					} else {

						if (null != mxlist && mxlist.size() > 0) {
							for (int i = 0; i < mxlist.size(); i++) {
								TbQycgjh2VO tmpqy = (TbQycgjh2VO) mxlist.get(i);
								ArrayList outbList = (ArrayList) CommonUnit
										.getOutGeneralBVO(tmpqy
												.getQycgjh2_chzj(), tmpqy
												.getPk_qycgjh2());
								if (null != outbList && outbList.size() > 0) {
									continue;
								}
								qmxList.add(tmpqy);
								isData = true;
							}
						}
						if (isData)
							qyVOList.add(qycgvo);
					}
				}

				if (qyVOList.size() > 0) {
					// 表头VO
					TbQycgjhVO[] qyc = new TbQycgjhVO[qyVOList.size()];
					qyc = (TbQycgjhVO[]) qyVOList.toArray(qyc);
					// 属性发运单主表数组
					this.setQVO(qyc);
					if (qmxList.size() > 0) {
						TbQycgjh2VO[] qycmxvo = new TbQycgjh2VO[qmxList.size()];
						qycmxvo = qmxList.toArray(qycmxvo);
						// 属性发运单字表数组赋值
						this.setQmxVO(qycmxvo);
					}

					getbillListPanel().getBillListData().setHeaderValueVO(qyc);

					getbillListPanel().getHeadBillModel().execLoadFormula();
				}

			}
		} catch (Exception e) {
			SCMEnv.error("数据加载失败！");
			e.printStackTrace(System.out);
		}
	}
}
