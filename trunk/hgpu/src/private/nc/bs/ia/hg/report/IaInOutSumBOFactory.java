package nc.bs.ia.hg.report;

import nc.bs.dao.BaseDAO;

/**
 * 
 * @author zhf  存货核算 收发存明细汇总表 查询组件  工厂类
 *
 */

public class IaInOutSumBOFactory {
	
	private InvoiceSumBO invoice = null;
	private EstimateSumBO estimate = null;
	private LastEstimateSumBO last = null;
	private ProductInSumBO pro = null;
	
	private SaleOutSumBO sale = null;
	private AlloOutSumBO allo = null;
	private SelfOutSumBO self = null;
	
	public static int type_in_invoice = 0;
	public static int type_in_estimate = 1;
	public static int type_in_lastestimate = 2;
	public static int type_in_product = 3;
	
	public static int type_out_sale = 4;
	public static int type_out_allo = 5;
	public static int type_out_self = 6;
	
	public AbstractInOutSumQryBO getQryBO(int type,BaseDAO dao){
		if(type == type_in_invoice){
			if(invoice == null)
				invoice = new InvoiceSumBO(dao);
			return invoice;
		}else if(type == type_in_estimate){
			if(estimate == null)
				estimate = new EstimateSumBO(dao);
			return estimate;
		}else if(type == type_in_lastestimate){
			if(last == null)
				last = new LastEstimateSumBO(dao);
			return last;
		}else if(type == type_in_product){
			if(pro == null)
				pro = new ProductInSumBO(dao);
			return pro;
		}else if(type == type_out_sale){
			if(sale == null)
				sale = new SaleOutSumBO(dao);
			return sale;
		}else if(type == type_out_allo){
			if(allo == null)
				allo = new AlloOutSumBO(dao);
			return allo;
		}else if(type == type_out_self){
			if(self == null)
				self = new SelfOutSumBO(dao);
			return self;
		}
			
		return null;		
	}

}
