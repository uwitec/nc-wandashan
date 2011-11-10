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
	 * @作者：zhf
	 * @说明：完达山物流项目 判断仓库是否总仓
	 * @时间：2011-3-23下午05:53:02
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

	public static final Integer INTEGER_ZERO_VALUE = new Integer(0); // 整数零

	public static final UFDouble DOUBLE_ZERO = new UFDouble(0f);

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
	 * @作者：zhf
	 * @说明：完达山物流项目 停止编辑公共方法
	 * @时间：2011-3-23下午08:12:29
	 * @param bm
	 */
	public static void stopEditing(BillModel bm) {
		BillItem[] items = bm.getBodyItems();
		if (items != null) {
			for (int i = 0; i < items.length; i++) {
				Component comp = items[i].getComponent();
				if (comp instanceof UIRefPane) {
					if (!((UIRefPane) comp).isProcessFocusLost()) {
						// System.out.println("处理:" + items[i].getName());
						((UIRefPane) comp).processFocusLost();
					}
				}
			}
		}

	}

	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 发运计划安排时过滤安排数量为0的行
	 * @时间：2011-3-23下午08:33:49
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
	 * @作者：mlr
	 * @说明：完达山物流项目  销售计划安排时    过滤出选中是否自动安排（剩余没有安排的记录）按钮   并且计划数量与计划安排数量的差大于零
	 * @时间：2011-3-23下午08:33:49
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
				//计划的销售数量
				UFDouble plannum=PuPubVO.getUFDouble_NullAsZero(vo.getAttributeValue("nnumber"));
				//本次主安排的数量
				UFDouble dealnum=PuPubVO.getUFDouble_NullAsZero(vo.getAttributeValue("nnum"));
				//本次安排辅数量
				UFDouble fdealnum=PuPubVO.getUFDouble_NullAsZero(vo.getAttributeValue("nassnum"));
				//已安排数量
				UFDouble ounum=PuPubVO.getUFDouble_NullAsZero(vo.getAttributeValue("ntaldcnum"));
				//自动安排的主数量
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
	 * @作者：zhf
	 * @说明：完达山物流项目 出库单自动拣货出库时 校验数据
	 * @时间：2011-4-3下午03:18:13
	 * @param card
	 * @param bodys
	 * @throws ValidationException
	 */
	public static void validationOnPickAction(BillCardPanel card,TbOutgeneralBVO[] bodys) throws ValidationException{
		if (null == bodys || bodys.length == 0) {
			throw new ValidationException("表体无货品数据");
		}
		Object pk_stordoc = card.getHeadItem("srl_pk")
		.getValueObject();
		if (null == pk_stordoc || "".equals(pk_stordoc)) {
			throw new ValidationException("出库仓库不能为空");
		}
		
		String pk_cargdoc = PuPubVO.getString_TrimZeroLenAsNull(card.getHeadItem("pk_cargdoc").getValueObject());
		if(pk_cargdoc == null){
			throw new ValidationException("出库货位不能为空");
		}
		for(TbOutgeneralBVO body:bodys){
			body.validationOnZdck();
		}
	}

	
	
	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 出库单自动拣货后 将数据设置到界面
	 * @时间：2011-4-3下午03:10:21
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
			// 调用接口查询缓存表中的实出主辅数量
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

			// 实发数量
			card.setBodyValueAt(noutnum, row, "noutnum");
			// 实发辅数量
			card.setBodyValueAt(nouatnum, row, "noutassistnum");
			// 批次
			card.setBodyValueAt(WdsWlPubTool.getSubSql2(ltmpbatch.toArray(new String[0])), row,
							"vbatchcode");
			// 单价
			card.setBodyValueAt(ltray.get(0).getNprice(), row, "nprice");
			row++;

		}
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
	 * @作者：lyf
	 * @说明：完达山物流项目
	 * @时间：2011-3-24上午11:31:59
	 * @return 当前的时间
	 */
	public static UFDate getCurDate() {
		UFDate date = new UFDate(System.currentTimeMillis());
		return date;
	}
	
	/**
	 * 
	 * @作者：lyf
	 * @说明：表体唯一性校验
	 * @时间：2011-6-7下午04:25:50
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

					throw new BusinessException("第[" + (i + 1) + "]行表体字段 "
							+ dis + " 存在重复!");
				} else {
					list.add(key);
				}
			}
		}
	}
	
	/**
	 * 解析编码规则。
	 * 创建日期：(2011-5-24 14:32:51)
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
			System.out.println("解析比例出错！");
			throw new BusinessException("解析比例出错！"+getString_NullAsTrimZeroLen(e.getMessage()));
		}

		return showvalues;
	}
	
	private static final ArrayList<CircularlyAccessibleValueObject> tempVoList = new ArrayList<CircularlyAccessibleValueObject>();
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
		return tempVoList.toArray(new CircularlyAccessibleValueObject[0]);
	}
	
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）公用组件 返回传入单据vo 的表头vo
	 * 2011-5-5上午10:17:20
	 * @param billvos  单据vo 数组
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
	 * @作者：zhf
	 * @说明：完达山物流项目 分仓入库时是否自动调整 偏差量
	 * @时间：2011-7-14上午11:11:24
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
