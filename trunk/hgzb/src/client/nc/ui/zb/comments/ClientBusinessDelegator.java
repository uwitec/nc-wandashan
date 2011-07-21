package nc.ui.zb.comments;

import nc.ui.pub.ToftPanel;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.zb.pub.LongTimeTask;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.zb.comments.BiEvaluationBodyVO;
import nc.vo.zb.comments.BidSlvendorVO;
import nc.vo.zb.pub.ZbPubTool;

public class ClientBusinessDelegator extends BusinessDelegator {

	public ClientBusinessDelegator() {
		super();
	}

	@Override
	public SuperVO[] queryHeadAllData(Class headClass, String strBillType,
			String strWhere) throws Exception {
		String where = "";
		if (strWhere != null && strWhere.length() > 0) {
			where = " cevaluationid in (select distinct zb_bidevaluation_h.cevaluationid from  zb_bidevaluation_h  ,zb_evaluation_b "
					+ " where zb_bidevaluation_h.cevaluationid =  zb_evaluation_b.cevaluationid and  isnull( zb_bidevaluation_h.dr,0)=0 and isnull(zb_evaluation_b.dr,0) = 0 "
					+ " and  " + strWhere + ") ";
			String s = ZbPubTool.getParam();
			 if(s!=null &&!"".equals(s))
				 where = where+ " and zb_bidevaluation_h.cbiddingid in(select h.cbiddingid from zb_bidding_h h where  nvl(h.dr, 0) = 0 and h.reserve1 = '"+s+"')";
		}
		SuperVO[] vo = HYPubBO_Client.queryByCondition(headClass, where);
		return vo;
	}

	public nc.vo.pub.CircularlyAccessibleValueObject[] queryBodyAllData(
			Class voClass, String billType, String key, String strWhere)
			throws Exception {
		CircularlyAccessibleValueObject[] vos = null;
		if (billType == null || billType.trim().length() == 0)
			return null;
		else {
			vos = (CircularlyAccessibleValueObject[]) HYPubBO_Client.queryAllBodyData(
					billType, voClass, key, strWhere);
			for(CircularlyAccessibleValueObject vo : vos) {
				String pk = vo.getPrimaryKey();
				BidSlvendorVO[] bvos =(BidSlvendorVO[])loadBodyData(pk);
				((BiEvaluationBodyVO)vo).setBidSlvendorVOs(bvos);
			}
		}
		return vos;

	}

	public SuperVO[] loadBodyData(String key) throws Exception {
		SuperVO[] supervos =null;
		if (key != null && !"".equals(key)) {
			supervos = HYPubBO_Client.queryByCondition(Class.forName(BidSlvendorVO.class.getName()), "cevaluationbid='"
					+ key + "' and isnull(dr,0)=0");

		}
		return supervos;
	}
	
	public BiEvaluationBodyVO[] doSaveSplitNumInfor(String cbiddingid,BiEvaluationBodyVO[] bodys,ToftPanel ui) throws Exception{
		Class[] ParameterTypes = new Class[]{String.class,BiEvaluationBodyVO[].class};
		Object[] ParameterValues = new Object[]{cbiddingid,bodys};
		Object o = LongTimeTask.
		calllongTimeService("pu", ui, "正在处理...", 1, "nc.bs.zb.comments.CommentsBO", null, "doSaveSplitNumInfor", ParameterTypes, ParameterValues);
		return (BiEvaluationBodyVO[])o;
	}
	
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）自动分量计算
	 * 2011-5-24下午08:27:44
	 * @param billvo
	 * @return
	 * @throws Exception
	 */
	public BiEvaluationBodyVO[] autoSplitNum(HYBillVO billvo,ToftPanel ui) throws Exception{
		Class[] ParameterTypes = new Class[]{HYBillVO.class};
		Object[] ParameterValues = new Object[]{billvo};
		Object o = LongTimeTask.
		calllongTimeService("pu", ui, "正在处理...", 1, "nc.bs.zb.comments.CommentsBO", null, "autoSplitNum", ParameterTypes, ParameterValues);
		return (BiEvaluationBodyVO[])o;
	}
}
