package nc.ui.wds.ic.so.out;

import java.awt.Container;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.ListSelectionEvent;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.pf.BillSourceDLG;
import nc.ui.scm.pub.query.SCMQueryConditionDlg;
import nc.ui.wds.w8004040204.ssButtun.ISsButtun;
import nc.vo.dm.so.order.SoorderBVO;
import nc.vo.dm.so.order.SoorderVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.lang.UFDate;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.wl.pub.CommonUnit;

public class FydnewDlg extends BillSourceDLG {
	Container m_parent = null;
	private List pkList = null;
	private boolean isStock = false;
	private String pk_stock = null;
	// 获取访问数据库对象
	IUAPQueryBS IUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(
			IUAPQueryBS.class.getName());

	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		if (getbillListPanel().getHeadTable().getSelectedRowCount() > 0) {
			List<SoorderBVO> fydmxList = new ArrayList<SoorderBVO>();
			SoorderBVO[] fydmx = null;
			// 获取所选择行的Id
			Object o = getbillListPanel().getHeadBillModel().getValueAt(
					getbillListPanel().getHeadTable().getSelectedRow(),
					"pk_soorder");
			// 判断是否为空
			if (o != null && o != "" && null != this.fydmxVO
					&& this.fydmxVO.length > 0) {
				String csaleid = o.toString();
				for (int i = 0; i < this.fydmxVO.length; i++) {
					String saleid = this.fydmxVO[i].getPk_soorder();
					if (csaleid.equals(saleid)) {
						// 如果所选择的行id不为空 根据它去缓存里查询出子表Vo 进行显示
						fydmxList.add(this.fydmxVO[i]);
					}
				}
				fydmx = new SoorderBVO[fydmxList.size()];
				fydmx = fydmxList.toArray(fydmx);
				getbillListPanel().setBodyValueVO(fydmx);
			}
		}

		// getbillListPanel().getHeadBillModel().execLoadFormula();
		getbillListPanel().getBodyBillModel().execLoadFormula();
	}

	// 点击确定按钮事件
	public void onOk() {
		if (getbillListPanel().getHeadBillModel().getRowCount() > 0) {
			AggregatedValueObject[] selectedBillVOs = getbillListPanel()
					.getMultiSelectedVOs(
							nc.vo.trade.pub.HYBillVO.class.getName(),
							SoorderVO.class.getName(),
							SoorderBVO.class.getName());
			retBillVo = selectedBillVOs.length > 0 ? selectedBillVOs[0] : null;
			retBillVos = selectedBillVOs;
			if (null == retBillVos || retBillVos.length != 1) {
				((MyClientUI) m_parent).showWarningMessage("请选择一条记录进行制单!");
				return;
			}
		}
		super.closeOK();
	}

	private SCMQueryConditionDlg m_dlgQry = null;

	public SoorderVO[] fydVO;
	public SoorderBVO[] fydmxVO;

	public SoorderVO[] getFydVO() {
		return fydVO;
	}

	public void setFydVO(SoorderVO[] fydVO) {
		this.fydVO = fydVO;
	}

	public SoorderBVO[] getFydmxVO() {
		return fydmxVO;
	}

	public void setFydmxVO(SoorderBVO[] fydmxVO) {
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

	public FydnewDlg(String pkField, String pkCorp, String operator,
			String funNode, String queryWhere, String billType,
			String businessType, String templateId, String currentBillType,
			Container parent,List pkList, boolean isStock,
			String pk_stock) {
		super(pkField, pkCorp, operator, funNode, queryWhere, billType,
				businessType, templateId, currentBillType, parent);
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
				// 托盘指定按钮和自动取货按钮可用
				((MyClientUI) m_parent).getButtonManager().getButton(
						ISsButtun.zdqh).setEnabled(true);
				((MyClientUI) m_parent).getButtonManager().getButton(
						ISsButtun.tpzd).setEnabled(true);
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
				((MyClientUI)m_parent).showErrorMessage(e.getMessage());
			}
		}

		return m_dlgQry;
	}

	public void loadHeadData() {
		try {
			// 获取查询条
			StringBuffer strWhere = new StringBuffer();
			strWhere.append(" isnull(wds_soorder.dr,0)=0 ");
			strWhere.append(" and wds_soorder.vbillstatus =1");
			strWhere.append(" and wds_soorder.dapprovedate<='"+ new UFDate(System.currentTimeMillis()).toString()+"'");
			if (!isStock) {//不是总仓人员的只能看到调出仓库是本仓库的
				strWhere.append(" and wds_soorder.pk_outwhouse = '" + pk_stock + "'");
			}
			String initWhereSql = m_dlgQry.getWhereSQL();
			if(initWhereSql != null && !"".equals(initWhereSql)){
				strWhere.append(" and "+initWhereSql);
			}
			// 表体VO
			List<SoorderBVO> fydmxList = new ArrayList<SoorderBVO>();
			SoorderBVO[] fydmx = null;
			ArrayList list = (ArrayList) IUAPQueryBS.retrieveByClause(
					SoorderVO.class, strWhere.toString());

			if (null != list && list.size() > 0) {
				List<SoorderVO> fydVOList = new ArrayList<SoorderVO>();
				for (int j = 0; j < list.size(); j++) {
					SoorderVO head = (SoorderVO) list.get(j);
					String mxWhere = " dr = 0 and pk_soorder= '"
							+ head.getPk_soorder() + "'";
					ArrayList mxlist = (ArrayList) IUAPQueryBS
							.retrieveByClause(SoorderBVO.class, mxWhere);
					boolean isData = false;
					// 判断总仓还是分仓
					//如果是总仓根据当登录人绑定的货位过滤存货
					if (isStock) {
						if (null != mxlist && mxlist.size() > 0
								&& null != pkList && pkList.size() > 0) {

							boolean isPkEqer = false;
							for (int i = 0; i < mxlist.size(); i++) {
								SoorderBVO body = (SoorderBVO) mxlist
										.get(i);
									if (pkList.contains(body.getPk_invbasdoc())) {
										isPkEqer = true;
										ArrayList outbList = (ArrayList) CommonUnit
												.getOutGeneralBVO(body
														.getPk_invbasdoc(),
														body.getPk_soorder_b());
										if (null != outbList
												&& outbList.size() > 0) {
											isData = true;
											break;
										}
										// 过滤安排数量是空或者0
										if (null == body.getNarrangnmu()
												|| body.getNarrangnmu()
														.toDouble()
														.doubleValue() <= 0)
											break;
										fydmxList.add(body);
										isData = false;
										break;
									}
							}
							if (!isData && isPkEqer)
								fydVOList.add(head);
						}
					} else {
						if (null != mxlist && mxlist.size() > 0) {
							for (int i = 0; i < mxlist.size(); i++) {
								SoorderBVO body = (SoorderBVO) mxlist
										.get(i);
								ArrayList outbList = (ArrayList) CommonUnit
										.getOutGeneralBVO(body
												.getPk_invbasdoc(), body
												.getPk_soorder_b());
								if (null != outbList && outbList.size() > 0) {
									continue;
								}
								fydmxList.add(body);
								isData = true;
							}
						}
						if (isData)
							fydVOList.add(head);
					}
				}

				if (fydVOList.size() > 0) {
					// 表头VO
					SoorderVO[] fyd = new SoorderVO[fydVOList.size()];
					fyd = (SoorderVO[]) fydVOList.toArray(fyd);
					// 属性发运单主表数组
					this.setFydVO(fyd);
					if (fydmxList.size() > 0) {
						SoorderBVO[] fydmxvo = new SoorderBVO[fydmxList
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
			((MyClientUI)m_parent).showErrorMessage(e.getMessage());
			
		}
	}

}
