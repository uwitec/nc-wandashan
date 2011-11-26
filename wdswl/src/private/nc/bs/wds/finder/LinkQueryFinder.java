package nc.bs.wds.finder;
import nc.vo.wl.pub.IBillDataFinder2;
import nc.vo.wl.pub.WdsWlPubConst;
public class LinkQueryFinder extends AbstractBillFinder2{
	public LinkQueryFinder() {
		super();
	}	
	public IBillDataFinder2 createBillDataFinder(String billType) throws Exception {
		return new WdsDataFinder2();
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
			return new String[]{WdsWlPubConst.WDSO};//销售出库回传单
		}else if(WdsWlPubConst.WDSO.equals(type)){//销售出库回传单
			return new String[]{"4C"};//销售出库单
		}else if(WdsWlPubConst.WDSC.equals(type)){//采购取样
			return new String[] {WdsWlPubConst.BILLTYPE_OTHER_OUT};
		}else if(WdsWlPubConst.WDSF.equals(type)){//装卸费核算
			return null;
		}else if(WdsWlPubConst.BILLTYPE_ALLO_IN.equals(type)){//wds调拨入库
			return new String[] {WdsWlPubConst.WDSP};
		}else if(WdsWlPubConst.WDSP.equals(type)){//wds调拨入库
			return new String[] {WdsWlPubConst.GYL4E};
		}
		return null;
	}

}
