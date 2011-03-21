package nc.bs.wds.w8004040208;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.wds.w8004040204.TbOutgeneralBVO;
import nc.vo.wds.w8004040204.TbOutgeneralTVO;
import nc.vo.wds.w8004040204.TbWarehousestockVO;

public class W8004040208Impl   {

	// 获取数据库访问对象
	IVOPersistence ivo = null;
	// 数据库查询对象
	IUAPQueryBS iuap = null;

	/**
	 * 根据单品主键查询改单品的中文名称
	 * 
	 * @param invpk
	 *            单品主键
	 * @return
	 * @throws BusinessException
	 */
	public String getInvbasdocName(String invpk) throws BusinessException {
		if (null != invpk && !"".equals(invpk)) {
			String sql = "select invname from bd_invbasdoc where pk_invbasdoc = '"
					+ invpk + "'";
			ArrayList list = (ArrayList) this.getIuap().executeQuery(sql,
					new ArrayListProcessor());
			if (null != list && list.size() > 0) {
				Object[] a = (Object[]) list.get(0);
				if (null != a && a.length > 0 && null != a[0]) {
					return a[0].toString();
				}
			}
		}
		return null;
	}

	/*
	 * 自动取货(non-Javadoc)
	 * 
	 * @see nc.itf.wds.w8004040208.Iw8004040208#w8004040208AutoPickAction(nc.vo.wds.w8004040204.TbOutgeneralBVO[])
	 */
	public String w8004040208AutoPickAction(TbOutgeneralBVO[] generalb)
			throws Exception {
//		deleteAutoPickAction(generalb);
//		TbOutgeneralTVO generalt = null;
//		double asum = 0;// 应发数量
//		if (null != generalb && generalb.length > 0) {
//			for (int i = 0; i < generalb.length; i++) {
//				if (null == generalb[i])
//					continue;
//				if (null != generalb[i].getCinventoryid()
//						&& !"".equals(generalb[i].getCinventoryid().trim())) {
//					String strWhere = "  dr = 0 and pk_invbasdoc = '"
//							+ generalb[i].getCinventoryid().trim()
//							+ "' and whs_status = 0 order by whs_batchcode ";
//					List list = (List) this.getIuap().retrieveByClause(
//							TbWarehousestockVO.class, strWhere);
//					TbWarehousestockVO[] wareVO = null;
//					// 获取应出辅数量
//					asum = generalb[i].getNshouldoutassistnum().toDouble();
//					if (null != list && list.size() > 0) {
//						wareVO = new TbWarehousestockVO[list.size()];
//						wareVO = (TbWarehousestockVO[]) list.toArray(wareVO);
//						for (int j = 0; j < wareVO.length; j++) {
//							TbWarehousestockVO warevo = wareVO[j];
//							int com = -1; // 当前日期和数据库中批次日期的时间差
//							int alertnum = -1; // 根据主键查询数来的销售警戒天数
//							if (null != warevo) {
//								String bathdate = ""; // 存储截取后的批次日期
//								if (warevo.getWhs_batchcode().length() > 8)
//									bathdate = warevo.getWhs_batchcode()
//											.substring(0, 8);
//								long DAY = 24L * 60L * 60L * 1000L;
//								SimpleDateFormat format = new SimpleDateFormat(
//										"yyyyMMdd");
//								Date date = new Date();
//								try {
//									// 计算 当前日期和数据库中批次日期的时间差
//									com = Integer
//											.parseInt(((date.getTime() - format
//													.parse(bathdate).getTime()) / DAY)
//													+ "");
//								} catch (RuntimeException e) {
//									// TODO Auto-generated catch block
//									return "数据库中批次日期不合格";
//								}
//								// 根据单品主键查询出该单品的转库警戒天数
//								String sql = "select b.def16,m.scalefactor from bd_invbasdoc b ,bd_measdoc m  where b.pk_invbasdoc = '"
//										+ generalb[i].getCinventoryid().trim()
//										+ "' and b.pk_measdoc1 = m.pk_measdoc and b.dr = 0 and m.dr=0";
//								ArrayList resultsList = (ArrayList) this
//										.getIuap().executeQuery(sql,
//												new ArrayListProcessor());
//								Object[] a = null;
//								if (null != resultsList
//										&& resultsList.size() > 0) {
//									a = (Object[]) resultsList.get(0);
//								}
//								if (null != a && a.length > 0 && null != a[0]) {
//									alertnum = Integer
//											.parseInt(a[0].toString());
//								} else {
//
//									return "'"
//											+ this.getInvbasdocName(generalb[i]
//													.getCinventoryid())
//											+ "' 存货档案中没有维护警戒天数";
//								}
//
//								// 如果当前日期和批次的时间差小于警戒天数 该货品可以出库
//								if (com < alertnum) {
//									if (null != warevo.getWhs_stockpieces()
//											&& !"".equals(warevo
//													.getWhs_stockpieces())
//											&& warevo.getWhs_stockpieces()
//													.toDouble() > 0) {
//										generalt = new TbOutgeneralTVO();
//										double tmpsum = asum;
//										// 库存数量减去应出数量
//										asum = warevo.getWhs_stockpieces()
//												.toDouble()
//												- asum;
//										// 换算率
//										double noutnum = 0;
//										if (null != a[1])
//											noutnum = Double.parseDouble(a[1]
//													.toString());
//										// 如果剩余数量大于等于0 当前托盘存货量够出
//										if (asum >= 0) {
//											// 把当前出货量存储实出数量
//											generalt
//													.setNoutassistnum(new UFDouble(
//															tmpsum));
//											// 计算实出主数量
//											if (noutnum != 0) {
//												generalt
//														.setNoutnum(new UFDouble(
//																tmpsum
//																		* noutnum));
//											}
//										} else {
//											// 如果剩余数量小于0 当前托盘存货量不够出，当前托盘存货量为出货量
//											asum = warevo.getWhs_stockpieces()
//													.toDouble()
//													- asum;
//											asum = Math.abs(asum);
//											generalt.setNoutassistnum(warevo
//													.getWhs_stockpieces());
//											if (noutnum != 0) {
//												generalt
//														.setNoutnum(new UFDouble(
//																warevo
//																		.getWhs_stockpieces()
//																		.toDouble()
//																		* noutnum));
//											}
//										}
//
//										if (null != generalb[i]
//												.getCsourcebillhid()
//												&& !"".equals(generalb[i]
//														.getCsourcebillhid())) {
//											generalt
//													.setCfirstbillhid(generalb[i]
//															.getCsourcebillhid()); // 源头主表
//										}
//										if (null != generalb[i]
//												.getCsourcebillbid()
//												&& !"".equals(generalb[i]
//														.getCsourcebillbid())) { // 源头子表
//											generalt
//													.setCfirstbillbid(generalb[i]
//															.getCsourcebillbid());
//										}
//										if (null != generalb[i]
//												.getVsourcebillcode()
//												&& !"".equals(generalb[i]
//														.getVsourcebillcode())) {
//											generalt
//													.setVsourcebillcode(generalb[i]
//															.getVsourcebillcode()); // 来源单据号
//										}
//										if (null != warevo.getPplpt_pk()
//												&& !"".equals(warevo
//														.getPplpt_pk())) {
//											generalt.setCdt_pk(warevo
//													.getPplpt_pk()); // 托盘主键
//										}
//										if (null != warevo.getWhs_stockpieces()
//												&& !"".equals(warevo
//														.getWhs_stockpieces())) {
//											generalt.setStockpieces(warevo
//													.getWhs_stockpieces()); // 库存数量
//										}
//										if (null != warevo
//												.getWhs_stocktonnage()
//												&& !"".equals(warevo
//														.getWhs_stocktonnage())) {
//											generalt.setStocktonnage(warevo
//													.getWhs_stocktonnage()); // 库存辅数量
//										}
//										if (null != warevo.getPk_invbasdoc()
//												&& !"".equals(warevo
//														.getPk_invbasdoc())) {
//											generalt.setPk_invbasdoc(warevo
//													.getPk_invbasdoc()); // 库存主键
//										}
//										if (null != warevo.getWhs_batchcode()
//												&& !"".equals(warevo
//														.getWhs_batchcode())) {
//											generalt.setVbatchcode(warevo
//													.getWhs_batchcode()); // 批次
//										}
//										generalt.setDr(0); // 删除标志
//										// 缓存表插入
//										this.getIvo().insertVO(generalt);
//
//										if (asum >= 0) {
//											break;
//										}
//									}
//
//								}
//							}
//						}
//					}
//
//				}
//			}
//
//		}
		return null;
	}

	/**
	 * 根据出库子表信息删除缓存里面数据
	 * 
	 * @param generalb
	 *            出库子表
	 * @throws BusinessException
	 */
	private void deleteAutoPickAction(TbOutgeneralBVO[] generalb)
			throws BusinessException {
		if (null != generalb && generalb.length > 0) {
			for (int i = 0; i < generalb.length; i++) {
				String strWhere = " pk_invbasdoc='"
						+ generalb[i].getCinventoryid()
						+ "' and cfirstbillbid ='"
						+ generalb[i].getCsourcebillbid() + "'";
				this.getIvo().deleteByClause(TbOutgeneralTVO.class, strWhere);
			}
		}
	}

	public IVOPersistence getIvo() {
		return (IVOPersistence) NCLocator.getInstance().lookup(
				IVOPersistence.class.getName());
	}

	public void setIvo(IVOPersistence ivo) {
		this.ivo = ivo;
	}

	public IUAPQueryBS getIuap() {
		return (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
	}

	public void setIuap(IUAPQueryBS iuap) {
		this.iuap = iuap;
	}

}
