package nc.bs.zb.gen;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.zb.pub.ZbBsPubTool;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.po.OrderItemVO;
import nc.vo.po.OrderVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.zb.entry.ZbResultBodyVO;
import nc.vo.zb.gen.GenOrderVO;
import nc.vo.zb.pub.ZbPubConst;
import nc.vo.zb.pub.ZbPubTool;

public class GenOrderBO {
	private BaseDAO dao = null;
	public BaseDAO getBaseDao(){
		if(dao == null){
			dao = new BaseDAO();
		}
		return dao;
	}

	/**
	 * 
	 * @author zhw
	 * @说明：（鹤岗矿业）根据供应商  存货分类 查询中标录入结果
	 * @param whereSql
	 * @param cl
	 * @return 
	 * @throws BusinessException
	 */
	public GenOrderVO[] queryDataForOrder(String whereSql, ClientLink cl) throws BusinessException {
		if (PuPubVO.getString_TrimZeroLenAsNull(whereSql) == null)
			return null;
		StringBuffer strb = new StringBuffer();
		StringBuffer strall = new StringBuffer();
		StringBuffer str = new StringBuffer();
		
		strb.append(" select h.vbillno,h.pk_corp,h.dbilldate,h.cbiddingid,h.ccustbasid,h.ccustmanid,h.pk_deptdoc,h.vemployeeid,h.pk_billtype,h.czbresultid ");

		ZbResultBodyVO bodyvo = new ZbResultBodyVO();
		String[] barrnames = bodyvo.getAttributeNames();

		for (String name : barrnames) {
			if (name.equalsIgnoreCase(bodyvo.getPKFieldName())
					|| "invcode".equalsIgnoreCase(name))
				continue;
			strb.append(",b." + name);
		}
		strb.append("," + bodyvo.getPKFieldName());

		str.append(" from zb_result_h h inner join " + bodyvo.getTableName()+ " b on h.czbresultid = b.czbresultid ");
		str.append(" inner join bd_invbasdoc inv on inv.pk_invbasdoc = b.cinvbasid ");
		str.append(" inner join bd_invcl cl on cl.pk_invcl = inv.pk_invcl ");
		str.append(" inner join zb_bidding_h zh on zh.cbiddingid = h.cbiddingid ");
		
		if (PuPubVO.getString_TrimZeroLenAsNull(whereSql) != null &&( whereSql.contains("h.temp") || whereSql.contains("h.ccustmanid"))){
			strb.append(",zh.vbillno,inv.invcode");
			strall.append(strb).append(str);
			whereSql=whereSql.replace("h.temp", "h.ccustmanid");
			strall.append(" where isnull(h.dr,0)=0 and isnull(b.dr,0)=0 and isnull(inv.dr,0)=0 and isnull(cl.dr,0)=0 and isnull(zh.dr,0)=0 ");
			strall.append(" and " + whereSql);
			strall.append(" and h.vbillstatus = " + IBillStatus.CHECKPASS);
			String s =ZbPubTool.getParam();
			if(s!=null &&!"".equals(s))
			   strall.append(" and zh.reserve1 = '" +s+"'");
			strall.append(" and (coalesce(b.nzbnum,0.0)-coalesce(b.reserve10,0.0))>0 ");
			strall.append(" order by h.vbillno,inv.invcode ");			
		}
		
		List ldata = (List) getBaseDao().executeQuery(strall.toString(),
				new BeanListProcessor(GenOrderVO.class));

		if (ldata == null || ldata.size() == 0) {
				return null;
		}
		GenOrderVO[] datas = (GenOrderVO[]) ldata.toArray(new GenOrderVO[0]);
//		VOUtil.ascSort(datas, ZbPubConst.DEAL_SORT_FIELDNAMES);
		return datas;
	}
	
	/**
	 * 
	 * @author zhw
	 * @说明：（鹤岗矿业）中标结果录入生成合同数量回写 2011-4-8下午01:44:52
	 * @param o1
	 *            修改后数据
	 * @param o2
	 *            修改前数据
	 * @param flag
	 *            保存/删除
	 * @throws BusinessException
	 */
	public void reWriteToResultForOrder(Object o1, Object o2, boolean flag)
			throws BusinessException {

		if (o1 == null)
			return;

		Map<String, UFDouble> newItemInfor = null;
		Map<String, UFDouble> oldItemInfor = null;
		List al = new ArrayList<String>();
		OrderItemVO[] items = null;
		
		if (o1 instanceof OrderItemVO[]) {
			items = (OrderItemVO[]) o1;
			newItemInfor = new HashMap<String, UFDouble>();
			
			for (OrderItemVO newitem : items) {
				if (flag) {// 保存
					// 如果删除 则回写0
					if(newitem.getStatus() == VOStatus.DELETED){
						newItemInfor.put(newitem.getCupsourcebillrowid(),UFDouble.ZERO_DBL);
						al.add(newitem.getCupsourcebillrowid());//
					} else {
						newItemInfor.put(newitem.getCupsourcebillrowid(), PuPubVO.getUFDouble_NullAsZero(newitem.getNordernum()));
					}
				} else {
					newItemInfor.put(newitem.getCupsourcebillrowid(), PuPubVO.getUFDouble_NullAsZero(newitem.getNordernum()));
				}

			}
		} else {
			newItemInfor = (Map<String, UFDouble>) o1;
		}
		if (o2 != null) {
			if (o2 instanceof OrderItemVO[]) {
				items = (OrderItemVO[]) o2;
				oldItemInfor = new HashMap<String, UFDouble>();
				for (OrderItemVO olditem : items) {
					oldItemInfor.put(olditem.getCupsourcebillrowid(), PuPubVO.getUFDouble_NullAsZero(olditem.getNordernum()));
				}
			} else {
				oldItemInfor = (Map<String, UFDouble>) o2;
			}
		}

		String sql = "update zb_result_b set reserve10 = coalesce(reserve10,0.0)+? where czbresultbid = ?";

		UFDouble nnum = UFDouble.ZERO_DBL;
		SQLParameter parameter = null;
		List<String> lresultbid = new ArrayList<String>();
		if (flag) {
			// int index = 0;
			for (String key : newItemInfor.keySet()) {
				parameter = new SQLParameter();
				// parameter.add
				nnum = PuPubVO.getUFDouble_NullAsZero(newItemInfor.get(key));
				if (oldItemInfor != null) {
					nnum = nnum.sub(PuPubVO.getUFDouble_NullAsZero(oldItemInfor.get(key)));
				}
				parameter.addParam(nnum);
				parameter.addParam(key);
				if (!lresultbid.contains(key))
					lresultbid.add(key);
				getBaseDao().executeUpdate(sql, parameter);
				String query = " select reserve10 from zb_result_b where czbresultbid = '" + key + "' and isnull(dr,0)=0 ";
				Object o = getBaseDao().executeQuery(query,ZbBsPubTool.COLUMNPROCESSOR);
				if (o == null)
					return;
				if (BigDecimal.ZERO.compareTo((BigDecimal) o) > 0)
					throw new BusinessException("累计数量小于零,回写出错");
			}
			return;
		}

		for (String key : newItemInfor.keySet()) {
			
			parameter = new SQLParameter();
			nnum = PuPubVO.getUFDouble_NullAsZero(newItemInfor.get(key)).multiply(-1);
			parameter.addParam(nnum);
			parameter.addParam(key);
			
			if (!lresultbid.contains(key))
				lresultbid.add(key);
			getBaseDao().executeUpdate(sql, parameter);
		}
	}
	
	//临时供应商是否存在着正式供应商ID
	public Object isExitCcustid (GenOrderVO headvo) throws BusinessException{
		String sql = "select count(0) from  bd_cumandoc where pk_cumandoc = '"+headvo.getCcustmanid()+"' and isnull(dr,0)=0 ";
		String sql1 = "select ccustbasid,ccustmanid from  bd_cubasdochg where ccubasdochgid = '"+headvo.getCcustmanid()+"' and isnull(dr,0)=0 and ccustbasid is not  null and ccustmanid is not null";
		Object o = getBaseDao().executeQuery(sql,ZbBsPubTool.COLUMNPROCESSOR);
		Object o1 = getBaseDao().executeQuery(sql1,ZbBsPubTool.ARRAYPROCESSOR);
		Object[] os = null;
		if(o1!=null)
			os = (Object[])o1;
		if(PuPubVO.getInteger_NullAs(o,new Integer(-1)).intValue()==0 &&(os==null || os.length==0))
			throw new BusinessException("临时供应商对应的正式供应商不存在");
		return os;
	}
	
	//校验是否超过中标结果数量
	public void checkOrderNum(OrderVO order) throws BusinessException{
		if(order !=null){
			OrderItemVO[] items = order.getBodyVO();
			for(OrderItemVO item:items){
				String csourcerowid = item.getCupsourcebillrowid();
				String type =item.getCupsourcebilltype();
				if(PuPubVO.getString_TrimZeroLenAsNull(csourcerowid)==null ||(PuPubVO.getString_TrimZeroLenAsNull(csourcerowid)!=null &&
						!PuPubVO.getString_TrimZeroLenAsNull(type).equals(ZbPubConst.ZB_Result_BILLTYPE)))
					continue;
				String sql = "select coalesce(b.nzbnum,0)-coalesce(b.reserve10,0) from zb_result_b b where b.czbresultbid = '"+csourcerowid+"' and  isnull(b.dr,0)=0 ";
				String sql1 = "select b.nordernum from po_order_b b where isnull(b.dr,0)=0 and b.corder_bid='"+item.getPrimaryKey()+"'";
				
				Object o = getBaseDao().executeQuery(sql, new ColumnProcessor());
				Object o1 = getBaseDao().executeQuery(sql1, new ColumnProcessor());
				BigDecimal oder =PuPubVO.getUFDouble_NullAsZero(item.getNordernum().sub(PuPubVO.getUFDouble_NullAsZero(o1))).toBigDecimal().setScale(4, RoundingMode.HALF_UP);
				BigDecimal num =PuPubVO.getUFDouble_NullAsZero(o).toBigDecimal().setScale(4, RoundingMode.HALF_UP);
				if(oder.doubleValue()-num.doubleValue()>0)
					throw new BusinessException("第"+item.getCrowno()+"合同数量超过中标结果数量");
			}
		}
		
	}
}
