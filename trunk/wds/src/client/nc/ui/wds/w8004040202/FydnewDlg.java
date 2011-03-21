package nc.ui.wds.w8004040202;

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
			if (o != null && o != "") {
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
					.getMultiSelectedVOs(nc.vo.trade.pub.HYBillVO.class.getName(), TbFydnewVO.class.getName(),
							TbFydmxnewVO.class.getName());
			retBillVo = selectedBillVOs.length > 0 ? selectedBillVOs[0] : null;
			retBillVos = selectedBillVOs;
			if (retBillVos != null && retBillVos.length > 1) {
				((MyClientUI) m_parent).showWarningMessage("请选择一条记录进行制单!");
				return;

			}

		}
		this.closeOK();
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
		super(null, null, null, null, "1=1", "4202", null, null, null, parent);
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
					" dr = 0 and billtype=1 and vbillstatus = 1 and fyd_fyzt = 0");
			if (voCons != null && voCons.length > 0 && voCons[0] != null) {
				sWhere.append(" and " + m_dlgQry.getWhereSQL(voCons));
			}

			// 表头VO
			TbFydnewVO[] fyd = null;
			// 表体VO
			List<TbFydmxnewVO> fydmxList = new ArrayList<TbFydmxnewVO>();
			TbFydmxnewVO[] fydmx = null;
			// 获取访问数据库对象
			IUAPQueryBS IUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance()
					.lookup(IUAPQueryBS.class.getName());

			ArrayList list = (ArrayList) IUAPQueryBS
					.executeQuery(
							"select fyd_pk,vbillno,srl_pk,pk_busitype,cdeptid,pk_psndoc,pk_kh,fyd_shdz,fyd_bz,csaleid,fyd_begints,fyd_endts,voperatorid,vapproveid,vapprovenote from tb_fydnew where "
									+ sWhere, new ArrayListProcessor());

			// 判断表头结果是否为空
			if (list != null && list.size() > 0) {
				// 循环表头结果
				fyd = new TbFydnewVO[list.size()];
				TbFydnewVO fydvo = null;
				for (int i = 0; i < list.size(); i++) {

					fydvo = new TbFydnewVO();

					// 获取表头的数据
					Object a[] = (Object[]) list.get(i);
					// 判断是否为空和它的第一个数据是否为空
					if (a != null && a.length > 0 && a[0] != null) {

						// 根据它的第一个数据查询子表
						ArrayList bodyList = (ArrayList) IUAPQueryBS
								.executeQuery(
										"select fyd_pk,cfd_pk,crowno,cfd_tj,cfd_xs,csaleid,corder_bid from tb_fydmxnew where dr = 0 and fyd_pk= '"
												+ a[0].toString() + "'",
										new ArrayListProcessor());

						if (null != a[0] && !"".equals(a[0])) {
							fydvo.setFyd_pk(a[0].toString());
						}
						if (null != a[1] && !"".equals(a[1])) {
							fydvo.setVbillno(a[1].toString());
						}
						if (null != a[2] && !"".equals(a[2])) {
							fydvo.setSrl_pk(a[2].toString());
						}
						if (null != a[3] && !"".equals(a[3])) {
							fydvo.setPk_busitype(a[3].toString());
						}
						if (null != a[4] && !"".equals(a[4])) {
							fydvo.setCdeptid(a[4].toString());
						}
						if (null != a[5] && !"".equals(a[5])) {
							fydvo.setPk_psndoc(a[5].toString());
						}
						if (null != a[6] && !"".equals(a[6])) {
							fydvo.setPk_kh(a[6].toString());
						}
						if (null != a[7] && !"".equals(a[7])) {
							fydvo.setFyd_shdz(a[7].toString());
						}
						if (null != a[8] && !"".equals(a[8])) {
							fydvo.setFyd_bz(a[8].toString());
						}
						if (null != a[9] && !"".equals(a[9])) {
							fydvo.setCsaleid(a[9].toString());
						}
						if (null != a[10] && !"".equals(a[10])) {
							fydvo.setFyd_begints(new UFDate(a[10].toString()));
						}
						if (null != a[11] && !"".equals(a[11])) {
							fydvo.setFyd_endts(new UFDate(a[11].toString()));
						}
						if (null != a[12] && !"".equals(a[12])) {
							fydvo.setVoperatorid(a[12].toString());
						}
						if (null != a[13] && !"".equals(a[13])) {
							fydvo.setVapproveid(a[13].toString());
						}
						if (null != a[14] && !"".equals(a[14])) {
							fydvo.setVapprovenote(a[14].toString());
						}
						

						if (bodyList != null && bodyList.size() > 0) {
							// 循环子表结果
							for (int j = 0; j < bodyList.size(); j++) {
								Object b[] = (Object[]) bodyList.get(j);

								TbFydmxnewVO fydmx1 = new TbFydmxnewVO();
								// 判断是否为空和它的第一个数据是否为空
								if (b != null && b.length > 0 && b[0] != null) {
									if (null != b[0] && !"".equals(b[0])) {
										fydmx1.setFyd_pk(b[0].toString());
									}
									if (null != b[1] && !"".equals(b[1])) {
										fydmx1.setCfd_pk(b[1].toString());
									}
									if (null != b[2] && !"".equals(b[2])) {
										fydmx1.setCrowno(b[2].toString());
									}
									if(null!= b[3]&& !"".equals(b[3])){
										fydmx1.setSeb_pk(b[3].toString());
									}
									if (null != b[4] && !"".equals(b[4])) {
										fydmx1.setCfd_xs((new UFDouble(b[4]
												.toString())));
									}
									if (null != b[5] && !"".equals(b[5])) {
										fydmx1.setCsaleid(b[5].toString());
									}
									if (null != b[6] && !"".equals(b[6])) {
										fydmx1.setCorder_bid(b[6].toString());
									}
									fydmxList.add(fydmx1);
								}
							}
						}
					}
					fyd[i] = fydvo;
				}
			}
			fydmx = new TbFydmxnewVO[fydmxList.size()];
			fydmx = fydmxList.toArray(fydmx);
			this.setFydVO(fyd);
			this.setFydmxVO(fydmx);
			getbillListPanel().getBillListData().setHeaderValueVO(fyd);

			getbillListPanel().getHeadBillModel().execLoadFormula();
		} catch (Exception e) {
			SCMEnv.error("数据加载失败！");
			e.printStackTrace(System.out);
		}
	}

}
