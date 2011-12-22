package nc.bs.pub.action;

import java.util.ArrayList;
import java.util.Hashtable;

import nc.bs.pub.compiler.AbstractCompiler2;
import nc.bs.wds.load.pub.CanelDeleteWDF;
import nc.vo.ic.pub.TbGeneralHVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.uap.pf.PFBusinessException;

/**
 *  退货入库单取消签字
 * @author zpm
 */
public class N_WDSZ_CANELSIGN extends AbstractCompiler2 {
	private java.util.Hashtable m_methodReturnHas=new java.util.Hashtable();
	private Hashtable m_keyHas=null;

	public N_WDSZ_CANELSIGN() {
		super();
	}
	/*
	* 备注：平台编写规则类
	* 接口执行类
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
			// ##################################################
			setParameter("AggObj",vo.m_preValueVo);
			setParameter("operate",operate);
			setParameter("date", date);
			AggregatedValueObject[] icBillVO = (AggregatedValueObject[]) runClass("nc.bs.wds.ic.other.in.ChangeTo4A", "canelSignQueryGenBillVO",
					"&AggObj:nc.vo.pub.AggregatedValueObject,&operate:String,&date:String", vo, m_keyHas,m_methodReturnHas);
			// ##################################################
			setParameter("AggObject",icBillVO);
			retObj = runClass("nc.bs.wds.ic.other.in.OtherInBO", "canelPushSign4A",
					"&date:String,&AggObject:nc.vo.pub.AggregatedValueObject[]", vo, m_keyHas,m_methodReturnHas);
			// ##################################################保存[其他入库]取消签字内容
			TbGeneralHVO headvo = (TbGeneralHVO)vo.m_preValueVo.getParentVO();
			setParameter("hvo", headvo);
			runClass("nc.bs.wds.ic.other.in.OtherInBO", "updateHVO",
					"&hvo:nc.vo.ic.pub.TbGeneralHVO", vo, m_keyHas,m_methodReturnHas);
			//删除下游装卸费核算单
			CanelDeleteWDF cw=new CanelDeleteWDF();
			cw.canelDeleteWDF(vo.m_preValueVo, vo.m_operator, vo.m_currentDate);			
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
	* 备注：平台编写原始脚本
	*/
	public String getCodeRemark(){
		return "	try {\n			super.m_tmpVo = vo;\n			Object retObj = null;\n			// 保存即提交\n			retObj = runClass(\"nc.bs.gt.pub.HYBillSave\", \"saveBill\",\"nc.vo.pub.AggregatedValueObject:01\", vo, m_keyHas,	m_methodReturnHas);\n			return retObj;\n		} catch (Exception ex) {\n			if (ex instanceof BusinessException)\n				throw (BusinessException) ex;\n			else\n				throw new PFBusinessException(ex.getMessage(), ex);\n		}\n";}
	/*
	* 备注：设置脚本变量的HAS
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