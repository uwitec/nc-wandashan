package nc.ui.wds.report.report6;
import java.util.Map;
import javax.swing.ListSelectionModel;
import nc.ui.wds.pub.button.report2.LevelSubTotalAction;
import nc.ui.wds.pub.report2.JxReportBaseUI;
import nc.ui.wds.pub.report2.ReportRowColCrossTool;
import nc.ui.wl.pub.report.WDSWLReportSql;
import nc.vo.wl.pub.WdsWlPubConst;
/**
 * @author mlr ������۷���̨��(����)
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
	 * ��ѯ��� ���õ�ui����֮�� ��������  
	 * @author mlr
	 * @˵�������׸ڿ�ҵ��
	 * 2011-12-22����10:42:36
	 * @param list
	 * @return
	 * @throws Exception 
	 */
	public void dealQueryAfter() throws Exception{		
		ReportRowColCrossTool.onCross(this, new String[]{"custcode","custname","ordercode","vbillno"},
                new String[]{"invtypename","chinvcl"}, 
                new String[]{"num"});
		setTolal1();//���úϼ�
	}
	/**
     * �ϼ�
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
		return WdsWlPubConst.report6;
	}
}
