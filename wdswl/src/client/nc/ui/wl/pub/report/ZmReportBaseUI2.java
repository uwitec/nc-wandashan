package nc.ui.wl.pub.report;

import java.util.List;
import java.util.Map;

import nc.bs.logging.Logger;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.wl.pub.LongTimeTask;
import nc.vo.pub.BusinessException;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.report.ReportBaseVO;

public class ZmReportBaseUI2 extends ZmReportBaseUI {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2436203500270302801L;

	@Override
	public Map getNewItems() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getQuerySQL() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void initReportUI() {
		// TODO Auto-generated method stub

	}
	
	/**
	 * 分组查询
	 */
	public List<ReportBaseVO[]> getReportVO(String[] sqls)
			throws BusinessException {
		List<ReportBaseVO[]> reportVOs = null;
		try {
			Class[] ParameterTypes = new Class[] { String[].class };
			Object[] ParameterValues = new Object[] { sqls };
			Object o = LongTimeTask.calllongTimeService(
					WdsWlPubConst.WDS_WL_MODULENAME, this, "正在查询...", 1,
					"nc.bs.wds.pub.report.ReportDMO", null, "queryVOBySql",
					ParameterTypes, ParameterValues);
			if (o != null) {
				reportVOs = (List<ReportBaseVO[]>) o;
			}
		} catch (Exception e) {
			Logger.error(e);
			MessageDialog.showErrorDlg(this, "警告", e.getMessage());
		}
		return reportVOs;
	}

}
