package nc.bs.wds.ic.other.in;
import nc.bs.dao.BaseDAO;
import nc.bs.trade.business.IBDBusiCheck;
import nc.bs.wl.pub.BsUniqueCheck;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.trade.pub.IBDACTION;
import nc.vo.wds.ie.storepersons.TbStockstaffVO;
/**
 * @author mlr
 * 后台校验类
 *  动作执行前 执行check方法
 *  传来的 AggregatedValueObject 对象是通过
 *  getBillUI().getChangedVOFromUI()方法得到的
 *  聚合vo
 */
public class BSCheck implements IBDBusiCheck{
	private BaseDAO dao=null;

	private BaseDAO getDao(){
		if(dao==null){
			dao=new BaseDAO();
		}
		return dao;	   
	}
	public void check(int intBdAction, AggregatedValueObject vo, Object userObj)
	throws Exception {
		if(intBdAction==IBDACTION.SAVE){
			if(vo==null){
				return;
			}
			vo.getParentVO();
		}
	}

	public void dealAfter(int intBdAction, AggregatedValueObject billVo,
			Object userObj) throws Exception {


	}

}
