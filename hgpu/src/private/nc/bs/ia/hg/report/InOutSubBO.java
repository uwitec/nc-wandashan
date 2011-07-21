package nc.bs.ia.hg.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nc.bd.accperiod.AccountCalendar;
import nc.bd.accperiod.AccperiodmonthAccessor;
import nc.bs.dao.BaseDAO;
import nc.bs.hg.pu.pub.HgBsPubTool;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.util.SQLHelper;
import nc.vo.bd.period2.AccperiodmonthVO;
import nc.vo.hg.ia.report.IaInvInOutReportVO;
import nc.vo.hg.ia.report.InOutSumPubVO;
import nc.vo.hg.pu.pub.HgPubTool;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.voutils.VOUtil;

public class InOutSubBO {
	
	private BaseDAO dao = null;
	private BaseDAO getDao(){
		if(dao == null){
			dao = new BaseDAO();
		}
		return dao;
	}
	
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）
	 * 2011-6-20下午03:50:41
	 * @param cons 用户定义查询条件
	 * @param isinvcl 是否按大类汇总查询
	 * @throws BusinessException
	 */
	public IaInvInOutReportVO[] queryInOutData(ConditionVO[] cons,UFBoolean isinvcl) throws BusinessException{
//		查询
		
		if(cons == null || cons.length == 0)
			return null;
		
		String monthid = null;
		AccountCalendar calendar = null;
		InOutSumPubVO.setIsInvcl(isinvcl);
		for(ConditionVO con:cons){
			if(con.getFieldCode().equalsIgnoreCase("accountdate")){//会计月
				monthid = PuPubVO.getString_TrimZeroLenAsNull(con.getValue());
				if(monthid == null)
					throw new BusinessException("待查询的会计月为空");
				calendar = AccountCalendar.getInstanceByAccperiodMonth(monthid);
			}
		}
		
		if(calendar == null)
			throw new BusinessException("待查询的会计月为空");
		
		
//		1、查询期初、期末结存
		String condition = InOutSumPubVO.dealCons(cons);
		IaInvInOutReportVO[] datas = dealDatas(getInitDatas(calendar, condition), getEndDatas(calendar, condition));
		
		VOUtil.ascSort(datas, InOutSumPubVO.sort_fields);
		
		String sdate1 = calendar.getMonthVO().getBegindate().toString();//开始日期
		String sdate2 = calendar.getMonthVO().getEnddate().toString();//截止日期		
		
		IaInOutSumBOFactory fac = new IaInOutSumBOFactory();
		
//		查询发票数量和金额
		fac.getQryBO(IaInOutSumBOFactory.type_in_invoice, getDao()).getDatas(datas,sdate1, sdate2, cons);
//		本期暂估数量金额
		fac.getQryBO(IaInOutSumBOFactory.type_in_estimate, getDao()).getDatas(datas,sdate1, sdate2, cons);
//		冲以前月份数量金额
		fac.getQryBO(IaInOutSumBOFactory.type_in_lastestimate, getDao()).getDatas(datas,sdate1, sdate2, cons);
//		产成品入库数量和金额
		fac.getQryBO(IaInOutSumBOFactory.type_in_product, getDao()).getDatas(datas,sdate1, sdate2, cons);
////		-----------------------------------------------------
//		外销出库数量和金额
		fac.getQryBO(IaInOutSumBOFactory.type_out_sale, getDao()).getDatas(datas,sdate1, sdate2, cons);
//		
//		内销数量金额
		fac.getQryBO(IaInOutSumBOFactory.type_out_allo, getDao()).getDatas(datas,sdate1, sdate2, cons);
//		
//		自用数量金额
		fac.getQryBO(IaInOutSumBOFactory.type_out_self, getDao()).getDatas(datas,sdate1, sdate2, cons);
////		调价价差金额
//		过滤空数量行
		if(datas == null || datas.length == 0)
			return null;
		UFDouble nallnum = null;
		List<IaInvInOutReportVO> ldata = new ArrayList<IaInvInOutReportVO>();
		for(IaInvInOutReportVO data:datas){		
			nallnum = UFDouble.ZERO_DBL;
			nallnum = nallnum.add(PuPubVO.getUFDouble_NullAsZero(data.getNinitnum()))
			.add(PuPubVO.getUFDouble_NullAsZero(data.getNabnum()))
			.add(PuPubVO.getUFDouble_NullAsZero(data.getNinvoicenum()))
			.add(PuPubVO.getUFDouble_NullAsZero(data.getNestimatenum()))
			.add(PuPubVO.getUFDouble_NullAsZero(data.getNlastestnum()))
			.add(PuPubVO.getUFDouble_NullAsZero(data.getNproductnum()))
			.add(PuPubVO.getUFDouble_NullAsZero(data.getNsaleoutnum()))
			.add(PuPubVO.getUFDouble_NullAsZero(data.getNalloutnum()))
			.add(PuPubVO.getUFDouble_NullAsZero(data.getNselfoutnum()));
			if(nallnum.doubleValue()>0)
				ldata.add(data);
		}
		IaInvInOutReportVO[] newdatas = null;
		if(ldata.size() > 0)			
			newdatas = ldata.toArray(new IaInvInOutReportVO[0]);
		return newdatas;
	}
	
	private IaInvInOutReportVO[] getInitDatas(AccountCalendar calendar,String condition) throws BusinessException{
		//		calendar.get
		AccperiodmonthVO month = AccperiodmonthAccessor.getInstance()
		.queryFormerAccperiodmonthVO(
				calendar.getMonthVO().getPk_accperiodscheme(),
				calendar.getYearVO().getPeriodyear(), calendar.getMonthVO().getMonth());
		String whereSql = null;
		if(month == null)
			whereSql = " caccountyear = '2011' and caccountmonth = '00'" ;//期初月份不存在
		else{
			String syear = month.getBegindate().toString().split("-")[0];
			whereSql = " caccountyear = '"+syear+"' and caccountmonth = '"+month.getMonth()+"'" ;
		}
		
		if(!checkIsEnd(whereSql)){//结账
//			如果未结账   从流水单据上汇总
			
			return null;
		}
		
		
		StringBuffer sql = new StringBuffer("select h.pk_corp,h.crdcenterid,sum(h.nabnum) ninitnum,sum(h.nabnum * h.nplanedprice) ninitmny" +
				", sum((man.planprice-h.nplanedprice)*h.nabnum) ndeparturemny ");
		if(!InOutSumPubVO.isinvcl.booleanValue()){
			sql.append(",inv.pk_invbasdoc cinvbasid,h.cinventoryid");
		}else{
			sql.append(",substr(invcode,0,2) cinvclid");
		}
				sql.append(" from ia_monthledger h " +
		        " inner join bd_invmandoc man on man.pk_invmandoc = h.cinventoryid "+
		        " inner join bd_invbasdoc inv on inv.pk_invbasdoc = man.pk_invbasdoc "+
				" where  h.frecordtypeflag = 3 and h.pk_corp = '"+SQLHelper.getCorpPk()+"' and isnull(h.dr,0) = 0 and coalesce(btryflag,'N') = 'N' and "+whereSql);
		if(PuPubVO.getString_TrimZeroLenAsNull(condition)!=null)
			sql.append(" and "+condition);
		sql.append(" group by h.pk_corp,h.crdcenterid");
		if(InOutSumPubVO.isinvcl.booleanValue()){
			sql.append(",substr(invcode,0,2)");
		}else{
			sql.append(",inv.pk_invbasdoc,h.cinventoryid");
		}
		List ldata = (List)getDao().executeQuery(sql.toString(), new BeanListProcessor(IaInvInOutReportVO.class));
		if(ldata == null || ldata.size() ==0)
			return null;
		return (IaInvInOutReportVO[])ldata.toArray(new IaInvInOutReportVO[0]);
	}
	
	private boolean checkIsEnd(String condition) throws BusinessException{
		String sql = "select count(0) from ia_monthledger where isnull(dr,0) = 0 and coalesce(btryflag,'N') = 'N' and "
			+condition+" and pk_corp = '"+SQLHelper.getCorpPk()+"'";
		int flag = PuPubVO.getInteger_NullAs(getDao().executeQuery(sql, HgBsPubTool.COLUMNPROCESSOR), -1);
		if(flag < 1){
			return false;
		}
		return true;
//		if(flag>1){
//			throw new BusinessException("本月结账数据异常");
//		}
	}
	
	private IaInvInOutReportVO[] getEndDatas(AccountCalendar calendar,String condition) throws BusinessException{
		String whereSql = " caccountyear = '"+calendar.getYearVO().getPeriodyear()+"' and caccountmonth = '"
		+calendar.getMonthVO().getMonth()+"'" ;
		
//		checkIsEnd(whereSql);
		if(!checkIsEnd(whereSql)){//结账
//			如果未结账   从流水单据上汇总
			
			throw new BusinessException("本会计月未结账");
		}
		
		StringBuffer sql = new StringBuffer("select h.pk_corp,h.crdcenterid,sum(h.nabnum) nabnum,sum(h.nabnum * h.nplanedprice) nabmny");
		if(!InOutSumPubVO.isinvcl.booleanValue()){
			sql.append(",inv.pk_invbasdoc cinvbasid,h.cinventoryid");
		}else{
			sql.append(",substr(invcode,0,2) cinvclid");
		}
		sql.append(" from ia_monthledger h " +
		        " inner join bd_invmandoc man on man.pk_invmandoc = h.cinventoryid "+
		        " inner join bd_invbasdoc inv on inv.pk_invbasdoc = man.pk_invbasdoc "+
				" where  isnull(h.dr,0) = 0 and h.pk_corp = '"+SQLHelper.getCorpPk()+"' and coalesce(btryflag,'N') = 'N' and h.frecordtypeflag = 3  and "+whereSql);
		if(PuPubVO.getString_TrimZeroLenAsNull(condition)!=null)
			sql.append(" and "+condition);
		sql.append(" group by h.pk_corp,h.crdcenterid");
		if(InOutSumPubVO.isinvcl.booleanValue()){
			sql.append(",substr(invcode,0,2)");
		}else{
			sql.append(",inv.pk_invbasdoc,h.cinventoryid");
		}
		List ldata = (List)getDao().executeQuery(sql.toString(), new BeanListProcessor(IaInvInOutReportVO.class));
		if(ldata == null || ldata.size() ==0)
			return null;
		return (IaInvInOutReportVO[])ldata.toArray(new IaInvInOutReportVO[0]);
	}
	
	private IaInvInOutReportVO[] dealDatas(IaInvInOutReportVO[] vos1,IaInvInOutReportVO[] vos2){
		if((vos1== null || vos1.length ==0)&&(vos2 == null || vos2.length ==0))
			return null;
		if(vos1== null || vos1.length ==0)
			return vos2;
		if(vos2 == null || vos2.length ==0)
			return vos1;
		java.util.Map<String, IaInvInOutReportVO> infor = new HashMap<String, IaInvInOutReportVO>();
		String key = null;
		for(IaInvInOutReportVO vo:vos2){
			key = HgPubTool.getString_NullAsTrimZeroLen(vo.getPk_corp())
			+HgPubTool.getString_NullAsTrimZeroLen(vo.getCrdcenterid())
			+HgPubTool.getString_NullAsTrimZeroLen(vo.getCinvclid())
			+HgPubTool.getString_NullAsTrimZeroLen(vo.getCinvbasid())
			+HgPubTool.getString_NullAsTrimZeroLen(vo.getCinventoryid());
			if(PuPubVO.getUFDouble_NullAsZero(vo.getNabnum()).equals(UFDouble.ZERO_DBL))
				vo.setNabmny(UFDouble.ZERO_DBL);
			infor.put(key, vo);			
		}
		IaInvInOutReportVO votmp = null;
		for(IaInvInOutReportVO vo:vos1){
			key = HgPubTool.getString_NullAsTrimZeroLen(vo.getPk_corp())
			+HgPubTool.getString_NullAsTrimZeroLen(vo.getCrdcenterid())
			+HgPubTool.getString_NullAsTrimZeroLen(vo.getCinvclid())
			+HgPubTool.getString_NullAsTrimZeroLen(vo.getCinvbasid())
			+HgPubTool.getString_NullAsTrimZeroLen(vo.getCinventoryid());
			if(infor.containsKey(key)){
				votmp = infor.get(key);
				votmp.setNinitnum(vo.getNinitnum());
				votmp.setNinitmny(vo.getNinitmny());
				votmp.setNdeparturemny(vo.getNdeparturemny());
				vo = votmp;
			}
			if(PuPubVO.getUFDouble_NullAsZero(vo.getNinitnum()).equals(UFDouble.ZERO_DBL))
				vo.setNinitmny(UFDouble.ZERO_DBL);
			infor.put(key, vo);			
		}
		return infor.values().toArray(new IaInvInOutReportVO[0]);
	}
	
//	private IaInvInOutReportVO[] getInitDatas(String date1) throws BusinessException{
//		StringBuffer str = new StringBuffer();
//		str.append("");
//	}
//	private IaInvInOutReportVO[] getEndDatas(String date2) throws BusinessException{
//		
//	}
	
}
