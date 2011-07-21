package nc.ui.zb.entry;

import java.util.ArrayList;

import nc.ui.pub.ClientEnvironment;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.zb.pub.LongTimeTask;
import nc.vo.po.OrderHeaderVO;
import nc.vo.po.OrderItemVO;
import nc.vo.po.OrderVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.constant.ScmConst;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.zb.entry.ZbResultBodyVO;
import nc.vo.zb.entry.ZbResultHeadVO;
import nc.vo.zb.pub.ZbPubConst;

public class ZbEntryHelper {
	
	private final static String ZbEntryBo = "nc.bs.zb.entry.ZbEntryBo";
	
	/**
	 * 获取供应商的税率
	 * @author zhw
	 * @说明：（鹤岗矿业）
	 * 2011-5-16下午05:37:41
	 * @param al
	 * @return
	 * @throws Exception 
	 */
	public static Object getnOrderTaxRate(String custbasid) throws Exception{
		
		Class[] ParameterTypes = new Class[]{String.class};
		Object[] ParameterValues = new Object[]{custbasid};
		Object o = LongTimeTask.callRemoteService("pu", ZbEntryBo, "getnOrderTaxRate", ParameterTypes, ParameterValues, 2);
		return o;
		
	}
	
	/**
	 * 获取采购组织
	 * @author zhw
	 * @说明：（鹤岗矿业）
	 * 2011-5-16下午05:37:41
	 * @param al
	 * @return
	 * @throws Exception 
	 */
	public static Object getpk_calbody(String ccorpid) throws Exception{
		
		Class[] ParameterTypes = new Class[]{String.class};
		Object[] ParameterValues = new Object[]{ccorpid};
		Object o  = LongTimeTask.callRemoteService("pu", ZbEntryBo, "getpk_calbody", ParameterTypes, ParameterValues, 2);
		
		return o;
		
	}
	
	/**
	 * 校验是否存在下游单据
	 * @author zhw
	 * @说明：（鹤岗矿业）
	 * 2011-5-16下午05:37:41
	 * @param al
	 * @return
	 * @throws Exception 
	 */
	public static void isExitDownBill(ZbResultBodyVO bodyvo) throws Exception{
	    
		Class[] ParameterTypes = new Class[]{ZbResultBodyVO.class};
		Object[] ParameterValues = new Object[]{bodyvo};
		LongTimeTask.callRemoteService("pu",ZbEntryBo, "isExitDownBill", ParameterTypes, ParameterValues, 2);
		
	}
	
	/**
	 * 临时供应商对应的  正式供应商id必须存在
	 * @author zhw
	 * @说明：（鹤岗矿业）
	 * 2011-5-16下午05:37:41
	 * @param al
	 * @return
	 * @throws Exception 
	 */
	public  static Object isExitCcustid(ZbResultHeadVO headvo) throws Exception{
	    
		Class[] ParameterTypes = new Class[]{ZbResultHeadVO.class};
		Object[] ParameterValues = new Object[]{headvo};
		Object o =LongTimeTask.callRemoteService("pu",ZbEntryBo, "isExitCcustid", ParameterTypes, ParameterValues, 2);
		return o;
	}
	
	/**
	 * 临时供应商对应的  正式供应商id必须存在
	 * @author zhw
	 * @说明：（鹤岗矿业）
	 * 2011-5-16下午05:37:41
	 * @param al
	 * @return
	 * @throws Exception 
	 */
	public  static void isComplete(String str) throws Exception{
	    
		Class[] ParameterTypes = new Class[]{String.class};
		Object[] ParameterValues = new Object[]{str};
		LongTimeTask.callRemoteService("pu",ZbEntryBo, "isComplete", ParameterTypes, ParameterValues, 2);
	}
	
	/**
	 * vo交换
	 * @author zhw
	 * @说明：（鹤岗矿业）
	 * 2011-5-16下午05:37:41
	 * @param al
	 * @return
	 * @throws Exception 
	 */
	public static OrderVO chgZB05TO21(HYBillVO billVO) throws Exception {
		ZbResultHeadVO headvo = (ZbResultHeadVO) billVO.getParentVO();
		
		
		//临时供应商对应的  正式供应商id必须存在
		Object ven=isExitCcustid(headvo);
		Object[] os =null;
		if(ven !=null)
			os=(Object[])ven;
		
		ZbResultBodyVO[] bodyvo = (ZbResultBodyVO[]) billVO.getChildrenVO();
		int len = bodyvo.length;
		OrderVO vo = new OrderVO();
		OrderHeaderVO head = new OrderHeaderVO();
		ArrayList<OrderItemVO> al = new ArrayList<OrderItemVO>();
		head.setPk_corp(ClientEnvironment.getInstance().getCorporation().getPk_corp());// 公司
		head.setDorderdate(ClientEnvironment.getInstance().getDate());// 单据日期
		head.setCpurorganization(PuPubVO.getString_TrimZeroLenAsNull(getpk_calbody(ClientEnvironment.getInstance().getCorporation().getPk_corp())));// 采购组织
		String s =PuPubVO.getString_TrimZeroLenAsNull(headvo.getCcustbasid());
		if(s.equalsIgnoreCase("null")|| s==null){
			if(os!=null&&os.length>0){
				head.setCvendorbaseid(PuPubVO.getString_TrimZeroLenAsNull(os[0]));// 供应商基础id
				head.setCvendormangid(PuPubVO.getString_TrimZeroLenAsNull(os[1]));// 供应商管理id
			}
		}else{
			head.setCvendorbaseid(headvo.getCcustbasid());// 供应商基础id
			head.setCvendormangid(headvo.getCcustmanid());// 供应商管理id
		}
		head.setCdeptid(headvo.getPk_deptdoc());// 采购部门 
		head.setForderstatus(new Integer(0));//自由态
		head.setBislatest(UFBoolean.TRUE);//是否是最新
		
		head.setCemployeeid(headvo.getVemployeeid());// 采购人员
		head.setCoperator(ClientEnvironment.getInstance().getUser().getPrimaryKey());// 制单人
		head.setTmaketime(ClientEnvironment.getServerTime()); // 制单时间
		head.setNversion(1);// 版本信息
		head.setVordercode(HYPubBO_Client.getBillNo(ScmConst.PO_Order,ClientEnvironment.getInstance().getCorporation().getPk_corp(),null, null));
		head.setNexchangeotobrate(new UFDouble(1.00));// 折本汇率
		
		isComplete(headvo.getPrimaryKey());
		
		Object o = getnOrderTaxRate(headvo.getCcustbasid());
		if(PuPubVO.getUFDouble_NullAsZero(o).equals(UFDouble.ZERO_DBL))
			throw new BusinessException("供应商税率为空,请维护税率");
		for (int i = 0; i < len; i++) {
			// 校验是否存在下游单据
			isExitDownBill(bodyvo[i]);
			UFDouble  nzbnum =PuPubVO.getUFDouble_NullAsZero(bodyvo[i].getNzbnum());//中标数量
			UFDouble  nre =PuPubVO.getUFDouble_NullAsZero(bodyvo[i].getReserve10());//累计数量
			UFDouble  norder = nzbnum.sub(nre);//订单数量
			if(nre.compareTo(UFDouble.ZERO_DBL)<0)
				throw new BusinessException("订单数量不可小于零");
			if(norder.compareTo(UFDouble.ZERO_DBL)==0)
				continue;
			OrderItemVO item = new OrderItemVO();
			item.setPk_corp(ClientEnvironment.getInstance().getCorporation().getPk_corp());// 公司
			item.setCrowno(bodyvo[i].getCrowno());// 行号
			item.setCmangid(bodyvo[i].getCinvmanid());// 存货管理ID
			item.setCbaseid(bodyvo[i].getCinvbasid());// 存货基本ID
			item.setNordernum(norder);// 订货数量
			item.setIdiscounttaxtype(1);// 扣税类别 1---应税外加
			
			item.setNoriginalcurprice(bodyvo[i].getNorderprice());// 无税单价
			item.setCupsourcebillid(bodyvo[i].getCzbresultid()); // 来源单据id
			item.setCupsourcebillrowid(bodyvo[i].getCzbresultbid());// 来源单据行id
			item.setCupsourcebilltype(ZbPubConst.ZB_Result_BILLTYPE);// 来源单据类型
			item.setCsourcebillid(bodyvo[i].getCupsourcebillid());
			item.setCsourcerowid(bodyvo[i].getCupsourcebillrowid());
			item.setCsourcebilltype(bodyvo[i].getCupsourcebilltype());
			item.setCcurrencytypeid(ZbPubConst.pk_currtype);// 币种
			item.setPk_arrvcorp(ClientEnvironment.getInstance().getCorporation().getPk_corp());// 收货公司
			item.setIisactive(new Integer(0));//是否激活
			item.setNtaxrate(PuPubVO.getUFDouble_NullAsZero(o));//税率
			
			item.setPk_invoicecorp(ClientEnvironment.getInstance().getCorporation().getPk_corp());// 收票公司
			item.setNexchangeotobrate(new UFDouble(1.00));// 折本汇率
			
			//无税单价
			UFDouble nOrderNoTaxPrice = null;
			//税率订货
			UFDouble nOrderTaxRate = null;
			nOrderTaxRate = PuPubVO.getUFDouble_NullAsZero(item.getNtaxrate()).multiply(new UFDouble(0.01));
			nOrderNoTaxPrice = PuPubVO.getUFDouble_NullAsZero(item.getNoriginalcurprice());// 无税单价
			item.setNoriginalnetprice(nOrderNoTaxPrice);// 净无税单价
			// 金额=数量*无税单价
			item.setNoriginalcurmny(nOrderNoTaxPrice.multiply(PuPubVO.getUFDouble_NullAsZero(item.getNordernum())));
			// 含税单价
			item.setNorgtaxprice(nOrderNoTaxPrice.add(nOrderTaxRate.multiply(nOrderNoTaxPrice)));
			// 净含税单价
			item.setNorgnettaxprice(nOrderNoTaxPrice.add(nOrderTaxRate.multiply(nOrderNoTaxPrice)));
			// 税额=金额*税率 按应税外加
			item.setNoriginaltaxmny(nOrderTaxRate.multiply(item.getNoriginalcurmny()));
			// 价税合计=金额+税额
			item.setNoriginaltaxpricemny(item.getNoriginalcurmny().add(item.getNoriginaltaxmny()));
			item.setForderrowstatus(new Integer(0)); //自由态
			
			al.add(item);
			
		}
		
//		if(al ==null || al.size()==0)
//			throw new BusinessException("已经生成合同");
		vo.setParentVO(head);
		vo.setChildrenVO(al.toArray(new OrderItemVO[0] ));
		return vo;
	}
}
