package nc.bs.wds.dm.sendinvdoc;

import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.trade.business.IBDBusiCheck;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.trade.pub.IBDACTION;
import nc.vo.wds.dm.sendinvdoc.SendinvdocVO;

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

		// 判断是修改后的保存还是新增后的保存
		SendinvdocVO ivo = (SendinvdocVO) vo.getParentVO();
		// 如果是新增后的保存执行下面的代码
		if (ivo.getPrimaryKey() == null
				|| ivo.getPrimaryKey().trim().equals("")) {

			String condition = " pk_invbasdoc='" + ivo.getPk_invbasdoc()
					+ "' and  isnull(dr,0)=0";
			List list = (List) getDao().retrieveByClause(SendinvdocVO.class,
					condition);
			if (list == null || list.size() == 0) {
				// 判断该行号是否在数据库中存在
				String cond = " crow='" + ivo.getCrow()
						+ "' and  isnull(dr,0)=0";
				List lis = (List) getDao().retrieveByClause(SendinvdocVO.class,
						cond);
				if (lis == null || lis.size() == 0) {
					return;
				} else {
					throw new BusinessException(" 该行号在数据库中已经存在 ");
				}

			} else {
				throw new BusinessException(" 该存货在数据库中已经存在 ");
			}
		} else {
			// 否则执行下面的代码
			String condition = " pk_wds_sendinvdoc='" + ivo.getPrimaryKey()
					+ "' and  isnull(dr,0)=0";
			List list = (List) getDao().retrieveByClause(SendinvdocVO.class,
					condition);

			if (list == null) {
				return;
			}
			// 判断修改后的记录，是否改变了存货（即拿数据库中的记录和ui中的当前记录进行比较）
			SendinvdocVO ivo2 = (SendinvdocVO) list.get(0);
			if (ivo.getPk_invbasdoc().equalsIgnoreCase(ivo2.getPk_invbasdoc())) {
				// 判断修改后的记录，是否改变了行号
				if (ivo.getCrow().equals(ivo2.getCrow())) {
					return;
				} else {
					
					// 判断该行号是否在数据库中存在
					String cond = " crow='" + ivo.getCrow()
							+ "' and  isnull(dr,0)=0";
					List lis = (List) getDao().retrieveByClause(
							SendinvdocVO.class, cond);
					if (lis == null || lis.size() == 0) {
						return;
					} else {
						throw new BusinessException(" 该行号在数据库中已经存在 ");
					}
				}
			}

			String condition1 = " pk_invbasdoc='" + ivo.getPk_invbasdoc()
					+ "' and  isnull(dr,0)=0";
			List list1 = (List) getDao().retrieveByClause(SendinvdocVO.class,
					condition1);
			if (list1 == null || list1.size() == 0) {
				// 判断修改后的记录，是否改变了行号
				if (ivo.getCrow().equals(ivo2.getCrow())) {
					return;
				} else {
					// 判断该行号是否在数据库中存在
					String cond = " crow='" + ivo.getCrow()
							+ "' and  isnull(dr,0)=0";
					List lis = (List) getDao().retrieveByClause(
							SendinvdocVO.class, cond);
					if (lis == null || lis.size() == 0) {
						return;
					} else {
						throw new BusinessException(" 该行号在数据库中已经存在 ");
					}
				}
			} else {
				throw new BusinessException(" 该存货在数据库中已经存在 ");
			}
		}

	}

	public void dealAfter(int intBdAction, AggregatedValueObject billVo,
			Object userObj) throws Exception {
		// TODO Auto-generated method stub

	}

}
