package nc.ui.zmpub.pub.report;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.ListSelectionModel;

import nc.bs.logging.Logger;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.IBillItem;
import nc.ui.pub.report.ReportItem;
import nc.ui.trade.report.query.QueryDLG;
import nc.ui.zmpub.pub.report.buttonaction.IReportButton;
import nc.ui.zmpub.pub.tool.LongTimeTask;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.zmpub.pub.report.ReportBaseVO;
/**
 * @作者：mlr
 * @说明：鸡西项目
 *        鸡西供应报表基类  
 *        支持ui初始化是加载动态列
 *        支持查询条件生成动态列  按动态列数据展开
 * @时间：2011-7-8下午03:20:53
 */
abstract public class ZmReportBaseUI extends ReportBaseUI{	
	private static final long serialVersionUID = -8293771841532487812L;  
    protected  ReportItem[]olditems=null; //记录初次加载的表体元素  
    private  Integer location=0;//查询动态列的插入位置 默认插入第0列
    
    
    private static String sql =null;
	public ZmReportBaseUI() {
		super();		
		initReportUI();
		setDynamicColumn();
	}
	
	protected void initPrivateButton() {		 
		addPrivateButton("刷新", "刷新数据",IReportButton.onboRefresh);
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
	public void setLocation(Integer location) {
		this.location=location;
	}
	@Override
	public ReportBaseVO[] getReportVO(String sql) throws BusinessException {
		 ReportBaseVO[] reportVOs = null;
	        try{
	        	this.sql=sql;
	            Class[] ParameterTypes = new Class[]{String.class};
	            Object[] ParameterValues = new Object[]{sql};
	            Object o = LongTimeTask.calllongTimeService(null, this, 
	                    "正在查询...", 1, "nc.bs.ca.pub.report.ReportDMO", null, 
	                    "queryVOBySql", ParameterTypes, ParameterValues);
	            if(o != null){
	                reportVOs = (ReportBaseVO[])o;
	            }
	        }catch(Exception e){
	            Logger.error(e);
	            MessageDialog.showErrorDlg(this, "警告", e.getMessage());
	        }
	        return reportVOs;
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
     */
    protected void onBoElse(Integer intBtn) throws Exception {
    	switch (intBtn) {
		  case IReportButton.onboRefresh: {			
			onboRefresh();
			break;
		  }
		}    	
    	super.onBoElse(intBtn);	
    }
    public void onboRefresh()throws Exception{
      	   //清空表体数据
    	   clearBody();	    	         	
      	  //设置动态列
    	  setDynamicColumn1();
          //得到查询结果
      	  ReportBaseVO[] vos=null;
    	  //设置基本列合并
    	   setColumn();
    	  //设置vo
 		  vos=getReportVO(sql);		       	 
         if(vos==null || vos.length==0)
        	 return;       
         if(vos != null){                	
 			super.updateBodyDigits();
 			setBodyVO(vos);				
            setTolal();	                
        }   
         this.showHintMessage("刷新操作结束");
    }
	/**
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 *      设置查询构成的动态列
	 * @时间：2011-7-15下午01:10:00
	 */
	public void setDynamicColumn1() {
		 ReportItem[] newitems;
			try {
			        newitems = getNewItems1();
				    ReportItem[] allitems = combin(olditems,newitems,location);        
			        getReportBase().setBody_Items(allitems);
			        updateUI();
			} catch (Exception e) {
				e.printStackTrace();
			}           		
	}
	/**
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 *        获得查询动态列元素
	 * @时间：2011-7-8上午09:54:00
	 * @return
	 * @throws Exception 
	 */
	public ReportItem[] getNewItems1() throws Exception {
	 List<ReportItem> list=new ArrayList<ReportItem>();
	 ConditionVO[] vos=getQueryDlg().getConditionVO();
     if(vos==null || vos.length==0)
    	 return null; 
     for(int i=0;i<vos.length;i++){
    	if(vos[i].getFieldCode().startsWith("is")){
    	  if(PuPubVO.getUFBoolean_NullAs(vos[i].getValue(), UFBoolean.FALSE).booleanValue()==true){
    		  ReportItem it=ReportPubTool.getItem(vos[i].getFieldCode().substring(2),getSpitName(vos[i].getFieldName()),IBillItem.STRING,2, 80);
    		  list.add(it);
    	      setItemsAfter(it,list);
    	  }
    	}  
	 }
     if(list.size()==0){
    	 return null; 
     }else{
    	return list.toArray(new ReportItem[0]); 
     }	
	}
	/**
	 * 传入将要展开的字段  在本次查询中
	 * 做一些后续处理
	 * @param it 为当前将要展开的 ReportItem
	 * @param list 装载当前查询条件生成的 动态列集合 
	 */
	public void setItemsAfter(ReportItem it, List<ReportItem> list) {
		
	}
	/**
	 * 获取分组字段的中文名
	 * 例如 ：是否按货位展开
	 * 则 取货位 从‘按’开始取值 到‘展’结束  
	 * @author mlr
	 * @说明：（鹤岗矿业）
	 * 2011-10-19下午02:14:16
	 * @param name
	 * @return
	 */
    private String getSpitName(String name){
    	if(name==null || name.length()==0){
    		return null;
    	}
    	return name.substring(2,name.lastIndexOf("展开"));
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
            //得到查询结果
        	ReportBaseVO[] vos=null;
        	//设置基本列合并
        	 setColumn();
        	 //设置vo
			 vos=getReportVO(getQuerySQL());		       	 
             if(vos==null || vos.length==0)
            	 return;       
             if(vos != null){                	
				super.updateBodyDigits();
				setBodyVO(vos);				
	            setTolal();	                
             }  
	  	} catch (BusinessException e) {
			e.printStackTrace();
		} catch (Exception e) {			
			e.printStackTrace();
		} 
	   }  
	}	
	  /**
     * 合计
     */
    public void setTolal() throws Exception {
 
    }
	 /**
     * 基本列合并
     */
    private void setColumn() {
    	   
    }
	/**
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 *        每次查询前清空表体数据
	 * @时间：2011-7-16下午02:38:27
	 */
	public void clearBody() {
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
	 *        子类必须实现getNewItems()方法
	 * @时间：2011-7-8下午03:22:30
	 */	
	public void setDynamicColumn() {
		 //-----------------------处理 模板  支持动态列      
		 getReportBase().getBillTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);	               
	        olditems = getReportBase().getBody_Items();       
	        ReportItem[] newitems=null;
			try {  
			        Map map = getNewItems();
			        Integer location=0;//初始化动态列加载
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
	 * 从对话框获取查询条件
	 * @author mlr
	 * @说明：（鹤岗矿业）
	 * 2011-10-19下午01:53:31
	 * @return
	 * @throws Exception
	 */
	protected String getQueryConditon()
	throws Exception {
		QueryDLG querylg= getQueryDlg();//获取查询对话框		
		ConditionVO[] vos=querylg.getConditionVO();//获取已被用户填写的查询条件
		ConditionVO[] vos1=filterQuery(vos);
		String sql=querylg.getWhereSQL(vos1);
		return sql;
  } 	
/**
 * 过滤掉展开的条件
 * @author mlr
 * @说明：（鹤岗矿业）
 * 2011-10-19下午02:46:14
 * @param vos
 * @return
 */
  private ConditionVO[] filterQuery(ConditionVO[] vos) {
	   List<ConditionVO> list=new ArrayList<ConditionVO>();
	   if(vos==null || vos.length==0)
		   return null;
	   for(int i=0;i<vos.length;i++){		   
	      if(vos[i].getFieldCode().startsWith("is")){
	    	  continue;
	      }
	      list.add(vos[i]);
	   }
	   if(list.size()==0)
		   return null;
		return list.toArray(new ConditionVO[0]);
  }
  /**
   * 过滤出掉展开的条件
   * @author mlr
   * @说明：（鹤岗矿业）
   * 2011-10-19下午02:46:14
   * @param vos
   * @return
   */
    private ConditionVO[] filterOrderBy(ConditionVO[] vos) {
  	   List<ConditionVO> list=new ArrayList<ConditionVO>();
  	   if(vos==null || vos.length==0)
  		   return null;
  	   for(int i=0;i<vos.length;i++){		   
  	      if(vos[i].getFieldCode().startsWith("is")){
  	    	list.add(vos[i]);
  	      } 	      
  	   }
  	   if(list.size()==0)
  		   return null;
  		return list.toArray(new ConditionVO[0]);
    }
    /**
     * @作者：mlr
     * @说明：完达山物流项目  
     *  继承类JxReportBaseUI的类的 此方法书写规范
     *  要想支持 按某个字段展开
     *  必须 在查询模板 配置展开字段  已is开头  之后是表对应需要展开字段的名字  类型为boolean类型
     *  中文名  为 是否按 ‘xx’ 展开  
     *  如果查询是 选中 如 是否按货位展开 则 取'货位'字段作为构造的查询动态列的 中文名字
     *  getGroupByOrSelectConditon 此方法获得 查询框确定后 返回已经确定的展开的字段
     * @时间：2011-5-10上午09:41:31
     * 
     */
	
	public abstract String getQuerySQL() throws Exception;
/**
  * 获取需要展开的字段 或 已确定分组需要查询的字段
  * 在查询框中 配置  必须已is开头后面跟上数据库对应字段的名称
  * @author mlr
  * @说明：（鹤岗矿业）
  * 2011-10-19下午01:55:14
  * @return
  * @throws Exception
  */
  protected String getGroupByOrSelectConditon()
	throws Exception {
		StringBuffer sql=new StringBuffer();
		QueryDLG querylg= getQueryDlg();//获取查询对话框	
		ConditionVO[] vos=querylg.getConditionVO();//获取已被用户填写的查询条件
		ConditionVO[] vos1=filterOrderBy(vos);
		if(vos1==null || vos1.length==0)
			return null;
		for(int i=0;i<vos1.length;i++){	
		
			    if(PuPubVO.getUFBoolean_NullAs(vos1[i].getValue(), UFBoolean.FALSE).booleanValue()==true){
			    	sql.append(vos1[i].getFieldCode().substring(2));
			    	if(i!=vos1.length-1){
			    	sql.append(" ,");
			    	}
			    }
					
	   }   
		return sql.toString();
  } 
}
