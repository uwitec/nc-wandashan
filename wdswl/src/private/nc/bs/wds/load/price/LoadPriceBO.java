package nc.bs.wds.load.price;

import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.trade.business.IBDBusiCheck;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.trade.pub.IBDACTION;
import nc.vo.wds.load.LoadpriceVO;

public class LoadPriceBO implements IBDBusiCheck {

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
		LoadpriceVO ivo = (LoadpriceVO) vo.getParentVO();
		// 如果是新增后的保存执行下面的代码
		if (ivo.getPk_invmandoc() == null
				|| "".equalsIgnoreCase(ivo.getPk_invmandoc())) {
			throw new BusinessException("请输入存货");
		}
		String condition = " pk_invmandoc='" + ivo.getPk_invmandoc()
				+ "' and  isnull(dr,0)=0";
		if (ivo.getPrimaryKey() == null
				|| ivo.getPrimaryKey().trim().equals("")) {

			List list = (List) getDao().retrieveByClause(LoadpriceVO.class,
					condition);
			if (list == null || list.size() == 0) {
				return;
			} else {
				throw new BusinessException(" 该存货已经设置过装卸费价格 ");
			}
		} else {
			// 否则执行下面的代码
			String pkSql = " pk_loadprice='" + ivo.getPrimaryKey()
					+ "' and  isnull(dr,0)=0";
			List list = (List) getDao().retrieveByClause(LoadpriceVO.class,
					pkSql);

			if (list == null) {
				return;
			}
			// 判断修改后的记录，是否改变了存货（即拿数据库中的记录和ui中的当前记录进行比较）
			LoadpriceVO ivo2 = (LoadpriceVO) list.get(0);
			if (ivo.getPk_invmandoc().equalsIgnoreCase(ivo2.getPk_invmandoc())) {
				return;
			}
			List list1 = (List) getDao().retrieveByClause(LoadpriceVO.class,
					condition);
			if (list1 == null || list1.size() == 0) {
				return;
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
