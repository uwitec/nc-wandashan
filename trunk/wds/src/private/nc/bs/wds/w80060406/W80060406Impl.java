package nc.bs.wds.w80060406;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.itf.wds.w80060406.Iw80060406;
import nc.jdbc.framework.JdbcSession;
import nc.jdbc.framework.PersistenceManager;
import nc.jdbc.framework.exception.DbException;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.wds.pub.WDSTools;
import nc.vo.wds.w8004040204.TbOutgeneralHVO;
import nc.vo.wds.w80060401.TbShipentryBVO;
import nc.vo.wds.w80060401.TbShipentryVO;
import nc.vo.wds.w80060406.TbFydmxnewVO;
import nc.vo.wds.w80060406.TbFydnewVO;

public class W80060406Impl implements Iw80060406 {

	// 数据库访问
	ArrayList<?> results = null;

	PersistenceManager sessioManager = null;

	// public void changeTbOutGeneral(AggregatedValueObject billvo)
	// throws BusinessException {
	// TbFydnewVO fydvo = (TbFydnewVO) billvo.getParentVO();
	// if (null != fydvo) {
	// String sql = "update tb_outgeneral_h set vbillstatus = '1' where dr = 0
	// and csourcebillhid = '"
	// + fydvo.getFyd_pk() + "'";
	// try {
	// sessioManager = PersistenceManager.getInstance();
	// JdbcSession jdbcSession = sessioManager.getJdbcSession();
	// jdbcSession.executeUpdate(sql);
	// } catch (DbException e) {
	// e.printStackTrace();
	// }
	//
	// }
	//
	// }

	/**
	 * 计划拆分中的查询明细按钮方法 根据条件查询出所有的值 返回拆除明细数组
	 * 
	 * @throws ParseException
	 */
	public TbFydmxnewVO[] queryFydmxnewVO(String strWhere, String stock,
			String begindate, String enddate, String stockr)
			throws BusinessException, ParseException {
		// TODO Auto-generated method stub
		TbFydmxnewVO[] spvo = null;
		// 现在的查询条件中是把所有的符合条件的都查询出来了 包括计划数为空的
		String sql = "select t.seb_pk ,t.se_pk,t.pk_invbasdoc,t.seb_plannum,b.se_type from tb_shipentry b,tb_shipentry_b t,bd_invbasdoc i  where b.dr = 0 and t.dr = 0 and b.se_pk = t.se_pk and i.pk_invbasdoc = t.pk_invbasdoc  "
				+ strWhere;

		List<TbFydmxnewVO> fydmxMonthList = new ArrayList<TbFydmxnewVO>();
		List<TbFydmxnewVO> fydmxList = new ArrayList<TbFydmxnewVO>();
		List<TbFydmxnewVO> tempList;
		try {
			sessioManager = PersistenceManager.getInstance();
			JdbcSession jdbcSession = sessioManager.getJdbcSession();
			results = null;
			// 查询
			results = (ArrayList<?>) jdbcSession.executeQuery(sql,
					new ArrayListProcessor());
		} catch (DbException e) {
			e.printStackTrace();
		}
		// 判断是否为空
		if (results != null && !results.isEmpty()) {
			// 主键值
			String sebpk = "";

			// 循环赋值
			for (int i = 0; i < results.size(); i++) {
				TbFydmxnewVO fydmx = new TbFydmxnewVO();
				boolean type = false; // 判断是月计划还是追加计划
				// 判断是否为月计划
				if (WDSTools.getString_TrimZeroLenAsNull(
						((Object[]) results.get(i))[4]).toString().equals("0")) {
					fydmx.setSeb_pk(WDSTools
							.getString_TrimZeroLenAsNull(((Object[]) results
									.get(i))[0]));
					type = true;
				} else {
					fydmx.setSeb_pk(WDSTools
							.getString_TrimZeroLenAsNull(((Object[]) results
									.get(i))[0]));
				}
				fydmx
						.setCfd_dw(WDSTools
								.getString_TrimZeroLenAsNull(((Object[]) results
										.get(i))[1]));
				fydmx
						.setPk_invbasdoc(WDSTools
								.getString_TrimZeroLenAsNull(((Object[]) results
										.get(i))[2]));
				fydmx
						.setCfd_plannum(WDSTools
								.getUFDouble_NullAsZero(((Object[]) results
										.get(i))[3]));
				if (type) {
					fydmxMonthList.add(fydmx); // 月计划集合中添加值
				} else
					fydmxList.add(fydmx); // 追加计划集合中添加值
			}
			List<TbFydmxnewVO> fydmxVOList = new ArrayList<TbFydmxnewVO>();
			// 判断月计划集合是否为空
			if (fydmxMonthList.size() > 0) {
				// 循环月计划集合
				for (int i = 0; i < fydmxMonthList.size(); i++) {
					TbFydmxnewVO fydmx = fydmxMonthList.get(i); // 获取对象
					// 判断追加集合是否为空
					if (null != fydmxList && fydmxList.size() > 0) {
						// 循环追加计划集合
						for (int j = 0; j < fydmxList.size(); j++) {
							TbFydmxnewVO fydmxvo = fydmxList.get(j); // 获取对象
							// 判断俩个对象中的商品名称是否一样
							if (fydmx.getPk_invbasdoc().equals(
									fydmxvo.getPk_invbasdoc())) {
								// 判断追加集合中的计划数是否为空
								if (null != fydmxvo.getCfd_plannum()
										&& !"".equals(fydmxvo.getCfd_plannum())) {
									// 判断月计划集合中的计划数是否为空
									if (null != fydmx.getCfd_plannum()
											&& !"".equals(fydmx
													.getCfd_plannum())) {
										// 如果两者计划数都不为空 进行相加 获得总的计划数和
										fydmx.setCfd_plannum(new UFDouble(fydmx
												.getCfd_plannum().toDouble()
												+ fydmxvo.getCfd_plannum()
														.toDouble()));
									} else { // 如果月计划中计划数为空
										// 把追加计划集合中的计划数赋给月计划中的计划数
										fydmx.setCfd_plannum(new UFDouble(
												fydmxvo.getCfd_plannum()
														.toDouble()));
									}
									// fydmxList.remove(j);
									// j--;
								}
							}
						}
					}
					fydmxVOList.add(fydmx);
				}
				if (fydmxVOList.size() > 0) {
					// 循环移除计划数为空的项
					for (int i = 0; i < fydmxVOList.size(); i++) {
						TbFydmxnewVO fydmx = fydmxVOList.get(i);
						if (null == fydmx.getCfd_plannum()
								|| "".equals(fydmx.getCfd_plannum())
								|| fydmx.getCfd_plannum().toDouble() == 0) {
							fydmxVOList.remove(i);
							i--;
						}
					}
					// //////////以下方法 查询出到货站和发货站的销售待发运量和已发运量///////////////////
					tempList = new ArrayList<TbFydmxnewVO>();
					for (int i = 0; i < fydmxVOList.size(); i++) {

						TbFydmxnewVO fydmx = fydmxVOList.get(i);
						fydmx.setCrowno(i + 1 + "0"); //行号
						// ///////////////////////////////////////////
						// 发货站库存数量
						sql = "select "
								+ "  sum(case"
								+ "  when ((to_date((to_char(sysdate, 'yyyy-MM-dd')), 'yyyy-MM-dd')) -"
								+ "  (to_date(substr(tb_warehousestock.whs_batchcode, 0, 8),"
								+ "  'yyyy-MM-dd'))) <= bd_invbasdoc.def16 then"
								+ "  (tb_warehousestock.whs_stockpieces)"
								+ "  end) ,"
								+ "  sum(case"
								+ "  when ((to_date((to_char(sysdate, 'yyyy-MM-dd')), 'yyyy-MM-dd')) -"
								+ "  (to_date(substr(tb_warehousestock.whs_batchcode, 0, 8),"
								+ "  'yyyy-MM-dd'))) <= bd_invbasdoc.def15 then"
								+ "  (tb_warehousestock.whs_stockpieces)"
								+ "   end) "
								+ "   from bd_stordoc, bd_cargdoc, tb_warehousestock, bd_invbasdoc"
								+ "  where bd_stordoc.pk_stordoc = bd_cargdoc.pk_stordoc"
								+ "  and tb_warehousestock.pk_cargdoc = bd_cargdoc.pk_cargdoc"
								+ "  and bd_invbasdoc.pk_invbasdoc = tb_warehousestock.pk_invbasdoc"
								+ "  and bd_stordoc.dr = 0"
								+ "  and bd_cargdoc.dr = 0"
								+ "  and tb_warehousestock.dr = 0"
								+ "  and bd_invbasdoc.dr = 0 and bd_invbasdoc.pk_invbasdoc='"
								+ fydmx.getPk_invbasdoc() + "' "
								+ "	 and bd_stordoc.pk_stordoc = '" + stockr
								+ "' ";
						this.getStockSum(sql, fydmx, false);
						// 到货站库存数量
						sql = "select sum(case"
								+ "  when ((to_date((to_char(sysdate, 'yyyy-MM-dd')), 'yyyy-MM-dd')) -"
								+ "  (to_date(substr(tb_warehousestock.whs_batchcode, 0, 8),"
								+ "  'yyyy-MM-dd'))) <= bd_invbasdoc.def19 then"
								+ "  (tb_warehousestock.whs_stockpieces)"
								+ "  end) "
								+ "   from bd_stordoc, bd_cargdoc, tb_warehousestock, bd_invbasdoc"
								+ "  where bd_stordoc.pk_stordoc = bd_cargdoc.pk_stordoc"
								+ "  and tb_warehousestock.pk_cargdoc = bd_cargdoc.pk_cargdoc"
								+ "  and bd_invbasdoc.pk_invbasdoc = tb_warehousestock.pk_invbasdoc"
								+ "  and bd_stordoc.dr = 0"
								+ "  and bd_cargdoc.dr = 0"
								+ "  and tb_warehousestock.dr = 0"
								+ "  and bd_invbasdoc.dr = 0 and bd_invbasdoc.pk_invbasdoc='"
								+ fydmx.getPk_invbasdoc() + "' "
								+ "	 and bd_stordoc.pk_stordoc = '" + stock
								+ "' ";
						this.getStockSum(sql, fydmx, true);
						// //////////已发和待发////////////////////////////
						sql = "select sum(case"
								+ " when tb_fydnew.fyd_fyzt = 1 then"
								+ " tb_fydmxnew.cfd_sffsl"
								+ " end ) ,"
								+ " sum(case"
								+ "  when tb_fydnew.fyd_fyzt = 0 then"
								+ "   tb_fydmxnew.cfd_xs"
								+ " end )"
								+ " from  tb_fydnew "
								+ " inner join tb_fydmxnew on tb_fydnew.fyd_pk = tb_fydmxnew.fyd_pk"
								+ " inner join bd_invbasdoc on tb_fydmxnew.pk_invbasdoc ="
								+ "  bd_invbasdoc.pk_invbasdoc"
								+ " where " // (tb_fydnew.iprintcount > 0)"+ "
								// and
								+ " (tb_fydnew.dmakedate >= '" + begindate
								+ "')" + "  and (tb_fydnew.dmakedate <= '"
								+ enddate + "')"
								+ "  and (tb_fydmxnew.pk_invbasdoc = '"
								+ fydmx.getPk_invbasdoc() + "')"
								+ "  and (tb_fydmxnew.dr = 0)";

						// 到货站
						strWhere = "  and (tb_fydnew.srl_pk = '" + stock + "')";
						String tempsql = sql + strWhere;
						this.getStockNum(tempsql, fydmx, true);
						// 发货站
						strWhere = "  and (tb_fydnew.srl_pk = '" + stockr
								+ "')";
						tempsql = "";
						tempsql = sql + strWhere;
						this.getStockNum(tempsql, fydmx, false);
						tempList.add(fydmx);
					}
					spvo = new TbFydmxnewVO[tempList.size()];
					spvo = tempList.toArray(spvo);
				}

				/*
				 * //////////////////////以下方法是为了求出到货站的上个月的单品出库值//////////////////////////////////////
				 * if (fydmxVOList.size() > 0) { SimpleDateFormat format = new
				 * SimpleDateFormat("yyyy-MM-dd"); Date begin =
				 * format.parse(begindate); format = new SimpleDateFormat("MM");
				 * int beginmonth = Integer.parseInt(format.format(begin)); //
				 * 开始月 format = new SimpleDateFormat("yyyy"); int beginyear =
				 * Integer.parseInt(format.format(begin)); // 开始年 // 结束时间 String
				 * edate = beginyear + "-" + beginmonth + "-20"; beginmonth =
				 * beginmonth - 1; // 拼装开始时间 if (beginmonth == 0) { beginmonth =
				 * 12; beginyear = beginyear - 1; } String bdate = beginyear +
				 * "-" + beginmonth + "-21"; // 转换 format = new
				 * SimpleDateFormat("yyyy-MM-dd"); bdate =
				 * format.format(format.parse(bdate)); edate =
				 * format.format(format.parse(edate)); sql = "select
				 * sum(ic_general_b.noutassistnum),ic_general_b.cinvbasid " +
				 * "from ic_general_h,ic_general_b " + " where
				 * ic_general_h.cgeneralhid = ic_general_b.cgeneralhid " + " and
				 * fbillflag = 3 " + "and cbilltypecode = '4C' " + "and
				 * cwarehouseid = '" + stock + "' " + " and dbilldate between '" +
				 * bdate + "' and '" + edate + "' " + " group by
				 * ic_general_b.cinvbasid";
				 * 
				 * try { sessioManager = PersistenceManager.getInstance();
				 * JdbcSession jdbcSession = sessioManager .getJdbcSession(); //
				 * 根据仓库 时间区间 出库类别 是否出库完毕，查询出分组后每个单品的和 results = (ArrayList<?>)
				 * jdbcSession.executeQuery(sql, new ArrayListProcessor()); }
				 * catch (DbException e) { e.printStackTrace(); } // 判断结果集是否为空
				 * if (null != results && !results.isEmpty()) { // 循环 for (int i =
				 * 0; i < results.size(); i++) { // 获取单品主键 String pkinv =
				 * WDSTools .getString_TrimZeroLenAsNull(((Object[]) results
				 * .get(i))[1]); // 判断是否为空 if (null != pkinv &&
				 * !"".equals(pkinv)) { // 循环计划子表的VO for (int j = 0; j <
				 * fydmxVOList.size(); j++) { // 获取对象 TbFydmxnewVO fydmx =
				 * fydmxVOList.get(j); // 判断对象中的单品主键是否为空 if (null !=
				 * fydmx.getPk_invbasdoc() && !"".equals(fydmx
				 * .getPk_invbasdoc())) { // 判断两个单品是否相等 if (pkinv.equals(fydmx
				 * .getPk_invbasdoc())) { // 如果单品一样 把查询出来的销售和 赋给计划子VO fydmx
				 * .setCfd_maxstock(WDSTools .getUFDouble_NullAsZero(((Object[])
				 * results .get(i))[0])); } } } } } } }
				 */// ////////////////////////////////////////////////////////////////////
			}
		}
		return spvo;
	}

	/**
	 * 根据仓库查询库存数量
	 * 
	 * @param sql
	 *            SQL语句
	 * @param fydmx
	 *            传来的对象进行赋值
	 * @param type
	 *            类型 true 为到货站 false为发货站
	 */
	private void getStockSum(String sql, TbFydmxnewVO fydmx, boolean type) {
		try {
			sessioManager = PersistenceManager.getInstance();
			JdbcSession jdbcSession = sessioManager.getJdbcSession();
			results = null;
			// 查询
			results = (ArrayList<?>) jdbcSession.executeQuery(sql,
					new ArrayListProcessor());
		} catch (DbException e) {
			e.printStackTrace();
		}
		// 判断是否为空
		if (results != null && !results.isEmpty()) {
			if (type) {
				// 到货站库存数量
				fydmx
						.setCfd_tostock(WDSTools
								.getUFDouble_NullAsZero(((Object[]) results
										.get(0))[0]));
			} else {
				// 发货站库存数量1
				fydmx
						.setCfd_outstock1(WDSTools
								.getUFDouble_NullAsZero(((Object[]) results
										.get(0))[0]));
				// 发货站库存数量2
				fydmx
						.setCfd_outstock2(WDSTools
								.getUFDouble_NullAsZero(((Object[]) results
										.get(0))[1]));
			}
		}

	}

	/**
	 * 根据条件查询出到货站或者发货站的待发运和已发运数量
	 * 
	 * @param sql
	 *            SQL语句
	 * @param fydmx
	 *            传来的对象进行赋值
	 * @param type
	 *            类型 true 为到货站 false为发货站
	 */
	private void getStockNum(String sql, TbFydmxnewVO fydmx, boolean type) {
		try {
			sessioManager = PersistenceManager.getInstance();
			JdbcSession jdbcSession = sessioManager.getJdbcSession();
			results = null;
			// 查询
			results = (ArrayList<?>) jdbcSession.executeQuery(sql,
					new ArrayListProcessor());
		} catch (DbException e) {
			e.printStackTrace();
		}
		// 判断是否为空
		if (results != null && !results.isEmpty()) {
			if (type) {
				// 到货站已发运量
				fydmx
						.setCfd_toshipped(WDSTools
								.getUFDouble_NullAsZero(((Object[]) results
										.get(0))[0]));
				// 到货站待发运量
				fydmx
						.setCfd_saletravel(WDSTools
								.getUFDouble_NullAsZero(((Object[]) results
										.get(0))[1]));
			} else {
				// 发货站已发运量
				fydmx
						.setCfd_outshipped(WDSTools
								.getUFDouble_NullAsZero(((Object[]) results
										.get(0))[0]));
				// 发货站待发运量
				fydmx
						.setCfd_outtravel(WDSTools
								.getUFDouble_NullAsZero(((Object[]) results
										.get(0))[1]));
			}
		}
	}

	// /**
	// * 对待发运,已发运,剩于,进行操作变更数据
	// * @param mybillVO 发运单聚合VO
	// * @return 是否成功
	// * @throws BusinessException
	// */
	// public boolean operFydNumData(AggregatedValueObject mybillVO)
	// throws BusinessException {
	// if (null != mybillVO.getParentVO() && null != mybillVO.getChildrenVO()
	// && mybillVO.getChildrenVO().length > 0) {
	//
	// IUAPQueryBS iuap = (IUAPQueryBS) NCLocator.getInstance().lookup(
	// IUAPQueryBS.class.getName());
	//
	// IVOPersistence ivo = (IVOPersistence) NCLocator.getInstance()
	// .lookup(IVOPersistence.class.getName());
	//
	// TbFydnewVO fydvo = (TbFydnewVO) mybillVO.getParentVO();
	//
	// TbFydmxnewVO[] fydmxvo = (TbFydmxnewVO[]) mybillVO.getChildrenVO();
	// //到货站，开始时间和结束时间
	// String strWhere = " dr = 0 and type = 0 and srl_pk = '"
	// + fydvo.getSrl_pkr() + "' and doperatordate between '"
	// + fydvo.getFyd_begints() + "' and '" + fydvo.getFyd_endts()
	// + "'";
	//
	// ArrayList list = (ArrayList) iuap.retrieveByClause(
	// TbShipentryVO.class, strWhere);
	//			
	// List<TbShipentryBVO> shipentryList = new ArrayList<TbShipentryBVO>();
	//			
	// if(null!=list&&list.size()>0){
	//				
	// TbShipentryVO shevo = (TbShipentryVO) list.get(0);
	//				
	// if(null==shevo)
	// return false;
	//				
	// strWhere = " dr = 0 and se_pk = '"+shevo.getSe_pk()+"'";
	// list = null;
	// list = (ArrayList) iuap.retrieveByClause(
	// TbShipentryBVO.class, strWhere);
	//				
	// if(null!=list&&list.size()>0){
	//					
	// TbShipentryBVO[] she_bvo = new TbShipentryBVO[list.size()];
	// she_bvo = (TbShipentryBVO[]) list.toArray(she_bvo);
	//					
	// if(null!=she_bvo&&she_bvo.length>0){
	// for (int i = 0; i < she_bvo.length; i++) {
	// TbShipentryBVO shipentry = she_bvo[i];
	// for (int j = 0; j < fydmxvo.length; j++) {
	// TbFydmxnewVO fydmx = fydmxvo[j];
	//								
	// if(fydmx.getPk_invbasdoc().equals(shipentry.getPk_invbasdoc())){
	// //发运单实发辅数量
	// double s_num = 0;
	// if(null!=fydmx.getCfd_sffsl())
	// s_num= fydmx.getCfd_sffsl().doubleValue();
	// else
	// break;
	// //设置已发运,如果原有数据进行累加
	// if(null!=shipentry.getSeb_shipped())
	// shipentry.setSeb_shipped(new
	// UFDouble(shipentry.getSeb_shipped().doubleValue()+s_num));
	// else
	// shipentry.setSeb_shipped(new UFDouble(s_num));
	// //设置待发运,如果原有数据进行相减
	// if(null!=shipentry.getSeb_travel())
	// shipentry.setSeb_travel(new
	// UFDouble(shipentry.getSeb_travel().doubleValue()-s_num));
	// else
	// shipentry.setSeb_travel(new UFDouble(s_num-(s_num+s_num))); //算出负数
	// //设置剩于,如果原有数据进行相减
	// if(null!=shipentry.getSeb_surplus())
	// shipentry.setSeb_surplus(new
	// UFDouble(shipentry.getSeb_surplus().doubleValue()-s_num));
	// else
	// shipentry.setSeb_surplus(new UFDouble(s_num-(s_num+s_num)));//算出负数
	// //添加集合,后面更新
	// shipentryList.add(shipentry);
	// }
	// }
	// //执行更新
	// ivo.updateVOList(shipentryList);
	// return true;
	// }
	// }
	// }
	// }
	// }
	// return false;
	// }

}
