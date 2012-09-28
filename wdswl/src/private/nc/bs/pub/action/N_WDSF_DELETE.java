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
 *   װж�ѽ���
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
	 * ��ע��ƽ̨��д������ �ӿ�ִ����
	 */
	public Object runComClass(PfParameterVO vo) throws BusinessException {
		try {
			super.m_tmpVo = vo;
			// ####���ű����뺬�з���ֵ,����DLG��PNL������������з���ֵ####
			Object retObj = null;
			// ����˵��:��ҵ����ɾ��
			//У��  �������ش�  �Ƿ��Ѿ�����
			valuteEnd(vo);			
			runClass("nc.bs.wds.load.account.LoadAccountBS", "writeBack","nc.vo.pub.AggregatedValueObject:01", vo, m_keyHas,	m_methodReturnHas);
			retObj = runClass("nc.bs.trade.comdelete.BillDelete", "deleteBill",
					"nc.vo.pub.AggregatedValueObject:01", vo, m_keyHas,
					m_methodReturnHas);
			// ##################################################
			//���յ�һ�α�����Ҫ��дװж����ɱ�־
			return retObj;
		} catch (Exception ex) {
			if (ex instanceof BusinessException)
				throw (BusinessException) ex;
			else
				throw new PFBusinessException(ex.getMessage(), ex);
		}
	}
	  /**
     * У�������ⵥ �Ƿ��Ѿ�����
     * @���ߣ�zhf
     * @˵�������ɽ������Ŀ 
     * @ʱ�䣺2012-9-28����11:50:53
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
			throw new Exception("�������ش����Ѿ����� �����޸�װж�Ѻ��㵥");
		}
	}
    /**
     * У�������ⵥ �Ƿ��Ѿ�����
     * @���ߣ�zhf
     * @˵�������ɽ������Ŀ 
     * @ʱ�䣺2012-9-28����11:50:53
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
			throw new Exception("�������ش����Ѿ����� ����ɾ��װж�Ѻ��㵥");
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
