package nc.vo.wdsnew.pub;

import nc.bs.zmpub.pub.tool.SingleVOChangeDataBsTool;
import nc.vo.dm.so.deal2.StoreInvNumVO;
import nc.vo.ic.pub.StockInvOnHandVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.zmpub.pub.report.ReportBaseVO;

/**
 * 库存可用量bo
 * 
 * @author mlr
 */
public class AvailNumDea2BO extends AvailNumBO {
	
	public SuperVO[] getAvailNumDatas(SuperVO[] vos) throws Exception {
		// 得到现存量
		SuperVO[] nvos = super.queryStockCombin(vos);
		if (nvos == null || nvos.length == 0)
			return null;
		// 得到订单占用量
		ReportBaseVO[] dvos = getAvailNum1(vos);
		
		// 得到可用量
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
				throw new BusinessException("获取存量数据异常");
		}

		StoreInvNumVO[] stocks = (StoreInvNumVO[]) SingleVOChangeDataBsTool
				.runChangeVOAry(hands, StoreInvNumVO.class,
						"nc.ui.wds.self.changedir.CHGHANDTOSTOCK");

		int len = stocks.length;

		StoreInvNumVO tmp = null;
		for (int i = 0; i < len; i++) {
			tmp = stocks[i];
			ReportBaseVO dvo = dvos==null?new ReportBaseVO():dvos[i];// 封装是订单占用量
			if (tmp == null)
				continue;
			if (dvo == null)
				dvo = new ReportBaseVO();
			// "whs_stocktonnage","whs_stockpieces"

			// 获取计划量 交换时 已获取
			// 获取现存量 交换时 已获取

			// 获取占用量
			tmp.setNdealnum(PuPubVO.getUFDouble_NullAsZero(dvo
					.getAttributeValue("whs_stocktonnage")));
			tmp.setNdealassnum(PuPubVO.getUFDouble_NullAsZero(dvo
					.getAttributeValue("whs_stockpieces")));

			// 设置可用量
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
