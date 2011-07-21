package nc.bs.hg.pu.plan.balance;
import java.util.List;

import nc.bs.dao.DAOException;
import nc.bs.hg.pu.plan.pub.PlanPubBO;
import nc.bs.hg.pu.pub.HgBsPubTool;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.vo.hg.pu.plan.balance.PlanMonDealVO;
import nc.vo.hg.pu.plan.month.PlanOtherBVO;
import nc.vo.hg.pu.pub.HgPubTool;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.trade.voutils.VOUtil;


public class BalancePlanBO {
	
	private PlanPubBO bo = null;
	private PlanPubBO getPubBO(){
		if(bo == null){
			bo = new PlanPubBO();
		}
		return bo;
	}
	
	/**
	 * 
	 * @author zhW
	 * @˵�������׸ڿ�ҵ���ƻ�ƽ���ѯ����  �ϼ���ѯ�¼���Ҫ����ƽ��ļƻ�
	 * 2010-11-22����05:00:00
	 * @param whereSql
	 * @param cl
	 * @return �¼��ϱ��ļƻ�
	 * @throws BusinessException
	 */
	public PlanMonDealVO[] queryPlanForDeal(String whereSql,ClientLink cl,String str) throws BusinessException{

		StringBuffer strb = new StringBuffer();
		strb.append(" select h.pk_corp,h.csourcebillno,h.vbillno,h.dbilldate,h.capplydeptid,h.capplypsnid,h.csupplydeptid"+
		",h.cyear,h.cmonth,h.cinvclassid,inv.invcode ");

		SuperVO bodyvo =  new PlanOtherBVO();;

		String[] barrnames = bodyvo.getAttributeNames();//

		for(String name:barrnames){
			if(name.equalsIgnoreCase("lsourceid")||name.equalsIgnoreCase(bodyvo.getPKFieldName())
					||"invcode".equalsIgnoreCase(name))
				continue;
			strb.append(",b."+name);
		}

		strb.append(","+bodyvo.getPKFieldName()+" pk_plan_b ");

		strb.append(" from hg_plan h inner join "+bodyvo.getTableName()+" b on h.pk_plan = b.pk_plan ");
		strb.append(" inner join bd_invbasdoc inv on inv.pk_invbasdoc = b.pk_invbasdoc ");
		strb.append(" inner join bd_invcl cl on cl.pk_invcl = inv.pk_invcl ");
		strb.append(" where isnull(h.dr,0)=0 and isnull(b.dr,0)=0 ");
		if(PuPubVO.getString_TrimZeroLenAsNull(whereSql)!=null)
			strb.append(" and "+whereSql);
//		strb.append(" and isnull(b.irowstatus,0) = "+HgPubConst.PLAN_ROW_STATUS_FREE+"");//δ�ύ��������
		strb.append(" and h.pk_billtype = 'HG02'");//�¼ƻ�
		strb.append(" and coalesce(b.bisuse,'N')='N' "); // Ӧ���Ʋ�������ʱ������
		//strb.append(" and h.cmonth = '"+cl.getAccountMonth()+"'"); //ֻ�ܲ����ǰ�µļƻ���
		strb.append(" and h.cyear = '"+cl.getAccountYear()+"'"); //ֻ�ܲ����ǰ��ļƻ���
		strb.append(" and coalesce(b.nnum,0.0)>0.0");
		if("deal".equalsIgnoreCase(str)){//���ѡ���Ѿ�ƽ�� 
			strb.append(" and coalesce(b.nreserve10,0.0)>0.0  and coalesce(b.vreserve2,'N')= 'N'");
	     }else if("undeal".equalsIgnoreCase(str)){//δƽ��
	    	 strb.append(" and coalesce(b.nreserve10,0.0)=0.0 ");
	     }else if("audit".equalsIgnoreCase(str)){//���
			 strb.append(" and coalesce(b.nreserve10,0.0)>0.0 and b.vreserve2 ='Y' ");
		}
		strb.append(" and coalesce(b.nouttotalnum,0.0)=0.0");
		//�û���ɫ�������Ȩ�޹���
		String powersql = getPubBO().queryClassPowerSql("bd_invcl", cl.getCorp(), cl.getUser());
		
		if(PuPubVO.getString_TrimZeroLenAsNull(powersql)!=null)
			strb.append(" and cl.pk_invcl in ("+powersql+")");

		List ldata = (List)getPubBO().executeQuery(strb.toString(), new BeanListProcessor(PlanMonDealVO.class));

		if(ldata == null||ldata.size() == 0)
			return null;
		
		PlanMonDealVO[] datas = (PlanMonDealVO[])ldata.toArray(new PlanMonDealVO[0]);

		VOUtil.ascSort(datas, HgPubTool.DEAL_SORT_FIELDNAMES);
		return datas;
	}
	
	public void balancePlan(List aldata,boolean isbalance) throws BusinessException{
		if(aldata == null||aldata.size() == 0){
			throw new BusinessException("����ƻ�����Ϊ��");
		}
		SQLParameter param = new SQLParameter();;
		String sql="update hg_planother_b set vreserve4 =?,vreserve5=?,nreserve10=? where pk_planother_b = ?";
		int size= aldata.size();
		for(int i=0;i<size;i++){
			
			PlanMonDealVO vo =(PlanMonDealVO)aldata.get(i);
			String inv =PuPubVO.getString_TrimZeroLenAsNull(vo.getInvcode());
			String cnextbillid =PuPubVO.getString_TrimZeroLenAsNull(vo.getCnextbillid());
			String cnextbillbid =PuPubVO.getString_TrimZeroLenAsNull(vo.getCnextbillbid());
			String mon = PuPubVO.getString_TrimZeroLenAsNull(vo.getCmonth());
			String year = PuPubVO.getString_TrimZeroLenAsNull(vo.getCyear());
			
			checkUpdateMon(inv,cnextbillid,cnextbillbid);
			
			if(isbalance){
				if(vo.getNreserve10()==null)
					throw new BusinessException("���"+inv+"ƽ������Ϊ��");
				if((PuPubVO.getUFDouble_NullAsZero(vo.getNreserve10()).compareTo(vo.getNnum()))>0)
					throw new BusinessException("���"+inv+"ƽ���������ڼƻ���������");
			}else{
				checkNouttotalnum(inv,cnextbillid,cnextbillbid,mon,year);
			}
			param.addParam(vo.getVreserve4());
			param.addParam(vo.getVreserve5());
			param.addParam(vo.getNreserve10());
			param.addParam(vo.getPk_plan_b());
			getPubBO().executeUpdate(sql,param);
			param.clearParams();
		}
		
	}
	/**
	 * �Ƿ���ڵ�����  ���ڵ������������� ������ƽ��
	 * @author zhw
	 * @˵�������׸ڿ�ҵ��
	 * 2011-2-22����03:55:54
	 * @param inv
	 * @param cnextbillid
	 * @param cnextbillbid
	 * @throws BusinessException
	 */
	public void checkUpdateMon(String inv,String cnextbillid ,String cnextbillbid) throws BusinessException{
		String sql ="select count(*) from hg_monupdate_b b join hg_plan h on b.pk_plan=h.pk_plan where " +
				"b.cnextbillid = '"+cnextbillid+"' and b.cnextbillbid = '"+cnextbillbid+"' and  h.vbillstatus = 8 " +
						"and isnull(b.dr,0)=0 and isnull(h.dr,0)=0";
        String len = PuPubVO.getString_TrimZeroLenAsNull(getPubBO().executeQuery(sql, HgBsPubTool.COLUMNPROCESSOR));
		if(Integer.parseInt(len)>0)
			throw new BusinessException("���"+inv+"������δ�����ĵ�����,������������������ƽ������");
		
	}
	
	public void checkNouttotalnum(String inv,String cnextbillid ,String cnextbillbid,String mon,String year) throws BusinessException{
		String sql ="select count(*) from hg_planother_b b join hg_plan h on b.pk_plan=h.pk_plan where " +
		"b.cnextbillid = '"+cnextbillid+"' and b.cnextbillbid = '"+cnextbillbid+"' and coalesce(b.nouttotalnum,0.0)>0 and h.cmonth ='"+mon+"'" 
		+" and h.cyear = '"+year+"' and isnull(b.dr,0)=0 and isnull(h.dr,0)=0";
		  String len = PuPubVO.getString_TrimZeroLenAsNull(getPubBO().executeQuery(sql, HgBsPubTool.COLUMNPROCESSOR));
			if(Integer.parseInt(len)>0)
				throw new BusinessException("���"+inv+"�Ѿ�����");
		
	}
	/**
	 * ���� ȡ��  �¼ƻ�ƽ��
	 * @author zhw
	 * @˵�������׸ڿ�ҵ��
	 * 2011-5-10����10:34:40
	 * @param aldata
	 * @param flag
	 * @throws BusinessException
	 */
	public void aduitPlans(List aldata,boolean flag) throws BusinessException{
		if(aldata == null||aldata.size() == 0){
			throw new BusinessException("����ƻ�����Ϊ��");
		}
		
		SQLParameter param = new SQLParameter();;
		String sql="update hg_planother_b set vreserve2 =? where pk_planother_b = ?";
		int size= aldata.size();
		for(int i=0;i<size;i++){
			PlanMonDealVO vo =(PlanMonDealVO)aldata.get(i);
			String reserve2= vo.getVreserve2();
			String pk_plan_b =vo.getPk_plan_b();
            if(!flag){//���ȡ�����
            	isExitToBill(pk_plan_b,vo.getInvcode());
			}
			param.addParam(reserve2);
			param.addParam(pk_plan_b);
			getPubBO().executeUpdate(sql,param);
			param.clearParams();
		}
	}
	
	public void isExitToBill(String pk_planother_b,String invcode) throws BusinessException{
		String sql = " select count(*) from to_bill_b  where csourcebid = '"+pk_planother_b+"' and isnull(dr,0)=0 ";
		String len = PuPubVO.getString_TrimZeroLenAsNull(getPubBO().executeQuery(sql,HgBsPubTool.COLUMNPROCESSOR));
		if(Integer.parseInt(len)>0){
			throw new BusinessException("���"+invcode+"�Ѿ������õļƻ�");
		}
	}

}
