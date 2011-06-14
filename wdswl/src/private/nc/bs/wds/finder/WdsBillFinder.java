package nc.bs.wds.finder;

import nc.bs.trade.billsource.IBillDataFinder;
import nc.vo.wl.pub.WdsWlPubConst;
/**
 * BillFinder
 * @author zpm
 *
 */
public class WdsBillFinder extends AbstractBillFinder {
	
	public WdsBillFinder() {
		super();
	}
	
	public IBillDataFinder createBillDataFinder(String billType) throws Exception {
		return new WdsDataFinder();
	}
	//注册下游单据类型
	public String[] getAllBillType() {
		String type = getCurrentvo().getType();
		if(WdsWlPubConst.WDS1.equals(type)){//发运计划录入
			return new String[]{WdsWlPubConst.WDS3};
		}else if(WdsWlPubConst.WDS3.equals(type)){//发运订单
			return new String[]{WdsWlPubConst.BILLTYPE_OTHER_OUT};
		}else if(WdsWlPubConst.BILLTYPE_OTHER_OUT.equals(type)){//其它出库
			return new String[]{WdsWlPubConst.BILLTYPE_OTHER_IN};
		}else if(WdsWlPubConst.BILLTYPE_OTHER_IN.equals(type)){//其它入库
			return null;
		}else if(WdsWlPubConst.WDS5.equals(type)){//销售运单
			return new String[]{WdsWlPubConst.BILLTYPE_SALE_OUT};
		}else if(WdsWlPubConst.BILLTYPE_SALE_OUT.equals(type)){//销售出库
			return new String[]{"4C"};//供应链销售出库
		}else if(WdsWlPubConst.WDSC.equals(type)){//采购取样
			return new String[] {WdsWlPubConst.BILLTYPE_OTHER_OUT};
		}
		return null;
	}

}
