package nc.bs.hg.pu.mondeal;

import java.util.ArrayList;

import nc.bs.dao.BaseDAO;
import nc.bs.hg.pu.pub.HgBsPubTool;
import nc.bs.trade.business.HYPubBO;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.uif.pub.exception.UifException;
import nc.vo.hg.pu.plan.mondeal.MonUpdateBVO;
import nc.vo.hg.pu.plan.month.PlanOtherBVO;
import nc.vo.hg.pu.plan.year.PlanYearNumVO;
import nc.vo.hg.pu.pub.HgPubConst;
import nc.vo.hg.pu.pub.HgPubTool;
import nc.vo.hg.pu.pub.PlanVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.relacal.SCMRelationsCal;
import nc.vo.trade.pub.HYBillVO;

public class MonDealBo {
	
	private HYPubBO bo = null;
	private HYPubBO getHYPubBO(){
		if(bo == null){
			bo = new HYPubBO();
		}
		return bo;
	}
	
	private BaseDAO dao = null;
	private BaseDAO getBaseDao(){
		if(dao == null){
			dao = new BaseDAO();
		}
		return dao;
	}
	
	public HYBillVO loadYearPlanData(String whereSql) throws BusinessException{
		
		HYBillVO billvo = null;
		//��ѯ������   ��ʹ��  ����
		
		billvo = queryAdjustBillVo(whereSql);
		//�ȼ��ص������Լ�������
		if(billvo!=null){
			return billvo;
		}
		//�������Լ�������ΪNULL  ��ȥ��ƻ�����  
		billvo = getAdjustBillVoByYearVo(whereSql);
		return billvo;
	}
	/**
	 * ��ѯ������HYBillVO
	 * @param whereSql
	 * @return
	 * @throws BusinessException
	 */
	private HYBillVO queryAdjustBillVo(String whereSql ) throws BusinessException{
		HYBillVO billvo = new HYBillVO();
		StringBuffer strbh = new StringBuffer();

		String sqlh = "select h.pk_plan from hg_plan h inner join hg_planyear_b b " +
		" on h.pk_plan =  b.pk_plan where  isnull(h.dr,0)=0 and isnull(b.dr,0) = 0  and h.vbillstatus = 1 "+
		" and  ";

		strbh.append(sqlh);
		strbh.append(whereSql);
		Object oh = getBaseDao().executeQuery(strbh.toString(), new ArrayListProcessor());
		if(oh==null)
			return null;
		//			throw new BusinessException("û���ҵ��õ��ݺŵ�����");
		ArrayList alh = (ArrayList)oh;
		if( alh == null || alh.size() ==0)
			return null;
		Object[] stmps = null;
		stmps = (Object[]) alh.get(0);
		String pk_planh =(String) stmps[0];

		String sql1 = "select h.pk_plan,b.pk_monudate_b from hg_plan h inner join hg_monupdate_b b " +
		" on h.pk_plan =  b.pk_plan where  isnull(h.dr,0)=0 and isnull(b.dr,0) = 0  and h.vbillstatus = 8 "+
		" and  cnextbillid ='"+pk_planh+"'";
		Object o1 = getBaseDao().executeQuery(sql1, new ArrayListProcessor());
		if(o1==null)
			return null;
		//			throw new BusinessException("�ڸõ�����û���ҵ��õ��ݺŵ�����");
		ArrayList al1 = (ArrayList)o1;
		if( al1 == null || al1.size() ==0)
			return null;
		//			throw new BusinessException("�ڸõ�����û���ҵ��õ��ݺŵ�����");
		int size1= al1.size();

		ArrayList<String> list = new ArrayList<String>();
		String[] pk_monupdate_b = new String[size1];
		for(int i=0;i<size1;i++){
			Object ob = al1.get(i);
			if(ob== null)
				return null;
			if(ob != null){
				Object[] str = (Object[])ob;
				String pk_plan = PuPubVO.getString_TrimZeroLenAsNull(str[0]);
				if(!list.contains(pk_plan))
					list.add(pk_plan);
				pk_monupdate_b[i] =PuPubVO.getString_TrimZeroLenAsNull(str[1]);
			}
		}

		if(list == null || list.size()==0)
			return null;
		if(list.size()>1)
			return null;
		//			throw new BusinessException("�Ӹõ����м��ص��˶�������");

		String str = PuPubVO.getString_TrimZeroLenAsNull(list.get(0));

		if(str== null)
			return null;

		PlanVO head =queryPlanVO(str);
		if(head==null)
			return null;
		//			throw new BusinessException("�Ӹõ����м��ر�ͷ����ʧ��");
		billvo.setParentVO(head);

		MonUpdateBVO[] body=queryMonUpdateBVO(pk_monupdate_b);

		if(body ==null || body.length==0)
			return null;
		//			throw new BusinessException("�Ӹõ����м��ر�������ʧ��");
		billvo.setChildrenVO(body);
		return billvo;
	}
	/**
	 * ��ѯHEADVO
	 * @param where
	 * @return
	 * @throws BusinessException
	 */
	private PlanVO queryPlanVO(String where) throws BusinessException{
		PlanVO vo=null;
		vo=(PlanVO)getBaseDao().retrieveByPK(PlanVO.class,where);
		if(vo == null )
			throw new BusinessException("�Ӹõ����м��ر�ͷ����ʧ��");	    
		return vo;
	}
	/**
	 * ��ѯ����bodyVO
	 * @param pk
	 * @return
	 * @throws BusinessException
	 */
	private MonUpdateBVO[] queryMonUpdateBVO(String[] pk) throws BusinessException{
		int len=pk.length;
		MonUpdateBVO[]  bodys =  new MonUpdateBVO[len];
		for(int i=0;i<len;i++){
			Object o =getBaseDao().retrieveByPK(MonUpdateBVO.class,pk[i]);
			if(o==null)
				throw new BusinessException("�Ӹõ����м��ر�������ʧ��");
			bodys[i]=(MonUpdateBVO)o;
		}
		return bodys;
	}
	
	/**
	 * �Ȳ�ѯ��Ӧ��ƻ�����  Ȼ����ƻ�����ת��Ϊ  ����������
	 * @param whereSql
	 * @return
	 * @throws BusinessException
	 */
	private HYBillVO getAdjustBillVoByYearVo(String whereSql)
			throws BusinessException {

		HYBillVO billvo = new HYBillVO();
		StringBuffer strb = new StringBuffer();
		String sql = "select h.pk_plan,b.pk_planyear_b from hg_plan h inner join hg_planyear_b b "
				+ " on h.pk_plan =  b.pk_plan where  isnull(h.dr,0)=0 and isnull(b.dr,0) = 0  and h.vbillstatus = 1 "
				+ " and  ";
		strb.append(sql);
		strb.append(whereSql);

		Object o = getBaseDao().executeQuery(strb.toString(),
				new ArrayListProcessor());

		if (o == null)
			throw new BusinessException("�ڸõ�����û���ҵ��õ��ݺŵ�����");
		ArrayList al = (ArrayList) o;
		if (al == null || al.size() == 0)
			throw new BusinessException("�ڸõ�����û���ҵ��õ��ݺŵ�����");
		int size = al.size();

		ArrayList<String> list = new ArrayList<String>();
		String[] pk_planyear_b = new String[size];

		for (int i = 0; i < size; i++) {
			Object ob = al.get(i);
			if (ob == null)
				return null;
			if (ob != null) {
				Object[] str = (Object[]) ob;
				String pk_plan = PuPubVO.getString_TrimZeroLenAsNull(str[0]);
				if (!list.contains(pk_plan))
					list.add(pk_plan);
				pk_planyear_b[i] = PuPubVO.getString_TrimZeroLenAsNull(str[1]);
			}
		}

		if (list == null || list.size() == 0)
			return null;
		if (list.size() > 1)
			throw new BusinessException("�Ӹõ����м��ص��˶�������");

		String str = PuPubVO.getString_TrimZeroLenAsNull(list.get(0));
		if (str != null) {
			for (int i = 0; i < size; i++) {
				String sqlStr = " select  cmonth from hg_planother_b b inner join HG_PLAN h on h.pk_plan = b.pk_plan"
						+ " where isnull(h.dr,0)=0 and isnull(b.dr,0)=0 and h.pk_billtype = '"
						+ HgPubConst.PLAN_MONTH_BILLTYPE
						+ "'"
						+ " and b.cnextbillid = '"
						+ str
						+ "' and coalesce(nouttotalnum,0)>0";// ���ÿ���·��Ƿ��Ѿ�ִ��
				Object o1 = dao.executeQuery(sqlStr, new ArrayProcessor());
				if (o1 != null) {
					Object[] month = (Object[]) o1;
					int len = month.length;
					if (month != null && len > 0)
						if (len == 12) {
							throw new BusinessException("�ƻ���ȫ��ִ�в��ܵ���");
						}
				}
			}
			PlanVO head = queryPlanVO(str);
			if (head == null)
				throw new BusinessException("�Ӹõ����м��ر�ͷ����ʧ��");

			billvo.setParentVO(head);
		}
		PlanYearNumVO[] body = queryPlanYearBVO(pk_planyear_b);
		if (body == null || body.length == 0)
			throw new BusinessException("�Ӹõ����м��ر�������ʧ��");
		billvo.setChildrenVO(body);
		//		
		changeVO(billvo);
		return billvo;
	}
	/**
	 * ��ƻ�bodyvo
	 * @param pk
	 * @return
	 * @throws BusinessException
	 */
	private PlanYearNumVO[] queryPlanYearBVO(String[] pk) throws BusinessException{
		int len=pk.length;
		PlanYearNumVO[]  bodys =  new PlanYearNumVO[len];
		for(int i=0;i<len;i++){
			Object o =getBaseDao().retrieveByPK(PlanYearNumVO.class,pk[i]);
			if(o==null)
				throw new BusinessException("���ص�����Ϣʧ��");
			bodys[i]=(PlanYearNumVO)o;
		}
		return bodys;
		
	}
	/**
	 * voת��  ��ƻ�VO ת��Ϊ�µ���VO
	 * @param vo
	 * @throws UifException
	 */
	private void changeVO(HYBillVO vo) throws UifException {
		PlanVO head = (PlanVO) vo.getParentVO().clone();
		head.setPk_plan(null);
		head.setVbillstatus(8);
		head.setPk_billtype(HgPubConst.PLAN_MONDEAL_BILLTYPE);
		head.setVbillno(getHYPubBO()
				.getBillNo(HgPubConst.PLAN_MONDEAL_BILLTYPE, head.getPk_corp(),
						null, null));
		head.setVapproveid(null);
		head.setDapprovedate(null);
		head.setVoperatorid(null);
		head.setDmakedate(null);
		head.setDbilldate(null);
		head.setVmemo(null);
		PlanYearNumVO[] year = (PlanYearNumVO[]) vo.getChildrenVO();
		int len = year.length;
		MonUpdateBVO[] mon = new MonUpdateBVO[len];

		for (int i = 0; i < len; i++) {
			MonUpdateBVO mo = new MonUpdateBVO();
			mo.setCrowno(String.valueOf((i+1)*10));
			mo.setCinventoryid(PuPubVO.getString_TrimZeroLenAsNull(year[i]
					.getCinventoryid()));
			mo.setPk_plan(null);
			mo.setPk_invbasdoc(PuPubVO.getString_TrimZeroLenAsNull(year[i]
					.getPk_invbasdoc()));
			mo.setPk_measdoc(PuPubVO.getString_TrimZeroLenAsNull(year[i]
					.getPk_measdoc()));
			mo.setVbatchcode(PuPubVO.getString_TrimZeroLenAsNull(year[i]
					.getVbatchcode()));
			if (PuPubVO
					.getUFBoolean_NullAs(year[i].getFisload(), UFBoolean.FALSE).booleanValue()) {
				for (int n = 0; n < 12; n++) {
					mo
							.setAttributeValue(
									HgPubConst.NMONTHNUM[n],
									PuPubVO
											.getUFDouble_NullAsZero(year[i]
													.getAttributeValue(HgPubConst.NAFTERNUM[n])));
					mo
							.setAttributeValue(
									HgPubConst.NAFTERNUM[n],
									PuPubVO
											.getUFDouble_NullAsZero(year[i]
													.getAttributeValue(HgPubConst.NAFTERNUM[n])));

				}
			} else {
				for (int n = 0; n < 12; n++) {
					mo
							.setAttributeValue(
									HgPubConst.NMONTHNUM[n],
									PuPubVO
											.getUFDouble_NullAsZero(year[i]
													.getAttributeValue(HgPubConst.NMONTHNUM[n])));
					mo
							.setAttributeValue(
									HgPubConst.NAFTERNUM[n],
									PuPubVO
											.getUFDouble_NullAsZero(year[i]
													.getAttributeValue(HgPubConst.NMONTHNUM[n])));
				}
			}
			for (int m = 0; m < 12; m++) {
				mo.setAttributeValue(HgPubConst.NTOTAILNUM[m], PuPubVO
						.getUFDouble_NullAsZero(year[i]
								.getAttributeValue(HgPubConst.NTOTAILNUM[m])));
			}
			mo.setCnextbillbid(PuPubVO.getString_TrimZeroLenAsNull(year[i]
					.getPk_planyear_b()));
			mo.setCnextbillid(PuPubVO.getString_TrimZeroLenAsNull(year[i]
					.getPk_plan()));
			mon[i] = mo;
		}
		vo.setParentVO(head);
		vo.setChildrenVO(mon);
	}
	/**
	 *  �������� ���ݻ�д���¼ƻ���
	 * @param dealMonVo
	 * @throws BusinessException
	 */
	public void updateMonthPlan(HYBillVO dealMonVo) throws BusinessException {
		if (dealMonVo == null)
			throw new BusinessException("��������Ϊ��");
		
		//��ƻ������   �¼ƻ������    �¼ƻ��岢��У��   ��Ҫ���¼ƻ���ts ���в���У��
		
		
		MonUpdateBVO[] body = (MonUpdateBVO[]) dealMonVo.getChildrenVO();
		int len = body.length;
		for (int n = 0; n < len; n++) {
			String rowid = body[n].getCnextbillbid();
			String id = body[n].getCnextbillid();

			UFDouble[] num = new UFDouble[12];
			for (int i = 0; i < 12; i++) {
				num[i] = PuPubVO.getUFDouble_NullAsZero(body[n]
						.getAttributeValue(HgPubConst.NAFTERNUM[i]));
			}
			String sqlUpdate = "update hg_planyear_b set ";
			for (int i = 0; i < 12; i++) {
				sqlUpdate = sqlUpdate + HgPubConst.NAFTERNUM[i] + "=" + num[i]
						+ ",";
			}
			sqlUpdate = sqlUpdate + " fisload='Y' where pk_planyear_b = '"
					+ rowid + "' and pk_plan = '" + id + "'";
			getBaseDao().executeUpdate(sqlUpdate);
			String sql = "select b.pk_planother_b,n.cmonth from hg_plan n inner join hg_planother_b b "
					+ " on n.pk_plan =  b.pk_plan where b.cnextbillbid = '"
					+ rowid
					+ "' and b.cnextbillid = '"
					+ id
					+ "'"
					+ "and isnull(n.dr,0)=0 and isnull(b.dr,0) = 0 order by n.cmonth ";
			Object o = getBaseDao().executeQuery(sql, new ArrayListProcessor());
			if (o == null)
				return;
			ArrayList al = (ArrayList) o;
			if (al.size() == 0)
				throw new BusinessException("δ�ҵ���Ӧ���¼ƻ�");
			int size = al.size();
			Object[] stmps = null;
			PlanOtherBVO[] bvos = new PlanOtherBVO[12];
			for (int i = 0; i < size; i++) {
				stmps = (Object[]) al.get(i);
				String pk = PuPubVO.getString_TrimZeroLenAsNull(stmps[0]);
				if (pk == null)
					throw new BusinessException("δ�ҵ���Ӧ���¼ƻ�");
				PlanOtherBVO bvo = (PlanOtherBVO) getBaseDao().retrieveByPK(
						PlanOtherBVO.class, pk);
				if (bvo == null)
					throw new BusinessException("δ�ҵ���Ӧ���¼ƻ�");
				bvo.setNnum(num[i]);
				bvos[i] = bvo;
			}
			SCMRelationsCal.calculate(bvos, HgPubTool.iaPrior, "nnum",
					HgPubTool.m_iDescriptions, HgPubTool.m_saKey);
			// getHYPubBO().updateAry(bvos);
			getBaseDao().updateVOArray(bvos);
		}
	}
	
	/**
	 * ����������  ��������Ѿ������ĵ���  �Ͳ��ܱ���
	 * @author zhw
	 * @˵�������׸ڿ�ҵ��
	 * 2011-2-22����03:55:54
	 * @param inv
	 * @param cnextbillid
	 * @param cnextbillbid
	 * @throws BusinessException
	 */
	public void checkUpdateMon(String cnextbillid ,String cnextbillbid) throws BusinessException{
		String sql ="select count(*) from hg_planother_b where coalesce(nreserve10,0.0)>0.0 and isnull(dr,0)=0 and cnextbillid = '"+cnextbillid+"' and cnextbillbid = '"+cnextbillbid+"'"; 
        String len = PuPubVO.getString_TrimZeroLenAsNull(getBaseDao().executeQuery(sql, HgBsPubTool.COLUMNPROCESSOR));
		if(Integer.parseInt(len)>0)
			throw new BusinessException("�õ����Ѿ�ƽ�������,���ܵ���");
		
	}
}

