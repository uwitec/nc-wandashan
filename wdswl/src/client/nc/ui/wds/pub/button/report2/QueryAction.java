package nc.ui.wds.pub.button.report2;

import nc.ui.wds.pub.report2.ReportBaseUI;
/**
 * 查询 事件
 * @author guanyj1
 *
 */
public class QueryAction extends AbstractActionAlwaysAvailable {

	public QueryAction() {
	}
	/**
	 * @param reportBaseUI
	 */
	public QueryAction(ReportBaseUI reportBaseUI) {
		super(reportBaseUI);
	}

	public void execute() throws Exception
	{
		getReportBaseUI().onQuery();
		//TODO i18n
		getReportBaseUI().showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory_report","UPPuifactory_report-000031")/*@res "查询成功"*/);
	}
}
