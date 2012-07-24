package nc.bs.pub.action;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import nc.bs.pub.compiler.AbstractCompiler2;
import nc.bs.wds.load.account.LoadAccountBS;
import nc.bs.wds.load.pub.PushSaveWDSF;
import nc.vo.ic.pub.TbGeneralHVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.uap.pf.PFBusinessException;

/**
 *  调拨入库单签字
 * @author lyf
 *
 */
public class N_WDS9_SIGN extends AbstractCompiler2 {
	private java.util.Hashtable m_methodReturnHas=new java.util.Hashtable();
	private Hashtable m_keyHas=null;

	public N_WDS9_SIGN() {
		super();
	}
	/*
	* 备注：平台编写规则类
	* 接口执行类
	*/
	public Object runComClass(PfParameterVO vo) throws BusinessException {
			try {
					super.m_tmpVo = vo;
					Object retObj = null;
					List userObject = (ArrayList)getUserObj();
				// ##################################################数据交换
				setParameter("AggObj",vo.m_preValueVo);
				setParameter("date", userObject.get(0));
				setParameter("operator", userObject.get(1));
				setParameter("pk_corp",vo.m_coId);
				// ##################################################推式保存生成调拨出库回传
				runClass("nc.bs.wds.ic.allocation.in.ChangToWDSP", "onSign",
						"&AggObj:nc.vo.pub.AggregatedValueObject,&operator:String,&pk_corp:String,&date:String", vo, m_keyHas,m_methodReturnHas);
				// ##################################################保存[调拨入库]签字内容
				TbGeneralHVO headvo = (TbGeneralHVO)vo.m_preValueVo.getParentVO();
				setParameter("hvo", headvo);
				runClass("nc.bs.wds.ic.allocation.in.AllocationInBO", "updateHVO",
						"&hvo:nc.vo.ic.pub.TbGeneralHVO", vo, m_keyHas,m_methodReturnHas);
				//生成装卸费核算单
				PushSaveWDSF pu=new PushSaveWDSF();
				pu.pushSaveWDSF(vo.m_preValueVo, vo.m_operator, vo.m_currentDate, LoadAccountBS.UNLOADFEE);
				return retObj;
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
