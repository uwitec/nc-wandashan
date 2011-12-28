package nc.ui.wds.report.report5;
import java.util.Map;
import javax.swing.ListSelectionModel;
import nc.ui.wds.pub.button.report2.LevelSubTotalAction;
import nc.ui.wds.pub.report2.JxReportBaseUI;
import nc.ui.wds.pub.report2.ReportRowColCrossTool;
import nc.ui.wl.pub.report.WDSWLReportSql;
import nc.vo.wl.pub.WdsWlPubConst;
/**
 * @author mlr 物流箱粉发运台账
 */
public class ReportUI extends JxReportBaseUI {
	private static final long serialVersionUID = 2193523266502400113L;
	public ReportUI() {
		super();
		setLocation(2);
		getReportBase().getBillTable().setSelectionMode(
				ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	}
	
	/**
	 * 查询完成 设置到ui界面之后 后续处理  
	 * @author mlr
	 * @说明：（鹤岗矿业）
	 * 2011-12-22上午10:42:36
	 * @param list
	 * @return
	 * @throws Exception 
	 */
	public void dealQueryAfter() throws Exception{		
		ReportRowColCrossTool.onCross(this, new String[]{"custcode","custname","ordercode","vbillno"
				,"carcode","vdrivername","vdrivercorp","sorderdate","cartime","forderdate",
				"djrfh","fahkw","xsadress","jxstel","zcyxtime","daodz"
				},
                new String[]{"invtypename","chinvcl","invcode","invname","invspec"}, 
                new String[]{"num"});
		setTolal1();//设置合计
	}
	/**
     * 合计
     */
    public void setTolal1() throws Exception {
      new LevelSubTotalAction(this).atuoexecute2();  	
    }  

	@Override
	public Map getNewItems() throws Exception {
		return null;
	}

	@Override
	public String getQuerySQL() throws Exception {
		return WDSWLReportSql.getOrdertoYunDan(getQueryConditon());
	}

	@Override
	public void initReportUI() {

	}

	@Override
	public String _getModelCode() {
		return WdsWlPubConst.report5;
	}
}
