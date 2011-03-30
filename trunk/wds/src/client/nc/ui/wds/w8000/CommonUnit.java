package nc.ui.wds.w8000;

import java.util.ArrayList;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.itf.uap.sfapp.IBillcodeRuleService;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.billcodemanage.BillCodeObjValueVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.lang.UFTime;
import nc.vo.wds.w8004040204.TbOutgeneralBVO;
import nc.vo.wds.w8004040204.TbOutgeneralHVO;
import nc.vo.wds.w8004040204.TbOutgeneralTVO;
import nc.vo.wds.w8004040204.TbWarehousestockVO;
import nc.vo.wds.w8004061002.BdCargdocTrayVO;
import nc.vo.wds.w80060406.TbFydmxnewVO;
import nc.vo.wds.w80060608.IcGeneralBVO;
import nc.vo.wds.w80060608.IcGeneralHVO;

public class CommonUnit {
	static IUAPQueryBS iuap = (IUAPQueryBS) NCLocator.getInstance().lookup(
			IUAPQueryBS.class.getName());
	static IVOPersistence ivo = (IVOPersistence) NCLocator.getInstance()
			.lookup(IVOPersistence.class.getName());

	/**
	 * 保管员查询本地出库表，进行过滤，只显示有本货位单品的数据
	 * 
	 * @param pkList
	 *            单品集合
	 * @param strWhere
	 *            连接sql语句条件
	 * @return 转换后的supervo
	 */
	public static SuperVO[] queryTbOutGeneral(List pkList, StringBuffer strWhere) {
		SuperVO[] queryVos = null;
		if (null != pkList && pkList.size() > 0) {
			StringBuffer tmpWhere = new StringBuffer();
			for (int i = 0; i < pkList.size(); i++) {
				if (tmpWhere.length() <= 0) {
					tmpWhere.append("'" + pkList.get(i) + "'");
					continue;
				}
				tmpWhere.append(",'").append(pkList.get(i)).append("'");
			}
			StringBuffer sql = new StringBuffer(
					"select distinct tb_outgeneral_h.general_pk,tb_outgeneral_h.vbillcode,tb_outgeneral_h.dbilldate,"
							+ "tb_outgeneral_h.vbilltype,tb_outgeneral_h.vbillstatus,tb_outgeneral_h.coperatorid,tb_outgeneral_h.tmaketime,"
							+ "tb_outgeneral_h.csourcebillhid,tb_outgeneral_h.vsourcebillcode,tb_outgeneral_h.srl_pkr,tb_outgeneral_h.srl_pk,"
							+ "tb_outgeneral_h.pk_calbody,tb_outgeneral_h.cbiztype,tb_outgeneral_h.cdispatcherid,"
							+ "tb_outgeneral_h.cwhsmanagerid,tb_outgeneral_h.cdptid,"
							+ "tb_outgeneral_h.cbizid,tb_outgeneral_h.ccustomerid,tb_outgeneral_h.pk_cubasdocc,"
							+ "tb_outgeneral_h.vdiliveraddress,tb_outgeneral_h.vnote,tb_outgeneral_h.cregister,"
							+ "tb_outgeneral_h.taccounttime,tb_outgeneral_h.cauditorid,tb_outgeneral_h.dauditdate,"
							+ "tb_outgeneral_h.clastmodiid,"
							+ "tb_outgeneral_h.tlastmoditime,tb_outgeneral_h.iprintcount,tb_outgeneral_h.comp,"
							+ "tb_outgeneral_h.qianzidate,tb_outgeneral_h.ts,tb_outgeneral_h.dr,tb_outgeneral_h.state "
							+ "from tb_outgeneral_h,tb_outgeneral_b b "
							+ "where")
					.append(strWhere)
					.append(
							" and tb_outgeneral_h.general_pk = b.general_pk and b.dr = 0 and tb_outgeneral_h.dr = 0"
									+ " and b.cinventoryid in (").append(
							tmpWhere).append(")");
			ArrayList list = new ArrayList();
			try {
				list = (ArrayList) iuap.executeQuery(sql.toString(),
						new ArrayListProcessor());
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (list.size() > 0) {
				queryVos = new SuperVO[list.size()];
				// 把获取出来的值转换成数组
				// WDSTools.getString_TrimZeroLenAsNull(tmpObj[15])
				for (int i = 0; i < list.size(); i++) {
					Object[] tmpObj = (Object[]) list.get(i);
					queryVos[i] = CommonUnit.getOutGenerlHVO(tmpObj);
				}
			}
		}
		return queryVos;
	}

	/**
	 * 根据单品主键和来源表体主键进行查询本地出库表是否存在该数据并做过出库
	 * 
	 * @param invbasdoc
	 *            单品主键
	 * @param sourceb_Pk
	 *            来源单据表体主键
	 * @return 查询后的结果集
	 * @throws BusinessException
	 */
	public static List getOutGeneralBVO(String invbasdoc, String sourceb_Pk)
			throws BusinessException {
		// /根据子表中 主键，单品主键，操作=‘Y’
		// 如果有该保管员做过出库，不显示该数据
		String strWhere = " dr = 0 and isoper = 'Y' and cinventoryid = '"
				+ invbasdoc + "' and csourcebillbid = '" + sourceb_Pk + "'";
		return (ArrayList) iuap.retrieveByClause(TbOutgeneralBVO.class,
				strWhere);
	}

	public static TbOutgeneralHVO getOutGenerlHVO(Object[] tmpObj) {
		TbOutgeneralHVO generalh = new TbOutgeneralHVO();
		generalh.setGeneral_pk(WDSTools.getString_TrimZeroLenAsNull(tmpObj[0])); // 主键
		generalh.setVbillcode(WDSTools.getString_TrimZeroLenAsNull(tmpObj[1]));// 单据号
		generalh.setDbilldate(CommonUnit.getUFDate_Null(tmpObj[2]));// 单据日期
		generalh.setVbilltype(WDSTools.getString_TrimZeroLenAsNull(tmpObj[3]));// 单据类型
		generalh.setVbillstatus(CommonUnit.getInteger_Null(tmpObj[4]));// 单据状态
		if (null != tmpObj[5])
			generalh.setCoperatorid(WDSTools
					.getString_TrimZeroLenAsNull(tmpObj[5].toString().trim()));// 制单人
		generalh.setTmaketime(WDSTools.getString_TrimZeroLenAsNull(tmpObj[6]));// 制单时间
		generalh.setCsourcebillhid(WDSTools
				.getString_TrimZeroLenAsNull(tmpObj[7]));// 来源单据表头
		generalh.setVsourcebillcode(WDSTools
				.getString_TrimZeroLenAsNull(tmpObj[8]));// 来源单据号
		generalh.setSrl_pkr(WDSTools.getString_TrimZeroLenAsNull(tmpObj[9]));// 入库仓库主键
		generalh.setSrl_pk(WDSTools.getString_TrimZeroLenAsNull(tmpObj[10]));// 仓库主键
		generalh
				.setPk_calbody(WDSTools.getString_TrimZeroLenAsNull(tmpObj[11]));// 库存组织主键
		generalh.setCbiztype(WDSTools.getString_TrimZeroLenAsNull(tmpObj[12]));// 业务类型
		generalh.setCdispatcherid(WDSTools
				.getString_TrimZeroLenAsNull(tmpObj[13]));// 收发类别
		generalh.setCwhsmanagerid(WDSTools
				.getString_TrimZeroLenAsNull(tmpObj[14]));// 库管员主键
		generalh.setCdptid(WDSTools.getString_TrimZeroLenAsNull(tmpObj[15]));// 部门主键
		generalh.setCbizid(WDSTools.getString_TrimZeroLenAsNull(tmpObj[16]));// 业务员主键
		generalh.setCcustomerid(WDSTools
				.getString_TrimZeroLenAsNull(tmpObj[17]));// 客户主键
		generalh.setPk_cubasdocc(WDSTools
				.getString_TrimZeroLenAsNull(tmpObj[18]));// 客商基本档案主键
		generalh.setVdiliveraddress(WDSTools
				.getString_TrimZeroLenAsNull(tmpObj[19]));// 收货地址
		generalh.setVnote(WDSTools.getString_TrimZeroLenAsNull(tmpObj[20]));// 备注
		generalh.setCregister(WDSTools.getString_TrimZeroLenAsNull(tmpObj[21]));// 签字人主键
		generalh.setTaccounttime(WDSTools
				.getString_TrimZeroLenAsNull(tmpObj[22]));// 签字时间
		generalh
				.setCauditorid(WDSTools.getString_TrimZeroLenAsNull(tmpObj[23]));// 审核人
		generalh.setDauditdate(CommonUnit.getUFDate_Null(tmpObj[24]));// 审核日期
		generalh.setClastmodiid(WDSTools
				.getString_TrimZeroLenAsNull(tmpObj[25]));// 最后修改人
		generalh.setTlastmoditime(WDSTools
				.getString_TrimZeroLenAsNull(tmpObj[26]));// 最后修改时间
		generalh.setIprintcount(CommonUnit.getInteger_Null(tmpObj[27]));// 打印次数
		generalh.setComp(WDSTools.getString_TrimZeroLenAsNull(tmpObj[28]));// 公司
		generalh.setQianzidate(CommonUnit.getUFDate_Null(tmpObj[29]));// 签字日期
		if (null != tmpObj[30])
			generalh.setTs(new UFTime(tmpObj[30].toString()));// 时间戳
		generalh.setDr(CommonUnit.getInteger_Null(tmpObj[31]));// 删除标志
		generalh.setState(CommonUnit.getInteger_Null(tmpObj[32]));// 状态
		return generalh;
	}

	/**
	 * 进行VO转换 本地出入库表头VO和ERP中出入库 转换表头
	 * 
	 * @param ich
	 *            本地出入库表头VO
	 * @return 转换后的ERP中出入库表头
	 */
	public static GeneralBillHeaderVO setGeneralHVO(IcGeneralHVO ich) {
		if (null == ich)
			return null;
		GeneralBillHeaderVO generalh = new GeneralBillHeaderVO();
		generalh.setCgeneralhid(ich.getCgeneralhid()); // PK
		generalh.setCbiztypeid(ich.getCbiztype());// 业务类型
		generalh.setCbilltypecode(ich.getCbilltypecode());// 单据类型编码
		generalh.setCcustomerid(ich.getCcustomerid());// 客户
		generalh.setPk_corp(ich.getPk_corp());// 公司
		generalh.setCregister(ich.getCregister());// 签字人
		generalh.setDaccountdate(ich.getDaccountdate());// 签字日期
		generalh.setAttributeValue("taccounttime", ich.getTaccounttime());// 签字时间
		generalh.setVbillcode(ich.getVbillcode());// 单据号
		generalh.setDbilldate(ich.getDbilldate());// 单据日期
		generalh.setCwarehouseid(ich.getCwarehouseid());// 仓库
		generalh.setAttributeValue("cotherwhid", ich.getCotherwhid());// 其他仓库
		generalh.setCdispatcherid(ich.getCdispatcherid());// 收发类别
		generalh.setCdptid(ich.getCdptid());// 部门
		generalh.setCwhsmanagerid(ich.getCwhsmanagerid()); // 库管员
		generalh.setCoperatorid(ich.getCoperatorid());// 制单人
		generalh.setAttributeValue("coperatoridnow", ich.getCoperatorid()); // 操作人
		generalh.setCinventoryid(ich.getCinventoryid()); // 库存产品
		generalh.setVdiliveraddress(ich.getVdiliveraddress()); // 地址
		generalh.setCbizid(ich.getCbizid()); // 业务员
		generalh.setVnote(ich.getVnote()); // 备注
		generalh.setFbillflag(ich.getFbillflag()); // 状态
		generalh.setPk_calbody(ich.getPk_calbody()); // 库存组织PK
		generalh.setAttributeValue("clastmodiid", ich.getClastmodiid());// 最后修改人
		generalh.setAttributeValue("tlastmoditime", ich.getTlastmoditime());// 最后修改时间
		generalh.setAttributeValue("tmaketime", ich.getTmaketime());// 制单时间
		generalh.setPk_cubasdocC(ich.getPk_cubasdocc());// 客户基本档案ID
		return generalh;
	}

	/**
	 * 进行VO转换 本地出入库表体VO和ERP中出入库 转换表体
	 * 
	 * @param ich
	 *            本地出入库表体VO
	 * @return 转换后的ERP中出入库表体
	 */
	public static GeneralBillItemVO setGeneralItemVO(IcGeneralBVO icb) {
		if (null == icb)
			return null;
		GeneralBillItemVO generalb = new GeneralBillItemVO();
		generalb.setCgeneralhid(icb.getCgeneralhid()); // 出库表头ID
		generalb.setCgeneralbid(icb.getCgeneralbid()); // 出库表体ID
		generalb.setAttributeValue("pk_corp", icb.getPk_corp()); // 公司
		generalb.setCinvbasid(icb.getCinvbasid());// 存货基本ID
		generalb.setCinventoryid(icb.getCinventoryid());// 存货ID
		generalb.setVbatchcode(icb.getVbatchcode());// 批次号
		generalb.setDbizdate(icb.getDbizdate());// 业务日期
		generalb.setNshouldoutnum(icb.getNshouldoutnum());// 应出数量
		generalb.setNshouldoutassistnum(icb.getNshouldoutassistnum());// 应出辅数量
		generalb.setNoutnum(icb.getNoutnum());// 实出数量
		generalb.setNoutassistnum(icb.getNoutassistnum());// 实出辅数量
		generalb.setCastunitid(icb.getCastunitid());// 辅计量单位ID
		generalb.setNprice(icb.getNprice());// 单价
		generalb.setNmny(icb.getNmny());// 金额
		generalb.setCsourcebillhid(icb.getCsourcebillhid());// 来源单据表头序列号
		generalb.setCfirstbillhid(icb.getCfirstbillhid());// 源头单据表头ID
		generalb.setCfreezeid(icb.getCfreezeid());// 锁定来源
		generalb.setCsourcebillbid(icb.getCsourcebillbid());// 来源单据表体序列号
		generalb.setCfirstbillbid(icb.getCfirstbillbid());// 源头单据表体ID
		generalb.setCsourcetype(icb.getCsourcetype());// 来源单据类型
		generalb.setCfirsttype(icb.getCfirsttype());// 源头单据类型
		generalb.setVsourcebillcode(icb.getVsourcebillcode());// 来源单据号
		generalb.setVfirstbillcode(icb.getVfirstbillcode());// 源头单据号
		generalb.setVsourcerowno(icb.getVsourcerowno());// 来源单据行号
		generalb.setVfirstrowno(icb.getVfirstrowno());// 源头单据行号
		generalb.setFlargess(icb.getFlargess());// 是否赠品
		generalb.setDfirstbilldate(icb.getDfirstbilldate());// 源头单据制单日期
		generalb.setCreceieveid(icb.getCreceieveid());// 收货单位
		generalb.setCrowno(icb.getCrowno());// 行号
		generalb.setHsl(icb.getHsl());// 换算率
		generalb.setNsaleprice(icb.getNsaleprice());// 销售价格
		generalb.setNtaxprice(icb.getNtaxprice());// 含税单价
		generalb.setNtaxmny(icb.getNtaxmny());// 含税金额
		generalb.setNsalemny(icb.getNsalemny());// 不含税金额
		generalb.setAttributeValue("cquotecurrency", icb.getCquotecurrency());// 设置币种

		return generalb;
	}

	/**
	 * 根据仓库主键查询出该仓库是否为总仓或分仓
	 * 
	 * @param pk_stock
	 *            仓库主键
	 * @return true 总仓 false 分仓
	 * @throws BusinessException
	 */
	public static boolean getSotckIsTotal(String pk_stock)
			throws BusinessException {
		String sql = "select def2 from bd_stordoc where pk_stordoc = '"
				+ pk_stock + "'";
		ArrayList list = (ArrayList) iuap.executeQuery(sql,
				new ArrayListProcessor());
		if (null != list && list.size() > 0 && null != list.get(0)
				&& !"".equals(list.get(0))) {
			Object[] a = (Object[]) list.get(0);
			if (null != a[0] && !"".equals(a[0]))
				if (a[0].equals("0"))
					return true;
		}

		return false;
	}

	/**
	 * 根据登录人查询人员类型
	 * 
	 * @param pk_user
	 *            用户主键
	 * @return 0 保管员 1 信息科 2 发运科 3内勤
	 * @throws BusinessException
	 */
	public static String getUserType(String pk_user) throws BusinessException {
		
		
		
		String sql = "select st_type from tb_stockstaff where dr = 0 and cuserid = '"
				+ pk_user + "'";
		ArrayList list = (ArrayList) iuap.executeQuery(sql,
				new ArrayListProcessor());
		if (null != list && list.size() > 0 && null != list.get(0)
				&& !"".equals(list.get(0))) {
			Object[] a = (Object[]) list.get(0);
			if (null != a[0] && !"".equals(a[0]))
				return a[0].toString();
		}

		return null;
	}

	/**
	 * 操作库存表数据，托盘状态，运单中实出数量
	 * 
	 * @param generalBVO
	 *            出库单子表
	 * @param isType
	 *            是否转分仓，true转分仓
	 * @param isStock
	 *            总仓或者分仓 true 总仓
	 * @param operationType
	 *            true 修改 false 增加
	 * @return 是否操作成功
	 * @throws BusinessException
	 */
	public static boolean operationWare(TbOutgeneralBVO[] generalBVO,
			boolean isType, boolean isStock, boolean operationType)
			throws BusinessException {
		if (null != generalBVO && generalBVO.length > 0) {
			if (operationType) {
				List wareList = new ArrayList();
				List gentList = new ArrayList();
				List cargList = new ArrayList();
				List fydList = new ArrayList();
				List addFydmxList = new ArrayList();// 添加发运单明细集合
				for (int i = 0; i < generalBVO.length; i++) {
					// 判断实出辅数量是否为空
					if (null != generalBVO[i].getNoutassistnum()
							&& !"".equals(generalBVO[i].getNoutassistnum())) {
						String sWhere = " dr = 0 and cfirstbillhid = '"
								+ generalBVO[i].getCsourcebillhid()
								+ "' and pk_invbasdoc ='"
								+ generalBVO[i].getCinventoryid()
								+ "' and cfirstbillbid='"
								+ generalBVO[i].getCsourcebillbid() + "'";
						// 操作数据库得到缓存表结果集
						ArrayList generaltList = (ArrayList) iuap
								.retrieveByClause(TbOutgeneralTVO.class, sWhere);
						// 判断结果集是否为空
						if (null != generaltList && generaltList.size() > 0) {
							// 根据来源单据子表主键获取发运单明细信息
							TbFydmxnewVO fydmxvo = (TbFydmxnewVO) iuap
									.retrieveByPK(TbFydmxnewVO.class,
											generalBVO[i].getCsourcebillbid());
							TbOutgeneralTVO[] generaltvo = new TbOutgeneralTVO[generaltList
									.size()];
							generaltvo = (TbOutgeneralTVO[]) generaltList
									.toArray(generaltvo);
							double sum1 = 0;// 用来计算实出主辅数量
							double sum2 = 0;
							String batchCode = null; // 批次
							String lbatchCode = null; // 源批次
							List<String> batchList = new ArrayList<String>(); // 批次集合
							double nprice = 0; // 单价
							double nmny = 0; // 金额
							for (int j = 0; j < generaltvo.length; j++) {
								// 获取库存表主键
								String pk_ware = generaltvo[j].getWhs_pk();
								// 通过库存表主键查询出相应的信息
								TbWarehousestockVO ware = (TbWarehousestockVO) iuap
										.retrieveByPK(TbWarehousestockVO.class,
												pk_ware);

								if (null != ware) {

									if (null != ware.getWhs_stockpieces()
											&& !"".equals(ware
													.getWhs_stockpieces())
											&& null != generaltvo[j]
													.getNoutassistnum()
											&& !"".equals(generaltvo[j]
													.getNoutassistnum())) {
										// 获取当前库存表中的库存辅数量
										double count1 = ware
												.getWhs_stockpieces()
												.toDouble();
										// 获取当前缓存表中的实出辅数量
										double num1 = generaltvo[j]
												.getNoutassistnum().toDouble();
										// 相减得出剩余
										double resultNum1 = count1 - num1;
										if (resultNum1 >= 0) {

											if (null != fydmxvo) {
												// 设置实发辅数量
												sum1 = sum1 + num1;
												// 累加批次
												if (batchList.size() < 1) {
													batchList.add(generaltvo[j]
															.getVbatchcode());
													// 源批次
													lbatchCode = ware
															.getWhs_lbatchcode();
													if (null != ware
															.getWhs_nprice())
														nprice = ware
																.getWhs_nprice()
																.toDouble(); // 单价
													if (null != ware
															.getWhs_nmny())
														nmny = ware
																.getWhs_nmny()
																.toDouble();// 金额
												} else {
													batchList.add(generaltvo[j]
															.getVbatchcode());
												}

											}
											// 库存辅数量
											ware
													.setWhs_stockpieces(new UFDouble(
															resultNum1));
											// generaltvo[j]
											// .setStockpieces(new UFDouble(
											// resultNum1));
											// 等于0 并且 是总仓的时候更新托盘状态
											if (resultNum1 == 0 && isStock) {
												// 当前托盘上已经没有货了。修改托盘状态和库存表状态
												ware.setWhs_status(new Integer(
														1));
												// 根据主键查询出托盘信息
												BdCargdocTrayVO carg = (BdCargdocTrayVO) iuap
														.retrieveByPK(
																BdCargdocTrayVO.class,
																ware
																		.getPplpt_pk());
												if (null != carg) {
													carg
															.setCdt_traystatus(new Integer(
																	0)); // 把托盘状态设置空闲
													cargList.add(carg);
												}
											}
										} else {
											// 如果剩余数量小于0说明库存数量不足不能出库，操作异常
											return false;
										}
									}
									// 判断主数量
									if (null != ware.getWhs_stocktonnage()
											&& !"".equals(ware
													.getWhs_stocktonnage())
											&& null != generaltvo[j]
													.getNoutnum()
											&& !"".equals(generaltvo[j]
													.getNoutnum())) {
										double count2 = ware
												.getWhs_stocktonnage()
												.toDouble();
										double num2 = generaltvo[j]
												.getNoutnum().toDouble();
										// 相减得出剩余
										double resultNum2 = count2 - num2;
										if (resultNum2 >= 0) {

											if (null != fydmxvo) {
												// 设置实发主数量
												sum2 = sum2 + num2;
											}
											// 库存主数量
											ware
													.setWhs_stocktonnage(new UFDouble(
															resultNum2));
											// generaltvo[j]
											// .setStocktonnage(new UFDouble(
											// resultNum2));
										}
									}
									// 更新当前库存表信息
									wareList.add(ware);
									// gentList.add(generaltvo[j]);
								}
							}
							if (isType && null != fydmxvo) {
								if (batchList.size() > 1) {

									// 最终批次赋值
									batchCode = batchList.get(0);
									// 循环批次集合
									for (int j = 0; j < batchList.size(); j++) {
										// 判断批次集合是否有一个
										if (j == batchList.size() - 1)
											break;
										// 如果批次集合中的批次不是同一个批次的进行批次相加
										if (!batchList.get(j).equals(
												batchList.get(j + 1)))
											// 为最终批次相加
											batchCode = batchCode + ","
													+ batchList.get(j + 1);
									}
									String[] tmpArray = batchCode.split(",");
									if (tmpArray.length == 1) {
										if (sum1 != 0)
											fydmxvo.setCfd_sffsl(new UFDouble(
													sum1));
										if (sum2 != 0)
											fydmxvo.setCfd_sfsl(new UFDouble(
													sum2));
										fydmxvo.setCfd_pc(tmpArray[0]);

									} else {
										for (int k = 0; k < tmpArray.length; k++) {
											sum1 = 0;
											sum2 = 0;

											for (int j = 0; j < generaltvo.length; j++) {
												if (tmpArray[k]
														.equals(generaltvo[j]
																.getVbatchcode())) {
													sum1 = sum1
															+ generaltvo[j]
																	.getNoutassistnum()
																	.toDouble()
																	.doubleValue();
													sum2 = sum2
															+ generaltvo[j]
																	.getNoutnum()
																	.toDouble()
																	.doubleValue();
												}
											}
											fydmxvo.setCfd_sffsl(new UFDouble(
													sum1));// 实发辅数量
											fydmxvo.setCfd_sfsl(new UFDouble(
													sum2));// 实发数量
											fydmxvo.setCfd_yfsl(new UFDouble(
													sum2)); // 应发数量（吨）
											fydmxvo
													.setCfd_xs(new UFDouble(
															sum1)); // 应发辅数量
											fydmxvo.setCfd_pk(null);// 把主键清空
											fydmxvo.setCfd_pc(tmpArray[k]);
											fydmxvo.setDr(0);
											if (nmny != 0)
												fydmxvo
														.setCfd_nmny(new UFDouble(
																nmny));
											if (nprice != 0)
												fydmxvo
														.setCfd_nprice(new UFDouble(
																nprice));
											if (null != lbatchCode)
												fydmxvo.setCfd_lpc(lbatchCode);
											ivo.insertVO(fydmxvo); // 插入子表
											fydmxvo.setCfd_pk(generalBVO[i]
													.getCsourcebillbid());// 还原主键
											fydmxvo.setDr(1);// 更改删除标志
										}
									}
								} else {
									if (sum1 != 0)
										fydmxvo
												.setCfd_sffsl(new UFDouble(sum1));
									if (sum2 != 0)
										fydmxvo.setCfd_sfsl(new UFDouble(sum2));
									fydmxvo.setCfd_pc(batchList.get(0));
									fydList.add(fydmxvo);
								}
							} else {
								if (null != fydmxvo) {
									if (sum1 != 0)
										fydmxvo
												.setCfd_sffsl(new UFDouble(sum1));
									if (sum2 != 0)
										fydmxvo.setCfd_sfsl(new UFDouble(sum2));
									if (batchList.size() > 0) {
										// 最终批次赋值
										batchCode = batchList.get(0);
										// 循环批次集合
										for (int j = 0; j < batchList.size(); j++) {
											// 判断批次集合是否有一个
											if (j == batchList.size() - 1
													|| batchList.size() == 1)
												break;
											// 如果批次集合中的批次不是同一个批次的进行批次相加
											if (!batchList.get(j).equals(
													batchList.get(j + 1)))
												// 为最终批次相加
												batchCode = batchCode + ","
														+ batchList.get(j + 1);
										}
										fydmxvo.setCfd_pc(batchCode);
									}

									if (nmny != 0)
										fydmxvo.setCfd_nmny(new UFDouble(nmny));
									if (nprice != 0)
										fydmxvo.setCfd_nprice(new UFDouble(
												nprice));
									if (null != lbatchCode)
										fydmxvo.setCfd_lpc(lbatchCode);
									if (null != fydmxvo)
										fydList.add(fydmxvo);
								}
							}
						}
					}
				}
				// 运单
				if (fydList.size() > 0)
					ivo.updateVOList(fydList);
				// 库存表
				if (wareList.size() > 0)
					ivo.updateVOList(wareList);
				// 出库缓存表
				// ivo.updateVOList(gentList);
				// 托盘状态
				if (cargList.size() > 0)
					ivo.updateVOList(cargList);
				// // 插入发运单明细
				// if (addFydmxList.size() > 0)
				// ivo.insertVOList(addFydmxList);
				// System.out.println(123);
				return true;
			} else {
				operationFyd(generalBVO, isType);
				return true;
			}
		}
		return false;
	}

	/**
	 * 出库修改功能，操作发运单明细
	 * 
	 * @param generalbVO
	 *            出库单表体VO数组
	 * @param type
	 *            false 销售出库 true 其他出库
	 * @throws BusinessException
	 */
	public static void operationFyd(TbOutgeneralBVO[] generalbVO, boolean type)
			throws BusinessException {

		for (int i = 0; i < generalbVO.length; i++) {
			// 销售
			if (!type) {
				// 根据来源单据子表主键获取发运单明细信息
				TbFydmxnewVO fydmxvo = (TbFydmxnewVO) iuap.retrieveByPK(
						TbFydmxnewVO.class, generalbVO[i].getCsourcebillbid());
				setFydPro(fydmxvo, generalbVO[i]);
			} else {
				String strWhere1 = "";
				// 如果等于8是自制出库，需要更换条件
				if (null != generalbVO[i].getCsourcetype()
						&& generalbVO[i].getCsourcetype().equals("8")) {
					strWhere1 = generalbVO[i].getCfirstbillhid();
				} else
					strWhere1 = generalbVO[i].getCsourcebillhid();

				String strWhere = "  fyd_pk = '" + strWhere1
						+ "' and pk_invbasdoc = '"
						+ generalbVO[i].getCinventoryid() + "'";
				ArrayList list = (ArrayList) iuap.retrieveByClause(
						TbFydmxnewVO.class, strWhere);
				double sum1 = 0;// 用来计算实出主辅数量
				double sum2 = 0;
				String batchCode = null; // 批次
				String lbatchCode = null; // 源批次
				double nprice = 0; // 单价
				double nmny = 0; // 金额

				List<String> batchList = new ArrayList<String>(); // 批次集合
				if (null == list || list.size() == 0)
					continue;
				for (int j = 0; j < list.size(); j++) {
					((TbFydmxnewVO) list.get(j)).setDr(1);
					// 设置删除标志为1 执行。。
					ivo.updateVO((TbFydmxnewVO) list.get(j));
				}
				TbFydmxnewVO fydmx = (TbFydmxnewVO) list.get(0);

				strWhere = " dr = 0 and cfirstbillhid = '"
						+ generalbVO[i].getCsourcebillhid()
						+ "' and pk_invbasdoc ='"
						+ generalbVO[i].getCinventoryid() + "'";
				// 操作数据库得到缓存表结果集
				ArrayList generaltList = (ArrayList) iuap.retrieveByClause(
						TbOutgeneralTVO.class, strWhere);
				// 判断结果集是否为空
				if (null != generaltList && generaltList.size() > 0) {
					for (int k = 0; k < generaltList.size(); k++) {
						TbOutgeneralTVO generalt = (TbOutgeneralTVO) generaltList
								.get(k);
						// 累加批次
						if (batchList.size() < 1) {
							batchList.add(generalt.getVbatchcode());
							// 源批次
							lbatchCode = generalt.getLvbatchcode();
							if (null != generalt.getNprice())
								nprice = generalt.getNprice().toDouble()
										.doubleValue(); // 单价
							if (null != generalt.getNmny())
								nmny = generalt.getNmny().toDouble()
										.doubleValue();// 金额
						} else {
							batchList.add(generalt.getVbatchcode());
						}

					}

					if (batchList.size() > 0) {

						// 最终批次赋值
						batchCode = batchList.get(0);
						// 循环批次集合
						for (int z = 0; z < batchList.size(); z++) {
							// 判断批次集合是否有一个
							if (z == batchList.size() - 1)
								break;
							// 如果批次集合中的批次不是同一个批次的进行批次相加
							if (!batchList.get(z).equals(batchList.get(z + 1)))
								// 为最终批次相加
								batchCode = batchCode + ","
										+ batchList.get(z + 1);
						}
						String[] tmpArray = batchCode.split(",");
						for (int k = 0; k < tmpArray.length; k++) {
							sum1 = 0;
							sum2 = 0;

							for (int z = 0; z < generaltList.size(); z++) {
								if (tmpArray[k]
										.equals(((TbOutgeneralTVO) generaltList
												.get(z)).getVbatchcode())) {
									sum1 = sum1
											+ ((TbOutgeneralTVO) generaltList
													.get(z)).getNoutassistnum()
													.toDouble().doubleValue();
									sum2 = sum2
											+ ((TbOutgeneralTVO) generaltList
													.get(z)).getNoutnum()
													.toDouble().doubleValue();
								}
							}

							fydmx.setCfd_sffsl(new UFDouble(sum1));// 实发辅数量
							fydmx.setCfd_sfsl(new UFDouble(sum2));// 实发数量
							fydmx.setCfd_yfsl(new UFDouble(sum2)); // 应发数量（吨）
							fydmx.setCfd_xs(new UFDouble(sum1)); // 应发辅数量
							fydmx.setCfd_pk(null);// 把主键清空
							fydmx.setCfd_pc(tmpArray[k]);
							fydmx.setDr(0);
							if (nmny != 0)
								fydmx.setCfd_nmny(new UFDouble(nmny));
							if (nprice != 0)
								fydmx.setCfd_nprice(new UFDouble(nprice));
							if (null != lbatchCode)
								fydmx.setCfd_lpc(lbatchCode);
							ivo.insertVO(fydmx); // 插入子表

						}

					}
				}
			}
		}

	}

	/**
	 * 设置发运单明细表中的属性
	 * 
	 * @param fydmxvo
	 * @param generalb
	 * @throws BusinessException
	 */
	private static void setFydPro(TbFydmxnewVO fydmxvo, TbOutgeneralBVO generalb)
			throws BusinessException {
		if (null != fydmxvo) {
			// 辅数量
			fydmxvo.setCfd_sffsl(generalb.getNoutassistnum());
			// 主数量
			fydmxvo.setCfd_sfsl(generalb.getNoutnum());
			// 批次
			fydmxvo.setCfd_pc(generalb.getVbatchcode());
			// 源批次
			fydmxvo.setCfd_lpc(generalb.getLvbatchcode());
			// 单价
			fydmxvo.setCfd_nprice(generalb.getNprice());
			// 金额
			fydmxvo.setCfd_nmny(generalb.getNmny());
			// 执行更新
			ivo.updateVO(fydmxvo);
		}
	}

	/**
	 * 根据当前登录者主键和存货单品主键查询出库存表中信息 进行批次排序
	 * 
	 * @param pk_user
	 *            当前登录者主键
	 * 
	 * @param pk_invbasdoc
	 *            存货单品主键
	 * @return 库存表集合
	 */
	public static List getStockDetailByPk_User(String pk_user,
			String pk_invbasdoc) {
		StringBuffer sql = new StringBuffer(
				"select o.whs_pk ,o.pplpt_pk,o.whs_stockpieces,o.whs_stocktonnage,o.pk_invbasdoc,o.whs_batchcode,"
						+ "o.whs_nprice,o.whs_nmny,o.whs_lbatchcode from tb_warehousestock o ,tb_stockstaff s, "
						+ " tb_stockstate k "
						+ " where o.dr = 0 and s.dr=0 and k.dr = 0 and  o.pk_cargdoc=s.pk_cargdoc and s.cuserid='"
						+ pk_user
						+ "' and o.whs_status = 0 and o.ss_pk = k.ss_pk and k.ss_isout = 0");
		if (null != pk_invbasdoc && pk_invbasdoc.length() > 0) {
			sql.append(" and o.pk_invbasdoc = '" + pk_invbasdoc + "'");
		}
		sql.append("  order by o.whs_batchcode ");
		ArrayList list = null;
		try {
			list = (ArrayList) iuap.executeQuery(sql.toString(),
					new ArrayListProcessor());
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (null != list && list.size() > 0) {
			List strList = new ArrayList();
			for (int i = 0; i < list.size(); i++) {
				Object[] a = (Object[]) list.get(i);
				if (null != a && a.length > 0 && null != a[0]) {
					TbWarehousestockVO ware = new TbWarehousestockVO();
					ware.setWhs_pk(WDSTools.getString_TrimZeroLenAsNull(a[0])); // 库存表主键
					ware
							.setPplpt_pk(WDSTools
									.getString_TrimZeroLenAsNull(a[1])); // 托盘主键
					ware.setWhs_stockpieces(WDSTools
							.getUFDouble_NullAsZero(a[2])); // 主数量
					ware.setWhs_stocktonnage(WDSTools
							.getUFDouble_NullAsZero(a[3])); // 辅数量
					ware.setPk_invbasdoc(WDSTools
							.getString_TrimZeroLenAsNull(a[4])); // 存货主键
					ware.setWhs_batchcode(WDSTools
							.getString_TrimZeroLenAsNull(a[5])); // 批次
					ware.setWhs_nprice(WDSTools.getUFDouble_NullAsZero(a[6]));// 单价
					ware.setWhs_nmny(WDSTools.getUFDouble_NullAsZero(a[7])); // 金额
					ware.setWhs_lbatchcode(WDSTools
							.getString_TrimZeroLenAsNull(a[8]));// 来源批次
					strList.add(ware);
				}
			}
			return strList;
		}
		return null;
	}

	/**
	 * 根据登录人员主键查询出对应的货品主键
	 * 
	 * @param pk
	 *            人员主键
	 * @return 货品主键数组
	 * @throws BusinessException
	 */
	public static List getInvbasdoc_Pk(String pk) {
		String sql = "select s.pk_invbasdoc from tb_stockstaff t,tb_spacegoods s "
				+ "where t.dr = 0 and s.dr = 0 and t.pk_cargdoc = s.pk_cargdoc and t.cuserid='"
				+ pk + "'";
		ArrayList list = null;
		try {
			list = (ArrayList) iuap.executeQuery(sql, new ArrayListProcessor());
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (null != list && list.size() > 0) {
			Object[] a = (Object[]) list.get(0);
			if (null != a && a.length > 0 && null != a[0]) {
				List strList = new ArrayList();
				for (int i = 0; i < list.size(); i++) {
					a = (Object[]) list.get(i);
					strList.add(a[0]);
				}
				return strList;
			}
		}
		return null;
	}

	/**
	 * 根据登录人员主键查询出对应的仓库主键
	 * 
	 * @param pk
	 *            人员主键
	 * @return 仓库主键
	 * @throws BusinessException
	 */
	public static String getStordocName(String pk) throws BusinessException {
		String tmp = null;
		String sql = "select pk_stordoc from tb_stockstaff where dr = 0 and cuserid='"
				+ pk + "'";
		ArrayList list = (ArrayList) iuap.executeQuery(sql,
				new ArrayListProcessor());
		if (null != list && list.size() > 0) {
			Object[] a = (Object[]) list.get(0);
			if (null != a && a.length > 0 && null != a[0]) {
				tmp = a[0].toString();
			}
		}

		return tmp;
	}

	/**
	 * 根据登录人员主键查询出对应的货位主键
	 * 
	 * @param pk
	 *            人员主键
	 * @return 货位主键
	 * @throws BusinessException
	 */
	public static String getCargDocName(String pk) throws BusinessException {
		String tmp = null;
		String sql = "select pk_cargdoc from tb_stockstaff where dr = 0 and cuserid='"
				+ pk + "'";
		ArrayList list = (ArrayList) iuap.executeQuery(sql,
				new ArrayListProcessor());
		if (null != list && list.size() > 0) {
			Object[] a = (Object[]) list.get(0);
			if (null != a && a.length > 0 && null != a[0]) {
				tmp = a[0].toString();
			}
		}

		return tmp;
	}

	/**
	 * 根据客户主键查询出所属区域，作为到货站
	 * 
	 * @param pk_cubasdoc
	 *            客户主键
	 * @return
	 * @throws BusinessException
	 */
	public static String getAreaclName(String pk_cubasdoc)
			throws BusinessException {
		String tmp = null;
		String sql = "select c.pk_areacl  from bd_cubasdoc c,bd_cumandoc m where c.dr = 0 and m.dr = 0  and m.pk_cubasdoc = c.pk_cubasdoc  and m.pk_cumandoc = '"
				+ pk_cubasdoc + "'";
		ArrayList list = (ArrayList) iuap.executeQuery(sql,
				new ArrayListProcessor());
		if (null != list && list.size() > 0) {
			Object[] a = (Object[]) list.get(0);
			if (null != a && a.length > 0 && null != a[0]) {
				tmp = a[0].toString();
			}
		}

		return tmp;
	}

	/**
	 * 根据销售主表的PK进行查询打印次数
	 * 
	 * @param pk
	 *            主键
	 * @return 打印次数
	 * @throws BusinessException
	 */
	public static int getIprintCount(String pk) throws BusinessException {
		int count = 0;

		String sql = "select iprintcount from so_sale where csaleid = '" + pk
				+ "' and iprintcount is not null";
		IUAPQueryBS iuap = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		ArrayList list = (ArrayList) iuap.executeQuery(sql,
				new ArrayListProcessor());
		if (null != list && list.size() > 0) {
			Object[] results = (Object[]) list.get(0);
			if (results != null && results.length > 0 && results[0] != null) {
				if (null != results[0] && !"".equals(results[0])) {
					count = Integer.parseInt(results[0].toString());
				}
			}
		}
		return count;
	}

	public static UFBoolean getUFBoolean_Null(Object value) {
		if (null != value)
			return new UFBoolean(value.toString());
		return null;
	}

	public static Integer getInteger_Null(Object value) {
		if (null != value)
			return new Integer(value.toString());
		return null;
	}

	public static UFDate getUFDate_Null(Object value) {
		if (null != value)
			return new UFDate(value.toString());
		return null;
	}

	/**
	 * 生成单据号 创建日期：(2001-11-22 13:03:58) zhy改造
	 * 
	 * @return java.lang.String
	 * @param billtype
	 *            java.lang.String
	 * @param pkcorp
	 *            java.lang.String
	 * @param cbillcode
	 *            java.lang.String
	 */
	public static String getBillCode(String billtype, String pkcorp,
			String gcbm, String operator) throws BusinessException {
		String scddh = null;
		try {
			BillCodeObjValueVO vo = new BillCodeObjValueVO();
			String[] names = { "", "" };
			String[] values = new String[] { "", "" };
			vo.setAttributeValue(names, values);
			scddh = getBillCode(billtype, pkcorp, vo);
		} catch (Exception e) {
			nc.vo.ic.pub.GenMethod.throwBusiException(e);
		}
		return scddh;
	}

	/**
	 * 生成单据号 创建日期：(2001-11-22 13:03:58)
	 * 
	 * @return java.lang.String
	 * @param billtype
	 *            java.lang.String
	 * @param pkcorp
	 *            java.lang.String
	 * @param cbillcode
	 *            java.lang.String
	 */
	private static String getBillCode(String billtype, String pkcorp,
			nc.vo.pub.billcodemanage.BillCodeObjValueVO billVO)
			throws BusinessException {
		String djh = null;
		try {
			IBillcodeRuleService bo = (IBillcodeRuleService) NCLocator
					.getInstance().lookup(IBillcodeRuleService.class.getName());
			djh = bo.getBillCode_RequiresNew(billtype, pkcorp, null, billVO);

		} catch (Exception e) {
			nc.vo.ic.pub.GenMethod.throwBusiException(e);
		}
		return djh;
	}
}
