/**
 * $文件说明$
 *
 * @author yangb
 * @version 
 * @see
 * @since
 * @time 2008-8-1 下午01:01:16
 */
package nc.ui.ic.pub.pf;

import java.awt.Container;
import java.util.ArrayList;
import java.util.List;

import nc.ui.hg.ic.pub.HgICPubHealper;
import nc.ui.ic.pub.tools.GenMethod;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.change.PfChangeBO_Client;
import nc.ui.querytemplate.IBillReferQuery;
import nc.ui.scm.pub.redun.RedunSourceDlg;
import nc.ui.scm.pub.sourceref.BillRefListPanel;
import nc.ui.scm.pub.sourceref.BillToBillRefPanel;
import nc.ui.scm.pub.sourceref.IBillReferQueryProxy;
import nc.ui.scm.pub.sourceref.SimpBillRefListPanel;
import nc.ui.scm.pub.sourceref.SourceRefDlg;
import nc.ui.scm.pub.sourceref.BillToBillRefPanel.ShowState;
import nc.vo.ic.ic700.Bill53Const;
import nc.vo.ic.pub.BillTypeConst;
import nc.vo.ic.pub.billtype.BillTypeFactory;
import nc.vo.ic.pub.billtype.IBillType;
import nc.vo.ic.pub.billtype.ModuleCode;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.po.OrderItemVO;
import nc.vo.po.OrderVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.bill.BillTempletVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.constant.ScmConst;
import nc.vo.scm.ic.ATPVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.SCMEnv;

/**
 * <p>
 * <b>本类主要完成以下功能：</b>
 * 
 * <ul>
 *  <li>功能条目1
 *  <li>功能条目2
 *  <li>...
 * </ul> 
 *
 * <p>
 * <b>变更历史（可选）：</b>
 * <p>
 * 	 XXX版本增加XXX的支持。
 * <p>
 * <p>
 * @version 本版本号
 * @since 上一版本号
 * @author yangb
 * @time 2008-8-1 下午01:01:16
 */
public class ICSourceRefBaseDlg extends SourceRefDlg {
  
  protected ICSplitModeDlg ivjSplitDlg;
  

  /**
   * ICSourceRefBaseDlg 的构造子
   * @param pkField
   * @param pkCorp
   * @param operator
   * @param funNode
   * @param queryWhere
   * @param billType
   * @param businessType
   * @param templateId
   * @param currentBillType
   * @param parent
   */
  public ICSourceRefBaseDlg(
      String pkField, String pkCorp, String operator, String funNode,
      String queryWhere, String billType, String businessType,
      String templateId, String currentBillType, Container parent) {
    super(pkField, pkCorp, operator, funNode, queryWhere, billType,
        businessType, templateId, currentBillType, parent);
    // TODO 自动生成构造函数存根
  }

  /**
   * ICSourceRefBaseDlg 的构造子
   * @param pkField
   * @param pkCorp
   * @param operator
   * @param funNode
   * @param queryWhere
   * @param billType
   * @param businessType
   * @param templateId
   * @param currentBillType
   * @param nodeKey
   * @param userObj
   * @param parent
   */
  public ICSourceRefBaseDlg(
      String pkField, String pkCorp, String operator, String funNode,
      String queryWhere, String billType, String businessType,
      String templateId, String currentBillType, String nodeKey,
      Object userObj, Container parent) {
    super(pkField, pkCorp, operator, funNode, queryWhere, billType,
        businessType, templateId, currentBillType, nodeKey, userObj, parent);
    // TODO 自动生成构造函数存根
  }

  @Override
  /**
   * 
   */
  /* 警告：此方法将重新生成。 */
  protected BillToBillRefPanel createBillToBillRefPanel() {
//    ICSourceBillToBillBaseUI refpanel = new ICSourceBillToBillBaseUI(getBusinessType(),
//        getBillType(),getCurrentBillType(),getPkCorp(),getOperator());
//    refpanel.createUI(refpanel.createBillRefListPanel(), refpanel.createSimpBillRefListPanel());
//    return refpanel;
    //return new IC4YTo4804UI();
    
    try{

      if(getBillType()!=null){

          IBillType billType = BillTypeFactory.getInstance().getBillType(getBillType());
          if(billType.typeOf(ModuleCode.IC)){

            billtobillrefpanel = new ICSourceBillToBillBaseUI(getBusinessType(),
                getBillType(),getCurrentBillType(),getPkCorp(),getOperator());
            BillRefListPanel billref = createDoubleTableListPanel(); 
            SimpBillRefListPanel simpbillref = createOneTableListPanel();
            
            String pk_billtypecode = billref.getRefTempletBilltypecode();
            if(StringUtil.isEmpty(pk_billtypecode))
              pk_billtypecode = billref.getBillType();
            
            BillTempletVO tvo = billref.getDefaultTemplet(pk_billtypecode, getBusinessType(),
                getOperator(), getPkCorp(),billref.getRefNodeCode());
            billref.createBillListPanel(tvo);
            simpbillref.createBillListPanel(tvo);
            billtobillrefpanel.createUI(billref, simpbillref);
            if(getQueyDlg()!=null && (getQueyDlg() instanceof IBillReferQueryProxy)){
              if(((IBillReferQueryProxy)getQueyDlg()).isShowDoubleTableRef()){
                billtobillrefpanel.switchShow(ShowState.DoubleTable);
              }else{
                billtobillrefpanel.switchShow(ShowState.OneTable);
              }
            }
            
            billref.getSourcectl().setRefdlg(this);
            simpbillref.getSourcectl().setRefdlg(this);
            return billtobillrefpanel;
          }
      }
      return super.createBillToBillRefPanel();

    }catch(Exception e){
      handleException(e);
      return null;
    }
  }

  @Override
  protected BillRefListPanel createDoubleTableListPanel() {
    // TODO 自动生成方法存根
    try{
      if(getBillType()!=null){
        IBillType billType = BillTypeFactory.getInstance().getBillType(getBillType());
        if(billType.typeOf(ModuleCode.IC))
          return new ICBillRefListPanel((ICSourceBillToBillBaseUI)getBilltobillrefpanel());
        
        if(ATPVO.isTOOrder(getBillType())){
            return (BillRefListPanel)Class.forName("nc.ui.to.icredun.DoubleBillRefListPanel5XTo4Y").getConstructor(new Class[]{
                String.class, String.class, String.class, String.class }).newInstance(new Object[]{
                    getBusinessType(), getBillType(), getCurrentBillType(), getPkCorp()
                });
        }
        
        if(BillTypeConst.SO_Order.equals(getBillType())){
          BillRefListPanel refp = (BillRefListPanel)Class.forName("nc.ui.so.pub.query.SaleOrderBillRefListPanel").getConstructor(
              new Class[]{
                  String.class, String.class,String.class, String.class,IBillReferQueryProxy.class
              }).newInstance(new Object[]{
                  getBusinessType(), getBillType(), getCurrentBillType(), getPkCorp(),getQueyDlg()
              });
              return refp;
        }
        
        if(BillTypeConst.SO_Invoice.equals(getBillType())){
          BillRefListPanel refp = (BillRefListPanel)Class.forName("nc.ui.so.so002.pf.SaleInvoiceBillRefListPanel").getConstructor(
              new Class[]{
                  String.class, String.class,String.class, String.class,IBillReferQueryProxy.class
              }).newInstance(new Object[]{
                  getBusinessType(), getBillType(), getCurrentBillType(), getPkCorp(),getQueyDlg()
              });
              return refp;
        }
        
        if(BillTypeConst.PO_ARRIVE.equals(getBillType())){
          BillRefListPanel refp = (BillRefListPanel)Class.forName("nc.ui.sourceref.ar.BillRefListPanelRcToStore").getConstructor(new Class[]{
              String.class, String.class, String.class, String.class,IBillReferQuery.class
          }).newInstance(new Object[]{
              getBusinessType(), getBillType(), getCurrentBillType(), getPkCorp(),getQueyDlg()
          });
          return refp;
        }
        
        if(BillTypeConst.SC_Order.equals(getBillType())){
          BillRefListPanel refp = (BillRefListPanel)Class.forName("nc.ui.sourceref.sc.BillRefListPanelSCToStore").getConstructor(new Class[]{
              String.class, String.class, String.class, String.class,IBillReferQuery.class
          }).newInstance(new Object[]{
              getBusinessType(), getBillType(), getCurrentBillType(), getPkCorp(),getQueyDlg()
          });
          return refp;
        }
        
        if(BillTypeConst.PO_Order.equals(getBillType())){
          BillRefListPanel refp = (BillRefListPanel)Class.forName("nc.ui.sourceref.po.BillRefListPanelPoToStore").getConstructor(new Class[]{
              String.class, String.class, String.class, String.class,IBillReferQuery.class
          }).newInstance(new Object[]{
              getBusinessType(), getBillType(), getCurrentBillType(), getPkCorp(),getQueyDlg()
          });
          return refp;
        }
        
        if(BillTypeConst.PO_MaterialAppBill.equals(getBillType())){
          BillRefListPanel refp = (BillRefListPanel)Class.forName("nc.ui.sourceref.mr.BillRefListPanelMrToOutStore").getConstructor(new Class[]{
              String.class, String.class, String.class, String.class,IBillReferQuery.class
          }).newInstance(new Object[]{
              getBusinessType(), getBillType(), getCurrentBillType(), getPkCorp(),getQueyDlg()
          });
          return refp;
        }
        
      }
    }catch(Exception e){
      handleException(e);
      return null;
    }
    return super.createDoubleTableListPanel();
  }

  @Override
  protected SimpBillRefListPanel createOneTableListPanel() {
    // TODO 自动生成方法存根
    try{
      if(getBillType()!=null){
        IBillType billType = BillTypeFactory.getInstance().getBillType(getBillType());
        if(billType.typeOf(ModuleCode.IC))
          return new ICSimpBillRefListPanel((ICSourceBillToBillBaseUI)getBilltobillrefpanel());
        
        if(ATPVO.isTOOrder(getBillType())){
            return (SimpBillRefListPanel)Class.forName("nc.ui.to.icredun.SimpBillRefListPanel5XTo4Y").getConstructor(new Class[]{
                String.class, String.class, String.class, String.class }).newInstance(new Object[]{
                    getBusinessType(), getBillType(), getCurrentBillType(), getPkCorp()
                });
        }
        
        if(BillTypeConst.SO_Order.equals(getBillType())){
              SimpBillRefListPanel refp = (SimpBillRefListPanel)Class.forName("nc.ui.so.pub.query.SaleOrderSimpBillRefListPanel").getConstructor(
              new Class[]{
                  String.class, String.class,String.class, String.class,IBillReferQueryProxy.class
              }).newInstance(new Object[]{
                  getBusinessType(), getBillType(), getCurrentBillType(), getPkCorp(),getQueyDlg()
              });
              return refp;
        }
        
        if(BillTypeConst.SO_Invoice.equals(getBillType())){
          SimpBillRefListPanel refp = (SimpBillRefListPanel)Class.forName("nc.ui.so.so002.pf.SaleInvoiceSimpBillRefListPanel").getConstructor(
          new Class[]{
              String.class, String.class,String.class, String.class,IBillReferQueryProxy.class
          }).newInstance(new Object[]{
              getBusinessType(), getBillType(), getCurrentBillType(), getPkCorp(),getQueyDlg()
          });
          return refp;
        }
        
        if(BillTypeConst.PO_ARRIVE.equals(getBillType())){
          SimpBillRefListPanel refp = (SimpBillRefListPanel)Class.forName("nc.ui.sourceref.ar.SimpBillRefPanelRcToStore").getConstructor(new Class[]{
              String.class, String.class, String.class, String.class,IBillReferQuery.class
          }).newInstance(new Object[]{
              getBusinessType(), getBillType(), getCurrentBillType(), getPkCorp(),getQueyDlg()
          });
          return refp;
        }
        
        if(BillTypeConst.SC_Order.equals(getBillType())){
          SimpBillRefListPanel refp = (SimpBillRefListPanel)Class.forName("nc.ui.sourceref.sc.SimpBillRefPanelSCToStore").getConstructor(new Class[]{
              String.class, String.class, String.class, String.class,IBillReferQuery.class
          }).newInstance(new Object[]{
              getBusinessType(), getBillType(), getCurrentBillType(), getPkCorp(),getQueyDlg()
          });
          return refp;
        }
        
        if(BillTypeConst.PO_Order.equals(getBillType())){
          SimpBillRefListPanel refp = (SimpBillRefListPanel)Class.forName("nc.ui.sourceref.po.SimpBillRefPanelPoToStore").getConstructor(new Class[]{
              String.class, String.class, String.class, String.class,IBillReferQuery.class
          }).newInstance(new Object[]{
              getBusinessType(), getBillType(), getCurrentBillType(), getPkCorp(),getQueyDlg()
          });
          return refp;
        }
        
        if(BillTypeConst.PO_MaterialAppBill.equals(getBillType())){
          SimpBillRefListPanel refp = (SimpBillRefListPanel)Class.forName("nc.ui.sourceref.mr.SimpBillRefPanelMrToOutStore").getConstructor(new Class[]{
              String.class, String.class, String.class, String.class,IBillReferQuery.class
          }).newInstance(new Object[]{
              getBusinessType(), getBillType(), getCurrentBillType(), getPkCorp(),getQueyDlg()
          });
          return refp;
        }
        
      }
    }catch(Exception e){
      handleException(e);
      return null;
    }
    return new ICSimpBillRefListPanel((ICSourceBillToBillBaseUI)getBilltobillrefpanel());
  }

  @Override
  protected boolean isShowInvHandPane() {
    // TODO 自动生成方法存根
    if(getBillType()!=null){
      if( BillTypeFactory.getInstance().getBillType(getBillType()).typeOf(ModuleCode.PO)
    		  ||BillTypeFactory.getInstance().getBillType(getBillType()).typeOf(ModuleCode.SC))
        return false;
    }
    if(Bill53Const.cbilltype.equals(getCurrentBillType()))
      return false;
    return super.isShowInvHandPane();
  }

  @Override
  protected boolean isShowSplitButton() {
    if(Bill53Const.cbilltype.equals(getCurrentBillType()))
      return false;
    return true;
  }

 
//  @Override
//  protected String getRefNodeCode() {
//    // TODO 自动生成方法存根
//    String cnodekey = "REFTEMPLET";
//    if(BillTypeConst.m_allocationOut.equals(getBillType()) && "4804".equals(getCurrentBillType())){
//      cnodekey = "4YREF4804";
//    }
//    return cnodekey;
//  }
  
  /**
   * get。
   * 创建日期：(2001-5-9 8:52:00)
   * @param row int
   */

  public ICSplitModeDlg getICSplitModeDlg(){
    if(ivjSplitDlg==null){
      ivjSplitDlg = new ICSplitModeDlg(this);
    }
    return ivjSplitDlg;
  }
  
  /**
  *
  */
  @Override
  protected void onBtnSplitMode() {
   getICSplitModeDlg().showModal();
 }
  
  /**
   * 此处插入方法说明。 创建日期：(2001-4-23 9:17:37)
   */
  public void loadHeadData() {
    
    try {
      //清现存量
      //showInvOnhand(null);
      //返回查询条件
      
      //Object[] reObjects = getbillListPanel().getSourcectl().queryAllData(m_whereStr);
      
//      Object[] reObjects = (Object[])LongTimeTask.procclongTime(this,
//          nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000190")/*@res "请等待，正在查询..."*/,
//          1000,-1,getbillListPanel().getSourcectl().getClass().getName(),
//          getbillListPanel(),"loadBillData",new Class[]{String.class,Object.class},new Object[]{m_whereStr,null});
      super.loadHeadData();
      isUpdateUIDataWhenSwitch = true;
      
    } catch (Exception e) {
      GenMethod.handleException(this, 
          nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON","UPPSCMCommon-000255")/*@res "数据加载失败"*/, e);
    }  
    
  }
  
  
  /**
   * 此处插入方法说明。 创建日期：(2001-7-6 19:38:38)
   */
  @Override
  protected void onOk() {
    try{
      retBillVos = null;
      retBillVo = null;
      retSrcBillVos = null;
      retSrcBillVo = null;
    
      retBillVos = getSelectedSourceVOs();//getbillListPanel().getSelectedSourceVOs();
      // modify by zhw 2011-04-15 按照合同上的总量进行容差控制
      if (ScmConst.PO_Order.equalsIgnoreCase(getBillType())) {
		   if (retBillVos == null || retBillVos.length == 0){
			   showErroMessage("没有选中数据");
			   return;
		   }
		   int len = retBillVos.length;
		   for (int i = 0; i < len; i++) {
				OrderVO vo = (OrderVO) retBillVos[i];
				OrderItemVO[] items = vo.getBodyVO();
				int itemlen= items.length;
				ArrayList<OrderItemVO> al = new ArrayList<OrderItemVO>();
				for (int j= 0; j< itemlen; j++) {
					String pk_invbasdoc = items[j].getCbaseid();
					UFDouble allowance = PuPubVO.getUFDouble_NullAsZero(HgICPubHealper.getAllowanceSet(pk_invbasdoc));
					if(UFDouble.ZERO_DBL.compareTo(allowance)!=0){
						UFDouble orderNum= PuPubVO.getUFDouble_NullAsZero(items[j].getNordernum());
						UFDouble  allnum= PuPubVO.getUFDouble_NullAsZero(items[j].getNaccumstorenum());
						UFDouble  sub= PuPubVO.getUFDouble_NullAsZero(allowance.div(100).multiply(orderNum).sub(allnum));
                        items[j].setNnotstorenum(sub);	  
					}
				    if(UFDouble.ZERO_DBL.compareTo(PuPubVO.getUFDouble_NullAsZero(items[j].getNnotstorenum()))<0){
				    	al.add(items[j]);
				    }
				}
				int  size =al.size();
				OrderItemVO[] itemvos = new OrderItemVO[size];
				for(int n=0;n<size;n++){
					itemvos[n]=al.get(n);
				}
				vo.setChildrenVO(itemvos);
				retBillVos[i]=vo;
		   }

	 }
      retSrcBillVos = retBillVos;
      
      if(Bill53Const.cbilltype.equals(getCurrentBillType()))
        retBillVos = PfChangeBO_Client.pfChangeBillToBillArray(retBillVos, getBillType(), getCurrentBillType());
      else
        retBillVos = ICDefaultSrcRefCtl.getTargetVOsForUIRef(retBillVos,getBillType() , getCurrentBillType(), 
          ((BillRefListPanel)getbillListPanel()).getHeadBillModel().getFormulaParse(), 
          getICSplitModeDlg().getIC035());
    
    }catch(Exception e){
      showErroMessage(e.getMessage());
      return;
    }
    
    if(retBillVos==null || retBillVos.length<=0){
      showErroMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
          "UPPSCMCommon-000199")/* @res "请选择单据" */);
    return;
    }
    
    retBillVo =  retBillVos[0];
    
    
    retSrcBillVo = retSrcBillVos[0];
    
      this.getAlignmentX();
      this.closeOK();
      
  }
  
  public AggregatedValueObject[] getSelectedSourceVOs() throws BusinessException{
    
    AggregatedValueObject[] retAggVos = getbillListPanel().getSelectedSourceVOs();
    
    if(retAggVos==null || retAggVos.length<=0)
      return retAggVos;
    
    if(getCurrentBillType().equals(BillTypeConst.m_allocationOut) && (getBillType().equals(BillTypeConst.m_sBillZZNDBDD)
        || getBillType().equals(BillTypeConst.m_sBillSFDBDD) || getBillType().equals(BillTypeConst.m_sBillGSJDBDD)
      || getBillType().equals(BillTypeConst.m_sBillZZJDBDD))){
      
      String cbillid = null;
      CircularlyAccessibleValueObject vo = null;
      CircularlyAccessibleValueObject[] bodyvos = null;
      for(int i=0;i<retAggVos.length;i++){
        try{
          cbillid = retAggVos[i].getParentVO().getPrimaryKey();
          vo = ((BillRefListPanel)getbillListPanel()).getHeadSourceVoFromBuffer(cbillid);
          if(vo!=null)
            retAggVos[i].setParentVO(vo);
          bodyvos = retAggVos[i].getChildrenVO();
          for(int k=0;k<bodyvos.length;k++){
            vo = ((BillRefListPanel)getbillListPanel()).getBodySourceVoFromBuffer(cbillid, bodyvos[k].getPrimaryKey());
            if(vo!=null)
              bodyvos[k] = vo;
          }
        }catch(Exception e){
          SCMEnv.error(e.getMessage());
        }
      }
    }
    
    return retAggVos; 
  }
  
  /**
   * 子菜单按钮响应事件调用该方法，用于
   * <li>1.查询来源单据
   * <li>2.显示来源单据，并进行选择
   * <li>3.获取选择的来源单据
   */
  public static void childButtonClicked(ButtonObject bo, String pkCorp, String FunNode,
      String pkOperator, String currentBillType, Container parent) {
    srcBillType = null;
    tgtBillType = currentBillType;
    String[] rets = getBillTypeAndBiz(bo);
    if(rets!=null && rets.length>0)
      srcBillType = rets[0];
    if(isUseSourceRefDlg(srcBillType,currentBillType)){
      SourceRefDlg.childButtonClicked(bo, pkCorp, FunNode, pkOperator, currentBillType, parent);
    }else{
      RedunSourceDlg.childButtonClicked(bo, pkCorp, FunNode, pkOperator, currentBillType, parent);
    }
    
  }
  
  private static String srcBillType;
  private static String tgtBillType;
  
  public static AggregatedValueObject[] getRetsVos() {
    if(isUseSourceRefDlg(srcBillType,tgtBillType))
      return SourceRefDlg.getRetsVos();
    else
      return RedunSourceDlg.getRetsVos();
  }
  
  /*
   * 
   */ 
  public static boolean makeFlag() {
    if (isUseSourceRefDlg(srcBillType,tgtBillType)) {
      return nc.ui.scm.pub.sourceref.SourceRefDlg.makeFlag();
    }else{
      return RedunSourceDlg.makeFlag();
    }
  }
  
  public static boolean isUseSourceRefDlg(String srcBillType,String targetBillType) {
    if("30".equals(srcBillType) && "4C".equals(targetBillType))
      return true;
    if("32".equals(srcBillType) && "4C".equals(targetBillType))
      return true;
    if("4Y".equals(srcBillType) && "4E".equals(targetBillType))
      return true;
    if(ATPVO.isTOOrder(srcBillType) &&  "4Y".equals(targetBillType))
      return true;
    if(("4Y".equals(srcBillType) || "4C".equals(srcBillType)) && "4453".equals(targetBillType))
      return true;
    if(BillTypeConst.PO_ARRIVE.equals(srcBillType) && "45".equals(targetBillType))
      return true;
    if(BillTypeConst.PO_Order.equals(srcBillType) && "45".equals(targetBillType))
      return true;
    if(BillTypeConst.PO_MaterialAppBill.equals(srcBillType) && "4D".equals(targetBillType))
      return true;
    if(BillTypeConst.SC_Order.equals(srcBillType) && "47".equals(targetBillType))
      return true;
    if(BillTypeConst.PO_ARRIVE.equals(srcBillType) && "47".equals(targetBillType))
        return true;
    return false;
  }
  
  

//  @Override
//  public boolean isChangeVo(String sourceBilltype, String targetBilltype) {
//    // TODO 自动生成方法存根
//    
//    IBillType sbillType = BillTypeFactory.getInstance().getBillType(sourceBilltype);
//    IBillType tbillType = BillTypeFactory.getInstance().getBillType(targetBilltype);
//    if(sbillType.typeOf(ModuleCode.IC) && tbillType.typeOf(ModuleCode.IC))
//      return false;
//    
//    return super.isChangeVo(sourceBilltype, targetBilltype);
//  }
  
//  /**
//   * 返回 用户选择VO数组或交换过的VO数组
//   * 
//   * @return
//   */
//  public static AggregatedValueObject[] getRetsVos(String srcBillType,String destBillType,FormulaParse fp,String IC035) {
//    AggregatedValueObject[] sourcebillvos = SourceRefDlg.getRetSrcVos();
//    if(sourcebillvos!=null && sourcebillvos.length>0){
//      return ICDefaultSrcRefCtl.getTargetVOsForUIRef(sourcebillvos,srcBillType , destBillType, fp, IC035);
//    }
//    AggregatedValueObject[] targetbillvos = SourceRefDlg.getRetsVos();
//    return ICDefaultSrcRefCtl.splitTargetVOs(srcBillType,destBillType,fp,IC035);
//  }
  
  public static boolean isCloseOK() {
	    if (isUseSourceRefDlg(srcBillType,tgtBillType)) {
	      return nc.ui.scm.pub.sourceref.SourceRefDlg.isCloseOK();
	    }else{
	      return RedunSourceDlg.isCloseOK();
	    }
	  }

}
