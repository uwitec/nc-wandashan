package nc.bs.pub.action;
import java.util.Hashtable;

import nc.bo.other.out.OtherOutBO;
import nc.bs.pub.compiler.AbstractCompiler2;
import nc.vo.ic.other.out.MyBillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.trade.pub.IBDACTION;
import nc.vo.uap.pf.PFBusinessException;
import nc.vo.wdsnew.pub.BillStockBO1;
import nc.vo.wl.pub.WdsWlPubConst;
/**
 * �������ⵥ
 * @author mlr
 */
public class N_WDSH_WRITE extends AbstractCompiler2 {
private java.util.Hashtable m_methodReturnHas=new java.util.Hashtable();
private Hashtable m_keyHas=null;
private BillStockBO1 stock=null;
private BillStockBO1 getStock(){
	if(stock==null){
		stock=new BillStockBO1();
	}
	return stock;
}

public N_WDSH_WRITE() {
	super();
}
/*
* ��ע��ƽ̨��д������
* �ӿ�ִ����
*/
public Object runComClass(PfParameterVO vo) throws BusinessException {

	try {
		super.m_tmpVo = vo;
		MyBillVO billVo = (MyBillVO)getVo();
		if(billVo == null){
			throw new BusinessException("��������Ϊ��");
		}
		billVo.setSLogUser(getOperator());
		billVo.setSLogCorp(vo.m_coId);
		billVo.setULogDate(new UFDate(System.currentTimeMillis()));
		Object retObj = null;
	    //�������ݻ�д
		OtherOutBO bo = new OtherOutBO();
		bo.writeBack(billVo, IBDACTION.SAVE);
		//�����ִ���		 
		getStock().updateStockByBill(vo.m_preValueVo, WdsWlPubConst.BILLTYPE_OTHER_OUT);
		//���е��ݵı������
		retObj = runClass("nc.bs.trade.comsave.BillSave", "saveBill","nc.vo.pub.AggregatedValueObject:01", vo, m_keyHas,m_methodReturnHas);
	
		
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
