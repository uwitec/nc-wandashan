package nc.bs.pub.action;

import java.util.Hashtable;

import nc.bs.ic.pub.WriteBackTool;
import nc.bs.pub.compiler.AbstractCompiler2;
import nc.vo.ic.pub.TbGeneralBVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.uap.pf.PFBusinessException;
/**
 *  �˻����
 * @author Administrator
 *
 */
public class N_WDSZ_WRITE extends AbstractCompiler2 {
private java.util.Hashtable m_methodReturnHas=new java.util.Hashtable();
private Hashtable m_keyHas=null;

public N_WDSZ_WRITE() {
	super();
}
/*
* ��ע��ƽ̨��д������
* �ӿ�ִ����
*/
public Object runComClass(PfParameterVO vo) throws BusinessException {


	try {
		super.m_tmpVo = vo;
		Object retObj = null;
		//		��д��Դ����
		TbGeneralBVO[] bodys = (TbGeneralBVO[])getVo().getChildrenVO();
		if(bodys == null || bodys.length == 0)
			throw new BusinessException("��������Ϊ��");

		WriteBackTool.setVsourcebillrowid("cfirstbillbid");
		WriteBackTool.setVsourcebillid("cfirstbillhid");
		WriteBackTool.setVsourcebilltype("cfirsttype");
		WriteBackTool.writeBack(bodys, "so_saleorder_b", "corder_bid", new String[]{"geb_anum"}, new String[]{"ntaldcnum"},new String[]{"nnumber"});
		//		��д����
		retObj = runClass("nc.bs.ic.pub.WdsIcInPubBillSave", "saveBill","nc.vo.pub.AggregatedValueObject:01", vo, m_keyHas,	m_methodReturnHas);
		return retObj;
	} catch (Exception ex) {
		if (ex instanceof BusinessException)
			throw (BusinessException) ex;
		else
			throw new PFBusinessException(ex.getMessage(), ex);
	}

}
/*
* ��ע��ƽ̨��дԭʼ�ű�
*/
public String getCodeRemark(){
	return "	try {\n			super.m_tmpVo = vo;\n			Object retObj = null;\n			// ���漴�ύ\n			retObj = runClass(\"nc.bs.gt.pub.HYBillSave\", \"saveBill\",\"nc.vo.pub.AggregatedValueObject:01\", vo, m_keyHas,	m_methodReturnHas);\n			return retObj;\n		} catch (Exception ex) {\n			if (ex instanceof BusinessException)\n				throw (BusinessException) ex;\n			else\n				throw new PFBusinessException(ex.getMessage(), ex);\n		}\n";}
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
