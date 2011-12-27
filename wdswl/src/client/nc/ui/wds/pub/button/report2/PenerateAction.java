package nc.ui.wds.pub.button.report2;

import nc.ui.wds.pub.report2.ReportBaseUI;
/**
 * ´©Í¸°´Å¥ÊÂ¼þ
 * @author guanyj1
 *
 */
public class PenerateAction extends AbstractActionHasDataAvailable {

	public PenerateAction() {
		super();
	}

	public PenerateAction(ReportBaseUI reportBaseUI) {
		super(reportBaseUI);
	}

	public void execute() throws Exception {
		getReportBaseUI().onPenerate();
	}

}
