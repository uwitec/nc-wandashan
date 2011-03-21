package nc.bs.wds.w80020202;

import java.sql.Ref;
import java.util.ArrayList;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.itf.uif.pub.IUifService;
import nc.itf.wds.w80020202.Iw80020202;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.lang.UFDouble;
import nc.vo.wds.w80020202.TbAcquisitionVO;
import nc.vo.wds.w80020202.TbHandlecostsBVO;
import nc.vo.wds.w80020202.TbHandlecostsVO;
import nc.vo.wds.w80020202.TbLogoVO;
import nc.vo.wds.w80021002.TbHandlingfeepriceVO;
import nc.vo.wds.w80021012.TbHandlingcolVO;
import nc.vo.wds.w80021016.TbHandlingacqVO;
import nc.vo.wds.w8004040204.TbOutgeneralBVO;
import nc.vo.wds.w8004040204.TbOutgeneralHVO;
import nc.vo.wds.w8004040210.TbGeneralBVO;
import nc.vo.wds.w8004040210.TbGeneralHVO;

public class W80020202Impl implements Iw80020202 {

	public AggregatedValueObject saveBD80020202(AggregatedValueObject billVO,
			Object userObj) throws Exception {
		// TODO Auto-generated method stub
		// 总费用
		double totalcost = 0;
		IVOPersistence perse = (IVOPersistence) NCLocator.getInstance().lookup(
				IVOPersistence.class.getName());
		TbHandlecostsVO tbHandlecostsVO = null;
		if (null != billVO) {
			tbHandlecostsVO = (TbHandlecostsVO) billVO.getParentVO();
		}
		// 按件计算
		ArrayList tbhandlecostsbVOs = getTbhandlecostsb(tbHandlecostsVO);
		if (null != tbhandlecostsbVOs) {
			for (int i = 0; i < tbhandlecostsbVOs.size(); i++) {
				totalcost += ((TbHandlecostsBVO) tbhandlecostsbVOs.get(i))
						.getHcb_cost().doubleValue();
			}
		}
		perse.insertVOList(tbhandlecostsbVOs);
		// 采集信息
		ArrayList tbacquisitionVOs = getTbacquisition(tbHandlecostsVO);
		if (null != tbacquisitionVOs) {
			for (int i = 0; i < tbacquisitionVOs.size(); i++) {
				totalcost += ((TbAcquisitionVO) tbacquisitionVOs.get(i))
						.getAqs_fy().doubleValue();
			}
		}
		perse.insertVOList(tbacquisitionVOs);
		// 贴标签
		ArrayList tblogoVos = getTblogo(tbHandlecostsVO);
		if (null != tblogoVos) {
			for (int i = 0; i < tblogoVos.size(); i++) {
				totalcost += ((TbLogoVO) tblogoVos.get(i)).getLgo_fy()
						.doubleValue();
			}
		}
		perse.insertVOList(tblogoVos);
		// 表头保存
		double hc_addcost = 0;
		if (null != tbHandlecostsVO.getHc_addcost()) {
			hc_addcost = tbHandlecostsVO.getHc_addcost().doubleValue();
		}
		totalcost += hc_addcost;
		tbHandlecostsVO.setDbilltype(1);
		tbHandlecostsVO.setHc_totalcost(new UFDouble(totalcost));
		if (null != tbHandlecostsVO.getHc_mannum()) {
			tbHandlecostsVO.setHc_avgcost(new UFDouble(totalcost
					/ tbHandlecostsVO.getHc_mannum().doubleValue()));
		}

		nc.itf.uif.pub.IUifService service = (IUifService) NCLocator
				.getInstance().lookup(IUifService.class.getName());
		AggregatedValueObject retVo = service.saveBD(billVO, userObj);

		return retVo;
	}

	/**
	 * 按件计算
	 * 
	 * @param tbHandlecostsVO
	 * @return
	 * @throws Exception
	 */
	public ArrayList getTbhandlecostsb(TbHandlecostsVO tbHandlecostsVO)
			throws Exception {
		IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		// 查询出入库单表体
		String bsql = "";
		// 仓库
		String geh_cwarehouseid = "";
		if (null != tbHandlecostsVO) {
			if (null != tbHandlecostsVO.getHandtype()) {
				// 装车，出库单
				if (tbHandlecostsVO.getHandtype().intValue() == 0) {
					if (null != tbHandlecostsVO.getCsourcebillhid()) {
						bsql = " select sum(b.noutassistnum),sum(b.noutnum) ,i.invspec "
								+ " from tb_outgeneral_b b,bd_invbasdoc i "
								+ " where b.cinventoryid=i.pk_invbasdoc and b.general_pk='"
								+ tbHandlecostsVO.getCsourcebillhid()
								+ "' group by i.invspec";
						TbOutgeneralHVO tbOutgeneralHVO = (TbOutgeneralHVO) query
								.retrieveByPK(TbOutgeneralHVO.class,
										tbHandlecostsVO.getCsourcebillhid());
						if (null != tbOutgeneralHVO) {
							geh_cwarehouseid = tbOutgeneralHVO.getSrl_pk();
						}
					}

				} else {
					if (null != tbHandlecostsVO.getCsourcebillhid()) {
						bsql = " select sum(tb.geb_banum),sum(tb.geb_anum) ,ti.invspec "
								+ " from tb_general_b tb,bd_invbasdoc ti "
								+ " where tb.geb_cinvbasid=ti.pk_invbasdoc and  tb.geh_pk='"
								+ tbHandlecostsVO.getCsourcebillhid()
								+ "' group by ti.invspec";
						TbGeneralHVO tbGeneralHVO = (TbGeneralHVO) query
								.retrieveByPK(TbGeneralHVO.class,
										tbHandlecostsVO.getCsourcebillhid());
						if (null != tbGeneralHVO) {
							geh_cwarehouseid = tbGeneralHVO
									.getGeh_cwarehouseid();
						}
					}
				}
			}
		}
		ArrayList os = (ArrayList) query.executeQuery(bsql.toString(),
				new ArrayListProcessor());
		// 按件计算
		ArrayList tbhandlecostsbs = new ArrayList();
		if (null != os && os.size() > 0) {
			for (int i = 0; i < os.size(); i++) {
				Object[] gvo = (Object[]) os.get(i);
				TbHandlecostsBVO tbHandlecostsBVO = new TbHandlecostsBVO();
				if (null != gvo[0]) {
					tbHandlecostsBVO
							.setHcb_box(new UFDouble(gvo[0].toString()));
				}
				if (null != gvo[1]) {
					tbHandlecostsBVO.setHcb_tonnage(new UFDouble(gvo[1]
							.toString()));
				}
				if (null != gvo[0]) {
					// 规格
					String hfsql = " hfp_specification='" + gvo[2].toString()
							+ "' and pk_stordoc='" + geh_cwarehouseid + "' ";
					ArrayList hfvos = (ArrayList) query.retrieveByClause(
							TbHandlingfeepriceVO.class, hfsql);
					if (null != hfvos && hfvos.size() > 0) {
						TbHandlingfeepriceVO tbHandlingfeepriceVO = (TbHandlingfeepriceVO) hfvos
								.get(0);
						if (null != tbHandlingfeepriceVO) {
							tbHandlecostsBVO.setHfp_pk(tbHandlingfeepriceVO
									.getHfp_pk());
							double hcb_cost = 0;
							if (tbHandlecostsVO.getHandtype().intValue() == 0) {
								hcb_cost = Double
										.parseDouble(gvo[0].toString())
										* tbHandlingfeepriceVO
												.getHfp_packpricep()
												.doubleValue();
								tbHandlecostsBVO.setHcb_cost(new UFDouble(
										hcb_cost));
							} else {
								hcb_cost = Double
										.parseDouble(gvo[0].toString())
										* tbHandlingfeepriceVO
												.getHtp_unboxpricep()
												.doubleValue();
								tbHandlecostsBVO.setHcb_cost(new UFDouble(
										hcb_cost));
							}
						}
					}
					tbHandlecostsBVO.setDr(0);
					tbHandlecostsBVO.setHc_pk(tbHandlecostsVO.getHc_pk());
					tbhandlecostsbs.add(tbHandlecostsBVO);
				}
			}
		}
		return tbhandlecostsbs;
	}

	/**
	 * 采集信息
	 * 
	 * @param tbHandlecostsVO
	 * @return
	 * @throws Exception
	 */
	public ArrayList getTbacquisition(TbHandlecostsVO tbHandlecostsVO)
			throws Exception {
		IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		if (null != tbHandlecostsVO) {
			if (null != tbHandlecostsVO.getHandtype()) {
				// 装车，出库单
				if (tbHandlecostsVO.getHandtype().intValue() == 0) {
					String sWhere = " general_pk='"
							+ tbHandlecostsVO.getCsourcebillhid()
							+ "' and dr=0  ";
					// 出库单子表
					ArrayList tbOutgeneralBVOs = (ArrayList) query
							.retrieveByClause(TbOutgeneralBVO.class, sWhere);

					// 添加采集基础信息
					ArrayList tbAcquisitionVOs = new ArrayList();
					for (int i = 0; i < tbOutgeneralBVOs.size(); i++) {
						TbAcquisitionVO tbAcquisitionVO = new TbAcquisitionVO();
						if (null != tbOutgeneralBVOs.get(i)) {
							TbOutgeneralBVO tbOutgeneralBVO = (TbOutgeneralBVO) tbOutgeneralBVOs
									.get(i);
							String hlcsql = " dr=0 and pk_invbasdoc='"
									+ tbOutgeneralBVO.getCinventoryid() + "' ";
							// 采集基础信息
							ArrayList tbHandlingcolVOs = (ArrayList) query
									.retrieveByClause(TbHandlingcolVO.class,
											hlcsql);
							if (null != tbHandlingcolVOs
									&& tbHandlingcolVOs.size() > 0
									&& null != tbHandlingcolVOs.get(0)) {
								TbHandlingcolVO tbHandlingcolVO = (TbHandlingcolVO) tbHandlingcolVOs
										.get(0);
								tbAcquisitionVO.setPk_invbasdoc(tbOutgeneralBVO
										.getCinventoryid());
								// 箱数
								double xs = 0;
								if (null != tbOutgeneralBVO.getNoutassistnum()) {
									xs = tbOutgeneralBVO.getNoutassistnum()
											.doubleValue();
								}
								tbAcquisitionVO.setAqs_xs(new UFDouble(xs));
								// 单价
								double jg = 0;
								if (null != tbHandlingcolVO.getChargecol()) {
									jg = tbHandlingcolVO.getChargecol()
											.doubleValue();
								}
								tbAcquisitionVO.setAqs_jg(new UFDouble(jg));
								tbAcquisitionVO
										.setAqs_fy(new UFDouble(xs * jg));

								tbAcquisitionVO.setDr(0);
								tbAcquisitionVO.setHc_pk(tbHandlecostsVO
										.getHc_pk());
								tbAcquisitionVOs.add(tbAcquisitionVO);
							}

						}
					}
					return tbAcquisitionVOs;
				} else {
					String sWhere = " geh_pk='"
							+ tbHandlecostsVO.getCsourcebillhid()
							+ "' and dr=0  ";
					// 出库单子表
					ArrayList tbGeneralBVOs = (ArrayList) query
							.retrieveByClause(TbGeneralBVO.class, sWhere);

					// 添加采集基础信息
					ArrayList tbAcquisitionVOs = new ArrayList();
					for (int i = 0; i < tbGeneralBVOs.size(); i++) {
						TbAcquisitionVO tbAcquisitionVO = new TbAcquisitionVO();
						if (null != tbGeneralBVOs.get(i)) {
							TbGeneralBVO tbGeneralBVO = (TbGeneralBVO) tbGeneralBVOs
									.get(i);
							String hlcsql = " dr=0 and pk_invbasdoc='"
									+ tbGeneralBVO.getGeb_cinvbasid() + "' ";
							// 采集基础信息
							ArrayList tbHandlingcolVOs = (ArrayList) query
									.retrieveByClause(TbHandlingcolVO.class,
											hlcsql);
							if (null != tbHandlingcolVOs
									&& tbHandlingcolVOs.size() > 0
									&& null != tbHandlingcolVOs.get(0)) {
								TbHandlingcolVO tbHandlingcolVO = (TbHandlingcolVO) tbHandlingcolVOs
										.get(0);
								tbAcquisitionVO.setPk_invbasdoc(tbGeneralBVO
										.getGeb_cinvbasid());
								// 箱数
								double xs = 0;
								if (null != tbGeneralBVO.getGeb_banum()) {
									xs = tbGeneralBVO.getGeb_banum()
											.doubleValue();
								}
								tbAcquisitionVO.setAqs_xs(new UFDouble(xs));
								// 单价
								double jg = 0;
								if (null != tbHandlingcolVO.getChargecol()) {
									jg = tbHandlingcolVO.getChargecol()
											.doubleValue();
								}
								tbAcquisitionVO.setAqs_jg(new UFDouble(jg));
								tbAcquisitionVO
										.setAqs_fy(new UFDouble(xs * jg));

								tbAcquisitionVO.setDr(0);
								tbAcquisitionVO.setHc_pk(tbHandlecostsVO
										.getHc_pk());
								tbAcquisitionVOs.add(tbAcquisitionVO);
							}
						}
					}
					return tbAcquisitionVOs;
				}
			}
		}
		return null;
	}

	/**
	 * 贴标签
	 */
	public ArrayList getTblogo(TbHandlecostsVO tbHandlecostsVO)
			throws Exception {
		IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		// 查询出入库单表体
		String bsql = "";
		if (null != tbHandlecostsVO) {
			if (null != tbHandlecostsVO.getHandtype()) {
				// 装车，出库单
				if (tbHandlecostsVO.getHandtype().intValue() == 0) {
					bsql = " select sum(noutassistnum) from tb_outgeneral_b where general_pk='"
							+ tbHandlecostsVO.getCsourcebillhid()
							+ "' and dr=0";
				} else {
					bsql = " select sum(geb_banum) from tb_general_b  where geh_pk='"
							+ tbHandlecostsVO.getCsourcebillhid()
							+ "' and dr=0";
				}
			}
		}
		double num = 0;
		ArrayList os = (ArrayList) query.executeQuery(bsql.toString(),
				new ArrayListProcessor());
		ArrayList tblogoVos = new ArrayList();
		TbLogoVO tbLogoVO = new TbLogoVO();
		if (null != os && os.size() > 0) {
			Object[] gvo = (Object[]) os.get(0);
			if (null != gvo[0]) {
				num = Double.parseDouble(gvo[0].toString());

			}

		}
		tbLogoVO.setLgo_xs(new UFDouble(num));
		//
		double dj = 0;

		// 贴标签基础表
		ArrayList tbhandlingacqVos = (ArrayList) query.retrieveByClause(
				TbHandlingacqVO.class, " dr =0 ");
		if (null != tbhandlingacqVos && tbhandlingacqVos.size() > 0
				&& null != tbhandlingacqVos.get(0)) {
			TbHandlingacqVO tbhandlingacqVO = (TbHandlingacqVO) tbhandlingacqVos
					.get(0);
			dj = tbhandlingacqVO.getHaq_dj().doubleValue();

		}
		tbLogoVO.setLgo_dj(new UFDouble(dj));
		tbLogoVO.setLgo_fy(new UFDouble(num * dj));
		tbLogoVO.setDr(0);
		tbLogoVO.setHc_pk(tbHandlecostsVO.getHc_pk());
		tblogoVos.add(tbLogoVO);

		return tblogoVos;
	}
}
