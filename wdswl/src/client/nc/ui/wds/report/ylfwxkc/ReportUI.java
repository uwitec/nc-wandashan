package nc.ui.wds.report.ylfwxkc;

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
import nc.ui.pub.bill.IBillItem;
import nc.ui.pub.report.ReportItem;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.wl.pub.LongTimeTask;
import nc.ui.wl.pub.report.ReportBaseUI;
import nc.ui.wl.pub.report.ReportPubTool;
import nc.ui.wl.pub.report.WDSWLReportSql;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.vosplit.SplitBillVOs;
import nc.vo.wds.ic.storestate.TbStockstateVO;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.report.IUFTypes;
import nc.vo.wl.pub.report.ReportBaseVO;
import nc.vo.wl.pub.report.SubtotalVO;
/**
 * 物流原料粉无效库存报表
 * @author yf
 *
 */
public class ReportUI extends ReportBaseUI {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**原料粉库存报表节点 */
	public static String  REPORT_YLFWXKC="80100213";
	private int splength = 0;
    
    private String ddatefrom =null;
    private String ddateto = null;
    //仓库,货位,生产日期,存货 
    private static String[] splitFields=new String[]{"PK_CUSTOMIZE1","pk_cargdoc","dstartdate","pk_invbasdoc"};   	
   //报表vo中存货状态主键
    private static String ss_pk="pk_storestate";
    //库存状态基础信息表的主键
    private static String pk="ss_pk";
    //库存辅数量对应字段
    private static String bnum="bnum";
    //非正常分类字段的对应前缀
    private static String num="num";
    //存货状态动态列,显示字段
    private static String displayName="ss_state";
    //动态列从原有列的哪个位置  开始插入 动态列       该字段记录动态列开始插入的位置
    private static int location=4;
    //设置总吨数是换算率字段
    private static String hsl="hsl";
    //总吨数对应字段
    private static String zton="sumnum";
    //总袋数
    private static String zdai="sumnum1";
    //合计行所在字段
    private static String total="invname";
    //存货状态主键 数组   箱粉非正常库存报表 所用字段  
    protected  String[] pk_storestates=null;  	

    private String[] displaynames = null;
    
    private String pk_stordoc=null;
   //存货分类原料粉编码
	private   String invclcode = "00";
    
	private void setCustomColumns() {
		//表体栏目分组设置
    	//当前单据表单
        UITable cardTable = getReportBase().getBillTable();
        //表单列头
        GroupableTableHeader cardHeader = (GroupableTableHeader) cardTable.getTableHeader();
        //表单 列对象
        TableColumnModel cardTcm = cardTable.getColumnModel();
        //新建 列标
        ColumnGroup zgroup1=new ColumnGroup("生产日期");
        //列对象 加入新列标
        zgroup1.add(cardTcm.getColumn(2));
        zgroup1.add(cardTcm.getColumn(3));
        
        //新建 列标
        ColumnGroup zgroup=new ColumnGroup("不合格原因");
        //列对象 加入新列标
        ColumnGroup[] zgroups = new ColumnGroup[splength];
        int j = 0;
        for(int i = 0; i < splength; i++){
        	zgroups[i] = new ColumnGroup(displaynames[i]);
        	zgroups[i].add(cardTcm.getColumn(location+j));
        	j++;
        	zgroups[i].add(cardTcm.getColumn(location+j));
        	j++;
        	zgroup.add(zgroups[i]);
        }
        //列表 加入 列头
        cardHeader.addColumnGroup(zgroup1);
        cardHeader.addColumnGroup(zgroup);
        //更新 单据表单
        getReportBase().getBillModel().updateValue();
	}	
	
	@Override
	public String _getModelCode() {
		return this.REPORT_YLFWXKC;
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
		setDynamicColumn();		
		setCustomColumns();//设置合并类列
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
	        ReportItem[] olditems = getReportBase().getBody_Items();       
	        ReportItem[] newitems;
			try {
			        newitems = getNewItems();
				    ReportItem[] allitems = combin(olditems,newitems);        
			        getReportBase().setBody_Items(allitems);
			        updateUI();
			} catch (Exception e) {
				e.printStackTrace();
			}            		
	}
	@Override
	public void setUIAfterLoadTemplate() {
          
	}
	/**
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 *       将动态列与静态列 合并
	 * @时间：2011-7-8下午03:30:07
	 * @param olditems
	 * @param newitems
	 * @return
	 */
	private ReportItem[] combin(ReportItem[] olditems, ReportItem[] newitems) {
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
	 *        从存货状态表中获得要组装动态列的数据
	 * @时间：2011-7-8上午09:54:00
	 * @return
	 * @throws Exception 
	 */
	private ReportItem[] getNewItems() throws Exception {
		//过滤库存状态为非正常的
		String wheresql=" isnull(tb_stockstate.dr,0)=0 and upper(coalesce(tb_stockstate.isok,'N'))='N'";
		SuperVO[] vos= HYPubBO_Client.queryByCondition(TbStockstateVO.class, wheresql);
		if(vos==null || vos.length==0){
			return null;
		}
		//动态列的元素
		ReportItem[] res=new ReportItem[vos.length*2];
		//动态列对应的库存状态表的主键
		pk_storestates=new String[vos.length];
		int size=vos.length;
		int j = 0;
		splength = size;
		displaynames = new String[size];
		for(int i=0;i<size;i++){
			//无效库存 状态名
			//(String)vos[i].getAttributeValue(displayName)
			//ColumnGroup[] cols=new ColumnGroup[displays.length];
			displaynames[i] = (String)vos[i].getAttributeValue(displayName);
		  ReportItem it=ReportPubTool.getItem(num+(i+1),"主数量",IBillItem.DECIMAL,i, 80);
		  ReportItem it1=ReportPubTool.getItem(bnum+(i+1),"辅数量",IBillItem.DECIMAL,i, 80);
		  res[j]=it;
		  j++;
		  res[j]=it1;
		  pk_storestates[i]=(String) vos[i].getAttributeValue(pk);
		  j++;
		}	
		return res;
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
			
		  	//设置查询模板默认查询条件
	        AccountCalendar  accCal = AccountCalendar.getInstance();     
	        getQueryDlg().setDefaultValue("ddatefrom", accCal.getMonthVO().getBegindate().toString(), "");
	        getQueryDlg().setDefaultValue("ddateto", accCal.getMonthVO().getEnddate().toString(), "");
			getQueryDlg().showModal();
		     if (getQueryDlg().getResult() == UIDialog.ID_OK) {		  
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
               
            	
            	//得到查询条件VOs
            	ConditionVO[] conditionVOs=getQueryDlg().getConditionVO();
            	//从查询对话框,获取仓库主键
            	int size=conditionVOs.length;
            	//仓库主键
            	pk_stordoc=null;
            	for(int i=0;i<size;i++){
            		if(conditionVOs[i].getFieldCode().equalsIgnoreCase("pk_stordoc")){
            			pk_stordoc=conditionVOs[i].getValue();
            		}        		
            	}	
            	 //得到sql查询结果
            	
            	clearBody();
                ReportBaseVO[] vos = getReportVO(getQuerySQL());
                
                if(vos != null){                	
					super.updateBodyDigits();
					ReportBaseVO[] voss=setVoByInvState(vos);
					setReportBaseVO(voss);
					setBodyVO(voss);				
	                setTolal();	                
                }                
	          }
		} catch (Exception e) {
            e.printStackTrace();
            showWarningMessage(e.getMessage());
        }
	}
	private void clearBody() {
		setBodyVO(null);
		updateUI();
	}

	/**
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 *        将vos 按仓库,货位,生产日期,存货 进行分组
	 *        然后存货状态类型 将各个组的数据进行合并
	 * @时间：2011-7-7下午04:46:33
	 * @param vos
	 */
	private ReportBaseVO[] setVoByInvState(ReportBaseVO[] vos) {
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
					UFDouble fnum=PuPubVO.getUFDouble_NullAsZero(vo.getAttributeValue(num));
					newvo.setAttributeValue(num+(k+1), znum.add(fnum));
					//从组合vo中取对应值	
					UFDouble znum1=PuPubVO.getUFDouble_NullAsZero(newvo.getAttributeValue(bnum+(k+1)));
					//从vo中取对应值
					UFDouble fnum1=PuPubVO.getUFDouble_NullAsZero(vo.getAttributeValue(bnum));
					newvo.setAttributeValue(bnum+(k+1), znum1.add(fnum1));
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
			UFDouble inum=PuPubVO.getUFDouble_NullAsZero(newvo.getAttributeValue(bnum+(i+1)));
		   	znum=znum.add(inum);		
		}
		//获得换算率 
		UFDouble hsl1=PuPubVO.getUFDouble_NullAsZero(newvo.getAttributeValue(hsl));
		newvo.setAttributeValue(zton, znum.multiply(hsl1));
		newvo.setAttributeValue(zdai, znum);
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
		//String invclcode,UFBoolean isNormal,String pk_stordoc,String pk_invbasdoc,UFBoolean isType,UFBoolean isInvcl,UFBoolean isShowStore,UFBoolean iscargdoc,UFBoolean isvbanchcode,String ddatefrom,String ddateto){

		return WDSWLReportSql.getQuerySQL(invclcode,new UFBoolean(false),pk_stordoc,null,new UFBoolean(false),new UFBoolean(false),new UFBoolean(false), new UFBoolean(false), new UFBoolean(true), ddatefrom, ddateto);
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
        String[] tolfields=new String[pk_storestates.length*2+2];
        int size=tolfields.length;
        int j = 0;
        for(int i=0;i<pk_storestates.length+2;i++){
        	if(j==size-1){
        		tolfields[j]=zton;
        	}else if(j==size-2){
        		tolfields[j]=zdai;
        	}else{
        		tolfields[j]=num+(i+1);
        		j++;
        		tolfields[j]=bnum+(i+1);
        	}
        	j++;
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

}

