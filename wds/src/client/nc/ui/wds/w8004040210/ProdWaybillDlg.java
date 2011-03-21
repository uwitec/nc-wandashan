package nc.ui.wds.w8004040210;

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
import nc.vo.wds.w8004040210.TbGeneralBVO;
import nc.vo.wds.w80060804.TbTranscompanyVO;

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
		super(null, null, null, null, "1=1", "4Y", null, null, null, parent);
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
					"select distinct head.vbillcode,head.dbilldate ,head.cwarehouseid ,head.cwhsmanagerid ,head.pk_corp ,"
							+ "head.cdptid ,head.pk_calbody ,head.cotherwhid ,head.cothercorpid ,head.cothercalbodyid ,"
							+ "head.cproviderid,head.cdispatcherid ,head.cdilivertypeid ,head.ccustomerid, "
							+ "head.cinventoryid,head.cbizid,head.cbiztype,head.cgeneralhid,head.fallocflag  "
							+ " from ic_general_h head inner join ic_general_b body "
							+ " on head.cgeneralhid = body.cgeneralhid  "
							+ " left outer join ic_general_bb3 bb3 "
							+ " ON body.cgeneralbid = bb3.cgeneralbid AND bb3.dr = 0"
							+ " inner join to_bill_b tobill on body.cfirstbillbid = tobill.cbill_bid"
							+ " WHERE head.cbilltypecode = '4Y'"
							+ " and head.fallocflag <> 0"
							+ " and (tobill.frowstatuflag <> 7 and tobill.frowstatuflag <> 9)"
							+ " and coalesce(head.boutretflag, 'N') = 'N'"
							+ " and ((coalesce(body.noutnum, 0) > 0 "
							+ " and coalesce(body.noutnum, 0) - coalesce(body.ntranoutnum, 0) - coalesce(body.naccumwastnum, 0) > 0)"
							+ " or (coalesce(body.noutnum, 0) < 0"
							+ " and coalesce(body.noutnum, 0) - coalesce(body.ntranoutnum, 0) - coalesce(body.naccumwastnum, 0) < 0))"
							+ " and head.dr = 0"
							+ " and body.dr = 0"
							+ " and tobill.dr = 0"
							+ " and (head.cothercorpid = '1021'  "
							+ " and  (head.cothercorpid = '1021'"
							+ " AND (head.fbillflag = 3 OR head.fbillflag = 4) "
							+ " and COALESCE(head.boutretflag, 'N') = 'N') and (cbilltypecode = '4Y'))");
			if (voCons != null && voCons.length > 0 && voCons[0] != null) {
				sWhere.append(" and " + m_dlgQry.getWhereSQL(voCons));
			}

			IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
					IUAPQueryBS.class.getName());
			// ��ѯ��������ļ���
			ArrayList os = (ArrayList) query.executeQuery(sWhere.toString(),
					new ArrayListProcessor());
			// ���˱��ر��ļ���
			ArrayList ttcs = new ArrayList();
			for (int i = 0; i < os.size(); i++) {
				// Object[] gvo = (Object[]) ttcs.get(i);
				String pwb_sql = "select count(geh_cgeneralhid) from tb_general_h where "
						+ "dr=0 and geh_cgeneralhid='"
						+ ((Object[]) os.get(i))[17] + "' and dr=0";
				ArrayList pwb_count = (ArrayList) query.executeQuery(pwb_sql,
						new ArrayListProcessor());
				int countnum = Integer
						.parseInt((((Object[]) pwb_count.get(0))[0]).toString());
				if (countnum == 0) {
					List invLisk = nc.ui.wds.w8000.CommonUnit
							.getInvbasdoc_Pk(ClientEnvironment.getInstance()
									.getUser().getPrimaryKey());
					String geb_sql = "select count(*) from ic_general_b where cgeneralhid ='"
							+ ((Object[]) os.get(i))[17]
							+ "' and cinvbasid in('";
					for (int k = 0; k < invLisk.size(); k++) {
						if (null != invLisk && invLisk.size() > 0
								&& null != invLisk.get(k)
								&& !"".equals(invLisk.get(k))) {
							geb_sql += invLisk.get(k) + "','";
						}
					}
					geb_sql += "') and dr=0";
					geb_sql += " and ((coalesce(noutnum, 0) > 0 "
							+ " and coalesce(noutnum, 0) - coalesce(ntranoutnum, 0) - coalesce(naccumwastnum, 0) > 0)"
							+ " or (coalesce(noutnum, 0) < 0"
							+ " and coalesce(noutnum, 0) - coalesce(ntranoutnum, 0) - coalesce(naccumwastnum, 0) < 0))"
							+ " and dr=0";
					ArrayList icgebs = (ArrayList) query.executeQuery(geb_sql,
							new ArrayListProcessor());
					int countnum1 = Integer
							.parseInt((((Object[]) icgebs.get(0))[0])
									.toString());
					if (0 != countnum1) {
						ttcs.add((Object[]) os.get(i));
					}

				} else {
					// ��ǰ��¼�ı���Ա�ܹ���Ļ�Ʒ
					List invLisk = nc.ui.wds.w8000.CommonUnit
							.getInvbasdoc_Pk(ClientEnvironment.getInstance()
									.getUser().getPrimaryKey());
					String geb_sql = "select count(*) from ic_general_b where cgeneralhid ='"
							+ ((Object[]) os.get(i))[17]
							+ "' and cinvbasid in('";
					for (int k = 0; k < invLisk.size(); k++) {
						if (null != invLisk && invLisk.size() > 0
								&& null != invLisk.get(k)
								&& !"".equals(invLisk.get(k))) {
							geb_sql += invLisk.get(k) + "','";
						}
					}
					geb_sql += "') and dr=0";
					geb_sql += " and ((coalesce(noutnum, 0) > 0 "
							+ " and coalesce(noutnum, 0) - coalesce(ntranoutnum, 0) - coalesce(naccumwastnum, 0) > 0)"
							+ " or (coalesce(noutnum, 0) < 0"
							+ " and coalesce(noutnum, 0) - coalesce(ntranoutnum, 0) - coalesce(naccumwastnum, 0) < 0))"
							+ " and dr=0";
					ArrayList icgebs = (ArrayList) query.executeQuery(geb_sql,
							new ArrayListProcessor());
					int countnum1 = Integer
							.parseInt((((Object[]) icgebs.get(0))[0])
									.toString());
					if (0 != countnum1) {
						// �鿴������ϸ�Ƿ�ȫ���ر�
						String gebbvosql = " dr=0 and geb_cgeneralhid ='"
								+ ((Object[]) os.get(i))[17]
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
														isallclose = false;
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
							ttcs.add((Object[]) os.get(i));
						}
					}

				}
			}
			// ������ͷVO
			GeneralBillHeaderVO[] gbhVO = new GeneralBillHeaderVO[ttcs.size()];
			// �����жϲ�ѯ����ģ���Ƿ�������

			for (int i = 0; i < ttcs.size(); i++) {

				Object[] gvo = (Object[]) ttcs.get(i);

				gbhVO[i] = new GeneralBillHeaderVO();
				gbhVO[i].setVbillcode((String) gvo[0]);
				if (null != gvo[1]) {
					gbhVO[i].setDbilldate(new UFDate(gvo[1].toString()));
				}

				gbhVO[i].setCwarehouseid((String) gvo[2]);
				gbhVO[i].setCwhsmanagerid((String) gvo[3]);
				gbhVO[i].setPk_corp((String) gvo[4]);
				gbhVO[i].setCdptid((String) gvo[5]);
				gbhVO[i].setPk_calbody((String) gvo[6]);
				gbhVO[i]
						.setAttributeValue("cotherwhid", "1021A91000000004YZ0P");
				gbhVO[i].setAttributeValue("cothercorpid", (String) gvo[8]);
				gbhVO[i].setAttributeValue("cothercalbodyid", (String) gvo[9]);
				// ����շ�����
				if (gvo[11] != null) {
					gbhVO[i].setCdispatcherid(gvo[11].toString());

				}

				gbhVO[i].setCdilivertypeid((String) gvo[12]);
				gbhVO[i].setCinventoryid((String) gvo[14]);
				gbhVO[i].setCbizid((String) gvo[15]);
				gbhVO[i].setCgeneralhid((String) gvo[17]);
				if (null != gvo[18]) {
					gbhVO[i]
							.setFallocflag(Integer.parseInt(gvo[18].toString()));
				}

			}

			// //�������VO��ȥ��Ϊ�յ�Ԫ��
			// for (int i = 0; i < gbhVO.length; i++) {
			//
			// }
			getbillListPanel().setHeaderValueVO(gbhVO);

			getbillListPanel().getHeadBillModel().execLoadFormula();
		} catch (Exception e) {
			SCMEnv.error("���ݼ���ʧ�ܣ�");
			e.printStackTrace(System.out);
		}
	}
}
