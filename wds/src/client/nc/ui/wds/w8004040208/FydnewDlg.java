package nc.ui.wds.w8004040208;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.plaf.PanelUI;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.pf.BillSourceDLG;
import nc.ui.scm.pub.query.SCMQueryConditionDlg;
import nc.ui.trade.button.IBillButton;
import nc.ui.wds.button.IButton;
import nc.ui.wds.w8000.CommonUnit;
import nc.ui.wds.w8004040204.ssButtun.ISsButtun;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.wds.w8004040204.TbOutgeneralBVO;
import nc.vo.wds.w80060401.TbShipentryVO;
import nc.vo.wds.w80060406.TbFydmxnewVO;
import nc.vo.wds.w80060406.TbFydnewVO;

public class FydnewDlg extends BillSourceDLG {
	Container m_parent = null;
	private List pkList = null;
	private boolean isStock = false;
	String pk_stock = "";

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

	public FydnewDlg(Container parent, List pkList, boolean isStock,
			String pk_stock) {
		super(null, null, null, null, "1=1", "4208", null, null, null, parent);
		m_parent = parent;
		this.pkList = pkList;
		this.isStock = isStock;
		this.pk_stock = pk_stock;
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

			StringBuffer sWhere = new StringBuffer(
					" dr = 0 and billtype=0 and (fyd_constatus = 0 or  fyd_constatus is null) and vbillstatus = 1 and fyd_fyzt = 0 and fyd_approstate =1 ");
			if (null != pk_stock && !"".equals(pk_stock)) {
				sWhere.append(" and srl_pk = '" + pk_stock + "' ");
			}
			if (voCons != null && voCons.length > 0 && voCons[0] != null) {
				sWhere.append(" and " + m_dlgQry.getWhereSQL(voCons));
			}
			// 表体VO
			List<TbFydmxnewVO> fydmxList = new ArrayList<TbFydmxnewVO>();
			TbFydmxnewVO[] fydmx = null;
			// 获取访问数据库对象
			IUAPQueryBS IUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance()
					.lookup(IUAPQueryBS.class.getName());

			ArrayList list = (ArrayList) IUAPQueryBS.retrieveByClause(
					TbFydnewVO.class, sWhere.toString());
			if (null != list && list.size() > 0) {
				List<TbFydnewVO> fydVOList = new ArrayList<TbFydnewVO>();
				for (int j = 0; j < list.size(); j++) {
					TbFydnewVO fydvo = (TbFydnewVO) list.get(j);
					String mxWhere = " dr = 0 and fyd_pk= '"
							+ fydvo.getFyd_pk() + "'";
					ArrayList mxlist = (ArrayList) IUAPQueryBS
							.retrieveByClause(TbFydmxnewVO.class, mxWhere);

					boolean isData = false;
					// 判断总仓还是分仓
					if (isStock) {
						if (null != mxlist && mxlist.size() > 0
								&& null != pkList && pkList.size() > 0) {
							boolean isPkEqer = false;
							for (int i = 0; i < mxlist.size(); i++) {
								TbFydmxnewVO tmpfyd = (TbFydmxnewVO) mxlist
										.get(i);

								for (int z = 0; z < pkList.size(); z++) {
									if (pkList.get(z).equals(
											tmpfyd.getPk_invbasdoc())) {
										isPkEqer = true;

										// /根据子表中 主键，单品主键，操作=‘Y’
										// 如果有该保管员做过出库，不显示该数据
										String strWhere = " dr = 0 and isoper = 'Y' and cinventoryid = '"
												+ tmpfyd.getPk_invbasdoc()
												+ "' and csourcebillhid = '"
												+ tmpfyd.getFyd_pk() + "'";
										ArrayList outbList = (ArrayList) IUAPQueryBS
												.retrieveByClause(
														TbOutgeneralBVO.class,
														strWhere);
										if (null != outbList
												&& outbList.size() > 0) {
											isData = true;
											break;
										}
										if (null == tmpfyd.getCfd_xs()
												|| tmpfyd.getCfd_xs()
														.toDouble()
														.doubleValue() <= 0)
											break;
										fydmxList.add(tmpfyd);
										break;
									}
								}
							}
							if (!isData && isPkEqer)
								fydVOList.add(fydvo);
						}
					} else {
						if (null != mxlist && mxlist.size() > 0) {
							for (int i = 0; i < mxlist.size(); i++) {
								TbFydmxnewVO tmpfyd = (TbFydmxnewVO) mxlist
										.get(i);
								ArrayList outbList = (ArrayList) CommonUnit
										.getOutGeneralBVO(tmpfyd
												.getPk_invbasdoc(), tmpfyd
												.getCfd_pk());
								if (null != outbList && outbList.size() > 0) {
									continue;
								}
								fydmxList.add(tmpfyd);
								isData = true;
							}
						}
						if (isData)
							fydVOList.add(fydvo);
					}
				}

				if (fydVOList.size() > 0) {
					// 表头VO
					TbFydnewVO[] fyd = new TbFydnewVO[fydVOList.size()];
					fyd = (TbFydnewVO[]) fydVOList.toArray(fyd);
					// 属性发运单主表数组
					this.setFydVO(fyd);
					if (fydmxList.size() > 0) {
						TbFydmxnewVO[] fydmxvo = new TbFydmxnewVO[fydmxList
								.size()];
						fydmxvo = fydmxList.toArray(fydmxvo);
						// 属性发运单字表数组赋值
						this.setFydmxVO(fydmxvo);
					}

					getbillListPanel().getBillListData().setHeaderValueVO(fyd);

					getbillListPanel().getHeadBillModel().execLoadFormula();
				}

			}
		} catch (Exception e) {
			SCMEnv.error("数据加载失败！");
			e.printStackTrace(System.out);
		}
	}

}
