package nc.bs.wds.tranprice.box;

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
 * @author Administrator
 *  箱数运价表后台校验类
 */
public class BoxBs  implements Serializable{

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
	 * @说明：完达山物流项目
	 * 审批前校验
	 *  已经审批过的单据是否存在 日期交叉
	 * @时间：2011-5-24下午01:05:16
	 */
	public void beforApprove(AggregatedValueObject vo) throws BusinessException{
		if(vo == null )
			return ;
		TranspriceHVO hvo = (TranspriceHVO)vo.getParentVO();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from wds_transprice_h where isnull(dr,0)=0  ");
		sql.append(" and vbillstatus=1");//审批通过的
		sql.append(" and reserve1='"+hvo.getReserve1()+"'");//仓库
		sql.append(" and carriersid='"+hvo.getCarriersid()+"'");//承运商 	
		sql.append(" and ipriceunit='"+hvo.getIpriceunit()+"'");
		sql.append(" and pk_billtype='"+WdsWlPubConst.WDSJ+"'");
		sql.append(" and ((nmincase<='"+hvo.getNmincase()+"' and nmaxcase>='"+hvo.getNmincase()+"')");		
		sql.append(" or (nmincase<='"+hvo.getNmaxcase()+"' and nmaxcase>='"+hvo.getNmaxcase()+"')");
		sql.append(" or (nmincase>='"+hvo.getNmincase()+"' and nmaxcase<='"+hvo.getNmaxcase()+"'))");
		List<TranspriceHVO> list = (ArrayList<TranspriceHVO>) getBaseDAO().executeQuery(sql.toString(),
				new BeanListProcessor(TranspriceHVO.class));
		if (list.size() > 0) {
			TranspriceHVO oldHvo = list.get(0);
			throw new BusinessException("和已经审批过的相同仓库相同承运商 运价表存在交叉:\n单据编号="
					+ oldHvo.getVbillno() +"\n运价编码="+oldHvo.getVpricecode()+"\n运价名称="+oldHvo.getVpricename()+"\n最小箱数=" + oldHvo.getNmincase()
					+ "\n最大箱数=" + oldHvo.getNmaxcase());
		}
	}

}
