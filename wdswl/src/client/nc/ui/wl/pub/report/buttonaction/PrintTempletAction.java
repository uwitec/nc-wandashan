package nc.ui.wl.pub.report.buttonaction;

import nc.ui.wl.pub.report.ReportBaseUI;
/**
 * ģ���ӡ
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
