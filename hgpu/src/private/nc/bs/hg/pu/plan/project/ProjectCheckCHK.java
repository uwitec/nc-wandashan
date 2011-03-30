package nc.bs.hg.pu.plan.project;
import nc.bs.dao.BaseDAO;
import nc.bs.hg.pu.pub.HgBsPubTool;
import nc.bs.trade.business.IBDBusiCheck;
import nc.bs.trade.comchkref.CheckRef;
import nc.vo.hg.pu.plan.project.PlanProjectVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.trade.pub.IBDACTION;

public class ProjectCheckCHK implements IBDBusiCheck, IBDACTION {

	CheckRef check = null;


	/**
	 * CostProjectBsBackCHK 构造子注释
	 */
	public ProjectCheckCHK() {
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
		PlanProjectVO[] bvos = (PlanProjectVO[])vo.getChildrenVO();
		if (bvos != null && bvos.length > 0
				&& bvos[0].getStatus() == VOStatus.NEW) {
			PlanProjectVO bvo = bvos[0];
			String code = PuPubVO.getString_TrimZeroLenAsNull(bvo.getVprojectcode());
			String name = PuPubVO.getString_TrimZeroLenAsNull(bvo.getVprojectname());
			
			BaseDAO dao = new BaseDAO();
			StringBuffer  str= new StringBuffer();
			str.append("select count(*) from HG_PLANPROJECT where ");
			str.append(" (vprojectname = '" + name
					+ "' and vprojectcode = '" + code
					+ "') and isnull(dr,0) = 0 ");
			
			String len = PuPubVO.getString_TrimZeroLenAsNull(dao.executeQuery(str.toString(), HgBsPubTool.COLUMNPROCESSOR));
			
			if(Integer.parseInt(len)>0)
				throw new BusinessException("计划项目已经存在！");
		}
	}

	private void onCheckDelete(HYBillVO vo) throws Exception {

	}

	private void checkTS(String pk, String tsOld, Class className)
			throws Exception {

	}
}
