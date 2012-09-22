package nc.vo.wdsnew.pub;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import nc.bs.zmpub.pub.tool.stock.BillStockBO;
import nc.vo.ic.pub.StockInvOnHandVO;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wds.ic.cargtray.BdCargdocTrayVO;
import nc.vo.wl.pub.Wds2WlPubConst;
import nc.vo.wl.pub.WdsWlPubConst;

/**
 * 完达山项目 业务单据先存量更新类 其他出库 其他入库 调拨出库 调拨入库 都通过该入口 更新现存量 一般查询现存量 也通过该入口
 * 
 * @author mlr
 */
public class BillStockBO1 extends BillStockBO {
	private PickTool tool=null;
	public PickTool getTool(){
		if(tool==null){
			tool=new PickTool();
		}
		return tool;
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6683129237534928560L;
	/**
	 * 单据类型 ->交换类 对应关系
	 */
	private Map<String, String> typetoChangeclass = new HashMap<String, String>();
	/**
	 * 单据类型->现存量 数量变化规则
	 */
	private Map<String, UFBoolean[]> typetosetnum = new HashMap<String, UFBoolean[]>();
	/**
	 * 现存量 数量变化字段 库存主数量 库存辅数量
	 */
	private String[] changeNums = new String[] { "whs_stocktonnage",
			"whs_stockpieces" };
	/**
	 * 现存量实现类 路径
	 */
	private String className = "nc.vo.ic.pub.StockInvOnHandVO";
	/**
	 * 先存量定义的最小维度 维度为： 公司 仓库 货位 存货 批次 存货状态 入库日期
	 */
	private String[] def_fields = new String[] { "pk_corp", "pk_customize1",
			"pk_cargdoc", "pplpt_pk","pk_invmandoc", "pk_invbasdoc", "whs_batchcode",
			"ss_pk", "creadate" };

	@Override
	public Map<String, String> getTypetoChangeClass() throws Exception {
		if (typetoChangeclass.size() == 0) {
			typetoChangeclass.put(WdsWlPubConst.BILLTYPE_OTHER_IN,
					"nc.bs.wds.self.changedir.CHGWDS7TOACCOUNTNUM");// 处理其他入库保存
			typetoChangeclass.put(WdsWlPubConst.BILLTYPE_OTHER_IN_1,
					"nc.bs.wds.self.changedir.CHGWDS7TOACCOUNTNUM");// 处理其他入库删除
			typetoChangeclass.put(WdsWlPubConst.BILLTYPE_ALLO_IN,
					"nc.bs.wds.self.changedir.CHGWDS9TOACCOUNTNUM");// 处理调拨入库保存
			typetoChangeclass.put(WdsWlPubConst.BILLTYPE_ALLO_IN_1,
					"nc.bs.wds.self.changedir.CHGWDS9TOACCOUNTNUM");// 处理调拨入库删除
			typetoChangeclass.put(WdsWlPubConst.BILLTYPE_OTHER_OUT,
					"nc.bs.wds.self.changedir.CHGWDS6TOACCOUNTNUM");// 处理其他出库保存
			typetoChangeclass.put(WdsWlPubConst.BILLTYPE_OTHER_OUT_1,
					"nc.bs.wds.self.changedir.CHGWDS6TOACCOUNTNUM");// 处理其他出库删除
			typetoChangeclass.put(WdsWlPubConst.BILLTYPE_SALE_OUT,
					"nc.bs.wds.self.changedir.CHGWDS8TOACCOUNTNUM");// 处理销售出库保存
			typetoChangeclass.put(WdsWlPubConst.BILLTYPE_SALE_OUT_1,
					"nc.bs.wds.self.changedir.CHGWDS8TOACCOUNTNUM");// 处理销售出库删除
			typetoChangeclass.put(Wds2WlPubConst.billtype_statusupdate,
					"nc.bs.wds.self.changedir.CHGWS20TOACCOUNTNUM");// 处理状态变更单保存
																	// 状态变化前减少量
			typetoChangeclass.put(Wds2WlPubConst.billtype_statusupdate_1,
					"nc.bs.wds.self.changedir.CHGWS20TOACCOUNTNUM1");// 处理状态变更单保存
																		// 状态变化后
																		// 新增量
			typetoChangeclass.put(Wds2WlPubConst.billtype_statusupdate_2,
					"nc.bs.wds.self.changedir.CHGWS20TOACCOUNTNUM");// 处理状态变更单删除
																	// 状态变化前减少量
			typetoChangeclass.put(Wds2WlPubConst.billtype_statusupdate_3,
					"nc.bs.wds.self.changedir.CHGWS20TOACCOUNTNUM1");// 处理状态变更单删除
																		// 状态变化后
																		// 新增量
			typetoChangeclass.put(WdsWlPubConst.BILLTYPE_ALLO_OUT,
					"nc.bs.wds.self.changedir.CHGWDSHTOACCOUNTNUM");// 处理调拨出库保存
			typetoChangeclass.put(WdsWlPubConst.BILLTYPE_ALLO_OUT_1,
					"nc.bs.wds.self.changedir.CHGWDSHTOACCOUNTNUM");// 处理调拨出库删除
			typetoChangeclass.put(WdsWlPubConst.BILLTYPE_OUT_IN,
					"nc.bs.wds.self.changedir.CHGWDSZTOACCOUNTNUM");// 处理退货入库保存
			typetoChangeclass.put(WdsWlPubConst.BILLTYPE_OUT_IN_1,
					"nc.bs.wds.self.changedir.CHGWDSZTOACCOUNTNUM");// 处理退货入库删除

		}
		return typetoChangeclass;
	}

	@Override
	public Map<String, UFBoolean[]> getTypetosetnum() throws Exception {
		if (typetosetnum.size() == 0) {
			typetosetnum.put(WdsWlPubConst.BILLTYPE_OTHER_IN, new UFBoolean[] {
					new UFBoolean(false), new UFBoolean(false) });
			typetosetnum
					.put(WdsWlPubConst.BILLTYPE_OTHER_IN_1, new UFBoolean[] {
							new UFBoolean(true), new UFBoolean(true) });
			typetosetnum.put(WdsWlPubConst.BILLTYPE_ALLO_IN, new UFBoolean[] {
					new UFBoolean(false), new UFBoolean(false) });
			typetosetnum.put(WdsWlPubConst.BILLTYPE_ALLO_IN_1, new UFBoolean[] {
					new UFBoolean(true), new UFBoolean(true) });
			typetosetnum.put(WdsWlPubConst.BILLTYPE_OTHER_OUT, new UFBoolean[] {
					new UFBoolean(true), new UFBoolean(true) });
			typetosetnum.put(WdsWlPubConst.BILLTYPE_OTHER_OUT_1,
					new UFBoolean[] { new UFBoolean(false),
							new UFBoolean(false) });
			typetosetnum.put(WdsWlPubConst.BILLTYPE_SALE_OUT, new UFBoolean[] {
					new UFBoolean(true), new UFBoolean(true) });
			typetosetnum.put(WdsWlPubConst.BILLTYPE_SALE_OUT_1,
					new UFBoolean[] { new UFBoolean(false),
							new UFBoolean(false) });
			typetosetnum
					.put(Wds2WlPubConst.billtype_statusupdate, new UFBoolean[] {
							new UFBoolean(true), new UFBoolean(true) });
			typetosetnum.put(Wds2WlPubConst.billtype_statusupdate_1,
					new UFBoolean[] { new UFBoolean(false),
							new UFBoolean(false) });
			typetosetnum.put(Wds2WlPubConst.billtype_statusupdate_2,
					new UFBoolean[] { new UFBoolean(false),
							new UFBoolean(false) });
			typetosetnum
					.put(Wds2WlPubConst.billtype_statusupdate_3,
							new UFBoolean[] { new UFBoolean(true),
									new UFBoolean(true) });
			typetosetnum.put(WdsWlPubConst.BILLTYPE_ALLO_OUT, new UFBoolean[] {
					new UFBoolean(true), new UFBoolean(true) });
			typetosetnum.put(WdsWlPubConst.BILLTYPE_ALLO_OUT_1,
					new UFBoolean[] { new UFBoolean(false),
							new UFBoolean(false) });
			typetosetnum.put(WdsWlPubConst.BILLTYPE_OUT_IN, new UFBoolean[] {
					new UFBoolean(false), new UFBoolean(false) });
			typetosetnum.put(WdsWlPubConst.BILLTYPE_OUT_IN_1, new UFBoolean[] {
					new UFBoolean(true), new UFBoolean(true) });

		}
		return typetosetnum;
	}

	/**
	 * 通过where条件查询现存量
	 * 
	 * @param whereSql
	 * @return
	 */
	public SuperVO[] queryStock(String whereSql) throws Exception {
		String clname = getClassName();
		if (clname == null || clname.length() == 0)
			throw new Exception("没有注册现存量实现类全路径");
		Class cl = Class.forName(clname);
		Collection list = getDao().retrieveByClause(cl, whereSql);
		if (list == null || list.size() == 0)
			return null;
		SuperVO[] vos = (SuperVO[]) list
				.toArray((SuperVO[]) java.lang.reflect.Array.newInstance(cl,
						list.size()));
		return vos;

	}

	@Override
	public String[] getChangeNums() {

		return changeNums;
	}

	@Override
	public String getClassName() {

		return className;
	}

	@Override
	public String[] getDef_Fields() {

		return def_fields;
	}

	@Override
	public String getThisClassName() {

		return this.getClass().getName();
	}

	/**
	 * 根据传入的现存量vo 取出维度 查询现存量 SuperVO[] 存放每个查询维度查询出来的现存量(按查询维度合并后)
	 * 
	 * @throws Exception
	 * @作者：mlr
	 * @说明：完达山物流项目
	 * @时间：2012-7-2下午12:27:29
	 * 
	 */
	public SuperVO[] queryStockCombin1(SuperVO[] vos,String whereSql) throws Exception {
		return super.queryStockCombin1(vos,whereSql);
	}
	/**
	 * 根据传入的现存量vo 取出维度 查询现存量 SuperVO[] 存放每个查询维度查询出来的现存量(按查询维度合并后)
	 * 
	 * @throws Exception
	 * @作者：mlr
	 * @说明：完达山物流项目
	 * @时间：2012-7-2下午12:27:29
	 * 
	 */
	public SuperVO[] queryStockCombin(SuperVO[] vos) throws Exception {
		return super.queryStockCombin(vos);
	}
	/**
	 * @author mlr
	 * 现存量更新后 校验 是否超货架容量
	 * @param whereSql
	 * @return
	 * @throws Exception
	 */
	public void check(SuperVO[] vos1) throws Exception {
		super.check(vos1);
		StockInvOnHandVO[]  vos=(StockInvOnHandVO[]) vos1;
		if (vos == null || vos.length == 0)
			return;
		for (int i = 0; i < vos.length; i++) {
	
			//查询  货架现存来那个已存数量
			String pk_cargdoc = vos[i].getPk_cargdoc();
			String cdtpk = vos[i].getPplpt_pk();
			String pk_invmandoc = vos[i].getPk_invmandoc();
			String wheresql = " pk_cargdoc = '" + pk_cargdoc
					+ "' and pplpt_pk='" + cdtpk + "' and pk_invmandoc ='"
					+ pk_invmandoc
					+ "' and isnull(dr,0)=0 and  whs_stockpieces > 0 ";
			StockInvOnHandVO vo = new StockInvOnHandVO();
			vo.setPk_cargdoc(pk_cargdoc);
			vo.setPplpt_pk(cdtpk);
			vo.setPk_invmandoc(pk_invmandoc);
			StockInvOnHandVO[] ols = (StockInvOnHandVO[]) queryStockCombin(new StockInvOnHandVO[] { vo });
			
			
			if (ols == null || ols.length == 0)
				return;
			for (int j = 0; j < ols.length; j++) {
				StockInvOnHandVO stock=ols[j];
				//计算是否超货架容量
				BdCargdocTrayVO[] bvos=getTool().queryCat(stock.getPk_customize1(), stock.getPk_cargdoc(), stock.getPk_invmandoc(), stock.getPplpt_pk());
				//由于货架   货位+货架 是惟一的 所以只取第一个
				if(bvos==null || bvos.length==0)
					continue;
				BdCargdocTrayVO bvo=bvos[0];
				//获得存货 在一托盘上的 存放箱数
				UFDouble boxnum=PuPubVO.getUFDouble_NullAsZero(getTool().getInvVolume(stock.getPk_invmandoc()));
				//计算出 货架的总容量
				UFDouble znum=boxnum.multiply(PuPubVO.getUFDouble_NullAsZero(bvo.getNsize()));
				//从现存量得到 目前现存量
				UFDouble  xcnum=PuPubVO.getUFDouble_NullAsZero(stock.getWhs_stockpieces());
				if(xcnum.doubleValue()>znum.doubleValue()){
					String cdtcode=bvo.getCdt_traycode();
					throw new Exception("货架编码为 ："+cdtcode+" 的货架  存货超量");
				}
			}
		}
	}


}
