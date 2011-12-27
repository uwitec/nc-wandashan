package nc.ui.wds.pub.button.report2;

import nc.ui.wds.pub.report2.ReportBaseUI;
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
