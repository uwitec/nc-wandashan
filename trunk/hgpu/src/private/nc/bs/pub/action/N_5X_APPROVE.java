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
 * ��ע����˾���������������
���ݶ���ִ���еĶ�ִ̬����Ķ�ִ̬���ࡣ
 *
 * �������ڣ�(2005-6-24)
 * @author��ƽ̨�ű�����
 */
public class N_5X_APPROVE extends AbstractCompiler2 {
private java.util.Hashtable m_methodReturnHas=new java.util.Hashtable();
private Hashtable m_keyHas=null;
/**
 * N_5D_APPROVE ������ע�⡣
 */
public N_5X_APPROVE() {
	super();
}

/*
 * ��ע��ƽ̨��д������ �ӿ�ִ����
 */
public Object runComClass(PfParameterVO vo) throws BusinessException {
		super.m_tmpVo = vo;
		Object retObj = null;
		Object[] inCurObject = getVos();
		Object[] objUser = getUserObjAry();
		if (inCurObject == null)
			/* * @res* "����û������!" */
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
			/* * @res * "����û������!" */
			throw new nc.vo.pub.BusinessException(
					nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(
							"topub", "UPPtopub-000091"));
    
    //����������Ϣ
    fillInitData(inCurVOs);
    
    setParameter("INCURVOS", inCurVOs);
		Object alLockedPK = null;
		
		try {

			// ����˵��:���ݼ�ҵ����
			alLockedPK = runClass("nc.bs.to.pub.BillImpl", "lockBills",
					"&INCURVOS:nc.vo.pub.AggregatedValueObject[]", vo,
					m_keyHas, m_methodReturnHas);
			if (retObj != null) {
				m_methodReturnHas.put("lockBills", retObj);
			}
			// ���ʱ���
			runClass("nc.bs.to.pub.CommonDataDMO", "checkTimeStamps",
					"&INCURVOS:nc.vo.to.pub.BillVO[]", vo, m_keyHas,
					m_methodReturnHas);
			if (retObj != null) {
				m_methodReturnHas.put("checkTimeStamp", retObj);
			}
			// ####�����Ϊ����������������ʼ...���ܽ����޸�####
			Hashtable  m_sysHasNoPassAndGoing =  procFlowBacth(vo);
			// �������л��޸ĵ���״̬��ȡ�����������µĵ���״̬
			nc.bs.to.to201.TransferOrderImpl boTrans = new nc.bs.to.to201.TransferOrderImpl();

      //---------------------��ʽ���ɵ������ⵥ�ĵ��ݵĴ���begin----------------------Ŀǰֻ����4Y����
      //�����Ƿ����ҵ������5X-4331-4Y,�ж��Ƿ����ʽ�������ε���.�������ֲ������ɣ����ֿ�����
      BusinessTypeTool busiTypeTool = new BusinessTypeTool();
      ArrayList alBusiId_5X43314Y = busiTypeTool.getBusiIds45X43314Y();
      ArrayList alPushVOs = new ArrayList();
      for (int i = 0; i < inCurVOs.length; i++) {
        Object obj = m_sysHasNoPassAndGoing.get(String.valueOf(i));
        // obj == null����ͨ����
        if (obj == null) {
          //����ҵ������5X-4331-4Y
          if(alBusiId_5X43314Y.contains(inCurVOs[i].getHeaderVO().getCbiztypeid())) {
            ArrayList retItems = new ArrayList();
            for(int j=0;j<inCurVOs[i].getItemVOs().length;j++){
              if(inCurVOs[i].getItemVOs()[j].getNnum().compareTo(UFDouble.ZERO_DBL)<0){
                retItems.add(inCurVOs[i].getItemVOs()[j]);
              }
            }
            //���ں��֣�����������
            if(retItems.size()>0){
              BillItemVO[] items = new BillItemVO[retItems.size()];
              items = (BillItemVO[]) retItems.toArray(items);
              BillVO bvo = new BillVO();
              bvo.setParentVO(inCurVOs[i].getHeaderVO());
              bvo.setChildrenVO(items);
              alPushVOs.add(bvo);
            }
          }
          //�������������
          else{
            alPushVOs.add(inCurVOs[i]);
          }
        }
      }
      //�ֵ�
      BillVO[] arPushVOs =splitVO(alPushVOs);
      //�����������ε��ݵ�VO���õ�����ƽ̨��
      this.setVos(arPushVOs);
      vo.m_splitValueVos=arPushVOs;
      
      //���ڵ������鳤�ȿ��ܸı䣬����userObjectҲ��Ҫ����һֱ�������п�������ҵ��
      Object[] userAry = new Object[arPushVOs.length];
      if(userAry.length>0){
        userAry[0] = objUser[0];
      }
      
      super.m_tmpVo.m_userObjs = userAry;
      //---------------------��ʽ���ɵ������ⵥ�ĵ��ݵĴ���end----------------------

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
			// ��������
			CommonDataDMO.objectReference(vos);
			/* д�ϻ���־ */
			boTrans.insertBusilogAfterApprove(inCurVOs);
			
			//liuys ����afteraction�¼�
			nc.bs.scm.plugin.InvokeEventProxy iep = new nc.bs.scm.plugin.InvokeEventProxy("to","5X");
			iep.afterAction(nc.vo.scm.plugin.Action.AUDIT, inCurVOs,null);
      /**zpm �� ҵ����־**/
      //����ҵ����־���÷�����������
      setParameter("INCURVO",inCurVOs);
      String sMsg = nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("topub", "UPPtopub-000127")/*��Ϣ���ɹ�*/;
      setParameter("ERR",sMsg);
      setParameter("FUN","����"); /*-=notranslate=-*/
      runClass("nc.bs.to.pub.CheckDMO","insertBusinesslog","&INCURVO:nc.vo.to.pub.BillVO[],&ERR:String,&FUN:String",vo,m_keyHas,m_methodReturnHas);
      /**zpm �� ҵ����־**/
			//���ƿɷ���ж�������
			BatchWorkflowRet bwr = new BatchWorkflowRet();
			bwr.setNoPassAndGoing(m_sysHasNoPassAndGoing);
			bwr.setUserObj(vos);
			return new Object[]{bwr}; //�����ű�����ֵ
		} catch (Exception e) {
      /**zpm �� ҵ����־**/
      setParameter("INCURVO",inCurVOs);
      setParameter("ERR",e.getMessage());
      setParameter("FUN","����"); /*-=notranslate=-*/
      runClass("nc.bs.to.pub.CheckDMO","insertBusinessExceptionlog","&INCURVO:nc.vo.to.pub.BillVO[],&ERR:String,&FUN:String",vo,m_keyHas,m_methodReturnHas);
      /**zpm �� ҵ����־**/
			throw new PFBusinessException(e.getMessage(), e);
		}
		
}

private void fillInitData(BillVO[] inCurVOs) {
  for(BillVO voAudit : inCurVOs){
    BillHeaderVO voHead = voAudit.getHeaderVO();
    BillItemVO[] voaItem = voAudit.getItemVOs();
    // ���뵱ǰ����Ա
    voAudit.setOperator(getOperator());
    voHead.setCauditorid(getOperator());
    // ���뵱ǰ����
    voHead.setDauditdate(getUserDate().getDate());
    // �ѵ���״̬��Ϊ�޸�
    voHead.setStatus(VOStatus.UPDATED);
    for (int j = 0; j < voaItem.length; j++) {
      voaItem[j].setStatus(VOStatus.UPDATED);
    }
  }
}

/**
 * �ֵ�
 */
private BillVO[] splitVO(ArrayList alPushVOs) {
  ArrayList ret = new ArrayList();
  //���ֿ�+����+�Ƿ��˻�+���˷�ʽ�Ե��������ֵ�(���ɵ�����)
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
* ��ע��ƽ̨��дԭʼ�ű�
*/
public String getCodeRemark(){
	return "	Object  retObj = null;\nnc.vo.pub.AggregatedValueObject inCurObject = getVo();\nObject objUser = getUserObj();\n//��鴫����������Ƿ�Ϸ����Ƿ�Ϊ�ա�\nif (inCurObject == null)\n/* @res\"���󣺵���û������\"*/\n        throw new java.rmi.RemoteException(\"Remote Call\",new nc.vo.pub.BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(\"topub\", \"UPPtopub-000090\")));\nif (!(inCurObject instanceof nc.vo.to.pub.BillVO))\n/* @res\"���󣺵������Ͳ�ƥ��\"*/\n        throw new java.rmi.RemoteException(\"Remote Call\",new nc.vo.pub.BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(\"topub\", \"UPPtopub-000089\")));\n//���ݺϷ���������ת��Ϊ�������ݡ�\nnc.vo.to.pub.BillVO inCurVO = null;\ninCurVO = (nc.vo.to.pub.BillVO) inCurObject;\n//��ȡƽ̨����Ĳ���\nsetParameter(\"INCURVO\", inCurVO);\nif(objUser instanceof nc.vo.scm.pub.session.ClientLink)\n            setParameter(\"CLNK\", (nc.vo.scm.pub.session.ClientLink) objUser);\nelse if(objUser instanceof ArrayList)\n//���ݱ���ʱ�������������������������������\n            setParameter(\"CLNK\",(nc.vo.scm.pub.session.ClientLink) (((ArrayList) objUser).get(1)));\nObject alLockedPK = null;\n//���ص�ǰ̨�ͻ��˵�VO\nArrayList alRet = new ArrayList();\nalRet.add(inCurVO);\ntry {\n//����\n      alLockedPK = runClassCom@\"nc.bs.to.pub.BillImpl\", \"lockBill\",\"&INCURVO:nc.vo.pub.AggregatedValueObject\"@;\n      //���ʱ���\n       runClassCom@\"nc.bs.to.pub.CommonDataDMO\", \"checkTimeStamp\",\"&INCURVO:nc.vo.pub.AggregatedValueObject\"@;\n         //####�����Ϊ����������������ʼ...���ܽ����޸�####\n         procActionFlow(vo);\n        nc.bs.to.to201.TransferOrderImpl boTrans = new nc.bs.to.to201.TransferOrderImpl();\n        boTrans.freshTs(inCurVO);\n        int iPfAppStauts = inCurVO.getHeaderVO().getFstatusflag().intValue();\n        //����δͨ��ʱ���ͷ�ATP\n        if (iPfAppStauts == nc.vo.to.pub.ConstVO.IBillStatus_UNCHECKED) {\n         //���µ��������֯�Ŀ�����\n                runClassCom@\"nc.bs.to.outer.ToOutATP\",\"modifyATPWhenCloseBill\",\"&INCURVO:nc.vo.pub.AggregatedValueObject\"@;\n         //���µ�������֯�Ŀ�����\n                runClassCom@\"nc.bs.to.outer.ToInATP\",\"modifyATPWhenCloseBill\",\"&INCURVO:nc.vo.pub.AggregatedValueObject\"@;\n         }\n         //�Ƿ����յ�����������ͨ��\n         if (iPfAppStauts != nc.vo.to.pub.ConstVO.IBillStatus_PASSCHECK) \n	return alRet;\n         //�õ����¹���\n         setParameter(\"INCURVO\", inCurVO);\n         int iAlterRuleFlag = boTrans.getAlterRuleFlag(inCurVO);\n         boolean bIn = true;\n         nc.vo.to.pub.BillVO[] voaSplits = boTrans.getSplitsVO(inCurVO,new UFBoolean(bIn));\n         if (voaSplits != null && voaSplits.length != 0) {\n         //������ⵥ\n             nc.vo.ic.pub.bill.GeneralBillVO voInStore = null;\n             nc.vo.to.pub.BillVO voIn = null;\n             nc.vo.ic.pub.bill.GeneralBillVO voOutStore = null;\n             for (int i = 0; i < voaSplits.length; i++) {\n	//�ֱ���ÿ�Ų�ֺ�ĵ���\n	voIn = voaSplits[i];\n	nc.vo.to.pub.BillItemVO[] voaBillItems = (nc.vo.to.pub.BillItemVO[]) voIn.getChildrenVO();\n	nc.vo.to.pub.BillHeaderVO voInHeader = (nc.vo.to.pub.BillHeaderVO) voIn.getParentVO();\n	Integer iAllocFlag = voInHeader.getFallocflag();\n	if (iAllocFlag == null)\n                             /* @res * \"����ʱ�����ֵ�������Ϊ��\"*/\n	      throw new nc.vo.pub.BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(\"topub\",\"UPPtopub-000100\"));\n	ArrayList alChildToIn = new ArrayList();//���ɵ�����ļ�¼\n	ArrayList alChildToOut = new ArrayList();//���ɵ������ļ�¼\n	ArrayList alDM = new ArrayList();//���ɷ����ռƻ��ļ�¼\n	for (int k = 0; k < voaBillItems.length; k++) {\n	//�Ƿ����ɷ����ռƻ��ı�־\n	     boolean bDelivPlanFlag = false;\n	           //�����������߷���\n                                  if (bDelivPlanFlag && voaBillItems[k].getNnum().doubleValue() >= 0d)  alDM.add(voaBillItems[k]);\n	   //���߷��ˣ�Ҫֱ�����ɵ������ļ�¼\n	     if (!bDelivPlanFlag) {\n	         alChildToIn.add(voaBillItems[k]);\n	         alChildToOut.add(voaBillItems[k]);\n	    }\n	}\n	if (alDM.size() != 0) {\n	      nc.vo.to.pub.BillItemVO[] voDMBillItems = new nc.vo.to.pub.BillItemVO[alDM.size()];\n	      voDMBillItems = (nc.vo.to.pub.BillItemVO[]) alDM.toArray(voDMBillItems);\n	      voIn.setChildrenVO(voDMBillItems);\n	      boTrans.setDMReceiver(new nc.vo.to.pub.BillVO[] { voIn });\n	      nc.vo.dm.pub.DMVO voShipping = (nc.vo.dm.pub.DMVO) changeData(voIn, voIn.getBillTypeCode(), \"7D\");\n                                   setParameter(\"PFACTION\", \"PUSHSAVE\");\n                                   setParameter(\"PFBILLTYPE\", \"7D\");\n	      setParameter(\"PFDATE\", getUserDate().toString());\n	      setParameter(\"PFVO\", voShipping);\n	      runClassCom@\"nc.bs.pub.pf.PfUtilBO\",\"processAction\",\"&PFACTION:String,&PFBILLTYPE:String,&PFDATE:String,&PFFLOW:nc.vo.pub.pf.PfUtilWorkFlowVO,&PFVO:nc.vo.pub.AggregatedValueObject, &PFUSEROBJ:Object\"@;\n	}\n	//˫����ʱ����\n	if (iAlterRuleFlag == nc.vo.to.pub.ConstVO.ALTER_ON_One) {\n	     if (alChildToIn.size() != 0) {\n	       nc.vo.to.pub.BillItemVO[] voICBillItems = new nc.vo.to.pub.BillItemVO[alChildToIn.size()];\n	       voICBillItems = (nc.vo.to.pub.BillItemVO[]) alChildToIn.toArray(voICBillItems);\n	       boTrans.setCustomerInfo(voICBillItems);\n	       voIn.setChildrenVO(voICBillItems);\n	       //ֱ�˵���ʱ�������ɵ��뵥\n	       if (iAllocFlag.intValue() != nc.vo.to.pub.ConstVO.ITransferType_DIRECT) {\n	        //������ת��Ϊ��ⵥ\n	        voInStore = (nc.vo.ic.pub.bill.GeneralBillVO) changeData(voIn, voIn.getBillTypeCode(), \"4E\");\n                                     setParameter(\"PFACTION\", \"PUSHSAVE\");\n                                     setParameter(\"PFBILLTYPE\", \"4E\");\n	        setParameter(\"PFDATE\", getUserDate().toString());\n	        setParameter(\"PFVO\", voInStore);\n                                     runClassCom@\"nc.bs.pub.pf.PfUtilBO\",\"processAction\",\"&PFACTION:String,&PFBILLTYPE:String,&PFDATE:String,&PFFLOW:nc.vo.pub.pf.PfUtilWorkFlowVO,&PFVO:nc.vo.pub.AggregatedValueObject, &PFUSEROBJ:Object\"@;\n	}\n                       }\n                      if (alChildToOut.size() != 0) {\n                            nc.vo.to.pub.BillItemVO[] voICBillItems = new nc.vo.to.pub.BillItemVO[alChildToOut.size()];\n	voICBillItems = (nc.vo.to.pub.BillItemVO[]) alChildToOut.toArray(voICBillItems);\n	boTrans.setCustomerInfo(voICBillItems);\n                             voIn.setChildrenVO(voICBillItems);\n	voOutStore = (nc.vo.ic.pub.bill.GeneralBillVO) changeData(voIn, voIn.getBillTypeCode(), \"4Y\");\n	//���õ������ⵥ��ʽ���涯��[PUSHSAVE]\n	setParameter(\"PFACTION\", \"PUSHSAVE\");\n	setParameter(\"PFBILLTYPE\", \"4Y\");\n	setParameter(\"PFDATE\", getUserDate().toString());\n	setParameter(\"PFVO\", voOutStore);\n	runClassCom@\"nc.bs.pub.pf.PfUtilBO\",\"processAction\",\"&PFACTION:String,&PFBILLTYPE:String,&PFDATE:String,&PFFLOW:nc.vo.pub.pf.PfUtilWorkFlowVO,&PFVO:nc.vo.pub.AggregatedValueObject, &PFUSEROBJ:Object\"@;\n                      }\n             }\n      }\n      nc.vo.scm.pub.smart.SmartVO[] svos = new nc.bs.scm.pub.smart.SmartDMO().selectBy(nc.vo.to.pub.BillItemVO.class,nc.vo.to.to201.ConstVO201.saQueryBody,\"cbillid = '\"+ inCurVO.getHeaderVO().getCbillid() + \"'\");\n      inCurVO.setChildrenVO(svos);\n}\n} catch (Exception e) {\n      throw e;\n} finally { //����˵��:���ݽ�ҵ����\n    setParameter(\"ALLPK\", (ArrayList) alLockedPK);\n    if (alLockedPK != null)\n           runClassCom@\"nc.bs.to.pub.BillImpl\", \"unlockBill\",\"&INCURVO:nc.vo.pub.AggregatedValueObject\"@;\n}\nreturn alRet;\n"; /*-=notranslate=-*/
  }
/*
* ��ע�����ýű�������HAS
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
