package nc.ui.zmpub.pub.report.buttonaction2;

import nc.vo.zmpub.pub.report2.ReportBaseUI;
/**
 * ÷±Ω”¥Ú”°
 * @author guanyj1
 *
 */
public class PrintDirectAction extends AbstractActionHasDataAvailable {

	public PrintDirectAction() {
		super();
	}

	public PrintDirectAction(ReportBaseUI reportBaseUI) {
		super(reportBaseUI);
	}

	public void execute() throws Exception {
		getReportBaseUI().onPrint();
	}

}
