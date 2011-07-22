package nc.ui.wds.report.xffzc;
import java.util.HashMap;
import java.util.Map;
import nc.bs.logging.Logger;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.IBillItem;
import nc.ui.pub.report.ReportItem;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.wl.pub.LongTimeTask;
import nc.ui.wl.pub.report.ReportPubTool;
import nc.ui.wl.pub.report.WDSReportBaseUI;
import nc.ui.wl.pub.report.WDSWLReportSql;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.vosplit.SplitBillVOs;
import nc.vo.wds.ic.storestate.TbStockstateVO;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.report.IUFTypes;
import nc.vo.wl.pub.report.ReportBaseVO;
import nc.vo.wl.pub.report.SubtotalVO;
/**
 * 非正常箱粉报表
 * @author mlr
 */
public class ReportUI extends WDSReportBaseUI{
	private static final long serialVersionUID = 1L;
   //报表vo中存货状态主键
    private  String ss_pk="pk_storestate";
    
   //存货分类箱粉编码
	private   String invclcode = "00";
    //库存辅数量对应字段
    private  String bnum="bnum";
    //非正常分类字段的对应前缀
    private  String num="num";
    
    //动态列从原有列的哪个位置  开始插入 动态列       该字段记录动态列开始插入的位置
    private  int location=3;
    //设置总吨数是换算率字段
    private  String hsl="hsl";
    //总吨数对应字段
    private  String zton="sumnum";
    //合计行所在字段
    private  String total="invname";  
    
	@Override
	public String _getModelCode() {	
		return WdsWlPubConst.REPORT06;
	}
	public ReportUI() {
		super();
		//去除字段自动排序的功能
		getReportBase().getBillTable().removeSortListener();
	}
	@Override
	public void setUIAfterLoadTemplate() {
          
	}
	/**
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 *        从存货状态表中获得要组装动态列的数据
	 *        父类调用该方法
	 * @时间：2011-7-8上午09:54:00
	 * @return
	 * @throws Exception 
	 */
	@Override
	public Map getNewItems() throws Exception {
		HashMap<String, Object> map=new HashMap<String, Object>();
		//设置动态列插入位置
	    map.put("location", new Integer(3));
		//过滤库存状态为非正常的
		String wheresql=" isnull(tb_stockstate.dr,0)=0 and upper(coalesce(tb_stockstate.isok,'N'))='N'";
		SuperVO[] vos= HYPubBO_Client.queryByCondition(TbStockstateVO.class, wheresql);
		if(vos==null || vos.length==0){
			return null;
		}
		//存货状态动态列,显示字段
	     String displayName="ss_state";
	     //库存状态基础信息表的主键      
	     String pk="ss_pk";  
	    //用于设置库龄存货数量数对应的主数量字段名  
	     String num="num";   	     
		//动态列的元素
		ReportItem[] res=new ReportItem[vos.length];
		//动态列对应的库存状态表的主键
		pk_storestates=new String[vos.length];
		int size=vos.length;
		for(int i=0;i<size;i++){
		  ReportItem it=ReportPubTool.getItem(num+(i+1),(String)vos[i].getAttributeValue(displayName),IBillItem.DECIMAL,i, 80);
		  res[i]=it;
		  pk_storestates[i]=(String) vos[i].getAttributeValue(pk);
		}	
		map.put("items",res);
		
		return map;
	}	
	@Override
	public ReportBaseVO[] getReportVO(String sql) throws BusinessException {
		 ReportBaseVO[] reportVOs = null;
	        try{
	            Class[] ParameterTypes = new Class[]{String.class};
	            Object[] ParameterValues = new Object[]{sql};
	            Object o = LongTimeTask.calllongTimeService(WdsWlPubConst.WDS_WL_MODULENAME, this, 
	                    "正在查询...", 1, "nc.bs.wds.pub.report.ReportDMO", null, 
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
	@Override
	public void onQuery() {
		try{		
			    super.onQuery();
            	//得到自定义查询条件
                //得到查询结果
                ReportBaseVO[] vos = getReportVO(getQuerySQL());
                if(vos != null && vos.length>0){                	
					//super.updateBodyDigits();					
					//根据是否货位展开 和 是否批次展开  合并查询出来的报表vo
                	 ReportBaseVO[] newVos=null;
					if(iscargdoc.booleanValue()==true&&isvbanchcode.booleanValue()==true){
						newVos=setVoByInvState(vos, fields0);
					     			
					}else if(iscargdoc.booleanValue()==false&&isvbanchcode.booleanValue()==false){
						newVos=setVoByInvState(vos, fields4);
					    			
					}else if(iscargdoc.booleanValue()==true&&isvbanchcode.booleanValue()==false){
					   newVos=setVoByInvState(vos, fields1);
					    							
					}else if(iscargdoc.booleanValue()==false&&isvbanchcode.booleanValue()==true){
					   newVos=setVoByInvState(vos, fields2);	
					} 
					setReportBaseVO(newVos);
				    setBodyVO(newVos);	
				    setTolal();
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
	 *        将vos 按仓库,货位,生产日期,存货 进行分组
	 *        然后存货状态类型 将各个组的数据进行合并
	 * @时间：2011-7-7下午04:46:33
	 * @param vos  vo数组
	 * @param splitFields 分组的维度条件
	 */
	private ReportBaseVO[] setVoByInvState(ReportBaseVO[] vos,String[] splitFields) {
		if(vos==null || vos.length==0){
			return vos;
		}
		CircularlyAccessibleValueObject[][] voss=SplitBillVOs.getSplitVOs(vos,splitFields);
		if(voss==null || voss.length==0){
			return vos;
		}
		ReportBaseVO[] newVos=new ReportBaseVO[voss.length];
		int size=voss.length;
		for(int i=0;i<size;i++){
			int size1=voss[i].length;
			//组合vo 用来将按维度分组后的vo数组  组合到一个vo中
			ReportBaseVO newvo=null;
			for(int j=0;j<size1;j++){
				ReportBaseVO vo=(ReportBaseVO) voss[i][j];
				if(vo==null){
				   continue;
				}
				if(newvo==null){
					newvo=(ReportBaseVO) vo.clone();
				}
				int length=pk_storestates.length;
				//取得存货状态主键的值
				Object s_pk=vo.getAttributeValue(ss_pk);
				String pk_s=null;
				if(s_pk!=null && !s_pk.equals("")){
				   pk_s=(String) s_pk;
				}else{
					continue;
				}				
				for(int k=0;k<length;k++){
					if(pk_storestates[k].equalsIgnoreCase(pk_s)){
					//从组合vo中取对应值	
					UFDouble znum=PuPubVO.getUFDouble_NullAsZero(newvo.getAttributeValue(num+(k+1)));
					//从vo中取对应值
					UFDouble fnum=PuPubVO.getUFDouble_NullAsZero(vo.getAttributeValue(bnum));
					newvo.setAttributeValue(num+(k+1), znum.add(fnum));
					break;
					}
				}				
			}
			if(newvo!=null){
				setZton(newvo);
				newVos[i]=newvo;			
			}
		}
		return newVos;
	}
	//设定非正常箱粉的总吨数
	private void setZton(ReportBaseVO newvo) {
		if(pk_storestates==null && pk_storestates.length==0){
			return;
		}
		int size =pk_storestates.length;
		UFDouble znum=new UFDouble("0.0");
		for(int i=0;i<size;i++){
			UFDouble inum=PuPubVO.getUFDouble_NullAsZero(newvo.getAttributeValue(num+(i+1)));
		   	znum=znum.add(inum);		
		}
		//获得换算率 
		UFDouble hsl1=PuPubVO.getUFDouble_NullAsZero(newvo.getAttributeValue(hsl));
		newvo.setAttributeValue(zton, znum.multiply(hsl1));
	}
	/**
     * 
     * @作者：mlr
     * @说明：完达山物流项目  
     * 根据自定义条件得到查询报表数据的SQL
     * @时间：2011-5-10上午09:41:31
     * @param wheresql
     * @return
     */
	private String getQuerySQL(){
		return WDSWLReportSql.getQuerySQL(invclcode,new UFBoolean(false),pk_stordoc,null,new UFBoolean(false),new UFBoolean(false),new UFBoolean(true), iscargdoc, isvbanchcode, ddatefrom, ddateto);
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
        String[] tolfields=new String[pk_storestates.length+1];
        int size=tolfields.length;
        for(int i=0;i<size;i++){
        	if(i==size-1){
        	  tolfields[i]=zton;
        	}else{
        	tolfields[i]=num+(i+1);
        	}
        } 
        int[] types=new int[size];
        for(int i=0;i<size;i++){
        	types[i]=IUFTypes.UFD;
        }
        svo.setValueFlds(tolfields);// 求值列:
        svo.setValueFldTypes(types);// 求值列的类型:
        svo.setTotalDescOnFld(total);// ----合计---字段 ---- 所在列
        setSubtotalVO(svo);
        doSubTotal();
    }
	@Override
	public void initReportUI() {
		
		
	}
}
