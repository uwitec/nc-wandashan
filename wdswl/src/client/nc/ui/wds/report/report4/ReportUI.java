package nc.ui.wds.report.report4;
import java.util.List;
import java.util.Map;

import javax.swing.ListSelectionModel;

import nc.ui.wl.pub.report.WDSWLReportSql;
import nc.ui.zmpub.pub.report.buttonaction2.LevelSubTotalAction;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.zmpub.pub.report.IUFTypes;
import nc.vo.zmpub.pub.report.ReportBaseVO;
import nc.vo.zmpub.pub.report2.CombinVO;
import nc.vo.zmpub.pub.report2.ReportRowColCrossTool;
import nc.vo.zmpub.pub.report2.ZmReportBaseUI2;
/**
 * @author mlr ������»��ܱ��� (�ϼ�)
 */
public class ReportUI extends ZmReportBaseUI2 {
	private static final long serialVersionUID = 2193523266502400113L;
    private static String[] combinsFields={"num","bnum"};//�ϲ��ֶ�
    private static String[] comconds={"cwarehouseid","stvcl","dbilldate",
    	                              "invtype","pk_invcl"};//�ϲ�����
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
		        if("(cinventoryid".equals(query[j].trim())){
		        	if(querys2==null){
		        		querys2="(cinventoryid ="+query[1];
		        	}else{
		        		querys2=querys2+"and (geb_cinventoryid ="+query[1];
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
                new String[]{"invtypename","invclname"}, 
                new String[]{"num"});
		setTolal1();//���úϼ�
	}
	/**
     * �ϼ�
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
		return WdsWlPubConst.report4;
	}
}
