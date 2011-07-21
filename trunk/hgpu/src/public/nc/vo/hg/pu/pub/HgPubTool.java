package nc.vo.hg.pu.pub;

import java.util.HashMap;
import java.util.Map;

import nc.bd.accperiod.AccountCalendar;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pf.change.AbstractConversion;
import nc.bs.pf.change.VOConversion;
import nc.bs.trade.business.HYPubBO;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.scm.pub.panel.RelationsCal;
import nc.vo.bd.period2.AccperiodmonthVO;
import nc.vo.hg.to.pub.StockNumParaVO;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.pf.change.IchangeVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.billcodemanage.BillCodeObjValueVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.to.pub.BillItemVO;
import nc.vo.to.pub.BillVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.trade.pub.IBillStatus;

//zhf 销售返还模块公共工具类   前后台均可调用
public class HgPubTool {

	public static final Integer INTEGER_ZERO_VALUE = new Integer(0); // 整数零

	private static nc.bs.pub.formulaparse.FormulaParse fp = new nc.bs.pub.formulaparse.FormulaParse();

	public static final Object execFomular(String fomular, String[] names,
			String[] values) throws BusinessException {
		fp.setExpress(fomular);
		if (names.length != values.length) {
			throw new BusinessException("传入参数异常");
		}
		int index = 0;
		for (String name : names) {
			fp.addVariable(name, values[index]);
			index++;
		}
		return fp.getValue();
	}

	private static nc.ui.pub.formulaparse.FormulaParse fpClient = new nc.ui.pub.formulaparse.FormulaParse();

	public static final Object execFomularClient(String fomular,
			String[] names, String[] values) throws BusinessException {
		fpClient.setExpress(fomular);
		if (names.length != values.length) {
			throw new BusinessException("传入参数异常");
		}
		int index = 0;
		for (String name : names) {
			fpClient.addVariable(name, values[index]);
			index++;
		}
		return fpClient.getValue();
	}
	
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）将调拨订单信息封装到库存存量参数vo内
	 * 2011-4-25下午02:10:19
	 * @param billvo
	 * @return
	 */
	public static StockNumParaVO[] transBillItemsToStockNumPara(BillVO billvo,UFDate uLogDate){
		if(billvo == null)
			return null;
		BillItemVO[] items = billvo.getItemVOs();
		if(items == null || items.length == 0)
			return null;
		Map<String,StockNumParaVO> lpara = new HashMap<String,StockNumParaVO>();
		StockNumParaVO tmppara = null;
		String key = null;
		for(BillItemVO item:items){
			key = item.getCinvbasid();
			if(lpara.containsKey(key))
				continue;
			tmppara = new StockNumParaVO();
			tmppara.setCoutcorpid(item.getCoutcorpid()==null?billvo.getHeaderVO().getCoutcorpid():item.getCoutcorpid());
			tmppara.setCincorpid(item.getCincorpid());
			tmppara.setCoutcalbodyid(item.getCoutcbid());
			tmppara.setCincalbodyid(item.getCincbid());
			tmppara.setCoutwarehouseid(item.getCoutwhid());
			tmppara.setCinwarehouseid(item.getCinwhid());
			tmppara.setCbatchid(item.getVbatch());
			tmppara.setCinvbasid(item.getCinvbasid());
			tmppara.setCininvid(item.getCininvid());
			tmppara.setCoutinvid(item.getCoutinvid());
			tmppara.setDlogdate(uLogDate ==  null?new UFDate(item.getAttributeValue("SDATE").toString()):uLogDate);
			lpara.put(key, tmppara);
		}
		if(lpara.size()==0)
			return null;
		return lpara.values().toArray(new StockNumParaVO[0]);
	}
	
	public static String getSubSql(String[] saID) {
		String sID = null;
		StringBuffer sbSql = new StringBuffer("(");
		for (int i = 0; i < saID.length; i++) {
			if (i > 0) {
				sbSql.append(",");
			}
			sbSql.append("'");
			sID = saID[i];
			if (sID == null) {
				sID = "";
			}
			sbSql.append(sID);
			sbSql.append("'");
		}
		sbSql = sbSql.append(")");
		return sbSql.toString();
	}

	public static String getPlanBTableName(String billtype) {
		if (billtype.equalsIgnoreCase(HgPubConst.PLAN_YEAR_BILLTYPE)) {
			return "hg_planyear_b";
		} else
			return "hg_planother_b";
	}

	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）将为null的字符串处理为“”。 2010-11-22下午02:51:02
	 * @param value
	 * @return
	 */
	public static String getString_NullAsTrimZeroLen(Object value) {
		if (value == null) {
			return "";
		}
		return value.toString().trim();
	}

	/**
	 * 
	 * @author zhf
	 * @说明 获取vo转换类实例
	 * @时间 2010-9-26上午10:47:52
	 * @param classname
	 * @return
	 */
	private static nc.vo.pf.change.IchangeVO getChangeClass(String classname)
			throws Exception {
		if (PuPubVO.getString_TrimZeroLenAsNull(classname) == null)
			return null;
		try {
			Class c = Class.forName(classname);
			Object o = c.newInstance();
			return (nc.vo.pf.change.IchangeVO) o;
		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
			throw ex;
		}
		// return null;
	}

	// /**
	// *
	// * @author zhf
	// * @说明：（鹤岗矿业）根据系统系统设置 返还 当前公司的供货单位和部门
	// * 2011-11-1下午04:43:16
	// * @param cgathercorp 集采公司
	// * @param sLogcorp 登陆公司
	// * @param sLogUser 登陆用户
	// * @return 供货单位 供货部门
	// */
	// public static String[] getSupplyInfor(String cgathercorp,String
	// sLogcorp,String sLogUser) throws BusinessException{
	// if(PuPubVO.getString_TrimZeroLenAsNull(cgathercorp)==null)
	// throw new BusinessException("系统未设置集采公司");
	// String[] supplyInfor = new String[2];
	// if(cgathercorp.equalsIgnoreCase(sLogcorp)){
	// return null;
	// }else{
	// supplyInfor[0] = sLogcorp;
	// String fumu =
	// "pk_dep->getColValue(bd_deptdoc,pk_fathedept,pk_deptdoc,deptdocid)";
	// String[] names = new String[]{"deptdocid"};
	// String[] values = new String[]{PuTool.getPsnByUser(sLogUser,
	// sLogcorp).getPk_deptdoc()};
	// String dept = PuPubVO.getString_TrimZeroLenAsNull(execFomularClient(fumu,
	// names, values));
	// if(dept == null){//上级部门为空
	// //上级单位为集采公司
	// supplyInfor[0] = cgathercorp;
	// }
	// supplyInfor[1] = dept;
	// }
	//		
	// return supplyInfor;
	// }

	/**
	 * 
	 * @author zhf
	 * @说明 单vo数据交换 该方法后台调用 前台不能使用
	 * @时间 2010-9-26上午11:30:42
	 * @param souVo
	 *            来源vo
	 * @param tarVo
	 *            目标vo
	 * @param chanclassname
	 *            vo交换类
	 * @throws Exception
	 */
	public static void runChangeVO(String souBilltype, String destBilltype,
			SuperVO souVo, SuperVO tarVo, String chanclassname)
			throws Exception {
		IchangeVO change = null;
		try {
			change = getChangeClass(chanclassname);
		} catch (Exception e) {// 可能存在类型转换异常 此处要求
								// changeClassName类需要继承VOConversion
			e.printStackTrace();
			throw new BusinessException(e);
		}

		if (!(change instanceof VOConversion)) {
			throw new BusinessException("数据转换组件异常，" + change.toString());
		}
		AggregatedValueObject preBillVo = getTmpBIllVo1();
		AggregatedValueObject tarBillVo = getTmpBIllVo2();

		preBillVo.setParentVO(souVo);
		tarBillVo.setParentVO(tarVo);
		AbstractConversion achange = (AbstractConversion) change;
		achange.setSourceBilltype(souBilltype);
		achange.setDestBilltype(destBilltype);
		achange.retChangeBusiVO(preBillVo, tarBillVo);
	}

	private static HYBillVO tmpBillVo1 = null;

	private static HYBillVO getTmpBIllVo1() {
		if (tmpBillVo1 == null) {
			tmpBillVo1 = new HYBillVO();
		}
		return tmpBillVo1;
	}

	private static HYBillVO tmpBillVo2 = null;

	private static HYBillVO getTmpBIllVo2() {
		if (tmpBillVo2 == null) {
			tmpBillVo2 = new HYBillVO();
		}
		return tmpBillVo2;
	}

	private static HYPubBO pubbo = null;

	private static HYPubBO getPubBO() {
		if (pubbo == null)
			pubbo = new HYPubBO();
		return pubbo;
	}

	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）批获取单据号 2011-11-1下午04:04:18
	 * @param billtype
	 * @param pk_corp
	 * @param vo
	 * @param intNum
	 * @return
	 * @throws BusinessException
	 */
	public static String[] getBatchBillNo(String billtype, String pk_corp,
			BillCodeObjValueVO vo, int intNum) throws BusinessException {
		return getPubBO().getBatchBillNo(billtype, pk_corp, vo, intNum);
	}

	public static String getBillNo(String billtype, String pk_corp,
			String custBillCode, BillCodeObjValueVO vo)
			throws BusinessException {
		return getPubBO().getBillNo(billtype, pk_corp, custBillCode, vo);
	}

	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）获取系统基准会计期间方案id 2010-11-23上午11:14:05
	 * @return
	 */
	public static String getStandAccperiodschemeID() {
		return PuPubVO.getString_TrimZeroLenAsNull(AccountCalendar
				.getInstance().getMonthVO().getPk_accperiodscheme());
	}

	public static String[] PLAN_YEAR_NUMKEYS = new String[] { "nmonnum1",
			"nmonnum2", "nmonnum3", "nmonnum4", "nmonnum5", "nmonnum6",
			"nmonnum7", "nmonnum8", "nmonnum9", "nmonnum10", "nmonnum11",
			"nmonnum12"

			, "nsafestocknum"// 安全库存
			, "nnum"// 毛需求
			, "nassistnum"// 毛需求辅数量
			, "nonhandnum"// 截止N结账的库存量
			, "nallusenum"// 上年度消耗
			, "nnonum" // 库存不可用
			, "nnetnum" // 净需求
			, "nreoutnum" // 预月预耗
			, "nreinnum" // 剩余资源
			, "nusenum" };

	public static String[] PLAN_MONTH_NUMKEYS = new String[] { "nmonnum1",
			"nmonnum2", "nmonnum3", "nmonnum4", "nmonnum5", "nmonnum6",
			"nmonnum7", "nmonnum8", "nmonnum9", "nmonnum10", "nmonnum11",
			"nmonnum12" };
	public static String[] PLAN_NUMKEYS = new String[] { "nsafestocknum"// 安全库存
			, "nnum"// 毛需求
			, "nassistnum"// 毛需求辅数量
			, "nonhandnum"// 截止N结账的库存量
			, "nallusenum"// 上年度消耗
			, "nnonum" // 库存不可用
			, "nnetnum" // 净需求
			, "nreoutnum" // 预月预耗
			, "nreinnum" // 剩余资源
			, "nusenum" };
	public static String[] PLAN_IC_NUMS = new String[] { 
//		"nsafestocknum"// 安全库存
			// ,"nassistnum"//毛需求辅数量
			 "nonhandnum"// 截止N结账的库存量
			// ,"nallusenum"//上年度消耗
			, "nnonum" // 库存不可用
			// ,"nnetnum" //净需求
			, "nreoutnum" // 预月预耗
			, "nreinnum" // 剩余资源
			// ,"nusenum"
	};

	public static final String[] DEAL_SORT_FIELDNAMES = new String[] {
			"vbillno", "invcode" };

	/** 公式计算用到数组 */
	/** 调用计算公式关键字列表(驱动计算时也要包括驱动列关键字) */
	public static String[] m_saKey = new String[] { "Y", "hsl", "nassistnum",
			"nnum", "nprice", "nmny" };

	/** 关键字对应的计算公式类的常量 ( 参见 RelationsCal ) */
	public static int[] m_iDescriptions = new int[] {
			RelationsCal.IS_FIXED_CONVERT, RelationsCal.CONVERT_RATE,
			RelationsCal.NUM_ASSIST, RelationsCal.NUM,
			RelationsCal.NET_PRICE_ORIGINAL, RelationsCal.MONEY_ORIGINAL };
	public static int[] iaPrior = new int[] { 0 };// 联动计算使用字段 含税优先 还是无税优先

	// 暂时使用以下方式定义 步长
	public static final int STEP_VALUE = 10;
	public static final int START_VALUE = 10;

	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）对vo进行行号设置 2011-1-26下午03:34:51
	 * @param voaCA
	 * @param sBillType
	 * @param sRowNOKey
	 */
	public static void setVOsRowNoByRule(
			CircularlyAccessibleValueObject[] voaCA, String sRowNOKey) {

		if (voaCA == null)
			return;
		int index = START_VALUE;
		for (CircularlyAccessibleValueObject vo : voaCA) {
			vo.setAttributeValue(sRowNOKey, String.valueOf(index));
			index = index + STEP_VALUE;
		}

	}

	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）返还资金类型 2011-2-2上午10:45:45
	 * @param ifundtype
	 * @return
	 */
	public static String getSFundType(int ifundtype) {
		if (ifundtype == HgPubConst.FUND_CHECK_FUND)
			return "资金";
		else if (ifundtype == HgPubConst.FUND_CHECK_QUATO)
			return "限额";
		else if (ifundtype == HgPubConst.FUND_CHECK_SPECIALFUND)
			return "专项资金";
		else
			return "";
	}

	public static int getIFundTypeByPlanType(String cplanbilltype) {
		if (PuPubVO.getString_TrimZeroLenAsNull(cplanbilltype) == null)
			return HgPubConst.FUND_CHECK_ERRORTYPE;
		if (cplanbilltype.equalsIgnoreCase(HgPubConst.PLAN_TEMP_BILLTYPE)
				|| cplanbilltype
						.equalsIgnoreCase(HgPubConst.PLAN_MONTH_BILLTYPE)) {
			return HgPubConst.FUND_CHECK_FUND_QUATO;
		} else if (cplanbilltype.equalsIgnoreCase(HgPubConst.PLAN_MNY_BILLTYPE)) {
			return HgPubConst.FUND_CHECK_SPECIALFUND;
		}
		return HgPubConst.FUND_CHECK_ERRORTYPE;
	}

	public static AccperiodmonthVO getCurrentMonth() {
		return AccountCalendar.getInstance().getMonthVO();
	}

	public static int getPurchaseInBusiStatus(GeneralBillVO vo){
		if(vo == null)
			return -1;
		GeneralBillHeaderVO head = vo.getHeaderVO();
		GeneralBillItemVO[] items= (GeneralBillItemVO[])vo.getChildrenVO();
		
		//到货   验收合格   不合格   无合同   入库		
		boolean isIn = false;
		boolean isNoPact = false;
		boolean isOk = false;
		
		if(PuPubVO.getString_TrimZeroLenAsNull(head.getAttributeValue(HgPubConst.VUSERDEF[3]))==null){
				return HgPubConst.purchaseIn_arr;
		}
		
		for(GeneralBillItemVO item:items){
			if(!PuPubVO.getUFDouble_NullAsZero(item.getNinnum()).equals(UFDouble.ZERO_DBL)){
				isIn = true;
			}
			if(PuPubVO.getString_TrimZeroLenAsNull(item.getCsourcetype())==null)
				isNoPact = true;
			if(!PuPubVO.getUFDouble_NullAsZero(item.getAttributeValue(HgPubConst.NUM_DEF_QUA)).equals(UFDouble.ZERO_DBL))
				isOk = true;
						
		}
		
		if(isIn)
			return HgPubConst.purchaseIn_in;
		if(isOk){
			if(isNoPact)
				return HgPubConst.purchaseIn_nopact;
			return HgPubConst.purchaseIn_ok;
		}
		return HgPubConst.purchaseIn_no;
	}

	public static int getIPlanBilltypeBySBilltype(String sbilltype) {
		if (sbilltype.equalsIgnoreCase("自由")) {
			return IBillStatus.FREE;
		} else if (sbilltype.equalsIgnoreCase("审批通过")) {
			return IBillStatus.CHECKPASS;
		} else if (sbilltype.equalsIgnoreCase("审批进行中")) {
			return IBillStatus.CHECKGOING;
		} else {
			return -1;
		}
	}
	
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）合同条款 涉及字段
	 * 2012-2-22下午05:37:13
	 * @return
	 */
	public static String[] getPactItems(){
		return new String[]{
			"pk_ct_term",
			"pk_ct_manage", 
			"pk_ct_termset", 
			"pk_corp", 
			"termcode", 
			"termname", 
			"termtypename", 
			"termcontent", 
			"otherinfo", 
			"memo",
//			"dr",
			"ts"};
	}

	public static String getMonth(int imonth) {
		if (imonth < 10)
			return "0" + String.valueOf(imonth);

		return String.valueOf(imonth);
	}
	
	public static Object getInvclasscode(String pk_invcl) {
		String sql =" select invclasscode from bd_invcl where pk_invcl = '"+pk_invcl+"' and isnull(dr,0)=0";
		IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		Object o =null;
		try {
			 o=query.executeQuery(sql,new ColumnProcessor());
			
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
			 MessageDialog.showWarningDlg(null, null, e.getMessage());
		}
		return o;
	}
	

	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）实扣时计算预扣
	 * 2011-4-14下午04:29:18
	 * @param noldbeforemny 原来的预扣
	 * @param nactmny 本次实扣
	 * @return
	 * @throws BusinessException
	 */
	public static  UFDouble getNBeforeMnyWhenAct(UFDouble nbeforemny,UFDouble nactmny) throws BusinessException{
		nbeforemny = PuPubVO.getUFDouble_NullAsZero(nbeforemny);
		nactmny = PuPubVO.getUFDouble_NullAsZero(nactmny);
		if(nbeforemny.doubleValue()<=nactmny.doubleValue()){
			return UFDouble.ZERO_DBL;
		}else
			return nbeforemny.sub(nactmny);
	}

}
