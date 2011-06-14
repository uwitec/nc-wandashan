package nc.bs.wds.load.teamdoc;

import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.trade.business.IBDBusiCheck;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.trade.pub.IBDACTION;
import nc.vo.wds.dm.sendinvdoc.SendinvdocVO;
import nc.vo.wds.load.teamdoc.TeamdocBVO;
import nc.vo.wds.load.teamdoc.TeamdocHVO;

public class BSCheck implements IBDBusiCheck {
	private BaseDAO dao;

	private BaseDAO getDao() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}

	public void check(int intBdAction, AggregatedValueObject vo, Object userObj)
			throws Exception {
		if (intBdAction != IBDACTION.SAVE) {
			return;
		}
		if (vo == null || vo.getParentVO() == null) {
			return;
		}

		validateTeamCode(vo);

	}
	//对主表某字段的唯一性的校验
	private void validateTeamCode(AggregatedValueObject vo) throws Exception{
		// 判断是修改后的保存还是新增后的保存
		TeamdocHVO ivo = (TeamdocHVO) vo.getParentVO();
		// 如果是新增后的保存执行下面的代码
		if (ivo.getPrimaryKey() == null	|| ivo.getPrimaryKey().trim().equals("")) {

			String condition = " teamcode='" + ivo.getTeamcode()
					+ "' and  isnull(dr,0)=0";
			List list = (List) getDao().retrieveByClause(TeamdocHVO.class,
					condition);
			if (list == null || list.size() == 0) {
				//对表体人员编码唯一性的校验
				validatePsn(vo);
				
			}else{
				throw new BusinessException(" 该班组编码在数据库中已经存在 ");
			}
		}else{
			// 否则执行下面的代码
			String condition = " pk_wds_teamdoc_h='" + ivo.getPrimaryKey()
					+ "' and  isnull(dr,0)=0";
			List list = (List) getDao().retrieveByClause(TeamdocHVO.class,
					condition);

			if (list == null) {
				return;
			}
			// 判断修改后的记录，是否改变了存货（即拿数据库中的记录和ui中的当前记录进行比较）
			TeamdocHVO ivo2 = (TeamdocHVO) list.get(0);
			if (ivo.getTeamcode().equalsIgnoreCase(ivo2.getTeamcode())) {
				
				// 判断人员编码是否在数据库中存在
				validatePsn(vo);

			}else{

				String condition1 = " teamcode='" + ivo.getTeamcode()
						+ "' and  isnull(dr,0)=0";
				List list1 = (List) getDao().retrieveByClause(TeamdocHVO.class,
						condition1);
				if (list1 == null || list1.size() == 0) {
					//判断人员编码是否在数据库中存在
					validatePsn(vo);

				} else {
					throw new BusinessException(" 该班组编码在数据库中已经存在 ");
				}
			}
		}
		
		
		
	}
	

   //对子表某字段的唯一性的校验，嵌套在主表里面
	private void validatePsn(AggregatedValueObject vo) throws Exception{	
		TeamdocHVO h = (TeamdocHVO) vo.getParentVO();
		// 判断人员编码是否在数据库中存在
		TeamdocBVO[] bvos = (TeamdocBVO[]) vo.getChildrenVO();
		for (int i = 0; i < bvos.length; i++) {
			
			//判断表体数据时新增后的保存还是编辑后保存					
			if (bvos[i].getPrimaryKey() == null
					|| bvos[i].getPrimaryKey().trim().equals("")) {

				String cond = " psncode='" + bvos[i].getPsncode()
						+ "' and  isnull(dr,0)=0 and pk_wds_teamdoc_h='"+h.getPrimaryKey()+"'";
				List lis = (List) getDao().retrieveByClause(
						TeamdocBVO.class, cond);
				if (lis == null || lis.size() == 0) {
					continue;
				} else {
					throw new BusinessException(" 同一班组,人员编码不能重复 ");
				}
			}else{
				String cond1= " pk_wds_teamdoc_b='" + bvos[i].getPrimaryKey()
				+ "' and  isnull(dr,0)=0 and pk_wds_teamdoc_h='"+h.getPrimaryKey()+"'";
				List lis1 = (List) getDao().retrieveByClause(TeamdocBVO.class, cond1);
				if(lis1==null ||lis1.size()==0){
					return;
				}
				TeamdocBVO tbo=(TeamdocBVO) lis1.get(0);
				if(tbo.getPsncode().equalsIgnoreCase(bvos[i].getPsncode())){
					return;
				} else {
					String cond = " psncode='" + bvos[i].getPsncode()
							+ "' and  isnull(dr,0)=0 and pk_wds_teamdoc_h='"+h.getPrimaryKey()+"'";
					List lis = (List) getDao().retrieveByClause(
							TeamdocBVO.class, cond);
					if (lis == null || lis.size() == 0) {
						continue;
					} else {
						throw new BusinessException(" 同一班组,人员编码不能重复 ");
					}							
				}						
			}
		}		
	}

	public void dealAfter(int intBdAction, AggregatedValueObject billVo,
			Object userObj) throws Exception {
		// TODO Auto-generated method stub

	}

}
