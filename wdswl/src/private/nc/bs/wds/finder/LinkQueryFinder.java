package nc.bs.wds.finder;
import nc.vo.scm.constant.ScmConst;
import nc.vo.wl.pub.IBillDataFinder2;
import nc.vo.wl.pub.Wds2WlPubConst;
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
			return new String[] {WdsWlPubConst.WDSP,WdsWlPubConst.WDSF,Wds2WlPubConst.billtype_alloinsendorder};
		}else if(WdsWlPubConst.WDSP.equals(type)){//wds调拨入库回传单
			return new String[] {WdsWlPubConst.GYL4E};
		}else if(WdsWlPubConst.BILLTYPE_OUT_IN.equals(type)){//退货入库
			return new String[]{WdsWlPubConst.WDSF,"4C"};//销售出库单
		}else if(WdsWlPubConst.BILLTYPE_OTHER_OUT.equals(type)){//其他出库
			return new String[]{WdsWlPubConst.WDSF,WdsWlPubConst.GYL4I,WdsWlPubConst.BILLTYPE_OTHER_IN};//ERP其他出库，物流其他入库
		}else if(WdsWlPubConst.BILLTYPE_OTHER_IN.equals(type)){//其他入库
			return new String[]{WdsWlPubConst.WDSF,WdsWlPubConst.GYL4A};//销售出库单
		}else if(WdsWlPubConst.HWTZ.equals(type)){//货位调整单
			return new String[]{WdsWlPubConst.BILLTYPE_OTHER_OUT};//其他出库
		}
		
//		zhf add
		else if(Wds2WlPubConst.billtype_statusupdate.equals(type)){//存货状态变更单
			return new String[]{WdsWlPubConst.BILLTYPE_OTHER_OUT};//其他出库
		}
		else if(WdsWlPubConst.WDSS.equals(type)){//特殊业务
			return new String[]{WdsWlPubConst.BILLTYPE_OTHER_OUT,WdsWlPubConst.BILLTYPE_OTHER_IN};//其他出库
		}
		else if(WdsWlPubConst.WDSG.equals(type)){//调出运单
			return new String[]{WdsWlPubConst.BILLTYPE_ALLO_OUT};//调拨出库
		}
		else if(WdsWlPubConst.BILLTYPE_ALLO_OUT.equals(type)){//调拨出库
			return new String[]{WdsWlPubConst.WDSX,WdsWlPubConst.WDSF};//其他出库
		}
		else if(WdsWlPubConst.WDSX.equals(type)){//调拨出库回传
			return new String[]{ScmConst.m_allocationOut};//erp调拨出库
		}
//		zhf end
		
		
		return null;
	}

}
