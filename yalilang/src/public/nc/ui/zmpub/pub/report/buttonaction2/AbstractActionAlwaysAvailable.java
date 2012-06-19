package nc.ui.zmpub.pub.report.buttonaction2;
/**
 * 此类实现了isButtonAvailable,使按钮始终有效
 * @author guanyj
 */
import nc.ui.report.base.IButtonActionAndState;
import nc.vo.zmpub.pub.report2.ReportBaseUI;

public abstract class AbstractActionAlwaysAvailable implements
		IButtonActionAndState {

	protected ReportBaseUI reportBaseUI = null;
	public AbstractActionAlwaysAvailable() {
	}
	public AbstractActionAlwaysAvailable(ReportBaseUI reportBaseUI) {
		this.reportBaseUI = reportBaseUI;
	}
	
	/**
	 * 按钮总是有效
	 */
	public int isButtonAvailable()
	{
		return ENABLE_ALWAYS;
	}
	public ReportBaseUI getReportBaseUI(){
		return reportBaseUI;
	}

	public void setReportBaseUI(ReportBaseUI reportBaseUI){
		this.reportBaseUI = reportBaseUI;
	}
}
