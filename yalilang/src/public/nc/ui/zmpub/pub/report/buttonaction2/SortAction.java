package nc.ui.zmpub.pub.report.buttonaction2;

import nc.ui.pub.beans.UIDialog;
import nc.vo.zmpub.pub.report2.ReportBaseUI;
import nc.vo.zmpub.pub.report2.ZmSortDLG;

/**
 * ≈≈–Ú
 * 
 * @author guanyj1
 */
public class SortAction extends AbstractActionHasDataAvailable {
	ZmSortDLG ZmSortDLG = null;

	public SortAction() {
		super();
	}

	public SortAction(ReportBaseUI reportBaseUI) {
		super(reportBaseUI);
	}

	public void execute() throws Exception {
		getZmSortDLG();

		if (ZmSortDLG.showModal() == UIDialog.ID_OK) {
			if (ZmSortDLG.getSortFields() != null) {
				String[] fields = ZmSortDLG.getSortFields();
				int[] asc = ZmSortDLG.getAscOrDesc();
				getReportBaseUI().onSort(fields, asc);
				getReportBaseUI().showHintMessage(
						nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"uifactory_report",
								"UPPuifactory_report-000034")/* @res "≈≈–Ú≥…π¶" */);
			}
		}
	}

	private ZmSortDLG getZmSortDLG() {

		ZmSortDLG = new ZmSortDLG(getReportBaseUI(), nc.ui.ml.NCLangRes
				.getInstance().getStrByID("uifactory_report",
						"UPPuifactory_report-000006")/* @res "≈≈–Ú…Ë÷√" */,
				getReportBaseUI().getVisibleFields(), null);
		return ZmSortDLG;
	}

}
