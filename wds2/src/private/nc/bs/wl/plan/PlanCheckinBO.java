package nc.bs.wl.plan;

import nc.bd.accperiod.AccountCalendar;
import nc.bs.dao.BaseDAO;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.vo.dm.SendplaninVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.scm.pu.PuPubVO;

/**
 * @作者：lyf
 * @说明：完达山物流项目 
 * 发运计划录入（WDS1）后台类
 */
public class PlanCheckinBO {
	
	BaseDAO dao = null;
	
	private BaseDAO getBaseDAO(){
		if(dao==null){
			dao = new BaseDAO();
		}
		return dao;
	}
	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目 
	 * 	保存前校验
	 * 当前登录人的管理的仓库在当前会计月是否已经有月计划
	 * @时间：2011-3-23下午09:14:56
	 * @param o
	 */
	public void beforeCheck(String  pk_inwhouse) throws BusinessException{
		AccountCalendar calendar = AccountCalendar.getInstance();
		UFDate beginDate = calendar.getMonthVO().getBegindate();
		UFDate endDate = calendar.getMonthVO().getEnddate();
		StringBuffer sql = new StringBuffer();
		sql.append(" select count(*) from wds_sendplanin ");
		sql.append(" where dmakedate between '");
		sql.append(beginDate+"' and '" + endDate);
		sql.append("' and pk_inwhouse ='"+pk_inwhouse+"' ");
		sql.append(" and isnull(dr,0)=0");
		int i = PuPubVO.getInteger_NullAs(getBaseDAO().executeQuery(sql.toString(), WdsPubResulSetProcesser.COLUMNPROCESSOR), 0);
		if( i>0){
			throw new BusinessException("该调入仓库，当前会计月已经有月计划,只可以做追加计划");
		}
	}
	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目 
	 *   弃审前校验 是否已经安排生产下游发运订单
	 * @时间：2011-3-27上午09:44:46
	 * @param 要弃审的 发运计划单据
	 * @throws BusinessException 
	 */
	public void beforeUnApprove(AggregatedValueObject obj) throws BusinessException{
		if(obj ==null){
			return;
		}
		SendplaninVO parent =(SendplaninVO) obj.getParentVO();
		String pk_sendplanin = parent.getPk_sendplanin();
		StringBuffer sql = new StringBuffer();	
		sql.append(" select count(*) ");
		sql.append(" from wds_sendorder ");
		sql.append(" join wds_sendorder_b ");
		sql.append(" on wds_sendorder.pk_sendorder= wds_sendorder_b.pk_sendorder");
		sql.append(" where isnull(wds_sendorder.dr,0)=0 and isnull(wds_sendorder_b.dr,0)=0 ");
		sql.append(" and wds_sendorder_b.csourcebillhid ='"+pk_sendplanin+"'");
		int i = PuPubVO.getInteger_NullAs(getBaseDAO().executeQuery(sql.toString(), WdsPubResulSetProcesser.COLUMNPROCESSOR), 0);
		if( i>0){
			throw new BusinessException("已有下游发运订单，请先删除发运订单再做此操作");
		}
		
	}
}
