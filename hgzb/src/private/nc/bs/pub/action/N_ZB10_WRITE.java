package nc.bs.pub.action;

import java.util.Hashtable;

import nc.bs.dao.BaseDAO;
import nc.bs.pub.compiler.AbstractCompiler2;
import nc.jdbc.framework.util.SQLHelper;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ValidationException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.uap.pf.PFBusinessException;
import nc.vo.zb.bidding.BiddingHeaderVO;
import nc.vo.zb.cust.CubasdocHgVO;
import nc.vo.zb.pub.ResultSetProcessorTool;

public class N_ZB10_WRITE extends AbstractCompiler2 {
private java.util.Hashtable m_methodReturnHas=new java.util.Hashtable();
private Hashtable m_keyHas=null;
/**
 * 
 *  ��Ӧ��ע�ᱣ��
 *
 */
public N_ZB10_WRITE() {
	super();
}
/*
* ��ע��ƽ̨��д������
* �ӿ�ִ����
*/
public Object runComClass(PfParameterVO vo) throws BusinessException {
try{
	super.m_tmpVo=vo;
	try {
			super.m_tmpVo = vo;
			Object retObj = null;
			HYBillVO billvo = (HYBillVO)getVo();
	             if(billvo ==null)
	            	 throw new ValidationException("�����쳣");
	             CubasdocHgVO head =(CubasdocHgVO)billvo.getParentVO();
	             if(head ==null)
	            	 throw new ValidationException("�����쳣");
	             
			//ֻ��ִ���˱������
			//���ݺ�Ψһ��У��
	             if(PuPubVO.getString_TrimZeroLenAsNull(head.getVbillno())==null)
	            	   throw new ValidationException("��Ӧ�̱��벻��Ϊ��");
			String sql = "select count(0) from bd_cubasdochg where isnull(dr,0) = 0 and vbillno = '"+head.getVbillno().trim()+"' " +
					"and pk_corp = '"+SQLHelper.getCorpPk()+"' and ccubasdochgid != '"+head.getPrimaryKey()+"'";
			BaseDAO dao = new  BaseDAO();
			if(PuPubVO.getInteger_NullAs(dao.executeQuery(sql, ResultSetProcessorTool.COLUMNPROCESSOR), 0)>0)
				throw new BusinessException("��Ӧ�̱����ظ�");
			
			retObj = runClass("nc.bs.zb.pub.HYBillSave", "saveBill","nc.vo.pub.AggregatedValueObject:01", vo, m_keyHas,	m_methodReturnHas);
			
			return retObj;
		} catch (Exception ex) {
			if (ex instanceof BusinessException)
				throw (BusinessException) ex;
			else
				throw new PFBusinessException(ex.getMessage(), ex);
		}
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
	return null;
}
//	return "	try {\n			super.m_tmpVo = vo;\n			Object retObj = null;\n			// ���漴�ύ\n			retObj = runClass(\"nc.bs.gt.pub.HYBillSave\", \"saveBill\",\"nc.vo.pub.AggregatedValueObject:01\", vo, m_keyHas,	m_methodReturnHas);\n			return retObj;\n		} catch (Exception ex) {\n			if (ex instanceof BusinessException)\n				throw (BusinessException) ex;\n			else\n				throw new PFBusinessException(ex.getMessage(), ex);\n		}\n";}
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
