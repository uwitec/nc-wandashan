package nc.ui.wds.report.kclcx;

import nc.bs.logging.Logger;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.wl.pub.LoginInforHelper;
import nc.ui.wl.pub.LongTimeTask;
import nc.ui.wl.pub.report.ReportBaseUI;
import nc.vo.pub.BusinessException;
import nc.vo.pub.query.ConditionVO;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.WdsWlPubTool;
import nc.vo.wl.pub.report.ReportBaseVO;
/**
 * 库存量查询报表
 */
public class ReportUI extends ReportBaseUI{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String cstoreid = null;
	public ReportUI() {
		super();
		init2();
	}

	private void init2(){
		LoginInforHelper login = new LoginInforHelper();
		try {
			cstoreid = login.getCwhid(_getUserID());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			cstoreid = null;
		}		
	}
	
	public String _getModelCode() {
        return "80061005";
	}

	@Override
	public ReportBaseVO[] getReportVO(String wheresql) throws BusinessException {
		// TODO Auto-generated method stub
		ReportBaseVO[] reportVOs = null;
		try{
			Class[] ParameterTypes = new Class[]{String.class};
			Object[] ParameterValues = new Object[]{wheresql};
			Object o = LongTimeTask.calllongTimeService(WdsWlPubConst.WDS_WL_MODULENAME, this, 
					"正在查询...", 1, "nc.bs.wds.report.kclcx.ReportKclcxBO", null, 
					"doQuery", ParameterTypes, ParameterValues);
			if(o != null){
				reportVOs = (ReportBaseVO[])o;
			}
		}catch(Exception e){
			Logger.error(e);
			MessageDialog.showErrorDlg(this, "警告", e.getMessage());
		}
	//	getReportBase().getReportInfoCtrl().setBodyDataVOs(reportVOs);
		
		return reportVOs;
	}

	@Override
	public void onQuery() {
		int ret = getQueryDlg().showModal();
		if(ret!=UIDialog.ID_OK)
			return;
		
		ConditionVO[] cons = getQueryDlg().getConditionVO();

		String sql = getQueryDlg().getWhereSQL();
		if(sql==null||sql.length()==0){
		      sql="  h.pk_corp = '"+_getCorpID()+"'";
		}else{
		    sql = sql + "  and h.pk_corp = '"+_getCorpID()+"'";
		}
		ReportBaseVO[] vos;
		try {
			vos = getReportVO(sql);
		} catch (BusinessException e) {
			e.printStackTrace();
			showErrorMessage(WdsWlPubTool.getString_NullAsTrimZeroLen(e.getMessage()));
			return;
		}
		getReportBase().getBillModel().clearBodyData();
		if(vos == null || vos.length == 0){
			return;
		}
		getReportBase().getBillModel().setBodyDataVO(vos);
//		getReportBase().getBillModel().execLoadFormula();
		
	}

	@Override
	public void setUIAfterLoadTemplate() {
		// TODO Auto-generated method stub
		
	}

}
