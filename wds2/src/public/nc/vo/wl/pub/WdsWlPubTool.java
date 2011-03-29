package nc.vo.wl.pub;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.vo.dm.PlanDealVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;

public class WdsWlPubTool {
	
	


	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ �жϲֿ��Ƿ��ܲ�
	 * @ʱ�䣺2011-3-23����05:53:02
	 * @param cwhid
	 * @return
	 */
	public static boolean isZc(String cwhid){
		if(PuPubVO.getString_TrimZeroLenAsNull(cwhid)==null)
			return false;
		if(cwhid.equalsIgnoreCase(WdsWlPubConst.WDS_WL_ZC))
			return true;
		return false;
	}

	public static final Integer INTEGER_ZERO_VALUE = new Integer(0); // ������
	
	public static final UFDouble DOUBLE_ZERO = new UFDouble(0.0);

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
	
	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ ֹͣ�༭��������
	 * @ʱ�䣺2011-3-23����08:12:29
	 * @param bm
	 */
	public static void stopEditing(BillModel bm){
	        BillItem[] items =bm.getBodyItems();
	        if (items != null) {
	            for (int i = 0; i < items.length; i++) {
	                Component comp = items[i].getComponent();
	                if (comp instanceof UIRefPane) {
	                    if (!((UIRefPane) comp).isProcessFocusLost()) {
	                        // System.out.println("����:" + items[i].getName());
	                        ((UIRefPane) comp).processFocusLost();
	                    }
	                }
	            }
	        }
	       
	}
	
	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ ���˼ƻ�����ʱ���˰�������Ϊ0����
	 * @ʱ�䣺2011-3-23����08:33:49
	 * @param ldata
	 * @return
	 */
	public static List<SuperVO> filterVOsZeroNum(List ldata,String numfield){
		if(ldata == null||ldata.size()==0)
			return null;
		List<SuperVO> lnewData = new ArrayList<SuperVO>();
		List<SuperVO> ldata2 = (List<SuperVO>)ldata;
		for(SuperVO vo:ldata2){
			if(PuPubVO.getUFDouble_NullAsZero(vo.getAttributeValue(numfield)).equals(WdsWlPubTool.DOUBLE_ZERO)){
				continue;
			}
			lnewData.add(vo);
		}
		return lnewData;
	}
	

	/**
	 * 
	 * @author zhf
	 * @˵�� ��ȡvoת����ʵ��
	 * @ʱ�� 2010-9-26����10:47:52
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
//			Logger.error(ex.getMessage(), ex);
			throw ex;
		}
		// return null;
	}
	
	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2011-3-24����11:31:59
	 * @return  ��ǰ��ʱ��
	 */
	public static UFDate getCurDate() {
		UFDate date = new UFDate(System.currentTimeMillis());
		return date;
	}


}
