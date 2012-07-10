package nc.bs.pub.action;

import java.util.ArrayList;
import java.util.Hashtable;

import nc.bs.ic.pub.IcInPubBO;
import nc.bs.pub.compiler.AbstractCompiler2;
import nc.bs.wds2.send.AlloInSendBO;
import nc.vo.ic.other.in.OtherInBillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.IBDACTION;
import nc.vo.uap.pf.PFBusinessException;
import nc.vo.wdsnew.pub.BillStockBO1;
import nc.vo.wl.pub.WdsWlPubConst;
/**
 *  �������
 * @author Administrator
 *
 */
public class N_WDS9_WRITE extends AbstractCompiler2 {
	private java.util.Hashtable m_methodReturnHas=new java.util.Hashtable();
	private Hashtable m_keyHas=null;
	private BillStockBO1 stock=null;
	private BillStockBO1 getStock(){
		if(stock==null){
			stock=new BillStockBO1();
		}
		return stock;
	}

	public N_WDS9_WRITE() {
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
			OtherInBillVO bill = (OtherInBillVO) getVo();
			boolean isnew = PuPubVO.getString_TrimZeroLenAsNull(bill.getParentVO().getPrimaryKey())==null?true:false;
			//�������ݻ�д
			IcInPubBO bo=new IcInPubBO();
			bo.writeBackForInBill(bill,IBDACTION.SAVE);	
			//�����ִ���		 
			getStock().updateStockByBill(vo.m_preValueVo, WdsWlPubConst.BILLTYPE_ALLO_IN);
			
			retObj = runClass("nc.bs.trade.comsave.BillSave", "saveBill","nc.vo.pub.AggregatedValueObject:01", vo, m_keyHas,	m_methodReturnHas);
			
//			���ɵ����˵�
			ArrayList retAry = (ArrayList)retObj;
			String headid = PuPubVO.getString_TrimZeroLenAsNull(retAry.get(0));
			new AlloInSendBO().createAlloInSendBill(headid, vo, isnew);
			
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
