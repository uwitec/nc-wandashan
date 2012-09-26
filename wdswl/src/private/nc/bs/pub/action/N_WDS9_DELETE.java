package nc.bs.pub.action;

import java.util.Hashtable;

import nc.bs.ic.pub.IcInPubBO;
import nc.bs.pub.compiler.AbstractCompiler2;
import nc.bs.wds2.send.AlloInSendBO;
import nc.vo.ic.other.in.OtherInBillVO;
import nc.vo.ic.pub.TbGeneralBVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.trade.pub.IBDACTION;
import nc.vo.uap.pf.PFBusinessException;
import nc.vo.wdsnew.pub.BillStockBO1;
import nc.vo.wl.pub.WdsWlPubConst;

/**
 * 
 * @author mlr
 * 
 */
public class N_WDS9_DELETE extends AbstractCompiler2 {
	private java.util.Hashtable m_methodReturnHas = new java.util.Hashtable();
	private Hashtable m_keyHas = null;

	private BillStockBO1 stock = null;

	private BillStockBO1 getStock() {
		if (stock == null) {
			stock = new BillStockBO1();
		}
		return stock;
	}

	public N_WDS9_DELETE() {
		super();
	}

	/*
	 * ��ע��ƽ̨��д������ �ӿ�ִ����
	 */
	public Object runComClass(PfParameterVO vo) throws BusinessException {
		try {
			super.m_tmpVo = vo;
			// ####���ű����뺬�з���ֵ,����DLG��PNL������������з���ֵ####
			Object retObj = null;
			AggregatedValueObject bill = getVo();
			//�������ݻ�д
			IcInPubBO bo=new IcInPubBO();
			bo.writeBackForInBill((OtherInBillVO) bill,IBDACTION.DELETE);	
	

			// �����ִ���
			getStock()
					.updateStockByBill(bill, WdsWlPubConst.BILLTYPE_ALLO_IN_1);

			if (bill == null || bill.getParentVO() == null) {
				throw new BusinessException("��������Ϊ��");
			}

			TbGeneralBVO[] bodys = (TbGeneralBVO[]) bill.getChildrenVO();
			if (bodys == null || bodys.length == 0) {
				throw new BusinessException("��������Ϊ��");
			}
			

			
			// ����˵��:��ҵ����ɾ��
			retObj = runClass("nc.bs.trade.comdelete.BillDelete", "deleteBill",
					"nc.vo.pub.AggregatedValueObject:01", vo, m_keyHas,
					m_methodReturnHas);
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
	public String getCodeRemark() {
		return "	//####���ű����뺬�з���ֵ,����DLG��PNL������������з���ֵ####\nObject retObj  =null;\n//����˵��:��ҵ����ɾ��\nretObj  =runClassCom@ \"nc.bs.trade.comdelete.BillDelete\", \"deleteBill\", \"nc.vo.pub.AggregatedValueObject:01\"@;\n//##################################################\nreturn retObj;\n";
	}

	/*
	 * ��ע�����ýű�������HAS
	 */
	private void setParameter(String key, Object val) {
		if (m_keyHas == null) {
			m_keyHas = new Hashtable();
		}
		if (val != null) {
			m_keyHas.put(key, val);
		}
	}
}
