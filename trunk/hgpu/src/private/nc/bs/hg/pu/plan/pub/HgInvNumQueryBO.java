package nc.bs.hg.pu.plan.pub;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bd.accperiod.AccountCalendar;
import nc.bs.hg.pu.pub.HgBsPubTool;
import nc.vo.hg.pu.pub.HgPubConst;
import nc.vo.hg.pu.pub.HgPubTool;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.BillStatus;
import nc.vo.scm.pu.PuPubVO;

/**
 * 
 * @author zhf   �������ѯ
 *
 */
public class HgInvNumQueryBO {

	PlanPubBO querybo = null;
	public HgInvNumQueryBO(PlanPubBO bo){
		querybo = bo;		
	}
	public HgInvNumQueryBO(){}
	
	private PlanPubBO getPubBO(){
		if(querybo == null){
			querybo = new PlanPubBO();
		}
		return querybo;
	}
	
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ����ȡ��ǰ���  ��ֹ��denddate����ƽ��˵㣩 �Ŀ������
	 * 2012-2-24����09:56:22
	 * @param corpid
	 * @param sCalbodyid
	 * @param denddate
	 * @param invmanids
	 * @return
	 * @throws BusinessException
	 */
	public Map<String,UFDouble> getCurrentStock(String corpid,String sCalbodyid,AccountCalendar ac,String[] invmanids) throws BusinessException{

		if(PuPubVO.getString_TrimZeroLenAsNull(corpid)==null||PuPubVO.getString_TrimZeroLenAsNull(sCalbodyid)==null||
				ac == null||invmanids==null||invmanids.length==0){
			throw new BusinessException("��������쳣");
		}
//		AccountCalendar ac = AccountCalendar.getInstance();
//		ac.setDate(denddate);

		//�Ӵ��������ȡ  �������ڶ�Ӧ����µ� �����  ��Ϊ��ǰ����
		String sql = "select cinventoryid,sum(nabnum) nnum from  ia_generalledger where isnull(dr,0)=0 and pk_corp = '"+corpid+"'" +
		" and crdcenterid = '"+sCalbodyid+"' and caccountyear = '"+ac.getYearVO().getPeriodyear()+"' and caccountmonth = '"+ac.getMonthVO().getMonth()+"'" +
		" and coalesce(btryflag,'N') = 'N' and cinventoryid in "+getPubBO().getTempTaBO().getSubSql(invmanids)
		+" group by pk_corp,crdcenterid,cinventoryid";

		Object o = getPubBO().executeQuery(sql, HgBsPubTool.ARRAYLISTPROCESSOR);
		
		return dealNumQueryResult(o);
	}

	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ��ʣ����Դ
	 * 2012-2-24����01:28:46
	 * @param sLogCorp
	 * @param dDate
	 * @param invmanids
	 * @return
	 * @throws BusinessException
	 */
	public Map<String,UFDouble> getRemainNum(String sLogCorp,String sCalbodyid,AccountCalendar ac,String[] invmanids) throws BusinessException{
		//ʣ����Դ   ��ƻ�   �¼ƻ�  �ɹ��ƻ�     
		//		�жϹ�˾  �����  ��Ӧ��  ����  ʣ��ɹ���ͬ�� �� �ɹ��ƻ���   ������˾����   ʣ���¼ƻ�  ����������

		if(PuPubVO.getString_TrimZeroLenAsNull(sLogCorp)==null||PuPubVO.getString_TrimZeroLenAsNull(sCalbodyid)==null||ac == null||invmanids == null||invmanids.length==0)
			return null;

		String pocorp = getPubBO().getGatherPoCorp();
		if(PuPubVO.getString_TrimZeroLenAsNull(pocorp)==null)
			throw new BusinessException("ϵͳδ���ü��ɹ�˾");

//		AccountCalendar ac = AccountCalendar.getInstance();
//		ac.setDate(dDate);
		Object o = null;
		String sql;
		if(sLogCorp.equalsIgnoreCase(pocorp)){//ͳ��ʣ��ɹ���ͬ��  ���ǲɹ���ͬ�İ汾����   ���°汾 δ�ر� ������ �ĺ�ͬ��ʣ������

			sql ="select cmangid,sum(coalesce(nordernum,0.0) - coalesce(naccumstorenum,0.0)) num from po_order_b b inner join po_order h" +
			" on isnull(h.dr,0)=0 and isnull(b.dr,0)=0 where h.pk_corp = '"+pocorp+"' " +
					" and b.pk_reqstoorg = '"+sCalbodyid+"' and coalesce(bislatest,'N')='Y'" +
			" and h.caccountyear = '"+ac.getYearVO().getPeriodyear()+"' and h.forderstatus ="+BillStatus.AUDITED +
			" group by h.pk_corp,b.pk_reqstoorg,b.cmangid";

			o = getPubBO().executeQuery(sql, HgBsPubTool.ARRAYLISTPROCESSOR);
		}else{//ͳ���ɼ��ɹ�˾�������¼ƻ���    
			o = getMoreMonthsPreUseNum(pocorp,sCalbodyid, ac, invmanids, " and h.pk_corp = '"+sLogCorp+"'" );
		}	
		
		return dealNumQueryResult(o);
	}
	
	public Map<String,UFDouble>  getMoreMonthsPreUseNum(String sLogCorp,String sCalbodyid,AccountCalendar ac,String[] invmanids) throws BusinessException{
		return getMoreMonthsPreUseNum(sLogCorp, sCalbodyid, ac, invmanids,null);
	}

	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ����ȡ  ����Ԥ��  �¼���˾��ʣ����Դ ���� �ϼ���˾������Ԥ��
	 * 2012-2-24����12:53:16
	 * @param sLogCorp
	 * @param ac
	 * @param invmanids
	 * @return
	 * @throws BusinessException
	 */
	public Map<String,UFDouble>  getMoreMonthsPreUseNum(String sLogCorp,String sCalbodyid,AccountCalendar ac,String[] invmanids,String addWhereSql) throws BusinessException{
		StringBuffer strsql= new StringBuffer();
		strsql.append("select b.cinventoryid,sum(coalesce(b.nnum,0.0)) num from hg_planother_b b inner join hg_plan h" +
		" on h.pk_plan = b.pk_plan where isnull(h.dr,0)=0 and isnull(b.dr,0)=0 " );
		
		if(PuPubVO.getString_TrimZeroLenAsNull(addWhereSql)!=null){
			strsql.append(addWhereSql);
		}//		
		strsql.append(" and h.csupplycorpid = '"+sLogCorp+
		"' and (b.csupplycalbodyid = '"+sCalbodyid+"' or b.csupplycalbodyid  is null) " +
		" and h.cyear = '"+ac.getYearVO().getPeriodyear()+"'" +
		" and h.cmonth > '"+ac.getMonthVO().getMonth()+
		"' and b.cinventoryid in "+getPubBO().getTempTaBO().getSubSql(invmanids)+
		" and h.pk_billtype ='"+HgPubConst.PLAN_MONTH_BILLTYPE+"' " +
		" group by h.pk_corp,b.csupplycalbodyid,b.cinventoryid");

		return dealNumQueryResult(getPubBO().executeQuery(strsql.toString(), HgBsPubTool.ARRAYLISTPROCESSOR));
//		return o;
	}
	
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ������ͳ��
	 * 2012-2-24����01:05:33
	 * @param sLogCorp ͳ�ƹ�˾
	 * @param cyear ͳ�ƻ�����
	 * @param cbeginmonth ͳ�ƿ�ʼ����·�
	 * @param cendmonth ͳ�ƽ�ֹ����·�
	 * @return
	 * @throws BusinessException
	 */
	public Map<String,UFDouble>  getSumOutNum(String sLogCorp,String sCalbodyid,String[] invmanids,String cyear,String cbeginmonth,String cendmonth) throws BusinessException{
		if(PuPubVO.getString_TrimZeroLenAsNull(sLogCorp)==null||invmanids == null||invmanids.length == 0||PuPubVO.getString_TrimZeroLenAsNull(cyear)==null)
			return null;

		//		 ���ı�ǩ: ���ǩ���·�����   
		StringBuffer sqlstr = new StringBuffer();
		sqlstr.append(" select cinventoryid,sum(coalesce(noutnum,0.0)) from ic_month_recordsign" );
		sqlstr.append(" where pk_corp = '"+sLogCorp+"' and cinventoryid in"+getPubBO().getTempTaBO().getSubSql(invmanids));
		sqlstr.append(" and pk_calbody = '"+sCalbodyid+"'");
		sqlstr.append(" and isnull(dr,0) = 0");
        sqlstr.append(" and substr(dyearmonth,1,4) = '"+cyear+"'");
        if(PuPubVO.getString_TrimZeroLenAsNull(cbeginmonth)!=null){
        	sqlstr.append(" and substr(dyearmonth,6,2) >= '"+cbeginmonth+"'");
        }else{
        	sqlstr.append(" and substr(dyearmonth,6,2) >= '01'");
        }
        if(PuPubVO.getString_TrimZeroLenAsNull(cendmonth)!=null){
        	sqlstr.append(" and substr(dyearmonth,6,2) <= '"+cendmonth+"'");
        }else{
        	sqlstr.append(" and substr(dyearmonth,6,2) <= '12'");
        }
        
        sqlstr.append(" group by pk_corp,cinventoryid");
        
        Object o = getPubBO().executeQuery(sqlstr.toString(), HgBsPubTool.ARRAYLISTPROCESSOR);
        return dealNumQueryResult(o);
	}

	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ�������ƽ��ר�ý��������
	 * 2012-2-24����12:46:18
	 * @param o
	 * @return
	 * @throws BusinessException
	 */
	private Map<String, UFDouble> dealNumQueryResult(Object o) throws BusinessException{
		if(o==null)
			return null;
		List l = (List)o;
		if(l == null||l.size()==0)
			return null;
		java.util.Map<String,UFDouble> invStockInfor = new HashMap<String, UFDouble>();

		int len = l.size();
		Object[] os = null;
		for(int i = 0;i<len;i++){
			os = (Object[])l.get(i);
			invStockInfor.put(HgPubTool.getString_NullAsTrimZeroLen(os[0]), PuPubVO.getUFDouble_NullAsZero(os[1]));
		}
		return invStockInfor;	
	}
}
