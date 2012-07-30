package nc.vo.wdsnew.pub;

import nc.bs.zmpub.pub.tool.SingleVOChangeDataBsTool;
import nc.vo.dm.so.deal2.StoreInvNumVO;
import nc.vo.ic.pub.StockInvOnHandVO;
import nc.vo.pub.SuperVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.zmpub.pub.report.ReportBaseVO;

/**
 * ��������bo
 * 
 * @author mlr
 */
public class AvailNumDea2BO extends AvailNumBO {
	protected SuperVO[] getNums(SuperVO[] nvos, ReportBaseVO[] dvos)
	throws Exception {
		if (nvos == null || nvos.length == 0)
			return null;
		
		StockInvOnHandVO[] hands = null;
		try{
			hands = (StockInvOnHandVO[])nvos;
		}catch(Exception e){
			return super.getNums(nvos, dvos);
		}
		
		if (nvos.length != dvos.length)
			return nvos;
		
		StoreInvNumVO[] stocks = (StoreInvNumVO[])SingleVOChangeDataBsTool.
		runChangeVOAry(hands, StoreInvNumVO.class, "nc.ui.wds.self.changedir.CHGHANDTOSTOCK");
		
		int len = stocks.length;
		
		StoreInvNumVO tmp = null;
		for (int i = 0; i < len; i++) {
			tmp = stocks[i];
			ReportBaseVO dvo = dvos[i];//��װ�Ƕ���ռ����
			if (tmp == null)
				continue;
			if (dvo == null)
				continue;
//			 "whs_stocktonnage","whs_stockpieces" 
			
//			��ȡ�ƻ���   ����ʱ �ѻ�ȡ
//			��ȡ�ִ���   ����ʱ  �ѻ�ȡ
			
//			��ȡռ����
			tmp.setNdealnum(PuPubVO.getUFDouble_NullAsZero(dvo.getAttributeValue("whs_stocktonnage")));
			tmp.setNdealassnum(PuPubVO.getUFDouble_NullAsZero(dvo.getAttributeValue("whs_stockpieces")));
			
//			���ÿ�����
			tmp.setNnum(tmp.getNstocknum().sub(tmp.getNdealnum()));
			tmp.setNassnum(tmp.getNstockassnum().sub(tmp.getNdealassnum()));
		}
		return nvos;
	}
}
