package nc.ui.wl.pub.report;
import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.ListSelectionModel;

import nc.bd.accperiod.AccountCalendar;
import nc.ui.bd.manage.UIRefCellEditor;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.IBillItem;
import nc.ui.pub.report.ReportItem;
import nc.ui.trade.report.query.QueryDLG;
import nc.ui.wl.pub.LoginInforHelper;
import nc.ui.wl.pub.WdsQueryDlg;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wl.pub.WdsWlPubTool;
import nc.vo.wl.pub.report.ReportBaseVO;
/**
 * 
 * @作者：mlr
 * @说明：完达山物流项目
 *        报表ui基类
 *        针对是否按批次 货位展开的报表ui
 * @时间：2011-7-8下午03:20:53
 */
abstract public class WDSReportBaseUI extends ReportBaseUI{	
	private static final long serialVersionUID = -8293771841532487812L;
	//查询条件开始日期
	protected static String ddatefrom =null;
	//查询条件结束日期
	protected static String ddateto = null;
	//查询仓库主键
    protected static String pk_stordoc=null;
    //查询存货主键
    protected static String pk_invbasdoc=null;
    //是否按仓库展开
    protected UFBoolean isstordoc=new UFBoolean(false);
    //是否按货位展开
    protected UFBoolean iscargdoc=new UFBoolean(false);
	//是否按批次展开
    protected UFBoolean isvbanchcode=new UFBoolean(false);
    //仓库字段名
    protected static String store="storename";
	//货位字段名
    protected static String cargdoc="csname";
    //批次字段名
    protected static  String banchcode="vbatchcode";
    //记录初次加载的表体元素  
    protected  ReportItem[]olditems=null;
    
    //三个全选
    //存货    仓库  货位  批次
    
    //按仓库 货位  存货 批次维度分组的字段数组
    protected static String[] fields0=new String[]{"pk_stordoc","pk_cargdoc","pk_invbasdoc","vbatchcode"};
    
    //三个选两个
    //存货   仓库  货位
    //存货   仓库  批次
    //存货   货位  批次
    
    //按仓库 货位  存货维度分组的字段数组
    protected static String[] fields1=new String[]{"pk_stordoc","pk_cargdoc","pk_invbasdoc"};
    //按仓库 货位  批次维度分组的字段数组
    protected static String[] fields2=new String[]{"pk_stordoc","pk_invbasdoc","vbatchcode"};
    //按 存货  货位 批次维度分组的字段数组
    protected static String[] fields3=new String[]{"pk_cargdoc","pk_invbasdoc","vbatchcode"};
       
    //三个只选一个
    //三种情况
    
    //按仓库 存货维度分组的字段数组
    protected static String[] fields4=new String[]{"pk_stordoc","pk_invbasdoc"};
    //按货位 存货维度分组的字段数组
    protected static String[] fields5=new String[]{"pk_cargdoc","pk_invbasdoc"};
    //按批次 存货维度分组的字段数组
    protected static String[] fields6=new String[]{"pk_invbasdoc","vbatchcode"};
    
    
    //三个都不选
    
    //按  存货维度分组的字段数组
    protected static String[] fields7=new String[]{"pk_invbasdoc"};
    
    //查询动态列的插入位置 默认插入第0列
    private  Integer location1=0;
    //报表模板初次加载时 动态列插入位置
    protected  Integer location=0;  
    protected  String[] pk_storestates=null;//存货状态主键 数组   箱粉非正常库存报表 专用字段   ---mlr
    
	public WDSReportBaseUI() {
		super();		
		//yf add
		LoginInforHelper login = new LoginInforHelper();
		try {
			cwhid = login.getCwhid(_getUserID());
			ccargdoc = login.getLogInfor(_getUserID()).getSpaceid();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			cwhid = null;
			ccargdoc = null;
		}
		//yf end
		initReportUI();
		setDynamicColumn();
	}
	/**
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 *       初始化ui类
	 * @时间：2011-7-15下午01:47:25
	 */
	public abstract void initReportUI();
	
    /**
     * 
     * @作者：mlr
     * @说明：完达山物流项目 
     *        设置查询动态列位置
     * @时间：2011-7-15下午08:08:09
     * @param location1
     */
	public void setLocation1(Integer location1) {
		this.location1 = location1;
	}
	@Override
	public ReportBaseVO[] getReportVO(String wheresql) throws BusinessException {
		
		return null;
	}
	/**
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 *       将动态列与静态列 合并
	 * @时间：2011-7-8下午03:30:07
	 * @param olditems 一般认为是静态列
	 * @param newitems 一般认为是动态列
	 * @return
	 */
	protected ReportItem[] combin(ReportItem[] olditems, ReportItem[] newitems,int location) {
		if(newitems==null || newitems.length==0){
		  return olditems;	
		}
		ReportItem[] its=new ReportItem[olditems.length+newitems.length];
		System.arraycopy(olditems, 0, its, 0, location);
	    System.arraycopy(newitems, 0, its,location,newitems.length);
	    System.arraycopy(olditems, location, its, location+newitems.length, olditems.length-location);
		return its;
	}
	/**
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目
	 *        根据某个维度(条件)
	 *        将两个数组中条件字段对应值相同的合并
	 *        根据 求值字段数组和类型数组 判断需要求和的字段
	 *        进行求和运算
	 *        
	 *        使用本方法的前提条件：
     *        两个vo数组按维度条件只能查到一个符合条件的vo
	 *             
	 * @时间：2011-7-11下午09:12:25
	 * @param vos
	 * @param vos1
	 * @param voCombinConds 条件字段数组
	 * @param types 求值类型
	 * @param combinFields 求值字段
	 * @return
	 */
	public  ReportBaseVO[] combinVoByFields(ReportBaseVO[] vos, ReportBaseVO[] vos1,
			String[] voCombinConds, int[] types,String[] combinFields) {
		    ReportBaseVO[] combins= (ReportBaseVO[]) nc.ui.wl.pub.report.CombinVO.combinVoByFields(vos, vos1, voCombinConds, types, combinFields);
		return combins;
	}
	/**
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 *      设置查询构成的动态列
	 * @时间：2011-7-15下午01:10:00
	 */
	private void setDynamicColumn1() {
		 ReportItem[] newitems;
			try {
			        newitems = getNewItems1();
				    ReportItem[] allitems = combin(olditems,newitems,location1);        
			        getReportBase().setBody_Items(allitems);
			        updateUI();
			} catch (Exception e) {
				e.printStackTrace();
			}           		
	}
	/**
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 *        获得查询动态列元素
	 * @时间：2011-7-8上午09:54:00
	 * @return
	 * @throws Exception 
	 */
	private ReportItem[] getNewItems1() throws Exception {
	 List<ReportItem> list=new ArrayList<ReportItem>();
	 if(isstordoc.booleanValue()==true){
	  list.add(ReportPubTool.getItem(store,"仓库",IBillItem.STRING,1, 80));
	 }
     if(iscargdoc.booleanValue()==true){
	  list.add(ReportPubTool.getItem(cargdoc,"货位",IBillItem.STRING,1, 80));
     }
     if(isvbanchcode.booleanValue()==true){
      list.add(ReportPubTool.getItem(banchcode,"批次",IBillItem.STRING,2, 80));
     } 
     if(list.size()==0){
    	 return null; 
     }else{
    	return list.toArray(new ReportItem[0]); 
     }	
	}	

	@Override
	public void onQuery() {
	  	//设置查询模板默认查询条件
        AccountCalendar  accCal = AccountCalendar.getInstance();     
        getQueryDlg().setDefaultValue("ddatefrom", accCal.getMonthVO().getBegindate().toString(), "");
        getQueryDlg().setDefaultValue("ddateto", accCal.getMonthVO().getEnddate().toString(), "");
        //yf add
        
		getQueryDlg().setDefaultValue("pk_stordoc", cwhid, cwhid);
		getQueryDlg().initData();
		if(PuPubVO.getString_TrimZeroLenAsNull(cwhid)!=null){
			if(!WdsWlPubTool.isZc(cwhid)){
				getComponent("pk_stordoc").setEnabled(false);
			}
		}
		//yf end
		getQueryDlg().showModal();
	     if (getQueryDlg().getResult() == UIDialog.ID_OK) {		  
	    	//清空表体数据
	    	 clearBody();	    	
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
        	//设置查询条件
        	setQueryCondition();           	
        	//设置动态列
        	setDynamicColumn1();
        	//中文的查询条件
            
        	String qryconditons = getQueryDlg().getChText();
        	if(!qryconditons.contains("截止日期")){
        		qryconditons = qryconditons+"并且(截止日期 小于等于 '"+ddateto+"')";
        	}
        	getReportBase().setHeadItem("ddatefrom", ddatefrom);
        	getReportBase().setHeadItem("ddateto", ddateto);
        	getReportBase().getHeadItem("qryconditons").setWidth(2);
        	getReportBase().setHeadItem("qryconditons", qryconditons);
	    }	
	}
	/**
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 *        每次查询前清空表体数据
	 * @时间：2011-7-16下午02:38:27
	 */
	private void clearBody() {
		 setBodyVO(null);
    	 updateUI();		
	}
	@Override
	public void setUIAfterLoadTemplate() {
		
		
	}
	/**
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 *        设置动态列
	 * @时间：2011-7-8下午03:22:30
	 */	
	private void setDynamicColumn() {
		 //-----------------------处理 模板  支持动态列      
		 getReportBase().getBillTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);	               
	        olditems = getReportBase().getBody_Items();       
	        ReportItem[] newitems=null;
			try {
			        Map map = getNewItems();
			        if(map !=null){
			        location=PuPubVO.getInteger_NullAs(map.get("location"), new Integer(0));
			        newitems=(ReportItem[])map.get("items");
			        }
				    ReportItem[] allitems = combin(olditems,newitems,location);   
				    olditems=allitems;
			        getReportBase().setBody_Items(allitems);
			        updateUI();
			} catch (Exception e) {
				e.printStackTrace();
			}            		
	}
    /**
     * 
     * @作者：mlr
     * @说明：完达山物流项目 
     *        获取报表模板初始化时的动态列
     *        map key=location 存放动态列插入位置
     *            key=items    存放动态列元素
     * @时间：2011-7-15下午02:21:31
     * @return
     */
	abstract public Map getNewItems()throws Exception ;
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
    	//从查询对话框,获取是否仓库展开
    	isstordoc=new UFBoolean(false);
    	//从查询对话框,获取是否货位展开
    	iscargdoc=new UFBoolean(false);
    	//从查询对话框,获取是批次展开
    	isvbanchcode=new UFBoolean(false);
    	pk_stordoc=null;
    	pk_invbasdoc=null;
    	for(int i=0;i<size;i++){
    		if(vos[i].getFieldCode().equalsIgnoreCase("pk_stordoc")){
    			pk_stordoc=vos[i].getValue();
    		}
    		if(vos[i].getFieldCode().equalsIgnoreCase("pk_invbasdoc")){
    			pk_invbasdoc=vos[i].getValue();
    		}
    		
    		if(vos[i].getFieldCode().equalsIgnoreCase("iscargdoc")){
    			iscargdoc=PuPubVO.getUFBoolean_NullAs(vos[i].getValue(), new UFBoolean(false));           			
    		}
            if(vos[i].getFieldCode().equalsIgnoreCase("isvbanchcode")){
            	isvbanchcode=PuPubVO.getUFBoolean_NullAs(vos[i].getValue(), new UFBoolean(false));
    		} 
            if(vos[i].getFieldCode().equalsIgnoreCase("isstordoc")){
            	isstordoc=PuPubVO.getUFBoolean_NullAs(vos[i].getValue(), new UFBoolean(false));           			
    		}
    	}		
	}	
	/**
	 * 
	 * @作者：yf
	 * @说明：完达山物流项目 
	 * @时间：2011-9-1下午01:58:46
	 * @param filedcode
	 * @return
	 */
	protected String cwhid = null;//仓库
	protected String ccargdoc = null;//货位
	protected Component getComponent(String filedcode){
		Object o = getQueryDlg().getValueRefObjectByFieldCode(filedcode);
		Component jb  = null;
		if(o instanceof UIRefCellEditor){
			jb = ((UIRefCellEditor)o).getComponent();//getUITabInput().getCellEditor(1, 4);
		}else{
			jb = (Component)o;
		}

		return jb;
	}
}
