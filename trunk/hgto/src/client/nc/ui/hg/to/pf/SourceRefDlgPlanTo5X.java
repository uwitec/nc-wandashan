package nc.ui.hg.to.pf;

import java.awt.Container;
import java.util.ArrayList;
import java.util.HashSet;

import nc.bd.accperiod.AccountCalendar;
import nc.ui.hg.to.pub.StockNumParaHelper;
import nc.ui.pub.change.PfChangeBO_Client;
import nc.ui.pub.para.SysInitBO_Client;
import nc.ui.scm.pub.sourceref.BillRefListPanel;
import nc.ui.scm.pub.sourceref.SimpBillRefListPanel;
import nc.ui.scm.pub.sourceref.SourceRefDlg;
import nc.vo.bd.period2.AccperiodmonthVO;
import nc.vo.hg.pu.pub.HgPubConst;
import nc.vo.hg.pu.pub.HgPubTool;
import nc.vo.hg.to.pub.StockNumParaVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.to.pub.BillHeaderVO;
import nc.vo.to.pub.BillItemVO;
import nc.vo.to.pub.BillVO;
import nc.vo.to.pub.ConstVO;
import nc.vo.trade.pub.IBillStatus;

public class SourceRefDlgPlanTo5X extends SourceRefDlg{
  /**
	 * 
	 */
	private static final long serialVersionUID = 6805960569720661438L;
//private HashSet refBillIds = new HashSet();
  private BillRefListPanelPlanTo5X doubleListPanel = null;
  private SimpBillRefListPanel simpListPanel = null;
  
  private String soubilltype = null;
  private String sLogCorp = null;
//  private 

  public SourceRefDlgPlanTo5X(
      String pkField, String pkCorp, String operator, String funNode, String queryWhere, String billType, String businessType, String templateId, String currentBillType, String nodeKey, Object userObj, Container parent) {
    super(pkField, pkCorp, operator, funNode, queryWhere, billType, businessType,
        templateId, currentBillType, nodeKey, userObj, parent);
//    refBillIds = ((TransferClientUI)parent).getRefBillIds();
    //打开参照界面时，跟据公司初始化分单方式
    soubilltype = billType;
    sLogCorp = pkCorp;
    initPara(pkCorp);
  }

  
  protected BillRefListPanel createDoubleTableListPanel() {
    if(doubleListPanel == null){
      doubleListPanel = new BillRefListPanelPlanTo5X(getBusinessType(), getBillType(),
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
      simpListPanel = new SimpBillRefListPanelPlanTo5X(getBusinessType(), getBillType(),
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
  
  public RefPlanQueryDlg getQueryConditionClient(){
    return (RefPlanQueryDlg)((BillReferQueryProxyPlanTo5X)super.getQueyDlg()).createOldQryDlg();
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
	   StringBuffer whereb = new StringBuffer();
	   whereb.append(" h.pk_billtype = '"+soubilltype+"'");
	   whereb.append(" and isnull(h.dr,0)=0 and isnull(b.dr,0)=0 ");
	   whereb.append(" and h.csupplycorpid = '"+sLogCorp+"'");
	  
	   AccperiodmonthVO month =  HgPubTool.getCurrentMonth();
	     //zhw modify  12-28  获取会计期间  
	   //得到基准期间方案的日历实例
		 AccountCalendar calendar = AccountCalendar.getInstance();
		 //设置日期
		 String iyear = calendar.getYearVO().getPeriodyear();
		 String imonth = calendar.getMonthVO().getMonth();
		 //zhw modify  12-28  获取会计期间   end
	   if(soubilltype.equalsIgnoreCase(HgPubConst.PLAN_MONTH_BILLTYPE)){
		  // whereb.append(" and h.pk_corp <> '"+sLogCorp+"'");
		   whereb.append(" and h.cmonth = '"+imonth+"'");
		   whereb.append(" and h.cyear = '"+iyear+"'");
		   //zhf modify 2011 02 11  月计划 按平衡数量 发货
		   whereb.append(" and (coalesce(b.nreserve10,0.0)-coalesce(b.nouttotalnum,0.0))>0.0 ");
//		   whereb.append(" and coalesce(b.nnum,0.0)>0.0 "); 
		   whereb.append(" and b.vreserve2 ='Y' ");
	   }else {
		   whereb.append(" and (coalesce(b.nnum,0.0)-coalesce(b.nouttotalnum,0.0))>0.0 ");
		   whereb.append(" and b.dreqdate >= '"+month.getBegindate().toString()+"'");
		   whereb.append(" and b.dreqdate <= '"+month.getEnddate().toString()+"'");
//		   whereb.append(" and coalesce(b.nnum,0.0)>0.0 ");
		   whereb.append(" and h.vbillstatus = "+IBillStatus.CHECKPASS);
	   }
	   return whereb.toString();
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

	   //    //分单操作
	   //    try {
	   //      retBillVos = splitOrderVOs((HYBillVO[])retBillVos,htPara);
	   //    }
	   //    catch (BusinessException e1) {
	   //      showErroMessage(e1.getMessage());
	   //      nc.vo.scm.pub.SCMEnv.out(e1);
	   //    }

	   if(isChangeVo(getBillType(),getCurrentBillType())){
		   try{
			   retBillVos = PfChangeBO_Client.pfChangeBillToBillArray(retBillVos, getBillType(), getCurrentBillType());
			   retBillVos = splitOrderVOs((BillVO[])retBillVos);
			   //获取参量子表信息
			   retBillVos = StockNumParaHelper.getStockNumParaVOs((BillVO[])retBillVos);
		   }catch(Exception e){
			   e.printStackTrace();
			   showErroMessage(HgPubTool.getString_NullAsTrimZeroLen(e.getMessage()));
			   nc.vo.scm.pub.SCMEnv.out(e);
		   }
	   }



	   //将选择的表体id保存到refBillIds中，再次参照时过滤掉已参照单据。注意在保存、删行、取消参照时清空缓存
	   //    pushRefBill();

	   //if(retBillVos!=null && retBillVos.length>0)
	   retBillVo =  retBillVos[0];


	   this.getAlignmentX();
	   this.closeOK();

   }
  
//  private void pushRefBill() {
//    String bid = null;
//    for(int i=0;i<retBillVos.length;i++){
//      for(int j=0;j<retBillVos[i].getChildrenVO().length;j++){
//        bid = ((BillVO)retBillVos[i]).getItemVOs()[j].getCsourcebid();
//        refBillIds.add(bid);
//      }
//    }
//  }

  private String m_splitmode = null;

  private void initPara(String pkCorp){
//    String sReturnValue = null;
    try {
    	m_splitmode = SysInitBO_Client.getParaString(pkCorp, HgPubConst.HG_TO_PARA_01);
    }
    catch (BusinessException e) {
      nc.vo.to.pub.Log.info(e);
    }
    if (m_splitmode != null) {
    	m_splitmode = m_splitmode.trim();
    }
  }

  
  private BillVO[] splitOrderVOs(BillVO[] voaApplication)
  throws BusinessException {
	  
	  if (voaApplication == null || voaApplication.length == 0) {
		    throw new BusinessException(nc.bs.ml.NCLangResOnserver
		        .getInstance().getStrByID("SCMCOMMON",
		            "UPPSCMCommon-000028")/* @res "参数错误" */);
		  }
	  
	  
	  ArrayList alApplication1 = new ArrayList();//非组织内
	  ArrayList alApplication2 = new ArrayList();//组织内
	  for (int i = 0; i < voaApplication.length; i++) {
	    if (voaApplication[i].getHeaderVO().getCoutcbid()==null
	        ||!voaApplication[i].getHeaderVO().getCoutcbid().equals(voaApplication[i].getHeaderVO().getCincbid())) {
	      alApplication1.add(voaApplication[i]);
	    } else {
	      alApplication2.add(voaApplication[i]);
	    }
	  }
	  
	  
//	  String sValue = "分单mode";
	  
	  StringBuffer sbCond1 = new StringBuffer("coutcorpid,coutcbid,cincorpid,cincbid,");
	  sbCond1.append("ctakeoutcorpid,ctakeoutcbid");
	       
	  StringBuffer shCond1 = new StringBuffer("bdrictflag");

	  StringBuffer sbCond2 = new StringBuffer("coutcorpid,coutcbid,coutwhid,");
	  sbCond2.append("cincorpid,cincbid,cinwhid,ctakeoutcorpid,ctakeoutcbid");
	  
	  StringBuffer shCond2 = new StringBuffer("bdrictflag");
	  
	    if (m_splitmode != null) {
	      if (m_splitmode.indexOf(ConstVO.sbillcode) >= 0) {
	        sbCond1.append(",csourceid");
	        sbCond2.append(",csourceid");
	      }
	      if (m_splitmode.indexOf(ConstVO.sBilldate) >= 0) {
	    	  sbCond1.append(",cinvbasid");
		      sbCond2.append(",cinvbasid");
	      }
	      if (m_splitmode.indexOf(ConstVO.sInDept) >= 0) {
	        sbCond1.append(",cindeptid");
	        sbCond2.append(",cindeptid");
	      }
	      if (m_splitmode.indexOf(ConstVO.sOutDept) >= 0) {
	        sbCond1.append(",coutdeptid");
	        sbCond2.append(",coutdeptid");
	      }
	    }
	
  // 按调出公司+调出组织+调入公司+调入组织+调拨类型(或者
  // 调出公司+调出组织+调入公司+调入组织+调出仓库+调入仓库+调拨类型)分单
  String[] saSplitCond1 = sbCond1.toString().split(",");
  String[] saSplitHeadCond1 = null;
  if (shCond1.length() > 0) {
    saSplitHeadCond1 = shCond1.toString().split(",");
  }
  String[] saSplitCond2 = sbCond2.toString().split(",");
  String[] saSplitHeadCond2 = null;
  if (shCond2.length() > 0) {
    saSplitHeadCond2 = shCond2.toString().split(",");
  }

  BillVO[] voaApplication1 = new BillVO[alApplication1.size()];
  voaApplication1 = (BillVO[]) alApplication1
      .toArray(voaApplication1);
  BillVO[] voaApplication2 = new BillVO[alApplication2.size()];
  voaApplication2 = (BillVO[]) alApplication2
      .toArray(voaApplication2);
  // 非组织内调拨订单分单
  BillVO[] voaOrder1 = (BillVO[]) nc.vo.scm.pub.vosplit.SplitBillVOs
      .getSplitVOs("nc.vo.to.pub.BillVO",
          "nc.vo.to.pub.BillHeaderVO",
          "nc.vo.to.pub.BillItemVO", voaApplication1,
          saSplitHeadCond1, saSplitCond1);
  // 组织内调拨订单分单
  BillVO[] voaOrder2 = (BillVO[]) nc.vo.scm.pub.vosplit.SplitBillVOs
      .getSplitVOs("nc.vo.to.pub.BillVO",
          "nc.vo.to.pub.BillHeaderVO",
          "nc.vo.to.pub.BillItemVO", voaApplication2,
          saSplitHeadCond2, saSplitCond2);
  
  int iLen1 = voaOrder1 == null ? 0 : voaOrder1.length;
  int iLen2 = voaOrder2 == null ? 0 : voaOrder2.length;
  BillVO[] voaOrder = new BillVO[iLen1 + iLen2];
  System.arraycopy(voaOrder1, 0, voaOrder, 0, iLen1);
  System.arraycopy(voaOrder2, 0, voaOrder, iLen1, iLen2);

  ArrayList alData = new ArrayList();

  if (voaOrder != null) {
    for (int i = 0; i < voaOrder.length; i++) {
      voaOrder[i].setOperator(voaApplication[0].getOperator());
      BillHeaderVO voHead = voaOrder[i].getHeaderVO();
      BillItemVO[] voaBody = voaOrder[i].getItemVOs();
      if (voaBody != null && voaBody.length != 0) {
        for (int j = 0; j < voaBody.length; j++) {
          // 设置新增订单表体状态
          voaBody[j].setStatus(nc.vo.pub.VOStatus.NEW);
          voaBody[j].setFrowstatuflag(new Integer(ConstVO.IBillStatus_FREE));
        }
//        // ############补充调拨订单的表头信息###########/
//        // 将调入申请的表体调出公司与调出组织放到调拨订单的表头
//        voHead.setCoutcorpid(voaBody[0].getCoutcorpid());
//        voHead.setCoutcbid(voaBody[0].getCoutcbid());
        // 调拨类型
//        voHead.setFallocflag(voaBody[0].getFallocflag());
        // 单据状态(新增订单为自由态)
//        voHead.setFstatusflag(new Integer(ConstVO.IBillStatus_FREE));
//        // 出货公司，出货组织
//        String sTakeOutCorpID = voaBody[0].getCtakeoutcorpid(); // 出货公司
//        String sTakeOutCbID = voaBody[0].getCtakeoutcbid(); // 出货组织
//        if (sTakeOutCorpID != null
//            && sTakeOutCorpID.trim().length() > 0
//            && sTakeOutCbID != null
//            && sTakeOutCbID.trim().length() > 0) {
//          // 将调入申请处理中填入的表体出货公司与出货组织放到调拨订单的表头
//          voHead.setCtakeoutcorpid(sTakeOutCorpID);
//          voHead.setCtakeoutcbid(sTakeOutCbID);
//        }
            alData.add(voaOrder[i]);
      }// ############补充调拨订单的表头信息结束###########/
    }
  }
  // 将分单结果转换为调拨订单数组
    nc.vo.to.pub.BillVO[] voaForDBDD = new nc.vo.to.pub.BillVO[alData.size()];
    voaForDBDD = (nc.vo.to.pub.BillVO[]) alData.toArray(voaForDBDD);


  return voaForDBDD;
}

}
