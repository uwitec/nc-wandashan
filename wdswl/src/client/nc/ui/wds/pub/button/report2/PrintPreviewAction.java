package nc.ui.wds.pub.button.report2;

import nc.ui.wds.pub.report2.ReportBaseUI;
/**
 * ��ӡԤ��
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
