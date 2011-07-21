package nc.ui.pp.ask;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import nc.itf.pp.ask.IAsk;
import nc.itf.pp.ask.IAskForReport;
import nc.ui.pub.ClientEnvironment;
import nc.vo.pp.ask.AskbillHeaderVO;
import nc.vo.pp.ask.AskbillMergeVO;
import nc.vo.pp.ask.EffectPriceParaVO;
import nc.vo.pp.ask.PriceauditHeaderVO;
import nc.vo.pp.ask.VendorInvPriceVO;
import nc.vo.pp.ask.VendorVO;
import nc.vo.pp.price.QuoteConVO;
import nc.vo.pp.price.StatParaVO;
import nc.vo.pp.price.StockExecVO;
import nc.vo.pp.price.StockVarVO;
import nc.vo.pp.report.QuotedpricesVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.query.ConditionVO;

public class AskHelper {
	private  static String beanName=IAsk.class.getName();
	private  static String beanNameForReport=IAskForReport.class.getName();
	
	public static String getPkDeptByPkPsn(String p0) throws Exception{
		IAsk bo = (IAsk)nc.bs.framework.common.NCLocator.getInstance().lookup(beanName);    
		String pk_psndoc = bo.getPkDeptByPkPsnForAsk(p0);	
		return pk_psndoc;
	 } 
	public static VendorVO[] queryVendorDetail(String p0,String p1) throws Exception{
		IAsk bo = (IAsk)nc.bs.framework.common.NCLocator.getInstance().lookup(beanName);    
		VendorVO[] vendorVO = bo.queryVendorDetail(p0,p1);	
		return vendorVO;
	 } 
	public static boolean deleteMy(Vector p0) throws Exception{
		IAsk bo = (IAsk)nc.bs.framework.common.NCLocator.getInstance().lookup(beanName);    
		boolean isSuccess = false;
		isSuccess = bo.discardAskbillVOsMy(p0);	
		return isSuccess;
	 } 
	public static boolean deleteMyForPriceAudit(Vector p0) throws Exception{
		IAsk bo = (IAsk)nc.bs.framework.common.NCLocator.getInstance().lookup(beanName);    
		boolean isSuccess = false;
		isSuccess = bo.discardPriceAuditbillVOsMy(p0);	
		return isSuccess;
	 } 
	public static Vector queryAllInquireMy(String strSQL) throws Exception{
		IAsk bo = (IAsk)nc.bs.framework.common.NCLocator.getInstance().lookup(beanName);    
		Vector v = bo.queryAllInquireMy(strSQL);	
		return v;
	 }
	public static Vector queryAllInquireMy(ConditionVO[] p0, String p1, UFBoolean[] p2,String p3) throws Exception{
		IAsk bo = (IAsk)nc.bs.framework.common.NCLocator.getInstance().lookup(beanName);    
		Vector v = bo.queryAllInquireMy(p0,p1,p2,p3);	
		return v;
	 }
	public static Vector queryAllForPriceAudit(ConditionVO[] p0, String p1, UFBoolean[] p2) throws Exception{
		IAsk bo = (IAsk)nc.bs.framework.common.NCLocator.getInstance().lookup(beanName);    
		Vector v = bo.queryAllForPriceAudit(p0,p1,p2);	
		return v;
	 }
	public static Vector queryAllForPriceAudit(String p0) throws Exception{
		IAsk bo = (IAsk)nc.bs.framework.common.NCLocator.getInstance().lookup(beanName);    
		Vector v = bo.queryAllForPriceAudit(p0);	
		return v;
	 }
	public static Vector queryAllForPriceAudit(ConditionVO[] p0, String p1, UFBoolean[] p2,String strOpr) throws Exception{
		IAsk bo = (IAsk)nc.bs.framework.common.NCLocator.getInstance().lookup(beanName);    
		Vector v = bo.queryAllForPriceAudit(p0,p1,p2,strOpr);	
		return v;
	 }
	public static Vector  queryAllForPriceAudit(String sCommenWhere, String pk_corp,String strOpr,boolean iswaitaudit) throws Exception{
		IAsk bo = (IAsk)nc.bs.framework.common.NCLocator.getInstance().lookup(beanName);    
		Vector v = bo.queryAllForPriceAudit(sCommenWhere, pk_corp, strOpr, iswaitaudit);	
		return v;
	}
//	public static Vector queryAllForPriceAudit( String p0) throws Exception{
//		IAsk bo = (IAsk)nc.bs.framework.common.NCLocator.getInstance().lookup(beanName);    
//		Vector v = bo.queryAllForPriceAudit(p0);	
//		return v;
//	 }
	public static VendorInvPriceVO[] queryForVendorInvPrice(ConditionVO[] p0, String p1) throws Exception{
		IAsk bo = (IAsk)nc.bs.framework.common.NCLocator.getInstance().lookup(beanName);    
		VendorInvPriceVO[] v = bo.queryForVendorInvPrice(p0,p1);	
		return v;
	 }
	public static Vector queryBodysForPriceAudit(ConditionVO[] p0, String p1, String p2) throws Exception{
		IAsk bo = (IAsk)nc.bs.framework.common.NCLocator.getInstance().lookup(beanName);    
		Vector v = bo.queryBodysForPriceAudit(p0,p1,p2);	
		return v;
	 }
	public static AskbillHeaderVO[] queryHeadersForPriceAudit(ConditionVO[] p0, String p1) throws Exception{
		IAsk bo = (IAsk)nc.bs.framework.common.NCLocator.getInstance().lookup(beanName);    
		AskbillHeaderVO[] v = bo.queryHeadersForPriceAudit(p0,p1);	
		return v;
	 }
	public static Vector findByPrimaryKey(String p0) throws Exception{
		IAsk bo = (IAsk)nc.bs.framework.common.NCLocator.getInstance().lookup(beanName);    
		Vector v = bo.findByPrimaryKeyForAskBill(p0);	
		return v;
	 } 
	public static Vector findByPrimaryKeyForDataPower(String p0,ConditionVO[] p1) throws Exception{
		IAsk bo = (IAsk)nc.bs.framework.common.NCLocator.getInstance().lookup(beanName);    
		Vector v = bo.findByPrimaryKeyForAskBillForDataPower(p0,p1);	
		return v;
	 } 
	
	public static Vector findByPrimaryKeyForPriceAuditBill(String p0) throws Exception{
		IAsk bo = (IAsk)nc.bs.framework.common.NCLocator.getInstance().lookup(beanName);    
		Vector v = bo.findByPrimaryKeyForPriceAuditBill(p0);	
		return v;
	 } 
//	public static Vector findByPrimaryKeyForPriceAuditBill(String p0,ConditionVO[] p1) throws Exception{
//			IAsk bo = (IAsk)nc.bs.framework.common.NCLocator.getInstance().lookup(beanName);    
//			Vector v = bo.findByPrimaryKeyForPriceAuditBill(p0,p1);	
//			return v;
//	} 
	public static Vector queryAllBodys(ArrayList  p0) throws Exception{
		IAsk bo = (IAsk)nc.bs.framework.common.NCLocator.getInstance().lookup(beanName);    
		Vector v = bo.queryAllBodysForAskBill(p0);	
		return v;
	 } 
	public static Vector queryAllBodysForPriceAudit(ArrayList  p0) throws Exception{
		IAsk bo = (IAsk)nc.bs.framework.common.NCLocator.getInstance().lookup(beanName);    
		Vector v = bo.queryAllBodysForPriceAudit(p0);	
		return v;
	 } 
	public static ArrayList queryForAudit(String  p0) throws Exception{
		IAsk bo = (IAsk)nc.bs.framework.common.NCLocator.getInstance().lookup(beanName);    
		ArrayList v = bo.queryForAudit(p0);	
		return v;
	 } 
	public static Hashtable getEffectAskPrice(EffectPriceParaVO  p0) throws Exception{
		IAsk bo = (IAsk)nc.bs.framework.common.NCLocator.getInstance().lookup(beanName);    
		Hashtable v = bo.getEffectAskPrice(p0);	
		return v;
	 } 
	
	public static Hashtable getBusiIdForOrd(ArrayList p0) throws Exception {
		IAsk bo = (IAsk)nc.bs.framework.common.NCLocator.getInstance().lookup(beanName);    
		Hashtable v = bo.getBusiIdForOrd(p0);	
		return v;
	}
	public static Hashtable querySourceInfoForGenOrder(Vector p0) throws Exception{
		IAsk bo = (IAsk)nc.bs.framework.common.NCLocator.getInstance().lookup(beanName);    
		Hashtable v = bo.querySourceInfoForGenOrder(p0);	
		return v;
	}
//	public static String queryCurrIDByCurrName(String p0) throws Exception{
//		IAsk bo = (IAsk)nc.bs.framework.common.NCLocator.getInstance().lookup(beanName);    
//		String v = bo.queryCurrIDByCurrName(p0);	
//		return v;
//	}
	public static Vector updateMyForExcelTOBill(Vector p0) throws Exception{
		IAsk bo = (IAsk)nc.bs.framework.common.NCLocator.getInstance().lookup(beanName);    
		Vector v = bo.updateMyForExcelToBill(p0);	
		return v;
	 }
	public static Hashtable queryEmailAddrForAskSend(String[] p0) throws Exception{
		IAsk bo = (IAsk)nc.bs.framework.common.NCLocator.getInstance().lookup(beanName);    
		Hashtable v = bo.queryEmailAddrForAskSend(p0);	
		return v;
	}
	public static void CheckIsGenOrder (String p0) throws Exception{
		IAsk bo = (IAsk)nc.bs.framework.common.NCLocator.getInstance().lookup(beanName);    
		bo.CheckIsGenOrder(p0);
	}
	public static  AskbillMergeVO queryDetailVOMy(ConditionVO[] p0, String p1, UFBoolean[] p2) throws Exception{
		IAsk bo = (IAsk)nc.bs.framework.common.NCLocator.getInstance().lookup(beanName);    
		return bo.queryDetailVOMy(p0,p1,p2);
	}
	public static  AskbillMergeVO queryDetailVOMy(String p0,String p1,UFBoolean[] p2) throws Exception{
		IAsk bo = (IAsk)nc.bs.framework.common.NCLocator.getInstance().lookup(beanName);    
		return bo.queryDetailVOMy(p0,p1,p2);
	}
	public static AskbillMergeVO queryStatisVOMy(
			ConditionVO[] p0,
			String p1,
			UFBoolean[] p2,
			String[] p3,
			String p4)
			throws Exception{
			IAsk bo = (IAsk)nc.bs.framework.common.NCLocator.getInstance().lookup(beanName);    
	        return bo.queryStatisVOMy(p0,p1,p2,p3,p4);
	}
	public static QuoteConVO[] queryQuoteConVOsMy(StatParaVO p0) throws  Exception{
		IAsk bo = (IAsk)nc.bs.framework.common.NCLocator.getInstance().lookup(beanName);    
        return bo.queryQuoteConVOsMy(p0);
    }
	public static StockExecVO[] queryStockStatVOsMy(StatParaVO p0)
	throws Exception{
		IAsk bo = (IAsk)nc.bs.framework.common.NCLocator.getInstance().lookup(beanName);    
        return bo.queryStockStatVOsMy(p0);
    }
	public static QuoteConVO[] queryPurExecVOsMy(StatParaVO p0) throws Exception{
		IAsk bo = (IAsk)nc.bs.framework.common.NCLocator.getInstance().lookup(beanName);    
        return bo.queryPurExecVOsMy(p0);
    }
	public static StockVarVO[] queryStockVarVOsMy(
			StatParaVO p0)
			throws BusinessException {
				IAsk bo = (IAsk)nc.bs.framework.common.NCLocator.getInstance().lookup(beanName);    
		        return bo.queryStockVarVOsMy(p0);
		    }
	public static QuotedpricesVO[] queryQuotedPrices(String p0, ConditionVO[] p1) 
	throws BusinessException{
		IAskForReport bo = (IAskForReport)nc.bs.framework.common.NCLocator.getInstance().lookup(beanNameForReport);    
        return bo.queryQuotedPrices(p0,p1);
    }
	public static Vector doSaveForAskBill(Vector p0) throws BusinessException {
		IAsk bo = (IAsk)nc.bs.framework.common.NCLocator.getInstance().lookup(beanName);    
        return bo.doSaveForAskBill(p0);
    }
	public static Vector doSaveForPriceAuditBill(Vector p0) throws BusinessException {
		IAsk bo = (IAsk)nc.bs.framework.common.NCLocator.getInstance().lookup(beanName);    
        return bo.doSaveForPriceAuditBill(p0);
    }
	public static String[] queryForVendorSelected(String p0,String[] p1) throws BusinessException{
		IAsk bo = (IAsk)nc.bs.framework.common.NCLocator.getInstance().lookup(beanName);    
        return bo.queryForVendorSelected(p0,p1);
    }
	public static PriceauditHeaderVO[] queryHeadersForPriceAudit2(ConditionVO[] p0, String p1) throws Exception{
		IAsk bo = (IAsk)nc.bs.framework.common.NCLocator.getInstance().lookup(beanName);    
		PriceauditHeaderVO[] v = bo.queryHeadersForPriceAudit2(p0,p1);	
		return v;
	}
	public static Vector queryAllBodysForPriceAudit2(ArrayList  p0) throws Exception{
		IAsk bo = (IAsk)nc.bs.framework.common.NCLocator.getInstance().lookup(beanName);    
		Vector v = bo.queryAllBodysForPriceAudit2(p0);	
		return v;
	 } 
}
