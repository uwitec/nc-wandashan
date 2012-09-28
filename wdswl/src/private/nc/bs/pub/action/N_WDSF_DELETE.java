package nc.bs.pub.action;
import java.util.Hashtable;
import nc.bs.dao.BaseDAO;
import nc.bs.pub.compiler.AbstractCompiler2;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.uap.pf.PFBusinessException;
import nc.vo.wds.load.account.ExaggLoadPricVO;
/**
 *   装卸费结算
 * @author Administrator
 *
 */
public class N_WDSF_DELETE extends AbstractCompiler2 {
	private java.util.Hashtable m_methodReturnHas = new java.util.Hashtable();
	private Hashtable m_keyHas = null;
    private BaseDAO dao=new BaseDAO();

	public N_WDSF_DELETE() {
		super();
	}

	/*
	 * 备注：平台编写规则类 接口执行类
	 */
	public Object runComClass(PfParameterVO vo) throws BusinessException {
		try {
			super.m_tmpVo = vo;
			// ####本脚本必须含有返回值,返回DLG和PNL的组件不允许有返回值####
			Object retObj = null;
			// 方法说明:行业公共删除
			//校验  调拨入库回传  是否已经审批
			valuteEnd(vo);			
			runClass("nc.bs.wds.load.account.LoadAccountBS", "writeBack","nc.vo.pub.AggregatedValueObject:01", vo, m_keyHas,	m_methodReturnHas);
			retObj = runClass("nc.bs.trade.comdelete.BillDelete", "deleteBill",
					"nc.vo.pub.AggregatedValueObject:01", vo, m_keyHas,
					m_methodReturnHas);
			// ##################################################
			//参照第一次保存需要回写装卸费完成标志
			return retObj;
		} catch (Exception ex) {
			if (ex instanceof BusinessException)
				throw (BusinessException) ex;
			else
				throw new PFBusinessException(ex.getMessage(), ex);
		}
	}
	  /**
     * 校验调拨入库单 是否已经审批
     * @作者：zhf
     * @说明：完达山物流项目 
     * @时间：2012-9-28上午11:50:53
     * @param vo
     * @throws Exception 
     */
	public void valuteEnd1(String chid) throws Exception {
		if (chid == null) {
			return;
		}
		StringBuffer sql = new StringBuffer();
		sql.append(" select h.vbillstatus from wds_writeback4y_b2 b join wds_writeback4y_h h ");
		sql.append(" on b.pk_wds_writeback4y_h=h.pk_wds_writeback4y_h ");
		sql.append(" where isnull(b.dr,0)=0 and isnull(h.dr,0)=0 ");
		sql.append(" and  b.csourcebillhid ='" + chid + "'");
		Integer status = PuPubVO.getInteger_NullAs(dao.executeQuery(sql
				.toString(), new ColumnProcessor()), -1);
		if (IBillStatus.CHECKPASS == status) {
			throw new Exception("调拨入库回传单已经审批 不能修改装卸费核算单");
		}
	}
    /**
     * 校验调拨入库单 是否已经审批
     * @作者：zhf
     * @说明：完达山物流项目 
     * @时间：2012-9-28上午11:50:53
     * @param vo
     * @throws Exception 
     */
	public void valuteEnd(PfParameterVO vo) throws Exception {
		if (vo == null)
			return;
		if (vo.m_preValueVo == null)
			return;
		ExaggLoadPricVO billvo = (ExaggLoadPricVO) vo.m_preValueVo;

		if (billvo.getTableVO(billvo.getTableCodes()[0]) == null
				|| billvo.getTableVO(billvo.getTableCodes()[0]).length == 0)
			return;
		SuperVO bodyvo = (SuperVO) billvo.getTableVO(billvo.getTableCodes()[0])[0];
		String chid = PuPubVO.getString_TrimZeroLenAsNull(bodyvo
				.getAttributeValue("csourcebillhid"));
		if (chid == null) {
			return;
		}
		StringBuffer sql = new StringBuffer();
		sql.append(" select h.vbillstatus from wds_writeback4y_b2 b join wds_writeback4y_h h ");
		sql.append(" on b.pk_wds_writeback4y_h=h.pk_wds_writeback4y_h ");
		sql.append(" where isnull(b.dr,0)=0 and isnull(h.dr,0)=0 ");
		sql.append(" and  b.csourcebillhid ='" + chid + "'");
		Integer status = PuPubVO.getInteger_NullAs(dao.executeQuery(sql
				.toString(), new ColumnProcessor()), -1);
		if (IBillStatus.CHECKPASS == status) {
			throw new Exception("调拨入库回传单已经审批 不能删除装卸费核算单");
		}
	}
	
	/*
	 * 备注：平台编写原始脚本
	 */
	public String getCodeRemark() {
		return "	//####本脚本必须含有返回值,返回DLG和PNL的组件不允许有返回值####\nObject retObj  =null;\n//方法说明:行业公共删除\nretObj  =runClassCom@ \"nc.bs.trade.comdelete.BillDelete\", \"deleteBill\", \"nc.vo.pub.AggregatedValueObject:01\"@;\n//##################################################\nreturn retObj;\n";
	}

	/*
	 * 备注：设置脚本变量的HAS
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
