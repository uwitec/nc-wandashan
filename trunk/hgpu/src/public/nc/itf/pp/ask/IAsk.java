package nc.itf.pp.ask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import nc.ui.pub.ClientEnvironment;
import nc.vo.pp.ask.AskbillHeaderVO;
import nc.vo.pp.ask.AskbillMergeVO;
import nc.vo.pp.ask.EffectPriceParaVO;
import nc.vo.pp.ask.EffectPriceVO;
import nc.vo.pp.ask.PriceauditHeaderVO;
import nc.vo.pp.ask.VendorInvPriceVO;
import nc.vo.pp.ask.VendorVO;
import nc.vo.pp.price.QuoteConVO;
import nc.vo.pp.price.StatParaVO;
import nc.vo.pp.price.StockExecVO;
import nc.vo.pp.price.StockVarVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;

public interface IAsk {
//	/**
//	 * 根据主键在数据库中添加一个VO对象。
//	 *
//	 * 创建日期：(2001-8-4)
//	 * @return Vector
//	 * @param key Vector
//	 * @exception BusinessException 异常说明。
//	 */
//	public abstract Vector insertMy(Vector v)
//			throws BusinessException;
//	/**
//	 * 根据主键在数据库中添加一个VO对象。
//	 *
//	 * 创建日期：(2001-8-4)
//	 * @return Vector
//	 * @param key Vector
//	 * @exception BusinessException 异常说明。
//	 */
//	public abstract Vector updateMy(Vector v)
//			throws BusinessException;
	 /**
	 * @功能：根据人员主键获取该人员所在部门主键
	 * @作者：晁志平
	 * 创建日期：(2001-9-14 10:59:44)
	 * @param:<|>
	 * @return:
	 * @exception:
	 * @see;
	 * @since:
	 *
	 * @return java.lang.String
	 * @param psnid java.lang.String
	 */
	public abstract String getPkDeptByPkPsnForAsk(String pk_psndoc) throws BusinessException;
	 /**
	 * @功能：查询询价单明细
	 * @作者：晁志平
	 * 创建日期：(2001-9-14 10:59:44)
	 * @param:<|>
	 * @return:
	 * @exception:
	 * @see;
	 * @since:
	 *
	 * @return java.lang.String
	 * @param psnid java.lang.String
	 */
	public abstract VendorVO[] queryVendorDetail(String logCorp, String logUser) throws BusinessException;
	/**
	 * 根据主键在数据库中删除VO对象数组。
	 *
	 * 创建日期：(2001-8-4)
	 * @return Vector
	 * @param key Vector
	 * @exception BusinessException 异常说明。
	 */
	public abstract boolean discardAskbillVOsMy(Vector v)
			throws BusinessException;
	/**
	 * @过滤询价单(询价维护)
	 */
	public abstract Vector  queryAllInquireMy(String strSQL) throws BusinessException; 
	public abstract Vector  queryAllInquireMy(ConditionVO[] conds, String pk_corp, UFBoolean[] status,String userid) throws BusinessException;
	/**
	 * 通过主键获得VO对象。
	 *
	 * 创建日期：(2001-6-7)
	 * @return nc.vo.pp.ask.AskbillVO
	 * @param key String
	 * @exception BusinessException 异常说明。
	 */
	public abstract Vector findByPrimaryKeyForAskBill(String key) throws BusinessException;
	/**
	 * 功能描述:查询询价单表体
	 * 输入参数: ArrayList(0)	询价单头主键[]
	 			 ArrayList(1)	询价单头时间截[]
	 * 返回值:ArrayList(0),询价单体[](自由项已处理)
	 */
	public abstract Vector findByPrimaryKeyForAskBillForDataPower(String key,ConditionVO[] conds) throws BusinessException;
	/**
	 * 功能描述:查询询价单表体
	 * 输入参数: ArrayList(0)	询价单头主键[]
	 			 ArrayList(1)	询价单头时间截[]
	 * 返回值:ArrayList(0),询价单体[](自由项已处理)
	 */
	public abstract Vector queryAllBodysForAskBill(ArrayList aryPara)
		throws BusinessException ;
	/**
	 * 功能描述:查询询价单表体
	 * 输入参数: ArrayList(0)	询价单头主键[]
	 			 ArrayList(1)	询价单头时间截[]
	 * 返回值:ArrayList(0),询价单体[](自由项已处理)
	 */
	public abstract Vector queryAllBodysForPriceAudit(ArrayList aryPara)
		throws BusinessException ;
//	/**
//	 * 根据主键在数据库中添加一个VO对象。
//	 *
//	 * 创建日期：(2001-8-4)
//	 * @return Vector
//	 * @param key Vector
//	 * @exception BusinessException 异常说明。
//	 */
//	public abstract Vector insertMyForPriceAudit(Vector v)
//			throws BusinessException;
//	/**
//	 * 根据主键在数据库中添加一个VO对象。
//	 *
//	 * 创建日期：(2001-8-4)
//	 * @return Vector
//	 * @param key Vector
//	 * @exception BusinessException 异常说明。
//	 */
//	public abstract Vector updateMyForPriceAudit(Vector v)
//			throws BusinessException;
	/**
	 * @功能：查询询价单明细
	 * @说明：
			 1.表体加字段
	 * @param  conds    ConditionVO[]
	 * @param  pk_corp  String
	 * @param  status   boolean[]
	 */
	public abstract Hashtable getEffectAskPrice(EffectPriceParaVO effectPricePara) throws BusinessException;
	/**
	 * @过滤询价单(询价维护)
	 */
	public abstract Vector  queryAllForPriceAudit(String strSQL) throws BusinessException ;
	
	/**
	 * 
	 * 查询价格审批单，增加了待审批查询条件
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * 使用示例
	 * <p>
	 * <b>参数说明</b>
	 * @param conds
	 * @param pk_corp
	 * @param status
	 * @param strOpr
	 * @return
	 * @throws BusinessException
	 * <p>
	 * @author donggq
	 * @time 2008-8-25 上午10:47:06
	 */
	public abstract Vector  queryAllForPriceAudit(ConditionVO[] conds, String pk_corp, UFBoolean[] status,String strOpr) throws BusinessException ;
	
	public abstract Vector  queryAllForPriceAudit(String sCommenWhere, String pk_corp,String strOpr,boolean iswaitaudit) throws BusinessException;
	
	/**
	 * @过滤询价单(询价维护)
	 */
	public abstract VendorInvPriceVO[]  queryForVendorInvPrice(ConditionVO[] conds, String pk_corp) throws BusinessException ;
	/**
	 * 通过主键获得VO对象。
	 *
	 * 创建日期：(2001-6-7)
	 * @return nc.vo.pp.ask.AskbillVO
	 * @param key String
	 * @exception BusinessException 异常说明。
	 */
	public abstract Vector findByPrimaryKeyForPriceAuditBill(String key) throws BusinessException;
	/**
	 * 通过主键获得VO对象。
	 *
	 * 创建日期：(2001-6-7)
	 * @return nc.vo.pp.ask.AskbillVO
	 * @param key String
	 * @exception BusinessException 异常说明。
	 */
	public abstract Vector findByPrimaryKeyForPriceAuditBill(String key,ConditionVO[] conds) throws BusinessException;
	/**
	 * 根据主键在数据库中删除VO对象数组
	 *
	 * 创建日期：(2001-6-7)
	 * @param vos AskbillVO[]
	 * @param key String
	 * @exception BusinessException 异常说明。
	 */
	public abstract boolean discardPriceAuditbillVOsMy(Vector v) throws BusinessException;
	/**
	 * 作者：汪维敏 功能：保存及审批操作时，前台需要刷新审批人，审批日期，ts，单据状态 参数： 返回： 例外： 日期：(2004-5-13
	 * 13:21:13) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @param key
	 *            java.lang.String
	 * @exception BusinessException
	 *                异常说明。
	 */
	public abstract ArrayList queryForAudit(String key) throws BusinessException;
	
	public abstract Vector  queryAllForPriceAudit(ConditionVO[] conds, String pk_corp, UFBoolean[] status) throws BusinessException ;
	/**
	 * @过滤询价单(询价维护)
	 */
	public abstract Vector  queryBodysForPriceAudit(ConditionVO[] conds, String pk_corp, String key) throws BusinessException ;
	/**
	 * @过滤询价单(询价维护)
	 */
	public abstract AskbillHeaderVO[]  queryHeadersForPriceAudit(ConditionVO[] conds, String pk_corp) throws BusinessException ;
	/**
	 * @功能：查询询价单明细
	 * @说明：
			 1.表体加字段
	 * @param  conds    ConditionVO[]
	 * @param  pk_corp  String
	 * @param  status   boolean[]
	 */
	public abstract Hashtable querySourceInfoForGenOrder(Vector v) throws BusinessException;
	/**
	 * 功能：获取请购单行ID对应的请购单业务类型主键
	 * 参数: ArrayList 请购单行ID
	 * 返回：ArrayList 请购单行ID对应的请购单业务类型主键
	 * 作者：晁志平
	 * 创建：2004-6-3 19:52:07)
	 *
	 */
	public abstract Hashtable getBusiIdForOrd(ArrayList listPara) throws BusinessException;
	/**
	 * 根据币种名称取得币种ID
	 * 假设：币种名称唯一
	 * 创建日期：(2001-10-27 13:30:59)
	 * @return java.lang.String
	 * @param currname java.lang.String
	 */
	public abstract String queryCurrIDByCurrName(String currname) throws BusinessException ;
	/**
	 * @询价单是否执行了请购
	 * @作者：周晓
	 * @参数：String[] saRowId		请购单行数组
	 * @返回值：UFBoolean[] uaExistAfter  注：true表示请购单行存在后续单据，false表示不存在
	 * 创建日期：(2005-08-09 15:04:05)
	 * @return nc.vo.pub.lang.UFBoolean[]
	 */
	public abstract UFBoolean[] queryIfExecPrayForAsk(String[] saRowId) throws BusinessException;
	/**
	 * 根据主键在数据库中添加一个VO对象。
	 *
	 * 创建日期：(2001-8-4)
	 * @param key String
	 * @exception BusinessException 异常说明。
	 */
	public abstract Vector updateMyForExcelToBill(Vector v) throws BusinessException;
	/**
	 * @发出时选择供应商电子邮箱
	 * @作者：周晓
	 * @参数：String[] saRowId		请购单行数组
	 * @返回值：UFBoolean[] uaExistAfter  注：true表示请购单行存在后续单据，false表示不存在
	 * 创建日期：(2005-08-09 15:04:05)
	 * @return nc.vo.pub.lang.UFBoolean[]
	 */
	public Hashtable queryEmailAddrForAskSend(String[] cvendorIds) throws BusinessException;
	/**
	 * @功能：根据供应商、存货、币种查询默认价格信息，此信息由采购提供接口;采购订单、委外订单取默认价格时使用
	 * @说明：
			 1.表体加字段
	 * @param   String[] cvendmangid--供应商
	 * @param   String[] cmangid--存货
	 * @param   String ccurrencytypeid--币种   
	 * @return	VO数组--|-- cvendmangid--供应商
                        |--cmangid--存货
                        |-- ccurrencytypeid--币种
                        |-- nquoteprice --无税报价
                        |-- nquotetaxprice --含税报价
                        |-- deliverdays --交货期
                        |-- dvalitdate--报价生效日期
                        |-- dinvalitdate --报价失效日期
	 * @throws	BusinessException
	 * @since	5.0

	 */
	public EffectPriceVO[] getEffectPriceForOrder(EffectPriceParaVO effectPricePara) throws BusinessException;
	/**
	 * 作者：zx
	 * 功能：为采购管理提供价格。
	 * 参数：String[] cmangids,		存货管理ID数组
	 *		String[] cvendormangids,	供应商管理ID数组，与cmangids一一对应
	 *		String[] ccurrencyids,		币种ID数组，与cmangids一一对应
	 		String sPricePolicy 	价格优先策略
	 		String curData 	订单当前日期
	 		String[] sRecieptAreas, 收货地区
			String sSendtype    发运方式
	 * 返回：UFDouble[]		与cmangids一一对应的供应商存货价格数组
	 * 例外：
	 * 日期：(2002-6-10 13:25:09)
	 */
	public UFDouble[] queryPriceForPO(String[] cmangids,
			String[] cvendormangids,
			String[] ccurrencyids,
			String sPricePolicy,
			String curData, 
			String[] sRecieptAreas,
			String sSendtype) throws BusinessException;
	/**
	 * “请购单生成订单限制方式”选择为“经过价格审批才能生成”,查询满足条件的请购单行。
	 *
	 * 创建日期：(2001-6-7)
	 * @return nc.vo.pp.ask.AskbillItemVO
	 * @param key String
	 * @exception java.sql.SQLException 异常说明。
	 */
	//public Hashtable queryIsGenPriceAudit(ArrayList prayRowIds) throws BusinessException;
	/**
	 * @回写生成订单次数
	 */
	public void  reWriteGenOrderNums(String[] addForOrder,String[] delForOrder) throws BusinessException;
	/**
	 * 功能描述:已生成订单的价格审批单不能弃审
	 * @throws BusinessException 
	 */
	public void CheckIsGenOrder (String condition)  throws  BusinessException;
	/**
	 * @功能：查询询价单明细
	 * @说明：
			 1.表体加字段
	 * @param  conds    ConditionVO[]
	 * @param  pk_corp  String
	 * @param  status   boolean[]
	 */
	public AskbillMergeVO queryDetailVOMy(String strSQL,String strSQLForFree,UFBoolean[] status) throws BusinessException;
	
	public AskbillMergeVO queryDetailVOMy(ConditionVO[] conds, String pk_corp, UFBoolean[] status) throws BusinessException;
	/**
	 * @功能：查询询价单统计汇总VO
	 */
	public AskbillMergeVO queryStatisVOMy(
		ConditionVO[] conds,
		String pk_corp,
		UFBoolean[] status,
		String[] groups,
		String priceType)
		throws BusinessException ;
	/**
	 * @功能：供应商报价对比VO[]
	 * @思路：
	 		1.根据查询条件过滤出不同存货ID   x 个 [po_askbill_b.cmangid]
	 		2.根据查询条件过滤出不同的币种ID y 个 [po_askbill.ccurrencytypeid]
	 		3.做两重循环：取出报价信息记录 x*y 条
	 * @处理：
	 		0.币种处理
	 		  币种只作为查询条件
			1.查询符合条件的存货管理档案ID
			2.根据ID获取相应的存货信息
			  #编码名称规格型号
			  #主计量
			  #换算率
			  #参考成本
			  #计划价
			  #最新价
			3.存货管理档案ID + 查询条件(含币种) -> 供应商报价对比表体VO[] (按期间循环)
			4.用“存货管理档案ID+币种ID”分组统计->最高价、最低价、平均价
	 * @return quotecons QuoteConVO[]
	 * @param  paravo    QuoteConParaVO
	 */
	public QuoteConVO[] queryQuoteConVOsMy(StatParaVO paravo) throws BusinessException ;
	/**
	 * @功能：存货执行价对比VO[]、存执行价变动VO[]
	 * @思路： 1.根据查询条件过滤出不同存货ID x 个 [po_askbill_b.cmangid] 2.根据查询条件过滤出不同的币种ID y 个
	 *      [po_askbill.ccurrencytypeid] 3.做两重循环：取出报价信息记录 x*y 条 4.以期间 pDates 的
	 *      pDates[i],pDates[i+1] 为分组项目统计
	 * @处理： 0.币种处理 币种只作为查询条件 1.查询符合条件的存货管理档案ID 2.根据ID获取相应的存货信息 #编码名称规格型号 #主计量
	 *      #换算率 #参考成本 #计划价 #最新价 3.存货管理档案ID + 查询条件(含币种) -> 存货执行价对比表体VO[] (按期间循环)
	 *      4.用“存货管理档案ID+币种ID”分组统计->最高价、最低价、平均价
	 * @return stockexecs StockExecVO[]
	 * @param paravo
	 *            QuoteConParaVO
	 * @throws BusinessException
	 */
	public StockExecVO[] queryStockStatVOsMy(StatParaVO paravo)
			throws BusinessException ;
	/**
	 * @功能：供应商、业务类型、业务员、部门报价对比VO[]
	 * @思路：
	 		1.根据查询条件过滤出不同存货ID   x 个 [po_askbill_b.cmangid]
	 		2.根据查询条件过滤出不同的币种ID y 个 [po_askbill.ccurrencytypeid]
	 		3.做两重循环：取出报价信息记录 x*y 条
	 * @处理：
	 		0.币种处理
	 		  币种只作为查询条件
			1.查询符合条件的存货管理档案ID
			2.根据ID获取相应的存货信息
			  #编码名称规格型号
			  #主计量
			  #换算率
			  #参考成本
			  #计划价
			  #最新价
			3.存货管理档案ID + 查询条件(含币种) -> 供应商、业务类型、业务员、部门报价对比表体VO[] (按期间循环)
			4.用“存货管理档案ID+币种ID”分组统计->最高价、最低价、平均价
	 * @return quotecons QuoteConVO[]
	 * @param  paravo    QuoteConParaVO
	 */
	public QuoteConVO[] queryPurExecVOsMy(StatParaVO paravo) throws BusinessException ;
	/**
	 * @功能：查询存货报价变动VO[]
	 * @思路：
	 		1.根据查询条件过滤出不同存货ID   x 个 [po_askbill_b.cmangid]
	 		2.根据查询条件过滤出不同的币种ID y 个 [po_askbill.ccurrencytypeid]
	 		3.做两重循环：取出报价信息记录 x*y 条
	 * @处理：
	 		0.币种处理
	 		  币种只作为查询条件
			1.查询符合条件的存货管理档案ID
			2.根据ID获取相应的存货信息
			  #编码名称规格型号
			  #主计量
			  #换算率
			  #参考成本
			  #计划价
			  #最新价
			3.存货管理档案ID + 查询条件 -> 存货报价变动表体VO[]
			4.平均价、最高价、最低价在UI处理
	 * @return stockvar StockVarVO
	 * @param  conds    ConditionVO[]
	 * @param  pk_corp  String
	 * @param  status   boolean[]
	 */
	public StockVarVO[] queryStockVarVOsMy(
			StatParaVO paravo)
		throws BusinessException;
	/**
	 * 新增或修改询报价单
	 * 
	 * 创建日期：(2001-8-4)
	 * 
	 * @param key
	 *            String
	 * @exception BusinessException
	 *                异常说明。
	 */
	public Vector doSaveForAskBill(Vector v) throws BusinessException ;
	/**
	 * 新增或修改价格审批单
	 * 
	 * 创建日期：(2001-8-4)
	 * 
	 * @param key
	 *            String
	 * @exception BusinessException
	 *                异常说明。
	 */
	public Vector doSaveForPriceAuditBill(Vector v) throws BusinessException ;
	/**
	 * @功能：查询评估分数
	 * @说明： 
	 * @param logCorp String--登陆公司
	 * @param vendorMangIDs String[]--供应商管理ID数组
	 * 
	 */
	public String[] queryForVendorSelected(String logCorp,String[] vendorMangIDs) throws BusinessException;
	/**
   * 
   * 方法功能描述：给请购单转单到采购订单界面，选择供应商时从供应商价格表中选择默认供应商使用。
   * 选取方法：同一存货所有供应商是否订货为“是”的最新有效价格记录，
   *         并将优先级最高的供应商带入到请购单行的供应商栏目。
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * @param loginDate 登录日期
   * @param pk_corp 存货所属公司
   * @param sInvBasids 存货基本档案id数组
   * @return hashmap<pk_invbasdoc,defaultVendor>
   * @throws BusinessException
   * <p>
   * @author donggq
   * @time 2008-2-29 下午03:06:52
   */
  public HashMap getDefaultVendors(String loginDate,String pk_corp,String[] sInvBasids)throws BusinessException;
//  /**
//   * 根据传入的where sql 查询价格审批单
//   * <p>
//   * <b>examples:</b>
//   * <p>
//   * 使用示例
//   * <p>
//   * <b>参数说明</b>
//   * @param strSQL
//   * @return
//   * @throws BusinessException
//   * <p>
//   * @author donggq
//   * @time 2008-8-5 下午05:10:23
//   */
//  public Vector queryAllForPriceAudit( String strSQL) throws BusinessException;
	public  PriceauditHeaderVO[] queryHeadersForPriceAudit2(ConditionVO[] p0, String p1) throws Exception;
	
	public  Vector queryAllBodysForPriceAudit2(ArrayList  p0) throws Exception;
	/**
	 * 
	 * 转为订单增加的接口方法，主要是返回价格及其对应的税率
	 * <p>
	 * <b>examples:</b>
	 * <p>
	 * 使用示例
	 * <p>
	 * <b>参数说明</b>
	 * 功能：为采购管理提供价格。
	 * 参数：String[] cmangids,		存货管理ID数组
	 *		String[] cvendormangids,	供应商管理ID数组，与cmangids一一对应
	 *		String[] ccurrencyids,		币种ID数组，与cmangids一一对应
	 		String sPricePolicy 	价格优先策略
	 		String curData 	订单当前日期
	 		String[] sRecieptAreas, 收货地区
			String sSendtype    发运方式
	 * 返回：UFDouble[][]		与cmangids一一对应的供应商存货[i][0]价格\[0][1]税率数组
	 * @throws BusinessException
	 * <p>
	 * @author donggq
	 * @time 2008-8-27 上午09:24:35
	 */
	public UFDouble[][] queryPriceForPOOrder(String[] cmangids,
			String[] cvendormangids,
			String[] ccurrencyids,
			String sPricePolicy,
			String curData, 
			String[] sRecieptAreas,
			String sSendtype) throws BusinessException;
}
