package nc.ui.wds.w80060206;

import java.awt.Container;
import java.util.ArrayList;

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
			// 查询调拨出库的集合
			ArrayList os = (ArrayList) query.executeQuery(sWhere.toString(),
					new ArrayListProcessor());
			// 过滤本地表后的集合
			ArrayList ttcs = new ArrayList();
			for (int i = 0; i < os.size(); i++) {
				// Object[] gvo = (Object[]) ttcs.get(i);
				String pwb_sql = "select count(pwb_cgeneralhid) from tb_prodwaybill where "
						+ "dr=0 and pwb_cgeneralhid='"
						+ ((Object[]) os.get(i))[17] + "' and dr=0";
				ArrayList pwb_count = (ArrayList) query.executeQuery(pwb_sql,
						new ArrayListProcessor());
				int countnum = Integer
						.parseInt((((Object[]) pwb_count.get(0))[0]).toString());
				if (countnum == 0) {
					ttcs.add((Object[]) os.get(i));
				}
			}
			// 创建表头VO
			GeneralBillHeaderVO[] gbhVO = new GeneralBillHeaderVO[ttcs.size()];
			// 用来判断查询单据模板是否有数据

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
				gbhVO[i].setAttributeValue("cotherwhid", (String) gvo[7]);
				gbhVO[i].setAttributeValue("cothercorpid", (String) gvo[8]);
				gbhVO[i].setAttributeValue("cothercalbodyid", (String) gvo[9]);
				// 获得收发类型
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

			// //重新填充VO，去除为空的元素
			// for (int i = 0; i < gbhVO.length; i++) {
			//
			// }
			getbillListPanel().setHeaderValueVO(gbhVO);

			getbillListPanel().getHeadBillModel().execLoadFormula();
		} catch (Exception e) {
			SCMEnv.error("数据加载失败！");
			e.printStackTrace(System.out);
		}
	}
}
