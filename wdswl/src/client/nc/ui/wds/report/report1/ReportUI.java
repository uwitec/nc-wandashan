package nc.ui.wds.report.report1;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.table.TableColumnModel;

import nc.bd.accperiod.AccountCalendar;
import nc.bs.logging.Logger;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.table.ColumnGroup;
import nc.ui.pub.beans.table.GroupableTableHeader;
import nc.ui.pub.bill.IBillItem;
import nc.ui.pub.report.ReportItem;
import nc.ui.scm.util.ObjectUtils;
import nc.ui.trade.report.query.QueryDLG;
import nc.ui.wl.pub.CombinVO;
import nc.ui.wl.pub.LongTimeTask;
import nc.ui.wl.pub.report.ReportPubTool;
import nc.ui.wl.pub.report.WDSWLReportSql;
import nc.ui.wl.pub.report.ZmReportBaseUI;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.report.ReportBaseVO;
/** 
 * 各仓产品库存 产品明细表
 * @author mlr
 */
public class ReportUI extends ZmReportBaseUI{
	private static final long serialVersionUID = -416464210087347398L;
	//现存量合并维度
	private static String[] combinConds={"invtype","pk_stordoc","pk_invcl","pk_storestate","pk_invbasdoc","days","creadate"};
	//数据合并字段
	private static String[] combinFields={"num","bnum"};
	//现存量数据追加维度
	private static String[] combinConds1={"invtype","pk_stordoc","pk_invcl","pk_invbasdoc"};
	//待检状态主键
	private static String pk_daijian="1021S31000000009FS99";
	//在途合并维度
	private static String[] zaiTuCombinFields={"invtype","pk_stordoc","pk_invcl","pk_invbasdoc"};
	//销售待发 合并维度
	private static String[] soDaiFa={"billcode","invtype","pk_stordoc","pk_invcl","b_pk","pk_invbasdoc"};

	
	private static int[][] hdays= {{0,30},{31,60},{61,90},{91,120},{121,150},{151,180},
	                               {181,210},{211,240},{241,270},{271,300},{301,330},{331,365}};	
	
	private AccountCalendar ac=AccountCalendar.getInstance();
	private ClientEnvironment ce=ClientEnvironment.getInstance();
	//合并追加的模式计算报表数据
	
	//增加设置物品类型的算法
	//增加本年和去年的无效库存算法
	//设置待检的算法
	//上述一个查询
	
	 //转分仓在途数量的算法	
	 //上述一个查询
	
	
	//增加正常销售算法  计算本月前正常待发 本月正常待发 本月前补货待发 本月前补货待发
	//这个一个查询
	
	//增加虚拟总仓转分仓仓库存量
	//这个一个查询
	
	
	//增加各个合计的计算算法
	
	@Override
	public Map getNewItems() throws Exception {
		HashMap<String, Object> map=new HashMap<String, Object>();
		//设置动态列插入位置
	    map.put("location", new Integer(7));
		ReportItem[] its=new ReportItem[60];
		String startName="num";
		String startName1="bnum";
		int size=its.length/2;
		List<ReportItem> list=new ArrayList<ReportItem>();
		for(int i=0;i<size-1;i++){
		  ReportItem it=ReportPubTool.getItem(startName+(i+1),"主数量",IBillItem.DECIMAL,i, 80);
		  ReportItem it1=ReportPubTool.getItem(startName1+(i+1),"辅数量",IBillItem.DECIMAL,i, 80);
		  list.add(it);
		  list.add(it1);
		}
		ReportItem[] rets=new ReportItem[list.size()]; 
		for(int i=0;i<list.size();i++){
		   rets[i]=list.get(i);
		}
		
		map.put("items",rets);	
		return map;
	}

	@Override
	public void onQuery() {
		getQueryDlg().showModal();
	     if (getQueryDlg().getResult() == UIDialog.ID_OK) {		  
	      try {
	    	 //清空表体数据
	    	 clearBody();	    	         	
        	//设置动态列
        	setDynamicColumn1();
        	//设置基本列合并
        	 setColumn();
        	 //设置vo
			 List<ReportBaseVO[]> list=getReportVO(new String[]{
					  getQuerySQL(getQueryConditon()),
					  getQuerySQL1(getQueryConditon()),
					  getQuerySQL2(getQueryConditon()),
					  getQuerySQL3(getQueryConditon())
				      });	 
			 ReportBaseVO[] vos=list.get(0);			
			ReportBaseVO[] cals=cal1(vos);//第一个查询的运算 计算货龄  和 待检
			ReportBaseVO[] vos1=list.get(1);
			ReportBaseVO[] cals1=cal2(vos1);//第二个查询的运算 计算转分仓在途
			ReportBaseVO[] zvos=CombinVO.addByContion2(cals, cals1, combinConds1, null);//将数据追加到一起
			ReportBaseVO[] vos2=list.get(2);
			ReportBaseVO[] cals2=cal3(vos2,25,27);//第三个查询的运算  计算本月销售
			ReportBaseVO[] zvos1=CombinVO.addByContion2(zvos, cals2, combinConds1, null);//将数据追加到一起
			ReportBaseVO[] vos3=list.get(3);
			ReportBaseVO[] cals3=cal4(vos3);//第四个查询的运算  计算本月前销售		
			ReportBaseVO[] zvos2=CombinVO.addByContion2(zvos1, cals3, combinConds1, null);//将数据追加到一起

			
			
			
			
             if(cals != null){                	
				super.updateBodyDigits();
				setBodyVO(cals);				
	            setTolal();	                
             }  
	  	} catch (BusinessException e) {
			e.printStackTrace();
		} catch (Exception e) {			
			e.printStackTrace();
		} 
	   }  
	}
	private ReportBaseVO[] cal4(ReportBaseVO[] vos3) throws Exception {
		return cal3(vos3,24,26);
	}

	/**
	 *计算 本月销售正常待发        本月前销售正常待发
	 *     本月补货待发(B字开头)   本月前销售补货待发
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 * @时间：2011-12-5下午01:19:24
	 * @param vos2
	 * @param loc 待发字段位置
	 * @return loc1 补货字段位置
	 * @throws Exception 
	 */
	private ReportBaseVO[] cal3(ReportBaseVO[] vos2,int loc,int loc1) throws Exception {
		if(vos2==null || vos2.length==0){
			return null;		
		}
		//构造左侧基础数据
		ReportBaseVO[] jichus=(ReportBaseVO[]) CombinVO.combinData(vos2, combinConds1, combinFields, ReportBaseVO.class);
		//按销售订单 表体id 进行数据合并 合并销售出库出库数量
        ReportBaseVO[] cvos=(ReportBaseVO[]) CombinVO.combinData((ReportBaseVO[])ObjectUtils.serializableClone(vos2), soDaiFa, combinFields, ReportBaseVO.class);       
        calSoDaiFaBas(cvos);//计算销售订单待发
        calSoDaiFa(jichus,cvos,loc);//计算销售订单待发去除补货
        calSoBuHuo(jichus,cvos,loc1);//计算销售订单补货待发		
        calType(jichus);
		return jichus;
	}
   /**
    * 计算出销售待发
    * @作者：mlr
    * @说明：完达山物流项目 
    * @时间：2011-12-5下午02:41:04
    * @param cvos
    * @return
    */
   private void calSoDaiFaBas(ReportBaseVO[] cvos) {
	   if(cvos==null || cvos.length==0)
		   return ;
	   for(int i=0;i<cvos.length;i++){
		   UFDouble nnumber=PuPubVO.getUFDouble_NullAsZero(cvos[i].getAttributeValue("nnumber"));//订单数量
		   UFDouble npacknumber=PuPubVO.getUFDouble_NullAsZero(cvos[i].getAttributeValue("npacknumber"));//订单辅数量
		   UFDouble num=PuPubVO.getUFDouble_NullAsZero(cvos[i].getAttributeValue("num"));//销售出库数量
		   UFDouble bnum=PuPubVO.getUFDouble_NullAsZero(cvos[i].getAttributeValue("bnum"));//销售出库辅数量
		   cvos[i].setAttributeValue("num", nnumber.sub(num)); 
		   cvos[i].setAttributeValue("bnum", npacknumber.sub(bnum));
	   }
	}

/**
    * 计算销售订单 补货待发
    * @作者  mlr
    * @说明：完达山物流项目 
    * @时间：2011-12-5下午01:55:09
    * @param jichus
    * @param cvos
 * @param loc1 
 * @throws Exception 
    */
	private void calSoBuHuo(ReportBaseVO[] jichus, ReportBaseVO[] cvos1, int loc1) throws Exception {
	     if(cvos1==null || cvos1.length==0)
	        	return ;
	       ReportBaseVO[] nvos=fielterSoBuHuo(cvos1);
	       ReportBaseVO[] newvos=(ReportBaseVO[]) CombinVO.combinData(nvos, combinConds1, combinFields, ReportBaseVO.class);
	       CombinVO.addByContion1(jichus, newvos, combinConds1, loc1+"");       		
	}
    /**
     * 计算销售订单 待发
     * @作者：mlr
     * @说明：完达山物流项目 
     * @时间：2011-12-5下午01:55:44
     * @param jichus
     * @param loc 
     * @param cvos
     * @throws Exception 
     */
	private void calSoDaiFa(ReportBaseVO[] jichus, ReportBaseVO[] cvos1, int loc) throws Exception {
        if(cvos1==null || cvos1.length==0)
        	return ;
       ReportBaseVO[] nvos=fielterSoDaiFa(cvos1);
       ReportBaseVO[] newvos=(ReportBaseVO[]) CombinVO.combinData(nvos, combinConds1, combinFields, ReportBaseVO.class);
       CombinVO.addByContion1(jichus, newvos, combinConds1, loc+"");       
	}
	/**
	 * 过滤出销售待发
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 * @时间：2011-12-5下午02:25:02
	 * @param vos
	 * @return
	 * @throws Exception 
	 */
	private ReportBaseVO[] fielterSoDaiFa(ReportBaseVO[] cvos1) throws Exception{
		  if(cvos1==null || cvos1.length==0)
	        	return null;
	        ReportBaseVO[] cvos=(ReportBaseVO[]) ObjectUtils.serializableClone(cvos1);
	        List<ReportBaseVO> list=new ArrayList<ReportBaseVO>();//记录正常待发
	        for(int i=0;i<cvos.length;i++){
	            String billcode=PuPubVO.getString_TrimZeroLenAsNull(cvos[i].getAttributeValue("billcode"));
	            if(billcode==null)
	            	continue;
	            if(!billcode.startsWith("B")){
	            	list.add(cvos[i]);
	            }
	        }	
		return list.toArray(new ReportBaseVO[0]);
	}
	/**
	 * 过滤出销售补货待发
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 * @时间：2011-12-5下午02:25:02
	 * @param vos
	 * @return
	 * @throws Exception 
	 */
	private ReportBaseVO[] fielterSoBuHuo(ReportBaseVO[] cvos1) throws Exception{
		  if(cvos1==null || cvos1.length==0)
	        	return null;
	        ReportBaseVO[] cvos=(ReportBaseVO[]) ObjectUtils.serializableClone(cvos1);
	        List<ReportBaseVO> list=new ArrayList<ReportBaseVO>();//记录正常待发
	        for(int i=0;i<cvos.length;i++){
	            String billcode=PuPubVO.getString_TrimZeroLenAsNull(cvos[i].getAttributeValue("billcode"));
	            if(billcode==null)
	            	continue;
	            if(billcode.startsWith("B")){
	            	list.add(cvos[i]);
	            }
	        }	
		return list.toArray(new ReportBaseVO[0]);
	}

	/**
	 * 计算在途数据
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 * @时间：2011-12-4下午08:05:29
	 * @param vos1
	 * @return
	 * @throws Exception 
	 */
   private ReportBaseVO[] cal2(ReportBaseVO[] vos) throws Exception {
	   if(vos==null || vos.length==0)
        	 return null;       
         //按最小维度进行数据合并
         ReportBaseVO[] cvos=(ReportBaseVO[]) CombinVO.combinData((ReportBaseVO[])ObjectUtils.serializableClone(vos), zaiTuCombinFields, combinFields, ReportBaseVO.class);
         calZaiTu(cvos);//计算库龄
         calType(cvos);//计算类型
         return cvos;		
	}
   /**
    * 计算在途数据
    * @作者：mlr
    * @说明：完达山物流项目 
    * @时间：2011-12-4下午08:17:00
    * @param cvos
    */
    private void calZaiTu(ReportBaseVO[] cvos) {
		int loc=21;//记录在途数据的字段位置
	   if(cvos==null|| cvos.length==0)
		   return;
	   for(int i=0;i<cvos.length;i++){
		   UFDouble num=PuPubVO.getUFDouble_NullAsZero(cvos[i].getAttributeValue("num"));
		   UFDouble bnum=PuPubVO.getUFDouble_NullAsZero(cvos[i].getAttributeValue("bnum"));
		   cvos[i].setAttributeValue("num"+loc, num);
		   cvos[i].setAttributeValue("bnum"+loc, bnum);
	   }
	}

/**
    * 增加设置物品类型的算法
	  增加本年和去年的无效库存算法
	  设置待检的算法
	  上述一个查询
    * @作者：mlr
    * @说明：完达山物流项目 
    * @时间：2011-12-4下午07:31:58
    * @param vos
    * @throws Exception
    */
	private ReportBaseVO[] cal1(ReportBaseVO[] vos) throws Exception {
		   if(vos==null || vos.length==0)
          	 return null;       
           //按最小维度进行数据合并
           ReportBaseVO[] cvos=(ReportBaseVO[]) CombinVO.combinData(vos, combinConds, combinFields, ReportBaseVO.class);
           //构建左侧基础数据
           ReportBaseVO[]  jichus=(ReportBaseVO[]) CombinVO.combinData((ReportBaseVO[])ObjectUtils.serializableClone(cvos), 
          		 combinConds1, combinFields, ReportBaseVO.class);
           calDays(jichus,cvos);//计算库龄
           calType(jichus);//计算物品类型     
           calYear(jichus,cvos);//计算去年和前年的大日期存货
           calDaiJian(jichus,cvos);//计算待检数量
          return jichus;		
	}

	/**
	 * 计算去年 和 前年的大日期物品
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 * @时间：2011-12-3下午03:26:36
	 * @param jichus
	 * @param cvos
	 * @throws Exception 
	 */
	private void calYear(ReportBaseVO[] jichus, ReportBaseVO[] cvos) throws Exception {
		if(cvos==null || cvos.length==0)
			return;
		ReportBaseVO[] vos=(ReportBaseVO[]) ObjectUtils.serializableClone(cvos);
		List<ReportBaseVO> qus=new ArrayList<ReportBaseVO>();//存放去年大日期
		List<ReportBaseVO> qis=new ArrayList<ReportBaseVO>();//存放前年大日期
		int year=ce.getDate().getYear()-1;//去年
		int year1=year-1;//前年
		int loc=14;//报表去年 和 前年 字段的位置
		for(int i=0;i<vos.length;i++){
			String  cdate=PuPubVO.getString_TrimZeroLenAsNull(vos[i].getAttributeValue("creadate"));
			if(cdate==null)
				continue;
			if(cdate.startsWith(year+"")){
				vos[i].setAttributeValue("num"+loc, vos[i].getAttributeValue("num"));
				vos[i].setAttributeValue("bnum"+loc,vos[i].getAttributeValue("bnum"));
				qus.add(vos[i]);
			}
			if(cdate.startsWith(year1+"")){
				vos[i].setAttributeValue("num"+(loc+1), vos[i].getAttributeValue("num"));
				vos[i].setAttributeValue("bnum"+(loc+1),vos[i].getAttributeValue("bnum"));
				qis.add(vos[i]);
			}			
		}
		if(qus.size()!=0){
		    ReportBaseVO[] nevos=(ReportBaseVO[]) CombinVO.combinData(qus.toArray(new ReportBaseVO[0]), combinConds1, combinFields, ReportBaseVO.class);
			CombinVO.addByContion1(jichus,nevos,combinConds1, null);
		}	
		if(qis.size()!=0){
		    ReportBaseVO[] nevos=(ReportBaseVO[]) CombinVO.combinData(qis.toArray(new ReportBaseVO[0]), combinConds1, combinFields, ReportBaseVO.class);
			CombinVO.addByContion1(jichus, qis.toArray(new ReportBaseVO[0]), combinConds1, null);
		}		
	}
	private void calDaiJian(ReportBaseVO[] jichus, ReportBaseVO[] cvos1)throws Exception{
	   ReportBaseVO[] cvos=(ReportBaseVO[]) ObjectUtils.serializableClone(cvos1);
	   if(cvos==null || cvos.length==0)
		   return;
	   
	   List<ReportBaseVO> list=new ArrayList<ReportBaseVO>();//记录待检
	   int loc =22;//记录待检的位置
	   for(int i=0;i<cvos.length;i++){
		   String pk=PuPubVO.getString_TrimZeroLenAsNull(cvos[i].getAttributeValue("pk_storestate"));//获取存货状态
	       if(pk==null){
	    	  continue;
	       }
	       if(pk.equals(pk_daijian)){
	    	  list.add(cvos[i]); 
	       }
	   }
	   if(list.size()==0)
		   return;
	  ReportBaseVO[] nevos=(ReportBaseVO[]) CombinVO.combinData(list.toArray(new ReportBaseVO[0]), combinConds1, combinFields, ReportBaseVO.class);
      CombinVO.addByContion1(jichus, nevos, combinConds1, loc+"");
	}

	/**
	 * 计算库龄 将每个月计算得出的库存数量 追加到 左侧基础数据中 最终
	 * 构建计算了库龄的报表数据
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 * @时间：2011-12-2下午04:00:05
	 * @param jichus
	 * @param cvos
	 * @throws Exception 
	 */
	private void calDays(ReportBaseVO[] jichus, ReportBaseVO[] cvos) throws Exception {	
		calDay(jichus,cvos);
		calDay1(jichus,cvos);		
	}
    /**
     * 计算0-90天库存中的每月的物品现存量
     * @作者：mlr
     * @说明：完达山物流项目 
     * @时间：2011-12-2下午04:05:13
     * @param jichus
     * @param cvos
     * @throws Exception 
     */
	private void calDay1(ReportBaseVO[] jichus, ReportBaseVO[] cvos) throws Exception {
		for(int i=0;i<3;i++){
			ReportBaseVO[] vos=filterByDays(cvos,hdays[i][0],hdays[i][1]);
			if(vos==null || vos.length==0)
				continue;
			ReportBaseVO[] nevos=(ReportBaseVO[]) CombinVO.combinData(vos, combinConds1, combinFields, ReportBaseVO.class);
			CombinVO.addByContion1(jichus, nevos, combinConds1, (i+1)+"");
		}		
	}
	private ReportBaseVO[] filterByDays(ReportBaseVO[] cvos,int satrtDays,int endDays) throws Exception{
		ReportBaseVO[] nvos=(ReportBaseVO[]) ObjectUtils.serializableClone(cvos);
		if(nvos==null || nvos.length==0){
			return null;
		}
		List<ReportBaseVO> list=new ArrayList<ReportBaseVO>();
		for(int i=0;i<nvos.length;i++){
		   int days=PuPubVO.getInteger_NullAs(nvos[i].getAttributeValue("days"), -1);
		   if(days>=satrtDays && days<=endDays){
			   list.add(nvos[i]);
		   }
		}
		if(list==null || list.size()==0)
			return null;
		return list.toArray(new ReportBaseVO[0]);
	}
   /**
    * 计算90-365天库存中的每月的物品现存量
    * @作者：mlr
    * @说明：完达山物流项目 
    * @时间：2011-12-2下午04:05:58
    * @param jichus
    * @param cvos
 * @throws Exception 
    */
	private void calDay(ReportBaseVO[] jichus, ReportBaseVO[] cvos) throws Exception {
		for(int i=3;i<12;i++){
			ReportBaseVO[] vos=filterByDays(cvos,hdays[i][0],hdays[i][1]);
			if(vos==null || vos.length==0)
				continue;
			ReportBaseVO[] nevos=(ReportBaseVO[]) CombinVO.combinData(vos, combinConds1, combinFields, ReportBaseVO.class);
			CombinVO.addByContion1(jichus, nevos, combinConds1, (i+2)+"");
			
		}
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
	/**
	 * 获得查询现存量的sql
	 * 主要用于 计算 货龄 待检
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 * @时间：2011-12-4下午07:56:28
	 * @param whereSql
	 * @return
	 * @throws Exception
	 */
	public String getQuerySQL(String whereSql) throws Exception {
		return WDSWLReportSql.getStoreSql(whereSql);
	}
	/**
	 * 获得查询 发运订单的sql
	 * 主要用于计算 转分仓在途
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 * @时间：2011-12-4下午07:57:34
	 * @param whereSql
	 * @return
	 * @throws Exception
	 */
	public String getQuerySQL1(String whereSql) throws Exception {
		QueryDLG querylg= getQueryDlg();//获取查询对话框		
		ConditionVO[] vos=(ConditionVO[]) ObjectUtils.serializableClone(querylg.getConditionVO());//获取已被用户填写的查询条件
		ConditionVO[] vos1=filterQuery(vos);
		if(vos1==null ||vos1.length==0){
			whereSql=null;
		}else{
			for(int i=0;i<vos1.length;i++){
				if(vos1[i].getFieldCode().equals("h.pk_customize1")){
					vos1[i].setFieldCode("h.pk_inwhouse");
				}
				if(vos1[i].getFieldCode().equals("h.pk_invmandoc")){
					vos1[i].setFieldCode("b.pk_invmandoc"); 
				}
			}
			whereSql=querylg.getWhereSQL(vos1);
		}	
		return WDSWLReportSql.getDmOrderSql(whereSql);
	}
    /**
     * 查询销售订单 本月  待发
     * @作者：mlr
     * @说明：完达山物流项目 
     * @时间：2011-12-5下午03:02:25
     * @param whereSql
     * @return
     * @throws Exception
     */
	public String getQuerySQL2(String whereSql) throws Exception {
		QueryDLG querylg= getQueryDlg();//获取查询对话框		
		ConditionVO[] vos=(ConditionVO[]) ObjectUtils.serializableClone(querylg.getConditionVO());//获取已被用户填写的查询条件
		ConditionVO[] vos1=filterQuery(vos);
		if(vos1==null ||vos1.length==0){
			whereSql=null;
		}else{
			for(int i=0;i<vos1.length;i++){
				if(vos1[i].getFieldCode().equals("h.pk_customize1")){
					vos1[i].setFieldCode("tbh.pk_stordoc");
				}
				if(vos1[i].getFieldCode().equals("h.pk_invmandoc")){
					vos1[i].setFieldCode("b.cinventoryid"); 
				}
			}
			whereSql=querylg.getWhereSQL(vos1);
		}			
	    String startDate=ac.getMonthVO().getBegindate().toString();
	    String endDate=ac.getMonthVO().getEnddate().toString();
	    String wsql="";
	    wsql=" h.dbilldate >= '"+startDate+"' and  h.dbilldate <='"+endDate+"' ";
	    
		if(whereSql==null || whereSql.length()==0){
			
			whereSql=wsql;
			
		}else{
			
			whereSql=whereSql+" and "+wsql;
			
		}
		return WDSWLReportSql.getOrderDaiFaSql(whereSql);
	}
	 /**
     * 查询销售订单 本月前  待发
     * @作者：mlr
     * @说明：完达山物流项目 
     * @时间：2011-12-5下午03:02:25
     * @param whereSql
     * @return
     * @throws Exception
     */
	public String getQuerySQL3(String whereSql) throws Exception {
		QueryDLG querylg= getQueryDlg();//获取查询对话框		
		ConditionVO[] vos=(ConditionVO[]) ObjectUtils.serializableClone(querylg.getConditionVO());//获取已被用户填写的查询条件
		ConditionVO[] vos1=filterQuery(vos);
		if(vos1==null ||vos1.length==0){
			whereSql=null;
		}else{
			for(int i=0;i<vos1.length;i++){
				if(vos1[i].getFieldCode().equals("h.pk_customize1")){
					vos1[i].setFieldCode("tbh.pk_stordoc");
				}
				if(vos1[i].getFieldCode().equals("h.pk_invmandoc")){
					vos1[i].setFieldCode("b.cinventoryid"); 
				}
			}
			whereSql=querylg.getWhereSQL(vos1);
		}		
		   String startDate=ac.getMonthVO().getBegindate().toString();
		    String endDate=ac.getMonthVO().getEnddate().toString();
		    String wsql="";
		    wsql=" h.dbilldate < '"+startDate+"' ";		    
			if(whereSql==null || whereSql.length()==0){				
				whereSql=wsql;				
			}else{				
				whereSql=whereSql+" and "+wsql;				
			}
			return WDSWLReportSql.getOrderDaiFaSql(whereSql);
	}

	@Override
	public void initReportUI() {
		setColumn();
		type.put(0, "常用");
		type.put(1, "不常用");
	}
	 /**
     * 基本列合并
     */
    private void setColumn() {
        //表体栏目分组设置
        UITable cardTable = getReportBase().getBillTable();
        GroupableTableHeader cardHeader = (GroupableTableHeader) cardTable.getTableHeader();
        TableColumnModel cardTcm = cardTable.getColumnModel();
        
        ColumnGroup shiji =new ColumnGroup("实际库存");
        
       //大的分组 有效
        ColumnGroup zgroup=new ColumnGroup("有效");
        int i=1;//设置变化赋值量
        ColumnGroup a1=new ColumnGroup("30天以内");
        a1.add(cardTcm.getColumn(6+i));
        a1.add(cardTcm.getColumn(7+i));
        zgroup.add(a1);
      
        
        ColumnGroup a2=new ColumnGroup("30-60天");
        a2.add(cardTcm.getColumn(8+i));
        a2.add(cardTcm.getColumn(9+i));
        zgroup.add(a2);
        
        ColumnGroup a3=new ColumnGroup("60-90天");
        a3.add(cardTcm.getColumn(10+i));
        a3.add(cardTcm.getColumn(11+i));
        zgroup.add(a3);
        
        ColumnGroup a4=new ColumnGroup("小计");
        a4.add(cardTcm.getColumn(12+i));
        a4.add(cardTcm.getColumn(13+i));
        zgroup.add(a4); 
        
        shiji.add(zgroup);
                    
        ColumnGroup zgroup2=new ColumnGroup("无效");
        
        ColumnGroup b1=new ColumnGroup("91-120");
        b1.add(cardTcm.getColumn(14+i));
        b1.add(cardTcm.getColumn(15+i));
        zgroup2.add(b1);
        
        ColumnGroup b2=new ColumnGroup("121-150");
        b2.add(cardTcm.getColumn(16+i));
        b2.add(cardTcm.getColumn(17+i));
        zgroup2.add(b2);
        
        ColumnGroup b3=new ColumnGroup("151-180");
        b3.add(cardTcm.getColumn(18+i));
        b3.add(cardTcm.getColumn(19+i));
        zgroup2.add(b3);
        
        ColumnGroup b4=new ColumnGroup("181-210");
        b4.add(cardTcm.getColumn(20+i));
        b4.add(cardTcm.getColumn(21+i));
        zgroup2.add(b4);
        
        ColumnGroup b5=new ColumnGroup("211-240");
        b5.add(cardTcm.getColumn(22+i));
        b5.add(cardTcm.getColumn(23+i));
        zgroup2.add(b5);
        
        ColumnGroup b6=new ColumnGroup("241-270");
        b6.add(cardTcm.getColumn(24+i));
        b6.add(cardTcm.getColumn(25+i));
        zgroup2.add(b6);
        
        ColumnGroup b7=new ColumnGroup("271-300");
        b7.add(cardTcm.getColumn(26+i));
        b7.add(cardTcm.getColumn(27+i));
        zgroup2.add(b7);
        
        
        ColumnGroup b8=new ColumnGroup("301-330");
        b8.add(cardTcm.getColumn(28+i));
        b8.add(cardTcm.getColumn(29+i));
        zgroup2.add(b8);
        
        ColumnGroup b9=new ColumnGroup("331-365");
        b9.add(cardTcm.getColumn(30+i));
        b9.add(cardTcm.getColumn(31+i));
        zgroup2.add(b9);
        
        ColumnGroup b10=new ColumnGroup("去年");
        b10.add(cardTcm.getColumn(32+i));
        b10.add(cardTcm.getColumn(33+i));
        zgroup2.add(b10);
        
        ColumnGroup b11=new ColumnGroup("前年");
        b11.add(cardTcm.getColumn(34+i));
        b11.add(cardTcm.getColumn(35+i));
        zgroup2.add(b11);
        
      
        
        ColumnGroup b12=new ColumnGroup("小计");
        b12.add(cardTcm.getColumn(36+i));
        b12.add(cardTcm.getColumn(37+i));
        zgroup2.add(b12);
        
        shiji.add(zgroup2);
        
        ColumnGroup c1=new ColumnGroup("小计");
        c1.add(cardTcm.getColumn(38+i));
        c1.add(cardTcm.getColumn(39+i));
        
        shiji.add(c1);
        
        ColumnGroup c2=new ColumnGroup("(促销品180g-200g)");
        c2.add(cardTcm.getColumn(40+i));
        c2.add(cardTcm.getColumn(41+i));
        
        shiji.add(c2);
        
        
        ColumnGroup c3=new ColumnGroup("医务赠品");
        c3.add(cardTcm.getColumn(42+i));
        c3.add(cardTcm.getColumn(43+i));
        
        shiji.add(c3);

        
        ColumnGroup c4=new ColumnGroup("限区域发货(批次促销)");
        c4.add(cardTcm.getColumn(44+i));
        c4.add(cardTcm.getColumn(45+i));
        
        
        shiji.add(c4);
        

        ColumnGroup c5=new ColumnGroup("转分仓 (在途)");
        c5.add(cardTcm.getColumn(46+i));
        c5.add(cardTcm.getColumn(47+i));
        
        shiji.add(c5);
        
        ColumnGroup c6=new ColumnGroup("待检(从货龄中区分出来)");
        c6.add(cardTcm.getColumn(48+i));
        c6.add(cardTcm.getColumn(49+i));
        
        shiji.add(c6);
        
        cardHeader.addColumnGroup(shiji);
        
        ColumnGroup c7=new ColumnGroup("合计");
        c7.add(cardTcm.getColumn(50+i));
        c7.add(cardTcm.getColumn(51+i));     
        cardHeader.addColumnGroup(c7);
        
        ColumnGroup qianfa=new ColumnGroup("欠发库存");  
        
     ColumnGroup zgroup3=new ColumnGroup("正常销售");
        
        ColumnGroup d1=new ColumnGroup("本月前正常待发");
        d1.add(cardTcm.getColumn(52+i));
        d1.add(cardTcm.getColumn(53+i));
        zgroup3.add(d1);
        
        ColumnGroup d2=new ColumnGroup("本月正常待发");
        d2.add(cardTcm.getColumn(54+i));
        d2.add(cardTcm.getColumn(55+i));
        zgroup3.add(d2);
        
        ColumnGroup d3=new ColumnGroup("本月前补货待发（B字头）");
        d3.add(cardTcm.getColumn(56+i));
        d3.add(cardTcm.getColumn(57+i));
        zgroup3.add(d3);
        
        ColumnGroup d4=new ColumnGroup("本月补货待发（B字头）");
        d4.add(cardTcm.getColumn(58+i));
        d4.add(cardTcm.getColumn(59+i));
        zgroup3.add(d4); 
        
        qianfa.add(zgroup3);
        
        
    ColumnGroup zgroup4=new ColumnGroup("转分仓");
        
        ColumnGroup e1=new ColumnGroup("本月虚拟总仓转分仓库存量");
        e1.add(cardTcm.getColumn(60+i));
        e1.add(cardTcm.getColumn(61+i));
        zgroup4.add(e1);
             
        qianfa.add(zgroup4);
        
        cardHeader.addColumnGroup(qianfa);
           
         ColumnGroup e2=new ColumnGroup("总计");
         e2.add(cardTcm.getColumn(62+i));
         e2.add(cardTcm.getColumn(63+i));
              
      cardHeader.addColumnGroup(e2);

        getReportBase().getBillModel().updateValue();
    }
    public String _getModelCode() {
        return WdsWlPubConst.report1;
    }

	@Override
	public String getQuerySQL() throws Exception {
		return null;
	}
	/**
	 * 计算存货的常用不常用类型
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 * @时间：2011-12-3下午02:21:21
	 * @param vos
	 */
	public static Map<Integer,String> type=new HashMap<Integer,String>();//物品类型
	public void calType(ReportBaseVO[] vos){
		if(vos==null || vos.length==0)
			return;
		for(int i=0;i<vos.length;i++){
			int t=PuPubVO.getInteger_NullAs(vos[i].getAttributeValue("invtype"),-1);
		    String value=type.get(t);
			if(value==null || value.length()==0)
				continue;
			vos[i].setAttributeValue("typename", value);
				
		}				
	}
	
	

}
