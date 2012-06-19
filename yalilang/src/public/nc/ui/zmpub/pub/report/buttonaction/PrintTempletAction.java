package nc.ui.zmpub.pub.report.buttonaction;

import nc.ui.zmpub.pub.report.ReportBaseUI;
/**
 * Ä£°å´òÓ¡
 * @author guanyj1
 *
 */
public class PrintTempletAction extends AbstractActionHasDataAvailable {

	public PrintTempletAction() {
		super();
	}

	public PrintTempletAction(ReportBaseUI reportBaseUI) {
		super(reportBaseUI);
	}

	public void execute() throws Exception {
		getReportBaseUI().onPrintTemplet();
	}

}
