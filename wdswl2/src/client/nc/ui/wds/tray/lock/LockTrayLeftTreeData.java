package nc.ui.wds.tray.lock;

import nc.ui.trade.business.HYPubBO_Client;
import nc.uif.pub.exception.UifException;
import nc.vo.ic.pub.StockInvOnHandVO;
import nc.vo.pub.SuperVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wds.ic.cargtray.SmallTrayVO;
import nc.vo.wl.pub.IVOTreeData2;
import nc.vo.wl.pub.WdsWlPubConst;

public class LockTrayLeftTreeData implements IVOTreeData2 {

	public String getShowFieldName() {
		// TODO Auto-generated method stub
		return "cdt_traycode";
	}
	
	private String wherePart = "";

	public SuperVO[] getTreeVO() {
//		// TODO Auto-generated method stub
		if(PuPubVO.getString_TrimZeroLenAsNull(wherePart)==null)
			return null;
		StringBuffer strWhere = new StringBuffer("isnull(dr,0)=0");
		if(PuPubVO.getString_TrimZeroLenAsNull(wherePart)!=null){
			strWhere.append(" and "+ wherePart);
		}
		strWhere.append(" and cdt_traycode not like '"+WdsWlPubConst.XN_CARGDOC_TRAY_NAME+"%'");
		strWhere.append(" and cdt_traystatus = "+StockInvOnHandVO.stock_state_null);
		SuperVO[] vos = null;
		try {
			vos = HYPubBO_Client.queryByCondition(SmallTrayVO.class, strWhere.toString());
		} catch (UifException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			vos = null;
		}
		if(vos == null || vos.length ==0)
			return null;
//		SmallTrayVO[] bodys = (SmallTrayVO[])vos;
//		for(SmallTrayVO body:bodys){
//			try {
//				body.setInvCodeAndName();
//			} catch (BusinessException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		return vos;
	}

	public void setWherePart(String whereSql) {
		// TODO Auto-generated method stub
		wherePart = whereSql;
	}

}
