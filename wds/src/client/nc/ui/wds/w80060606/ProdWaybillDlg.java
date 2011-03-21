package nc.ui.wds.w80060606;

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
					"select vbillcode,dbilldate ,cwarehouseid ,cwhsmanagerid ," +
					"pk_corp ,cdptid ,pk_calbody ,cotherwhid ," +
					"cothercorpid ,cothercalbodyid ,cproviderid,cdispatcherid ,cdilivertypeid ,ccustomerid, " +
					"cinventoryid,cbizid ,cbiztype,cgeneralhid " +
					"  from ic_general_h where cgeneralhid" +
					" in(select cgeneralhid from ic_general_b " +
					"where (noutnum <> ntranoutnum or ntranoutnum is null) and dr=0 ) " +
					"and cbilltypecode='4Y' and dr=0 and cothercalbodyid='1021B1100000000001JL' " +
					" and cothercorpid='1021' ");
			if (voCons != null && voCons.length > 0 && voCons[0] != null) {
				sWhere.append(" and " + m_dlgQry.getWhereSQL(voCons));
			}

			IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
					IUAPQueryBS.class.getName());
			ArrayList ttcs = (ArrayList) query.executeQuery(sWhere.toString(),new ArrayListProcessor());
			GeneralBillHeaderVO[] gbhVO = new GeneralBillHeaderVO[ttcs.size()];
			for(int i = 0;i<ttcs.size();i++){
				gbhVO[i]=new GeneralBillHeaderVO();
				Object[] gvo=(Object[])ttcs.get(i);
				gbhVO[i].setVbillcode((String)gvo[0]);
				if(null!=gvo[1]){
					gbhVO[i].setDbilldate(new UFDate(gvo[1].toString()));
				}
				gbhVO[i].setCwarehouseid((String)gvo[2]);
				gbhVO[i].setCwhsmanagerid((String)gvo[3]);
				gbhVO[i].setPk_corp((String)gvo[4]);
				gbhVO[i].setCdptid((String)gvo[5]);
				gbhVO[i].setPk_calbody((String)gvo[6]);
				gbhVO[i].setAttributeValue("cotherwhid", (String)gvo[7]);
				gbhVO[i].setAttributeValue("cothercorpid", (String)gvo[8]);
				gbhVO[i].setAttributeValue("cothercalbodyid", (String)gvo[9]);
				gbhVO[i].setCdilivertypeid((String)gvo[12]);
				gbhVO[i].setCinventoryid((String)gvo[14]);
				gbhVO[i].setCbizid((String)gvo[15]);
				gbhVO[i].setCgeneralhid((String)gvo[17]);
				
				//gbhVO[i].setVbillcode(());
				//gbhvo[i].
			}
			// IICPub_GeneralBillBO
			// QueryVO qrVO=new QueryVO();
			// qrVO.setPk_corp(getPkCorp());
			// qrVO.setWhereSql(sWhere.toString());
			//			
			// NYBalanceVO[] vo=ClientApplicationHelper.queryAppForKJ(qrVO);
			// BuildVO(vo);
			getbillListPanel().setHeaderValueVO(gbhVO);
			getbillListPanel().getHeadBillModel().execLoadFormula();
		} catch (Exception e) {
			SCMEnv.error("数据加载失败！");
			e.printStackTrace(System.out);
		}
	}
}
