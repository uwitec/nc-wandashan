package nc.vo.wl.pub;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.vo.ic.other.out.TbOutgeneralBVO;
import nc.vo.ic.other.out.TbOutgeneralTVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
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
	public static boolean isZc(String cwhid) {
		if (PuPubVO.getString_TrimZeroLenAsNull(cwhid) == null)
			return false;
		if (cwhid.equalsIgnoreCase(WdsWlPubConst.WDS_WL_ZC))
			return true;
		return false;
	}

	public static final Integer INTEGER_ZERO_VALUE = new Integer(0); // ������

	public static final UFDouble DOUBLE_ZERO = new UFDouble(0f);

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
	public static String getSubSql2(String[] saID) {
		String sID = null;
		StringBuffer sbSql = new StringBuffer();
		for (int i = 0; i < saID.length; i++) {
			if (i > 0) {
				sbSql.append(",");
			}
//			sbSql.append("'");
			sID = saID[i];
			if (sID == null) {
				sID = "";
			}
			sbSql.append(sID);
//			sbSql.append("'");
		}
//		sbSql = sbSql.append(")");
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
	public static void stopEditing(BillModel bm) {
		BillItem[] items = bm.getBodyItems();
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
	public static List<SuperVO> filterVOsZeroNum(List ldata, String numfield) {
		if (ldata == null || ldata.size() == 0)
			return null;
		List<SuperVO> lnewData = new ArrayList<SuperVO>();
		List<SuperVO> ldata2 = (List<SuperVO>) ldata;
		for (SuperVO vo : ldata2) {
			if (PuPubVO.getUFDouble_NullAsZero(vo.getAttributeValue(numfield))
					.equals(WdsWlPubTool.DOUBLE_ZERO)) {
				continue;
			}
			lnewData.add(vo);
		}
		return lnewData;
	}
	
	/**
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ  ���ۼƻ�����ʱ    ���˳�ѡ���Ƿ��Զ����ţ�ʣ��û�а��ŵļ�¼����ť   ���Ҽƻ�������ƻ����������Ĳ������
	 * @ʱ�䣺2011-3-23����08:33:49
	 * @param ldata
	 * @return
	 */
	public static List<SuperVO> filterVOsisAutoSell(List ldata){

		
		if (ldata == null || ldata.size() == 0)
			return null;
		List<SuperVO> lnewData = new ArrayList<SuperVO>();
		List<SuperVO> ldata2 = (List<SuperVO>) ldata;
		for (SuperVO vo : ldata2) {
			if ((PuPubVO.getUFBoolean_NullAs(vo.getAttributeValue("isonsell"), UFBoolean.FALSE)).booleanValue() ) {
				//�ƻ�����������
				UFDouble plannum=PuPubVO.getUFDouble_NullAsZero(vo.getAttributeValue("nnumber"));
				//���������ŵ�����
				UFDouble dealnum=PuPubVO.getUFDouble_NullAsZero(vo.getAttributeValue("nnum"));
				//���ΰ��Ÿ�����
				UFDouble fdealnum=PuPubVO.getUFDouble_NullAsZero(vo.getAttributeValue("nassnum"));
				//�Ѱ�������
				UFDouble ounum=PuPubVO.getUFDouble_NullAsZero(vo.getAttributeValue("ntaldcnum"));
				//�Զ����ŵ�������
				UFDouble atonum=plannum.sub(dealnum).sub(ounum);
			    vo.setAttributeValue("nassnum",atonum.div(dealnum.div(fdealnum)) );				
			   	if(plannum.compareTo(dealnum)>0){
			   	 vo.setAttributeValue("nnum",atonum); 			   	 
			   	 lnewData.add(vo);
			   	}
			   	
			}		
			
		}
		return lnewData;	
	}	
	
	
	
	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ ���ⵥ�Զ��������ʱ У������
	 * @ʱ�䣺2011-4-3����03:18:13
	 * @param card
	 * @param bodys
	 * @throws ValidationException
	 */
	public static void validationOnPickAction(BillCardPanel card,TbOutgeneralBVO[] bodys) throws ValidationException{
		if (null == bodys || bodys.length == 0) {
			throw new ValidationException("�����޻�Ʒ����");
		}
		Object pk_stordoc = card.getHeadItem("srl_pk")
		.getValueObject();
		if (null == pk_stordoc || "".equals(pk_stordoc)) {
			throw new ValidationException("����ֿⲻ��Ϊ��");
		}
		
		String pk_cargdoc = PuPubVO.getString_TrimZeroLenAsNull(card.getHeadItem("pk_cargdoc").getValueObject());
		if(pk_cargdoc == null){
			throw new ValidationException("�����λ����Ϊ��");
		}
		for(TbOutgeneralBVO body:bodys){
			body.validationOnZdck();
		}
	}

	
	
	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ ���ⵥ�Զ������ ���������õ�����
	 * @ʱ�䣺2011-4-3����03:10:21
	 * @param bodys
	 * @param card
	 * @param trayInfor
	 */
	public static void setDatasToPanelForOutBill(TbOutgeneralBVO[] bodys,
			BillCardPanel card, Map<String, List<TbOutgeneralTVO>> trayInfor) {
		List<TbOutgeneralTVO> ltray = null;
		String key = null;
		int row = 0;
//		StringBuffer msg = new StringBuffer();
		java.util.List<String> ltmpbatch = null;
		for (TbOutgeneralBVO body : bodys) {
			card.setBodyValueAt(null, row, "noutnum");
			card.setBodyValueAt(null, row, "noutassistnum");
			card.setBodyValueAt(null, row, "vbatchcode");
			key = body.getCrowno();
			// ���ýӿڲ�ѯ������е�ʵ����������
			if (!trayInfor.containsKey(key))
				continue;
			ltray = trayInfor.get(key);

			if (ltray == null || ltray.size() == 0)
				continue;

			UFDouble noutnum = WdsWlPubTool.DOUBLE_ZERO;
			UFDouble nouatnum = WdsWlPubTool.DOUBLE_ZERO;
			UFDouble nmny = WdsWlPubTool.DOUBLE_ZERO;
			ltmpbatch=new ArrayList<String>();
			for (TbOutgeneralTVO trayVo : ltray) {
				noutnum = noutnum.add(PuPubVO.getUFDouble_NullAsZero(trayVo.getNoutnum()));
				nouatnum = nouatnum.add(PuPubVO.getUFDouble_NullAsZero(trayVo.getNoutassistnum()));
				nmny = nmny.add(PuPubVO.getUFDouble_NullAsZero(trayVo.getNmny()));
				if(ltmpbatch.contains(trayVo.getVbatchcode()))
					continue;
				if(ltmpbatch.size()>1)
					ltmpbatch.add(",");
				ltmpbatch.add(trayVo.getVbatchcode());
			}

			// ʵ������
			card.setBodyValueAt(noutnum, row, "noutnum");
			// ʵ��������
			card.setBodyValueAt(nouatnum, row, "noutassistnum");
			// ����
			card.setBodyValueAt(WdsWlPubTool.getSubSql2(ltmpbatch.toArray(new String[0])), row,
							"vbatchcode");
			// ����
			card.setBodyValueAt(ltray.get(0).getNprice(), row, "nprice");
			row++;

		}
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
	
	public static void setVOsRowNoByRule(
			AggregatedValueObject[] voaCA, String sRowNOKey) {
		if(voaCA == null || voaCA.length ==0)
			return;
		for(AggregatedValueObject voa:voaCA){
			setVOsRowNoByRule(voa.getChildrenVO(), sRowNOKey);
		}
	}
	
	private static Map<String,String> invCodeInfor = new HashMap<String, String>();
	
	public static String getInvCodeByInvid(String cinvid) {
		if(!invCodeInfor.containsKey(cinvid)){
			String formu = "invcode->getColValue(bd_invbasdoc,invcode,pk_invbasdoc,cinvid)";
			String[] names = new String[]{"cinvid"};
			String[] values = new String[]{cinvid};
			String rets;
			try {
				rets = getString_NullAsTrimZeroLen(execFomular(formu, names, values));
//				return rets;
				if(rets == null)
					rets = cinvid;
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return cinvid;
			}
			invCodeInfor.put(cinvid, rets);
		}
			
		return invCodeInfor.get(cinvid);
	}
	
private static Map<String,String> custNameInfor = new HashMap<String, String>();
	
	public static String getCustNameByid(String custmanid) {
		if(!custNameInfor.containsKey(custmanid)){
			String formu = "custname->getColValue(bd_cubasdoc,custname,pk_cubasdoc,getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,cumanid))";
			String[] names = new String[]{"cumanid"};
			String[] values = new String[]{custmanid};
			String rets;
			try {
				rets = getString_NullAsTrimZeroLen(execFomular(formu, names, values));
//				return rets;
				if(rets == null)
					rets = custmanid;
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return custmanid;
			}
			custNameInfor.put(custmanid, rets);
		}
			
		return custNameInfor.get(custmanid);
	}

	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2011-3-24����11:31:59
	 * @return ��ǰ��ʱ��
	 */
	public static UFDate getCurDate() {
		UFDate date = new UFDate(System.currentTimeMillis());
		return date;
	}
	
	/**
	 * 
	 * @���ߣ�lyf
	 * @˵��������Ψһ��У��
	 * @ʱ�䣺2011-6-7����04:25:50
	 * @param table
	 * @param model
	 * @param fields
	 * @param displays
	 * @throws Exception
	 */
	public static void beforeSaveBodyUnique(UITable table, BillModel model,
			String[] fields, String[] displays) throws Exception {
		int num = table.getRowCount();
		if (fields == null || fields.length == 0) {
			return;
		}
		if (num > 0) {
			ArrayList<String> list = new ArrayList<String>();
			for (int i = 0; i < num; i++) {
				String key = "";
				for (String str : fields) {
					Object o1 = model.getValueAt(i, str);
					key = key + "," + String.valueOf(o1);
				}
				String dis = "";
				for (int j = 0; j < displays.length; j++) {
					dis = dis + "[ " + displays[j] + " ]";
				}

				if (list.contains(key)) {

					throw new BusinessException("��[" + (i + 1) + "]�б����ֶ� "
							+ dis + " �����ظ�!");
				} else {
					list.add(key);
				}
			}
		}
	}
	
	/**
	 * �����������
	 * �������ڣ�(2011-5-24 14:32:51)
	 */
	public static  String[] splitCode(String value) throws BusinessException{
		java.util.StringTokenizer st = new java.util.StringTokenizer(value, " ,.+/\\:;", false);
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
			throw new BusinessException("������������"+getString_NullAsTrimZeroLen(e.getMessage()));
		}

		return showvalues;
	}
	
	private static final ArrayList<CircularlyAccessibleValueObject> tempVoList = new ArrayList<CircularlyAccessibleValueObject>();
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
		return tempVoList.toArray(new CircularlyAccessibleValueObject[0]);
	}
	
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ��������� ���ش��뵥��vo �ı�ͷvo
	 * 2011-5-5����10:17:20
	 * @param billvos  ����vo ����
	 * @return
	 */
	public static ArrayList<CircularlyAccessibleValueObject> getBodysVOFromAggBillVo(AggregatedValueObject[] billvos,Class parentClass){
		if(billvos == null || billvos.length ==0)
			return null;
		tempVoList.clear();
		for(AggregatedValueObject bill:billvos){
			if(bill.getChildrenVO() == null || bill.getChildrenVO().length ==0)
				continue;
			tempVoList.addAll(Arrays.asList(bill.getChildrenVO()));
		}
		if(tempVoList.size() == 0)
			return null;
		return tempVoList;
	}
	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ �ֲ����ʱ�Ƿ��Զ����� ƫ����
	 * @ʱ�䣺2011-7-14����11:11:24
	 * @param cstoreid
	 * @return
	 * @throws BusinessException
	 */
	public static boolean isAutoAdjustStore(String cstoreid)throws BusinessException {
		String fomular = "def2->getColValue(bd_stordoc,"+WdsWlPubConst.wds_warehouse_sytz+",pk_stordoc,cwhid)";
		String[] names = new String[]{"cwhid"};
		String[] values = new String[]{cstoreid};
		UFBoolean sytz = PuPubVO.getUFBoolean_NullAs(WdsWlPubTool.execFomular(fomular, names, values),UFBoolean.FALSE);
		return sytz.booleanValue();
	}
}
