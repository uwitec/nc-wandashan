package nc.ui.wds.invcl;

import nc.ui.pub.ClientEnvironment;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.pub.IVOTreeDataByID;
import nc.uif.pub.exception.UifException;
import nc.vo.pub.SuperVO;
import nc.vo.wds.invcl.WdsInvClVO;

public class WdsInvClTreeData implements IVOTreeDataByID {

	public String getShowFieldName() {
		// TODO Auto-generated method stub
		return "vinvclcode,vinvclname";
	}

	public SuperVO[] getTreeVO() {
		// TODO Auto-generated method stub
		String whereSql = "pk_corp = '"+ClientEnvironment.getInstance().getCorporation().getPrimaryKey()+"'";
		SuperVO[] vos =  null;
		try {
			vos = HYPubBO_Client.queryByCondition(WdsInvClVO.class, whereSql);
		} catch (UifException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			vos = null;
		}
		if(vos == null || vos.length ==0)
			return null;
		return vos;
	}

	public String getIDFieldName() {
		// TODO Auto-generated method stub
		return "pk_invcl";
	}

	public String getParentIDFieldName() {
		// TODO Auto-generated method stub
		return "pk_father";
	}

}
