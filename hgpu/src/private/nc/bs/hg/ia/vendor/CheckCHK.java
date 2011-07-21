package nc.bs.hg.ia.vendor;
import nc.bs.dao.BaseDAO;
import nc.bs.hg.pu.pub.HgBsPubTool;
import nc.bs.trade.business.IBDBusiCheck;
import nc.bs.trade.comchkref.CheckRef;
import nc.ui.hg.ia.vendor.BalFreezeVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.trade.pub.IBDACTION;

public class CheckCHK implements IBDBusiCheck, IBDACTION {

	CheckRef check = null;
	
	/**
	 * CostProjectBsBackCHK 构造子注释
	 */
	public CheckCHK() {
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
		BalFreezeVO[] bvos = (BalFreezeVO[])vo.getChildrenVO();
		
		if (bvos == null || bvos.length == 0) 
			return;
		
		BalFreezeVO bvo = bvos[0];
		String custbasid = PuPubVO.getString_TrimZeroLenAsNull(bvo.getCcustbasid());
		String custmanid = PuPubVO.getString_TrimZeroLenAsNull(bvo.getCcustmanid());
		String szxmid = PuPubVO.getString_TrimZeroLenAsNull(bvo.getSzxmid());
		String pk_corp = PuPubVO.getString_TrimZeroLenAsNull(bvo.getPk_corp());
		String pk_balfreeze = PuPubVO.getString_TrimZeroLenAsNull(bvo.getPk_balfreeze());
		BaseDAO dao = new BaseDAO();
		StringBuffer  str= new StringBuffer();
		str.append("select count(0) from hg_balfreeze where ");
		str.append(" (ccustbasid = '" + custbasid + "' and ccustmanid = '" + custmanid
				+ "' and szxmid = '" + szxmid + "') and isnull(dr,0) = 0 " +
						"and pk_corp = '"+pk_corp+"'");
		
		if(pk_balfreeze!=null)
			str.append(" and pk_balfreeze <> '" + pk_balfreeze + "' ");
		
		String len = PuPubVO.getString_TrimZeroLenAsNull(dao.executeQuery(str.toString(), HgBsPubTool.COLUMNPROCESSOR));
		
		if(Integer.parseInt(len)>0){
			throw new BusinessException(" 该供应商已经存在");
		}
	}
	
	private void onCheckDelete(HYBillVO vo) throws Exception {

	}

	private void checkTS(String pk, String tsOld, Class className)
			throws Exception {

	}
}
