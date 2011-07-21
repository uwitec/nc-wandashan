package nc.bs.hg.pu.check.fund;
import nc.bs.dao.BaseDAO;
import nc.bs.hg.pu.pub.HgBsPubTool;
import nc.bs.trade.business.IBDBusiCheck;
import nc.bs.trade.comchkref.CheckRef;
import nc.vo.hg.pu.check.fund.FUNDSETVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.trade.pub.IBDACTION;

public class FundCheckCHK implements IBDBusiCheck, IBDACTION {

	CheckRef check = null;
	
	/**
	 * CostProjectBsBackCHK 构造子注释
	 */
	public FundCheckCHK() {
		super();
		
	}
	
/**
 * 校验
 */
	public void check(int intBdAction, AggregatedValueObject vo, Object userObj)
			throws Exception {
		switch (intBdAction) {
		case DELETE:
			onCheckDelete((HYBillVO) vo);
			break;
		case SAVE:
			onCheckSave((HYBillVO) vo);
			break;
		}

	}

	public void dealAfter(int intBdAction, AggregatedValueObject billVo,
			Object userObj) throws Exception {

	}

	private void onCheckSave(HYBillVO vo) throws Exception {
		FUNDSETVO[] bvos = (FUNDSETVO[])vo.getChildrenVO();
		
		if (bvos == null || bvos.length == 0) 
			return;
		
		FUNDSETVO bvo = bvos[0];
		String cdeptid = PuPubVO.getString_TrimZeroLenAsNull(bvo.getCdeptid());
		String pk_corp = PuPubVO.getString_TrimZeroLenAsNull(bvo.getPk_corp());
		String imonth=PuPubVO.getString_TrimZeroLenAsNull(bvo.getImonth());
		String cyear =PuPubVO.getString_TrimZeroLenAsNull(bvo.getCyear());
		String ifundtype =PuPubVO.getString_TrimZeroLenAsNull(bvo.getIfundtype());
		String vdef1 = PuPubVO.getString_TrimZeroLenAsNull(bvo.getVdef1());
		String vdef2 = PuPubVO.getString_TrimZeroLenAsNull(bvo.getVdef2());
		String pk_fundset= PuPubVO.getString_TrimZeroLenAsNull(bvo.getPk_fundset());
		BaseDAO dao = new BaseDAO();
		StringBuffer  str= new StringBuffer();
		str.append("select count(*) from hg_fundset where ");
		str.append(" (imonth = " + imonth + " and cyear = '" + cyear
				+ "' and ifundtype = '" + ifundtype + "') and isnull(dr,0) = 0 " +
						"and vdef2 = '"+vdef2+"'");
		
		if(pk_fundset!=null)
			str.append(" and pk_fundset <> '" + pk_fundset + "' ");
		
		if(pk_corp!=null)
			str.append(" and pk_corp = '" + pk_corp + "' ");
		else
			str.append(" and (pk_corp = '' or pk_corp is null) ");
		
		if(cdeptid!=null)
			str.append(" and cdeptid = '" + cdeptid + "' ");
		else
			str.append(" and (cdeptid = '' or cdeptid is null) ");
		
		if(vdef1!=null)
			str.append(" and vdef1 = '" + vdef1 + "' ");
		else
			str.append(" and (vdef1 = '' or vdef1 is null) ");
		
		String len = PuPubVO.getString_TrimZeroLenAsNull(dao.executeQuery(str.toString(), HgBsPubTool.COLUMNPROCESSOR));
		
		if(Integer.parseInt(len)>0){
			if(vdef1!=null)
				throw new BusinessException("客户,年度和月份不能重复！");
			
			throw new BusinessException(" 公司,部门,年度和月份不能重复！");
		}
	}
	
	private void onCheckDelete(HYBillVO vo) throws Exception {

	}

	private void checkTS(String pk, String tsOld, Class className)
			throws Exception {

	}
}
