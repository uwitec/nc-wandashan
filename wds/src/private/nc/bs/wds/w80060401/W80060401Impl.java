package nc.bs.wds.w80060401;

import java.util.*;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.itf.uif.pub.IUifService;
import nc.itf.wds.w80060401.Iw80060401;
import nc.jdbc.framework.JdbcSession;
import nc.jdbc.framework.PersistenceManager;
import nc.jdbc.framework.exception.DbException;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.ui.trade.business.HYPubBO_Client;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.wds.pub.WDSTools;
import nc.vo.wds.w80060401.TbShipentryBVO;
import nc.vo.wds.w80060401.TbShipentryVO;
import nc.vo.wds.w80060406.TbFydmxnewVO;

public class W80060401Impl implements Iw80060401 {

	// 数据库查询对象
	IUAPQueryBS iuap = (IUAPQueryBS) NCLocator.getInstance().lookup(
			IUAPQueryBS.class.getName());
	// 获取数据库访问对象
	IVOPersistence ivo = (IVOPersistence) NCLocator.getInstance().lookup(
			IVOPersistence.class.getName());

	public TbShipentryBVO[] queryShipentryBVO(String strWhere)
			throws BusinessException { 
		String sql = "select tb_invcl_bas.pk_invbasdoc from tb_invcl,tb_invcl_bas,bd_invbasdoc "
				+ " where tb_invcl.pk_invcl=tb_invcl_bas.pk_invcl "
				+ " and tb_invcl.dr=0 and tb_invcl_bas.dr=0 and bd_invbasdoc.dr = 0 and bd_invbasdoc.pk_invbasdoc = tb_invcl_bas.pk_invbasdoc "
				+ " order by tb_invcl.invcode,tb_invcl_bas.fr_order";

		ArrayList<?> results = null;
		TbShipentryBVO[] spvo = null;
		PersistenceManager sessioManager = null;

		try {
			sessioManager = PersistenceManager.getInstance();
			JdbcSession jdbcSession = sessioManager.getJdbcSession();
			results = (ArrayList<?>) jdbcSession.executeQuery(sql,
					new ArrayListProcessor());
		} catch (DbException e) {
			e.printStackTrace();
		}
		if (results != null && !results.isEmpty()) {
			spvo = new TbShipentryBVO[results.size()];
			for (int i = 0; i < results.size(); i++) {
				spvo[i] = new TbShipentryBVO();
				spvo[i]
						.setPk_invbasdoc(WDSTools
								.getString_TrimZeroLenAsNull(((Object[]) results
										.get(i))[0]));

			}
		}
		return spvo;

	}

	/**
	 * 判断录入类型后进行存储
	 * 
	 * @param billvo
	 *            聚合VO
	 * @param begindate
	 *            开始时间
	 * @param enddate
	 *            结束时间
	 * @param list
	 *            追加计划中应用的集合用来计算剩余数量
	 * @throws Exception
	 */
	public void insertShipertryVO(AggregatedValueObject billvo,
			String begindate, String enddate, ArrayList shvolist)
			throws Exception {
		// TODO Auto-generated method stub

		if (null != billvo) {
			TbShipentryVO shvo = (TbShipentryVO) billvo.getParentVO();
			TbShipentryBVO[] shbVO = (TbShipentryBVO[]) billvo.getChildrenVO();

			if (null != shvo) {
				// 判断主键是否为空，如果为空说明为新增，有值说明是修改
				if (null == shvo.getSe_pk() || "".equals(shvo.getSe_pk())) {
					// 判断计划类型，0 月计划 1 追加计划
					if (shvo.getSe_type() == 0) {
						// 判断子表数组对象是否为空
						if (null != shbVO && shbVO.length > 0) {
							for (int i = 0; i < shbVO.length; i++) {
								TbShipentryBVO shbvo = shbVO[i];
								// 判断计划数是否为空
								if (null != shbvo.getSeb_plannum()
										&& !"".equals(shbvo.getSeb_plannum())) {
									// 把计划数转换成剩余
									shbvo
											.setSeb_surplus(shbvo
													.getSeb_plannum());
									shbVO[i] = shbvo;
								}
							}
							billvo.setChildrenVO(shbVO);
							return;
						}
					} else { // 追加计划
						ArrayList shbvoList = this.plannumTosurplus(shvolist,
								shbVO, 0);
						// Update 月计划子表中剩余数量
						ivo.updateVOList(shbvoList);
						return;
					}
				} else { // 修改

					// 判断计划类型，0 月计划 1 追加计划
					if (shvo.getSe_type() == 0) {
						if (null != shbVO && shbVO.length > 0) {

							String sql = "select b.pk_invbasdoc, sum(seb_plannum) "
									+ "from tb_shipentry_b b, tb_shipentry t "
									+ "where b.dr = 0 and t.dr = 0 and b.se_pk = t.se_pk "
									+ " and t.doperatordate between '"
									+ begindate
									+ "' and '"
									+ enddate
									+ "' "
									+ " and t.srl_pk = '"
									+ shvo.getSrl_pk()
									+ "' "
									+ " and t.se_pk <> '"
									+ shvo.getSe_pk()
									+ "' "
									+ " group by b.pk_invbasdoc ";

							ArrayList<?> results = null;
							PersistenceManager sessioManager = null;
							TbShipentryBVO[] shibVO = null;
							// //////获取数据库访问对象，查询数据库、、、、、
							try {
								sessioManager = PersistenceManager
										.getInstance();
								JdbcSession jdbcSession = sessioManager
										.getJdbcSession();
								results = (ArrayList<?>) jdbcSession
										.executeQuery(sql,
												new ArrayListProcessor());
								if (results != null && !results.isEmpty()) {
									// 查询出数据库中所有本仓单品的计划数包含月计划和追加计划
									shibVO = new TbShipentryBVO[results.size()];
									for (int i = 0; i < results.size(); i++) {
										shibVO[i] = new TbShipentryBVO();
										shibVO[i]
												.setPk_invbasdoc(WDSTools
														.getString_TrimZeroLenAsNull(((Object[]) results
																.get(i))[0]));
										shibVO[i]
												.setSeb_plannum(WDSTools
														.getUFDouble_NullAsZero(((Object[]) results
																.get(i))[1]));
									}
									// update 主表
									// ivo.updateVO(shvo);
									billvo.setParentVO(shvo);
									for (int i = 0; i < shbVO.length; i++) {
										TbShipentryBVO shbvo = shbVO[i];
										if (null != shbvo) {

											// 判断当前页面中的计划数是否为空
											if (null != shbvo.getSeb_plannum()
													&& !"".equals(shbvo
															.getSeb_plannum())) {
												if (null != shibVO
														&& shibVO.length > 0) {
													for (int j = 0; j < shibVO.length; j++) {
														double sum = 0; // 总的计划数
														// 判断两个单品是否相等
														if (shbvo
																.getPk_invbasdoc()
																.equals(
																		shibVO[j]
																				.getPk_invbasdoc())) {
															// 判断数据库中的计划数是否为空
															if (null != shibVO[j]
																	.getSeb_plannum()
																	&& !""
																			.equals(shibVO[j]
																					.getSeb_plannum())) {
																// 不为空把数据库中的计划数和当前的计划数相加，算出总的计划数
																sum = shibVO[j]
																		.getSeb_plannum()
																		.toDouble()
																		+ shbvo
																				.getSeb_plannum()
																				.toDouble();
															} else {
																// 如果数据库中的计划数为空
																// 把当前的计划数就是总的计划数
																sum = shbvo
																		.getSeb_plannum()
																		.toDouble();
															}
															// 判断月计划中的已发运数是否为空
															if (null != shbvo
																	.getSeb_shipped()
																	&& !""
																			.equals(shbvo
																					.getSeb_shipped())) {
																// 用总的计划数减去已发运的数量
																// 等于
																// 剩余的数量
																sum = sum
																		- shbvo
																				.getSeb_shipped()
																				.toDouble();
															}
															// 给剩余数量赋值
															shbvo
																	.setSeb_surplus(new UFDouble(
																			sum));

															// update 子表
															// ivo.updateVO(shbvo);
															shbVO[i] = shbvo;
															break;
														}
													}
												}
											}
										}
										billvo.setChildrenVO(shbVO);
									}
								} else {// /////数据库中没有记录
									// 判断子表数组对象是否为空
									// update 主表
									// ivo.updateVO(shvo);
									billvo.setParentVO(shvo);
									if (null != shbVO && shbVO.length > 0) {
										for (int i = 0; i < shbVO.length; i++) {
											TbShipentryBVO shbvo = shbVO[i];
											// 判断计划数是否为空
											if (null != shbvo.getSeb_plannum()
													&& !"".equals(shbvo
															.getSeb_plannum())) {
												// 把计划数转换成剩余
												shbvo.setSeb_surplus(shbvo
														.getSeb_plannum());
											}
										}
										// Update子表
										// ivo.updateVOArray(shbVO);
										billvo.setChildrenVO(shbVO);
									}
								}
							} catch (DbException e) {
								e.printStackTrace();
							}

						}
					} else { // 追加计划
						ArrayList shbvoList = this.plannumTosurplus(shvolist,
								shbVO, 1);
						// Update 月计划子表中剩余数量
						ivo.updateVOList(shbvoList);
						// // update 主表
						// ivo.updateVO(shvo);
						// // update 子表
						// ivo.updateVOArray(shbVO);

					}
				}
			}
		}
	}

	// 计划数转换剩余数量
	private ArrayList<TbShipentryBVO> plannumTosurplus(ArrayList shvolist,
			TbShipentryBVO[] shbVO, int type) throws BusinessException {
		ArrayList<TbShipentryBVO> shbvoList = new ArrayList<TbShipentryBVO>();
		if (null != shvolist && shvolist.size() > 0) {

			TbShipentryVO shipvo = (TbShipentryVO) shvolist.get(0);
			String sWhere = " dr = 0 and se_pk ='" + shipvo.getSe_pk() + "'";
			ArrayList list = (ArrayList) iuap.retrieveByClause(
					TbShipentryBVO.class, sWhere);
			if (null != list && list.size() > 0) {

				// 循环月计划中的子表信息
				for (int i = 0; i < list.size(); i++) {
					// 获取单个对象
					TbShipentryBVO shbvo = (TbShipentryBVO) list.get(i);
					// 判断月计划中对象
					if (null != shbvo) {
						// 判读当前子表信息
						if (null != shbVO && shbVO.length > 0) {
							// 循环当前界面中的子表信息
							for (int j = 0; j < shbVO.length; j++) {
								if (null != shbVO[j].getSeb_plannum()
										&& !""
												.equals(shbVO[j]
														.getSeb_plannum())) {
									// 判断如果当前页面中的单品与月计划中的单品信息一致，修改月计划中的剩余数量，用月计划中的剩余加上当前单品的计划数
									if (shbvo.getPk_invbasdoc().equals(
											shbVO[j].getPk_invbasdoc())) {
										if (type == 0) {
											// 判断月计划中的剩余数是否为空
											if (null != shbvo.getSeb_surplus()
													&& !"".equals(shbvo
															.getSeb_surplus())) {
												double sum = shbvo
														.getSeb_surplus()
														.toDouble()
														+ shbVO[j]
																.getSeb_plannum()
																.toDouble();
												shbvo
														.setSeb_surplus(new UFDouble(
																sum));
											} else {
												// 如果月计划中没有计划数的单品，直接把计划数赋给月计划中的剩余
												shbvo.setSeb_surplus(shbVO[j]
														.getSeb_plannum());
											}
											shbvoList.add(shbvo);
											break;
										} else if (type == 1) {
											double sum = 0;
											if (null != shbvo.getSeb_plannum()
													&& !"".equals(shbvo
															.getSeb_plannum())) {
												sum = shbVO[j].getSeb_plannum()
														.toDouble()
														+ shbvo
																.getSeb_plannum()
																.toDouble();
											} else {
												sum = shbVO[j].getSeb_plannum()
														.toDouble();
											}
											// 判断月计划中的已发运数是否为空
											if (null != shbvo.getSeb_shipped()
													&& !"".equals(shbvo
															.getSeb_shipped())) {
												// 用总的计划数减去已发运的数量 等于 剩余的数量
												sum = sum
														- shbvo
																.getSeb_shipped()
																.toDouble();
											}
											// 给剩余数量赋值
											shbvo.setSeb_surplus(new UFDouble(
													sum));
											shbvoList.add(shbvo);
											break;
										}
									}
								}
							}
						}
					}
				}

			}
		}
		return shbvoList;
	}

	public AggregatedValueObject deleteBD80060401(AggregatedValueObject billVO,
			Object userObj) throws Exception {
		// TODO Auto-generated method stub
		boolean isupdate = Boolean.parseBoolean(((List) userObj).get(2)
				.toString());
		if (isupdate) {
			List list = ((List) ((List) userObj).get(1));
			if (null != list && list.size() > 0) {
				ivo.updateVOList(list);
			}
		}
		nc.itf.uif.pub.IUifService service = (IUifService) NCLocator
				.getInstance().lookup(IUifService.class.getName());
		return service.deleteBD(billVO, ((List) userObj).get(0));
		// return HYPubBO_Client.deleteBD(billVO, ((List) userObj).get(0));
	}

	public AggregatedValueObject saveBD80060401(AggregatedValueObject billVO,
			Object userObj) throws Exception {
		// TODO Auto-generated method stub
		this.insertShipertryVO(billVO, ((List) userObj).get(1).toString(),
				((List) userObj).get(2).toString(),
				((ArrayList) ((List) userObj).get(3)));
		nc.itf.uif.pub.IUifService service = (IUifService) NCLocator
				.getInstance().lookup(IUifService.class.getName());

		AggregatedValueObject bvo = service.saveBD(billVO, ((List) userObj)
				.get(0));
		// HYPubBO_Client.saveBD(billVO, ((List) userObj).get(0));
		return bvo;
	}

}
