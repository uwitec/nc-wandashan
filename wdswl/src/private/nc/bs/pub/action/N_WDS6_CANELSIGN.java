package nc.bs.pub.action;

import java.util.ArrayList;
import java.util.Hashtable;

import nc.bs.dao.BaseDAO;
import nc.bs.pub.compiler.AbstractCompiler2;
import nc.bs.wds.load.pub.CanelDeleteWDF;
import nc.bs.wds2.set.OutInSetBO;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.vo.ic.other.out.TbOutgeneralHVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.uap.pf.PFBusinessException;

/**
 *  其它出库单取消签字
 * @author zpm
 */
public class N_WDS6_CANELSIGN extends AbstractCompiler2 {
	private java.util.Hashtable m_methodReturnHas=new java.util.Hashtable();
	private Hashtable m_keyHas=null;
	BaseDAO dao = null;
	
	private BaseDAO getBaseDAO(){
		if(dao==null){
			dao = new BaseDAO();
		}
		return dao;
	}

	public N_WDS6_CANELSIGN() {
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
			String date = null;
			String operate = null;
			ArrayList<String> list = (ArrayList<String>)vo.m_userObj;
			if(list != null && list.size()>0){
				date = list.get(0);
				operate = list.get(1);
			}
			setParameter("AggObj",vo.m_preValueVo);
			setParameter("operate",operate);
			setParameter("date", date);
			
			check(vo.m_preValueVo);
			
			TbOutgeneralHVO head = (TbOutgeneralHVO)vo.m_preValueVo.getParentVO();
//			UFBoolean isxnap = PuPubVO.getUFBoolean_NullAs(head.getIsxnap(), UFBoolean.FALSE);
			
//			是否回传erp
			OutInSetBO setbo = new OutInSetBO();
			UFBoolean isreturn = PuPubVO.getUFBoolean_NullAs(setbo.isReturnErp(head.getCdispatcherid()), UFBoolean.FALSE);

			if(isreturn.booleanValue()){
				// #################################################
				AggregatedValueObject[] icBillVO = (AggregatedValueObject[]) runClass("nc.bs.wds.ic.other.out.ChangeTo4I", "canelSignQueryGenBillVO",
						"&AggObj:nc.vo.pub.AggregatedValueObject,&operate:String,&date:String", vo, m_keyHas,m_methodReturnHas);
				// ##################################################
				setParameter("AggObject",icBillVO);
				retObj = runClass("nc.bs.wds.ic.other.out.OtherOutBO", "canelPushSign4I",
						"&date:String,&AggObject:nc.vo.pub.AggregatedValueObject[]", vo, m_keyHas,m_methodReturnHas);
				// ##################################################保存[其它出库]取消签字内容

			}
			TbOutgeneralHVO headvo = (TbOutgeneralHVO)vo.m_preValueVo.getParentVO();
			setParameter("hvo", headvo);
			runClass("nc.bs.wds.ic.other.out.OtherOutBO", "updateHVO",
					"&hvo:nc.vo.ic.other.out.TbOutgeneralHVO", vo, m_keyHas,m_methodReturnHas);
			//删除下游装卸费核算单
			CanelDeleteWDF cw=new CanelDeleteWDF();
			cw.canelDeleteWDF(vo.m_preValueVo, date, operate);
			return retObj;
		} catch (Exception ex) {
			if (ex instanceof BusinessException)
				throw (BusinessException) ex;
			else
				throw new PFBusinessException(ex.getMessage(), ex);
		}
	}
	/**
	 * 校验是否存在下游其他入库单
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 * @时间：2012-7-24下午02:39:21
	 * @param valueVo
	 */
	private void check(AggregatedValueObject obj) throws BusinessException{

		if(obj ==null){
			return;
		}
		TbOutgeneralHVO parent =(TbOutgeneralHVO) obj.getParentVO();
		String pk_sendorder = parent.getPrimaryKey();
		StringBuffer sql = new StringBuffer();	
		sql.append(" select count(*) ");
		sql.append(" from tb_general_h ");
		sql.append(" join tb_general_b ");
		sql.append(" on tb_general_h.geh_pk= tb_general_b.geh_pk");
		sql.append(" where isnull(tb_general_h.dr,0)=0 and isnull(tb_general_b.dr,0)=0 ");
		sql.append(" and tb_general_b.csourcebillhid ='"+pk_sendorder+"'");
		int i = PuPubVO.getInteger_NullAs(getBaseDAO().executeQuery(sql.toString(), WdsPubResulSetProcesser.COLUMNPROCESSOR), 0);
		if( i>0){
			throw new BusinessException("已有下游其他入库单，请先删除其他库单再做此操作");
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