package nc.ui.wds.report.report3;
import java.util.List;
import java.util.Map;
import javax.swing.ListSelectionModel;
import nc.ui.pub.beans.UIDialog;
import nc.ui.wl.pub.CombinVO;
import nc.ui.wl.pub.report.ReportRowColCrossTool;
import nc.ui.wl.pub.report.WDSWLReportSql;
import nc.ui.wl.pub.report.ZmReportBaseUI2;
import nc.vo.pub.BusinessException;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.report.IUFTypes;
import nc.vo.wl.pub.report.ReportBaseVO;
/**
 * @author mlr 出入库月汇总报表 
 */
public class ReportUI extends ZmReportBaseUI2 {
	private static final long serialVersionUID = 2193523266502400113L;
    private static String[] combinsFields={"num","bnum"};//合并字段
    private static String[] comconds={"cwarehouseid","stvcl","dbilldate",
    	                              "invtype","pk_invcl","pk_invmandoc",
    	                              "pk_invbasdoc",};//合并条件
    private static int[] types={IUFTypes.UFD,IUFTypes.UFD};//合并类型
	public ReportUI() {
		super();
		setLocation(2);
		getReportBase().getBillTable().setSelectionMode(
				ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	}
	@Override
	public void onQuery() {
		getQueryDlg().showModal();
		if (getQueryDlg().getResult() == UIDialog.ID_OK) {
			try {
				// 清空表体数据
				clearBody();
				setDynamicColumn1();
				// 得到查询结果
				List<ReportBaseVO[]> list = getReportVO(new String[] {
						getQuerySQL1(getQueryConditon()), getQuerySQL2(getQueryConditon()) });

				ReportBaseVO[] vos = null;
				vos = combinListVOs(list);
				if (vos == null || vos.length == 0)
					return;
				if (vos != null) {
					super.updateBodyDigits();
					setBodyVO(vos);
					//setQueryAfter(vos);
					//setTolal();
				}
			} catch (BusinessException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
    /**
     * 进行数据交叉形成动态二维表
     * @作者：mlr
     * @说明：完达山物流项目 
     * @时间：2011-12-19下午03:24:11
     * @param vos
     * @throws Exception 
     */
	private void setQueryAfter(ReportBaseVO[] vos) throws Exception {
		ReportRowColCrossTool.onCross(this, new String[]{"cwarehousename","stvclname","dbilldate"},
				                            new String[]{"invtypename","invclname","invcode","invname","invtype","invspec"}, 
				                            new String[]{"num"});
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
