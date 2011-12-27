package nc.ui.wds.pub.button.report2;
/**
 * 条件过滤
 * @author guanyj1
 */
import nc.ui.pub.beans.UIDialog;
import nc.ui.wds.pub.report2.ReportBaseUI;
import nc.ui.wds.pub.report2.ZmDataSetFilterDlg;
public class FilterAction extends AbstractActionHasDataAvailable {
	ZmDataSetFilterDlg m_filterDlg = null;
	public FilterAction() {
		super();
	}
	public FilterAction(ReportBaseUI reportBaseUI) {
		super(reportBaseUI);
	}
	public void execute() throws Exception {
		getFilterDLG();
		if (m_filterDlg.showModal() == UIDialog.ID_OK)
		{
			String strFomula = m_filterDlg.getFomula();
			getReportBaseUI().onFilter(strFomula);
			//TODO i18n
			getReportBaseUI().showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory_report","UPPuifactory_report-000017")/*@res "过滤成功"*/);
		}
	}
	private ZmDataSetFilterDlg getFilterDLG()
	{
		
			m_filterDlg = new ZmDataSetFilterDlg(getReportBaseUI(),getReportBaseUI().getVisibleFields(), getReportBaseUI().getAllBodyDataVO());
		
		return m_filterDlg;
	}
}
