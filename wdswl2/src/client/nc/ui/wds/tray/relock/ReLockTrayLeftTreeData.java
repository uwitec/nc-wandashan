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
	 * zhf  �˴����ܴ���©��  ������� һ���λ�Ʒ  ���¼�뵽ͬһ����������ʱ   ���ְ󶨵������  
	 * �����������
	 * ���� �����δ�� ������100   �����100 ����  60������ʵ������  40��û�а�ʵ������
	 * 
	 * ��γ��⣿  �������������� ����û�ָ����ʵ������  ϵͳ��ʵ�����̵�����״̬���
	 * ʵ������δ����ļ�������  �ȴ��´εĳ��� 
	 * 
	 * �ڽ��������� ������  ���������У��
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
