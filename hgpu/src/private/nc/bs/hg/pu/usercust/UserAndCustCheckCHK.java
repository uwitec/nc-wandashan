package nc.bs.hg.pu.usercust;
import nc.bs.dao.BaseDAO;
import nc.bs.hg.pu.pub.HgBsPubTool;
import nc.bs.trade.business.IBDBusiCheck;
import nc.bs.trade.comchkref.CheckRef;
import nc.vo.hg.pu.usercust.UserAndCustVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.trade.pub.IBDACTION;

public class UserAndCustCheckCHK implements IBDBusiCheck, IBDACTION {

	CheckRef check = null;
	

	/**
	 * CostProjectBsBackCHK 构造子注释
	 */
	public UserAndCustCheckCHK() {
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
		UserAndCustVO[] bvos = (UserAndCustVO[])vo.getChildrenVO();
		if (bvos != null && bvos.length > 0) {
			UserAndCustVO bvo = bvos[0];
			String pk_corp = PuPubVO.getString_TrimZeroLenAsNull(bvo.getPk_corp());
			String pk_cust = PuPubVO.getString_TrimZeroLenAsNull(bvo.getPk_cust());
			String pk =PuPubVO.getString_TrimZeroLenAsNull(bvo.getPk());
			String pk_user =PuPubVO.getString_TrimZeroLenAsNull(bvo.getPk_user());
			BaseDAO dao = new BaseDAO();
			StringBuffer  str= new StringBuffer();
			str.append("select count(*) from HG_USERANDCUST where ");
			str.append(" (pk_corp = '" + pk_corp
					+ "' and pk_cust = '" + pk_cust + "' and pk_user ='"+pk_user+"') and isnull(dr,0) = 0 ");
			if(pk!=null)
				str.append(" and pk  <> '" + pk + "' ");
			
			String len = PuPubVO.getString_TrimZeroLenAsNull(dao.executeQuery(str.toString(), HgBsPubTool.COLUMNPROCESSOR));
			
			if(Integer.parseInt(len)>0)
				throw new BusinessException("该供应商与登录人之间的关系已存在");
		}
	}

	private void onCheckDelete(HYBillVO vo) throws Exception {

	}

	private void checkTS(String pk, String tsOld, Class className)
			throws Exception {

	}
}
