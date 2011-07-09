package nc.bs.wds.dm.storetranscorp;
import nc.bs.dao.BaseDAO;
import nc.bs.trade.business.IBDBusiCheck;
import nc.bs.wl.pub.BsUniqueCheck;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.trade.pub.IBDACTION;
/**
 * 
 * 基本档案 后台校验类 
 * author:mlr
 * */
public class BSCheck implements IBDBusiCheck {
	private BaseDAO dao;
	private BaseDAO getDao() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}
	/**
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 *        基本档案后台校验方法
	 * @时间：2011-4-10下午06:59:34
	 */
	public void check(int intBdAction, AggregatedValueObject vo, Object userObj)
			throws Exception {
		if (intBdAction != IBDACTION.SAVE) {
			return;
		}
		if (vo == null || vo.getParentVO() == null) {
			return;
		}
		if(vo.getChildrenVO()==null || vo.getChildrenVO().length==0){
			return;
		}
		SuperVO[] vos=(SuperVO[]) vo.getChildrenVO();		
		BsUniqueCheck.FieldUniqueChecks(vos,new String[]{"pk_wds_tanscorp_h"}," and pk_stordoc <> '"+vo.getParentVO().getAttributeValue("pk_stordoc")+"'", "该[承运商]在其他仓库已经存在");	
	}
	/**
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 *        基本档案后台校验后续处理类
	 * @时间：2011-4-10下午06:59:34
	 */
	public void dealAfter(int intBdAction, AggregatedValueObject billVo,
			Object userObj) throws Exception {
		
	}

}
