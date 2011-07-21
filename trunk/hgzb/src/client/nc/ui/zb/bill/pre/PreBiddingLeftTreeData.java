package nc.ui.zb.bill.pre;

import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.pub.IVOTreeData;
import nc.uif.pub.exception.UifException;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.trade.voutils.VOUtil;
import nc.vo.zb.bidding.SmallBiddingHeaderVO;
import nc.vo.zb.pub.ZbPubConst;
import nc.vo.zb.pub.ZbPubTool;

public class PreBiddingLeftTreeData implements IVOTreeData {

	public String getShowFieldName() {
		// TODO Auto-generated method stub
		return "cnameno";
	}

	public SuperVO[] getTreeVO() {
		// TODO Auto-generated method stub
		String strWhere = "isnull(dr,0)=0 and vbillstatus = "+IBillStatus.CHECKPASS+" and ibusstatus = "+ZbPubConst.BIDDING_BUSINESS_STATUE_SUBMIT;
		SuperVO[] vos = null;
		try {
			String s = ZbPubTool.getParam();
		    if(s!=null &&!"".equals(s))
		    	strWhere = strWhere+" and reserve1 = '"+s+"'";
			vos = HYPubBO_Client.queryByCondition(SmallBiddingHeaderVO.class, strWhere);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			vos = null;
		}
		VOUtil.ascSort(vos,new String[]{"vbillno"});
		for(SuperVO vo:vos){
			vo.setAttributeValue("cnameno",PuPubVO.getString_TrimZeroLenAsNull(vo.getAttributeValue("vbillno"))+PuPubVO.getString_TrimZeroLenAsNull(vo.getAttributeValue("cname")));
		}
		return vos;
	}

}
