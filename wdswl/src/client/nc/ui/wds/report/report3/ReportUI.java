package nc.ui.wds.report.report3;
import java.util.List;
import java.util.Map;

import javax.swing.ListSelectionModel;

import nc.ui.wds.pub.button.report2.LevelSubTotalAction;
import nc.ui.wds.pub.report2.ReportRowColCrossTool;
import nc.ui.wds.pub.report2.ZmReportBaseUI2;
import nc.ui.wl.pub.CombinVO;
import nc.ui.wl.pub.report.WDSWLReportSql;
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
	/**
	 * 设置到ui界面之前 处理分组查询后的数据
	 * @author mlr
	 * @说明：（鹤岗矿业）
	 * 2011-12-22上午10:42:36
	 * @param list
	 * @return
	 */
	public ReportBaseVO[] dealBeforeSetUI(List<ReportBaseVO[]> list)throws Exception{		
		return  combinListVOs(list);
	}
	/**
	 * 接收查询的组合sql
	 * @author mlr
	 * @说明：（鹤岗矿业）
	 * 2011-12-22上午10:41:05
	 * @return
	 */
	public String[] getSqls()throws Exception{
		String querycond=getQueryConditon();
		String querys2=null;
		if(querycond!=null&&querycond.length()>0){
		String[] que= querycond.split("and");
		
	//	String query1=null;
		for(int i=0;i<que.length;i++){
		    String[] query=	 que[i].split("=");
		    for(int j=0;j<query.length;j++){
		        if("(dbilldate".equals(query[j].trim())){
		    	    querys2="( geh_dbilldate ="+ query[1];
		        } 
		        if("(srl_pk".equals(query[j].trim())){
		        	if(querys2==null){
		        		querys2="(geh_cwarehouseid ="+query[1];
		        	}else{
		        		querys2=querys2+"and (geh_cwarehouseid ="+query[1];
		        	}
		        }
		    }
		    String[] query1=que[i].split(">=");
		         if("(dbilldate".equals(query1[0].trim())){
		        	 if(querys2==null){
	    	            querys2="( geh_dbilldate >="+ query1[1];
		        	 }else{
		        		 querys2=querys2+"and (geh_dbilldate >="+query1[1];
		        	 }
	              } 
		    String[] query2=que[i].split("<=");
		         if("(dbilldate".equals(query2[0].trim())){
		        	 if(querys2==null){
	    	            querys2="( geh_dbilldate <="+ query2[1];
		        	 }else{
		        		 querys2=querys2+"and (geh_dbilldate <="+query2[1];
		        	 }
	              }
		     }
		}
	
		return new String[] {
				getQuerySQL1(querycond), getQuerySQL2(querys2) };		
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
		ReportRowColCrossTool.onCross(this, new String[]{"cwarehousename","stvclname","dbilldate"},
                new String[]{"invtypename","invclname","invcode","invname","invspec"}, 
                new String[]{"num"});
		setTolal1();//设置合计
	}
	/**
     * 合计
     */
    public void setTolal1() throws Exception {
      new LevelSubTotalAction(this).atuoexecute2();  	
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
