package nc.ui.wds.report.xfzkc;
import java.util.List;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumnModel;
import nc.bd.accperiod.AccountCalendar;
import nc.bs.logging.Logger;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.table.ColumnGroup;
import nc.ui.pub.beans.table.GroupableTableHeader;
import nc.ui.wl.pub.LongTimeTask;
import nc.ui.wl.pub.report.CombinVO;
import nc.ui.wl.pub.report.ReportBaseUI;
import nc.ui.wl.pub.report.WDSWLReportSql;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.vosplit.SplitBillVOs;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.report.IUFTypes;
import nc.vo.wl.pub.report.ReportBaseVO;
import nc.vo.wl.pub.report.SubtotalVO;
/**
 * 物流箱粉总库存报表
 * @author mlr
 */
public class ReportUI extends ReportBaseUI{
	private static final long serialVersionUID = 1L;
	//存货分类箱粉编码
	private static  String invclcode = "00";
	//常用存货显示字段
	private static String  invcommon="常用存货";
	//不常用存货显示字段
	private static String  uninvcommon="不常用存货";
	//存货类型字段   0表示常用     1表示不常用
	private static String  invtype="invtype";	
	private String pk_stordoc=null;
    private String ddatefrom =null;
    private String ddateto = null;
    //存货类型名字
	private static String invtypename="invatypename";
    //主单位 数量前缀
    private static String unit="unit";
    //辅单位 数量前缀
    private static String bunit="bunit";  
    //按 存货类型  存货分类   存货维度分组的字段数组
    private static String[] fields=new String[]{"invtype","pk_invcl","pk_invbasdoc"};
    //库龄字段名字
    private static String  days="days";
    //用于设置库龄存货数量数对应的主数量字段名
    private static String num="num";
     //用于设置库龄存货数量对应的辅数量字段名
    private static String bnum="bnum";
    //库存状态 待检  对应的主键值
    private static String  stateid="1021S31000000009FS99";
    //库存状态对应的主键字段名字
    private static String  stockstate="ss_pk"; 
    //待发和在途对应的类型字段名
    private static String type="type"; 
    //计划主数量字段值名字
    private static String numplan="plannum";
    //计划辅数量字段值名字
    private static String bnumplan="bplannum";
    //vo合并的条件
    private static String[] voCombinConds={"invtype","pk_invcl","pk_invbasdoc"};
    //将要合并的求值的类型
    private static int[] types={IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD
    	                        ,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD};
    //将要合并的求值子段
    private static String[] combinFields={"unit1","unit2","unit3","unit4","unit5","unit6","unit7","unit8","unit9","unit10","unit11",
    	                                  "bunit1","bunit2","bunit3","bunit4","bunit5","bunit6","bunit7","bunit8","bunit9","bunit10","bunit11",  };
	@Override
	public String _getModelCode() {	
		return WdsWlPubConst.REPORT15;
	}
	public ReportUI() {
		super();
		initReportUI();
	}
	/**
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目
	 *        完成ui初始化设置 
	 * @时间：2011-7-8下午03:20:53
	 */
	private void initReportUI() {		
	  		
	}
	/**
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 *      设置查询条件
	 * @时间：2011-7-15下午01:08:49
	 */
	private void setQueryCondition() {
		ConditionVO[] vos=getQueryDlg().getConditionVO();
    	//从查询对话框,获取仓库主键
    	int size=vos.length;
    	//从查询对话框,获取是否货位展开
    
    	pk_stordoc=null;
    	for(int i=0;i<size;i++){
    		if(vos[i].getFieldCode().equalsIgnoreCase("pk_stordoc")){
    			pk_stordoc=vos[i].getValue();
    		} 		
    	}		
	}	
	 /**
     * 基本列合并
     */
    private void setColumn() {
        //表体栏目分组设置
        UITable cardTable = getReportBase().getBillTable();
        GroupableTableHeader cardHeader = (GroupableTableHeader) cardTable.getTableHeader();
        TableColumnModel cardTcm = cardTable.getColumnModel();
        
        ColumnGroup zgroup=new ColumnGroup("货龄");
        ColumnGroup a1=new ColumnGroup("30天以内");
        a1.add(cardTcm.getColumn(5));
        a1.add(cardTcm.getColumn(6));
        zgroup.add(a1);
        ColumnGroup a2=new ColumnGroup("30-60天");
        a2.add(cardTcm.getColumn(7));
        a2.add(cardTcm.getColumn(8));
        zgroup.add(a2);
        ColumnGroup a3=new ColumnGroup("60-90天");
        a3.add(cardTcm.getColumn(9));
        a3.add(cardTcm.getColumn(10));
        zgroup.add(a3);
        ColumnGroup a4=new ColumnGroup("90天以后");
        a4.add(cardTcm.getColumn(11));
        a4.add(cardTcm.getColumn(12));
        zgroup.add(a4);      
        cardHeader.addColumnGroup(zgroup);
              
        ColumnGroup zgroup2=new ColumnGroup("小计");
        zgroup2.add(cardTcm.getColumn(13));
        zgroup2.add(cardTcm.getColumn(14));
        cardHeader.addColumnGroup(zgroup2);
                
        ColumnGroup zgroup3=new ColumnGroup("促销品");
        zgroup3.add(cardTcm.getColumn(15));
        zgroup3.add(cardTcm.getColumn(16));
        cardHeader.addColumnGroup(zgroup3);
        
        ColumnGroup a11=new ColumnGroup("在途");
        a11.add(cardTcm.getColumn(17));
        a11.add(cardTcm.getColumn(18));
        cardHeader.addColumnGroup(a11);
        
        
        ColumnGroup a22=new ColumnGroup("待检");
        a22.add(cardTcm.getColumn(19));
        a22.add(cardTcm.getColumn(20));
        cardHeader.addColumnGroup(a22);
        
        
        ColumnGroup zgroup4=new ColumnGroup("合计");
        zgroup4.add(cardTcm.getColumn(21));
        zgroup4.add(cardTcm.getColumn(22));
        cardHeader.addColumnGroup(zgroup4);
        
        ColumnGroup a33=new ColumnGroup("待发");
        a33.add(cardTcm.getColumn(23));
        a33.add(cardTcm.getColumn(24));
        cardHeader.addColumnGroup(a33); 
        
        ColumnGroup a5=new ColumnGroup("预发后库存");
        a5.add(cardTcm.getColumn(25));
        a5.add(cardTcm.getColumn(26));
        cardHeader.addColumnGroup(a5);      
        getReportBase().getBillModel().updateValue();
    }
	@Override
	public void setUIAfterLoadTemplate() {
		 getReportBase().getBillTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION); 
		 setColumn();
	}
	
	public List<ReportBaseVO[]> getReportVO(String[] sqls) throws BusinessException {
		 List<ReportBaseVO[]> reportVOs = null;
	        try{
	            Class[] ParameterTypes = new Class[]{String[].class};
	            Object[] ParameterValues = new Object[]{sqls};
	            Object o = LongTimeTask.calllongTimeService(WdsWlPubConst.WDS_WL_MODULENAME, this, 
	                    "正在查询...", 1, "nc.bs.wds.pub.report.ReportDMO", null, 
	                    "queryVOBySql", ParameterTypes, ParameterValues);
	            if(o != null){
	                reportVOs = (List<ReportBaseVO[]>)o;
	            }
	        }catch(Exception e){
	            Logger.error(e);
	            MessageDialog.showErrorDlg(this, "警告", e.getMessage());
	        }
	        return reportVOs;
	}
	@Override
	public void onQuery() {
		try{
		  	//设置查询模板默认查询条件
	        AccountCalendar  accCal = AccountCalendar.getInstance();     
	        getQueryDlg().setDefaultValue("ddatefrom", accCal.getMonthVO().getBegindate().toString(), "");
	        getQueryDlg().setDefaultValue("ddateto", accCal.getMonthVO().getEnddate().toString(), "");
			getQueryDlg().showModal();
		     if (getQueryDlg().getResult() == UIDialog.ID_OK) {		
		    	 setQueryCondition();
            	//校验开始日期，截止日期
            	UIRefPane obj1 = (UIRefPane)getQueryDlg().getValueRefObjectByFieldCode("ddatefrom");
            	UIRefPane obj2 = (UIRefPane)getQueryDlg().getValueRefObjectByFieldCode("ddateto");
            	ddatefrom = obj1.getRefName();
            	if(ddatefrom == null ||"".equalsIgnoreCase(ddatefrom)){
            		showErrorMessage("请输入开始日期");
            		return ;
            	}
            	//截止日期如果为空，则默认为当前日期
            	ddateto = obj2.getRefName();
            	if(ddateto == null || "".equalsIgnoreCase(ddateto)){
            		ddateto = _getCurrDate().toString();
            	}
            	//中文的查询条件
            	String qryconditons = getQueryDlg().getChText();
            	if(!qryconditons.contains("截止日期")){
            		qryconditons = qryconditons+"并且(截止日期 小于等于 '"+ddateto+"')";
            	}
            	getReportBase().setHeadItem("ddatefrom", ddatefrom);
            	getReportBase().setHeadItem("ddateto", ddateto);
            	getReportBase().getHeadItem("qryconditons").setWidth(2);
            	getReportBase().setHeadItem("qryconditons", qryconditons);
            	//得到自定义查询条件
                //得到查询结果
                List<ReportBaseVO[]> list=getReportVO(new String[]{getQuerySQL(),getQuerySQL1(),getQuerySQL2()});
                ReportBaseVO[] vos1= list.get(1);
                ReportBaseVO[] vos=list.get(0);  
                ReportBaseVO[] vos2= list.get(2); 
                if(vos1 != null&&vos1.length>0 || vos!=null&&vos1.length>0 || vos2!=null&&vos2.length>0 ){                	
					super.updateBodyDigits();
				    ReportBaseVO[]newVos=setVoByContion(vos);
				    ReportBaseVO[]newVos1=setVoByContion(vos1);
				    ReportBaseVO[] combins=CombinVO.combinVoByFields(newVos,newVos1,voCombinConds,types,combinFields);
				    ReportBaseVO[]newVos2=setVoByContion(vos2);
				    ReportBaseVO[] combins1=CombinVO.combinVoByFields(newVos2,combins,voCombinConds,types,combinFields);		    
				    setAfterQuery(combins1);
				    setReportBaseVO(combins1);
					setBodyVO(combins1);	
					setDefSubtotal(new String[]{"invclname"}, combinFields);  
                }                
	          }
		} catch (Exception e) {
            e.printStackTrace();
            showWarningMessage(e.getMessage());
        }
	}
	/**
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 *       查询结束后,对vo的后续处理
	 * @时间：2011-7-13下午03:07:52
	 * @param combins
	 */
	private void setAfterQuery(ReportBaseVO[] combins) {
		if (combins == null || combins.length == 0) {
			return;
		}
		int size = combins.length;
		for (int i = 0; i < size; i++) {
			Integer type = PuPubVO.getInteger_NullAs(combins[i].getAttributeValue(invtype), new Integer(0));
			if (type == 0) {
				combins[i].setAttributeValue(invtypename, invcommon);
			} else if (type == 1) {
				combins[i].setAttributeValue(invtypename, uninvcommon);
			}
		}	
	}
	/**
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 *      按照报表需求,加工初次查询形成的报表vo
	 *      加工条件：
	 *      首先 按 存货类型 存货分类 存货 进行分组,然后对每组vo进行合并,将每组vo合并按要求合并成一个vo
	 *      按什么条件合并呢？
	 *      首先判断货龄:  如果是30天以内 将该vo的库存数量加到表示30天以内的字段上
	 *                    如果是30-60天  将该vo的库存数量加到表示30-60天的字段上
	 *                    如果是60-90天  将该vo的库存数量加到表示60-90天的字段上
	 *                    如果是90天以外 将该vo的库存数量加到表示90天以外的字段上
	 *      然后判断:     通过存货状态的主键 查看该存货是否为待检,
	 *                    如果是 将计划数量和计划辅数量加到待检字段上
	 *      最后判断:     通过待发和在途类型,判断该存货是待发还是在途
	 *                    如果是待发 将该存货的计划数量和计划辅数量加到待发字段上
	 *                    如果是在途 加该存货的计划数量和计划辅数量加到在途字段上 
	 *                                     
	 * @时间：2011-7-11下午01:01:57
	 * @param vos
	 * @return
	 */
	private ReportBaseVO[]  setVoByContion(ReportBaseVO[] vos) {
		if(vos==null || vos.length==0){
			return vos;
		}
		CircularlyAccessibleValueObject[][]voss =SplitBillVOs.getSplitVOs(vos,fields);
		if(voss==null || voss.length==0){
			return vos;
		}
		//new 开头的vo为重新组装放入界面的vo
		ReportBaseVO[] newVos=new ReportBaseVO[voss.length];
		int size=voss.length;
		for(int i=0;i<size;i++){
			ReportBaseVO newVo=null;
			int size1=voss[i].length;
		    for(int j=0;j<size1;j++){
		    	ReportBaseVO oldVo=(ReportBaseVO) voss[i][j];
		    	if(newVo==null){
		    	   newVo=(ReportBaseVO) oldVo.clone();
		        }
		    	//根据库龄设置存货数量
		    	setDayNum(newVo,oldVo);
		    	//设置存货待检数量
		    	setDaiJian(newVo,oldVo);    		    	
		    	//设置存货在途或待发数数量
		    	setZaiTuorDaiFa(newVo,oldVo);		    	
		    }
		    newVos[i]=newVo;
		}	
		return newVos;
	}
	/**
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 *        根据库龄设置存货数量
	 * @时间：2011-7-11下午03:12:30
	 * @param newVo
	 * @param oldVo
	 */
	private void setDayNum(ReportBaseVO newVo, ReportBaseVO oldVo) {
		//获得存货的库龄
    	Integer daynum=PuPubVO.getInteger_NullAs(oldVo.getAttributeValue(days), new Integer(-1));	
    	//获得主数量
    	UFDouble oldnum=PuPubVO.getUFDouble_NullAsZero(oldVo.getAttributeValue(num));
    	//获得辅数量
    	UFDouble boldnum=PuPubVO.getUFDouble_NullAsZero(oldVo.getAttributeValue(bnum));		    	
    	//设置库龄
    	setDayNum(newVo,daynum, oldnum,boldnum);		
	}
	/**
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 *      设置存货的待检数量
	 * @时间：2011-7-11下午03:10:43
	 * @param newVo
	 * @param oldVo
	 */
	private void setDaiJian(ReportBaseVO newVo,ReportBaseVO oldVo) {
		//获得存货状态主键
    	String pk_state=PuPubVO.getString_TrimZeroLenAsNull(oldVo.getAttributeValue(stockstate));		    	
    	//获得库存主数量
    	UFDouble cnum=PuPubVO.getUFDouble_NullAsZero(oldVo.getAttributeValue(num));
    	//获得库存辅数量
    	UFDouble bcnum=PuPubVO.getUFDouble_NullAsZero(oldVo.getAttributeValue(bnum));
    	if(stateid.equalsIgnoreCase(pk_state)){
    		setDaiJian(newVo,cnum,bcnum);
    	}			
	}
	/**
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 *       设置在途或待发数量
	 * @时间：2011-7-11下午03:03:06
	 * @param newVo
	 * @param oldVo
	 */
	private void setZaiTuorDaiFa(ReportBaseVO newVo, ReportBaseVO oldVo) {
		    //获得待发或在途的类型
		    Integer itype=PuPubVO.getInteger_NullAs(oldVo.getAttributeValue(type),new Integer(2));	
		    //获得计划主数量
    	    UFDouble planNum=PuPubVO.getUFDouble_NullAsZero(oldVo.getAttributeValue(numplan));
    	    //获得计划主数量
    	    UFDouble bplanNum=PuPubVO.getUFDouble_NullAsZero(oldVo.getAttributeValue(bnumplan));   	
    	    //类型为   0   表示待发   类型为  1 表示已发
		if(itype==0){
			//待发主数量 表示字段unit9
		    UFDouble oldnum=PuPubVO.getUFDouble_NullAsZero(newVo.getAttributeValue(unit+"10"));
		    newVo.setAttributeValue(unit+"10", oldnum.add(planNum));
		     //待发辅数量 表示字段unit9
		    UFDouble boldnum=PuPubVO.getUFDouble_NullAsZero(newVo.getAttributeValue(bunit+"10"));
		    newVo.setAttributeValue(bunit+"10", boldnum.add(bplanNum));			
		}else if(itype==1){
		    //在途主数量表示字段 unit7
			UFDouble oldnum=PuPubVO.getUFDouble_NullAsZero(newVo.getAttributeValue(unit+"7"));
			newVo.setAttributeValue(unit+"7", oldnum.add(planNum));
			//待发辅数量 表示字段unit7
			UFDouble boldnum=PuPubVO.getUFDouble_NullAsZero(newVo.getAttributeValue(bunit+"7"));
			newVo.setAttributeValue(bunit+"7", boldnum.add(bplanNum));	
		}	
	}
	/**
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 *       设置待检存货的主数量和辅数量
	 * @时间：2011-7-11下午02:51:22
	 * @param newVo
	 * @param planNum 库存主数量
	 * @param bplanNum 库存辅辅数量
	 */
	private void setDaiJian(ReportBaseVO newVo, UFDouble num,
			UFDouble bnum) {
		//获得原来的待检主数量 用unit7字段表示
		UFDouble oldnum=PuPubVO.getUFDouble_NullAsZero(newVo.getAttributeValue(unit+"8"));
		newVo.setAttributeValue(unit+"8",oldnum.add(num));
		//获得原来的待检辅数量 用unit7字段表示
		UFDouble boldnum=PuPubVO.getUFDouble_NullAsZero(newVo.getAttributeValue(bunit+"8"));
		newVo.setAttributeValue(bunit+"8",boldnum.add(bnum));		
	}
	/**
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 *      设置库龄 字段的值
	 *      
	 *      如果是30天以内 将该vo的库存数量加到表示30天以内的字段上
	 *                    如果是30-60天  将该vo的库存数量加到表示30-60天的字段上
	 *                    如果是60-90天  将该vo的库存数量加到表示60-90天的字段上
	 *                    如果是90天以外 将该vo的库存数量加到表示90天以外的字段上
	 *      
	 * @时间：2011-7-11下午02:07:43
	 * @param newVo
	 * @param daynum
	 */
	private void setDayNum(ReportBaseVO newVo, Integer daynum,UFDouble num,UFDouble bnum) {
		if(daynum<0){
			return;
		}
		if(daynum<=30){
			//unit1为30天以内库龄字段的主数量
			UFDouble oldnum=PuPubVO.getUFDouble_NullAsZero(newVo.getAttributeValue(unit+"1"));
			newVo.setAttributeValue(unit+"1",num.add(oldnum));	
			//bunit1为30天以内库龄字段的辅数量
			UFDouble boldnum=PuPubVO.getUFDouble_NullAsZero(newVo.getAttributeValue(bunit+"1"));
			newVo.setAttributeValue(bunit+"1",bnum.add(boldnum));			  
		}
		if(daynum>30 && daynum<=60){
			//unit1为30天以内库龄字段的主数量
			UFDouble oldnum=PuPubVO.getUFDouble_NullAsZero(newVo.getAttributeValue(unit+"2"));
			newVo.setAttributeValue(unit+"2",num.add(oldnum));	
			//bunit1为30天以内库龄字段的辅数量
			UFDouble boldnum=PuPubVO.getUFDouble_NullAsZero(newVo.getAttributeValue(bunit+"2"));
			newVo.setAttributeValue(bunit+"2",bnum.add(boldnum));					
		}
		if(daynum>60 && daynum<=90){
			//unit1为30天以内库龄字段的主数量
			UFDouble oldnum=PuPubVO.getUFDouble_NullAsZero(newVo.getAttributeValue(unit+"3"));
			newVo.setAttributeValue(unit+"3",num.add(oldnum));	
			//bunit1为30天以内库龄字段的辅数量
			UFDouble boldnum=PuPubVO.getUFDouble_NullAsZero(newVo.getAttributeValue(bunit+"3"));
			newVo.setAttributeValue(bunit+"3",bnum.add(boldnum));				
		}
		if(daynum>90){
			//unit1为30天以内库龄字段的主数量
			UFDouble oldnum=PuPubVO.getUFDouble_NullAsZero(newVo.getAttributeValue(unit+"4"));
			newVo.setAttributeValue(unit+"4",num.add(oldnum));	
			//bunit1为30天以内库龄字段的辅数量
			UFDouble boldnum=PuPubVO.getUFDouble_NullAsZero(newVo.getAttributeValue(bunit+"4"));
			newVo.setAttributeValue(bunit+"4",bnum.add(boldnum));			
		}		
	}
	
	/**
     * 
     * @作者：mlr
     * @说明：完达山物流项目  
     * 根据自定义条件得到查询报表数据的SQL 只查询库存
     * 
     * @时间：2011-5-10上午09:41:31
     * @param wheresql
     * @return
     */
	private String getQuerySQL(){		
	 return WDSWLReportSql.getQuerySQL(invclcode,new UFBoolean(true),pk_stordoc,new UFBoolean(true),new UFBoolean(true),new UFBoolean(false), new UFBoolean(false), new UFBoolean(false), ddatefrom, ddateto);
	}
	/**
     * 
     * @作者：mlr
     * @说明：完达山物流项目  
     * 根据自定义条件得到查询报表数据的SQL 只查询待发的
     * @时间：2011-5-10上午09:41:31
     * @param wheresql
     * @return
     */
	private String getQuerySQL1(){		
	  return WDSWLReportSql.getQuerySQL1(invclcode,pk_stordoc,new UFBoolean(true),new UFBoolean(true),new UFBoolean(false), new UFBoolean(false), new UFBoolean(false), ddatefrom, ddateto);
	}
	/**
     * 
     * @作者：mlr
     * @说明：完达山物流项目  
     * 根据自定义条件得到查询报表数据的SQL,只查询在途
     * @时间：2011-5-10上午09:41:31
     * @param wheresql
     * @return
     */
	private String getQuerySQL2(){	
	 return WDSWLReportSql.getQuerySQL2(invclcode,pk_stordoc,new UFBoolean(true),new UFBoolean(true),new UFBoolean(false), new UFBoolean(false), new UFBoolean(false), ddatefrom, ddateto);
	}
	 /**
	  * 
	  * @作者：mlr
	  * @说明：完达山物流项目:设置序号
	  * @时间：2011-5-11下午02:13:25
	  * @param vos
	  */
    public void setReportBaseVO(ReportBaseVO[] vos){
        if(vos!=null && vos.length>0){
            for(int i = 0 ;i < vos.length;i++){
                vos[i].setAttributeValue("lineno", (i+1));
            }
        }
    }
    /**
     * 合计
     */
    public void setTolal() throws Exception {
        SubtotalVO svo = new SubtotalVO();
        svo.setGroupFldCanNUll(true);// 分组列的数据是否可以为空。
        svo.setAsLeafRs(new boolean[] { true });// 分组列合并后是否作为末级节点记录。    
//        svo.setValueFlds(new String[]{"",""});// 求值列:
//        svo.setValueFldTypes(new int[]{1,2});// 求值列的类型:
        svo.setTotalDescOnFld("invclname");// ----合计---字段 ---- 所在列
        setSubtotalVO(svo);
        doSubTotal();
    }
	@Override
	public ReportBaseVO[] getReportVO(String wheresql) throws BusinessException {
	   
		return null;
	}
}
