package nc.ui.wds.dm.storetranscorp;

import nc.ui.trade.bsdelegate.BDBusinessDelegator;
import nc.ui.trade.business.HYPubBO_Client;
import nc.vo.pub.SuperVO;
/**
 * xjx   add
 * @author Administrator
 *
 */

public class MyDelegator extends BDBusinessDelegator {

//	public String getBodyCondition(Class bodyClass, String key) {
//		if (bodyClass == nc.vo.wds.ic.cargtray.BdCargdocTrayVO.class)
//			return "pk_cargdoc = '" + key + "' and isnull(dr,0)=0 ";
//		return null;
//	}
	@Override
	public SuperVO[] queryHeadAllData(Class headClass, String strBillType,
			String strWhere) throws Exception {	
		String where = "";
		if(strWhere!=null && strWhere.length()>0){
			where =  " pk_stordoc in (SELECT bd_stordoc.pk_stordoc FROM bd_stordoc  "+                //  bd_stordoc          wds_stortranscorp_b
       " left join wds_stortranscorp_b  on bd_stordoc.pk_stordoc=wds_stortranscorp_b.pk_stordoc "
       +" and   (isnull(wds_stortranscorp_b.dr, 0) = 0) where (isnull(bd_stordoc.dr, 0) = 0) and bd_stordoc.def1='1'  and "+strWhere+") ";
		}
		SuperVO[] vo = HYPubBO_Client.queryByCondition(headClass, where);
		return vo;
	}
}