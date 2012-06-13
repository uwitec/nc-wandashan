package nc.ui.wds.tray.relock;

import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.pub.IVOTreeData;
import nc.uif.pub.exception.UifException;
import nc.vo.ic.pub.StockInvOnHandVO;
import nc.vo.pub.SuperVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wds.ic.cargtray.SmallTrayVO;

public class ReLockTrayLeftTreeData implements IVOTreeData {

	public String getShowFieldName() {
		// TODO Auto-generated method stub
		return "cdt_traycode";
	}
	
	/**
	 * zhf  此处可能存在漏洞  如果出现 一批次货品  多次录入到同一虚拟托盘上时   部分绑定的情况下  
	 * 考虑这种情况
	 * 比如 该批次存货 出货：100   库存量100 满足  60个绑定了实际托盘  40个没有绑定实际托盘
	 * 
	 * 如何出库？  如果出现这种情况 如果用户指定了实际托盘  系统将实际托盘的锁定状态清除
	 * 实际托盘未用完的继续锁定  等待下次的出库 
	 * 
	 * 在解锁过程中 不进行  存量不足的校验
	 * 
	 */

	public SuperVO[] getTreeVO() {
		StringBuffer strWhere = new StringBuffer("isnull(dr,0)=0");
		if(PuPubVO.getString_TrimZeroLenAsNull(ReLockTrayDialog.xntrayid)!=null
				&&PuPubVO.getString_TrimZeroLenAsNull(ReLockTrayDialog.invmanid)!=null
				&&PuPubVO.getString_TrimZeroLenAsNull(ReLockTrayDialog.batchcode)!=null){
			strWhere.append(" and cdt_pk in (select ctrayid from wds_xnrelation where isnull(dr,0)=0 and cxntrayid = '"+ReLockTrayDialog.xntrayid+"'" +
				" and pk_invmandoc = '"+ReLockTrayDialog.invmanid+"' and vbatchcode = '"+ReLockTrayDialog.batchcode+"')");
		}
			
		else
			return null;
		strWhere.append(" and cdt_traystatus = "+StockInvOnHandVO.stock_state_lock);
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
		return vos;
	}
}
