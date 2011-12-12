package nc.ui.wl.pub.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.ui.pub.bill.IBillItem;
import nc.ui.pub.report.ReportItem;
import nc.vo.wl.pub.report.ReportBaseVO;

/**
 * 
 * @author zpm
 * 
 */
public class ReportCrossUtil {

	protected ReportBaseVO[] baseVOS = null;

	protected Map<String,ReportBaseVO> map = new HashMap<String,ReportBaseVO>();
	
	//记录表体ITEMKEY
	protected ArrayList<CodeName> list_item = new ArrayList<CodeName>();//记录开始交叉行的列名字段
	
	protected ArrayList<CodeName> list_item1 = new ArrayList<CodeName>();//记录本月费用的交叉行的列名字段
	
	protected ArrayList<CodeName> list_item2 = new ArrayList<CodeName>();//记录拖欠费用的交叉行的列名字段
	
	public ReportCrossUtil(ReportBaseVO[] baseVOS) {
		this.baseVOS = baseVOS;
	}
	/**
	 * 重构报表
	 * @param strCrsRows<交叉行[code,name]>
	 * @param strCrsCols<交叉列>
	 * @param strCrsVals<交叉值>
	 */
	public void drawCrossTable(String[][] strCrsRows, String strCrsCols,String[] strCrsVals) {
		if(baseVOS != null && baseVOS.length > 0){
			for(ReportBaseVO basevo : baseVOS){
				//处理行
				ReportBaseVO new_basevo = null;
				String o_row = null;
				if(strCrsRows!=null && strCrsRows.length>0){
					for(int j = 0 ;j < strCrsRows[0].length; j++){
						String rowstr = strCrsRows[0][j];
						if(o_row != null){
							String n_word = basevo.getAttributeValue(rowstr) == null ? "" : (String)basevo.getAttributeValue(rowstr);
							o_row = o_row + "," + n_word;
						}else{
							o_row = basevo.getAttributeValue(rowstr) == null ? "" : (String)basevo.getAttributeValue(rowstr);
						}
					}
					if(list_item.size() == 0 ){
						for(int i = 0 ;i < strCrsRows[0].length; i++){
							CodeName codename = new CodeName();
							codename.setCode(strCrsRows[0][i]);
							codename.setName(strCrsRows[1][i]);
							codename.setDataType(IBillItem.STRING);
							codename.setLength(strCrsRows[1][i].length()+10);
							list_item.add(codename);
						}
					}
					//
					if(!map.containsKey(o_row)){
						new_basevo = new ReportBaseVO();
						for(int i = 0 ;i < strCrsRows[0].length; i++){
							new_basevo.setAttributeValue(strCrsRows[0][i], basevo.getAttributeValue(strCrsRows[0][i]));
						}
						map.put(o_row, new_basevo);
					}
				}
				//处理列
				if(strCrsCols!=null && !"".equals(strCrsCols)){
					Object obj = basevo.getAttributeValue(strCrsCols);
					if(obj!=null && !"".equals(obj)){
						Object o1 = basevo.getAttributeValue(strCrsVals[0]);
						Object o2 = basevo.getAttributeValue(strCrsVals[1]);
						if(o1!=null && !"".equals(o1)){
							CodeName codename = new CodeName();
							codename.setCode("本月"+(String)obj);
							codename.setName((String)obj);
							codename.setDataType(IBillItem.DECIMAL);
							codename.setLength(((String)obj).length()+10);
							boolean flag = isExists(list_item1,codename);
							if(flag){
								list_item1.add(codename);
							}
							//
							ReportBaseVO basevo_now = map.get(o_row);
							basevo_now.setAttributeValue("本月"+(String)obj, o1);
						}
						if(o2!=null && !"".equals(o2)){
							CodeName codename = new CodeName();
							codename.setCode("往月"+(String)obj);
							codename.setName((String)obj);
							codename.setDataType(IBillItem.DECIMAL);
							codename.setLength(((String)obj).length()+10);
							boolean flag = isExists(list_item2,codename);
							if(flag){
								list_item2.add(codename);
							}
							//
							ReportBaseVO basevo_now = map.get(o_row);
							basevo_now.setAttributeValue("往月"+(String)obj, o2);
						}
					}
				}
			}
		}
	}
	public boolean isExists(List<CodeName> list,CodeName codename){
		boolean flag = true;
		if(list!=null){
			for(CodeName sts : list){
				if(sts.getCode().equals(codename.getCode())){
					flag = false;
					break;
				}
			}
		}
		return flag;
	}
	public ReportBaseVO[] getReportVO(){
		ReportBaseVO[] vos = null;
		ArrayList<ReportBaseVO> list = new ArrayList<ReportBaseVO>();
		if(map!=null && map.size()>0){
			Iterator<String> it = map.keySet().iterator();
			while(it.hasNext()){
				ReportBaseVO vo = map.get(it.next());
				list.add(vo);
			}
		}
		if(list.size()>0){
			vos = new ReportBaseVO[list.size()];
			list.toArray(vos);
		}
		return vos;
	}
	public ReportItem[] getReportItem(){
		ReportItem[] items = null;
		ArrayList<ReportItem> list = getAllReportItem(list_item);
		ArrayList<ReportItem> list1 = getAllReportItem(list_item1);
		ArrayList<ReportItem> list2 = getAllReportItem(list_item2);
		list1.addAll(list2);
		list.addAll(list1);
		//增加合计列
			ReportItem it = new ReportItem();
			it.setKey("total_sum");
			it.setName("合计");
			it.setShow(true);
			it.setDataType(IBillItem.DECIMAL);
			it.setEdit(false);
			it.setWidth(100);
		//
		list.add(it);	
		if(list!=null && list.size()>0){
			items = new ReportItem[list.size()];
			list.toArray(items);
		}
		return items;
	}
	public int getList_itemSize(){
		return list_item.size();
	}
	public int getList_item1Size(){
		return list_item1.size();
	}
	public int getList_item2Size(){
		return list_item2.size();
	}
	public ArrayList<ReportItem> getAllReportItem(ArrayList<CodeName> list){
		ArrayList<ReportItem> l_rpt = new ArrayList<ReportItem>();
		if(list!=null && list.size()>0){
			for(CodeName t : list){
				ReportItem it = new ReportItem();
				it.setKey(t.getCode());
				it.setName(t.getName());
				it.setShow(true);
				it.setDataType(t.getDataType());
				it.setEdit(false);
//				it.setWidth(t.getLength());
				it.setWidth(80);
				l_rpt.add(it);
			}
		}
		return l_rpt;
	}
	
	public class CodeName {
		public String code = null;
		public String name = null;
		public int DataType = -1; 
		public int length = 0;
		public int getLength() {
			return length;
		}
		public void setLength(int length) {
			this.length = length;
		}
		public int getDataType() {
			return DataType;
		}
		public void setDataType(int dataType) {
			DataType = dataType;
		}
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
	}
}
