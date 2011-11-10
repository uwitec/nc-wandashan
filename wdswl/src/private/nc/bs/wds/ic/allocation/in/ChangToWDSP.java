package nc.bs.wds.ic.allocation.in;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.naming.NamingException;

import nc.bs.dao.BaseDAO;
import nc.bs.scm.pub.smart.SmartDMO;
import nc.bs.trade.business.HYPubBO;
import nc.vo.ic.other.in.OtherInBillVO;
import nc.vo.ic.pub.TbGeneralBVO;
import nc.vo.ic.pub.TbGeneralHVO;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.trade.pub.IExAggVO;
import nc.vo.wds.ic.write.back4y.MultiBillVO;
import nc.vo.wds.ic.write.back4y.Writeback4yB1VO;
import nc.vo.wds.ic.write.back4y.Writeback4yB2VO;
import nc.vo.wds.ic.write.back4y.Writeback4yHVO;
import nc.vo.wl.pub.WdsWlPubConst;

import org.apache.tools.ant.BuildException;

/**
 * 物流调拨入库单签字 推式生成调拨入库回传单
 * 
 * @author lxg
 * 
 */
public class ChangToWDSP {
	HYPubBO pubbo = null;

	HYPubBO getHypubBO() {
		if (pubbo == null) {
			pubbo = new HYPubBO();
		}
		return pubbo;
	}

	BaseDAO dao = null;

	BaseDAO getBaseDAO() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}

	private String coperator = null;
	private String pk_corp = null;
	private UFDate logDate = null;

	/**
	 * 
	 * @作者：lxg
	 * @说明：完达山物流项目 调拨入库单签字，生成调拨入库回传单
	 * @时间：2011-11-5下午13:50:05
	 * @param billVO
	 * @param coperator
	 *            操作员
	 * @param pk_crop
	 *            登录公司
	 * @param date
	 *            登录日期
	 * @return
	 * @throws BusinessException
	 */
	public void onSign(AggregatedValueObject billVO, String coperator,
			String pk_corp, String date) throws BusinessException {
		if (billVO == null) {
			return;
		}
		this.coperator = coperator;
		this.pk_corp = pk_corp;
		this.logDate = new UFDate(date);
		// 表体按照调拨出库来分组（当前一般只有一个调拨出库）<调拨出库单单主键，物流调拨入库单表体VO>
		HashMap<String, ArrayList<TbGeneralBVO>> map = new HashMap<String, ArrayList<TbGeneralBVO>>();
		TbGeneralHVO head = null;
		if (billVO instanceof IExAggVO) {
			OtherInBillVO bill = (OtherInBillVO) billVO;
			head = (TbGeneralHVO) bill.getParentVO();
			TbGeneralBVO[] bvos = (TbGeneralBVO[]) bill.getTableVO(bill
					.getTableCodes()[0]);
			if(bvos == null || bvos.length ==0){
				return ;
			}
			for (TbGeneralBVO bvo : bvos) {
				String key = PuPubVO.getString_TrimZeroLenAsNull(bvo
						.getGylbillhid());// getGylbillhid
				if (key == null) {
					continue;
				}
				if (map.containsKey(key)) {
					map.get(key).add(bvo);
				} else {
					ArrayList<TbGeneralBVO> list = new ArrayList<TbGeneralBVO>();
					list.add(bvo);
					map.put(key, list);
				}
			}
		}
		// 将 调拨入库单 转换 成调拨入库回传单
		if (map.size() == 0) {
			return;
		}
		for (String key : map.keySet()) {
			MultiBillVO writeBillVO = getMutiBillvo(key, head, map.get(key));
			// 将来源明细上的数量汇总加到出库汇总上（Writeback4yB2VO-->Writeback4yB1VO）
			sumNums(writeBillVO);
			getHypubBO().saveBill(writeBillVO);
		}
	}

	/**
	 * 
	 * @作者：lxg
	 * @说明：完达山物流项目 调拨入库单取消签字，生成调拨入库回传单
	 * @时间：2011-11-05下午14:37:05
	 * @param billVO
	 * @param coperator
	 *            操作员
	 * @param pk_crop
	 *            登录公司
	 * @param date
	 *            登录日期
	 * @return
	 * @throws BusinessException
	 */
	public void onCanclSign(AggregatedValueObject billVO, String coperator,
			String pk_corp, String date) throws BusinessException {

		if (billVO == null) {
			return;
		}
		this.coperator = coperator;
		this.pk_corp = pk_corp;
		this.logDate = new UFDate(date);
		// 查询调拨入库单对应的 下游单据
		TbGeneralHVO head = (TbGeneralHVO) billVO.getParentVO();
		StringBuffer bur = new StringBuffer();
		bur.append(" isnull(dr,0)=0  ");
		bur.append(" and pk_corp='" + pk_corp + "'");
		bur.append(" and pk_wds_writeback4Y_h in (");
		bur
				.append(" select distinct pk_wds_writeback4Y_h from wds_writeback4y_b2 ");
		bur.append(" where isnull(dr,0)=0 and csourcebillhid='"
				+ head.getPrimaryKey() + "'");
		bur.append(" )");
		Writeback4yHVO[] writeHvos = (Writeback4yHVO[]) getHypubBO()
				.queryByCondition(Writeback4yHVO.class, bur.toString());
		if (writeHvos != null && writeHvos.length > 0) {
			for (Writeback4yHVO writeHead : writeHvos) {
				Integer vbillstatus = PuPubVO.getInteger_NullAs(writeHvos[0]
						.getVbillstatus(), IBillStatus.FREE);
				if (vbillstatus == IBillStatus.CHECKPASS) {
					throw new BusinessException("下游调拨入库台账回写已经审批完成,不能再弃审");
				}
				MultiBillVO writeBillVO = new MultiBillVO();
				String where = " isnull(dr,0)=0 and pk_wds_writeback4Y_h = '"
						+ writeHead.getPrimaryKey() + "'";
				Writeback4yB1VO[] b1vos = (Writeback4yB1VO[]) getHypubBO()
						.queryByCondition(Writeback4yB1VO.class, where);
				Writeback4yB2VO[] b2vos = (Writeback4yB2VO[]) getHypubBO()
						.queryByCondition(Writeback4yB2VO.class, where);
				// 将由该调拨入库单生成 调拨入库回传来源明细 删除
				boolean isDel = false;
				int count = 0;
				if (b2vos != null) {
					for (Writeback4yB2VO b2vo : b2vos) {
						String csourcebillhid = b2vo.getCsourcebillhid();
						if (head.getPrimaryKey().equalsIgnoreCase(
								csourcebillhid)) {
							b2vo.setStatus(VOStatus.DELETED);
							count++;
						}
					}
				}
				if (count == b2vos.length)
					isDel = true;
				writeBillVO.setParentVO(writeHvos[0]);
				writeBillVO.setTableVO(writeBillVO.getTableCodes()[0], b1vos);
				writeBillVO.setTableVO(writeBillVO.getTableCodes()[1], b2vos);
				if (isDel) {
					getHypubBO().deleteBill(writeBillVO);
				} else {
					// 将来源明细上的数量汇总加到订单汇总上（Writeback4yB2VO-->Writeback4yB1VO）
					sumNumsDel(writeBillVO);
					getHypubBO().saveBill(writeBillVO);
				}
			}
		} else {
			throw new BuildException("查询下游调拨入库回传单据异常：未查询到数据");
		}
	}

	/**
	 * 
	 * @作者：lxg
	 * @说明：完达山物流项目
	 * @时间：2011-11-05下午14:50:05
	 * @param key
	 * @param head
	 * @param list
	 * @return
	 * @throws BusinessException
	 */
	public MultiBillVO getMutiBillvo(String key, TbGeneralHVO head,
			ArrayList<TbGeneralBVO> list) throws BusinessException {
		MultiBillVO writeBillVO = new MultiBillVO();
		GeneralBillVO generalVO = null;
		try {
			generalVO = getGeneralBill(key);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("查询调拨出库单失败");
		}
		GeneralBillHeaderVO generalHead = generalVO.getHeaderVO();
		GeneralBillItemVO[] generalBods = generalVO.getItemVOs();
		if (generalBods == null || generalBods.length == 0) {
			throw new BusinessException("查询调拨出库表体失败");
		}
		// 根据调拨出库 主键，查询是否已经生成调拨入库回传单（调拨入库回传单 表头保存调拨出库id,
		// 一一对应关系）
		String strWhere = " isnull(dr,0)=0 and cgeneralhid='" + key
				+ "' and pk_corp='" + pk_corp + "'";
		Writeback4yHVO[] writeHvos = (Writeback4yHVO[]) getHypubBO()
				.queryByCondition(Writeback4yHVO.class, strWhere);
		if (writeHvos != null && writeHvos.length > 0) {// --如果下游单据已存在
			Integer vbillstatus = PuPubVO.getInteger_NullAs(writeHvos[0]
					.getVbillstatus(), IBillStatus.FREE);
			if (vbillstatus == IBillStatus.CHECKPASS) {
				throw new BusinessException("下游调拨入库回写单已经审批完成,不能再审核");
			}
			String where = " isnull(dr,0)=0 and pk_wds_writeback4Y_h = '"
					+ writeHvos[0].getPrimaryKey() + "'";
			Writeback4yB1VO[] b1vos = (Writeback4yB1VO[]) getHypubBO()
					.queryByCondition(Writeback4yB1VO.class, where);
			Writeback4yB2VO[] b2vos = (Writeback4yB2VO[]) getHypubBO()
					.queryByCondition(Writeback4yB2VO.class, where);
			int i = 0;
			if (b2vos != null) {
				i = 10 * b2vos.length;
			}
			Writeback4yB2VO[] b2vosNew = getWriteBackB2vo(i, head, list);
			ArrayList<Writeback4yB2VO> b2list = new ArrayList<Writeback4yB2VO>();
			b2list.addAll(Arrays.asList(b2vos));
			b2list.addAll(Arrays.asList(b2vosNew));
			writeBillVO.setParentVO(writeHvos[0]);
			writeBillVO.setTableVO(writeBillVO.getTableCodes()[0], b1vos);
			writeBillVO.setTableVO(writeBillVO.getTableCodes()[1], b2list
					.toArray(new Writeback4yB2VO[0]));
		} else {// ---如果下游单据不存在
			Writeback4yHVO hvo = getWrirteBackHvo(key, generalHead.getVbillcode());
			Writeback4yB1VO[] b1vos = getWriteBackB1vo(generalHead, generalBods);
			Writeback4yB2VO[] b2vos = getWriteBackB2vo(0, head, list);
			writeBillVO.setParentVO(hvo);
			writeBillVO.setTableVO(writeBillVO.getTableCodes()[0], b1vos);
			writeBillVO.setTableVO(writeBillVO.getTableCodes()[1], b2vos);
		}
		return writeBillVO;
	}

	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目 :生成调拨入库回传单表头信息
	 * @时间：2011-11-9下午07:35:18
	 * @param cgeneralhid:ERP调拨入库单主键
	 * @param vbillno:erp调拨出库单单据号
	 * @return
	 */
	public Writeback4yHVO getWrirteBackHvo(String cgeneralhid, String vbillno) {
		Writeback4yHVO head = new Writeback4yHVO();
		head.setCgeneralhid(cgeneralhid);// 调拨出库单单据号
		head.setVbillno(vbillno);// 调拨出库单据号
		head.setVbillstatus(IBillStatus.FREE);// 单据状态
		head.setPk_billtype(WdsWlPubConst.WDSP);// 单据类型
		head.setStatus(VOStatus.NEW);
		head.setPk_corp(pk_corp);
		return head;
	}

	/**
	 * 
	 * @作者：lxg
	 * @说明：完达山物流项目 :根据调拨入库
	 * @时间：2011-10-26下午15:15:05
	 * @return
	 */
	public Writeback4yB1VO[] getWriteBackB1vo(GeneralBillHeaderVO generalHead,
			GeneralBillItemVO[] generalBods) {
		if (generalBods == null || generalBods.length == 0) {
			return null;
		}
		Writeback4yB1VO[] b1vos = new Writeback4yB1VO[generalBods.length];
		for (int i = 0; i < generalBods.length; i++) {
			b1vos[i] = new Writeback4yB1VO();
			b1vos[i].setCrowno("" + (i + 1) * 10);// 行号
			b1vos[i].setCfirstbillhid(generalBods[i].getCfirstbillhid());// 调拨订单信息
			b1vos[i].setCfirstbillbid(generalBods[i].getCfirstbillbid());
			b1vos[i].setCfirsttype(generalBods[i].getCfirsttype());
			b1vos[i].setVfirstbillcode(generalBods[i].getVfirstbillcode());
			b1vos[i].setCgeneralhid(generalBods[i].getCgeneralhid());// 调拨出库库信息
			b1vos[i].setCgeneralbid(generalBods[i].getCgeneralbid());
			b1vos[i].setVbillcode(generalHead.getVbillcode());
			b1vos[i].setCbilltypecode(generalHead.getCbilltypecode());
			b1vos[i].setPk_invmandoc(generalBods[i].getCinventoryid());// 存货管理id
			b1vos[i].setPk_invbasdoc(generalBods[i].getCinvmanid());// 存货基本id
			b1vos[i].setUnit(generalBods[i].getPk_measdoc());// 存货主计量单位
			b1vos[i].setAssunit(generalBods[i].getCastunitid());// 存货辅计量单位
			b1vos[i].setNnumber(generalBods[i].getNshouldoutnum());// 应出数量
			b1vos[i].setNpacknumber(generalBods[i].getNshouldoutassistnum());// 应出辅数量
			b1vos[i].setNoutnum(generalBods[i].getNoutnum());// 实出数量
			b1vos[i].setNoutassistnum(generalBods[i].getNoutassistnum());// 应出辅数量
		}
		return b1vos;
	}

	/**
	 * 
	 * @作者：lxg
	 * @说明：完达山物流项目
	 * @时间：2011-10-26下午15:22:05
	 * @param list
	 * @return
	 */
	public Writeback4yB2VO[] getWriteBackB2vo(int rowNo, TbGeneralHVO head,
			ArrayList<TbGeneralBVO> list) {
		if (list == null || list.size() == 0) {
			return null;
		}
		Writeback4yB2VO[] b2vos = new Writeback4yB2VO[list.size()];
		for (int i = 0; i < list.size(); i++) {
			b2vos[i] = new Writeback4yB2VO();
			rowNo = rowNo + 10;
			b2vos[i].setCrowno("" + rowNo);// 行号
			// 调拨订单信息
			b2vos[i].setCfirsttype(list.get(i).getCfirsttype());
			b2vos[i].setCfirstbillhid(list.get(i).getCfirstbillhid());
			b2vos[i].setCfirstbillbid(list.get(i).getCfirstbillbid());
			b2vos[i].setVfirstbillcode(list.get(i).getVfirstbillcode());
			// 调拨出库单信息
			b2vos[i].setCgeneralhid(list.get(i).getGylbillhid());
			b2vos[i].setCgeneralbid(list.get(i).getGylbillbid());
			b2vos[i].setVbillcode(list.get(i).getGylbillcode());
			b2vos[i].setCbilltypecode(list.get(i).getGylbilltype());
			// 物流调拨入库信息
			b2vos[i].setCsourcebillhid(list.get(i).getGeh_pk());
			b2vos[i].setCsourcebillbid(list.get(i).getGeb_pk());
			b2vos[i].setVsourcebillcode(head.getGeh_billcode());
			b2vos[i].setCsourcetype(WdsWlPubConst.BILLTYPE_ALLO_IN);
			b2vos[i].setPk_invmandoc(list.get(i).getGeb_cinventoryid());
			b2vos[i].setPk_invbasdoc(list.get(i).getGeb_cinvbasid());
			b2vos[i].setUnit(list.get(i).getCastunitid());
			b2vos[i].setAssunit(list.get(i).getCastunitid());
			b2vos[i].setNinnum(list.get(i).getGeb_anum());
			b2vos[i].setNinassistnum(list.get(i).getGeb_banum());
			b2vos[i].setStatus(VOStatus.NEW);
		}
		return b2vos;
	}

	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目 ：查询ERP调出出库单
	 * @时间：2011-11-9下午07:42:11
	 * @param primarykey
	 *            :ERP调拨出库单主键
	 * @return
	 * @throws BusinessException
	 * @throws Exception
	 * @throws NamingException
	 */
	public GeneralBillVO getGeneralBill(String primarykey)
			throws BusinessException, Exception, NamingException {
		if (primarykey == null) {
			primarykey = "";
		}
		GeneralBillHeaderVO headvo = new GeneralBillHeaderVO();
		GeneralBillItemVO[] itemvo = new GeneralBillItemVO[0];
		// 公司+单据类型+主键
		GeneralBillVO bill = new GeneralBillVO();
		SmartDMO dmo = null;
		dmo = new SmartDMO();
		String bodySql = " select * from ic_general_b where isnull(dr,0)=0 and cgeneralhid='"
				+ primarykey + "'";
		headvo = (GeneralBillHeaderVO) dmo.selectByKey(
				GeneralBillHeaderVO.class, primarykey);
		itemvo = (GeneralBillItemVO[]) dmo.selectBySql2(bodySql,
				GeneralBillItemVO.class);
		bill.setParentVO(headvo);
		bill.setChildrenVO(itemvo);
		return bill;
	}

	/**
	 * 
	 * @作者：lxg
	 * @说明：完达山物流项目 ：将明细数量汇总入库
	 * @时间：2011-10-27下午20:44:34
	 */
	private void sumNumsDel(MultiBillVO writeBillVO) {
		Writeback4yB1VO[] b1vos = (Writeback4yB1VO[]) writeBillVO
				.getTableVO(writeBillVO.getTableCodes()[0]);
		Writeback4yB2VO[] b2vos = (Writeback4yB2VO[]) writeBillVO
				.getTableVO(writeBillVO.getTableCodes()[1]);
		if (b2vos == null || b2vos.length == 0) {
			return;
		} else {
			for (Writeback4yB2VO b2vo : b2vos) {
				String cgeneralbid = PuPubVO.getString_TrimZeroLenAsNull(b2vo
						.getCgeneralbid());
				if (b2vo.getStatus() != VOStatus.DELETED)
					continue;
				UFDouble ninnum = PuPubVO.getUFDouble_NullAsZero(b2vo
						.getNinnum());
				UFDouble ninassistnum = PuPubVO.getUFDouble_NullAsZero(b2vo
						.getNinassistnum());
				for (Writeback4yB1VO b1vo : b1vos) {
					UFDouble ntotalInnum = PuPubVO.getUFDouble_NullAsZero(b1vo
							.getNinnum());// 
					UFDouble ntotalInassistnum = PuPubVO
							.getUFDouble_NullAsZero(b1vo.getNinassistnum());
					String cgeneralbid_total = b1vo.getCgeneralbid();
					if (cgeneralbid.equalsIgnoreCase(cgeneralbid_total)) {
						b1vo.setNinnum(ntotalInnum.add(ninnum));
						b1vo.setNinassistnum(ntotalInassistnum
								.add(ninassistnum));
						if (PuPubVO.getString_TrimZeroLenAsNull(b1vo
								.getPrimaryKey()) != null) {
							b1vo.setStatus(VOStatus.UPDATED);
						}
					}
				}
			}
		}
	}

	/**
	 * 
	 * @作者：lxg
	 * @说明：完达山物流项目 ：将明细数量汇总到入库汇总上
	 * @时间：2011-10-27下午20:20:34
	 */
	private void sumNums(MultiBillVO writeBillVO) {
		Writeback4yB1VO[] b1vos = (Writeback4yB1VO[]) writeBillVO
				.getTableVO(writeBillVO.getTableCodes()[0]);
		Writeback4yB2VO[] b2vos = (Writeback4yB2VO[]) writeBillVO
				.getTableVO(writeBillVO.getTableCodes()[1]);
		if (b2vos == null || b2vos.length == 0) {
			return;
		} else {
			for (Writeback4yB2VO b2vo : b2vos) {
				String cgeneralbid = PuPubVO.getString_TrimZeroLenAsNull(b2vo
						.getCgeneralbid());
				String primaryKey = PuPubVO.getString_TrimZeroLenAsNull(b2vo
						.getPrimaryKey());
				if (cgeneralbid == null || primaryKey != null)// 只将本次新增的明显信息
																// 汇总到 入库汇总上去
					continue;
				UFDouble ninnum = PuPubVO.getUFDouble_NullAsZero(b2vo
						.getNinnum());
				UFDouble ninassistnum = PuPubVO.getUFDouble_NullAsZero(b2vo
						.getNinassistnum());
				for (Writeback4yB1VO b1vo : b1vos) {
					UFDouble ntotalInnum = PuPubVO.getUFDouble_NullAsZero(b1vo
							.getNinnum());// 
					UFDouble ntotalInassistnum = PuPubVO
							.getUFDouble_NullAsZero(b1vo.getNinassistnum());
					String cgeneralbid_total = b1vo.getCgeneralbid();
					if (cgeneralbid.equalsIgnoreCase(cgeneralbid_total)) {
						b1vo.setNinnum(ntotalInnum.add(ninnum));
						b1vo.setNinassistnum(ntotalInassistnum
								.add(ninassistnum));
						if (PuPubVO.getString_TrimZeroLenAsNull(b1vo
								.getPrimaryKey()) != null) {
							b1vo.setStatus(VOStatus.UPDATED);
						}
					}
				}
			}
		}
	}

}
