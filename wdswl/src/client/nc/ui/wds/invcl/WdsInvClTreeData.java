package nc.ui.wds.invcl;

import nc.ui.pub.ClientEnvironment;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.pub.IVOTreeData;
import nc.uif.pub.exception.UifException;
import nc.vo.pub.SuperVO;
import nc.vo.wds.invcl.WdsInvClVO;

public class WdsInvClTreeData implements IVOTreeData {

	public String getShowFieldName() {
		// TODO Auto-generated method stub
		return "vinvclcode";
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
		return vos;
	}

}
