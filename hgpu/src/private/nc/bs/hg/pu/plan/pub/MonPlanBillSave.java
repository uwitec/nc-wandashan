package nc.bs.hg.pu.plan.pub;

import java.util.ArrayList;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.hg.pu.pub.HYBillSave;
import nc.bs.hg.pu.pub.HgBsPubTool;
import nc.bs.trade.business.HYPubBO;
import nc.vo.hg.pu.plan.month.PlanOtherBVO;
import nc.vo.hg.pu.pub.HgPubTool;
import nc.vo.hg.pu.pub.PlanVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.voutils.VOUtil;

/**
 * �¼ƻ�����Ǯ����
 * @author zhw
 *
 */
public class MonPlanBillSave extends HYBillSave {

	private BaseDAO dao = null;

	private BaseDAO getBaseDao() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}

     /**
      * ���ݱ���ǰ����
      */
	protected void beforeSave(AggregatedValueObject billVo)
			throws BusinessException {
		if (billVo == null)
			throw new BusinessException("��������Ϊ��");

		PlanVO head = (PlanVO) billVo.getParentVO();
		if (head == null)
			return;

		PlanOtherBVO[] bvos = (PlanOtherBVO[]) billVo.getChildrenVO();
		if (bvos == null || bvos.length == 0)
			return;

		VOUtil.sort(bvos, new String[] { "pk_invbasdoc" },
				new int[] { VOUtil.DESC });

		ArrayList<String> als = new ArrayList<String>();
		ArrayList<UFDouble> alu = new ArrayList<UFDouble>();

		for (PlanOtherBVO bvo : bvos) {
			als.add(bvo.getPk_invbasdoc());// ���id
			alu.add(bvo.getNnum());// ��ǰ����¼ƻ���
		}

		if (als == null || als.size() == 0)
			return;

		ArrayList<String> al = (ArrayList<String>) checkUinqueRule(head, als);// Ψһ��У��

		HYPubBO bo = new HYPubBO();
		int size = al.size();
		StringBuffer str = new StringBuffer();
		for (int i = 0; i < size; i++) {
			String err = al.get(i);
			if (err != null) {
				Object invcode = bo.findColValue("bd_invbasdoc", "invcode",
						"pk_invbasdoc='" + err + "'");
				str.append("���" + invcode + "�Ѿ����ڡ�\n");
			}
		}
		if (str == null || str.length() == 0)
			return;
		throw new BusinessException(str.toString());
	}


	/**
	 * 
	 * @author zhw
	 * @˵����������ҽҩ��
	 * 2011-8-4����04:55:06
	 * @param corp ��˾
	 * @param als���iD����
	 * @param alu ��ǰ�ƻ�������
	 * @throws BusinessException 
	 */
	public ArrayList<String> onSaveCheck(AggregatedValueObject billVo) throws BusinessException {
		
		if (billVo == null)
			throw new BusinessException("��������Ϊ��");

		PlanVO head = (PlanVO) billVo.getParentVO();
		if (head == null)
			return null;

		PlanOtherBVO[] bvos = (PlanOtherBVO[]) billVo.getChildrenVO();
		if (bvos == null || bvos.length == 0)
			return null;

		VOUtil.sort(bvos, new String[] { "pk_invbasdoc" },new int[] { VOUtil.DESC });
		
		ArrayList<String> als = new ArrayList<String>();
		ArrayList<UFDouble> alu = new ArrayList<UFDouble>();
	
		for (PlanOtherBVO bvo : bvos) {
			als.add(bvo.getPk_invbasdoc());//���id
			alu.add(bvo.getNnum());//��ǰ����¼ƻ���
		}
		
		if (als == null || als.size() == 0)
			return null;

		ArrayList aluy = getYearNum(head, als);// ��ƻ�����
		ArrayList alum = getMonUsedNum(head, als);// �¼ƻ��ۼ���
		ArrayList<String> al = new ArrayList<String>();

		int size = als.size();
		for (int i = 0; i < size; i++) {
			if (aluy == null || aluy.size() == 0) {
				throw new BusinessException("���д������ƻ��������ڡ�");
			}

			UFDouble ny = PuPubVO.getUFDouble_NullAsZero(aluy.get(i));

			if (ny.equals(UFDouble.ZERO_DBL))
				al.add(als.get(i) + "&��ƻ��������ڡ�");
			else {
				UFDouble nre = UFDouble.ZERO_DBL;
				
				if (alum == null || alum.size() == 0)
					nre = ny.sub(PuPubVO.getUFDouble_NullAsZero(alu.get(i)));
				else
					nre = ny.sub(PuPubVO.getUFDouble_NullAsZero(alu.get(i)))
							.sub(PuPubVO.getUFDouble_NullAsZero(alum.get(i)));
				if (nre.compareTo(UFDouble.ZERO_DBL) < 0)
					al.add(als.get(i) + "&�¼ƻ�������ʣ��ƻ���[" + nre+"]��\n");
			}
		}
		return al;
	}

	/**
	 * 
	 * @author zhw
	 * @˵�������׸ڿ�ҵ��//  ��ƻ�����
	 * 2011-8-4����04:55:26
	 * @param corp ��˾
	 * @param als���ID����
	 * @return ��ƻ���������
	 * @throws DAOException
	 */
	
	@SuppressWarnings("unchecked")
	private ArrayList getYearNum(PlanVO head, ArrayList<String> als)
			throws DAOException {

		String sql = " select b.nnum from hg_plan h join hg_planyear_b b on h.pk_plan = b.pk_plan where nvl(h.dr, 0) =0 and nvl(b.dr, 0) = 0 "
				+ " and h.pk_corp = '"+ head.getPk_corp()+ "' and h.pk_billtype='HG01' and h.cyear = '" + head.getCyear() + "'and h.capplydeptid = '" + head.getCapplydeptid()
				+"' and b.pk_invbasdoc in "+ HgPubTool.getSubSql(als.toArray(new String[0]))+ " order by  b.pk_invbasdoc desc";
		
		Object o =  getBaseDao().executeQuery(sql.toString(), HgBsPubTool.COLUMNLISTPROCESSOR);
		if(o==null)
			return null;
		ArrayList al =(ArrayList)o;

		return al;
	}

	/**
	 * 
	 * @author zhw
	 * @˵�������׸ڿ�ҵ��//�¼ƻ��ۼ���
	 * 2011-8-4����04:55:32
	 * @param corp  ��˾
	 * @param als ���ID����
	 * @return �¼ƻ��ۼ�������
	 * @throws DAOException
	 */
	
	@SuppressWarnings("unchecked")
	private ArrayList getMonUsedNum(PlanVO head, ArrayList<String> als)
			throws DAOException {

		String sql = " select sum(coalesce(b.nouttotalnum,0)) from hg_plan h join hg_planother_b b on h.pk_plan = b.pk_plan "
				+ " where nvl(h.dr, 0) =0 and nvl(b.dr, 0) = 0 and h.pk_corp = '"+ head.getPk_corp()+ "' and h.pk_billtype = 'HG02'" 
				+ " and h.cyear = '" + head.getCyear() +"' and h.capplydeptid = '" + head.getCapplydeptid()
				+ " ' and b.pk_invbasdoc in " + HgPubTool.getSubSql(als.toArray(new String[0]))+ " group by  b.pk_invbasdoc order by  b.pk_invbasdoc desc";

		ArrayList al = (ArrayList) getBaseDao()
				.executeQuery(sql.toString(), HgBsPubTool.COLUMNLISTPROCESSOR);

		return al;
	}

	/**
	 * 
	 * @author zhw
	 * @˵�������׸ڿ�ҵ��   //�¼ƻ�Ψһ��У��
	 * 2011-8-4����04:55:37
	 * @param head   �ƻ���ͷ
	 * @param als  ���id����
	 * @return   ��Ψһ���ID����
	 * @throws BusinessException
	 */
	
	@SuppressWarnings("unchecked")
	private ArrayList<String> checkUinqueRule(PlanVO head, ArrayList<String> als)
			throws BusinessException {

		String pk_plan = head.getPrimaryKey();
		StringBuffer sql = new StringBuffer();
		sql.append(" select b.pk_invbasdoc  from hg_plan h,hg_planother_b b where h.pk_plan = b.pk_plan and isnull(h.dr,0)=0 and isnull(b.dr,0) = 0 ");
		sql.append(" and h.cyear = '" + head.getCyear() + "' and h.pk_corp = '"+ head.getPk_corp() + "' and h.pk_billtype = 'HG02' and h.cmonth ='"+head.getCmonth()+"'");
		sql.append(" and h.capplydeptid = '" + head.getCapplydeptid() + "'");
	    
		
		if (pk_plan != null && !"".equals(pk_plan)) {// �޸�ʱ����У�鵱ǰ����
			sql.append("	and h.pk_plan <> '" + pk_plan + "' ");
		}
		
		sql.append(" and b.pk_invbasdoc in "+ HgPubTool.getSubSql(als.toArray(new String[0]))
						+ " group by  b.pk_invbasdoc having(count(0))>0 order by  b.pk_invbasdoc desc");

		ArrayList<String> al = (ArrayList<String>) getBaseDao().executeQuery(
				sql.toString(), HgBsPubTool.COLUMNLISTPROCESSOR);

		return al;
	}
}
