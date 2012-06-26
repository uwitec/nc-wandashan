package nc.bs.pub.action;

import java.util.Hashtable;
import nc.bs.ic.pub.IcInPubBO;
import nc.bs.ic.pub.WriteBackTool;
import nc.bs.pub.compiler.AbstractCompiler2;
import nc.vo.ic.pub.TbGeneralBVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.uap.pf.PFBusinessException;


/**
 *  �˻����
 * @author Administrator
 *
 */
public class N_WDSZ_DELETE extends AbstractCompiler2 {
	private java.util.Hashtable m_methodReturnHas = new java.util.Hashtable();
	private Hashtable m_keyHas = null;


	public N_WDSZ_DELETE() {
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
			AggregatedValueObject  bill = getVo();
			if(bill == null || bill.getParentVO() == null){
				throw new BusinessException("��������Ϊ��");
			}
			
			TbGeneralBVO[] bodys = (TbGeneralBVO[])bill.getChildrenVO();
			if(bodys == null || bodys.length ==0){
				throw new BusinessException("��������Ϊ��");
			}
			
			// ##################################################
			IcInPubBO bo = new IcInPubBO();
//			bo.deleteAdjustBill((OtherInBillVO)bill);
			
//			�޶���Դ�����ۼ���
			for(TbGeneralBVO body:bodys){
				body.setStatus(VOStatus.DELETED);
			}
			WriteBackTool.setVsourcebillrowid("cfirstbillbid");
			WriteBackTool.setVsourcebillid("cfirstbillhid");
			WriteBackTool.setVsourcebilltype("cfirsttype");
			WriteBackTool.writeBack(bodys, "so_saleorder_b", "corder_bid", new String[]{"geb_anum"}, new String[]{"ntaldcnum"},new String[]{"nnumber"});
//			�޶�����
			
			// ##################################################
			retObj = runClass("nc.bs.trade.comdelete.BillDelete", "deleteBill",
					"nc.vo.pub.AggregatedValueObject:01", vo, m_keyHas,
					m_methodReturnHas);// ����˵��:��ҵ����ɾ��
			// ##################################################
		//	bo.deleteOtherInforOnDelBill(bill.getParentVO().getPrimaryKey(),bodys);//��д���̣�ɾ�����
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
