package nc.ui.wl.pub.report.buttonaction;

import nc.ui.wl.pub.report.ReportBaseUI;
/**
 * ¥Ú”°‘§¿¿
 * @author guanyj1
 *
 */
public class PrintPreviewAction extends AbstractActionHasDataAvailable {

	public PrintPreviewAction() {
		super();
	}

	public PrintPreviewAction(ReportBaseUI reportBaseUI) {
		super(reportBaseUI);
	}

	public void execute() throws Exception {
		getReportBaseUI().onPrintPreview();
	}

}
