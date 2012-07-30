package nc.ui.zmpub.pub.report.buttonaction;

/**
 * 此类实现了isButtonAvailable,使按钮在有数据的时候有效 用于nc.ui.pm.pub.report.ReportBaseUI
 * @author guanyj1
 * 2007.8.10
 */
import nc.ui.report.base.IButtonActionAndState;
import nc.ui.zmpub.pub.report.ReportBaseUI;

public abstract class AbstractActionHasDataAvailable implements
		IButtonActionAndState {
	protected ReportBaseUI reportBaseUI = null;

	public AbstractActionHasDataAvailable() {
	}

	public AbstractActionHasDataAvailable(ReportBaseUI reportBaseUI) {
		this.reportBaseUI = reportBaseUI;
	}

	/**
	 * 表体有数据时按钮有效
	 */
	public int isButtonAvailable() {
		return ENABLE_WHEN_HAS_DATA;
	}

	/**
	 * 
	 * @return ReportBaseUI
	 */
	public ReportBaseUI getReportBaseUI() {
		return reportBaseUI;
	}

	public void setReportBaseUI(ReportBaseUI reportBaseUI) {
		this.reportBaseUI = reportBaseUI;
	}
}
