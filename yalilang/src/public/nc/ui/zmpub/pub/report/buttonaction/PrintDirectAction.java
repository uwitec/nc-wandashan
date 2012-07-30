package nc.ui.zmpub.pub.report.buttonaction;

import nc.ui.zmpub.pub.report.ReportBaseUI;

/**
 * ÷±Ω”¥Ú”°
 * 
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
