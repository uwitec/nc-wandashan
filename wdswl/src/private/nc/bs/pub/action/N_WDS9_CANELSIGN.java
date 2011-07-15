package nc.bs.pub.action;

import java.util.ArrayList;
import java.util.Hashtable;

import nc.bs.ic.pub.IcInPubBO;
import nc.bs.pub.compiler.AbstractCompiler2;
import nc.vo.ic.other.in.OtherInBillVO;
import nc.vo.ic.pub.TbGeneralHVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.trade.pub.IBDACTION;
import nc.vo.uap.pf.PFBusinessException;

/**
 *  ������ⵥȡ��ǩ��
 * @author zpm
 */
public class N_WDS9_CANELSIGN extends AbstractCompiler2 {
	private java.util.Hashtable m_methodReturnHas=new java.util.Hashtable();
	private Hashtable m_keyHas=null;

	public N_WDS9_CANELSIGN() {
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
			String date = null;
			String operate = null;
			ArrayList<String> list = (ArrayList<String>)vo.m_userObj;
			if(list != null && list.size()>0){
				 date = list.get(0);
				 operate = list.get(1);
			}
			
			OtherInBillVO bill = (OtherInBillVO)getVo();
			if(bill == null||bill.getHeaderVo() == null||bill.getChildrenVO()==null||bill.getChildrenVO().length ==0)
				throw new BusinessException("�������ݷǷ�");
			
			// ##################################################
			setParameter("AggObj",vo.m_preValueVo);
			setParameter("operate",operate);
			setParameter("date", date);
			AggregatedValueObject[] icBillVO = (AggregatedValueObject[]) runClass("nc.bs.wds.ic.allocation.in.ChangeTo4E", "canelSignQueryGenBillVO",
					"&AggObj:nc.vo.pub.AggregatedValueObject,&operate:String,&date:String", vo, m_keyHas,m_methodReturnHas);
			// ##################################################
			setParameter("AggObject",icBillVO);
			retObj = runClass("nc.bs.wds.ic.allocation.in.AllocationInBO", "canelPushSign4E",
					"&date:String,&AggObject:nc.vo.pub.AggregatedValueObject[]", vo, m_keyHas,m_methodReturnHas);
			// ##################################################����[�������]ȡ��ǩ������
			TbGeneralHVO headvo = (TbGeneralHVO)vo.m_preValueVo.getParentVO();
			setParameter("hvo", headvo);
			runClass("nc.bs.wds.ic.allocation.in.AllocationInBO", "updateHVO",
					"&hvo:nc.vo.ic.pub.TbGeneralHVO", vo, m_keyHas,m_methodReturnHas);
			
//			zhf add  ȡ��ǩ�ֺ�  erp�ĵ�����ⵥ��ɾ��  ����ն�erp��Դ��������ת�������Ļ�д  ��������ϵͳ�ĵ������Դ���ת�������������
			IcInPubBO bo = new IcInPubBO();
			bo.writeBackForInBill(bill, IBDACTION.SAVE, true);		
			
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