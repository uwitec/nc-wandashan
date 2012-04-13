package nc.ui.wds.ic.so.out;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import nc.ui.pub.bill.BillListPanel;
import nc.ui.trade.pub.ListPanelPRTS;
import nc.ui.wl.pub.LongTimeTask;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.zmpub.pub.report.ReportBaseVO;
/**
 * 列表打印
 * @author xjx  
 *
 */
public class MyListSoDateSource extends ListPanelPRTS {
	private BillListPanel m_billcardpanel = null;

	public MyListSoDateSource(String moduleName, BillListPanel billcardpanel) {
		super(moduleName, billcardpanel);
		this.m_billcardpanel = billcardpanel;
		try {
			setTpInfors();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * gebb_num ninassistnum
	 */
	private static final long serialVersionUID = 1L;

	Map<Integer, ArrayList<String>> tpInfor = null;// 记录出库单所以的托盘信息<当前 表体行,托盘信息>

	public void setTpInfors() throws Exception {
		if (tpInfor == null) {
			tpInfor = new HashMap<Integer, ArrayList<String>>();
		}
		String[] general_b_pks = super.getItemValuesByExpress("general_b_pk");// 得到所有的表体主键信息
		String classname = "nc.bs.wds.ic.so.out.SoOutBO";
		String methodname = "getCorpTP";
		Class[] ParameterTypes = new Class[] { String.class };
		if (general_b_pks != null && general_b_pks.length > 0) {
			for (int i = 0; i < general_b_pks.length; i++) {
				Object[] ParameterValues = new Object[] { general_b_pks[i] };
				ReportBaseVO[] list = (ReportBaseVO[]) LongTimeTask
						.callRemoteService(WdsWlPubConst.WDS_WL_MODULENAME,
								classname, methodname, ParameterTypes,
								ParameterValues, 2);
				ArrayList<String> tp = new ArrayList<String>();
				if (list != null && list.length > 0) {
					for (int j = 0; j < list.length; j++) {
						// getItemValuesByExpress(itemExpress);
						StringBuffer bur = new StringBuffer();
						bur.append("托盘编号："
								+ list[j].getAttributeValue("cdt_traycode"));
						bur.append("实入辅数量: "
								+ list[j].getAttributeValue("noutassistnum")
								+ ",");
						bur.append("\n");
						tp.add(bur.toString());
					}
				}
				tpInfor.put(i, tp);
			}
		}
	}

	@Override
	public String[] getItemValuesByExpress(String itemExpress) {

		if(itemExpress.startsWith("h_")||itemExpress.startsWith("t_")){
			return super.getItemValuesByExpress(itemExpress);		
			}else{
			int rowCount=m_billcardpanel.getBodyBillModel().getRowCount();
			String[] retStr=new String[rowCount];
			for(int i=0;i<rowCount;i++){
	//			BillItem item=	m_billcardpanel.getBodyBillModel().getBodyItems()[i];

				for(int j=0;j<rowCount;j++){
				Object obj=m_billcardpanel.getBodyBillModel().getValueAt(j,itemExpress);
				if(obj!=null)
					retStr[j]=obj.toString();
				else
					retStr[j]="";
				}
				
				if (itemExpress.equals("general_b_pk")) {
					ArrayList<String>  list = new ArrayList<String>();
					Set<Integer> set = tpInfor.keySet();
					for(Integer key:set){
						list.addAll(tpInfor.get(key));
					}
					return list.toArray(new String[0]);	
				}
				// lyf begin 将托盘分担到表体对应行：如果该行有多个托盘，则要增加空行
				ArrayList<String> list = new ArrayList<String>();
				for (int row1 = 0; row1 < retStr.length; row1++) {
					list.add(retStr[row1]);
					ArrayList<String> tp = tpInfor.get(row1);
					for (int m = 1; m < tp.size(); m++) {
						list.add("");
					}
				}
				return list.toArray(new String[0]);
				// lyf end
			}
			return null;
		}			
	}
}
