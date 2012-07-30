package nc.vo.wdsnew.pub;

import nc.bs.zmpub.pub.tool.SingleVOChangeDataBsTool;
import nc.vo.dm.so.deal2.StoreInvNumVO;
import nc.vo.ic.pub.StockInvOnHandVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.zmpub.pub.report.ReportBaseVO;

/**
 * ��������bo
 * 
 * @author mlr
 */
public class AvailNumDea2BO extends AvailNumBO {
	
	public SuperVO[] getAvailNumDatas(SuperVO[] vos) throws Exception {
		// �õ��ִ���
		SuperVO[] nvos = super.queryStockCombin(vos);
		if (nvos == null || nvos.length == 0)
			return null;
		// �õ�����ռ����
		ReportBaseVO[] dvos = getAvailNum1(vos);
		
		// �õ�������
		SuperVO[] zvos = getNums(nvos, dvos);
		return zvos;
	}
	
	
	protected SuperVO[] getNums(SuperVO[] nvos, ReportBaseVO[] dvos)
	throws Exception {
		if (nvos == null || nvos.length == 0)
			return null;

		StockInvOnHandVO[] hands = null;
		try {
			hands = (StockInvOnHandVO[]) nvos;
		} catch (Exception e) {
			return super.getNums(nvos, dvos);
		}

		if(dvos != null && dvos.length == 0){
		
			if (nvos.length != dvos.length)
				throw new BusinessException("��ȡ���������쳣");
		}

		StoreInvNumVO[] stocks = (StoreInvNumVO[]) SingleVOChangeDataBsTool
				.runChangeVOAry(hands, StoreInvNumVO.class,
						"nc.ui.wds.self.changedir.CHGHANDTOSTOCK");

		int len = stocks.length;

		StoreInvNumVO tmp = null;
		for (int i = 0; i < len; i++) {
			tmp = stocks[i];
			ReportBaseVO dvo = dvos==null?new ReportBaseVO():dvos[i];// ��װ�Ƕ���ռ����
			if (tmp == null)
				continue;
			if (dvo == null)
				dvo = new ReportBaseVO();
			// "whs_stocktonnage","whs_stockpieces"

			// ��ȡ�ƻ��� ����ʱ �ѻ�ȡ
			// ��ȡ�ִ��� ����ʱ �ѻ�ȡ

			// ��ȡռ����
			tmp.setNdealnum(PuPubVO.getUFDouble_NullAsZero(dvo
					.getAttributeValue("whs_stocktonnage")));
			tmp.setNdealassnum(PuPubVO.getUFDouble_NullAsZero(dvo
					.getAttributeValue("whs_stockpieces")));

			// ���ÿ�����
			tmp.setNnum(PuPubVO.getUFDouble_NullAsZero(tmp.getNstocknum()).sub(
					PuPubVO.getUFDouble_NullAsZero(tmp.getNdealnum())));
			tmp.setNassnum(PuPubVO
					.getUFDouble_NullAsZero(tmp.getNstockassnum()).sub(
							PuPubVO
									.getUFDouble_NullAsZero(tmp
											.getNdealassnum())));
		}
		return stocks;
	}
}
