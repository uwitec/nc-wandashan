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

//zhf ���۷���ģ�鹫��������   ǰ��̨���ɵ���
public class ZbPubTool {

	public static final Integer INTEGER_ZERO_VALUE = new Integer(0); // ������

	private static nc.bs.pub.formulaparse.FormulaParse fp = new nc.bs.pub.formulaparse.FormulaParse();

	public static final Object execFomular(String fomular, String[] names,
			String[] values) throws BusinessException {
		fp.setExpress(fomular);
		if (names.length != values.length) {
			throw new BusinessException("��������쳣");
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
			throw new BusinessException("��������쳣");
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
	 * @˵�������׸ڿ�ҵ����Ϊnull���ַ�������Ϊ������ 2010-11-22����02:51:02
	 * @param value
	 * @return
	 */
	public static String getString_NullAsTrimZeroLen(Object value) {
		if (value == null) {
			return "";
		}
		return value.toString().trim();
	}



	// ��ʱʹ�����·�ʽ���� ����
	public static final int STEP_VALUE = 10;
	public static final int START_VALUE = 10;

	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ����vo�����к����� 2011-1-26����03:34:51
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
	 * ��ȡʱ���ִ�ʱ����һ�ִ�
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
	 * @˵�������׸ڿ�ҵ��������� ���ش��뵥��vo �ı�ͷvo
	 * 2011-5-5����10:17:20
	 * @param billvos  ����vo ����
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
	 * @˵�������׸ڿ�ҵ��
	 * 2011-5-6����01:51:00
	 * @param num
	 * @return
	 */
	public static String tranIntNumToStringNum(int num){
		switch (num) {
		case 1:
			return "һ";
		case 2:
			return "��";
		case 3:
			return "��";
		case 4:
			return "��";
		case 5:
			return "��";
		case 6:
			return "��";
		case 7:
			return "��";
		case 8:
			return "��";
		case 9:
			return "��";
		case 10:
			return "ʮ";     
		}
		return "һ";
	}

	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ����������ת��Ϊʱ���ʽ�ַ���
	 * 2011-5-12����12:32:15
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
	 * �����������
	 * �������ڣ�(2011-5-24 14:32:51)
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
			System.out.println("������������");
			throw new BusinessException("������������"+ZbPubTool.getString_NullAsTrimZeroLen(e.getMessage()));
		}

		return showvalues;
	}
	
	public static int[] colBiddingVendorRates(String splitrate) throws BusinessException{
		String[] ss = splitCode(splitrate);
		if(ss == null || ss.length == 0)
			throw new BusinessException("��Ӧ�̷����������ô���"+splitrate);
		int[] is = new int[ss.length];
		int index = 0;
		try{
			for(String s:ss){
				is[index] = Integer.parseInt(s);
				index++;
			}
		}catch(NumberFormatException ne){
			ne.printStackTrace();
			throw new BusinessException("��Ӧ�̷��������������ʹ���"+splitrate);
		}
		return is;
	}
	
	public static String getSql(String sql) {
		if (sql == null)
			return null;
		if (sql.contains("'�ύ'")) {
			sql = sql.replace("'�ύ'"," 3");
		}
		if (sql.contains("'����'")) {
			sql =sql.replace("'����'"," 8");
		}
		if (sql.contains("'����ͨ��'")) {
			sql =sql.replace("'����ͨ��'"," 1");
		}
		return sql;
	}
	
	public static String getSql1(String sql) {
		if (sql == null)
			return null;
		if (sql.contains("'��ʼ'")) {
			sql = sql.replace("'��ʼ'"," 0");
		}
		if (sql.contains("'Ͷ��'")) {
			sql =sql.replace("'Ͷ��'"," 1");
		}
		if (sql.contains("'����'")) {
			sql =sql.replace("'����'"," 2");
		}
		if (sql.contains("'�б�'")) {
			sql =sql.replace("'�б�'"," 3");
		}
		if (sql.contains("'���'")) {
			sql =sql.replace("'���'"," 4");
		}
		if (sql.contains("'����'")) {
			sql =sql.replace("'����'"," 5");
		}
		return sql;
	}
	
	public static String getSql2(String sql) {
		if (sql == null)
			return null;
		if (sql.contains("'�����б�'")) {
			sql = sql.replace("'�����б�'"," 0");
		}
		if (sql.contains("'�ֳ��б�'")) {
			sql =sql.replace("'�ֳ��б�'"," 1");
		}
		return sql;
	}
	
	public static String getParam() throws BusinessException{
		IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		ArrayList<ParamSetVO> al =(ArrayList<ParamSetVO>)query.retrieveByClause(ParamSetVO.class, " isnull(dr,0)=0 and pk_corp ='"+SQLHelper.getCorpPk()+"'");
		if(al == null || al.size()==0 || al.size()>1)
			throw new BusinessException("�������ó���");
		ParamSetVO vo = al.get(0);
		String str =PuPubVO.getString_TrimZeroLenAsNull(vo.getAttributeValue("reserve1"));
		return str;
	}
	
}
