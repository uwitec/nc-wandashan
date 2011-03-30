package nc.bs.hg.so.pub;

import java.util.ArrayList;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.hg.pu.pub.HgBsPubTool;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;

public class HgSoPubBO {
	
	private BaseDAO dao = null;
	private BaseDAO getBaseDao(){
		if(dao == null){
			dao = new BaseDAO();
		}
		return dao;
	}
	
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）销售定单取采购合同价  根据入库行关联合同
	 * 2012-2-22上午10:32:43
	 * @param cinvmanid
	 * @param cbatchid
	 * @return
	 * @throws Exception
	 */
	public UFDouble[] callPurchasePactPrice(String cinvmanid,String cbatchid) throws Exception{
		if(PuPubVO.getString_TrimZeroLenAsNull(cinvmanid)==null||PuPubVO.getString_TrimZeroLenAsNull(cbatchid)==null)
			return null;
		String sql = "select ord.ntaxrate,ord.noriginalcurprice from po_order_b ord inner join ic_general_b pi" +
				" on pi.csourcebillbid = ord.corder_bid where isnull(ord.dr,0)=0 and isnull(pi.dr,0)=0 " +
				" and pi.cinventoryid = '"+cinvmanid+"'" +
		        " and pi.vbatchcode = '"+cbatchid+"'";
		
//		String sql = " ";

		Object o = getBaseDao().executeQuery(sql, HgBsPubTool.ARRAYLISTPROCESSOR);
		
		if(o == null)
			return null;
		UFDouble[] prices = null;
		List ldata = (List)o;
		if(ldata.size()==0)
			return null;
		Object[] os = (Object[])ldata.get(0);
		prices = new UFDouble[]{PuPubVO.getUFDouble_NullAsZero(os[0]),PuPubVO.getUFDouble_NullAsZero(os[1])};
		return prices;
	}
	

	/**
	 * 
	 * @author zhw
	 * @说明：（鹤岗矿业）通过采购发票的源头体ID取出合同的无税单价
	 * 2012-2-22上午10:32:43
	 * @param cinvmanid
	 * @param cbatchid
	 * @return
	 * @throws Exception
	 */
	public UFDouble calFormPoOrder(String orderbid ) throws Exception{
		if(PuPubVO.getString_TrimZeroLenAsNull(orderbid)==null)
			return null;
		String sql = "select noriginalcurprice from po_order_b where corder_bid ='"+orderbid+"' and isnull(dr,0)=0 ";

		Object o = getBaseDao().executeQuery(sql, HgBsPubTool.COLUMNPROCESSOR);
		
		if(o == null)
			return null;
		UFDouble prices = null;
		
		Object os = (Object)o;
		prices = PuPubVO.getUFDouble_NullAsZero(os);
		return prices;
	}

	/**
	 * liuys add for 鹤岗矿业
	 * 
	 * 查询登录人与供应商的关联关系
	 * 
	 * @param loginUser
	 * @return
	 * @throws Exception
	 */
	public ArrayList queryUserAndCust(String loginUser) throws Exception{
		List custcodes = null;
		if(loginUser == null)
			throw new  BusinessException("未取到登录用户,请检查");
		String sql = "select cu.custcode from hg_userandcust st,  bd_cubasdoc cu where cu.dr = 0 and st.dr=0 and st.pk_custbas = cu.pk_cubasdoc and st.pk_user = '"+loginUser.trim()+"'";
		Object o = getBaseDao().executeQuery(sql, HgBsPubTool.ARRAYLISTPROCESSOR);
		if(o != null)
			custcodes = (ArrayList)o;
		return (ArrayList) custcodes;
	}

	/**
	 * 
	 * @author zhw
	 * @说明：（鹤岗矿业）代储代销出库单取供应商 根据存货加批次
	 * 2012-2-22上午10:32:43
	 * @param cinvmanid
	 * @param cbatchid
	 * @return
	 * @throws Exception
	 */
	public UFDouble[] callPurchasePactCvendorid(String cinvmanid,String cbatchid) throws Exception{
		if(PuPubVO.getString_TrimZeroLenAsNull(cinvmanid)==null||PuPubVO.getString_TrimZeroLenAsNull(cbatchid)==null)
			return null;
		String sql = "select h.cproviderid from ic_general_h h join ic_general_b b on h.cgeneralhid = b.cgeneralhid " +
				" where nvl(h.dr, 0) = 0 and nvl(b.dr, 0) = 0 and b.cinventoryid = '"+cinvmanid+"' and b.vbatchcode = '"+cbatchid+"' ";

		Object o = getBaseDao().executeQuery(sql, HgBsPubTool.COLUMNPROCESSOR);
		
		if(o == null)
			return null;
		UFDouble[] prices = null;
		List ldata = (List)o;
		if(ldata.size()==0)
			return null;
		Object[] os = (Object[])ldata.get(0);
		prices = new UFDouble[]{PuPubVO.getUFDouble_NullAsZero(os[0]),PuPubVO.getUFDouble_NullAsZero(os[1])};
		return prices;
	}

}
