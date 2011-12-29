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
			return new String[]{WdsWlPubConst.BILLTYPE_OTHER_IN,WdsWlPubConst.WDSF};
		}else if(WdsWlPubConst.BILLTYPE_OTHER_IN.equals(type)){//其它入库
			return new String[]{WdsWlPubConst.WDSF};
		}else if(WdsWlPubConst.WDS5.equals(type)){//销售运单
			return new String[]{WdsWlPubConst.BILLTYPE_SALE_OUT};
		}else if(WdsWlPubConst.BILLTYPE_SALE_OUT.equals(type)){//销售出库
			return new String[]{WdsWlPubConst.WDSO,WdsWlPubConst.WDSF};//销售出库回传单
		}else if(WdsWlPubConst.WDSC.equals(type)){//采购取样
			return new String[] {WdsWlPubConst.BILLTYPE_OTHER_OUT};
		}else if(WdsWlPubConst.WDSF.equals(type)){//装卸费核算
			return null;
		}else if(WdsWlPubConst.WDSO.equals(type)){//销售出库回传单
			return new String[]{"4C"};//销售出库单
		}else if(WdsWlPubConst.BILLTYPE_ALLO_IN.equals(type)){//wds调拨入库
			return new String[] {WdsWlPubConst.WDSP,WdsWlPubConst.WDSF};
		}else if(WdsWlPubConst.WDSP.equals(type)){//wds调拨入库回传单
			return new String[] {WdsWlPubConst.GYL4E};
		}else if(WdsWlPubConst.BILLTYPE_OUT_IN.equals(type)){//退货入库
			return new String[]{WdsWlPubConst.WDSF,"4C"};//销售出库单
		}else if(WdsWlPubConst.BILLTYPE_OTHER_OUT.equals(type)){//其他出库
			return new String[]{WdsWlPubConst.WDSF,WdsWlPubConst.GYL4I};//ERP其他出库，物流其他入库
		}else if(WdsWlPubConst.BILLTYPE_OTHER_IN.equals(type)){//其他入库
			return new String[]{WdsWlPubConst.WDSF,WdsWlPubConst.GYL4A};//销售出库单
		}
		return null;
	}

}
