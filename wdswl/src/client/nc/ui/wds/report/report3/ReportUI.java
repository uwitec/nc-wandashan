package nc.ui.wds.report.report3;
import java.util.List;
import java.util.Map;
import javax.swing.ListSelectionModel;
import nc.ui.wds.pub.report2.ReportRowColCrossTool;
import nc.ui.wds.pub.report2.ZmReportBaseUI2;
import nc.ui.wl.pub.CombinVO;
import nc.ui.wl.pub.report.WDSWLReportSql;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.report.IUFTypes;
import nc.vo.wl.pub.report.ReportBaseVO;
/**
 * @author mlr ������»��ܱ��� 
 */
public class ReportUI extends ZmReportBaseUI2 {
	private static final long serialVersionUID = 2193523266502400113L;
    private static String[] combinsFields={"num","bnum"};//�ϲ��ֶ�
    private static String[] comconds={"cwarehouseid","stvcl","dbilldate",
    	                              "invtype","pk_invcl","pk_invmandoc",
    	                              "pk_invbasdoc",};//�ϲ�����
    private static int[] types={IUFTypes.UFD,IUFTypes.UFD};//�ϲ�����
	public ReportUI() {
		super();
		setLocation(2);
		getReportBase().getBillTable().setSelectionMode(
				ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	}
	/**
	 * ���õ�ui����֮ǰ ��������ѯ�������
	 * @author mlr
	 * @˵�������׸ڿ�ҵ��
	 * 2011-12-22����10:42:36
	 * @param list
	 * @return
	 */
	public ReportBaseVO[] dealBeforeSetUI(List<ReportBaseVO[]> list)throws Exception{		
		return  combinListVOs(list);
	}
	/**
	 * ���ղ�ѯ�����sql
	 * @author mlr
	 * @˵�������׸ڿ�ҵ��
	 * 2011-12-22����10:41:05
	 * @return
	 */
	public String[] getSqls()throws Exception{
		return new String[] {
				getQuerySQL1(getQueryConditon()), getQuerySQL2(getQueryConditon()) };		
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
		
		ReportRowColCrossTool.onCross(this, new String[]{"cwarehousename","stvclname","dbilldate"},
                new String[]{"invtypename","invclname","invcode","invname","invtype","invspec"}, 
                new String[]{"num"});
	}
    /**
     * �������ݽ����γɶ�̬��ά��
     * @���ߣ�mlr
     * @˵�������ɽ������Ŀ 
     * @ʱ�䣺2011-12-19����03:24:11
     * @param vos
     * @throws Exception 
     */
	private void setQueryAfter(ReportBaseVO[] vos) throws Exception {
		}
	private String getQuerySQL2(String whereSql) {
		return WDSWLReportSql.getInStore(whereSql);
	}

	private String getQuerySQL1(String whereSql) {
		return WDSWLReportSql.getOutStore(whereSql);
	}
	private ReportBaseVO[] combinListVOs(List<ReportBaseVO[]> list)
			throws Exception {
		if (list == null || list.size() == 0) {
			return null;
		}
		ReportBaseVO[] rvos=list.get(0);
		ReportBaseVO[]nrvos=(ReportBaseVO[]) CombinVO.combinData(rvos, comconds, combinsFields, ReportBaseVO.class);
		ReportBaseVO[] rvos1=list.get(1);
		ReportBaseVO[]nrvos1=(ReportBaseVO[]) CombinVO.combinData(rvos1, comconds, combinsFields, ReportBaseVO.class);
		ReportBaseVO[] news=(ReportBaseVO[]) CombinVO.combinVoByFields(nrvos, nrvos1, comconds, types, combinsFields);
	    return news;
	}

	@Override
	public Map getNewItems() throws Exception {
		return null;
	}

	@Override
	public String getQuerySQL() throws Exception {
		return null;
	}

	@Override
	public void initReportUI() {

	}

	@Override
	public String _getModelCode() {
		return WdsWlPubConst.report3;
	}
}
