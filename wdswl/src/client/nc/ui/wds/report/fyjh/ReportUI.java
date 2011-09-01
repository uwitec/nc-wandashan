package nc.ui.wds.report.fyjh;

import nc.bs.logging.Logger;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.wl.pub.LoginInforHelper;
import nc.ui.wl.pub.LongTimeTask;
import nc.ui.wl.pub.report.ReportBaseUI;
import nc.vo.pub.BusinessException;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.WdsWlPubTool;
import nc.vo.wl.pub.report.ReportBaseVO;
/**
 * 
 * @author yf
 * 发运计划 ：录入月计划和追加计划合并报表
 *
 */
public class ReportUI extends ReportBaseUI {
	private String cwhid = null;//仓库
	public ReportUI(){
		super();
		init2();
	}
	//当前操作员  仓库权限
	private void init2(){
		LoginInforHelper login = new LoginInforHelper();
		try {
			cwhid = login.getCwhid(_getUserID());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			cwhid = null;
		}	
	}
	
	@Override
	public String _getModelCode() {
		return "80060435";
	}
	/**
	 * 返回 报表数据
	 */
	@Override
	public ReportBaseVO[] getReportVO(String wheresql) throws BusinessException {
		ReportBaseVO[] reportVOs = null;
		try{
			Class[] ParameterTypes = new Class[]{String.class};
			Object[] ParameterValues = new Object[]{wheresql};
			Object o = LongTimeTask.calllongTimeService(WdsWlPubConst.WDS_WL_MODULENAME, this, 
					"正在查询...", 1, "nc.bs.wds.report.fyjh.ReportFyjhBO", null, 
					"doQuery", ParameterTypes, ParameterValues);
			if(o != null){
				reportVOs = (ReportBaseVO[])o;
			}
		}catch(Exception e){
			Logger.error(e);
			MessageDialog.showErrorDlg(this, "警告", e.getMessage());
		}
		getReportBase().getReportInfoCtrl().setBodyDataVOs(reportVOs);
		return reportVOs;
	}

	@Override
	public void onQuery() {
//		if(PuPubVO.getString_TrimZeroLenAsNull(cstoreid) != null){
//			getQueryDlg().setDefaultValue("pk_outwhouse", null, cstoreid);
//		}
		if(PuPubVO.getString_TrimZeroLenAsNull(cwhid)!=null){
			getQueryDlg().setDefaultValue("pk_outwhouse",cwhid,cwhid);
		}
		getQueryDlg().setDefaultValue("month", null, _getCurrDate().toString());
		int ret = getQueryDlg().showModal();
		if(ret!=UIDialog.ID_OK)
			return;
					
		StringBuffer whereSql = new StringBuffer();
		//解构 条件vos
		ConditionVO cvos[] = getQueryDlg().getConditionVO();
		for (ConditionVO cvo : cvos) {
			String key = cvo.getFieldCode();
			String value = cvo.getValue();
			String op = cvo.getOperaCode();
			//当条件vo为月份统计的时候 修改键值，以匹配月份比较 2011-01
			if("month".equalsIgnoreCase(key)){
				key = "substr(wds_sendplanin.dmakedate, 1, 7)";
				value = value.substring(0, 7);
				getReportBase().setHeadItem("month", value);
			}
			whereSql.append(key+op+"'"+value+"' and ");
		}
		String qryconditons = getQueryDlg().getChText();
    	getReportBase().setHeadItem("qryconditons", qryconditons);
		
//		String where = getQueryDlg().getWhereSQL();
//		if (where != null && !"".equals(where)) {
//			if(where.contains("month")){
//				int start = where.indexOf("month");
//				int end = start + 7;
//				String sub = where.substring(start, end);
//				where.replace("month", "substr(sp.dmakedate, 1, 7)");
//				
//			}
//			whereSql.append(where + " and");
//		}		
		whereSql.append(" wds_sendplanin.pk_corp = '"+_getCorpID()+"'");			//按公司过滤
//		whereSql.append(" and substr(sp.dmakedate, 1, 7) = '2011-07' ");//按月份过滤
		whereSql.append(" and wds_sendplanin.vbillstatus= " + IBillStatus.CHECKPASS);	//行业的单据状态:审批通过
		ReportBaseVO[] vos;
		try {
			vos = getReportVO(whereSql.toString());
		} catch (BusinessException e) {
			e.printStackTrace();
			showErrorMessage(WdsWlPubTool.getString_NullAsTrimZeroLen(e.getMessage()));
			return;
		}
		//清空表体数据
		getReportBase().getBillModel().clearBodyData();
		if(vos == null || vos.length == 0){
			return;
		}
		//设置表体 数据
		getReportBase().getBillModel().setBodyDataVO(vos);
		//执行 模板公式
		getReportBase().getBillModel().execLoadFormula();
	}

	
	@Override
	public void setUIAfterLoadTemplate() {
		// TODO Auto-generated method stub

	}

}
