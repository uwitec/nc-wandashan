package nc.ui.zb.bidding.adjust;

import nc.itf.zb.pub.IVOTreeData2;
import nc.ui.trade.business.HYPubBO_Client;
import nc.uif.pub.exception.UifException;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.zb.bidding.SmallBiddingBodyVO;
import nc.vo.zb.pub.ZbPubTool;

public class AjustBiddingLeftTreeData implements IVOTreeData2 {

	public String getShowFieldName() {
		// TODO Auto-generated method stub
		return "invcode/invname";
	}
	
	private String wherePart = "";

	public SuperVO[] getTreeVO() {
		// TODO Auto-generated method stub
		if(PuPubVO.getString_TrimZeroLenAsNull(wherePart)==null)
			return null;
		String strWhere = "isnull(dr,0)=0";
		if(PuPubVO.getString_TrimZeroLenAsNull(wherePart)!=null){
			strWhere = strWhere + " and "+ wherePart;
		}
		SuperVO[] vos = null;
		try {
			vos = HYPubBO_Client.queryByCondition(SmallBiddingBodyVO.class, strWhere);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			vos = null;
		}
		if(vos == null || vos.length ==0)
			return null;
		SmallBiddingBodyVO[] bodys = (SmallBiddingBodyVO[])vos;
		for(SmallBiddingBodyVO body:bodys){
			try {
				body.setInvCodeAndName();
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return vos;
	}

	public void setWherePart(String whereSql) {
		// TODO Auto-generated method stub
		wherePart = whereSql;
	}

}
