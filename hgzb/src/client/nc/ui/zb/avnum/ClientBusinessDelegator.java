package nc.ui.zb.avnum;

import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.business.HYPubBO_Client;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.zb.avnum.AvNumBodyVO;
import nc.vo.zb.avnum.AvVendorVO;
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
			where = " cavnumid in (select distinct zb_avnum_h.cavnumid from  zb_avnum_h  ,zb_avnum_b "
					+ " where zb_avnum_h.cavnumid =  zb_avnum_b.cavnumid and  isnull( zb_avnum_h.dr,0)=0 and isnull(zb_avnum_b.dr,0) = 0 "
					+ " and  " + strWhere + ") ";
			String s = ZbPubTool.getParam();
			 if(s!=null &&!"".equals(s))
				 where = where+ " and zb_avnum_h.cbiddingid in(select h.cbiddingid from zb_bidding_h h where  nvl(h.dr, 0) = 0 and h.reserve1 = '"+s+"')";
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
				AvVendorVO[] bvos =(AvVendorVO[])loadBodyData(pk);
				((AvNumBodyVO)vo).setAvVendorVO(bvos);
			}
		}
		return vos;

	}

	public SuperVO[] loadBodyData(String key) throws Exception {
		SuperVO[] supervos =null;
		if (key != null && !"".equals(key)) {
			supervos = HYPubBO_Client.queryByCondition(Class.forName(AvVendorVO.class.getName()), "cavnumbid='"
					+ key + "' and isnull(dr,0)=0");

		}
		return supervos;
	}

}
