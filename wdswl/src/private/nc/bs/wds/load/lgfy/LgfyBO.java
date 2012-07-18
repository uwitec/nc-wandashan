package nc.bs.wds.load.lgfy;

import java.util.ArrayList;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.wds.load.account.LoadpriceB2VO;
import nc.vo.wl.pub.WdsWlPubTool;

public class LgfyBO {
	private BaseDAO dao = null;

	private BaseDAO getDao() {
		if (dao == null)
			dao = new BaseDAO();
		return dao;
	}

	public void beforeSave(AggregatedValueObject vo) throws BusinessException {
		if (vo == null) {
			return;
		}
		LoadpriceB2VO[] bvos = (LoadpriceB2VO[]) vo.getChildrenVO();
		List<String> delete = new ArrayList<String>();
		for (LoadpriceB2VO bvo : bvos) {
			if (VOStatus.DELETED == bvo.getStatus()) {
				delete.add(bvo.getPrimaryKey());
			}
		}
		if (delete != null && delete.size() > 0) {
			deleteTVOs(delete);
		}
	}

	public void afterDelete(AggregatedValueObject vo) throws BusinessException {
		if (vo == null) {
			return;
		}
		LoadpriceB2VO[] bvos = (LoadpriceB2VO[]) vo.getChildrenVO();
		List<String> delete = new ArrayList<String>();
		for (LoadpriceB2VO bvo : bvos) {
			delete.add(bvo.getPrimaryKey());
		}
		if (delete != null && delete.size() > 0) {
			deleteTVOs(delete);
		}
	}

	/**
	 * 
	 * @作者：yf
	 * @说明：完达山物流项目 删除相关用工信息
	 * @时间：2012-7-18下午02:18:36
	 * @param delete
	 * @throws DAOException
	 */
	private void deleteTVOs(List<String> delete) throws DAOException {
		if (delete == null || delete.size() <= 0) {
			return;
		}
		StringBuffer sql = new StringBuffer();
		sql
				.append(" update wds_loadprice_t set dr = 1 where pk_loadprice_b2 in "
						+ WdsWlPubTool.getSubSql(delete.toArray(new String[0])));
		getDao().executeUpdate(sql.toString());
	}

}
