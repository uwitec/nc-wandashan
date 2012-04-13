package nc.ui.dm.so.send;

import nc.bs.logging.Logger;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.wl.pub.LoginInforHelper;
import nc.ui.wl.pub.LongTimeTask;
import nc.vo.pub.BusinessException;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.WdsWlPubTool;
import nc.vo.zmpub.pub.report.ReportBaseVO;
import nc.vo.zmpub.pub.report2.ReportBaseUI;

/**
 * 
 * @author zhf  销售安排后的  次日发运汇总表
 *
 */

public class SoSendUI extends ReportBaseUI {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String cstoreid = null;
	public SoSendUI(){
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
	@Override
	public ReportBaseVO[] getReportVO(String wheresql) throws BusinessException {
		ReportBaseVO[] reportVOs = null;
		try{
			Class[] ParameterTypes = new Class[]{String.class,String.class};
			Object[] ParameterValues = new Object[]{wheresql,cstoreid};
			Object o = LongTimeTask.calllongTimeService(WdsWlPubConst.WDS_WL_MODULENAME, this, 
					"正在查询...", 1, "nc.bs.dm.so.send.SoSendBO", null, 
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
	
	private void setHeadData(ConditionVO[] cons){
		for(ConditionVO con:cons){
			if(con.getFieldCode().equalsIgnoreCase("h.dbilldate")){
				getReportBase().getHeadItem("dbilldate").setValue(con.getValue());
			}
		}		
	}

	@Override
	public void onQuery() {
		
		int ret = getQueryDlg().showModal();
		if(ret!=UIDialog.ID_OK)
			return;
		
		ConditionVO[] cons = getQueryDlg().getConditionVO();
		setHeadData(cons);
		String sql = getQueryDlg().getWhereSQL();

		if(PuPubVO.getString_TrimZeroLenAsNull(cstoreid)!=null){
			sql += "and pk_outwhouse = '"+cstoreid+"'";
		}
		

		sql = sql + "  and h.pk_corp = '"+_getCorpID()+"' and pk_outwhouse = '"+cstoreid+"'";

		ReportBaseVO[] vos;
		try {
			vos = getReportVO(sql);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			showErrorMessage(WdsWlPubTool.getString_NullAsTrimZeroLen(e.getMessage()));
			return;
		}
		getReportBase().getBillModel().clearBodyData();
		if(vos == null || vos.length == 0){
			return;
		}
		getReportBase().getBillModel().setBodyDataVO(vos);
		getReportBase().getBillModel().execLoadFormula();
	}

	@Override
	public void setUIAfterLoadTemplate() {
		// TODO Auto-generated method stub

	}
	
	  public String _getModelCode() {
	        // return _getCE().getModuleCode();
	        return "80060208";
	    }


}
