package nc.ui.zmpub.pub.report.buttonaction;

import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.report.sort.SortDLG;
import nc.ui.zmpub.pub.report.ReportBaseUI;
/**
 * ≈≈–Ú
 * @author guanyj1
 */
public class SortAction extends AbstractActionHasDataAvailable {
	SortDLG sortDlg = null;

	public SortAction() {
		super();
	}

	public SortAction(ReportBaseUI reportBaseUI) {
		super(reportBaseUI);
	}

	public void execute() throws Exception {
		if (getSortDlg().showModal() == UIDialog.ID_OK)
		{
			if (getSortDlg().getSortFields() != null)
			{
				String[] fields = getSortDlg().getSortFields();
				int[] asc = getSortDlg().getAscOrDesc();
				getReportBaseUI().onSort(fields, asc);
				getReportBaseUI().showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory_report","UPPuifactory_report-000034")/*@res "≈≈–Ú≥…π¶"*/);
			}
		}
	}
	private SortDLG getSortDlg()
	{
		if (sortDlg == null)
			sortDlg = new SortDLG(getReportBaseUI(), nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory_report","UPPuifactory_report-000006")/*@res "≈≈–Ú…Ë÷√"*/, getReportBaseUI()
					.getVisibleFields(), null);
		return sortDlg;
	}

}
