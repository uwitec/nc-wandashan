package nc.bs.hg.pu.plan.balance;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.trade.pub.IBillStatus;
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
		strb.append(" and h.vbillstatus = '"+IBillStatus.CHECKPASS+"'"); //ֻ�ܲ������ͨ����
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
		
		int size= aldata.size();
		for(int i=0;i<size;i++){
			
			PlanMonDealVO vo =(PlanMonDealVO)aldata.get(i);
			String inv =PuPubVO.getString_TrimZeroLenAsNull(vo.getInvcode());
//			String cnextbillid =PuPubVO.getString_TrimZeroLenAsNull(vo.getCnextbillid());
//			String cnextbillbid =PuPubVO.getString_TrimZeroLenAsNull(vo.getCnextbillbid());
			String mon = PuPubVO.getString_TrimZeroLenAsNull(vo.getCmonth());
			if(mon.compareTo("11")<0){
				mon = "0"+(Integer.parseInt(mon)-1);
			}else{
				mon=PuPubVO.getString_TrimZeroLenAsNull((Integer.parseInt(mon)-1));
			}
			String year = PuPubVO.getString_TrimZeroLenAsNull(vo.getCyear());
			String corp = PuPubVO.getString_TrimZeroLenAsNull(vo.getPk_corp());
			String pk_invbasdoc = PuPubVO.getString_TrimZeroLenAsNull(vo.getPk_invbasdoc());
//			checkUpdateMon(inv,cnextbillid,cnextbillbid);
			String vreserve3 = null;
			if(isbalance){
				if(vo.getNreserve10()==null)
					throw new BusinessException("���"+inv+"ƽ������Ϊ��");
				//modify by  zhw   ����У��ƽ�������� �ƻ����Ĺ�ϵ   У��ƽ����������ʣ�����Ĺ�ϵ
//				if((PuPubVO.getUFDouble_NullAsZero(vo.getNreserve10()).compareTo(vo.getNnum()))>0)
//					throw new BusinessException("���"+inv+"ƽ���������ڼƻ���������");
				vreserve3 ="Y";
			}else{
//				checkNouttotalnum(inv,cnextbillid,cnextbillbid,mon,year);
			    vreserve3 ="N";
			}
			
			String sql="update hg_planother_b set vreserve4 ='"+vo.getVreserve4()+"',vreserve5='"+vo.getVreserve5()+"',nreserve10="+vo.getNreserve10()+" where pk_planother_b = '"+vo.getPk_plan_b()+"'";
	        String sql1 =" update hg_planother_b set vreserve3='"+vreserve3+"' where pk_planother_b in (select b.pk_planother_b from hg_plan h, hg_planother_b b "+
	                      " where h.pk_plan = b.pk_plan  and nvl(h.dr, 0) = 0 and nvl(b.dr, 0) = 0 and h.cyear = '"+year+"' and h.pk_corp = '"+corp+"' and h.pk_billtype = 'HG02' "+
	                      " and h.cmonth ='"+mon+"' and b.pk_invbasdoc = '"+pk_invbasdoc+"' ) ";
			getPubBO().executeUpdate(sql);
			getPubBO().executeUpdate(sql1);
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
	public ArrayList<String> checkBalance(List aldata) throws BusinessException {
		
		if(aldata == null||aldata.size() == 0){
			throw new BusinessException("����ƻ�����Ϊ��");
		}
		
		int asize= aldata.size();
		PlanMonDealVO[] vos =new PlanMonDealVO[asize];
		for(int i=0;i<asize;i++){
			PlanMonDealVO vo =(PlanMonDealVO)aldata.get(i);
			vos[i]=vo;
			
		}
		VOUtil.sort(vos, new String[] { "pk_invbasdoc" },new int[] { VOUtil.DESC });
		
		ArrayList<String> als = new ArrayList<String>();
		ArrayList<UFDouble> alu = new ArrayList<UFDouble>();
	
		for (PlanMonDealVO bvo : vos) {
			als.add(bvo.getPk_invbasdoc());//���id
			alu.add(bvo.getNreserve10());//��ǰ����¼ƻ�ƽ����
		}
		
		if (als == null || als.size() == 0)
			return null;

		Map aluy = getYearNum(vos[0], als);// ��ƻ�����
		Map alum = getMonUsedNum(vos[0], als);// �¼ƻ��ۼ���
		ArrayList<String> al = new ArrayList<String>();

		int size = als.size();
		for (int i = 0; i < size; i++) {
			if (aluy == null || aluy.size() == 0) {
				throw new BusinessException("���д������ƻ��������ڡ�\n");
			}

			UFDouble ny = PuPubVO.getUFDouble_NullAsZero(aluy.get(als.get(i)));

			if (ny.equals(UFDouble.ZERO_DBL))
				al.add(als.get(i) + "&��ƻ��������ڡ�\n");
			else {
				UFDouble nre = UFDouble.ZERO_DBL;
				
				if (alum == null || alum.size() == 0)
					nre = ny.sub(PuPubVO.getUFDouble_NullAsZero(alu.get(i)));
				else
					nre = ny.sub(PuPubVO.getUFDouble_NullAsZero(alu.get(i)))
							.sub(PuPubVO.getUFDouble_NullAsZero(alum.get(als.get(i))));
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
    private Map getYearNum(PlanMonDealVO head, ArrayList<String> als)
            throws DAOException {

        String sql = " select b.pk_invbasdoc,b.nnum from hg_plan h join hg_planyear_b b on h.pk_plan = b.pk_plan where nvl(h.dr, 0) =0 and nvl(b.dr, 0) = 0 "
                + " and h.pk_corp = '"+ head.getPk_corp()+ "'  and h.vbillstatus =1  and h.pk_billtype='HG01' and h.cyear = '" + head.getCyear() + "'and h.capplydeptid = '" + head.getCapplydeptid()
                +"' and b.pk_invbasdoc in "+ HgPubTool.getSubSql(als.toArray(new String[0]));
        
        Object o =  getPubBO().getBaseDao().executeQuery(sql.toString(), HgBsPubTool.ARRAYLISTPROCESSOR);
        
        if(o==null)
            return null;
        ArrayList al =(ArrayList)o;
        
        if(al==null || al.size()==0)
            return null;
        
        int size = al.size();
        
        Map  map = new HashMap();
        for(int i=0;i<size;i++){
            Object o1 = al.get(i);
            if(o1!=null){
                Object[] os = (Object[])o1;
                if(os != null && os.length>0){
                    map.put(os[0],os[1]);
                }
            }
        }
        return map;
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
    private Map getMonUsedNum(PlanMonDealVO head, ArrayList<String> als)
            throws DAOException {

        String sql = " select  b.pk_invbasdoc,sum(coalesce(b.nouttotalnum,0)) from hg_plan h join hg_planother_b b on h.pk_plan = b.pk_plan "
                + " where nvl(h.dr, 0) =0 and nvl(b.dr, 0) = 0 and h.pk_corp = '"+ head.getPk_corp()+ "' and h.pk_billtype = 'HG02'" 
                + " and h.cyear = '" + head.getCyear() + "' and h.cmonth ='"+head.getCmonth()+"'and h.capplydeptid = '" + head.getCapplydeptid()
                + " ' and b.pk_invbasdoc in " + HgPubTool.getSubSql(als.toArray(new String[0]))+ " group by  b.pk_invbasdoc ";

       Object o =  getPubBO().getBaseDao().executeQuery(sql.toString(), HgBsPubTool.ARRAYLISTPROCESSOR);

        if(o==null)
            return null;
        ArrayList al =(ArrayList)o;
        
        if(al==null || al.size()==0)
            return null;
        
        int size = al.size();
        
        Map  map = new HashMap();
        for(int i=0;i<size;i++){
            Object o1 = al.get(i);
            if(o1!=null){
                Object[] os = (Object[])o1;
                if(os != null && os.length>0){
                    map.put(os[0],os[1]);
                }
            }
        }
        return map;
    }

}
