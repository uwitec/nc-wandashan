package nc.vo.wdsnew.pub;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nc.bs.dao.BaseDAO;
import nc.jdbc.framework.util.SQLHelper;
import nc.ui.scm.util.ObjectUtils;
import nc.uif.pub.exception.UifException;
import nc.vo.ic.other.out.TbOutgeneralBVO;
import nc.vo.ic.pub.StockInvOnHandVO;
import nc.vo.ic.pub.TbGeneralBVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.voutils.VOUtil;
import nc.vo.wds.ic.cargtray.BdCargdocTrayVO;
import nc.vo.wds.invbasdoc.InvbasdocVO;
import nc.vo.wds.transfer.TransferBVO;
import nc.vo.zmpub.pub.tool.ZmPubTool;

/**
 * 完达山物流出入库单自动拣货
 * 
 * @author mlr
 */
public class PickTool implements Serializable {
	private static final long serialVersionUID = -6131447795689577612L;
	// 拣货单
	private Map<String, List<StockInvOnHandVO>> mpick = new HashMap<String, List<StockInvOnHandVO>>();
	private BillStockBO1 stock = null;
	private BaseDAO dao = null;

	private BaseDAO getDao() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}

	private BillStockBO1 getStock() {
		if (stock == null) {
			stock = new BillStockBO1();
		}
		return stock;
	}

	/**
	 * 该方法前台 必须远程调用 (查询数据库) 入库自动拣货
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目 1 根据 仓库 货位 存货 查询现存量 根据先进先出原则 拣货
	 * 
	 *             2捡完货后 形成拣货单 拣货单构成 是一个map key=出库单表体id ，value=现存量vo
	 * 
	 *             3重新构造拣货后的 入库单表体
	 * 
	 *             4 反回数据
	 * @时间：2012-6-20上午09:56:08
	 * @param pk_stordoc
	 * @param pk_cargdoc
	 * @param bvos
	 * @return
	 * @throws Exception
	 */
	public TbGeneralBVO[] autoPick2(String pk_stordoc, String pk_cargdoc,
			TbGeneralBVO[] bvos) throws Exception {
		if (pk_stordoc == null || pk_stordoc.length() == 0)
			throw new Exception("入库仓库为空");
		if (pk_cargdoc == null || pk_cargdoc.length() == 0)
			throw new Exception("入库货位为空");
		// 清空拣货单
		mpick.clear();
		if (bvos == null || bvos.length == 0)
			return null;
		for (int i = 0; i < bvos.length; i++) {
			//表头是 默认货位  真正拣货时  应该 按表体实际货位拣货
			String pk_car=PuPubVO.getString_TrimZeroLenAsNull(bvos[i].getGeb_space());
			if(pk_car!=null){
				pk_cargdoc=pk_car;
			}
			pick(pk_stordoc, pk_cargdoc, bvos[i], i);
			
		}
		return createBill(bvos);
	}

	/**
	 * 根据拣货单 重新构建出库单表体信息
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目
	 * @时间：2012-6-20上午10:43:38
	 * @return
	 * @throws Exception
	 */
	private TbGeneralBVO[] createBill(TbGeneralBVO[] bvos) throws Exception {
		List<TbGeneralBVO> list = new ArrayList<TbGeneralBVO>();// 存货重新构建的表体数据
		for (int i = 0; i < bvos.length; i++) {
			// 取出该行的拣货单
			List<StockInvOnHandVO> li = mpick.get(i + "");
			// 如果没有现存量 该行保持不动
			if (li == null || li.size() == 0) {
				list.add(bvos[i]);
			} else {
				// 否则 根据拣货单 重新构造 批次拆行后的表体
				for (int j = 0; j < li.size(); j++) {
					TbGeneralBVO vo = (TbGeneralBVO) ObjectUtils
							.serializableClone(bvos[i]);
					// vo.setGeb_vbatchcode(li.get(j).getWhs_batchcode());//
					// 设置批次
					// vo.setGeb_proddate(new
					// UFDate(getDate(li.get(j).getWhs_batchcode())));// 设置生成日期
					// vo.setCdt_pk(li.get(j).getSs_pk());// 设置存货状态
					vo.setAttributeValue("geb_bsnum", li.get(j)
							.getAttributeValue("whs_omnum"));// 设置应收辅数量
					vo.setAttributeValue("geb_banum", li.get(j)
							.getAttributeValue("whs_oanum"));// 设置实收辅数量
					// 设置货架信息
					vo.setAttributeValue("geb_customize4", li.get(j)
							.getAttributeValue("pplpt_pk"));// 设置货架id
					list.add(vo);
				}
			}
		}
		throw new StockException(list.toArray(new TbGeneralBVO[0]));
	}

	/**
	 * 前台必须远程调用 (查询数据库)
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目 按行拣货 根据 仓库 货位 存货 查询现存量拣货 拣货后的数据 放入拣货单
	 * @时间：2012-6-20上午10:04:23
	 * @param pk_stordoc
	 * @param pk_cargdoc
	 * @param i
	 * @param tbOutgeneralBVO
	 * @throws Exception
	 */
	private void pick(String pk_stordoc, String pk_cargdoc, TbGeneralBVO vo,
			int i) throws Exception {
		StringBuffer error = new StringBuffer();// 存放拣货错误信息
		check(vo);
		// 去货位货架绑定 根据 存货 +货位 查询货架
		BdCargdocTrayVO[] tvos = queryCat(pk_stordoc, pk_cargdoc,
				vo.geb_cinventoryid,vo.getGeb_customize4());
		sort1(tvos);// 按货架编码 从大到小排序
		if (tvos == null || tvos.length == 0)
			error.append("表体第  存货 没有货架 ,请去货位货架绑定  设置货架   && ");
		// 去现存量 根据 存货 +货位 + 货架编码 查询 货架已有存货
		// 构造现存量 vo 设置货位 存货 货架 货架已有箱数 货架剩余可容纳箱数
		StockInvOnHandVO[] stocks = setCatVolume(tvos, vo, pk_stordoc);
		if (stocks == null || stocks.length == 0) {
			if (error.toString().length() == 0) {
				error.append("表体第 存货 没有可用货架  &&");
			}
		}
		sort(stocks);// 按货架编码由大到小排序
		StockInvOnHandVO[] stocks1=spitNum(stocks);//过滤掉可容纳量为零的
		if(stocks!=null && stocks.length>0){
			if(stocks1==null || stocks1.length==0){
				error.append("表体第 存货 没有可用货架  &&");
			}
		}
		// 货架分量
		// 开始拣货
		// 拣货分量 构造拣货单
		spiltNum(stocks1, vo, i, error);
	}
    /**
     * 
     * @作者：zhf
     * @说明：完达山物流项目 
     * @时间：2012-9-12下午03:05:05
     * @param stocks
     * @return
     */
	private StockInvOnHandVO[] spitNum(StockInvOnHandVO[] stocks) {
		if(stocks==null || stocks.length==0)
			return null;
		List<StockInvOnHandVO> list=new ArrayList<StockInvOnHandVO>();
		for(int i=0;i<stocks.length;i++){
		   if(PuPubVO.getUFDouble_NullAsZero(stocks[i].getWhs_stockpieces()).doubleValue()>0){
			   list.add(stocks[i]);
		   }	
		}
		return list.toArray(new StockInvOnHandVO[0]);
	}

	private void sort1(BdCargdocTrayVO[] tvos) {
		if (tvos == null || tvos.length == 0)
			return;
		VOUtil.descSort(tvos, new String[] { "cdt_traycode" });
	}

	/**
	 * 按货架编码由大到小排序
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目
	 * @时间：2012-9-11下午02:48:55
	 * @param stocks1
	 */
	private void sort(StockInvOnHandVO[] stocks1) {
		if (stocks1 == null || stocks1.length == 0)
			return;
		VOUtil.descSort(stocks1, new String[] { "whs_customize3" });

	}

	/**
	 * 拣货分量
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目
	 * @时间：2012-6-20上午10:25:33
	 * @param stocks
	 *            现存量
	 * @param vo
	 *            出库单表体
	 * @throws Exception
	 */
	private void spiltNum(StockInvOnHandVO[] vos, TbGeneralBVO vo, int index,
			StringBuffer error) throws Exception {

		UFDouble zbnum = PuPubVO.getUFDouble_NullAsZero(vo.getGeb_bsnum());// 取得入库单应收辅数量
		UFDouble noutnum = PuPubVO.getUFDouble_NullAsZero(vo.getGeb_banum());// 获得实收数量
		vo.setVnote(null);
	    if(noutnum.doubleValue()>zbnum.doubleValue()){
	    	throw new Exception("表体行 第 "+(index+1)+"行 实收数量不能大于应收数量 ");
	    }
		
		if (vos == null || vos.length == 0) {
			mpick.put("" + index, null);// 如果现存量为空 则该行拣货单设置为空
			vo.setVnote(error.toString());
			return;
		}
		if (zbnum.doubleValue() == 0) {
			mpick.put("" + index, null);// 如果入库单应收辅数量为0 则该行拣货单设置为空
			error.append(" 应收数量不能为空 && ");
			vo.setVnote(error.toString());
			return;
		}
		if (noutnum.doubleValue() > 0) {
//			mpick.put("" + index, null);// 如果入库单实收数量有值 不再参与 自动拣货 则该行拣货单设置为空
//			error.append(" 存在实收数量的不能参与自动拣货 && ");
			zbnum=noutnum;
			vo.setVnote(error.toString());
		//	return;
		}
		// 进行分量
		// 按 批次号 由小到大 依次分量
		List<StockInvOnHandVO> list = new ArrayList<StockInvOnHandVO>();
		for (int i = 0; i < vos.length; i++) {
			UFDouble bnum = PuPubVO.getUFDouble_NullAsZero(vos[i]
					.getAttributeValue("whs_stockpieces")); // 货架可容纳量
			if (zbnum.doubleValue() > bnum.doubleValue()) {
				if (i == vos.length - 1) {
					vos[i].setAttributeValue("whs_omnum", zbnum);// 设置应收数量(辅数量)
					vos[i].setAttributeValue("whs_oanum", bnum);// 设置实收数量
				} else {
					zbnum = zbnum.sub(bnum);
					vos[i].setAttributeValue("whs_omnum", bnum);// 设置应收数量(辅数量)
					vos[i].setAttributeValue("whs_oanum", bnum);// 设置实收数量(辅数量)
				}
				list.add(vos[i]);
			} else if (zbnum.doubleValue() < bnum.doubleValue()) {
				vos[i].setAttributeValue("whs_omnum", zbnum);// 设置应收数量 (辅数量)
				vos[i].setAttributeValue("whs_oanum", zbnum);// 设置实收数量(辅数量)
				list.add(vos[i]);
				break;
			} else {
				vos[i].setAttributeValue("whs_omnum", zbnum);// 设置应收数量 (辅数量)
				vos[i].setAttributeValue("whs_oanum", zbnum);// 设置实收数量(辅数量)
				list.add(vos[i]);
				break;
			}
		}
		mpick.put(index + "", list);
		vo.setVnote(error.toString());
		updateStock1(list);
	}
	/**
	 * 根据 货架信息 得到 现存量信息
	 * 
	 * 循环便利 货架信息 逐个去现存量 按货架+仓库 +货位 +存货 维度 查询货架占用量 构造现存量vo 设置货架占用量 和 可用量
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目
	 * @时间：2012-9-11下午02:12:35
	 * @param tvos
	 * @param vo2
	 * @return
	 * @throws Exception
	 */
	private StockInvOnHandVO[] setCatVolume(BdCargdocTrayVO[] tvos,
			TbGeneralBVO vo2, String pk_stordoc) throws Exception {
		if (tvos == null || tvos.length == 0)
			return null;
		StockInvOnHandVO[] vos = new StockInvOnHandVO[tvos.length];// 构造现存量vo
		for (int i = 0; i < tvos.length; i++) {
			String pk_cargdoc = tvos[i].getPk_cargdoc();
			String cdtpk = tvos[i].getPrimaryKey();
			String pk_invmandoc = tvos[i].getCdt_invmandoc();
			String wheresql = " pk_cargdoc = '" + pk_cargdoc
					+ "' and pplpt_pk='" + cdtpk + "' and pk_invmandoc ='"
					+ pk_invmandoc
					+ "' and isnull(dr,0)=0 and  whs_stockpieces > 0 ";
			StockInvOnHandVO vo = new StockInvOnHandVO();
			vo.setPk_cargdoc(pk_cargdoc);
			vo.setPplpt_pk(cdtpk);
			vo.setPk_invmandoc(pk_invmandoc);
			StockInvOnHandVO[] stocks = (StockInvOnHandVO[]) getStock()
					.queryStockCombin(new StockInvOnHandVO[] { vo });
			if (stocks == null || stocks.length == 0) {
				// whs_stockpieces 货架可容纳量
				// whs_omnum 拣货已经占用字段
				// whs_oanum 拣货已经占用字段
				vos[i] = createStockVO(tvos[i], vo2, pk_stordoc);
			} else {
				vos[i] = createStockVO(tvos[i], stocks[0], vo2, pk_stordoc);
			}
		}
		return vos;
	}

	/**
	 * 根据货架信息 创建 现存量vo
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目
	 * @时间：2012-9-11下午05:17:34
	 * @param bdCargdocTrayVO
	 * @param stockInvOnHandVO
	 * @param pk_stordoc
	 * @return
	 * @throws Exception
	 */
	private StockInvOnHandVO createStockVO(BdCargdocTrayVO vo,
			StockInvOnHandVO st, TbGeneralBVO vo2, String pk_stordoc)
			throws Exception {
		StockInvOnHandVO stock = new StockInvOnHandVO();
		stock.setPk_corp(SQLHelper.getCorpPk());
		stock.setPplpt_pk(vo.getPrimaryKey());// 设置货架
		stock.setWhs_batchcode(vo2.getGeb_vbatchcode());// 设置批次号
		stock.setCreadate(new UFDate(getDate(vo2.getGeb_vbatchcode())));
		stock.setSs_pk(vo2.getCdt_pk());//设置存货状态
		stock.setPk_cargdoc(vo.getPk_cargdoc());// 设置货位
		stock.setPk_customize1(pk_stordoc);// 设置仓库
		stock.setPk_invbasdoc(vo.getCdt_invbasdoc()); // 设置货架绑定的存货
		stock.setPk_invmandoc(vo.getCdt_invmandoc());
		stock.setWhs_customize3(vo.getCdt_traycode());// 设置货架编码
		UFDouble yboxnum = PuPubVO.getUFDouble_NullAsZero(st
				.getWhs_stockpieces());// 货架已经占用量
		stock.setWhs_stocktonnage(yboxnum);// 设置货架已经占用量(箱)
		Integer size = vo.getNsize();// 获取货架中托盘数量
		if (size == null || size <= 0) {
			throw new Exception("货架编码 ：" + vo.getCdt_traycode() + " 容量没有设置 ");
		}
		UFDouble boxnum = getInvVolume(vo.getCdt_invmandoc());// 获取某个存货的托盘存放存货的箱数
		UFDouble zboxnum = boxnum.multiply(size);// 计算货架的总容量
		UFDouble kboxnum = zboxnum.sub(yboxnum);// 获得托盘可容纳量
		// 进行取整操作
		Integer tnum = (kboxnum.div(boxnum)).intValue();// 获得空托盘数量
		stock.setWhs_stockpieces(boxnum.multiply(tnum));// 设置货架可容纳量（箱）
		return stock;
	}

	/**
	 * 根据货架信息 创建 现存量vo
	 * 
	 * @param vo2
	 * @param stockInvOnHandVO
	 * @param pk_stordoc
	 * @作者：mlr
	 * @说明：完达山物流项目
	 * @时间：2012-9-11下午04:32:25
	 * @param bdCargdocTrayVO
	 * @return
	 * @throws Exception
	 */
	private StockInvOnHandVO createStockVO(BdCargdocTrayVO vo,
			TbGeneralBVO vo2, String pk_stordoc) throws Exception {
		StockInvOnHandVO stock = new StockInvOnHandVO();
		stock.setPk_corp(SQLHelper.getCorpPk());
		stock.setPplpt_pk(vo.getPrimaryKey());// 设置货架
		stock.setWhs_batchcode(vo2.getGeb_vbatchcode());// 设置批次号
		stock.setCreadate(new UFDate(getDate(vo2.getGeb_vbatchcode())));
		stock.setSs_pk(vo2.getCdt_pk());//设置存货状态
		stock.setPk_cargdoc(vo.getPk_cargdoc());// 设置货位
		stock.setWhs_customize3(vo.getCdt_traycode());// 设置货架编码
		stock.setPk_customize1(pk_stordoc);// 设置仓库
		stock.setPk_invbasdoc(vo.getCdt_invbasdoc()); // 设置货架绑定的存货
		stock.setPk_invmandoc(vo.getCdt_invmandoc());
		stock.setWhs_stocktonnage(new UFDouble(0));// 设置货架已经占用量(箱)
		Integer size = vo.getNsize();// 获取货架中托盘数量
		if (size == null || size <= 0) {
			throw new Exception("货架编码 ：" + vo.getCdt_traycode() + " 容量没有设置 ");
		}
		UFDouble boxnum = getInvVolume(vo.getCdt_invmandoc());// 获取某个存货的托盘存放存货的箱数
		stock.setWhs_stockpieces(boxnum.multiply(size));// 设置货架可容纳量（箱）
		return stock;
	}

	/**
	 * 获取某个存货的 托盘容量（箱）
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目
	 * @时间：2012-9-11下午04:51:17
	 * @param cdt_invmandoc
	 * @return
	 * @throws Exception
	 */
	public UFDouble getInvVolume(String cdt_invmandoc) throws Exception {
		String wheresql = " pk_invmandoc = '" + cdt_invmandoc
				+ "' and isnull(dr,0)=0 ";
		List list = (List) getDao().retrieveByClause(InvbasdocVO.class,
				wheresql);
		if (list == null || list.size() == 0)
			throw new Exception("存货档案没有维护对应存货信息");
		InvbasdocVO vo = (InvbasdocVO) list.get(0);
		

		if (PuPubVO.getUFDouble_NullAsZero(vo.getTray_volume()).doubleValue() <= 0) {
			String invode = (String) ZmPubTool
					.execFomular(
							"invcode->getColValue(bd_invbasdoc,invcode,pk_invbasdoc,pk_invbasdoc)",
							new String[] { "pk_invbasdoc" }, new String[] { vo
									.getPk_invbasdoc() });

			throw new Exception("存货编码：" + invode + " 没有维护托盘容量信息");
		}
		return PuPubVO.getUFDouble_NullAsZero(vo.getTray_volume());
	}

	/**
	 * 去货位存货绑定 查询货架信息
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目
	 * @时间：2012-9-11下午01:22:46
	 * @param pk_stordoc
	 * @param pk_cargdoc
	 * @param geb_cinventoryid
	 * @param cdtpk 
	 * @return
	 * @throws Exception
	 */
	public  BdCargdocTrayVO[] queryCat(String pk_stordoc, String pk_cargdoc,
			String geb_cinventoryid, String cdtpk) throws Exception {
		String wheresql = " pk_cargdoc='" + pk_cargdoc
				+ "' and  cdt_invmandoc ='" + geb_cinventoryid
				+ "' and isnull(dr,0)=0 ";
		if(cdtpk!=null && cdtpk.length()>0){
			wheresql =wheresql+" and cdt_pk ='"+cdtpk+"'";
		}

		List list = (List) getDao().retrieveByClause(BdCargdocTrayVO.class,
				wheresql);
		if (list == null || list.size() == 0)
			return null;
		return (BdCargdocTrayVO[]) list.toArray(new BdCargdocTrayVO[0]);
	}

	/**
	 * 自动拣货入库前 检验必填数据
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目
	 * @时间：2012-9-11下午12:00:46
	 * @param vo
	 * @throws Exception
	 */
	private void check(TbGeneralBVO vo) throws Exception {
		if (PuPubVO.getUFDouble_NullAsZero(vo.getGeb_bsnum()).doubleValue() <= 0) {
			throw new Exception("应收 辅数量必须大于零");
		}
		if (PuPubVO.getUFDouble_NullAsZero(vo.getGeb_snum()).doubleValue() <= 0) {
			throw new Exception("应收 主数量必须大于零");
		}
		if (vo.getGeb_vbatchcode() == null
				|| vo.getGeb_vbatchcode().length() == 0) {
			throw new Exception("批次不能为空");
		}
		if (vo.getCdt_pk() == null || vo.getCdt_pk().length() == 0) {
			throw new Exception("存货状态不能为空");
		}
	}

	/**
	 * 该方法前台 必须远程调用 (查询数据库) 出库自动拣货
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目 1 根据 仓库 货位 存货 查询现存量 根据先进先出原则 拣货
	 * 
	 *             2捡完货后 形成拣货单 拣货单构成 是一个map key=出库单表体id ，value=现存量vo
	 * 
	 *             3重新构造拣货后的 出库单表体
	 * 
	 *             4 反回数据
	 * @时间：2012-6-20上午09:56:08
	 * @param pk_stordoc
	 * @param pk_cargdoc
	 * @param bvos
	 * @return
	 * @throws Exception
	 */
	public TbOutgeneralBVO[] autoPick(String pk_stordoc, String pk_cargdoc,
			TbOutgeneralBVO[] bvos) throws Exception {
		if (pk_stordoc == null || pk_stordoc.length() == 0)
			throw new Exception("出库仓库为空");
		if (pk_cargdoc == null || pk_cargdoc.length() == 0)
			throw new Exception("出库货位为空");
		// 清空拣货单
		mpick.clear();
		if (bvos == null || bvos.length == 0)
			return null;
		for (int i = 0; i < bvos.length; i++) {
			pick(pk_stordoc, pk_cargdoc, bvos[i], i);
		}
		return createBill(bvos);
	}

	/**
	 * 根据拣货单 重新构建出库单表体信息
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目
	 * @时间：2012-6-20上午10:43:38
	 * @return
	 * @throws Exception
	 */
	private TransferBVO[] createBill(TransferBVO[] bvos) throws Exception {
		List<TransferBVO> list = new ArrayList<TransferBVO>();// 存货重新构建的表体数据
		for (int i = 0; i < bvos.length; i++) {
			// 取出该行的拣货单
			List<StockInvOnHandVO> li = mpick.get(i + "");
			// 如果没有现存量 该行保持不动
			if (li == null || li.size() == 0) {
				list.add(bvos[i]);
			} else {
				// 否则 根据拣货单 重新构造 批次拆行后的表体
				for (int j = 0; j < li.size(); j++) {
					TransferBVO vo = (TransferBVO) ObjectUtils
							.serializableClone(bvos[i]);
					vo.setVbatchcode(li.get(j).getWhs_batchcode());// 设置批次
					vo.setVuserdef7(getDate(li.get(j).getWhs_batchcode()));// 设置生成日期
					vo.setVuserdef9(li.get(j).getSs_pk());// 设置存货状态
					vo.setVuserdef8(li.get(j).getPplpt_pk());// 设置货架id
					vo.setAttributeValue("nshouldoutassistnum", li.get(j)
							.getAttributeValue("whs_omnum"));// 设置应发辅数量
					vo.setAttributeValue("noutassistnum", li.get(j)
							.getAttributeValue("whs_oanum"));// 设置实发辅数量
					list.add(vo);
				}
			}
		}
		throw new StockException(list.toArray(new TransferBVO[0]));
	}

	/**
	 * 根据拣货单 重新构建出库单表体信息
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目
	 * @时间：2012-6-20上午10:43:38
	 * @return
	 * @throws Exception
	 */
	private TbOutgeneralBVO[] createBill(TbOutgeneralBVO[] bvos)
			throws Exception {
		List<TbOutgeneralBVO> list = new ArrayList<TbOutgeneralBVO>();// 存货重新构建的表体数据
		for (int i = 0; i < bvos.length; i++) {
			// 取出该行的拣货单
			List<StockInvOnHandVO> li = mpick.get(i + "");
			// 如果没有现存量 该行保持不动
			if (li == null || li.size() == 0) {
				list.add(bvos[i]);
			} else {
				// 否则 根据拣货单 重新构造 批次拆行后的表体
				for (int j = 0; j < li.size(); j++) {
					TbOutgeneralBVO vo = (TbOutgeneralBVO) ObjectUtils
							.serializableClone(bvos[i]);
					vo.setVbatchcode(li.get(j).getWhs_batchcode());// 设置批次
					vo.setVuserdef7(getDate(li.get(j).getWhs_batchcode()));// 设置生成日期
					vo.setVuserdef9(li.get(j).getSs_pk());// 设置存货状态
					vo.setVuserdef8(li.get(j).getPplpt_pk());// 设置货架id
					vo.setAttributeValue("nshouldoutassistnum", li.get(j)
							.getAttributeValue("whs_omnum"));// 设置应发辅数量
					vo.setAttributeValue("noutassistnum", li.get(j)
							.getAttributeValue("whs_oanum"));// 设置实发辅数量
					list.add(vo);
				}
			}
		}
		throw new StockException(list.toArray(new TbOutgeneralBVO[0]));
	}

	/**
	 * 设置生产失效日期
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目
	 * @时间：2012-6-28下午02:07:45
	 * @param whs_batchcode
	 * @param row
	 * @throws UifException
	 */
	private String getDate(String va) {
		// 如果批次号输入格式正确就给生产日期赋值
		String year = va.substring(0, 4);
		String month = va.substring(4, 6);
		String day = va.substring(6, 8);
		String startdate = year + "-" + month + "-" + day;
		return startdate;
	}

	/**
	 * 前台必须远程调用 (查询数据库)
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目 按行拣货 根据 仓库 货位 存货 查询现存量拣货 拣货后的数据 放入拣货单
	 * @时间：2012-6-20上午10:04:23
	 * @param pk_stordoc
	 * @param pk_cargdoc
	 * @param i
	 * @param tbOutgeneralBVO
	 * @throws Exception
	 */
	private void pick(String pk_stordoc, String pk_cargdoc, TransferBVO vo,
			int i) throws Exception {
		//将错误提示信息 清空
		vo.setVuserdef14(null);
		// 构建查询条件
		String whereSql = " pk_customize1 = '" + pk_stordoc + "' "
				+ " and  pk_cargdoc = '" + vo.getPk_defdoc2() + "' "
				+ " and pk_invmandoc='" + vo.getCinventoryid() + "'"
				+ " and isnull(dr,0)=0 " + " and pk_corp='"
				+ SQLHelper.getCorpPk() + "'" + " and whs_stockpieces >0 ";// 库存主数量大于0
		String vbantcode = PuPubVO.getString_TrimZeroLenAsNull(vo
				.getVbatchcode());// 获得批次号
		String sspk = PuPubVO.getString_TrimZeroLenAsNull(vo.getVuserdef9());// 获得存货状态

		String cdtpk = PuPubVO.getString_TrimZeroLenAsNull(vo.getVuserdef8());// 获取货架id

		if (sspk != null) {
			whereSql = whereSql + " and ss_pk='" + sspk + "'";
		}
		if (vbantcode != null) {
			whereSql = whereSql + " and whs_batchcode='" + vbantcode + "'";
		}
		if (cdtpk != null) {
			whereSql = whereSql + " and pplpt_pk='" + cdtpk + "'";

		}
		// 查询现存量
		StockInvOnHandVO[] stocks = (StockInvOnHandVO[]) getStock().queryStock(
				whereSql);
		// 开始拣货
		// 拣货分量 构造拣货单
		spiltNum(stocks, vo, i);
	}

	/**
	 * 前台必须远程调用 (查询数据库)
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目 按行拣货 根据 仓库 货位 存货 查询现存量拣货 拣货后的数据 放入拣货单
	 * @时间：2012-6-20上午10:04:23
	 * @param pk_stordoc
	 * @param pk_cargdoc
	 * @param i
	 * @param tbOutgeneralBVO
	 * @throws Exception
	 */
	private void pick(String pk_stordoc, String pk_cargdoc, TbOutgeneralBVO vo,
			int i) throws Exception {
		//将错误提示信息 清空
		vo.setVuserdef14(null);
		// 构建查询条件
		String whereSql = " pk_customize1 = '" + pk_stordoc + "' "
				+ " and  pk_cargdoc = '" + pk_cargdoc + "' "
				+ " and pk_invmandoc='" + vo.getCinventoryid() + "'"
				+ " and isnull(dr,0)=0 " + " and pk_corp='"
				+ SQLHelper.getCorpPk() + "'" + " and whs_stockpieces >0 ";// 库存主数量大于0
		String vbantcode = PuPubVO.getString_TrimZeroLenAsNull(vo
				.getVbatchcode());// 获得批次号
		String sspk = PuPubVO.getString_TrimZeroLenAsNull(vo.getVuserdef9());// 获得存货状态
		String cdtpk = PuPubVO.getString_TrimZeroLenAsNull(vo.getVuserdef8());// 获取货架id

		if (sspk != null) {
			whereSql = whereSql + " and ss_pk='" + sspk + "'";
		}
		if (vbantcode != null) {
			whereSql = whereSql + " and whs_batchcode='" + vbantcode + "'";
		}
		if (cdtpk != null) {
			whereSql = whereSql + " and pplpt_pk='" + cdtpk + "'";

		}
		// 查询现存量
		StockInvOnHandVO[] stocks = (StockInvOnHandVO[]) getStock().queryStock(
				whereSql);
		sortout(stocks);//出库按 批次由小到大  按货架编码由小到大排序
		// 开始拣货
		// 拣货分量 构造拣货单
		spiltNum(stocks, vo, i);
	}
    /**
     * 出库按 批次由小到大  按货架编码由小到大排序
     * @作者：mlr
     * @说明：完达山物流项目 
     * @时间：2012-9-13上午11:18:39
     * @param stocks
     * @throws BusinessException 
     */
	private void sortout(StockInvOnHandVO[] stocks) throws BusinessException {
       if(stocks==null || stocks.length==0)
    	   return ;
       //设置货架编码
       for(int i=0;i<stocks.length;i++){
    	   String cdtpk=stocks[i].getPplpt_pk();
    	   String cdtcode=(String) ZmPubTool.execFomular("code->getColValue(bd_cargdoc_tray,cdt_traycode,cdt_pk,cdt_pk)", new String[]{"cdt_pk"}, new String[]{cdtpk});
    	   stocks[i].setWhs_customize3(cdtcode);
       }
       VOUtil.ascSort(stocks,new String[]{"whs_batchcode","whs_customize3"});//按批次号  和货架编码排序	
	}

	/**
	 * 拣货分量
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目
	 * @时间：2012-6-20上午10:25:33
	 * @param stocks
	 *            现存量
	 * @param vo
	 *            出库单表体
	 * @throws Exception
	 */
	private void spiltNum(StockInvOnHandVO[] vos, TransferBVO vo, int index)
			throws Exception {
		StringBuffer error = new StringBuffer();// 存货拣货错误 或者提示信息
		UFDouble zbnum = PuPubVO.getUFDouble_NullAsZero(vo
				.getNshouldoutassistnum());// 取得出库单应发辅数量
		UFDouble noutnum = PuPubVO.getUFDouble_NullAsZero(vo.getNoutnum());// 获得实发数量
		if (vos == null || vos.length == 0) {
			mpick.put("" + index, null);// 如果现存量为空 则该行拣货单设置为空
			error.append(" 没有现存量 &&");
			vo.setVuserdef14(error.toString());
			return;
		}
		if (zbnum.doubleValue() == 0) {
			mpick.put("" + index, null);// 如果出库单应发辅数量为0 则该行拣货单设置为空
			error.append(" 应发数量不能为空 &&");
			vo.setVuserdef14(error.toString());
			return;
		}
		if (noutnum.doubleValue() > 0) {
			mpick.put("" + index, null);// 如果出库单实发数量有值 不再参与 自动拣货 则该行拣货单设置为空
			error.append("存在实发数量不能参与自动拣货 ");
			vo.setVuserdef14(error.toString());
			return;
		}
		// 进行分量
		// 按 批次号 由小到大 依次分量
		List<StockInvOnHandVO> list = new ArrayList<StockInvOnHandVO>();
		for (int i = 0; i < vos.length; i++) {
			UFDouble bnum = PuPubVO.getUFDouble_NullAsZero(vos[i]
					.getAttributeValue("whs_stockpieces"));
			if (zbnum.doubleValue() > bnum.doubleValue()) {
				if (i == vos.length - 1) {
					vos[i].setAttributeValue("whs_omnum", zbnum);// 设置应发数量(辅数量)
					vos[i].setAttributeValue("whs_oanum", bnum);// 设置实发数量
				} else {
					zbnum = zbnum.sub(bnum);
					vos[i].setAttributeValue("whs_omnum", bnum);// 设置应发数量(辅数量)
					vos[i].setAttributeValue("whs_oanum", bnum);// 设置实发数量(辅数量)
				}
				list.add(vos[i]);
			} else if (zbnum.doubleValue() < bnum.doubleValue()) {
				vos[i].setAttributeValue("whs_omnum", zbnum);// 设置应发数量 (辅数量)
				vos[i].setAttributeValue("whs_oanum", zbnum);// 设置实发数量(辅数量)
				list.add(vos[i]);
				break;
			} else {
				vos[i].setAttributeValue("whs_omnum", zbnum);// 设置应发数量 (辅数量)
				vos[i].setAttributeValue("whs_oanum", zbnum);// 设置实发数量(辅数量)
				list.add(vos[i]);
				break;
			}
		}
		mpick.put(index + "", list);
		updateStock(list);
	}

	/**
	 * 拣货分量
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目
	 * @时间：2012-6-20上午10:25:33
	 * @param stocks
	 *            现存量
	 * @param vo
	 *            出库单表体
	 * @throws Exception
	 */
	private void spiltNum(StockInvOnHandVO[] vos, TbOutgeneralBVO vo, int index)
			throws Exception {
        StringBuffer error=new StringBuffer();//存货拣货错误 或者提示信息
		UFDouble zbnum = PuPubVO.getUFDouble_NullAsZero(vo
				.getNshouldoutassistnum());// 取得出库单应发辅数量
		UFDouble noutnum = PuPubVO.getUFDouble_NullAsZero(vo.getNoutnum());// 获得实发数量
		if (vos == null || vos.length == 0) {
			mpick.put("" + index, null);// 如果现存量为空 则该行拣货单设置为空
			error.append(" 没有现存量 &&");
			vo.setVuserdef14(error.toString());
			return;
		}
		if (zbnum.doubleValue() == 0) {
			mpick.put("" + index, null);// 如果出库单应发辅数量为0 则该行拣货单设置为空
			error.append(" 应发数量不能为空 &&");
			vo.setVuserdef14(error.toString());
			return;
		}
		if (noutnum.doubleValue() > 0) {
			mpick.put("" + index, null);// 如果出库单实发数量有值 不再参与 自动拣货 则该行拣货单设置为空
			error.append("存在实发数量不能参与自动拣货 ");
			vo.setVuserdef14(error.toString());
			return;
		}
		// 进行分量
		// 按 批次号 由小到大 依次分量
		List<StockInvOnHandVO> list = new ArrayList<StockInvOnHandVO>();
		for (int i = 0; i < vos.length; i++) {
			UFDouble bnum = PuPubVO.getUFDouble_NullAsZero(vos[i]
					.getAttributeValue("whs_stockpieces"));
			if (zbnum.doubleValue() > bnum.doubleValue()) {
				if (i == vos.length - 1) {
					vos[i].setAttributeValue("whs_omnum", zbnum);// 设置应发数量(辅数量)
					vos[i].setAttributeValue("whs_oanum", bnum);// 设置实发数量
				} else {
					zbnum = zbnum.sub(bnum);
					vos[i].setAttributeValue("whs_omnum", bnum);// 设置应发数量(辅数量)
					vos[i].setAttributeValue("whs_oanum", bnum);// 设置实发数量(辅数量)
				}
				list.add(vos[i]);
			} else if (zbnum.doubleValue() < bnum.doubleValue()) {
				vos[i].setAttributeValue("whs_omnum", zbnum);// 设置应发数量 (辅数量)
				vos[i].setAttributeValue("whs_oanum", zbnum);// 设置实发数量(辅数量)
				list.add(vos[i]);
				break;
			} else {
				vos[i].setAttributeValue("whs_omnum", zbnum);// 设置应发数量 (辅数量)
				vos[i].setAttributeValue("whs_oanum", zbnum);// 设置实发数量(辅数量)
				list.add(vos[i]);
				break;
			}
		}
		mpick.put(index + "", list);
		updateStock(list);
	}

	/**
	 * 入库更新现存量
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目
	 * @时间：2012-7-23下午04:38:38
	 * @param list
	 * @throws Exception
	 */
	private void updateStock1(List<StockInvOnHandVO> lis) throws Exception {
		List<StockInvOnHandVO> list = (List<StockInvOnHandVO>) ObjectUtils
				.serializableClone(lis);
		if (list == null || list.size() == 0)
			return;
		for (int i = 0; i < list.size(); i++) {
			StockInvOnHandVO vo = list.get(i);
			if (vo == null)
				return;
			UFDouble uf1 = PuPubVO.getUFDouble_NullAsZero(vo.getWhs_oanum());
			vo.setWhs_stockpieces(uf1);
		}
		getStock().updateStock(list.toArray(new StockInvOnHandVO[0]));
	}

	/**
	 * 出库更新现存量
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目
	 * @时间：2012-7-23下午04:38:38
	 * @param list
	 * @throws Exception
	 */
	private void updateStock(List<StockInvOnHandVO> lis) throws Exception {
		List<StockInvOnHandVO> list = (List<StockInvOnHandVO>) ObjectUtils
				.serializableClone(lis);
		if (list == null || list.size() == 0)
			return;
		for (int i = 0; i < list.size(); i++) {
			StockInvOnHandVO vo = list.get(i);
			if (vo == null)
				return;
			UFDouble uf1 = PuPubVO.getUFDouble_NullAsZero(vo.getWhs_oanum());
			vo.setWhs_stockpieces(new UFDouble(0).sub(uf1));
		}
		getStock().updateStock(list.toArray(new StockInvOnHandVO[0]));
	}

	/**
	 * 拣货分量
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目 zhf modify存货状态调整单特殊处理 特殊支持
	 * @时间：2012-6-20上午11:08:10
	 * @param vos
	 * @param row
	 * @param zbnum
	 */
	public void spiltNum(List<StockInvOnHandVO> vos, int row, UFDouble zbnum,
			boolean isfromic) {
		// List<StockInvOnHandVO> list=new ArrayList<StockInvOnHandVO>();
		int len = vos.size();
		for (int i = 0; i < len; i++) {
			UFDouble bnum = PuPubVO.getUFDouble_NullAsZero(vos.get(i)
					.getAttributeValue("whs_stockpieces"));

			// 为zhf存货状态调整单特殊处理 特殊支持
			if (PuPubVO.getUFDouble_NullAsZero(zbnum).equals(new UFDouble(0.0))
					&& !isfromic) {
				vos.get(i).setAttributeValue("whs_omnum", bnum);// 设置应发数量 //
				// (辅数量)
				vos.get(i).setAttributeValue("whs_oanum", bnum);// 设置实发数量(辅数量)
				continue;
			}

			if (zbnum.doubleValue() > bnum.doubleValue()) {
				if (i == len - 1) {
					vos.get(i).setAttributeValue("whs_omnum", zbnum);// 设置应发数量
					// //
					// (辅数量)
					vos.get(i).setAttributeValue("whs_oanum", bnum);// 设置实发数量(辅数量)
				} else {
					zbnum = zbnum.sub(bnum);
					vos.get(i).setAttributeValue("whs_omnum", bnum);// 设置应发数量 //
					// (辅数量)
					vos.get(i).setAttributeValue("whs_oanum", bnum);// 设置实发数量(辅数量)
				}
			} else {
				vos.get(i).setAttributeValue("whs_omnum", zbnum);// 设置应发数量 (辅数量)
				vos.get(i).setAttributeValue("whs_oanum", zbnum);// 设置实发数量(辅数量)
				break;
			}
		}
	}

	/**
	 * 该方法前台 必须远程调用 (查询数据库)
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目 1 根据 仓库 货位 存货 查询现存量 根据先进先出原则 拣货
	 * 
	 *             2捡完货后 形成拣货单 拣货单构成 是一个map key=出库单表体id ，value=现存量vo
	 * 
	 *             3重新构造拣货后的 出库单表体
	 * 
	 *             4 反回数据
	 * @时间：2012-6-20上午09:56:08
	 * @param pk_stordoc
	 * @param pk_cargdoc
	 * @param bvos
	 * @return
	 * @throws Exception
	 */
	public TransferBVO[] autoPick1(String pk_stordoc, String pk_cargdoc,
			TransferBVO[] bvos) throws Exception {
		if (pk_stordoc == null || pk_stordoc.length() == 0)
			throw new Exception("出库仓库为空");
		if (pk_cargdoc == null || pk_cargdoc.length() == 0)
			throw new Exception("出库货位为空");
		// 清空拣货单
		mpick.clear();
		if (bvos == null || bvos.length == 0)
			return null;
		for (int i = 0; i < bvos.length; i++) {
			//表头是 默认货位  真正拣货时  应该 按表体实际货位拣货
			String pk_car=PuPubVO.getString_TrimZeroLenAsNull(bvos[i].getPk_defdoc2());
			if(pk_car!=null){
				pk_cargdoc=pk_car;
			}
			pick(pk_stordoc, pk_cargdoc, bvos[i], i);
		}
		return createBill(bvos);
	}

	/**
	 * 拣货分量
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目
	 * @时间：2012-6-20上午11:08:10
	 * @param vos
	 * @param row
	 * @param zbnum
	 */
	public void spiltNum(List<StockInvOnHandVO> vos, int row, UFDouble zbnum) {
		spiltNum(vos, row, zbnum, true);
	}
}
