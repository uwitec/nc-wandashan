package nc.bs.hg.pu.check.oldmaterials;
import nc.bs.dao.BaseDAO;
import nc.bs.hg.pu.pub.HgBsPubTool;
import nc.bs.trade.business.IBDBusiCheck;
import nc.bs.trade.comchkref.CheckRef;
import nc.vo.hg.pu.check.oldmaterials.OLDMATERALSVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.trade.pub.IBDACTION;

public class OldmaterialsCheckCHK implements IBDBusiCheck, IBDACTION {

	CheckRef check = null;
	

	/**
	 * CostProjectBsBackCHK 构造子注释
	 */
	public OldmaterialsCheckCHK() {
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
		OLDMATERALSVO[] bvos = (OLDMATERALSVO[])vo.getChildrenVO();
		if (bvos != null && bvos.length > 0) {
			OLDMATERALSVO bvo = bvos[0];
			String coldbasid = PuPubVO.getString_TrimZeroLenAsNull(bvo.getColdbasid());
			String coldmanid = PuPubVO.getString_TrimZeroLenAsNull(bvo.getColdmanid());
			String pk_oldmaterials =PuPubVO.getString_TrimZeroLenAsNull(bvo.getPk_oldmaterials());
			BaseDAO dao = new BaseDAO();
			StringBuffer  str= new StringBuffer();
			str.append("select count(*) from HG_OLDMATERIALS where ");
			str.append(" (coldbasid = '" + coldbasid
					+ "' and coldmanid = '" + coldmanid + "') and isnull(dr,0) = 0 ");
			if(pk_oldmaterials!=null)
				str.append(" and pk_oldmaterials <> '" + pk_oldmaterials + "' ");
			
			String len = PuPubVO.getString_TrimZeroLenAsNull(dao.executeQuery(str.toString(), HgBsPubTool.COLUMNPROCESSOR));
			
			if(Integer.parseInt(len)>0)
				throw new BusinessException(" 旧物资已经存在！");
		}
	}

	private void onCheckDelete(HYBillVO vo) throws Exception {

	}

	private void checkTS(String pk, String tsOld, Class className)
			throws Exception {

	}
}
