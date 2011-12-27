package nc.ui.wds.pub.button.report2;

import nc.ui.wds.pub.report2.ReportBaseUI;
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
