package nc.vo.zb.pub;

import java.util.ArrayList;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.util.SQLHelper;
import nc.ui.pub.beans.MessageDialog;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.zb.bidding.BiddingTimesVO;
import nc.vo.zb.parmset.ParamSetVO;

//zhf 销售返还模块公共工具类   前后台均可调用
public class ZbPubTool {

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
	 * 获取时间轮次时间表第一轮次
	 * @param times
	 * @return
	 */
	public static BiddingTimesVO getFirstTime(BiddingTimesVO[] times){
		BiddingTimesVO retTime = null;
		for(BiddingTimesVO time:times){
			if(PuPubVO.getString_TrimZeroLenAsNull(time.getCprecircalnoid())==null){
				retTime = time;
				break;
			}
		}
		return retTime;
	}

	private static final List<CircularlyAccessibleValueObject> tempVoList = new ArrayList<CircularlyAccessibleValueObject>();
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）公用组件 返回传入单据vo 的表头vo
	 * 2011-5-5上午10:17:20
	 * @param billvos  单据vo 数组
	 * @return
	 */
	public static CircularlyAccessibleValueObject[] getParentVOFromAggBillVo(AggregatedValueObject[] billvos,Class parentClass){
		if(billvos == null || billvos.length ==0)
			return null;
		tempVoList.clear();
		for(AggregatedValueObject bill:billvos){
			if(bill.getParentVO() == null)
				continue;
			tempVoList.add(bill.getParentVO());
		}
		if(tempVoList.size() == 0)
			return null;
		return tempVoList.toArray((CircularlyAccessibleValueObject[])java.lang.reflect.Array.newInstance(parentClass, billvos.length));
	}

	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）
	 * 2011-5-6下午01:51:00
	 * @param num
	 * @return
	 */
	public static String tranIntNumToStringNum(int num){
		switch (num) {
		case 1:
			return "一";
		case 2:
			return "二";
		case 3:
			return "三";
		case 4:
			return "四";
		case 5:
			return "五";
		case 6:
			return "六";
		case 7:
			return "七";
		case 8:
			return "八";
		case 9:
			return "九";
		case 10:
			return "十";     
		}
		return "一";
	}

	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）将毫秒数转换为时间格式字符串
	 * 2011-5-12下午12:32:15
	 * @param m
	 * @return
	 */
	public static String tranMillsToTime(long m){
		String value;
		if (m == 24 * 3600000) {
			value = "24:00:00";
			return value;
		}
		long seconds = m / 1000;
		long hour = seconds / 3600;
		hour %= 24;
		long minute = seconds / 60;
		minute %= 60;
		long second = seconds % 60;
		value = "";
		if (hour < 10)
			value += "0" + hour;
		else
			value += hour;
		value += ":";
		if (minute < 10)
			value += "0" + minute;
		else
			value += minute;
		value += ":";
		if (second < 10)
			value += "0" + second;
		else
			value += second;

		return value;
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
	 * 解析编码规则。
	 * 创建日期：(2011-5-24 14:32:51)
	 */
	public static  String[] splitCode(String value) throws BusinessException{
		java.util.StringTokenizer st = new java.util.StringTokenizer(value, " ,+/\\:;", false);
		int count = st.countTokens();
		String[] showvalues = new String[count];
		int index = 0;
		try{
			while(st.hasMoreTokens()){
				showvalues[index++] = st.nextToken().trim();
			}
		}
		catch(Exception e){
			System.out.println("解析比例出错！");
			throw new BusinessException("解析比例出错！"+ZbPubTool.getString_NullAsTrimZeroLen(e.getMessage()));
		}

		return showvalues;
	}
	
	public static int[] colBiddingVendorRates(String splitrate) throws BusinessException{
		String[] ss = splitCode(splitrate);
		if(ss == null || ss.length == 0)
			throw new BusinessException("供应商分量比例设置错误"+splitrate);
		int[] is = new int[ss.length];
		int index = 0;
		try{
			for(String s:ss){
				is[index] = Integer.parseInt(s);
				index++;
			}
		}catch(NumberFormatException ne){
			ne.printStackTrace();
			throw new BusinessException("供应商分量比例数据类型错误"+splitrate);
		}
		return is;
	}
	
	public static String getSql(String sql) {
		if (sql == null)
			return null;
		if (sql.contains("'提交'")) {
			sql = sql.replace("'提交'"," 3");
		}
		if (sql.contains("'自由'")) {
			sql =sql.replace("'自由'"," 8");
		}
		if (sql.contains("'审批通过'")) {
			sql =sql.replace("'审批通过'"," 1");
		}
		return sql;
	}
	
	public static String getSql1(String sql) {
		if (sql == null)
			return null;
		if (sql.contains("'初始'")) {
			sql = sql.replace("'初始'"," 0");
		}
		if (sql.contains("'投标'")) {
			sql =sql.replace("'投标'"," 1");
		}
		if (sql.contains("'评标'")) {
			sql =sql.replace("'评标'"," 2");
		}
		if (sql.contains("'中标'")) {
			sql =sql.replace("'中标'"," 3");
		}
		if (sql.contains("'完成'")) {
			sql =sql.replace("'完成'"," 4");
		}
		if (sql.contains("'流标'")) {
			sql =sql.replace("'流标'"," 5");
		}
		return sql;
	}
	
	public static String getSql2(String sql) {
		if (sql == null)
			return null;
		if (sql.contains("'网上招标'")) {
			sql = sql.replace("'网上招标'"," 0");
		}
		if (sql.contains("'现场招标'")) {
			sql =sql.replace("'现场招标'"," 1");
		}
		return sql;
	}
	
	public static String getParam() throws BusinessException{
		IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		ArrayList<ParamSetVO> al =(ArrayList<ParamSetVO>)query.retrieveByClause(ParamSetVO.class, " isnull(dr,0)=0 and pk_corp ='"+SQLHelper.getCorpPk()+"'");
		if(al == null || al.size()==0 || al.size()>1)
			throw new BusinessException("参数设置出错");
		ParamSetVO vo = al.get(0);
		String str =PuPubVO.getString_TrimZeroLenAsNull(vo.getAttributeValue("reserve1"));
		return str;
	}
	
}
