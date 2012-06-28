package nc.bs.pub.action;

import java.util.Hashtable;

import nc.bo.other.out.OtherOutBO;
import nc.bs.pub.compiler.AbstractCompiler2;
import nc.bs.wdsnew.pub.BillStockBO1;
import nc.vo.ic.other.out.MyBillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.trade.pub.IBDACTION;
import nc.vo.uap.pf.PFBusinessException;
import nc.vo.wl.pub.WdsWlPubConst;

/**
 * ���۳��ⵥɾ��
 * @author zpm
 * 
 */
public class N_WDS8_DELETE extends AbstractCompiler2 {
	private java.util.Hashtable m_methodReturnHas = new java.util.Hashtable();
	private Hashtable m_keyHas = null;
	private BillStockBO1 stock = null;

	private BillStockBO1 getStock() {
		if (stock == null) {
			stock = new BillStockBO1();
		}
		return stock;
	}

	public N_WDS8_DELETE() {
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
			// ����˵��:��ҵ����ɾ��

			MyBillVO billVo = (MyBillVO) getVo();
			if (billVo == null) {
				throw new BusinessException("��������Ϊ��");
			}
			// �������ݻ�д
			OtherOutBO bo = new OtherOutBO();
			bo.writeBack(billVo, IBDACTION.DELETE);
			// �����ִ���
			getStock().updateStockByBill(vo.m_preValueVo,
					WdsWlPubConst.BILLTYPE_SALE_OUT_1);
			// ���е��ݱ���
			retObj = runClass("nc.bs.trade.comdelete.BillDelete", "deleteBill",
					"nc.vo.pub.AggregatedValueObject:01", vo, m_keyHas,
					m_methodReturnHas);

		
			// CanelDeleteWDF pu=new CanelDeleteWDF();
			// pu.canelDeleteWDF(vo.m_preValueVo, vo.m_operator,
			// vo.m_currentDate);
			// ##################################################
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
