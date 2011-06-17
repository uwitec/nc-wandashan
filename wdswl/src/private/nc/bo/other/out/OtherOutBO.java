package nc.bo.other.out;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nc.bs.dao.BaseDAO;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.itf.scm.cenpur.service.TempTableUtil;
import nc.jdbc.framework.SQLParameter;
import nc.vo.ic.other.out.MyBillVO;
import nc.vo.ic.other.out.TbOutgeneralBVO;
import nc.vo.ic.other.out.TbOutgeneralTVO;
import nc.vo.ic.pub.StockInvOnHandVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.IBDACTION;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.WdsWlPubConsts;

/**
 * 
 * @author Administrator 其他出库后台类
 * 
 * 业务控制说明： 库存出库单 保存时 需要新增 托盘明细子子表数据 和 修改 托盘存量信息表数据 签字时 需要将托盘状态根据托盘存量进行 设置 0为空盘
 * 1为有货 删除存量为0的托盘存量数据 取消签字时 签字的逆操作 删除时 删除单据 删除托盘明细子子表 回复托盘存量信息表的存量 修改保存时
 * 先做删除时的操作，然后同新增 *
 * 
 */
public class OtherOutBO {

	private BaseDAO dao = null;

	BaseDAO getBaseDAO() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}

	private TempTableUtil ttutil = null;

	private TempTableUtil getTempTableUtil() {
		if (ttutil == null)
			ttutil = new TempTableUtil();
		return ttutil;
	}

	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 删除存量缓存表 * 回撤托盘存量表 回写来源单据数量
	 * @时间：2011-4-8下午03:30:07
	 * @param billVo
	 * @throws BusinessException
	 */
	public void deleteOutBill(MyBillVO billVo) throws BusinessException {
		if (billVo == null) {
			return;
		}
		TbOutgeneralBVO[] bodys = (TbOutgeneralBVO[]) billVo.getChildrenVO();
		if (bodys == null || bodys.length == 0)
			return;

		// 回写数量
		writeBack(billVo, IBDACTION.DELETE, false);

		List<TbOutgeneralTVO> ltray = new ArrayList<TbOutgeneralTVO>();
		for (TbOutgeneralBVO body : bodys) {
			if (body.getTrayInfor() == null)
				continue;
			ltray.addAll(body.getTrayInfor());
		}
		if (ltray.size() <= 0)
			return;
		deleteOtherInforOnDelBill(ltray);
	}

	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 完达山物流项目 暂时未考虑数量修改后的回写 出库单回写 其他出库回写：发运订单（WDS3），采购取样(WDSC)
	 *             销售出库回写：销售运单（WDS5）,erp红字销售订单(30)
	 * @时间：2011-4-8下午06:52:43
	 * @param newBillVo
	 *            要操作的单据数据
	 * @param iBdAction
	 *            操作状态
	 * @param isNew
	 *            是否新增保存
	 * @throws Exception
	 */
	public void writeBack(MyBillVO newBillVo, int iBdAction, boolean isNew)
			throws BusinessException {
		if (newBillVo == null || newBillVo.getParentVO() == null
				|| newBillVo.getChildrenVO() == null
				|| newBillVo.getChildrenVO().length == 0)
			return;
		TbOutgeneralBVO[] bodys = (TbOutgeneralBVO[]) newBillVo.getChildrenVO();
		if (bodys == null || bodys.length == 0)
			return;
		Map<String, UFDouble> numInfor = new HashMap<String, UFDouble>();
		Map<String, UFDouble> numassInfor = new HashMap<String, UFDouble>();

		String sourcetype = null;
		sourcetype = PuPubVO.getString_TrimZeroLenAsNull(bodys[0]
				.getCsourcetype());
		if (sourcetype == null)// 其他出库和销售出库自制的情况不需要回写
			return;
		if (iBdAction == IBDACTION.DELETE) {
			for (TbOutgeneralBVO body : bodys) {
				String key = body.getCsourcebillbid();
				if (sourcetype.equalsIgnoreCase(WdsWlPubConst.WDS3)
						|| sourcetype.equalsIgnoreCase(WdsWlPubConst.WDSC)
						|| sourcetype.equalsIgnoreCase("30")
						|| sourcetype.equalsIgnoreCase(WdsWlPubConst.WDS5)) {
					if(numInfor.containsKey(key)){
						UFDouble noutnum = numInfor.get(key);
						UFDouble noutassistnum = numassInfor.get(key);
						UFDouble nnewoutnum = PuPubVO
						.getUFDouble_NullAsZero(body.getNoutnum())
						.multiply(-1);
						UFDouble nnewoutassistnum = PuPubVO
						.getUFDouble_NullAsZero(body.getNoutassistnum())
						.multiply(-1);
						noutnum = noutnum.add(nnewoutnum);
						noutassistnum = noutassistnum.add(nnewoutassistnum);
						numInfor.put(key, noutnum);
						numassInfor.put(key, noutassistnum);
					}else{
						numInfor.put(key, PuPubVO
								.getUFDouble_NullAsZero(body.getNoutnum())
								.multiply(-1));
						numassInfor.put(key, PuPubVO
								.getUFDouble_NullAsZero(body.getNoutassistnum())
								.multiply(-1));
					}
					
				}
			}
		} else if (iBdAction == IBDACTION.SAVE) {
			if (isNew) {
				for (TbOutgeneralBVO body : bodys) {
					String key = body.getCsourcebillbid();
					if (sourcetype.equalsIgnoreCase(WdsWlPubConst.WDS3)
							|| sourcetype.equalsIgnoreCase(WdsWlPubConst.WDSC)
							|| sourcetype.equalsIgnoreCase("30")
							|| sourcetype.equalsIgnoreCase(WdsWlPubConst.WDS5)) {
					
						if(numInfor.containsKey(key)){
							UFDouble noutnum = numInfor.get(key);
							UFDouble noutassistnum = numassInfor.get(key);
							UFDouble nnewoutnum = PuPubVO
							.getUFDouble_NullAsZero(body.getNoutnum());
							UFDouble nnewoutassistnum = PuPubVO
							.getUFDouble_NullAsZero(body.getNoutassistnum());
							noutnum = noutnum.add(nnewoutnum);
							noutassistnum = noutassistnum.add(nnewoutassistnum);
							numInfor.put(key, noutnum);
							numassInfor.put(key, noutassistnum);
						}else{
							numInfor.put(key, PuPubVO
									.getUFDouble_NullAsZero(body.getNoutnum()));
							numassInfor.put(key, PuPubVO
									.getUFDouble_NullAsZero(body.getNoutassistnum()));
						}

					}
				}
			} else {
				String sql = "select noutnum,noutassistnum from tb_outgeneral_b where isnull(dr,0)=0 and general_b_pk = ?";
				SQLParameter para = null;
				for (TbOutgeneralBVO body : bodys) {
					String key = body.getCsourcebillbid();
					if (sourcetype.equalsIgnoreCase(WdsWlPubConst.WDS3)
							|| sourcetype.equalsIgnoreCase(WdsWlPubConst.WDSC)
							|| sourcetype.equalsIgnoreCase("30")
							|| sourcetype.equalsIgnoreCase(WdsWlPubConst.WDS5)) {
						if (body.getStatus() == VOStatus.NEW) {
							if(numInfor.containsKey(key)){
								UFDouble noutnum = numInfor.get(key);
								UFDouble nouassistnum = numassInfor.get(key);
								UFDouble nnewoutnum = PuPubVO
								.getUFDouble_NullAsZero(body.getNoutnum());
								UFDouble nnewoutassistnum = PuPubVO
								.getUFDouble_NullAsZero(body.getNoutassistnum());
								noutnum = noutnum.add(nnewoutnum);
								nouassistnum = nouassistnum.add(nnewoutassistnum);
								numInfor.put(key, noutnum);
								numassInfor.put(key, nouassistnum);
							}else{
								numInfor.put(key, PuPubVO
										.getUFDouble_NullAsZero(body.getNoutnum()));
								numassInfor.put(key, PuPubVO
										.getUFDouble_NullAsZero(body.getNoutassistnum()));
							}

						} else if (body.getStatus() == VOStatus.DELETED) {
							if(numInfor.containsKey(key)){
								UFDouble noutnum = numInfor.get(key);
								UFDouble nouassistnum = numassInfor.get(key);
								UFDouble nnewoutnum = PuPubVO
								.getUFDouble_NullAsZero(body.getNoutnum().multiply(-1));
								UFDouble nnewoutassistnum = PuPubVO
								.getUFDouble_NullAsZero(body.getNoutassistnum().multiply(-1));
								noutnum = noutnum.add(nnewoutnum);
								nouassistnum = nouassistnum.add(nnewoutassistnum);
								numInfor.put(key, noutnum);
								numassInfor.put(key, nouassistnum);
							}else{
								numInfor.put(body.getCsourcebillbid(), PuPubVO
										.getUFDouble_NullAsZero(body.getNoutnum())
										.multiply(-1));
								numassInfor.put(body.getCsourcebillbid(), PuPubVO
										.getUFDouble_NullAsZero(
												body.getNoutassistnum()).multiply(
												-1));
							}
							

						} else if (body.getStatus() == VOStatus.UPDATED) {
							UFDouble noldoutnum = null;
							UFDouble noutassistnum = null;
							// 取出原来的数量
							para = new SQLParameter();
							para.addParam(body.getPrimaryKey());
							Object o = getBaseDAO().executeQuery(sql, para,
									WdsPubResulSetProcesser.ARRAYLISTPROCESSOR);
							if (o != null) {
								ArrayList<Object[]> list = (ArrayList<Object[]>) o;
								if (list.size() == 0) {
									throw new BusinessException("获取原实出数量异常");
								}
								Object[] colum = (Object[]) list.get(0);
								noldoutnum = PuPubVO
										.getUFDouble_NullAsZero(colum[0]);
								noutassistnum = PuPubVO
										.getUFDouble_NullAsZero(colum[1]);
							}
							if(numInfor.containsKey(key)){
								UFDouble noutnum = numInfor.get(key);
								UFDouble nouassistnum = numassInfor.get(key);
								UFDouble nnewoutnum = PuPubVO
								.getUFDouble_NullAsZero(body.getNoutnum().sub(noldoutnum));
								UFDouble nnewoutassistnum = PuPubVO
								.getUFDouble_NullAsZero(body.getNoutassistnum().sub(noldoutnum));
								noutnum = noutnum.add(nnewoutnum);
								nouassistnum = nouassistnum.add(nnewoutassistnum);
								numInfor.put(key, noutnum);
								numassInfor.put(key, nouassistnum);
							}else{
								numInfor.put(body.getCsourcebillbid(), PuPubVO
										.getUFDouble_NullAsZero(body.getNoutnum())
										.sub(noldoutnum));
								numassInfor.put(body.getCsourcebillbid(), PuPubVO
										.getUFDouble_NullAsZero(
												body.getNoutassistnum()).sub(
												noutassistnum));
							}
						}
					}
				}
			}
		}

		if (numInfor.size() > 0) {
			String sql = null;
			if (sourcetype.equalsIgnoreCase(WdsWlPubConst.WDS3)) {
				sql = " update wds_sendorder_b set noutnum=coalesce(noutnum,0)+?,nassoutnum=coalesce(nassoutnum,0)+?"
						+ " where pk_sendorder_b=? ";
			} else if (sourcetype.equalsIgnoreCase(WdsWlPubConst.WDS5)) {
				sql = " update wds_soorder_b set noutnum=coalesce(noutnum,0)+? ,nassoutnum=coalesce(nassoutnum,0)+?"
						+ " where pk_soorder_b=? ";
			} else if (sourcetype.equalsIgnoreCase(WdsWlPubConst.WDSC)) {
				sql = " update wds_cgqy_b set noutnum=coalesce(noutnum,0)+? ,nassoutnum=coalesce(nassoutnum,0)+? "
						+ " where pk_cgqy_b=? ";
			} else if (sourcetype.equalsIgnoreCase("30")) {
				sql = "update so_saleorder_b set "
						+ WdsWlPubConst.DM_SO_DEALNUM_FIELD_NAME
						+ " = coalesce("
						+ WdsWlPubConst.DM_SO_DEALNUM_FIELD_NAME
						+ ",0)+? "
						+ " nasttaldcnum=coalesce(nasttaldcnum,0)+? where corder_bid=?";
			}
			SQLParameter para = null;
			for (String key : numInfor.keySet()) {
				if (para == null)
					para = new SQLParameter();
				else
					para.clearParams();
				para.addParam(numInfor.get(key));
				para.addParam(numassInfor.get(key));
				para.addParam(key);
				getBaseDAO().executeUpdate(sql, para);
				para.clearParams();
			}

			// 控制不能超数量发货
			checkNoutNumByOrderNum(sourcetype, numInfor.keySet().toArray(
					new String[0]));
		}
	}

	public void checkNoutNumByOrderNum(String sourcetype, String[] ids)
			throws BusinessException {
		String sql = null;
		if (sourcetype.equalsIgnoreCase(WdsWlPubConst.WDS3)) {
			sql = "select count(0) from wds_sendorder_b where (coalesce(ndealnum,0) - coalesce(noutnum,0)) < 0 "
					+ "and pk_sendorder_b in "
					+ getTempTableUtil().getSubSql(ids);
		} else if (sourcetype.equalsIgnoreCase(WdsWlPubConst.WDS5)) {
			sql = "select count(0) from wds_soorder_b where (coalesce(narrangnmu,0) - coalesce(noutnum,0)) < 0 "
					+ "and pk_soorder_b in "
					+ getTempTableUtil().getSubSql(ids);
		} else if (sourcetype.equalsIgnoreCase(WdsWlPubConst.WDSC)) {
			sql = "select count(0) from wds_cgqy_b where (coalesce(nplannum,0) - coalesce(noutnum,0)) < 0 "
					+ "and pk_cgqy_b in " + getTempTableUtil().getSubSql(ids);
		} else if (sourcetype.equalsIgnoreCase("30")) {
			sql = "select count(0) from so_saleorder_b where (coalesce(nnumber,0) - coalesce("
					+ WdsWlPubConst.DM_SO_DEALNUM_FIELD_NAME
					+ ",0)) < 0 "
					+ "and corder_bid in " + getTempTableUtil().getSubSql(ids);
		}
		if (PuPubVO.getInteger_NullAs(getBaseDAO().executeQuery(sql,
				WdsPubResulSetProcesser.COLUMNPROCESSOR), -1) > 0) {
			throw new BusinessException("超发运订单出库，请核实实出数量");
		}
	}

	public void deleteOtherInforOnDelBill(List<TbOutgeneralTVO> ltray)
			throws BusinessException {
		// 恢复托盘存量
		backTrayInforOnDelBill(ltray);
		// 删除单据托盘明细表
		if (ltray.size() > 0)// 数据无法回复 硬删除
			getBaseDAO().deleteVOList(ltray);
	}

	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 根据库存托盘明细数据 回复 托盘存量 用于 库存单据删除时
	 * @时间：2011-4-8下午04:00:16
	 * @param ltray
	 * @throws BusinessException
	 */
	public void backTrayInforOnDelBill(List<TbOutgeneralTVO> ltray)
			throws BusinessException {
		String whereSql = null;
		StockInvOnHandVO trayInv = null;
		if (ltray == null || ltray.size() == 0)
			return;
		List<StockInvOnHandVO> linvOnhand = new ArrayList<StockInvOnHandVO>();
		for (TbOutgeneralTVO tray : ltray) {
			whereSql = " pplpt_pk = '" + tray.getCdt_pk()
					+ "' and pk_invbasdoc = '" + tray.getPk_invbasdoc()
					+ "' and isnull(dr,0)=0 and whs_batchcode = '"
					+ tray.getVbatchcode() + "'";
			List<StockInvOnHandVO> linv = (List<StockInvOnHandVO>) getBaseDAO()
					.retrieveByClause(StockInvOnHandVO.class, whereSql);
			if (linv == null || linv.size() == 0) {
				throw new BusinessException("原货品托盘存放信息丢失，无法删除出库单");
			}

			if (linv.size() > 1)
				throw new BusinessException("获取原货品托盘存放信息异常");

			trayInv = linv.get(0);

			trayInv.setWhs_stockpieces(PuPubVO.getUFDouble_NullAsZero(
					trayInv.getWhs_stockpieces()).add(
					PuPubVO.getUFDouble_NullAsZero(tray.getNoutassistnum())));
			trayInv.setWhs_stocktonnage(PuPubVO.getUFDouble_NullAsZero(
					trayInv.getWhs_stocktonnage()).add(
					PuPubVO.getUFDouble_NullAsZero(tray.getNoutnum())));
			trayInv.setWhs_status(0);
			trayInv.setStatus(VOStatus.UPDATED);
			linvOnhand.add(trayInv);
			updateBdcargdocTray(tray.getCdt_pk(), 1);
		}
		if (linvOnhand.size() > 0)
			getBaseDAO().updateVOArray(
					linvOnhand.toArray(new StockInvOnHandVO[0]),
					WdsWlPubConsts.stockinvonhand_fieldnames);

	}

	// 更新托盘状态
	public void updateBdcargdocTray(String trayPK, int state)
			throws BusinessException {
		String sql = "update bd_cargdoc_tray set cdt_traystatus = " + state
				+ " where cdt_pk='" + trayPK + "'";
		getBaseDAO().executeUpdate(sql);
	}
}
