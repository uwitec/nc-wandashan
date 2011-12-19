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
 * �����о���Ҫ�õ� �� ��ѯ������ĳһ�е����� �� ĳ���е����� չ�� ������ �γɶ�ά�Ķ�̬�� ʲô��һά�� ��ν��һά����� ���ǹ̶������
 * �����ϵ������Ƕ�̬�仯�� Ҳ���� ������ ���� �������
 * 
 * �������� ��Ӧ�ö�֪��
 * 
 * ��ôʲô�Ƕ�ά����  ��ν�Ķ�ά����� �� �� �ж��Ƕ�̬�仯��
 * 
 * Ҳ���� �Ὣ��ѯ������ ĳһ�е����� ��̬չ�� �ŵ�������
 * 
 * @author mlr 2011-12-11 ----- 2011-12-16 
 */
public class ReportRowColCrossTool {
	private static ReportItem[] m_crsResultValItems = null;
	// ������ά��ĸ����б�ͷ�ı�ͷ����
	private static FldgroupVO[] fldgroupVOs = null;
	// ���˳ߴ�
	private static int fielterNum = 0;
	// ������չ���ó�ʼλ��
	private static int itemstart = 0;

	// ������ͷ�е�ά������
	// ���������ݽ�����ɺ�
	// ���ø�ά�� ���� ���϶��ͷ��
	/**
	 * �������ݽ��� ������ά��Ĺ�����
	 * 
	 * @author mlr
	 * @˵�������׸ڿ�ҵ�� 2011-12-15����11:20:17
	 * @param strCrsRows
	 *            Ҫ�������
	 * @param strCrsCols
	 *            Ҫ�������
	 * @param strCrsVals
	 *            Ҫ�����ֵ
	 * @throws Exception
	 */
	public static void onCross(ReportBaseUI ui, String[] strCrsRows,
			String[] strCrsCols, String[] strCrsVals) throws Exception {
		// ������ ������ Ҫ���� һ���ϲ���ά��
		// �������е� �����ֶε�ά�� ���� �����е����ݺϲ�����
		if (strCrsRows == null || strCrsRows.length == 0)
			throw new Exception("�����в���Ϊ��");
		if (strCrsCols == null || strCrsCols.length == 0)
			throw new Exception("�����в���Ϊ��");
		if (strCrsVals == null || strCrsVals.length == 0)
			throw new Exception("����ֵ����Ϊ��");
		// ���������� ����ά��
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
		fielterNum = strCrsVals.length;// ����ά��
		itemstart = strCrsRows.length;// ��¼���������ɵĳ�ʼλ��
		String[] strows = new String[] { comRows.toString() };
		drawCrossTable(ui, strows,
				new String[] { comCols.toString(), "&type" }, strCrsVals);
		// drawCrossTable(ui,strows,strCrsCols,strCrsVals);

	}

	/**
	 * �� �����溯�����봫�뽻���У������У�������ֵ����
	 * 
	 * @param rows
	 *            java.lang.String[]
	 * @param columns
	 *            java.lang.String[]
	 */
	protected static void drawCrossTable(ReportBaseUI ui, String[] strCrsRows,
			String[] strCrsCols, String[] strCrsVals) throws Exception {
		// �����ڴ�����Ԫ����
		MemoryResultSetMetaData mrsmd = getReportGeneralUtil(ui)
				.createMeteData();
		// �����ڴ�����
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
	 * �����ά�� �������ֶν��к������ݺϼ�
	 * û��ʵ���ò���
	 * @author mlr
	 * @˵�������׸ڿ�ҵ��
	 * 2011-12-19����09:56:24
	 * @param ui
	 */
	public static void onTotal(ReportBaseUI ui,int start){
		if(ui==null)
			return;
		indexs.clear();
		ReportItem[] items=(ReportItem[]) ui.getReportBase().getBillModel().getBodyItems();
		ArrayList<ReportItem> ls=new ArrayList<ReportItem>();
	    Map<String,List<ReportItem>> map=new HashMap<String,List<ReportItem>>();//���¹���item 
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
		   ReportItem item=ReportPubTool.getItem("num��"+i, "�ϼ�", IBillItem.DECIMAL, 2, 80);
		   //�����ϼƵĹ�ʽ
		   String  st="";
		   for(int j=0;j<list.size();j++){
			   st=st+"+"+list.get(j).getKey();
		   }
		   st="num��"+i+" -> "+st.substring(1);
		   item.setLoadFormula(new String[]{st});
 		   list.add(item);
	    }	
		//�������ϼƵ�item
		for(int i=0;i<indexs.size();i++){
		   List<ReportItem> list=map.get(indexs.get(i));
	       for(int j=0;j<list.size();j++){
	    	   ls.add(list.get(j));
	       }
	    }
		ui.getReportBase().setBody_Items(ls.toArray(new ReportItem[0]));
		//ui.getReportBase().getBillModel().execLoadFormula();
	}
	private static ArrayList<String> indexs=new ArrayList<String>();//��¼�ع���item��˳��
	private static void Add(Map<String,List<ReportItem>> map, ReportItem item) {
	   String[] cls=item.getNote().split("��");
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
       ��֧�ֺϼ�
	 * ���������ĸ��϶��ͷ ��Ҫ���� �������ж��е������ ���� ���϶��ͷ �÷��� ��Ҫ���� ���ݽṹ�е� �ݹ�����ĵķ�ʽ ������ ���ͷ����
	 * �Ƚ����Կ��� �� ά��
	 * ��ѯ���浱��  �й��������б�ͷ �ķ���
	 * ���� �Ǹ����������ĸ����б�ͷ ������ڶ�������е������
	 * �ᵼ�� �����ĸ����б�ͷ�����ݳ��� ���������ĸ����б�ͷ��item ����ȫ�����
	 * ���� �� д�˱��������ڹ��������б�ͷ����
	 * @author mlr
	 * @˵�������׸ڿ�ҵ�� 2011-12-16����03:54:16
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
		// ��¼
		List<ZmColumnGroup> list = new ArrayList<ZmColumnGroup>();
		int start = itemstart;
		int cossize = fielterNum;
		int size = 0;// ��¼���ͷ�ĸ���
		FldgroupVO[] fgvos1 = filter(fgvos, cossize - 1);
		if(istotal==false){
		  for (int i = 0; i < fgvos1.length; i++) {
			String[] cbs = fgvos1[i].getGroupname().split("��");
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
					String[] cbs = fgvos1[i].getGroupname().split("��");
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
						if("�ϼ�".equals(st)){
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
		//��ʼ�ݹ����� ���������б�ͷ
		for (int x = size - 2; x >= 0; x--) {
			// /////////////////
			List<ZmColumnGroup> list1 = map.get(x);
			List<ZmColumnGroup> flist = new ArrayList<ZmColumnGroup>();
			for (int i = 0; i < fgvos1.length; i++) {
				String[] cbs = fgvos1[i].getGroupname().split("��");
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
	 * ���ߴ��������
	 * @author mlr
	 * @˵�������׸ڿ�ҵ�� 2011-12-16����09:17:45
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
	 * ͨ������� ��ȡ����item ��������:(2002-11-5 17:34:05)
	 * @author mlr ���Բ�ѯ���� ����ѯ���� �д��� �������Ѿ�����
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
