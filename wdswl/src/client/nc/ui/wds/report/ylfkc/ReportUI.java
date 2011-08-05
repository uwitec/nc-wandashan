package nc.ui.wds.report.ylfkc;
import java.util.List;
import java.util.Map;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumnModel;
import nc.bs.logging.Logger;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.table.ColumnGroup;
import nc.ui.pub.beans.table.GroupableTableHeader;
import nc.ui.wl.pub.LongTimeTask;

import nc.ui.wl.pub.report.WDSReportBaseUI;
import nc.ui.wl.pub.report.WDSWLReportSql;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.vosplit.SplitBillVOs;
import nc.vo.wl.pub.CombinVO;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.report.IUFTypes;
import nc.vo.wl.pub.report.ReportBaseVO;
import nc.vo.wl.pub.report.SubtotalVO;
/**
 * 原料粉库库存报表
 * @author mlr
 */
public class ReportUI extends WDSReportBaseUI{
	private static final long serialVersionUID = 1L;
    //原料粉存货分类编码
	private String invclcode = "00";
    //主单位 数量前缀
    private String unit="unit"; 
    //辅单位 数量前缀
    private  String bunit="bunit";  
	  //库龄字段名字
    private  String  days="days";
    //用于设置库龄存货数量数对应的主数量字段名
    private  String num="num";
     //用于设置库龄存货数量对应的辅数量字段名
    private  String bnum="bnum";
    //库存状态 待检  对应的主键值
    private  String  stateid="1021S31000000009FS99";
    //库存状态对应的主键字段名字
    private  String  stockstate="ss_pk"; 
    //待发和在途对应的类型字段名
    private  String type="type"; 
    //计划主数量字段值名字
    private  String numplan="plannum";
    //计划辅数量字段值名字
    private  String bnumplan="bplannum";
	//存货类型字段   0表示常用     1表示不常用
	private static String  invtype="invtype";
    //将要合并的求值的类  
    private  int[] types={IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD
    	                  ,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD };
    //将要合并的求值子段
    private  String[] combinFields={"unit1","unit2","unit3","unit4","unit5","unit6","unit7","unit8",
    	                            "bunit1","bunit2","bunit3","bunit4","bunit5","bunit6","bunit7","bunit8"  };
	@Override
	public String _getModelCode() {	
		return WdsWlPubConst.REPORT17;
	}
	public ReportUI() {
		super();
		//查询动态列插入位置
		setLocation1(2);
		getReportBase().getBillTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION); 
		//去除字段自动排序的功能
		getReportBase().getBillTable().removeSortListener();
	    setColumn();
	}
	 /**
     * 基本列合并
     */
    private void setColumn() {
        //表体栏目分组设置
        UITable cardTable = getReportBase().getBillTable();
        GroupableTableHeader cardHeader = (GroupableTableHeader) cardTable.getTableHeader();
        TableColumnModel cardTcm = cardTable.getColumnModel();
        //用来判断是否按货位 批次 展开  如果按货位批次展开 i=2 只有一个 i=1 都没有 0       
        int i=0;
        if(iscargdoc!=null&&iscargdoc.booleanValue()==true){
     	   i=i+1;
        }
        if(isvbanchcode!=null&&isvbanchcode.booleanValue()==true){
           i=i+1;	
        }  
        if(isstordoc!=null&&isstordoc.booleanValue()==true){
            i=i+1;	
        }  
        ColumnGroup zgroup=new ColumnGroup("货龄");
        ColumnGroup a1=new ColumnGroup("30天以内");
        a1.add(cardTcm.getColumn(2+i));
        a1.add(cardTcm.getColumn(3+i));
        zgroup.add(a1);
        ColumnGroup a2=new ColumnGroup("30-60天");
        a2.add(cardTcm.getColumn(4+i));
        a2.add(cardTcm.getColumn(5+i));
        zgroup.add(a2);
        ColumnGroup a3=new ColumnGroup("60-90天");
        a3.add(cardTcm.getColumn(6+i));
        a3.add(cardTcm.getColumn(7+i));
        zgroup.add(a3);
        
        ColumnGroup a4=new ColumnGroup("90-120天");
        a4.add(cardTcm.getColumn(8+i));
        a4.add(cardTcm.getColumn(9+i));
        zgroup.add(a4);  
        
        ColumnGroup zgroup2=new ColumnGroup("120-180天");
        zgroup2.add(cardTcm.getColumn(10+i));
        zgroup2.add(cardTcm.getColumn(11+i));       
        zgroup.add(zgroup2);  
        
        ColumnGroup zgroup3=new ColumnGroup("大于180天");
        zgroup3.add(cardTcm.getColumn(12+i));
        zgroup3.add(cardTcm.getColumn(13+i));
        zgroup.add(zgroup3);  
        cardHeader.addColumnGroup(zgroup);
                       
        ColumnGroup zgroup33=new ColumnGroup("合计");
        zgroup33.add(cardTcm.getColumn(14+i));
        zgroup33.add(cardTcm.getColumn(15+i));
        cardHeader.addColumnGroup(zgroup33);
            
        ColumnGroup a22=new ColumnGroup("待发");
        a22.add(cardTcm.getColumn(16+i));
        a22.add(cardTcm.getColumn(17+i));    
        cardHeader.addColumnGroup(a22);
              
        getReportBase().getBillModel().updateValue();
    }
	@Override
	public void setUIAfterLoadTemplate() {
		
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
		  	    super.onQuery();
            	//得到自定义查询条件
                //得到查询结果
            	 List<ReportBaseVO[]> list=null;
            	//设置基本列合并
            	 setColumn();
            	//根据是否货位展开  是否批次展开 选择生成报表vo的查询语句             	            
             	list=getReportVO(new String[]{getQuerySQL(),getQuerySQL1()});        	
                 ReportBaseVO[] vos1= list.get(1);
                 setVbachCode(vos1);
                 ReportBaseVO[] vos2=list.get(0);   
                 if(vos1 != null&&vos1.length>0 || vos2!=null&&vos2.length>0 ){                	
                 	//三个全选
                     //存货    仓库  货位  批次
                 	if(isstordoc.booleanValue()==true && iscargdoc.booleanValue()==true && isvbanchcode.booleanValue()==true){
                 		ReportBaseVO[]newVos=setVoByContion(vos1,fields0);
      				    ReportBaseVO[]newVos1=setVoByContion(vos2,fields0);
      				    ReportBaseVO[] combins=(ReportBaseVO[])CombinVO.combinVoByFields(newVos,newVos1,fields0,types,combinFields);
      				  
      				   
      				    setReportBaseVO(combins);
      					setBodyVO(combins);	
      					
                 	}
                 	 //三个选两个
                     //存货   仓库  货位
                     //存货   仓库  批次
                     //存货   货位  批次
                 	if(isstordoc.booleanValue()==true && iscargdoc.booleanValue()==true&&isvbanchcode.booleanValue()==false){
                 		ReportBaseVO[]newVos=setVoByContion(vos1,fields1);
      				    ReportBaseVO[]newVos1=setVoByContion(vos2,fields1);
      				    ReportBaseVO[] combins=(ReportBaseVO[])CombinVO.combinVoByFields(newVos,newVos1,fields1,types,combinFields);
      				 
      				    setReportBaseVO(combins);
      					setBodyVO(combins);	
      					
                 	}
                     if(isstordoc.booleanValue()==true && isvbanchcode.booleanValue()==true&&iscargdoc.booleanValue()==false){
                     	ReportBaseVO[]newVos=setVoByContion(vos1,fields2);
      				    ReportBaseVO[]newVos1=setVoByContion(vos2,fields2);
      				    ReportBaseVO[] combins=(ReportBaseVO[])CombinVO.combinVoByFields(newVos,newVos1,fields2,types,combinFields);
      				  
      				    setReportBaseVO(combins);
      					setBodyVO(combins);	
      					
                 	}
                 	if(iscargdoc.booleanValue()==true && isvbanchcode.booleanValue()==true&&isstordoc.booleanValue()==false){
                 		ReportBaseVO[]newVos=setVoByContion(vos1,fields3);
      				    ReportBaseVO[]newVos1=setVoByContion(vos2,fields3);
      				    ReportBaseVO[] combins=(ReportBaseVO[])CombinVO.combinVoByFields(newVos,newVos1,fields3,types,combinFields);
      				
      				    setReportBaseVO(combins);
      					setBodyVO(combins);	
      					
                 	}
                 	//三个只选一个
                     //三种情况
                     if(isstordoc.booleanValue()==true && iscargdoc.booleanValue()==false&&isvbanchcode.booleanValue()==false){
                     	ReportBaseVO[]newVos=setVoByContion(vos1,fields4);
      				    ReportBaseVO[]newVos1=setVoByContion(vos2,fields4);
      				    ReportBaseVO[] combins=(ReportBaseVO[])CombinVO.combinVoByFields(newVos,newVos1,fields4,types,combinFields);
      				  
      				    setReportBaseVO(combins);
      					setBodyVO(combins);	
      					
                 	}
                     if(iscargdoc.booleanValue()==true&&isstordoc.booleanValue()==false && isvbanchcode.booleanValue()==false){
                     	ReportBaseVO[]newVos=setVoByContion(vos1,fields5);
      				    ReportBaseVO[]newVos1=setVoByContion(vos2,fields5);
      				    ReportBaseVO[] combins=(ReportBaseVO[])CombinVO.combinVoByFields(newVos,newVos1,fields5,types,combinFields);
      				
      				    setReportBaseVO(combins);
      					setBodyVO(combins);	
      					
                 	}
                 	if(isvbanchcode.booleanValue()==true&& iscargdoc.booleanValue()==false &&isstordoc.booleanValue()==false){
                 		ReportBaseVO[]newVos=setVoByContion(vos1,fields6);
      				    ReportBaseVO[]newVos1=setVoByContion(vos2,fields6);
      				    ReportBaseVO[] combins=(ReportBaseVO[])CombinVO.combinVoByFields(newVos,newVos1,fields6,types,combinFields);
      			
      				    setReportBaseVO(combins);
      					setBodyVO(combins);	
      					  
                 	}
                 	
                 	//三个都不选
                     if(iscargdoc.booleanValue()==false && isvbanchcode.booleanValue()==false&&isstordoc.booleanValue()==false){
                     	ReportBaseVO[]newVos=setVoByContion(vos1,fields7);
      				    ReportBaseVO[]newVos1=setVoByContion(vos2,fields7);
      				    ReportBaseVO[] combins=(ReportBaseVO[])CombinVO.combinVoByFields(newVos,newVos1,fields7,types,combinFields);
      			
      				    setReportBaseVO(combins);
      					setBodyVO(combins);	
      					 
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
     * 根据自定义条件得到查询报表数据的SQL 只查询库存的
     * @时间：2011-5-10上午09:41:31
     * @param wheresql
     * @return
     */
	private String getQuerySQL() {		
		return WDSWLReportSql.getQuerySQL(invclcode,new UFBoolean(true), pk_stordoc,null,new UFBoolean(false),new UFBoolean(false),isstordoc, iscargdoc, isvbanchcode, ddatefrom, ddateto);
	}
	/**
     * 
     * @作者：mlr
     * @说明：完达山物流项目  
     * 根据自定义条件得到查询报表数据的SQL 只查询待发的运单
     * @时间：2011-5-10上午09:41:31
     * @param wheresql
     * @return
     */
	private String getQuerySQL1() {		
	   return WDSWLReportSql.getQuerySQL1(invclcode, pk_stordoc,null,new UFBoolean(false),new UFBoolean(false),isstordoc, iscargdoc, isvbanchcode, ddatefrom, ddateto);
	}
	/**
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 *       对没有批次字段的 vo 设置默认批次
	 * @时间：2011-7-14下午03:30:19
	 * @param vos1
	 */
	private void setVbachCode(ReportBaseVO[] vos) {
		
		if(vos==null && vos.length==0){
			return;
		}
		int size=vos.length;
		for(int i=0;i<size;i++){
		 vos[i].setAttributeValue(banchcode, "");
		}	
		
	}
	/**
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 *      按照报表需求,加工初次查询形成的报表vo
	 *      加工条件：
	 *      首先 按 仓库 存货 进行分组,然后对每组vo进行合并,将每组vo合并按要求合并成一个vo
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
	private ReportBaseVO[]  setVoByContion(ReportBaseVO[] vos,String[] fields) {
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
		        //setDaiJian(newVo,oldVo);    		    	
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
			//待发主数量 表示字段unit8
		    UFDouble oldnum=PuPubVO.getUFDouble_NullAsZero(newVo.getAttributeValue(unit+"8"));
		    newVo.setAttributeValue(unit+"8", oldnum.add(planNum));
		     //待发辅数量 表示字段unit8
		    UFDouble boldnum=PuPubVO.getUFDouble_NullAsZero(newVo.getAttributeValue(bunit+"8"));
		    newVo.setAttributeValue(bunit+"8", boldnum.add(bplanNum));			
		}else if(itype==1){
//		    //在途主数量表示字段 unit8
//			//待发主数量 表示字段unit9
//			UFDouble oldnum=PuPubVO.getUFDouble_NullAsZero(newVo.getAttributeValue(unit+"8"));
//			newVo.setAttributeValue(unit+"8", oldnum.add(planNum));
//			//待发辅数量 表示字段unit9
//			UFDouble boldnum=PuPubVO.getUFDouble_NullAsZero(newVo.getAttributeValue(bunit+"8"));
//			newVo.setAttributeValue(bunit+"8", boldnum.add(bplanNum));	
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
		UFDouble oldnum=PuPubVO.getUFDouble_NullAsZero(newVo.getAttributeValue(unit+"7"));
		newVo.setAttributeValue(unit+"7",oldnum.add(num));
		//获得原来的待检辅数量 用unit7字段表示
		UFDouble boldnum=PuPubVO.getUFDouble_NullAsZero(newVo.getAttributeValue(bunit+"7"));
		newVo.setAttributeValue(bunit+"7",boldnum.add(bnum));		
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
		if(daynum>90 && daynum<=120){
			//unit1为30天以内库龄字段的主数量
			UFDouble oldnum=PuPubVO.getUFDouble_NullAsZero(newVo.getAttributeValue(unit+"4"));
			newVo.setAttributeValue(unit+"4",num.add(oldnum));	
			//bunit1为30天以内库龄字段的辅数量
			UFDouble boldnum=PuPubVO.getUFDouble_NullAsZero(newVo.getAttributeValue(bunit+"4"));
			newVo.setAttributeValue(bunit+"4",bnum.add(boldnum));			
		}	
		if(daynum>120 && daynum<=180){
			//unit1为30天以内库龄字段的主数量
			UFDouble oldnum=PuPubVO.getUFDouble_NullAsZero(newVo.getAttributeValue(unit+"5"));
			newVo.setAttributeValue(unit+"5",num.add(oldnum));	
			//bunit1为30天以内库龄字段的辅数量
			UFDouble boldnum=PuPubVO.getUFDouble_NullAsZero(newVo.getAttributeValue(bunit+"5"));
			newVo.setAttributeValue(bunit+"5",bnum.add(boldnum));			
		}		
		if(daynum>180){
			//unit1为30天以内库龄字段的主数量
			UFDouble oldnum=PuPubVO.getUFDouble_NullAsZero(newVo.getAttributeValue(unit+"6"));
			newVo.setAttributeValue(unit+"6",num.add(oldnum));	
			//bunit1为30天以内库龄字段的辅数量
			UFDouble boldnum=PuPubVO.getUFDouble_NullAsZero(newVo.getAttributeValue(bunit+"6"));
			newVo.setAttributeValue(bunit+"6",bnum.add(boldnum));			
		}		
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
        svo.setValueFlds(combinFields);// 求值列:
        svo.setValueFldTypes(types);// 求值列的类型:
        svo.setTotalDescOnFld("invclname");// ----合计---字段 ---- 所在列
        setSubtotalVO(svo);
        doSubTotal();
    }
	@Override
	public ReportBaseVO[] getReportVO(String wheresql) throws BusinessException {
	   
		return null;
	}
	@Override
	public Map getNewItems() throws Exception {		
		return null;
	}
	@Override
	public void initReportUI() {		
		
	}
}
