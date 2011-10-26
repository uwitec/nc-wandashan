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
	
	
	private OtherInSumBO oin = null;
	private OtherOutSumBO oout = null;
	
	public static int type_in_invoice = 0;
	public static int type_in_estimate = 1;
	public static int type_in_lastestimate = 2;
	public static int type_in_product = 3;
	
	public static int type_out_sale = 4;
	public static int type_out_allo = 5;
	public static int type_out_self = 6;
	
//	后续扩展增加   两列 zhf 2011-08-25
	public static int type_in_other = 7;//其他入库单
	public static int type_out_other = 8;//其他出库单
	
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
		}else if(type == type_in_other){
			if(oin == null)
				oin = new OtherInSumBO(dao);
			return oin;
		}else if(type == type_out_other){
			if(oout == null)
				oout = new OtherOutSumBO(dao);
			return oout;
		}
			
		return null;		
	}

}
