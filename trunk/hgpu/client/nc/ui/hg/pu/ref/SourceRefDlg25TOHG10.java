package nc.ui.hg.pu.ref;

import java.awt.Container;
import java.util.ArrayList;
import java.util.HashSet;

import nc.ui.hg.pu.pub.LongTimeTask;
import nc.ui.pub.ClientEnvironment;
import nc.ui.scm.pub.sourceref.BillRefListPanel;
import nc.ui.scm.pub.sourceref.SimpBillRefListPanel;
import nc.ui.scm.pub.sourceref.SourceRefDlg;
import nc.vo.hg.pu.invoice.BzbVO;
import nc.vo.hg.pu.invoice.BzhVO;
import nc.vo.hg.pu.pub.HgPubConst;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.pi.InvoiceHeaderVO;
import nc.vo.pi.InvoiceItemVO;
import nc.vo.pi.InvoiceVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.trade.voutils.VOUtil;

public class SourceRefDlg25TOHG10 extends SourceRefDlg{
  /**
	 * 
	 */
	private static final long serialVersionUID = 6805960569720661438L;
//private HashSet refBillIds = new HashSet();
  private BillRefListPanel25TOHG10 doubleListPanel = null;
  private SimpBillRefListPanel simpListPanel = null; 

  public SourceRefDlg25TOHG10(
      String pkField, String pkCorp, String operator, String funNode, String queryWhere, String billType, String businessType, String templateId, String currentBillType, String nodeKey, Object userObj, Container parent) {
    super(pkField, pkCorp, operator, funNode, queryWhere, billType, businessType,
        templateId, currentBillType, nodeKey, userObj, parent);
  }

  
  protected BillRefListPanel createDoubleTableListPanel() {
    if(doubleListPanel == null){
      doubleListPanel = new BillRefListPanel25TOHG10(getBusinessType(), getBillType(),
          getCurrentBillType(), getPkCorp(),getQueryConditionClient()){
       public void  setSourceVOToUI(CircularlyAccessibleValueObject[] srcHeadVOs, 
           CircularlyAccessibleValueObject[] srcBodyVOs) throws BusinessException{
         ArrayList<CircularlyAccessibleValueObject> arysourcebvo = new ArrayList<CircularlyAccessibleValueObject>();
         HashSet hsheadid = new HashSet();
         for(int i=0,loop=srcBodyVOs.length;i<loop;i++){
//           if(refBillIds.contains(srcBodyVOs[i].getAttributeValue("cbill_bid")))
//             continue;
           arysourcebvo.add(srcBodyVOs[i]);
//           hsheadid.add(srcBodyVOs[i].getAttributeValue("cbillid"));
         }
         srcBodyVOs =  arysourcebvo.toArray(new CircularlyAccessibleValueObject[0]);
         arysourcebvo.clear();
         for(int i =0,loop = srcHeadVOs.length;i<loop;i++){
//           if(hsheadid.contains(srcHeadVOs[i].getAttributeValue("cbillid")))
             arysourcebvo.add(srcHeadVOs[i]); 
         }
         srcHeadVOs =  arysourcebvo.toArray(new CircularlyAccessibleValueObject[0]);
         
           
         super.setSourceVOToUI(srcHeadVOs, srcBodyVOs);
       }
      };
    }
     return doubleListPanel;
  }

  /**
   * 返回 SimpBillRefListPanel 特性值。
   * 
   * @return SimpBillRefListPanel
   */
  protected SimpBillRefListPanel createOneTableListPanel() {
    if(simpListPanel == null){
      simpListPanel = new SimpBillRefListPanel25TOHG10(getBusinessType(), getBillType(),
          getCurrentBillType(), getPkCorp(),getQueryConditionClient()){
       public void  setSourceVOToUI(CircularlyAccessibleValueObject[] srcHeadVOs, 
           CircularlyAccessibleValueObject[] srcBodyVOs) throws BusinessException{
         ArrayList<CircularlyAccessibleValueObject> arysourcebvo = new ArrayList<CircularlyAccessibleValueObject>();
         HashSet hsheadid = new HashSet();
         for(int i=0,loop=srcBodyVOs.length;i<loop;i++){
//           if(refBillIds.contains(srcBodyVOs[i].getAttributeValue("cbill_bid")))
//             continue;
           arysourcebvo.add(srcBodyVOs[i]);
//           hsheadid.add(srcBodyVOs[i].getAttributeValue("cbillid"));
         }
         srcBodyVOs =  arysourcebvo.toArray(new CircularlyAccessibleValueObject[0]);
         arysourcebvo.clear();
         for(int i =0,loop = srcHeadVOs.length;i<loop;i++){
//           if(hsheadid.contains(srcHeadVOs[i].getAttributeValue("cbillid")))
             arysourcebvo.add(srcHeadVOs[i]); 
         }
         srcHeadVOs =  arysourcebvo.toArray(new CircularlyAccessibleValueObject[0]);
         
           
         super.setSourceVOToUI(srcHeadVOs, srcBodyVOs);
       }
      };
    }
     return simpListPanel;
  }
  
  public RefHG10QueryDlg getQueryConditionClient(){
    return (RefHG10QueryDlg)((BillReferQueryProxy25TOHG10)super.getQueyDlg()).createOldQryDlg();
  }
  
   protected nc.ui.pub.beans.UIPanel getPanlCmd() {
     if (panlCmd == null) {
         try {
             panlCmd = new nc.ui.pub.beans.UIPanel();
             panlCmd.setName("PanlCmd");
             panlCmd.setPreferredSize(new java.awt.Dimension(0, 40));
             panlCmd.setLayout(new java.awt.FlowLayout());
             
             panlCmd.add(getbtnOk(), getbtnOk().getName());
             panlCmd.add(getbtnCancel(), getbtnCancel().getName());
             panlCmd.add(getbtnQuery(), getbtnQuery().getName());
             panlCmd.add(getbtnSwitch(), getbtnSwitch().getName());
             panlCmd.add(getbtnRefQry(), getbtnRefQry().getName());
             if(isShowSplitButton())
               panlCmd.add(getbtnSplitMode(), getbtnSplitMode().getName());
             
         } catch (java.lang.Throwable ex) {
             handleException(ex);
         }
     }
     return panlCmd;
 }  
   
   /**
    * 是否显示目标单据操作面板.
    * 创建日期:(2001-3-23 2:02:27)
    * @param e ufbill.BillEditEvent
    */
   protected boolean isShowSplitButton(){
     return false;
   }
   
   private String getQueryCondition(){
	   return " isnull(h.dr,0)=0 ";
   }

   
   
   /**
    * 覆盖原方法，主要是根据缓存的已参照过的表体id组织查询条件
    * 过滤已经参照过得表体行
    */
   public void loadHeadData() {
     try {
       
       getbillListPanel().loadBillData(getQueryCondition(), null);
      
       isUpdateUIDataWhenSwitch = true;
       
     } catch (Exception e) {
       nc.vo.scm.pub.SCMEnv.out("数据加载失败！"); 
       nc.vo.scm.pub.SCMEnv.out(e);
       showErroMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000255"));
     }  
   }
  


  /**
   * 此处插入方法说明。 创建日期：(2001-7-6 19:38:38)
   */
  protected void onOk() {
    try{
    
      retBillVos = getbillListPanel().getSelectedSourceVOs();
    
    }catch(BusinessException e){
      showErroMessage(e.getMessage());
      return;
    }
    
    if(retBillVos==null || retBillVos.length<=0){
      showErroMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
          "UPPSCMCommon-000199")/* @res "请选择单据" */);
    return;
    }
    
    //保存转单前vo
    retSrcBillVos = retBillVos;
    retSrcBillVo = retBillVo;
    int len =retBillVos.length;
    BzhVO hvo = new BzhVO();
    BzbVO[] bvos = new BzbVO[len];
    UFDouble noriginalcurmnyh = UFDouble.ZERO_DBL;
	UFDouble noriginaltaxmnyh = UFDouble.ZERO_DBL;
	UFDouble noriginalsummnyh = UFDouble.ZERO_DBL;
	UFDouble noriginalcurmnyhf = UFDouble.ZERO_DBL;
	UFDouble noriginaltaxmnyhf = UFDouble.ZERO_DBL;
	UFDouble noriginalsummnyhf = UFDouble.ZERO_DBL;
	UFDouble noriginalsummnyhf1 = UFDouble.ZERO_DBL;
	UFDouble nbalance =UFDouble.ZERO_DBL;
	String reserve1 = null;
	String reserve2 = null;
	UFDouble ntaxrate = UFDouble.ZERO_DBL;
    for(int i=0;i<len;i++){
    	InvoiceVO ivo=(InvoiceVO)retBillVos[i];
    	InvoiceHeaderVO head = ivo.getHeadVO();
    	UFDouble ninvoicenum = UFDouble.ZERO_DBL;
    	UFDouble noriginalcurmny = UFDouble.ZERO_DBL;
    	UFDouble noriginaltaxmny = UFDouble.ZERO_DBL;
    	UFDouble noriginalsummny = UFDouble.ZERO_DBL;
    	InvoiceItemVO[] items =(InvoiceItemVO[])ivo.getChildrenVO();
    	for(InvoiceItemVO item:items ){
    		ninvoicenum = ninvoicenum.add(item.getNinvoicenum());//发票数量
    		noriginalcurmny =noriginalcurmny.add(item.getNoriginalcurmny());//金额
    		noriginaltaxmny =noriginaltaxmny.add(item.getNoriginaltaxmny());//税额
    		noriginalsummny =noriginalsummny.add(item.getNoriginalsummny());//价税合计
    		ntaxrate =item.getNtaxrate();//税率
    	}
    	BzbVO bvo = new BzbVO();
    	bvo.setNnum(ninvoicenum);//发票数量
    	bvo.setStatus(VOStatus.NEW);//
    	bvo.setCinvoice(head.getVinvoicecode());//发票号
    	bvo.setCbiztype(head.getCbiztype());//业务流程
    	bvo.setCdeptid(head.getCdeptid());//采购部门
    	bvo.setCsourcebillhid(head.getPrimaryKey());
    	bvo.setNtaxtaye(ntaxrate);//税率
    	bvo.setNmny(noriginalcurmny);//金额
    	bvo.setNtaxmny(noriginaltaxmny);//税额
    	bvo.setNtaxtotail(noriginalsummny);//价税合计
    	reserve1 = head.getCvendorbaseid();
    	reserve2= head.getCvendormangid();
    	if(i<10){
    		bvo.setCrowno("0"+(i+1));
    	}else{
    		bvo.setCrowno(PuPubVO.getString_TrimZeroLenAsNull((i+1)));
    	}
    	bvos[i]=bvo;
    	}
    for(BzbVO vo:bvos){
    	if(!HgPubConst.Expenses_Flow.equalsIgnoreCase(vo.getCbiztype())){//如果不是运费流程
    		noriginalcurmnyh =noriginalcurmnyh.add(vo.getNmny());//金额
        	noriginaltaxmnyh =noriginaltaxmnyh.add(vo.getNtaxmny());//税额
        	noriginalsummnyh =noriginalsummnyh.add(vo.getNtaxtotail());//价税合计
    	}else{
    		if(!PuPubVO.getUFDouble_NullAsZero(vo.getNtaxtaye()).equals(UFDouble.ZERO_DBL)){//税率不为零
    			noriginalcurmnyhf =noriginalcurmnyh.add(vo.getNmny());//金额
            	noriginaltaxmnyhf =noriginaltaxmnyh.add(vo.getNtaxmny());//税额
            	noriginalsummnyhf =noriginalsummnyh.add(vo.getNtaxtotail());//价税合计
        	}else{//税率为零
        		noriginalsummnyhf1 =noriginalsummnyhf1.add(vo.getNmny());
    		}
    	}
    		
    }
    hvo.setNbzmny(noriginalcurmnyh);
    hvo.setNbztax(noriginaltaxmnyh);
    hvo.setNtaxtotail(noriginalsummnyh);
    hvo.setNfreightmny(noriginalcurmnyhf);
    hvo.setNfreightaxtotail(noriginalsummnyhf);
    hvo.setNfreighttax(noriginaltaxmnyhf);
    hvo.setNzatax(noriginalsummnyhf1);
    hvo.setReserve1(reserve1);//供应商基本ID
    hvo.setReserve2(reserve2);//供应商管理ID
    String corp = ClientEnvironment.getInstance().getCorporation().getPk_corp();
    try {
    	nbalance = PuPubVO.getUFDouble_NullAsZero(getThreeParameters(reserve1,corp));
	} catch (Exception e) {
		 showErroMessage(e.getMessage());
		e.printStackTrace();
	}
	if(!UFDouble.ZERO_DBL.equals(nbalance)){
		hvo.setNbalance(nbalance);
		hvo.setNreimmny(hvo.getNfreightaxtotail().add(hvo.getNzatax().add(hvo.getNtaxtotail())));
		hvo.setNnoreimmny(hvo.getNbalance().sub(hvo.getNreimmny()));
	}
	
	
    HYBillVO billvo = new HYBillVO();
    billvo.setParentVO(hvo);
    VOUtil.ascSort(bvos, new String[]{"crowno"}); 
    billvo.setChildrenVO(bvos);
   
    retBillVo= billvo;
      this.getAlignmentX();
      this.closeOK();
      
  }
  @Override
public AggregatedValueObject getRetVo() {
	// TODO Auto-generated method stub
	return retBillVo;
}
  @Override
public AggregatedValueObject[] getRetVos() {
	// TODO Auto-generated method stub
	return new AggregatedValueObject[]{retBillVo};
}
   public Object getThreeParameters(String ccustmanid,String corp) throws Exception{
	    
	   Class[] ParameterTypes = new Class[]{String.class,String.class};
		Object[] ParameterValues = new Object[]{ccustmanid,corp};
		Object o = LongTimeTask.callRemoteService("pu","nc.bs.hg.pu.invoice.InvoiceBo", "getThreeParameters", ParameterTypes, ParameterValues, 2);
		return o;
   }
}
