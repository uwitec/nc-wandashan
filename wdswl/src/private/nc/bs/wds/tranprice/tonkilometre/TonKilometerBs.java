package nc.bs.wds.tranprice.tonkilometre;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.wds.tranprice.tonkilometre.TranspriceHVO;
import nc.vo.wl.pub.WdsWlPubConst;

/**
 * 
 * @author Administrator 吨公里运价表后台校验类
 */
public class TonKilometerBs implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BaseDAO dao = null;

	BaseDAO getBaseDAO() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}

	/**
	 * 
	 * @throws DAOException
	 * @作者：lyf
	 * @说明：完达山物流项目 审批前校验 已经审批过的单据是否存在 日期范围交叉
	 * @时间：2011-5-24下午01:05:16
	 */
	public void beforApprove(AggregatedValueObject vo) throws BusinessException {
		if (vo == null)
			return;
		TranspriceHVO hvo = (TranspriceHVO) vo.getParentVO();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from wds_transprice_h where isnull(dr,0)=0  ");
		sql.append(" and vbillstatus='1'");// 审批通过的
		sql.append(" and reserve1='"+hvo.getReserve1()+"'");//仓库
		sql.append(" and carriersid='"+hvo.getCarriersid()+"'");//承运商 		
		sql.append(" and pk_billtype='" + WdsWlPubConst.WDSI + "'");
		sql.append(" and( (dstartdate<='" + hvo.getDstartdate()
				+ "' and denddate>='" + hvo.getDstartdate() + "')");
		sql.append(" or (dstartdate<='" + hvo.getDenddate()
				+ "' and denddate>='" + hvo.getDenddate() + "')");
		sql.append(" or (dstartdate>='" + hvo.getDstartdate()
				+ "' and denddate<='" + hvo.getDenddate() + "'))");
		List<TranspriceHVO> list = (ArrayList<TranspriceHVO>) getBaseDAO()
				.executeQuery(sql.toString(),
						new BeanListProcessor(TranspriceHVO.class));
		if (list.size() > 0) {
			TranspriceHVO oldHvo = list.get(0);
			throw new BusinessException("和已经审批过相同仓库的相同承运商 吨公里运价表存在日期交叉:\n单据编号="
					+ oldHvo.getVbillno() +"\n运价编码="+oldHvo.getVpricecode()+"\n运价名称="+oldHvo.getVpricename()+"\n开始日期=" + oldHvo.getDstartdate()
					+ "\n截止日期=" + oldHvo.getDenddate());
		}

	}

}
