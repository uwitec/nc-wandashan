package nc.ui.wds.tray.lock;

import nc.ui.trade.pub.IVOTreeData;
import nc.vo.pub.SuperVO;

public class LockTrayRightTreeData implements IVOTreeData {

	public String getShowFieldName() {
		// TODO Auto-generated method stub
		return "cdt_traycode";
	}
	
//	private String wherePart = "";

	public SuperVO[] getTreeVO() {
		// TODO Auto-generated method stub
//		if(PuPubVO.getString_TrimZeroLenAsNull(wherePart)==null)
//			return null;
//		String strWhere = "isnull(dr,0)=0";
//		if(PuPubVO.getString_TrimZeroLenAsNull(wherePart)!=null){
//			strWhere = strWhere + " and "+ wherePart;
//		}
//		SuperVO[] vos = null;
//		try {
//			vos = HYPubBO_Client.queryByCondition(SmallTrayVO.class, strWhere);
//		} catch (UifException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			vos = null;
//		}
//		if(vos == null || vos.length ==0)
//			return null;
//		SmallTrayVO[] bodys = (SmallTrayVO[])vos;
//		for(SmallTrayVO body:bodys){
//			try {
//				body.setInvCodeAndName();
//			} catch (BusinessException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		return null;
	}

//	public void setWherePart(String whereSql) {
//		// TODO Auto-generated method stub
//		wherePart = whereSql;
//	}

}
