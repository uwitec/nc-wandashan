package nc.bs.hg.pu.plan.year;

import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.hg.pu.pub.HgBsPubTool;
import nc.bs.trade.comsave.BillSave;
import nc.vo.hg.pu.plan.month.PlanOtherBVO;
import nc.vo.hg.pu.plan.year.PlanYearBVO;
import nc.vo.hg.pu.pub.HgPubConst;
import nc.vo.hg.pu.pub.HgPubTool;
import nc.vo.hg.pu.pub.PlanVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDate;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.relacal.SCMRelationsCal;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.trade.pub.IBillStatus;

public class YearPlanBO {
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）年计划弃审时校验
	 * 2011-1-29下午12:48:09
	 * @param uLogdate
	 * @param yearvo
	 * @param dao
	 * @throws BusinessException
	 */
	public void checkYearPlanOnUnapprove(UFDate uLogdate,PlanVO yearvo,BaseDAO dao) throws BusinessException{
		int planyear = Integer.valueOf(yearvo.getCyear());
		int nowyear = uLogdate.getYear();
		int flag = nowyear - planyear;
		if(flag<0){
			throw new BusinessException("计划已执行完毕");
		}
		if(flag==0){
			int month = uLogdate.getMonth();
			if(month>1)
				throw new BusinessException("计划已部分执行不能弃审操作");
			String sql = " select  count(*) from hg_planother_b b inner join HG_PLAN h on h.pk_plan = b.pk_plan" +
			" where isnull(h.dr,0)=0 and isnull(b.dr,0)=0 and h.cmonth = '01' and h.pk_billtype = '"+HgPubConst.PLAN_MONTH_BILLTYPE+"'" +
			" and b.cnextbillid = '"+yearvo.getPrimaryKey()+"' and coalesce(nouttotalnum,0)>0";//检查 1月份是否已经执行
			Object o = dao.executeQuery(sql, HgBsPubTool.COLUMNLISTPROCESSOR);
			if(PuPubVO.getInteger_NullAs(o, HgPubConst.IPRAYTYPE).intValue()>0)
				throw new BusinessException("计划已部分执行不能弃审操作");
		}
	}
	
	public void splitYearPlan2MonthPlan(HYBillVO yearvo,String corp) throws BusinessException{
		if(yearvo == null)
			throw new BusinessException("出入数据为空");
		//生成月计划  12个  
		//		表头转换 不需要转换  直接使用  copy 12份  补录月份即可
		//		表体转换
		HYBillVO[] months = new HYBillVO[12];
		//	    AggPlanVO tmpmonth = null;
		PlanVO tmphead = null;
		PlanYearBVO[] bodys = (PlanYearBVO[])yearvo.getChildrenVO();;
		PlanOtherBVO[] tmpbodys = null;
		PlanOtherBVO tmpbody = null;
		String[] names = null;
		int index = 0;

		//预申请单据号   12个
		String[] billcodes = HgPubTool.getBatchBillNo(HgPubConst.PLAN_MONTH_BILLTYPE, corp, null, 12);

		for(int i = 1;i<13;i++){
			tmphead = (PlanVO)yearvo.getParentVO().clone();
			tmphead.setCmonth(getMonth(i));
			tmphead.setDmakedate(tmphead.getDapprovedate());
			tmphead.setDbilldate(tmphead.getDapprovedate());
			tmphead.setPk_billtype(HgPubConst.PLAN_MONTH_BILLTYPE);
			tmphead.setVoperatorid(tmphead.getVapproveid());
			tmphead.setStatus(VOStatus.NEW);
			tmphead.setPrimaryKey(null);
			tmphead.setVbillno(billcodes[i-1]);
			tmphead.setCsourcebillno(PuPubVO.getString_TrimZeroLenAsNull(yearvo.getParentVO().getAttributeValue("vbillno")));
			tmphead.setVbillstatus(IBillStatus.FREE);

			tmpbodys = new PlanOtherBVO[bodys.length];
			index = 0;
			for(PlanYearBVO yearb:bodys){
				tmpbody = new PlanOtherBVO();
				names = tmpbody.getAttributeNames();
				for(String name:names){
					tmpbody.setAttributeValue(name, yearb.getAttributeValue(name));
					tmpbody.setNnum(
							PuPubVO.getUFDouble_NullAsZero(
									yearb.getAttributeValue("nmonnum"+String.valueOf(i))));
				}
				tmpbody.setStatus(VOStatus.NEW);
				tmpbody.setCnextbillbid(yearb.getPrimaryKey());
				tmpbody.setCnextbillid(yearb.getPk_plan());
				tmpbody.setPrimaryKey(null);
				tmpbodys[index] = tmpbody;
				index ++;
			}

			//需要联动更改    辅数量   计划价  计划金额

			SCMRelationsCal.calculate(tmpbodys, HgPubTool.iaPrior, "nnum", HgPubTool.m_iDescriptions, HgPubTool.m_saKey);

			months[i-1] = new HYBillVO();
			months[i-1].setParentVO(tmphead);
			months[i-1].setChildrenVO(tmpbodys);
		}

		if(months == null||months.length == 0)
			return;
		//		组合
		//		保存月计划   郑  提供保存月计划  接口  支持批存入   自动审批通过
		BillSave savebo = new BillSave();

		for(HYBillVO month:months){
			savebo.saveBill(month);
		}
	}	
	
	
	private String getMonth(int month){
		String smonth = String.valueOf(month);
		if(month<10)
			smonth = "0"+smonth;
		return smonth;
	}

	
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）删除年计划的12个月计划
	 * 2011-1-29下午01:47:26
	 * @param splanyearid
	 * @param dao
	 * @throws BusinessException
	 */
	public void delMonthPlans(String splanyearid,BaseDAO dao) throws BusinessException{
		if(PuPubVO.getString_TrimZeroLenAsNull(splanyearid)==null)
			return;
		//先查询出 12个月计划的 主表id
		String sql = "select distinct h.pk_plan from hg_plan h inner join hg_planother_b b on h.pk_plan = b.pk_plan" +
				" where isnull(h.dr,0)=0 and isnull(b.dr,0)=0 and h.pk_billtype = '"+HgPubConst.PLAN_MONTH_BILLTYPE+"'" +
						" and b.cnextbillid = '"+splanyearid+"'";
		Object o = dao.executeQuery(sql, HgBsPubTool.COLUMNLISTPROCESSOR);
		if(o == null)
			return;
		List l = (List)o;
		if(l.size()==0)
			return;
		if(l.size()!=12)
			throw new BusinessException("获取月计划异常");
		//先删除子表
		String subsql = HgPubTool.getSubSql((String[])l.toArray(new String[0]));
		sql = " update hg_planother_b set dr=1 where isnull(dr,0)=0 and pk_plan in "+subsql;
		//删除主表
		dao.executeUpdate(sql);
		sql = " update hg_plan set dr=1 where isnull(dr,0)=0 and pk_plan in "+subsql;
	}
}
