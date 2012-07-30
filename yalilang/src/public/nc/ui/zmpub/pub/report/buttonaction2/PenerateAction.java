package nc.ui.zmpub.pub.report.buttonaction2;

import nc.vo.zmpub.pub.report2.ReportBaseUI;

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
