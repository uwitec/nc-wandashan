package nc.bs.pub.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.pub.compiler.AbstractCompiler2;
import nc.bs.pub.compiler.BatchWorkflowRet;
import nc.bs.to.pub.BusinessTypeTool;
import nc.bs.to.pub.CommonDataDMO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pub.vosplit.SplitBillVOs;
import nc.vo.to.pub.BillHeaderVO;
import nc.vo.to.pub.BillItemVO;
import nc.vo.to.pub.BillVO;
import nc.vo.uap.pf.PFBusinessException;


/**
 * 备注：公司间调拨订单的审批
单据动作执行中的动态执行类的动态执行类。
 *
 * 创建日期：(2005-6-24)
 * @author：平台脚本生成
 */
public class N_5X_APPROVE extends AbstractCompiler2 {
private java.util.Hashtable m_methodReturnHas=new java.util.Hashtable();
private Hashtable m_keyHas=null;
/**
 * N_5D_APPROVE 构造子注解。
 */
public N_5X_APPROVE() {
	super();
}

/*
 * 备注：平台编写规则类 接口执行类
 */
public Object runComClass(PfParameterVO vo) throws BusinessException {
		super.m_tmpVo = vo;
		Object retObj = null;
		Object[] inCurObject = getVos();
		Object[] objUser = getUserObjAry();
		if (inCurObject == null)
			/* * @res* "错误：没有数据!" */
			throw new nc.vo.pub.BusinessException(
					nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(
							"topub", "UPPtopub-000091"));
    nc.vo.to.pub.BillVO[] inCurVOs = new BillVO[inCurObject.length];
    for(int i=0,len=inCurObject.length;i<len;i++){
      inCurVOs[i] = (nc.vo.to.pub.BillVO) inCurObject[i];
      if (inCurVOs[i].getIPAdress() == null) {
        inCurVOs[i].setIPAdress(InvocationInfoProxy.getInstance().getClientHost());
      }
    }
    
		if (inCurVOs == null || inCurVOs.length == 0)
			/* * @res * "错误：没有数据!" */
			throw new nc.vo.pub.BusinessException(
					nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(
							"topub", "UPPtopub-000091"));
    
    //补充审批信息
    fillInitData(inCurVOs);
    
    setParameter("INCURVOS", inCurVOs);
		Object alLockedPK = null;
		
		try {

			// 方法说明:单据加业务锁
			alLockedPK = runClass("nc.bs.to.pub.BillImpl", "lockBills",
					"&INCURVOS:nc.vo.pub.AggregatedValueObject[]", vo,
					m_keyHas, m_methodReturnHas);
			if (retObj != null) {
				m_methodReturnHas.put("lockBills", retObj);
			}
			// 检查时间戳
			runClass("nc.bs.to.pub.CommonDataDMO", "checkTimeStamps",
					"&INCURVOS:nc.vo.to.pub.BillVO[]", vo, m_keyHas,
					m_methodReturnHas);
			if (retObj != null) {
				m_methodReturnHas.put("checkTimeStamp", retObj);
			}
			// ####该组件为单动作工作流处理开始...不能进行修改####
			Hashtable  m_sysHasNoPassAndGoing =  procFlowBacth(vo);
			// 工作流中会修改单据状态，取该事务中最新的单据状态
			nc.bs.to.to201.TransferOrderImpl boTrans = new nc.bs.to.to201.TransferOrderImpl();

      //---------------------推式生成调拨出库单的单据的处理begin----------------------目前只能推4Y单据
      //根据是否存在业务流程5X-4331-4Y,判断是否可推式生成下游单据.存在蓝字不可生成，红字可生成
      BusinessTypeTool busiTypeTool = new BusinessTypeTool();
      ArrayList alBusiId_5X43314Y = busiTypeTool.getBusiIds45X43314Y();
      ArrayList alPushVOs = new ArrayList();
      for (int i = 0; i < inCurVOs.length; i++) {
        Object obj = m_sysHasNoPassAndGoing.get(String.valueOf(i));
        // obj == null审批通过，
        if (obj == null) {
          //存在业务流程5X-4331-4Y
          if(alBusiId_5X43314Y.contains(inCurVOs[i].getHeaderVO().getCbiztypeid())) {
            ArrayList retItems = new ArrayList();
            for(int j=0;j<inCurVOs[i].getItemVOs().length;j++){
              if(inCurVOs[i].getItemVOs()[j].getNnum().compareTo(UFDouble.ZERO_DBL)<0){
                retItems.add(inCurVOs[i].getItemVOs()[j]);
              }
            }
            //存在红字，可以推下游
            if(retItems.size()>0){
              BillItemVO[] items = new BillItemVO[retItems.size()];
              items = (BillItemVO[]) retItems.toArray(items);
              BillVO bvo = new BillVO();
              bvo.setParentVO(inCurVOs[i].getHeaderVO());
              bvo.setChildrenVO(items);
              alPushVOs.add(bvo);
            }
          }
          //否则可以推下游
          else{
            alPushVOs.add(inCurVOs[i]);
          }
        }
      }
      //分单
      BillVO[] arPushVOs =splitVO(alPushVOs);
      //将可生成下游单据的VO设置到流程平台中
      this.setVos(arPushVOs);
      vo.m_splitValueVos=arPushVOs;
      
      //由于单据数组长度可能改变，所以userObject也需要长度一直，否则有可能数组业界
      Object[] userAry = new Object[arPushVOs.length];
      if(userAry.length>0){
        userAry[0] = objUser[0];
      }
      
      super.m_tmpVo.m_userObjs = userAry;
      //---------------------推式生成调拨出库单的单据的处理end----------------------

			StringBuffer sbID = new StringBuffer("'");
			for (int i = 0; i < inCurVOs.length; i++) {
				sbID.append(inCurVOs[i].getHeaderVO().getCbillid()).append("','");
			}
			sbID = sbID.delete(sbID.length() - 2, sbID.length());
			BillVO[] vos = new nc.bs.to.pub.BillImpl().queryBillByWhere(
					new String[] { "ctypecode","cbillid", "cauditorid",
							"taudittime", "dauditdate", "ts",
							"fstatusflag" },"cbillid in("+ sbID.toString()+")",
					nc.vo.to.to201.ConstVO201.saQueryBody,
					null);
			// 减少流量
			CommonDataDMO.objectReference(vos);
			/* 写上机日志 */
			boTrans.insertBusilogAfterApprove(inCurVOs);
			
			//liuys 调用afteraction事件
			nc.bs.scm.plugin.InvokeEventProxy iep = new nc.bs.scm.plugin.InvokeEventProxy("to","5X");
			iep.afterAction(nc.vo.scm.plugin.Action.AUDIT, inCurVOs,null);
      /**zpm 加 业务日志**/
      //插入业务日志，该方法可以配置
      setParameter("INCURVO",inCurVOs);
      String sMsg = nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("topub", "UPPtopub-000127")/*信息：成功*/;
      setParameter("ERR",sMsg);
      setParameter("FUN","审批"); /*-=notranslate=-*/
      runClass("nc.bs.to.pub.CheckDMO","insertBusinesslog","&INCURVO:nc.vo.to.pub.BillVO[],&ERR:String,&FUN:String",vo,m_keyHas,m_methodReturnHas);
      /**zpm 加 业务日志**/
			//控制可否进行动作驱动
			BatchWorkflowRet bwr = new BatchWorkflowRet();
			bwr.setNoPassAndGoing(m_sysHasNoPassAndGoing);
			bwr.setUserObj(vos);
			return new Object[]{bwr}; //动作脚本返回值
		} catch (Exception e) {
      /**zpm 加 业务日志**/
      setParameter("INCURVO",inCurVOs);
      setParameter("ERR",e.getMessage());
      setParameter("FUN","审批"); /*-=notranslate=-*/
      runClass("nc.bs.to.pub.CheckDMO","insertBusinessExceptionlog","&INCURVO:nc.vo.to.pub.BillVO[],&ERR:String,&FUN:String",vo,m_keyHas,m_methodReturnHas);
      /**zpm 加 业务日志**/
			throw new PFBusinessException(e.getMessage(), e);
		}
		
}

private void fillInitData(BillVO[] inCurVOs) {
  for(BillVO voAudit : inCurVOs){
    BillHeaderVO voHead = voAudit.getHeaderVO();
    BillItemVO[] voaItem = voAudit.getItemVOs();
    // 放入当前操作员
    voAudit.setOperator(getOperator());
    voHead.setCauditorid(getOperator());
    // 放入当前日期
    voHead.setDauditdate(getUserDate().getDate());
    // 把单据状态置为修改
    voHead.setStatus(VOStatus.UPDATED);
    for (int j = 0; j < voaItem.length; j++) {
      voaItem[j].setStatus(VOStatus.UPDATED);
    }
  }
}

/**
 * 分单
 */
private BillVO[] splitVO(ArrayList alPushVOs) {
  ArrayList ret = new ArrayList();
  //按仓库+部门+是否退回+发运方式对调拨订单分单(生成调拨出)
  String[] saCnd4Y = new String[] { "cinwhid", "cindeptid",
      "ctakeoutwhid", "ctakeoutdeptid", "bretractflag","pk_sendtype" };
  for(int i=0;i<alPushVOs.size();i++){
    BillVO vo = (BillVO) alPushVOs.get(i);
    BillVO[] voaSplits4Y = (BillVO[]) SplitBillVOs.getSplitVO(
        "nc.vo.to.pub.BillVO", "nc.vo.to.pub.BillHeaderVO",
        "nc.vo.to.pub.BillItemVO", vo, null, saCnd4Y);
    ret.addAll(Arrays.asList(voaSplits4Y));
  }
  return (BillVO[]) ret.toArray(new BillVO[ret.size()]);
}

/*
* 备注：平台编写原始脚本
*/
public String getCodeRemark(){
	return "	Object  retObj = null;\nnc.vo.pub.AggregatedValueObject inCurObject = getVo();\nObject objUser = getUserObj();\n//检查传入参数类型是否合法，是否为空。\nif (inCurObject == null)\n/* @res\"错误：单据没有数据\"*/\n        throw new java.rmi.RemoteException(\"Remote Call\",new nc.vo.pub.BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(\"topub\", \"UPPtopub-000090\")));\nif (!(inCurObject instanceof nc.vo.to.pub.BillVO))\n/* @res\"错误：单据类型不匹配\"*/\n        throw new java.rmi.RemoteException(\"Remote Call\",new nc.vo.pub.BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(\"topub\", \"UPPtopub-000089\")));\n//数据合法，把数据转换为调拨单据。\nnc.vo.to.pub.BillVO inCurVO = null;\ninCurVO = (nc.vo.to.pub.BillVO) inCurObject;\n//获取平台传入的参数\nsetParameter(\"INCURVO\", inCurVO);\nif(objUser instanceof nc.vo.scm.pub.session.ClientLink)\n            setParameter(\"CLNK\", (nc.vo.scm.pub.session.ClientLink) objUser);\nelse if(objUser instanceof ArrayList)\n//单据保存时，若满足审批条件会调该审批动作：\n            setParameter(\"CLNK\",(nc.vo.scm.pub.session.ClientLink) (((ArrayList) objUser).get(1)));\nObject alLockedPK = null;\n//返回到前台客户端的VO\nArrayList alRet = new ArrayList();\nalRet.add(inCurVO);\ntry {\n//加锁\n      alLockedPK = runClassCom@\"nc.bs.to.pub.BillImpl\", \"lockBill\",\"&INCURVO:nc.vo.pub.AggregatedValueObject\"@;\n      //检查时间戳\n       runClassCom@\"nc.bs.to.pub.CommonDataDMO\", \"checkTimeStamp\",\"&INCURVO:nc.vo.pub.AggregatedValueObject\"@;\n         //####该组件为单动作工作流处理开始...不能进行修改####\n         procActionFlow(vo);\n        nc.bs.to.to201.TransferOrderImpl boTrans = new nc.bs.to.to201.TransferOrderImpl();\n        boTrans.freshTs(inCurVO);\n        int iPfAppStauts = inCurVO.getHeaderVO().getFstatusflag().intValue();\n        //审批未通过时，释放ATP\n        if (iPfAppStauts == nc.vo.to.pub.ConstVO.IBillStatus_UNCHECKED) {\n         //更新调出库存组织的可用量\n                runClassCom@\"nc.bs.to.outer.ToOutATP\",\"modifyATPWhenCloseBill\",\"&INCURVO:nc.vo.pub.AggregatedValueObject\"@;\n         //更新调入库存组织的可用量\n                runClassCom@\"nc.bs.to.outer.ToInATP\",\"modifyATPWhenCloseBill\",\"&INCURVO:nc.vo.pub.AggregatedValueObject\"@;\n         }\n         //是否最终的审批人审批通过\n         if (iPfAppStauts != nc.vo.to.pub.ConstVO.IBillStatus_PASSCHECK) \n	return alRet;\n         //得到更新规则\n         setParameter(\"INCURVO\", inCurVO);\n         int iAlterRuleFlag = boTrans.getAlterRuleFlag(inCurVO);\n         boolean bIn = true;\n         nc.vo.to.pub.BillVO[] voaSplits = boTrans.getSplitsVO(inCurVO,new UFBoolean(bIn));\n         if (voaSplits != null && voaSplits.length != 0) {\n         //调拨入库单\n             nc.vo.ic.pub.bill.GeneralBillVO voInStore = null;\n             nc.vo.to.pub.BillVO voIn = null;\n             nc.vo.ic.pub.bill.GeneralBillVO voOutStore = null;\n             for (int i = 0; i < voaSplits.length; i++) {\n	//分别处理每张拆分后的单据\n	voIn = voaSplits[i];\n	nc.vo.to.pub.BillItemVO[] voaBillItems = (nc.vo.to.pub.BillItemVO[]) voIn.getChildrenVO();\n	nc.vo.to.pub.BillHeaderVO voInHeader = (nc.vo.to.pub.BillHeaderVO) voIn.getParentVO();\n	Integer iAllocFlag = voInHeader.getFallocflag();\n	if (iAllocFlag == null)\n                             /* @res * \"审批时，出现调拨类型为空\"*/\n	      throw new nc.vo.pub.BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(\"topub\",\"UPPtopub-000100\"));\n	ArrayList alChildToIn = new ArrayList();//生成调拨入的记录\n	ArrayList alChildToOut = new ArrayList();//生成调拨出的记录\n	ArrayList alDM = new ArrayList();//生成发运日计划的记录\n	for (int k = 0; k < voaBillItems.length; k++) {\n	//是否生成发运日计划的标志\n	     boolean bDelivPlanFlag = false;\n	           //负订单不能走发运\n                                  if (bDelivPlanFlag && voaBillItems[k].getNnum().doubleValue() >= 0d)  alDM.add(voaBillItems[k]);\n	   //不走发运，要直接生成调拨入库的记录\n	     if (!bDelivPlanFlag) {\n	         alChildToIn.add(voaBillItems[k]);\n	         alChildToOut.add(voaBillItems[k]);\n	    }\n	}\n	if (alDM.size() != 0) {\n	      nc.vo.to.pub.BillItemVO[] voDMBillItems = new nc.vo.to.pub.BillItemVO[alDM.size()];\n	      voDMBillItems = (nc.vo.to.pub.BillItemVO[]) alDM.toArray(voDMBillItems);\n	      voIn.setChildrenVO(voDMBillItems);\n	      boTrans.setDMReceiver(new nc.vo.to.pub.BillVO[] { voIn });\n	      nc.vo.dm.pub.DMVO voShipping = (nc.vo.dm.pub.DMVO) changeData(voIn, voIn.getBillTypeCode(), \"7D\");\n                                   setParameter(\"PFACTION\", \"PUSHSAVE\");\n                                   setParameter(\"PFBILLTYPE\", \"7D\");\n	      setParameter(\"PFDATE\", getUserDate().toString());\n	      setParameter(\"PFVO\", voShipping);\n	      runClassCom@\"nc.bs.pub.pf.PfUtilBO\",\"processAction\",\"&PFACTION:String,&PFBILLTYPE:String,&PFDATE:String,&PFFLOW:nc.vo.pub.pf.PfUtilWorkFlowVO,&PFVO:nc.vo.pub.AggregatedValueObject, &PFUSEROBJ:Object\"@;\n	}\n	//双方即时更新\n	if (iAlterRuleFlag == nc.vo.to.pub.ConstVO.ALTER_ON_One) {\n	     if (alChildToIn.size() != 0) {\n	       nc.vo.to.pub.BillItemVO[] voICBillItems = new nc.vo.to.pub.BillItemVO[alChildToIn.size()];\n	       voICBillItems = (nc.vo.to.pub.BillItemVO[]) alChildToIn.toArray(voICBillItems);\n	       boTrans.setCustomerInfo(voICBillItems);\n	       voIn.setChildrenVO(voICBillItems);\n	       //直运调拨时，不生成调入单\n	       if (iAllocFlag.intValue() != nc.vo.to.pub.ConstVO.ITransferType_DIRECT) {\n	        //把数据转换为入库单\n	        voInStore = (nc.vo.ic.pub.bill.GeneralBillVO) changeData(voIn, voIn.getBillTypeCode(), \"4E\");\n                                     setParameter(\"PFACTION\", \"PUSHSAVE\");\n                                     setParameter(\"PFBILLTYPE\", \"4E\");\n	        setParameter(\"PFDATE\", getUserDate().toString());\n	        setParameter(\"PFVO\", voInStore);\n                                     runClassCom@\"nc.bs.pub.pf.PfUtilBO\",\"processAction\",\"&PFACTION:String,&PFBILLTYPE:String,&PFDATE:String,&PFFLOW:nc.vo.pub.pf.PfUtilWorkFlowVO,&PFVO:nc.vo.pub.AggregatedValueObject, &PFUSEROBJ:Object\"@;\n	}\n                       }\n                      if (alChildToOut.size() != 0) {\n                            nc.vo.to.pub.BillItemVO[] voICBillItems = new nc.vo.to.pub.BillItemVO[alChildToOut.size()];\n	voICBillItems = (nc.vo.to.pub.BillItemVO[]) alChildToOut.toArray(voICBillItems);\n	boTrans.setCustomerInfo(voICBillItems);\n                             voIn.setChildrenVO(voICBillItems);\n	voOutStore = (nc.vo.ic.pub.bill.GeneralBillVO) changeData(voIn, voIn.getBillTypeCode(), \"4Y\");\n	//调用调拨出库单推式保存动作[PUSHSAVE]\n	setParameter(\"PFACTION\", \"PUSHSAVE\");\n	setParameter(\"PFBILLTYPE\", \"4Y\");\n	setParameter(\"PFDATE\", getUserDate().toString());\n	setParameter(\"PFVO\", voOutStore);\n	runClassCom@\"nc.bs.pub.pf.PfUtilBO\",\"processAction\",\"&PFACTION:String,&PFBILLTYPE:String,&PFDATE:String,&PFFLOW:nc.vo.pub.pf.PfUtilWorkFlowVO,&PFVO:nc.vo.pub.AggregatedValueObject, &PFUSEROBJ:Object\"@;\n                      }\n             }\n      }\n      nc.vo.scm.pub.smart.SmartVO[] svos = new nc.bs.scm.pub.smart.SmartDMO().selectBy(nc.vo.to.pub.BillItemVO.class,nc.vo.to.to201.ConstVO201.saQueryBody,\"cbillid = '\"+ inCurVO.getHeaderVO().getCbillid() + \"'\");\n      inCurVO.setChildrenVO(svos);\n}\n} catch (Exception e) {\n      throw e;\n} finally { //方法说明:单据解业务锁\n    setParameter(\"ALLPK\", (ArrayList) alLockedPK);\n    if (alLockedPK != null)\n           runClassCom@\"nc.bs.to.pub.BillImpl\", \"unlockBill\",\"&INCURVO:nc.vo.pub.AggregatedValueObject\"@;\n}\nreturn alRet;\n"; /*-=notranslate=-*/
  }
/*
* 备注：设置脚本变量的HAS
*/
private void setParameter(String key,Object val)	{
	if (m_keyHas==null){
		m_keyHas=new Hashtable();
	}
	if (val!=null)	{
		m_keyHas.put(key,val);
	}
}
}
