package nc.ui.wds.pub.button.report2;
/**
 * ¿∏ƒø…Ë÷√
 * @author guanyj1
 */
import nc.ui.trade.report.columnfilter.ColumnFilterDLG;
import nc.ui.wds.pub.report2.ReportBaseUI;
public class ColumnFilterAction extends AbstractActionAlwaysAvailable {

	public ColumnFilterAction() {
		super();
	}

	public ColumnFilterAction(ReportBaseUI reportBaseUI) {
		super(reportBaseUI);
	}

	public void execute() throws Exception {
		ColumnFilterDLG columnFilterDlg = new ColumnFilterDLG(getReportBaseUI(),
				getReportBaseUI().getInvisibleFields(), getReportBaseUI()
						.getVisibleFields(), getReportBaseUI().getTitle());
		if (columnFilterDlg.showModal() == nc.ui.pub.beans.UIDialog.ID_OK)
		{
			String[] trueNames = new String[columnFilterDlg
					.getVisibleField().length];
			String[] showNames = new String[columnFilterDlg
					.getVisibleField().length];
			for (int i = 0; i < trueNames.length; i++)
			{
				trueNames[i] = columnFilterDlg.getVisibleField()[i]
						.getFieldName();
				trueNames[i] = trueNames[i].replace('.', '_');
				showNames[i] = columnFilterDlg.getVisibleField()[i]
						.getFieldShowName();
			}
			getReportBaseUI().onColumnFilter(columnFilterDlg.getTitle(),
					trueNames, showNames,columnFilterDlg.isAdjustOrder());
		}
	}

}
