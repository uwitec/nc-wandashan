package nc.ui.wl.pub.report;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.table.TableColumnModel;

import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.table.GroupableTableHeader;
import nc.ui.pub.bill.IBillItem;
import nc.ui.pub.report.ReportGeneralUtil;
import nc.ui.pub.report.ReportItem;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.cquery.FldgroupVO;
import nc.vo.pub.rs.MemoryResultSet;
import nc.vo.pub.rs.MemoryResultSetMetaData;
/**
 * 报表中经常要用到 将 查询出来的某一列的数据 或 某几列的数据 展开 到行上 形成二维的动态表 什么是一维表？ 所谓的一维表就是 列是固定不变的
 * 而行上的数据是动态变化的 也就是 横坐标 不变 纵坐标变
 * 
 * 这种数据 是应该都知道
 * 
 * 那么什么是二维表呢  所谓的二维表就是 行 和 列都是动态变化的
 * 
 * 也就是 会将查询出来的 某一列的数据 动态展开 放到行上面
 * 
 * @author mlr 2011-12-11 ----- 2011-12-16 
 */
public class ReportRowColCrossTool {
	private static ReportItem[] m_crsResultValItems = null;
	// 构建二维表的复合列表头的表头数据
	private static FldgroupVO[] fldgroupVOs = null;
	// 过滤尺寸
	private static int fielterNum = 0;
	// 交叉列展开得初始位置
	private static int itemstart = 0;

	// 保存多表头列的维度数组
	// 用于在数据交叉完成后
	// 利用该维度 构建 复合多表头列
	/**
	 * 报表数据交叉 构建二维表的工具类
	 * 
	 * @author mlr
	 * @说明：（鹤岗矿业） 2011-12-15上午11:20:17
	 * @param strCrsRows
	 *            要交叉的行
	 * @param strCrsCols
	 *            要交叉的列
	 * @param strCrsVals
	 *            要交叉的值
	 * @throws Exception
	 */
	public static void onCross(ReportBaseUI ui, String[] strCrsRows,
			String[] strCrsCols, String[] strCrsVals) throws Exception {
		// 交叉行 的数据 要看作 一个合并的维度
		// 按交叉行的 联合字段的维度 进行 交叉列的数据合并操作
		if (strCrsRows == null || strCrsRows.length == 0)
			throw new Exception("交叉行不能为空");
		if (strCrsCols == null || strCrsCols.length == 0)
			throw new Exception("交叉列不能为空");
		if (strCrsVals == null || strCrsVals.length == 0)
			throw new Exception("交叉值不能为空");
		// 构建交叉行 联合维度
		StringBuffer comRows = new StringBuffer();
		for (int i = 0; i < strCrsRows.length; i++) {
			comRows.append(strCrsRows[i]);
			if (i != strCrsRows.length - 1) {
				comRows.append(" ");
			}
		}
		StringBuffer comCols = new StringBuffer();
		String[] cols = new String[strCrsCols.length + 1];
		for (int i = 0; i < strCrsCols.length; i++) {
			comCols.append(strCrsCols[i]);
			cols[i] = strCrsCols[i];
			if (i != strCrsCols.length - 1) {
				comCols.append(" ");
			}
		}
		cols[cols.length - 1] = "&type";
		fielterNum = strCrsVals.length;// 过滤维度
		itemstart = strCrsRows.length;// 记录交叉列生成的初始位置
		String[] strows = new String[] { comRows.toString() };
		drawCrossTable(ui, strows,
				new String[] { comCols.toString(), "&type" }, strCrsVals);
		// drawCrossTable(ui,strows,strCrsCols,strCrsVals);

	}

	/**
	 * × 报表交叉函数。须传入交叉行，交叉列，及交叉值数组
	 * 
	 * @param rows
	 *            java.lang.String[]
	 * @param columns
	 *            java.lang.String[]
	 */
	protected static void drawCrossTable(ReportBaseUI ui, String[] strCrsRows,
			String[] strCrsCols, String[] strCrsVals) throws Exception {
		// 构造内存结果集元数据
		MemoryResultSetMetaData mrsmd = getReportGeneralUtil(ui)
				.createMeteData();
		// 构造内存结果集
		Vector dataVec = ui.getReportBase().getBillModel().getDataVector();
		MemoryResultSet mrsOrg = getReportGeneralUtil(ui).vector2Mrs(dataVec,
				mrsmd);
		ZmCrossTable ct = new ZmCrossTable();
		ct.setStrSp("_");
		ct.setMrsOrg(mrsOrg);
		ct.setCrsRows(strCrsRows);
		ct.setCrsCols(strCrsCols);
		ct.setCrsVals(strCrsVals);
		ReportItem[] risOrg = ui.getReportBase().getBody_Items();
		String[] strMrsOrgDisNames = new String[risOrg.length];
		for (int i = 0; i < risOrg.length; i++)
			strMrsOrgDisNames[i] = risOrg[i].getName();
		ct.setMrsOrgColDisNames(strMrsOrgDisNames);
		MemoryResultSet mrsCrs = ct.getCrossTableMrs();
		ReportItem[] risNew = getCrossBody_Items(ui, strCrsRows.length, mrsCrs);
		FldgroupVO[] fgvos = ct.getCrossTableFldGrpVOs();
		if (fgvos != null)
			// ui.getReportBase().setFieldGroup(fgvos);
			ui.getReportBase().setBody_Items(risNew);
		Vector vecBodyDataVec = getReportGeneralUtil(ui).mrs2Vector(mrsCrs);
		ui.getReportBase().getBillModel().setDataVector(vecBodyDataVec);	
	  //  onTotal(ui,5);
		createItem(fgvos, ui,true);
		ui.getBodyDataVO();
		//ui.getReportBase().getBillModel().execLoadFormula();
		
	}
	/**
	 * 交叉二维表 按交叉字段进行横向数据合计
	 * 没有实现用不了
	 * @author mlr
	 * @说明：（鹤岗矿业）
	 * 2011-12-19上午09:56:24
	 * @param ui
	 */
	public static void onTotal(ReportBaseUI ui,int start){
		if(ui==null)
			return;
		indexs.clear();
		ReportItem[] items=(ReportItem[]) ui.getReportBase().getBillModel().getBodyItems();
		ArrayList<ReportItem> ls=new ArrayList<ReportItem>();
	    Map<String,List<ReportItem>> map=new HashMap<String,List<ReportItem>>();//重新构建item 
		if(items==null || items.length==0)
			return;
		for(int i=0;i<5;i++){
		 ls.add(items[i]);						
	   }
		for(int i=5;i<items.length;i++){
		   if(items[i].getNote()==null || items[i].getNote().length()==0){
			   continue;
		   }
		   Add(map,items[i]);			
		}
		
		for(int i=0;i<indexs.size();i++){
		   List<ReportItem> list=map.get(indexs.get(i));
		   ReportItem item=ReportPubTool.getItem("numˉ"+i, "合计", IBillItem.DECIMAL, 2, 80);
		   //构建合计的公式
		   String  st="";
		   for(int j=0;j<list.size();j++){
			   st=st+"+"+list.get(j).getKey();
		   }
		   st="numˉ"+i+" -> "+st.substring(1);
		   item.setLoadFormula(new String[]{st});
 		   list.add(item);
	    }	
		//构建带合计的item
		for(int i=0;i<indexs.size();i++){
		   List<ReportItem> list=map.get(indexs.get(i));
	       for(int j=0;j<list.size();j++){
	    	   ls.add(list.get(j));
	       }
	    }
		ui.getReportBase().setBody_Items(ls.toArray(new ReportItem[0]));
		//ui.getReportBase().getBillModel().execLoadFormula();
	}
	private static ArrayList<String> indexs=new ArrayList<String>();//记录重构的item的顺序
	private static void Add(Map<String,List<ReportItem>> map, ReportItem item) {
	   String[] cls=item.getNote().split("ˉ");
       if(map.get(cls[0])==null || map.get(cls[0]).size()==0){
    	   List<ReportItem> list=new ArrayList<ReportItem>();
    	   map.put(cls[0],list);
    	   list.add(item);
    	   indexs.add(cls[0]);
    	   return;
       }
	   map.get(cls[0]).add(item);
	}

	/**
	 * 
       不支持合计
	 * 构建交叉表的复合多表头 主要用于 交叉列有多列的情况下 构建 复合多表头 该方法 主要采用 数据结构中的 递归运算的的方式 来构建 多表头序列
	 * 比较难以看懂 和 维护
	 * 查询引擎当中  有构建复合列表头 的方法
	 * 但是 那个方法构建的复合列表头 如果存在多个交叉列的情况下
	 * 会导致 构建的复合列表头的数据出错 ，即构建的复合列表头的item 是完全错误的
	 * 所以 ： 写了本方法用于构建复合列表头数据
	 * @author mlr
	 * @说明：（鹤岗矿业） 2011-12-16下午03:54:16
	 * @param fgvos
	 * @param ui
	 */
	private static void createItem(FldgroupVO[] fgvos, ReportBaseUI ui,boolean istotal) {
		if (fgvos == null || fgvos.length == 0)
			return;
		UITable cardTable = ui.getReportBase().getBillTable();
		GroupableTableHeader cardHeader = (GroupableTableHeader) cardTable
				.getTableHeader();
		TableColumnModel cardTcm = cardTable.getColumnModel();	
		// 记录
		List<ZmColumnGroup> list = new ArrayList<ZmColumnGroup>();
		int start = itemstart;
		int cossize = fielterNum;
		int size = 0;// 记录多表头的个数
		FldgroupVO[] fgvos1 = filter(fgvos, cossize - 1);
		if(istotal==false){
		  for (int i = 0; i < fgvos1.length; i++) {
			String[] cbs = fgvos1[i].getGroupname().split("ˉ");
			size = cbs.length;
			String colName = cbs[cbs.length - 1];
			String name = "";
			for (int k = 0; k < cbs.length - 1; k++) {
				name = name + cbs[k];
			}
			ZmColumnGroup cp = new ZmColumnGroup(colName);
			cp.setParentName(name);
			cp.setName(name + colName);
			for (int j = start; j < start + fielterNum; j++) {
				cp.add(cardTcm.getColumn(j));
			}
			start = start + fielterNum;
			list.add(cp);
		  }
		}else{
		    for (int i = 0; i < fgvos1.length; i++) {
					String[] cbs = fgvos1[i].getGroupname().split("ˉ");
					size = cbs.length;
					String colName = cbs[cbs.length - 1];
					String name = "";
					for (int k = 0; k < cbs.length - 1; k++) {
						name = name + cbs[k];
					}
					ZmColumnGroup cp = new ZmColumnGroup(colName);
					cp.setParentName(name);
					cp.setName(name + colName);
					for (int j = start; j < start + fielterNum; j++) {
						String st=ui.getReportBase().getBillModel().getColumnName(j );
						if("合计".equals(st)){
							continue;
						}
						cp.add(cardTcm.getColumn(j));
					}
					start = start + fielterNum;
					list.add(cp);
		  }		
		}
		Map<Integer, List<ZmColumnGroup>> map = new HashMap<Integer, List<ZmColumnGroup>>();
		map.put(size - 2, list);
		//开始递归运算 构建复合列表头
		for (int x = size - 2; x >= 0; x--) {
			// /////////////////
			List<ZmColumnGroup> list1 = map.get(x);
			List<ZmColumnGroup> flist = new ArrayList<ZmColumnGroup>();
			for (int i = 0; i < fgvos1.length; i++) {
				String[] cbs = fgvos1[i].getGroupname().split("ˉ");
				String name = cbs[x];
				String sname = "";
				for (int k = 0; k < x; k++) {
					sname = sname + cbs[k];
				}
				if (!isExist(sname + name, flist)) {
					ZmColumnGroup cp = new ZmColumnGroup(name);
					cp.setParentName(sname);
					cp.setName(sname + name);
					flist.add(cp);
				}
			}
			for (int i = 0; i < flist.size(); i++) {
				String name = flist.get(i).getName();
				for (int j = 0; j < list1.size(); j++) {
					if (name.equals(list1.get(j).getParentName())) {
						flist.get(i).add(list1.get(j));
					}
				}
			}
			map.put(x - 1, flist);
		}
		List<ZmColumnGroup> flist1 = new ArrayList<ZmColumnGroup>();
		flist1 = map.get(-1);
		// //////////
		for (int i = 0; i < flist1.size(); i++) {
			cardHeader.addColumnGroup(flist1.get(i));
		}
		ui.getReportBase().getBillModel().updateValue();
	}

	private static boolean isExist(String name, List<ZmColumnGroup> flist) {
		if (name == null) {
			return false;
		}
		if (flist == null || flist.size() == 0)
			return false;
		boolean isExist = false;
		for (int i = 0; i < flist.size(); i++) {
			if (flist.get(i).getName().equals(name)) {
				isExist = true;
				break;
			}
		}
		return isExist;
	}
	/**
	 * 按尺寸过滤数据
	 * @author mlr
	 * @说明：（鹤岗矿业） 2011-12-16上午09:17:45
	 * @param fgvos
	 * @param cossize
	 * @return
	 */
	private static FldgroupVO[] filter(FldgroupVO[] fgvos, int cossize) {
		if (fgvos == null || fgvos.length == 0)
			return null;
		if(cossize==0)
			cossize=1;
		List<FldgroupVO> list = new ArrayList<FldgroupVO>();
		for (int i = 0; i < fgvos.length; i = i + cossize) {
			list.add(fgvos[i]);
		}
		return list.toArray(new FldgroupVO[0]);
	}
	
	protected static ReportGeneralUtil getReportGeneralUtil(ReportBaseUI ui) {
		return ui.getReportBase().getReportGeneralUtil();
	}
	/**
	 * 通过结果集 获取表体item 创建日期:(2002-11-5 17:34:05)
	 * @author mlr 来自查询引擎 单查询引擎 有错误 本方法已经修正
	 * @return nc.ui.pub.report.ReportItem[]
	 */
	protected static ReportItem[] getCrossBody_Items(ReportBaseUI ui,
			int lenRows, MemoryResultSet mrsCrs) throws Exception {
		MemoryResultSetMetaData mrsmd = mrsCrs.getMetaData0();
		ReportItem[] riCrs = new ReportItem[mrsmd.getColumnCount()];
		Vector vecResultValueItem = new Vector();
		for (int i = 0; i < mrsmd.getColumnCount(); i++) {
			ReportItem ri = new ReportItem();
			String strKey = mrsmd.getColumnName(i + 1);
			if (i < lenRows) {
				ri.setKey(strKey);
			} else {
				StringTokenizer st = new StringTokenizer(strKey, "_");
				StringBuffer sb = new StringBuffer();
				while (st.hasMoreElements()) {
					String letter = StringUtil.getPYIndexStr(st.nextElement()
							.toString(), true);
					sb.append(letter);
					sb.append("_");
				}
				ri.setKey(sb.toString());
				ri.setNote(strKey);
				vecResultValueItem.add(ri);
			}

			ri.setDataType(getReportGeneralUtil(ui).sqlType2itemType(
					mrsmd.getColumnType(i + 1)));
			ReportItem riOrg = ui.getReportBase().getBody_Item(strKey);
			if (riOrg != null)
				ri.setName(riOrg.getName());
			else if (strKey.equals("&type")) {
				ri.setName("");
			} else if (strKey.equals("value")) {
				ri.setName("");
			} else {
				int index = strKey.lastIndexOf("_");
				ri.setName(strKey.substring(index + 1));
			}
			ri.setWidth(80);
			riCrs[i] = ri;
		}
		m_crsResultValItems = (ReportItem[]) vecResultValueItem
				.toArray(new ReportItem[0]);
		return riCrs;
	}
	private class Item {
		private String code = null;

	}
}
