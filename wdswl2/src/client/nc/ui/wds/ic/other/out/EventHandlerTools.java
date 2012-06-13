package nc.ui.wds.ic.other.out;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.ui.pub.ClientEnvironment;
import nc.vo.ic.other.out.TbOutgeneralBVO;
import nc.vo.ic.other.out.TbOutgeneralHVO;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.locator.LocatorVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.scm.pu.PuPubVO;

public class EventHandlerTools {
	
	private IUAPQueryBS iuap = null;
	private IUAPQueryBS getQueryBO(){
		if(iuap == null){
			iuap = (IUAPQueryBS) NCLocator.getInstance().lookup(
					IUAPQueryBS.class.getName());
		}
		return iuap;
	}

	/**
	 * 转换 把当前页面中的VO转换成ERP中的出库单聚合OV 调用方法 进行回写ERP中出库单
	 * 
	 * @return ERP中出库单聚合VO
	 * @throws Exception
	 */
	protected GeneralBillVO changeReqOutgeneraltoGeneral(
			AggregatedValueObject obj) throws Exception {
		if (obj == null) {
			throw new BusinessException("请选择表头进行签字");
		}
		// 本地出库表 表头
		TbOutgeneralHVO tmphvo = (TbOutgeneralHVO) obj.getParentVO();
		// 本地出库表 表体
		TbOutgeneralBVO[] tmpbvo = null;
		// 出库聚合VO
		GeneralBillVO gBillVO = new GeneralBillVO();
		// 出库表头VO
		GeneralBillHeaderVO generalhvo = null;
		// 出库子表集合
		List<GeneralBillItemVO> generalBVOList = new ArrayList<GeneralBillItemVO>();
		// 出库子表VO数组
		GeneralBillItemVO[] generalBVO = null;
		String sWhere = " dr = 0 and general_pk = '" + tmphvo.getGeneral_pk()
				+ "'";
		ArrayList list = (ArrayList) getQueryBO().retrieveByClause(
				TbOutgeneralHVO.class, sWhere);
		if (null != list && list.size() > 0) {
			TbOutgeneralHVO outhvo = (TbOutgeneralHVO) list.get(0);
			generalhvo = new GeneralBillHeaderVO();

			if (null != outhvo) {
				// 给出库单表头赋值

				generalhvo.setPk_corp(outhvo.getPk_corp());// 公司主键
				generalhvo.setAttributeValue("cothercorpid", outhvo.getPk_corp());// 对方公司
				// generalhvo.setCbiztypeid(outhvo.getCbiztype());// 业务类型
				generalhvo.setCbilltypecode("4I");// 库存单据类型编码
				generalhvo.setVbillcode(outhvo.getVbillcode());// 单据号
				generalhvo.setDbilldate(outhvo.getDbilldate());// 单据日期
				generalhvo.setCwarehouseid(outhvo.getSrl_pk());// 仓库ID
				generalhvo.setAttributeValue("cotherwhid", outhvo.getSrl_pkr());// 其他仓库
				generalhvo.setCdispatcherid(outhvo.getCdispatcherid());// 收发类型outhvo.getCdispatcherid()
				generalhvo.setCdptid(outhvo.getCdptid());// 部门ID1021B110000000000BN9
				generalhvo.setCwhsmanagerid(outhvo.getCwhsmanagerid());// 库管员ID
				generalhvo.setCoperatorid(ClientEnvironment.getInstance()
						.getUser().getPrimaryKey());// 制单人
				generalhvo.setAttributeValue("coperatoridnow",
						ClientEnvironment.getInstance().getUser()
								.getPrimaryKey());// 操作人
				generalhvo.setVdiliveraddress(outhvo.getVdiliveraddress());// 发运地址
				generalhvo.setCbizid(outhvo.getCbizid());// 业务员ID
				generalhvo.setVnote(outhvo.getVnote());// 备注
				generalhvo.setFbillflag(2);// 单据状态
				generalhvo.setAttributeValue("clastmodiid", outhvo
						.getClastmodiid());// 最后修改人
				generalhvo.setAttributeValue("tlastmoditime", outhvo
						.getTlastmoditime());// 最后修改时间
				generalhvo
						.setAttributeValue("tmaketime", outhvo.getTmaketime());// 制单时间
				sWhere = " dr = 0 and general_pk = '" + tmphvo.getGeneral_pk()
						+ "'";
				list = (ArrayList) getQueryBO().retrieveByClause(TbOutgeneralBVO.class,
						sWhere);
				if (null != list && list.size() > 0) {
					tmpbvo = new TbOutgeneralBVO[list.size()];
					tmpbvo = (TbOutgeneralBVO[]) list.toArray(tmpbvo);
					// 给表体赋值
					for (int i = 0; i < tmpbvo.length; i++) {
						// 单据表体附表--货位
						LocatorVO locatorvo = new LocatorVO();
						locatorvo.setPk_corp(tmpbvo[i].getComp());
						boolean isBatch = false;
						GeneralBillItemVO generalb = new GeneralBillItemVO();
						generalb.setAttributeValue("pk_corp", outhvo.getPk_corp());// 公司

						generalb.setCinvbasid(tmpbvo[i].getCinventoryid());// 存货ID
						generalb.setVbatchcode(tmpbvo[i].getLvbatchcode());// 批次号
						// 查询生产日期和失效日期
						String sql = "select dproducedate ,dvalidate  from scm_batchcode where pk_invbasdoc='"
								+ tmpbvo[i].getCinventoryid()
								+ "' and vbatchcode='"
								+ tmpbvo[i].getLvbatchcode() + "' and dr=0";
						ArrayList batchList = (ArrayList) getQueryBO().executeQuery(
								sql, new ArrayListProcessor());
						if (null != batchList && batchList.size() > 0) {
							Object[] batch = (Object[]) batchList.get(0);
							// 生产日期
							if (null != batch[0] && !"".equals(batch[0]))
								generalb
										.setScrq(new UFDate(batch[0].toString()));
							// 失效日期
							if (null != batch[0] && !"".equals(batch[0]))
								generalb.setDvalidate(new UFDate(batch[1]
										.toString()));
							isBatch = true;
						}
						String pk_invmandoc = "select pk_invmandoc from bd_invmandoc  where pk_invbasdoc='"
								+ tmpbvo[i].getCinventoryid().trim()
								+ "' and pk_corp='"
								+ outhvo.getPk_corp()
								+ "' and dr=0 ";
						ArrayList tmpList = (ArrayList) getQueryBO().executeQuery(
								pk_invmandoc, new ArrayListProcessor());
						if (null != tmpList && tmpList.size() > 0) {
							Object[] a = (Object[]) tmpList.get(0);
							generalb.setCinventoryid(PuPubVO
									.getString_TrimZeroLenAsNull(a[0]));// 存货基本ID
						} else {
							isBatch = false;
						}
						generalb.setDbizdate(outhvo.getDbilldate());// 业务日期
						generalb.setNshouldoutnum(tmpbvo[i].getNshouldoutnum());// 应出数量
						generalb.setNshouldoutassistnum(tmpbvo[i]
								.getNshouldoutassistnum());// 应出辅数量
						generalb.setNoutnum(tmpbvo[i].getNoutnum());// 实出数量
						locatorvo.setNoutspacenum(tmpbvo[i].getNoutnum());
						generalb.setNoutassistnum(tmpbvo[i].getNoutassistnum());// 实出辅数量
						locatorvo.setNoutspaceassistnum(tmpbvo[i]
								.getNoutassistnum());
						locatorvo.setCspaceid(tmpbvo[i].getCspaceid());// 货位ID
						generalb.setCastunitid(tmpbvo[i].getCastunitid());// 辅计量单位ID
						generalb.setNprice(tmpbvo[i].getNprice());// 单价
						generalb.setNmny(tmpbvo[i].getNmny());// 金额
						generalb.setCsourcebillhid(tmpbvo[i]
								.getCsourcebillhid());// 来源单据表头序列号
						generalb.setCfirstbillhid(tmpbvo[i].getCfirstbillhid());// 源头单据表头ID
						generalb.setCfreezeid(tmpbvo[i].getCsourcebillhid());// 锁定来源
						generalb.setCsourcebillbid(tmpbvo[i]
								.getCsourcebillbid());// 来源单据表体序列号
						generalb.setCfirstbillbid(tmpbvo[i].getCfirstbillbid());// 源头单据表体ID
						// generalb.setCsourcetype();// 来源单据类型
						// generalb.setCfirsttype();// 源头单据类型
						generalb.setVsourcebillcode(tmpbvo[i]
								.getVsourcebillcode());// 来源单据号
						generalb.setVfirstbillcode(tmpbvo[i]
								.getVsourcebillcode());// 源头单据号
						generalb.setVsourcerowno(tmpbvo[i].getCrowno());// 来源单据行号
						generalb.setVfirstrowno(tmpbvo[i].getCrowno());// 源头单据行号
						generalb.setFlargess(tmpbvo[i].getFlargess());// 是否赠品
						generalb.setDfirstbilldate(tmpbvo[i]
								.getDfirstbilldate());// 源头单据制单日期
						generalb.setCrowno(tmpbvo[i].getCrowno());// 行号
						generalb.setHsl(tmpbvo[i].getHsl());// 换算率
						LocatorVO[] locatorVO = new LocatorVO[] { locatorvo };
						generalb.setLocator(locatorVO);
						if (isBatch)
							// 给出库子表集合添加对象
							generalBVOList.add(generalb);
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
		}

		return gBillVO;

	}
	
	/**
	 * 生成随机数20位 年月日小时分秒毫秒加上4位随机数
	 * 
	 * @return
	 */
	protected String getRandomNum() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmssms");
		String tmp = format.format(new Date());
		tmp = tmp + Math.round((Math.random() * 1000000));
		tmp = tmp.substring(0, 20);
		return tmp;
	}

}
