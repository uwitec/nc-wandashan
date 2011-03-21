package nc.ui.wds.w80020206;

import java.util.ArrayList;
import java.util.List;

import com.ibm.db2.jcc.a.a;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.wds.w80020206.Iw80020206;
import nc.itf.wds.w8004040210.Iw8004040210;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.query.INormalQuery;
import nc.ui.wds.w8000.CommonUnit;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.locator.LocatorVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.wds.w8004040204.TbOutgeneralBVO;
import nc.vo.wds.w8004040204.TbOutgeneralHVO;
import nc.vo.wds.w80060406.TbFydnewVO;
import nc.vo.wds.w80060604.SoSaleVO;
import nc.vo.wds.w80060604.SoSaleorderBVO;
import nc.vo.wds.w80060608.IcGeneralBVO;
import nc.vo.wds.w80060608.IcGeneralHVO;

/**
 * 
 * 该类是AbstractMyEventHandler抽象类的实现类， 主要是重载了按钮的执行动作，用户可以对这些动作根据需要进行修改
 * 
 * @author author
 * @version tempProject version
 */

public class MyEventHandler extends AbstractMyEventHandler {

	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
		getButtonManager().getButton(
				nc.ui.wds.w80020206.buttun0206.ISsButtun.Qzqr)
				.setEnabled(false);
		getButtonManager().getButton(
				nc.ui.wds.w80020206.buttun0206.ISsButtun.Qxqz)
				.setEnabled(false);
	}

	@Override
	protected void onBoQuery() throws Exception {
		// TODO Auto-generated method stub
		StringBuffer strWhere = new StringBuffer();

		if (askForQueryCondition(strWhere) == false)
			return;// 用户放弃了查询

		strWhere.append(" and ( vdef6='2' or vdef6='3' ) and vdef5='2' ");

		SuperVO[] queryVos = queryHeadVOs(strWhere.toString());

		getBufferData().clear();
		// 增加数据到Buffer
		addDataToBuffer(queryVos);

		updateBuffer();
	}

	protected boolean askForQueryCondition(StringBuffer sqlWhereBuf)
			throws Exception {
		if (sqlWhereBuf == null)
			throw new IllegalArgumentException(
					"askForQueryCondition().sqlWhereBuf cann't be null");
		UIDialog querydialog = getQueryUI();

		if (querydialog.showModal() != UIDialog.ID_OK)
			return false;
		INormalQuery query = (INormalQuery) querydialog;

		String strWhere = query.getWhereSql();
		if (strWhere == null)
			strWhere = "1=1";

		if (getButtonManager().getButton(IBillButton.Busitype) != null) {
			if (getBillIsUseBusiCode().booleanValue())
				// 业务类型编码
				strWhere = "(" + strWhere + ") and "
						+ getBillField().getField_BusiCode() + "='"
						+ getBillUI().getBusicode() + "'";

			else
				// 业务类型
				strWhere = "(" + strWhere + ") and "
						+ getBillField().getField_Busitype() + "='"
						+ getBillUI().getBusinessType() + "'";

		}

		strWhere = "(" + strWhere + ") and (isnull(dr,0)=0)";

		// if (getHeadCondition() != null)
		// strWhere = strWhere + " and " + getHeadCondition();
		// 现在我先直接把这个拼好的串放到StringBuffer中而不去优化拼串的过程
		sqlWhereBuf.append(strWhere);
		return true;
	}

	IUAPQueryBS iuap = (IUAPQueryBS) NCLocator.getInstance().lookup(
			IUAPQueryBS.class.getName());

	@Override
	protected void onQzqr() throws Exception {
		// TODO Auto-generated method stub
		if (getBufferData().getCurrentRow() >= 0) {
			SoSaleVO generalh = (SoSaleVO) getBufferData().getCurrentVO()
					.getParentVO();
			TbFydnewVO[] tbFydnewVOs = (TbFydnewVO[]) getBufferData()
					.getCurrentVO().getChildrenVO();

			for (int i = 0; i < tbFydnewVOs.length; i++) {
				if (null != tbFydnewVOs[i]) {
					if (null != tbFydnewVOs[i].getFyd_constatus()) {
						if (0 == tbFydnewVOs[i].getFyd_constatus()) {
							getBillUI().showErrorMessage("运单未确认，不能签字！");
							return;
						}
					} else {
						getBillUI().showErrorMessage("运单未确认，不能签字！");
						return;
					}
				} else {
					getBillUI().showErrorMessage("子表有错误信息，不能关闭！");
					return;
				}
			}

			Object result = iuap.retrieveByPK(SoSaleVO.class, generalh
					.getCsaleid());
			if (null != result) {
				generalh = (SoSaleVO) result;
				if (null != generalh.getVdef6()) {
					if ("3".equals(generalh.getVdef6())) {
						getBillUI().showErrorMessage("该单据已经签字");
						return;
					} else {
						generalh.setVdef6("3");
						AggregatedValueObject billvo = changeReqOutgeneraltoGeneral();
						Iw80020206 iw = (Iw80020206) NCLocator.getInstance()
								.lookup(Iw80020206.class.getName());
						iw.whs_processAction80020206("PUSHSAVE", "SIGN", "4C",
								_getDate().toString(), billvo, generalh);

						getBillUI().showHintMessage("签字成功");
						getButtonManager().getButton(
								nc.ui.wds.w80020206.buttun0206.ISsButtun.Qzqr)
								.setEnabled(false);
						getButtonManager().getButton(
								nc.ui.wds.w80020206.buttun0206.ISsButtun.Qxqz)
								.setEnabled(true);
						// getBufferData().getCurrentVO().setParentVO(generalh);
						// updateBuffer();
						super.onBoRefresh();
						return;
					}
				} else {
					getBillUI().showErrorMessage("该单据状态错误");
					return;
				}

				// if (!"3".equals(generalh.getVdef6())) {
				//
				//					
				// }

			}
		}

	}

	/**
	 * 转换 把当前页面中的VO转换成ERP中的出库单聚合OV 调用方法 进行回写ERP中出库单
	 * 
	 * @return ERP中出库单聚合VO
	 * @throws Exception
	 */
	public GeneralBillVO changeReqOutgeneraltoGeneral() throws Exception {
		if (getBufferData().getCurrentRow() < 0) {
			getBillUI().showErrorMessage("请选择表头进行签字");
			return null;
		}
		// 本地出库表 表头
		SoSaleVO soSaleVO = (SoSaleVO) getBufferData().getCurrentVO()
				.getParentVO();
		// 本地出库表 表体
		TbFydnewVO[] tbFydnewVOs = (TbFydnewVO[]) getBufferData()
				.getCurrentVO().getChildrenVO();

		// 出库聚合VO
		GeneralBillVO gBillVO = new GeneralBillVO();
		// 出库表头VO
		GeneralBillHeaderVO generalhvo = null;
		// 出库子表集合
		List<GeneralBillItemVO> generalBVOList = new ArrayList<GeneralBillItemVO>();
		// 出库子表VO数组
		GeneralBillItemVO[] generalBVO = null;
		// Object o = myClientUI.getBillCardPanel().getBodyValueAt(0,
		// "cfirstbillhid");
		if (null != soSaleVO.getCsaleid() && !"".equals(soSaleVO.getCsaleid())) {
			// String sWhere = " dr = 0 and csaleid = '"
			// + outbvo[0].getCfirstbillhid() + "'";
			// ArrayList list = (ArrayList)
			// iuap.retrieveByClause(SoSaleVO.class,
			// sWhere);
			// if (null != list && list.size() > 0) {
			// SoSaleVO salehvo = (SoSaleVO) list.get(0);
			generalhvo = new GeneralBillHeaderVO();

			if (null != tbFydnewVOs && null != tbFydnewVOs[0]) {
				// 给出库单表头赋值
				TbFydnewVO tbFydnewVO = tbFydnewVOs[0];
				String fyd_sql = " csourcebillhid='" + tbFydnewVO.getFyd_pk()
						+ "' and dr=0 ";
				TbOutgeneralHVO tbOutgeneralHVO = new TbOutgeneralHVO();
				ArrayList list = (ArrayList) iuap.retrieveByClause(
						TbOutgeneralHVO.class, fyd_sql);
				if (null != list && null != list.get(0)) {
					tbOutgeneralHVO = (TbOutgeneralHVO) list.get(0);
				}
				generalhvo.setPk_corp(soSaleVO.getPk_corp());// 公司主键
				generalhvo.setCbiztypeid(soSaleVO.getCbiztype());// 业务类型
				generalhvo.setCbilltypecode("4C");// 库存单据类型编码
				generalhvo.setVbillcode(CommonUnit.getBillCode("4C",
						ClientEnvironment.getInstance().getCorporation()
								.getPk_corp(), "", ""));// 单据号
				generalhvo.setDbilldate(getBillUI()._getDate());// 单据日期
				generalhvo.setCwarehouseid(tbOutgeneralHVO.getSrl_pk());// 仓库ID
				generalhvo.setCdispatcherid(tbOutgeneralHVO.getCdispatcherid());// 收发类型ID
				generalhvo.setCdptid(soSaleVO.getCdeptid());// 部门ID
				generalhvo.setCwhsmanagerid(tbOutgeneralHVO.getCwhsmanagerid());// 库管员ID
				generalhvo.setCoperatorid(tbOutgeneralHVO.getCoperatorid());// 制单人
				generalhvo.setAttributeValue("coperatoridnow", tbOutgeneralHVO
						.getCoperatorid());// 操作人
				// generalhvo.setCinventoryid(outbvo[0].getCinventoryid());//
				// 存货ID
				generalhvo.setAttributeValue("csalecorpid", soSaleVO
						.getCsalecorpid());// 销售组织
				generalhvo.setCcustomerid(soSaleVO.getCcustomerid());// 客户ID
				generalhvo.setVdiliveraddress(soSaleVO.getVreceiveaddress());// 发运地址
				generalhvo.setCbizid(soSaleVO.getCemployeeid());// 业务员ID
				generalhvo.setVnote(soSaleVO.getVnote());// 备注
				generalhvo.setFbillflag(2);// 单据状态
				generalhvo.setPk_calbody(soSaleVO.getCcalbodyid());// 库存组织PK
				generalhvo.setAttributeValue("clastmodiid", tbOutgeneralHVO
						.getClastmodiid());// 最后修改人
				generalhvo.setAttributeValue("tlastmoditime", tbOutgeneralHVO
						.getTlastmoditime());// 最后修改时间
				generalhvo.setAttributeValue("tmaketime", tbOutgeneralHVO
						.getTmaketime());// 制单时间
				generalhvo.setPk_cubasdocC(tbOutgeneralHVO.getPk_cubasdocc());// 客户基本档案ID

				String sql_sosaleorder = " csaleid='" + soSaleVO.getCsaleid()
						+ "' and dr=0 ";
				ArrayList sosaleorders = (ArrayList) iuap.retrieveByClause(
						SoSaleorderBVO.class, sql_sosaleorder);
				// 给表体赋值
				for (int i = 0; i < sosaleorders.size(); i++) {
					// 单据表体附表--货位
					LocatorVO locatorvo = new LocatorVO();
					locatorvo.setPk_corp(soSaleVO.getPk_corp());
					boolean isBatch = false;
					if (null != sosaleorders) {
						// sWhere = " dr = 0 and corder_bid = '"
						// + outbvo[i].getCfirstbillbid() + "'";
						// ArrayList saleblist = (ArrayList) iuap
						// .retrieveByClause(SoSaleorderBVO.class, sWhere);
						if (null != sosaleorders.get(i)) {
							SoSaleorderBVO saleorderbvo = (SoSaleorderBVO) sosaleorders
									.get(i);
							GeneralBillItemVO generalb = new GeneralBillItemVO();
							generalb.setAttributeValue("pk_corp", saleorderbvo
									.getPk_corp());// 公司
							generalb.setCinvbasid(saleorderbvo
									.getCinvbasdocid());// 存货基本ID
							generalb.setCinventoryid(saleorderbvo
									.getCinventoryid());// 存货ID
							generalb.setVbatchcode("2009");// 批次号
							// 查询生产日期和失效日期
							String sql = "select dproducedate ,dvalidate  from scm_batchcode where pk_invbasdoc='"
									+ saleorderbvo.getCinvbasdocid()
									+ "' and vbatchcode='"
									+ "2009"
									+ "' and dr=0";
							ArrayList batchList = (ArrayList) iuap
									.executeQuery(sql, new ArrayListProcessor());
							if (null != batchList && batchList.size() > 0) {
								Object[] batch = (Object[]) batchList.get(0);
								// 生产日期
								if (null != batch[0] && !"".equals(batch[0]))
									generalb.setScrq(new UFDate(batch[0]
											.toString()));
								// 失效日期
								if (null != batch[0] && !"".equals(batch[0]))
									generalb.setDvalidate(new UFDate(batch[1]
											.toString()));
								isBatch = true;
							}
							generalb
									.setDbizdate(tbOutgeneralHVO.getDbilldate());// 业务日期
							generalb
									.setNshouldoutnum(saleorderbvo.getNnumber());// 应出数量
							// 所有出库单子表VO
							String sql_outbvo = " cfirstbillhid='"
									+ soSaleVO.getCsaleid()
									+ "' and cinventoryid='"
									+ saleorderbvo.getCinvbasdocid()
									+ "' and dr=0 ";

							ArrayList outbvos = (ArrayList) iuap
									.retrieveByClause(TbOutgeneralBVO.class,
											sql_outbvo);
							double noutnum = 0;
							double noutassistnum = 0;
							if (null != outbvos) {
								for (int j = 0; j < outbvos.size(); j++) {
									if (null != outbvos.get(j)) {
										TbOutgeneralBVO outbvo = (TbOutgeneralBVO) outbvos
												.get(j);
										if (null != outbvo.getNoutnum()
												&& null != outbvo
														.getNoutassistnum()) {
											noutnum += outbvo.getNoutnum()
													.doubleValue();
											noutassistnum += outbvo
													.getNoutassistnum()
													.doubleValue();
										}
									}
								}
							}

							if (noutnum == 0 || noutassistnum == 0) {
								isBatch = false;
							}

							// TbOutgeneralBVO outbvo = new TbOutgeneralBVO();
							// if (null != outbvos && null != outbvos.get(0)) {
							// outbvo = (TbOutgeneralBVO) outbvos.get(0);
							// }
							generalb.setNshouldoutassistnum(saleorderbvo
									.getNpacknumber());// 应出辅数量
							generalb.setNoutnum(new UFDouble(noutnum));// 实出数量
							locatorvo.setNoutspacenum(new UFDouble(noutnum));
							generalb.setNoutassistnum(new UFDouble(
									noutassistnum));// 实出辅数量
							locatorvo.setNoutspaceassistnum(new UFDouble(
									noutassistnum));
							// 跟据来源单据表头主键和基本存货主键得到货位ID
							String sql_cspaceid = "select distinct cspaceid from tb_outgeneral_b where"
									+ " cfirstbillhid='"
									+ soSaleVO.getCsaleid()
									+ "' and cinventoryid='"
									+ saleorderbvo.getCinvbasdocid()
									+ "' and dr=0 ";
							ArrayList cspaceidList = (ArrayList) iuap
									.executeQuery(sql_cspaceid,
											new ArrayListProcessor());
							if (null != cspaceidList && cspaceidList.size() > 0) {
								Object[] cspaceid = (Object[]) cspaceidList
										.get(0);
								if (null != cspaceid[0]
										&& !"".equals(cspaceid[0])) {
									locatorvo.setCspaceid(cspaceid[0]
											.toString());// 货位ID
								}

							}
							generalb.setCastunitid(saleorderbvo
									.getCpackunitid());// 辅计量单位ID
							generalb.setNprice(saleorderbvo.getNprice());// 单价
							generalb.setNmny(saleorderbvo.getNmny());// 金额
							generalb.setCsourcebillhid(soSaleVO.getCsaleid());// 来源单据表头序列号
							generalb.setCfirstbillhid(soSaleVO.getCsaleid());// 源头单据表头ID
							generalb.setCfreezeid(soSaleVO.getCsaleid());// 锁定来源
							generalb.setCsourcebillbid(saleorderbvo
									.getCorder_bid());// 来源单据表体序列号
							generalb.setCfirstbillbid(saleorderbvo
									.getCorder_bid());// 源头单据表体ID
							generalb.setCsourcetype(soSaleVO.getCreceipttype());// 来源单据类型
							generalb.setCfirsttype(soSaleVO.getCreceipttype());// 源头单据类型
							generalb.setVsourcebillcode(soSaleVO
									.getVreceiptcode());// 来源单据号
							generalb.setVfirstbillcode(soSaleVO
									.getVreceiptcode());// 源头单据号
							generalb.setVsourcerowno(saleorderbvo.getCrowno());// 来源单据行号
							generalb.setVfirstrowno(saleorderbvo.getCrowno());// 源头单据行号
							generalb
									.setFlargess(saleorderbvo.getBlargessflag());// 是否赠品
							generalb.setDfirstbilldate(soSaleVO.getDmakedate());// 源头单据制单日期
							generalb.setCreceieveid(saleorderbvo
									.getCreceiptcorpid());// 收货单位
							generalb.setCrowno(saleorderbvo.getCrowno());// 行号
							generalb.setHsl(saleorderbvo.getNquoteunitrate());// 换算率
							generalb.setNsaleprice(saleorderbvo.getNnetprice());// 销售价格
							generalb.setNtaxprice(saleorderbvo.getNtaxprice());// 含税单价
							generalb.setNtaxmny(saleorderbvo.getNtaxmny());// 含税金额
							generalb.setNsalemny(saleorderbvo.getNmny());// 不含税金额
							generalb.setAttributeValue("cquotecurrency",
									saleorderbvo.getCcurrencytypeid());// 设置币种
							LocatorVO[] locatorVO = new LocatorVO[] { locatorvo };
							generalb.setLocator(locatorVO);
							if (isBatch)
								// 给出库子表集合添加对象
								generalBVOList.add(generalb);
						}
					}
				}
				// 转换数组
				generalBVO = new GeneralBillItemVO[generalBVOList.size()];
				generalBVO = generalBVOList.toArray(generalBVO);
				// 聚合VO表头赋值
				gBillVO.setParentVO(generalhvo);
				// 聚合VO表体赋值
				gBillVO.setChildrenVO(generalBVO);
			}
		}
		// }
		// }

		return gBillVO;
	}

	/**
	 * 取消签字
	 */
	@Override
	protected void onQxqz() throws Exception {
		// TODO Auto-generated method stub
		if (getBufferData().getCurrentRow() >= 0) {
			SoSaleVO generalh = (SoSaleVO) getBufferData().getCurrentVO()
					.getParentVO();
			Object result = iuap.retrieveByPK(SoSaleVO.class, generalh
					.getCsaleid());
			if (null != result) {
				generalh = (SoSaleVO) result;
				if (null != generalh.getVdef6()) {
					if (!"3".equals(generalh.getVdef6())) {
						getBillUI().showErrorMessage("该单据已经取消签字，取消失败");
						return;
					} else {
						generalh.setVdef6("2");
					}
				} else {
					getBillUI().showErrorMessage("该单据状态错误");
					return;
				}

				String strWhere = " select distinct cgeneralhid from ic_general_b where cfirstbillhid='"
						+ generalh.getCsaleid() + "' and dr=0 ";

				ArrayList batchList = (ArrayList) iuap.executeQuery(strWhere,
						new ArrayListProcessor());
				if (null != batchList && batchList.size() > 0) {
					Object[] batch = (Object[]) batchList.get(0);
					strWhere = " dr = 0 and cgeneralhid='" + batch[0] + "'";
				} else {
					getBillUI().showErrorMessage("该单据已经取消签字,取消失败");
					return;
				}

				ArrayList list = (ArrayList) iuap.retrieveByClause(
						IcGeneralHVO.class, strWhere);
				if (null != list && list.size() > 0) {
					IcGeneralHVO header = (IcGeneralHVO) list.get(0);
					if (null != header) {

						strWhere = " dr = 0 and cgeneralhid = '"
								+ header.getCgeneralhid() + "'";
						list = (ArrayList) iuap.retrieveByClause(
								IcGeneralBVO.class, strWhere);
						if (null != list && list.size() > 0) {
							IcGeneralBVO[] itemvo = new IcGeneralBVO[list
									.size()];
							GeneralBillItemVO[] generalbItem = new GeneralBillItemVO[itemvo.length];
							itemvo = (IcGeneralBVO[]) list.toArray(itemvo);
							AggregatedValueObject billvo = new GeneralBillVO();
							billvo
									.setParentVO(CommonUnit
											.setGeneralHVO(header));
							for (int i = 0; i < itemvo.length; i++) {
								generalbItem[i] = CommonUnit
										.setGeneralItemVO(itemvo[i]);
							}
							billvo.setChildrenVO(generalbItem);

							Iw80020206 iw = (Iw80020206) NCLocator
									.getInstance().lookup(
											Iw80020206.class.getName());
							iw.whs_processAction80020206("CANCELSIGN",
									"DELETE", "4C", _getDate().toString(),
									billvo, generalh);

							getBillUI().showHintMessage("取消签字成功");
							getButtonManager()
									.getButton(
											nc.ui.wds.w80020206.buttun0206.ISsButtun.Qzqr)
									.setEnabled(true);
							getButtonManager()
									.getButton(
											nc.ui.wds.w80020206.buttun0206.ISsButtun.Qxqz)
									.setEnabled(false);
							super.onBoRefresh();
							return;
						}
					}
				}
				getBillUI().showErrorMessage("签字失败,销售出库单已被删除");
				return;

			}
		}
	}

}