package nc.bs.pub.action;

import java.util.Hashtable;
import nc.bs.ic.pub.IcInPubBO;
import nc.bs.pub.compiler.AbstractCompiler2;
import nc.bs.wds.load.account.LoadAccountBS;
import nc.bs.wds.load.pub.CanelDeleteWDF;
import nc.vo.ic.other.in.OtherInBillVO;
import nc.vo.ic.pub.TbGeneralBVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.IBDACTION;
import nc.vo.uap.pf.PFBusinessException;
import nc.vo.wl.pub.WdsWlPubTool;


/**
 *  �������
 * @author Administrator
 *
 */
public class N_WDS7_DELETE extends AbstractCompiler2 {
	private java.util.Hashtable m_methodReturnHas = new java.util.Hashtable();
	private Hashtable m_keyHas = null;


	public N_WDS7_DELETE() {
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
			UFDouble nallnum = WdsWlPubTool.DOUBLE_ZERO;
			for(TbGeneralBVO body:bodys){
				body.validateOnSave();
				nallnum = nallnum.add(PuPubVO.getUFDouble_NullAsZero(body.getGeb_banum()));
			}
//			boolean isNew = false;
//			String geh_pk =((TbGeneralHVO)bill.getParentVO()).getGeh_pk();
//			if(geh_pk == null || "".equals(geh_pk)){==0
//				isNew=true;
//			}
			// ##################################################
			IcInPubBO bo = new IcInPubBO();
			bo.deleteAdjustBill((OtherInBillVO)bill);
			bo.writeBackForInBill((OtherInBillVO)bill, IBDACTION.DELETE, false); //���������[��д������������]
			// ##################################################
			retObj = runClass("nc.bs.trade.comdelete.BillDelete", "deleteBill",
					"nc.vo.pub.AggregatedValueObject:01", vo, m_keyHas,
					m_methodReturnHas);// ����˵��:��ҵ����ɾ��
			// ##################################################
			bo.deleteOtherInforOnDelBill(bill.getParentVO().getPrimaryKey(),bodys);//��д���̣�ɾ�����
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
