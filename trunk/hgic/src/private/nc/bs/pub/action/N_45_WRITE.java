package nc.bs.pub.action;

import java.util.ArrayList;
import java.util.Hashtable;

import nc.bs.hg.ic.pub.HgICPubBO;
import nc.bs.pub.compiler.AbstractCompiler2;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.uap.pf.PFBusinessException;
/**
 * ��ע�����ɹ���ⵥ�ı���
���ݶ���ִ���еĶ�ִ̬����Ķ�ִ̬���ࡣ
 *
 * �������ڣ�(2006-4-10)
 * @author ƽ̨�ű�����
 */
public class N_45_WRITE extends AbstractCompiler2 {
private java.util.Hashtable m_methodReturnHas=new java.util.Hashtable();
private Hashtable m_keyHas=null;
/**
 * N_45_SAVE ������ע�⡣
 */
public N_45_WRITE() {
	super();
}
/*
* ��ע��ƽ̨��д������
* �ӿ�ִ����
*/
public Object runComClass(PfParameterVO vo) throws BusinessException {
try{
	super.m_tmpVo=vo;
	//####���ű����뺬�з���ֵ,����DLG��PNL������������з���ֵ####
//*************��ƽ̨ȡ���ɸö����������ڲ�����������ȡ����Ҫ�����VO��***********
nc.vo.pub.AggregatedValueObject invo=(nc.vo.pub.AggregatedValueObject)getVo();
setParameter("INCURVO",invo);
Object retObj=null;
Object alLockedPK=null;
try{
//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####
//����˵��:������ⵥ�ݼ�ҵ����
alLockedPK=runClass("nc.bs.ic.pub.bill.ICLockBO","lockBill","&INCURVO:nc.vo.pub.AggregatedValueObject",vo,m_keyHas,m_methodReturnHas);
	if (retObj != null) {
		m_methodReturnHas.put("lockBill",retObj);
	}
//�÷���<������>
//����˵��:�����Դ����ʱ���
runClass("nc.bs.ic.pub.check.CheckDMO","checkSourceBillTimeStamp_new","&INCURVO:nc.vo.pub.AggregatedValueObject",vo,m_keyHas,m_methodReturnHas);
	if (retObj != null) {
		m_methodReturnHas.put("checkSourceBillTimeStamp_new",retObj);
	}
//##################################################
	setParameter("BillType","45");
	setParameter("BillDate",getUserDate().toString());
	setParameter("ActionName","SAVEBASE");
	setParameter("P3",null);
	setParameter("P5",getUserObj());
	retObj=runClass("nc.bs.pub.pf.PfUtilBO","processAction","&ActionName:String,&BillType:String,&BillDate:String,&P3:nc.vo.pub.pf.PfUtilWorkFlowVO,&INCURVO:nc.vo.pub.AggregatedValueObject,&P5:Object",vo,m_keyHas,m_methodReturnHas);
	if (retObj != null) {
		m_methodReturnHas.put("processAction",retObj);
	}
	HgICPubBO bo = new HgICPubBO();
	bo.check(invo);
}catch(Exception e){
//############################
//����ҵ����־���÷�����������
setParameter("EXC",e.getMessage());
setParameter("FUN","����");
runClass("nc.bs.ic.pub.check.CheckBO","insertBusinessExceptionlog","&INCURVO:nc.vo.pub.AggregatedValueObject,&EXC:String,&FUN:String",vo,m_keyHas,m_methodReturnHas);
	if (retObj != null) {
		m_methodReturnHas.put("insertBusinessExceptionlog",retObj);
	}
//###########################
     if (e instanceof BusinessException)
			throw (BusinessException) e;
		else
			throw new BusinessException("Remote Call", e);
}
finally{
//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####
//����˵��:������ⵥ�ݽ�ҵ����
setParameter("ALLPK",(ArrayList)alLockedPK);
if(alLockedPK!=null)
runClass("nc.bs.ic.pub.bill.ICLockBO","unlockBill","&INCURVO:nc.vo.pub.AggregatedValueObject,&ALLPK:ArrayList",vo,m_keyHas,m_methodReturnHas);
	if (retObj != null) {
		m_methodReturnHas.put("unlockBill",retObj);
	}
//##################################################
}
return retObj;
//************************************************************************
} catch (Exception ex) {
  throw new PFBusinessException(ex.getMessage(), ex);
}
}
/*
* ��ע��ƽ̨��дԭʼ�ű�
*/
public String getCodeRemark(){
	return "	//####���ű����뺬�з���ֵ,����DLG��PNL������������з���ֵ####\n//*************��ƽ̨ȡ���ɸö����������ڲ�����������ȡ����Ҫ�����VO��***********\nnc.vo.pub.AggregatedValueObject invo=(nc.vo.pub.AggregatedValueObject)getVo();\nsetParameter(\"INCURVO\",invo);\nObject retObj=null;\nObject alLockedPK=null;\ntry{\n//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n//����˵��:������ⵥ�ݼ�ҵ����\nalLockedPK=runClassCom@\"nc.bs.ic.pub.bill.ICLockBO\",\"lockBill\",\"&INCURVO:nc.vo.pub.AggregatedValueObject\"@;\n//�÷���<������>\n//����˵��:�����Դ����ʱ���\nrunClassCom@\"nc.bs.ic.pub.check.CheckDMO\",\"checkSourceBillTimeStamp_new\",\"&INCURVO:nc.vo.pub.AggregatedValueObject\"@;\n//##################################################\n	setParameter(\"BillType\",\"45\");\n	setParameter(\"BillDate\",getUserDate().toString());\n	setParameter(\"ActionName\",\"SAVEBASE\");\n	setParameter(\"P3\",null);\n	setParameter(\"P5\",getUserObj());\n	retObj=runClassCom@\"nc.bs.pub.pf.PfUtilBO\",\"processAction\",\"&ActionName:String,&BillType:String,&BillDate:String,&P3:nc.vo.pub.pf.PfUtilWorkFlowVO,&INCURVO:nc.vo.pub.AggregatedValueObject,&P5:Object\"@;\n}catch(Exception e){\n//############################\n//����ҵ����־���÷�����������\nsetParameter(\"EXC\",e.getMessage());\nsetParameter(\"FUN\",\"����\");\nrunClassCom@\"nc.bs.ic.pub.check.CheckBO\",\"insertBusinessExceptionlog\",\"&INCURVO:nc.vo.pub.AggregatedValueObject,&EXC:String,&FUN:String\"@;\n//###########################\n     if (e instanceof BusinessException)\n			throw (BusinessException) e;\n		else\n			throw new BusinessException(\"Remote Call\", e);\n}\nfinally{\n//####��Ҫ˵�������ɵ�ҵ���������������Ҫ�����޸�####\n//����˵��:������ⵥ�ݽ�ҵ����\nsetParameter(\"ALLPK\",(ArrayList)alLockedPK);\nif(alLockedPK!=null)\nrunClassCom@\"nc.bs.ic.pub.bill.ICLockBO\",\"unlockBill\",\"&INCURVO:nc.vo.pub.AggregatedValueObject,&ALLPK:ArrayList\"@;\n//##################################################\n}\nreturn retObj;\n//************************************************************************\n";}
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

