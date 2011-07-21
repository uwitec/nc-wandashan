package nc.ui.zb.gen;

import java.util.List;

import nc.ui.pub.ClientEnvironment;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.zb.entry.ZbEntryHelper;
import nc.ui.zb.pub.LongTimeTask;
import nc.vo.po.OrderHeaderVO;
import nc.vo.po.OrderItemVO;
import nc.vo.po.OrderVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.constant.ScmConst;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.zb.gen.GenOrderVO;
import nc.vo.zb.pub.ZbPubConst;
import nc.vo.zb.pub.ZbPubTool;

//zhf  计划处理 前后台访问 代理类    
public class GenrationOrderHealper {

	private static String bo = "nc.bs.zb.gen.GenOrderBO";
	
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）计划处理  查询下级待处理计划
	 * 2010-11-24上午10:27:12
	 * @param whereSql
	 * @param cl
	 * @param iplantype
	 * @return
	 * @throws Exception
	 */
	public static GenOrderVO[] queryDatas(String whereSql,ClientLink cl) throws Exception{
		Class[] ParameterTypes = new Class[]{String.class,ClientLink.class};
		Object[] ParameterValues = new Object[]{whereSql,cl};
		Object o = LongTimeTask.callRemoteService("pu",bo, "queryDataForOrder", ParameterTypes, ParameterValues, 2);
		return o==null?null:(GenOrderVO[])o;
	}
	
	public static OrderVO dealVO(List<GenOrderVO> lseldata) throws Exception{
		
		int size= lseldata.size();
		
		OrderVO vo = new OrderVO();
		OrderHeaderVO head = new OrderHeaderVO();
		OrderItemVO[] body = new OrderItemVO[size];
		
		head.setPk_corp(ClientEnvironment.getInstance().getCorporation().getPk_corp());// 公司
		head.setDorderdate(ClientEnvironment.getInstance().getDate());// 单据日期
		head.setCpurorganization(PuPubVO.getString_TrimZeroLenAsNull(ZbEntryHelper.getpk_calbody(ClientEnvironment.getInstance().getCorporation().getPk_corp())));// 采购组织
		String s =PuPubVO.getString_TrimZeroLenAsNull(lseldata.get(0).getCcustbasid());
		GenOrderVO gen = lseldata.get(0);
		//临时供应商对应的  正式供应商id必须存在
		Object ven=isExitCcustid(gen);
		Object[] os =null;
		if(ven !=null)
			os=(Object[])ven;
		if(s.equalsIgnoreCase("null")|| s==null){
			if(os!=null&&os.length>0){
				head.setCvendorbaseid(PuPubVO.getString_TrimZeroLenAsNull(os[0]));// 供应商基础id
				head.setCvendormangid(PuPubVO.getString_TrimZeroLenAsNull(os[1]));// 供应商管理id
			}
		}else{
			head.setCvendorbaseid(lseldata.get(0).getCcustbasid());// 供应商基础id
			head.setCvendormangid(lseldata.get(0).getCcustmanid());// 供应商管理id
		}
		head.setCdeptid(null);// 采购部门 
		head.setForderstatus(new Integer(0));//自由态
		
		head.setCemployeeid(null);// 采购人员
		head.setCoperator(ClientEnvironment.getInstance().getUser().getPrimaryKey());// 制单人
		head.setTmaketime(ClientEnvironment.getServerTime()); // 制单时间
		head.setNversion(1);// 版本信息
		head.setVordercode(HYPubBO_Client.getBillNo(ScmConst.PO_Order,ClientEnvironment.getInstance().getCorporation().getPk_corp(),null, null));
		head.setNexchangeotobrate(new UFDouble(1.00));// 折本汇率
		head.setBislatest(UFBoolean.TRUE);//是否是最新
		
		Object o = ZbEntryHelper.getnOrderTaxRate(lseldata.get(0).getCcustbasid());
		if(PuPubVO.getUFDouble_NullAsZero(o).equals(UFDouble.ZERO_DBL))
			throw new BusinessException("供应商税率为空,请维护税率");
		for(int i=0;i<size;i++){
			gen =lseldata.get(i);
			OrderItemVO item = new OrderItemVO();
			UFDouble  nzbnum =PuPubVO.getUFDouble_NullAsZero(gen.getNzbnum());//中标数量
			UFDouble  nre =PuPubVO.getUFDouble_NullAsZero(gen.getReserve10());//累计数量
			UFDouble  norder = nzbnum.sub(nre);//订单数量
			item.setPk_corp(ClientEnvironment.getInstance().getCorporation().getPk_corp());// 公司
			item.setCrowno(gen.getCrowno());// 行号
			item.setCmangid(gen.getCinvmanid());// 存货管理ID
			item.setCbaseid(gen.getCinvbasid());// 存货基本ID
			item.setNordernum(norder);// 订货数量
			item.setIdiscounttaxtype(1);// 扣税类别 1---应税外加
			item.setForderrowstatus(new Integer(0)); //自由态
			
			item.setNoriginalcurprice(gen.getNorderprice());// 无税单价
			item.setCupsourcebillid(gen.getCzbresultid()); // 来源单据id
			item.setCupsourcebillrowid(gen.getCzbresultbid());// 来源单据行id
			item.setCupsourcebilltype(ZbPubConst.ZB_Result_BILLTYPE);// 来源单据类型
			item.setCsourcebillid(gen.getCupsourcebillid());
			item.setCsourcerowid(gen.getCupsourcebillrowid());
			item.setCsourcebilltype(gen.getCupsourcebilltype());			
			
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
			body[i] = item;
		}
		ZbPubTool.setVOsRowNoByRule(body,"crowno");//设置行号
		vo.setParentVO(head);
		vo.setChildrenVO(body);
		return vo;
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
	public  static Object isExitCcustid(GenOrderVO headvo) throws Exception{
	    
		Class[] ParameterTypes = new Class[]{GenOrderVO.class};
		Object[] ParameterValues = new Object[]{headvo};
		Object o =LongTimeTask.callRemoteService("pu",bo, "isExitCcustid", ParameterTypes, ParameterValues, 2);
		return o;
	}
	
}
