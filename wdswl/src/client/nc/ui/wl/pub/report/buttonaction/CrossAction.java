package nc.ui.wl.pub.report.buttonaction;
/**
 * 交叉处理
 * @author guanyj1
 */
import nc.bs.logging.Logger;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.trade.report.controller.CrossInfo;
import nc.ui.trade.report.cross.CrossDLG;
import nc.ui.wl.pub.report.ReportBaseUI;
import nc.vo.pub.BusinessException;

public class CrossAction extends AbstractActionHasDataAvailable {
	private CrossDLG m_crossDlg = null;

	public CrossAction() {
		super();
	}

	public CrossAction(ReportBaseUI reportBaseUI) {
		super(reportBaseUI);
	}

	public void execute() throws Exception {
		if (getCrossDLG().showModal() == CrossDLG.ID_OK)
		{
			CrossInfo result = getCrossDLG().getCrossInfoResult();
			try{
			getReportBaseUI().onCross(result.getTrueNames(CrossInfo.ROWS),
					result.getTrueNames(CrossInfo.COLS),
					result.getTrueNames(CrossInfo.VALUES));
			}
			catch(BusinessException e){
				throw e;
			}
			catch(Exception e){
				MessageDialog.showErrorDlg(getReportBaseUI(),nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory_report","UPPuifactory_report-000015")/*@res "交叉失败"*/,nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory_report","UPPuifactory_report-000016")/*@res "请刷新界面或重新查询后再作交叉操作"*/);
				Logger.error(e.getMessage(),e);
			}
		}
	}
	/**
	 * @return 返回交叉向导框
	 */
	private CrossDLG getCrossDLG()
	{
		if (m_crossDlg == null)
		{
			m_crossDlg = new CrossDLG(getReportBaseUI(),getReportBaseUI().getModelVOs());

		}
		return m_crossDlg;
	}

}
