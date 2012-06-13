package nc.ui.wds.dm.storebing;

import nc.ui.trade.bsdelegate.BDBusinessDelegator;
import nc.ui.trade.business.HYPubBO_Client;
import nc.vo.pub.SuperVO;

/**
  *
  *抽象业务代理类的缺省实现
  *@author author
  *@version tempProject version
  */
public class MyDelegator extends BDBusinessDelegator{


 @Override
	public SuperVO[] queryHeadAllData(Class headClass, String strBillType,
			String strWhere) throws Exception {	
		String where = "";
		if(strWhere!=null && strWhere.length()>0){//wds_storecust_h     tb_storcubasdoc
			where =  " 	pk_wds_storecust_h in (SELECT tb_storcubasdoc.pk_wds_storecust_h FROM wds_storecust_h  "+                //  bd_stordoc          wds_stortranscorp_b
    " join tb_storcubasdoc  on wds_storecust_h.pk_wds_storecust_h=tb_storcubasdoc.pk_wds_storecust_h "
    +" WHERE (isnull(wds_storecust_h.dr, 0) = 0)  and  (isnull(tb_storcubasdoc.dr, 0) = 0) and "+strWhere+") ";
		}
		SuperVO[] vo = HYPubBO_Client.queryByCondition(headClass, where);
		return vo;
	}
}