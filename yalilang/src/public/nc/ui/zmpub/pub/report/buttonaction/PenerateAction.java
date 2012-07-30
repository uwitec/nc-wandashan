package nc.ui.zmpub.pub.report.buttonaction;

import nc.ui.zmpub.pub.report.ReportBaseUI;

/**
 * ´©Í¸°´Å¥ÊÂ¼þ
 * 
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
