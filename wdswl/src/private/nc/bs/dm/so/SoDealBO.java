package nc.bs.dm.so;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nc.bs.dao.BaseDAO;
import nc.bs.pub.pf.PfUtilBO;
import nc.bs.pub.pf.PfUtilTools;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.ui.pub.bill.BillStatus;
import nc.vo.dm.so.deal.SoDeHeaderVo;
import nc.vo.dm.so.deal.SoDealBillVO;
import nc.vo.dm.so.deal.SoDealVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.vosplit.SplitBillVOs;
import nc.vo.wl.pub.WdsWlPubConst;

public class SoDealBO {
	
	private BaseDAO m_dao = null;
	private BaseDAO getDao() {
		if (m_dao == null) {
			m_dao = new BaseDAO();
		}
		// int a = 0;
		return m_dao;
	}

	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目
	 * @时间：2011-3-29下午02:08:02
	 * @param whereSql
	 * @return
	 * @throws Exception
	 */
	public SoDealVO[] doQuery(String whereSql,String pk_storedoc) throws Exception {
		SoDealVO[] datas = null;
		// 实现查询发运计划的逻辑
		StringBuffer sql = new StringBuffer();
		sql.append("select  ");
		String[] names = SoDealVO.m_headNames;

		for (String name : names) {
			sql.append(name + ", ");
		}
		names = SoDealVO.m_bodyNames;
		for (String name : names) {
			sql.append(name + ", ");
		}
		sql.append(" 'aaa' ");
		sql.append(" from so_sale h  " );
		sql.append(" inner join so_saleorder_b b on h.csaleid = b.csaleid");
		sql.append(" where ");
		sql.append("  isnull(h.dr,0)=0  and isnull(b.dr,0)=0  ");
		if (whereSql != null && whereSql.length() > 0) {
			sql.append(" and " + whereSql);
		}
		sql.append(" and h.creceiptcustomerid in(");
		sql.append(" select distinct tb_storcubasdoc.pk_cumandoc  ");
		sql.append(" from wds_storecust_h ");
		sql.append(" join tb_storcubasdoc ");
		sql.append(" on wds_storecust_h.pk_wds_storecust_h = tb_storcubasdoc.pk_wds_storecust_h ");
		sql.append(" where wds_storecust_h.pk_stordoc ='"+pk_storedoc+"'");
		sql.append("  and isnull(wds_storecust_h.dr,0)=0");
		sql.append("  and isnull(tb_storcubasdoc.dr,0)=0");
		sql.append(" )");
		Object o = getDao().executeQuery(sql.toString(),
				new BeanListProcessor(SoDealVO.class));
		if (o == null)
			return null;
		ArrayList<SoDealVO> list = (ArrayList<SoDealVO>) o;
		datas = list.toArray(new SoDealVO[0]);

		/**
		 * liuys add 
		 * 虚拟流程查询, 根据销售订单查询ERP销售出库,如果有对应的销售出库单并且该单标识为虚拟安排,则在销售计划安排只能查询出虚拟流程的单据
		 *  出库实出量总和(多个对应出库实出相加) - 销售订单已安排量 > 将安排的数量
		 * 
		 */ 
		String pk = null;
		for (int i = 0; i < datas.length; i++) {
			pk = PuPubVO.getString_TrimZeroLenAsNull(datas[i].getCorder_bid());
			if(pk == null){
				continue;
			}
			StringBuffer generalSql = new StringBuffer();
			generalSql.append(" select sum(ic_general_b.noutnum) from ic_general_h ");
			generalSql.append(" join ic_general_b on ");
			generalSql.append(" ic_general_h.cgeneralhid = ic_general_b.cgeneralhid");
			generalSql.append(" where isnull(ic_general_h.dr,0)=0 ");
			generalSql.append(" and ic_general_h.fbillflag='"+BillStatus.AUDIT+"' ");
			generalSql.append(" and isnull(ic_general_b.dr,0)=0 ");
			generalSql.append(" and ic_general_h."+WdsWlPubConst.WDS_IC_ZG_DEF+"='"+WdsWlPubConst.WDS_IC_FLAG_wu+"'");//虚拟出库
			generalSql.append(" and ic_general_b.csourcebillbid='"+pk + "'");
			UFDouble noutnum =PuPubVO.getUFDouble_NullAsZero( getDao().executeQuery(generalSql.toString(),
					WdsPubResulSetProcesser.COLUMNPROCESSOR));
			// 情况1 : 如果未查询到销售出库对应表体单据,则不是虚拟安排,按正常安排流程走
			if (noutnum.doubleValue() ==0)
				continue;
			//订单已安排量
			UFDouble ntaldcnum = PuPubVO.getUFDouble_NullAsZero( datas[i].getNtaldcnum());
			UFDouble nnum = PuPubVO.getUFDouble_NullAsZero(datas[i].getNnumber());
			UFDouble npacknum = PuPubVO.getUFDouble_NullAsZero(datas[i].getNpacknumber());
			UFDouble nhsl = new UFDouble(1);
			if(npacknum.doubleValue() >0){
				nhsl = nnum.div(npacknum);
			}
			//如果(出库实出量总和(多个对应出库实出相加) - 销售订单已安排量 > 将安排的数量,则加入list,可以查询出来
			if(noutnum.sub(ntaldcnum).doubleValue()>0){
				datas[i].setIsxnap(UFBoolean.TRUE);
				UFDouble nlefnum = noutnum.sub(ntaldcnum);
				datas[i].setNnumber(nlefnum);
				if(npacknum.doubleValue() >0){
					datas[i].setNpacknumber(nlefnum.div(nhsl));
				}
			}
		}
			
		return datas;
	}

	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目 将本次安排数量，回写到销售订单安排累计发运数量
	 * @时间：2011-3-25下午04:44:08
	 * @param dealnumInfor
	 * @throws BusinessException
	 */
	private void reWriteDealNumForPlan(Map<String, UFDouble> map)
			throws BusinessException {
		if (map == null || map.size() == 0)
			return;
		for (Entry<String, UFDouble> entry : map.entrySet()) {
			String sql = "update so_saleorder_b set "
					+ WdsWlPubConst.DM_SO_DEALNUM_FIELD_NAME
					+ " = coalesce("
					+ WdsWlPubConst.DM_SO_DEALNUM_FIELD_NAME
					+ ",0)+"
					+ PuPubVO.getUFDouble_NullAsZero(entry.getValue())
							.doubleValue() + " where corder_bid='"
					+ entry.getKey() + "'";
			if (getDao().executeUpdate(sql) == 0) {
				throw new BusinessException("数据异常：该发运计划可能已被删除，请重新查询数据");
			};
			// 将计划数量（nplannum）和累计安排数量(ndealnum)比较

			// 如果累计安排数量大于计划数量将抛出异常

			String sql1 = "select count(0) from so_saleorder_b where corder_bid='"
					+ entry.getKey()
					+ "'and (coalesce(nnumber,0)-coalesce("
					+ WdsWlPubConst.DM_SO_DEALNUM_FIELD_NAME + ",0))>=0";
			Object o = getDao().executeQuery(sql1,
					WdsPubResulSetProcesser.COLUMNPROCESSOR);
			if (o == null) {
				throw new BusinessException("超计划量安排");
			}
		}
	}

	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目
	 * @时间：2011-3-25下午03:58:14
	 * @param ldata
	 * @param infor
	 *            :登录人，登录公司，登录日期
	 * @throws Exception
	 */
	public void doDeal(List<SoDealVO> ldata, List<String> infor)
			throws Exception {
		if (ldata == null || ldata.size() == 0)
			return;
//		//1.将明细按照 是否大日期 分单，然后分别按照现存量来过滤
		SoDealBoUtils util= new SoDealBoUtils();
		CircularlyAccessibleValueObject[][] splitVos = SplitBillVOs.getSplitVOs(
				(CircularlyAccessibleValueObject[]) (ldata
						.toArray(new SoDealVO[0])),
				new String[]{"cbodywarehouseid","disdate"});//根据发货仓库和是否大日期分单
		if(splitVos == null || splitVos.length==0){
			return ;
		}
		SoDealVO[] vos = null;
		for(int i=0;i<splitVos.length;i++){
			vos = (SoDealVO[])splitVos[i];
			if(vos != null && vos.length>0){
				String pk_outwhouse= PuPubVO.getString_TrimZeroLenAsNull(vos[0].getCbodywarehouseid());
				if(pk_outwhouse  == null){
					throw  new BusinessException("发货仓库不能未空");
				}
				UFBoolean fisdate = PuPubVO.getUFBoolean_NullAs(vos[0].getDisdate(), UFBoolean.FALSE);
				if(fisdate.booleanValue()){
					util.initInvNumInfor(true,infor.get(1), pk_outwhouse, Arrays.asList(vos));
				}else{
					util.initInvNumInfor(false,infor.get(1), pk_outwhouse, Arrays.asList(vos));

				}
			}
		}
		//2.回写销售订单累计安排数量
		Map<String, UFDouble> map = new HashMap<String, UFDouble>();
		for (int i = 0; i < ldata.size(); i++) {
			String key = ldata.get(i).getCorder_bid();
			UFDouble num = PuPubVO.getUFDouble_NullAsZero(ldata.get(i)
					.getNnum());
			if (map.containsKey(key)) {
				UFDouble oldValue = PuPubVO
						.getUFDouble_NullAsZero(map.get(key));
				map.put(key, oldValue.add(num));
			}
			map.put(key, num);
		}
		reWriteDealNumForPlan(map);
		// 3.销售计划安排vo---》销售订单
		// 3.1按  发货站 客户 分单
		CircularlyAccessibleValueObject[][] datas = SplitBillVOs.getSplitVOs(
				(CircularlyAccessibleValueObject[]) (ldata
						.toArray(new SoDealVO[0])),
				WdsWlPubConst.SO_PLAN_DEAL_SPLIT_FIELDS);
		if (datas == null || datas.length == 0)
			return;
		int len = datas.length;
		SoDealVO[] tmpVOs = null;
		SoDealBillVO[] planBillVos = new SoDealBillVO[len];
		for (int i = 0; i < len; i++) {
			tmpVOs = (SoDealVO[]) datas[i];
			planBillVos[i] = new SoDealBillVO();
			planBillVos[i].setParentVO(getPlanHead(tmpVOs[0]));
			planBillVos[i].setChildrenVO(tmpVOs);
		}
		//3.2进行数据交换，生成销售运单
		PfParameterVO paraVo = new PfParameterVO();
		paraVo.m_operator = infor.get(0);
		paraVo.m_coId = infor.get(1);
		paraVo.m_currentDate = infor.get(2);
		AggregatedValueObject[] orderVos = (AggregatedValueObject[]) PfUtilTools
				.runChangeDataAry(WdsWlPubConst.WDS4, WdsWlPubConst.WDS5,
						planBillVos, paraVo);
		//3.3 调用销售运单保存脚本，保存销售运单
		if (orderVos == null || orderVos.length == 0) {
			return;
		}
		PfUtilBO pfbo = new PfUtilBO();
		for (AggregatedValueObject bill : orderVos) {
			pfbo.processAction(WdsWlPubConst.DM_PLAN_TO_ORDER_SAVE,
					WdsWlPubConst.WDS5, infor.get(2), null, bill, null);
		}
	}

	private SoDeHeaderVo getPlanHead(SoDealVO dealVo) {
		if (dealVo == null)
			return null;
		SoDeHeaderVo head = new SoDeHeaderVo();
		String[] names = head.getAttributeNames();
		for (String name : names) {
			head.setAttributeValue(name, dealVo.getAttributeValue(name));
		}
		return head;
	}
	//	

}
