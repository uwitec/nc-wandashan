package nc.ui.wds.rdcl;

import nc.ui.pub.ClientEnvironment;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.pub.IVOTreeDataByID;
import nc.uif.pub.exception.UifException;
import nc.vo.pub.SuperVO;
import nc.vo.wds.rdcl.RdclVO;

public class ClientTreeCardData implements IVOTreeDataByID {

	public String getShowFieldName() {
		// TODO Auto-generated method stub
		return "rdcode,rdname";
	}

	public SuperVO[] getTreeVO() {
		// TODO Auto-generated method stub
		String whereSql = "pk_corp = '"+ClientEnvironment.getInstance().getCorporation().getPrimaryKey()+"'";
		SuperVO[] vos =  null;
		try {
			vos = HYPubBO_Client.queryByCondition(RdclVO.class, whereSql);
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
		return "pk_rdcl";
	}

	public String getParentIDFieldName() {
		return "pk_frdcl";
	}

}
