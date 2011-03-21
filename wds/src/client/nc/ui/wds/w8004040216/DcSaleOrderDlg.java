package nc.ui.wds.w8004040216;

import java.awt.Container;
import java.util.ArrayList;

import javax.swing.event.ListSelectionEvent;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.pf.BillSourceDLG;
import nc.ui.scm.pub.query.SCMQueryConditionDlg;
import nc.ui.wds.w80060204.MyClientUI;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.so.so001.SaleOrderVO;
import nc.vo.so.so001.SaleorderBVO;
import nc.vo.so.so001.SaleorderHVO;

public class DcSaleOrderDlg extends BillSourceDLG {
	Container m_parent = null;

	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		if (getbillListPanel().getHeadTable().getSelectedRowCount() > 0) {

			// ��ȡ��ѡ���е�Id
			Object o = getbillListPanel().getHeadBillModel().getValueAt(
					getbillListPanel().getHeadTable().getSelectedRow(),
					"csaleid");
			// �ж��Ƿ�Ϊ��
			if (o != null && o != "") {
				String csaleid = o.toString();
				for (int i = 0; i < this.saleVO.length; i++) {
					String saleid = this.saleVO[i].getHeadVO().getCsaleid();
					if (csaleid.equals(saleid)) {
						// �����ѡ�����id��Ϊ�� ������ȥ�������ѯ���ӱ�Vo ������ʾ
						getbillListPanel().setBodyValueVO(
								this.saleVO[i].getBodyVOs());
						break;
					}
				}

			}
		}

		// getbillListPanel().getHeadBillModel().execLoadFormula();
		getbillListPanel().getBodyBillModel().execLoadFormula();
	}

	public void onOk() {
		if (getbillListPanel().getHeadBillModel().getRowCount() > 0) {
			AggregatedValueObject[] selectedBillVOs = getbillListPanel()
					.getMultiSelectedVOs(SaleOrderVO.class.getName(),
							SaleorderHVO.class.getName(),
							SaleorderBVO.class.getName());
			retBillVo = selectedBillVOs.length > 0 ? selectedBillVOs[0] : null;
			retBillVos = selectedBillVOs;
			String cumId = null;
			if (retBillVos != null && retBillVos.length > 0) {
				for (int i = 0; i < retBillVos.length; i++) {
					SaleorderHVO salehvo = (SaleorderHVO) retBillVos[i]
							.getParentVO();
					if (null == cumId || "".equals(cumId)) {
						cumId = salehvo.getCcustomerid();
					} else {
						if (!cumId.equals(salehvo.getCcustomerid())) {
							((MyClientUI) m_parent)
									.showWarningMessage("������Ϣ��һ�£����ܺϲ�����!");
							cumId = null;
							return;
						}
					}
				}
			}

		}
		this.closeOK();
	}

	private SCMQueryConditionDlg m_dlgQry = null;

	public SaleOrderVO[] saleVO;

	public DcSaleOrderDlg(String pkField, String pkCorp, String operator,
			String funNode, String queryWhere, String billType,
			String businessType, String templateId, String currentBillType,
			Container parent) {
		super(pkField, pkCorp, operator, funNode, queryWhere, billType,
				businessType, templateId, currentBillType, parent);
		// TODO Auto-generated constructor stub
	}

	public DcSaleOrderDlg(Container parent) {
		super(null, null, null, null, "1=1", "0216", null, null, null, parent);
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
		// �õ����ýڵ�Ĳ�ѯ�Ի���
		// funnode 40092010 ���뵥
		// qrynodekey 40099906 ���յ��ݲ�ѯ
		m_dlgQry = getQueryDlg(pkCorp, funNode, operator, qrynodekey);

		if (m_dlgQry.showModal() == nc.ui.pub.beans.MessageDialog.ID_OK) {

			initVar(null, pkCorp, operator, null, "null", billType, null, null,
					currentBillType, null, parent);
			loadHeadData();
			addBillUI();
			setQueyDlg(m_dlgQry);
			if (showModal() == nc.ui.pub.beans.MessageDialog.ID_OK) {
				// ��ȡ��ѡVO
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
				// ���س�������
				m_dlgQry.hideNormal();
				// ���õ�������
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
			// ���ò�Ʒ�鴫��������뵱ǰ��ѯ�������������������ѯ����
			// ����dplanbindate�Ĳ�ѯ����
			nc.vo.pub.query.ConditionVO[] voCons = m_dlgQry.getConditionVO();

			StringBuffer sWhere = new StringBuffer(
					" dr = 0 and fstatus =2 and boutendflag ='N' and vreceiptcode like '%CO%' and vdef5='2' ");
			if (voCons != null && voCons.length > 0 && voCons[0] != null) {
				sWhere.append(" and " + m_dlgQry.getWhereSQL(voCons));
			}

			// �ۺ�VO
			SaleOrderVO[] saleVO = null;
			SaleOrderVO salevo = null;
			// ��ͷVO
			SaleorderHVO[] salehVO = null;
			// ��ȡ�������ݿ����
			IUAPQueryBS IUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance()
					.lookup(IUAPQueryBS.class.getName());

			ArrayList resultlist = (ArrayList) IUAPQueryBS
					.executeQuery(
							"select csaleid,vreceiptcode ,ccustomerid ,creceiptcustomerid ," +
							" vreceiveaddress,cemployeeid,cbiztype,vnote ," +
							" dbilldate,dapprovedate,cdeptid from so_sale where "
									+ sWhere, new ArrayListProcessor());
			// ���˺�洢�Ľ����
			ArrayList<Object[]> list = new ArrayList<Object[]>();
			// ���й��ˣ��Ƴɶ����ľͲ����ٳ����ˡ��жϽ�����Ƿ�Ϊ��
			if (resultlist != null && resultlist.size() > 0) {
				for (int i = 0; i < resultlist.size(); i++) {
					// ��ȡ��ͷ������
					Object a[] = (Object[]) resultlist.get(i);
					// �ж��Ƿ�Ϊ�պ����ĵ�һ�������Ƿ�Ϊ��
					if (a != null && a.length > 0 && a[0] != null) {
						// ���ݲ�ѯ��������������Ϊ�������в�ѯ���鿴���ݿ����Ƿ��д˼�¼
						String sql = "select count(csaleid) " +
								"from tb_fydnew where billtype = 1 and dr = 0 and csaleid ='"
								+ a[0].toString() + "'";
						ArrayList pwb_count = (ArrayList) IUAPQueryBS
								.executeQuery(sql, new ArrayListProcessor());
						int countnum = Integer.parseInt((((Object[]) pwb_count
								.get(0))[0]).toString());
						if (countnum == 0) {
							list.add(a);
						}
					}
				}
			}

			// �жϱ�ͷ����Ƿ�Ϊ��
			if (list != null && list.size() > 0) {
				// ѭ����ͷ���
				saleVO = new SaleOrderVO[list.size()];
				salehVO = new SaleorderHVO[list.size()];
				for (int i = 0; i < list.size(); i++) {

					salevo = new SaleOrderVO();

					// ��ȡ��ͷ������
					Object a[] = (Object[]) list.get(i);
					// �ж��Ƿ�Ϊ�պ����ĵ�һ�������Ƿ�Ϊ��
					if (a != null && a.length > 0 && a[0] != null) {

						// �������ĵ�һ�����ݲ�ѯ�ӱ�
						ArrayList bodyList = (ArrayList) IUAPQueryBS
								.executeQuery(
										"select csaleid,crowno ,cinvbasdocid ,nnumber ," +
										"npacknumber,corder_bid  from so_saleorder_b where csaleid='"
												+ a[0].toString() + "'",
										new ArrayListProcessor());
						SaleorderHVO salehvo = new SaleorderHVO();

						if (null != a[0] && !"".equals(a[0])) {
							salehvo.setCsaleid(a[0].toString());
						}
						if (null != a[1] && !"".equals(a[1])) {
							salehvo.setVreceiptcode(a[1].toString());
						}
						if (null != a[2] && !"".equals(a[2])) {
							salehvo.setCcustomerid(a[2].toString());
						}
						if (null != a[3] && !"".equals(a[3])) {
							salehvo.setCreceiptcustomerid(a[3].toString());
						}
						if (null != a[4] && !"".equals(a[4])) {
							salehvo.setVreceiveaddress(a[4].toString());
						}
						if (null != a[5] && !"".equals(a[5])) {
							salehvo.setCemployeeid(a[5].toString());
						}
						if (null != a[6] && !"".equals(a[6])) {
							salehvo.setCbiztype(a[6].toString());
						}
						if (null != a[7] && !"".equals(a[7])) {
							salehvo.setVnote(a[7].toString());
						}
						if (null != a[8] && !"".equals(a[8])) {
							salehvo.setDbilldate(new UFDate(a[8].toString()));
						}
						if (null != a[9] && !"".equals(a[9])) {
							salehvo
									.setDapprovedate(new UFDate(a[9].toString()));
						}
						if (null != a[10] && !"".equals(a[10])) {
							salehvo.setCdeptid(a[10].toString());
						}
						salevo.setParentVO(salehvo);

						salehVO[i] = salehvo;

						saleVO[i] = salevo;

						SaleorderBVO[] salebVO = null;
						SaleorderBVO salebvo = null;
						if (bodyList != null && bodyList.size() > 0) {
							salebVO = new SaleorderBVO[bodyList.size()];
							// ѭ���ӱ���
							for (int j = 0; j < bodyList.size(); j++) {
								Object b[] = (Object[]) bodyList.get(j);

								salebvo = new SaleorderBVO();
								// �ж��Ƿ�Ϊ�պ����ĵ�һ�������Ƿ�Ϊ��
								if (b != null && b.length > 0 && b[0] != null) {
									if (null != b[0] && !"".equals(b[0])) {
										salebvo.setCsaleid(b[0].toString());
									}
									if (null != b[1] && !"".equals(b[1])) {
										salebvo.setCrowno(b[1].toString());
									}
									if (null != b[2] && !"".equals(b[2])) {
										salebvo
												.setCinvbasdocid(b[2]
														.toString());
									}
									
									if (null != b[3] && !"".equals(b[3])) {
										salebvo.setNnumber((new UFDouble(b[3]
												.toString())));
									}
									if (null != b[4] && !"".equals(b[4])) {
										salebvo.setNpacknumber((new UFDouble(
												b[4].toString())));
									}
									if (null != b[5] && !"".equals(b[5])) {
										salebvo.setCorder_bid(b[5].toString());
									}
								}
								salebVO[j] = salebvo;
							}
							saleVO[i].setChildrenVO(salebVO);

						}
					}
				}
			}

			this.setSaleVO(saleVO);
			getbillListPanel().getBillListData().setHeaderValueVO(salehVO);

			getbillListPanel().getHeadBillModel().execLoadFormula();
		} catch (Exception e) {
			SCMEnv.error("���ݼ���ʧ�ܣ�");
			e.printStackTrace(System.out);
		}
	}

	public SaleOrderVO[] getSaleVO() {
		return saleVO;
	}

	public void setSaleVO(SaleOrderVO[] saleVO) {
		this.saleVO = saleVO;
	}
}
