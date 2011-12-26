package nc.bs.pub.action;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import nc.bs.pub.compiler.AbstractCompiler2;
import nc.bs.wds.load.account.LoadAccountBS;
import nc.bs.wds.load.pub.PushSaveWDSF;
import nc.vo.ic.other.out.TbOutgeneralHVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.uap.pf.PFBusinessException;

/**
 *  ���۳��ⵥǩ��
 * @author zpm
 *
 */
public class N_WDS8_SIGN extends AbstractCompiler2 {
	private java.util.Hashtable m_methodReturnHas=new java.util.Hashtable();
	private Hashtable m_keyHas=null;

	public N_WDS8_SIGN() {
		super();
	}
	/*
	* ��ע��ƽ̨��д������
	* �ӿ�ִ����
	*/
	public Object runComClass(PfParameterVO vo) throws BusinessException {
	try{
		super.m_tmpVo=vo;
				super.m_tmpVo = vo;
				Object retObj = null;				
				List userObject = (ArrayList)getUserObj();
				// ##################################################���ݽ���
				setParameter("AggObj",vo.m_preValueVo);
				setParameter("date", userObject.get(0));
				setParameter("operator", userObject.get(1));
				setParameter("pk_corp",vo.m_coId);
				// ##################################################��ʽ�����������۳���ش�
				runClass("nc.bs.wds.ic.so.out.ChangToWDSO", "onSign",
						"&AggObj:nc.vo.pub.AggregatedValueObject,&operator:String,&pk_corp:String,&date:String", vo, m_keyHas,m_methodReturnHas);
				// ##################################################����[���۳���]ǩ������
				//����ǩ����Ϣ
				TbOutgeneralHVO headvo = (TbOutgeneralHVO)vo.m_preValueVo.getParentVO();
				setParameter("hvo", headvo);
				runClass("nc.bs.wds.ic.so.out.SoOutBO", "updateHVO",
						"&hvo:nc.vo.ic.other.out.TbOutgeneralHVO", vo, m_keyHas,m_methodReturnHas);
				//����װж�Ѻ��㵥
				PushSaveWDSF pu=new PushSaveWDSF();
				pu.pushSaveWDSF(vo.m_preValueVo, vo.m_operator, vo.m_currentDate, LoadAccountBS.LOADFEE);		
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
