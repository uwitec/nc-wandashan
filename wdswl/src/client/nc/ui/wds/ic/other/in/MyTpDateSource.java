package nc.ui.wds.ic.other.in;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillItem;
import nc.ui.trade.pub.CardPanelPRTS;
import nc.ui.wl.pub.LongTimeTask;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.report.ReportBaseVO;

//  xjx   add  卡片型打印
public class MyTpDateSource extends CardPanelPRTS {
	private BillCardPanel m_billcardpanel = null;

	public MyTpDateSource(String moduleName, BillCardPanel billcardpanel) {
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
		String[] general_b_pks = super.getItemValuesByExpress("geb_pk");// 得到所有的表体主键信息
		String classname = "nc.bs.wds.ic.other.in.OtherInBO";
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
								+ list[j].getAttributeValue("ninassistnum")
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
		int headCount = 0;
		int bodyCount = 0;
		int tailCount = 0;
		if (m_billcardpanel.getHeadItems() != null) {
			headCount = m_billcardpanel.getHeadItems().length;
		}
		if (m_billcardpanel.getBillModel() != null
				&& m_billcardpanel.getBillModel().getBodyItems() != null) {
			bodyCount = m_billcardpanel.getBillModel().getBodyItems().length;
		}
		if (m_billcardpanel.getTailItems() != null) {
			tailCount = m_billcardpanel.getTailItems().length;
		}
		int rowCount = m_billcardpanel.getRowCount();
		// 表头
		if (itemExpress.startsWith("h_") || itemExpress.startsWith("t_")) {
			return super.getItemValuesByExpress(itemExpress);
		} else {
			for (int i = 0; i < bodyCount; i++) {
				BillItem item = m_billcardpanel.getBillModel().getBodyItems()[i];
				if (item == null)
					return null;
				String[] rslt = new String[rowCount];
				if (item.getKey().equals(itemExpress)) {
					// UICheckbox
					if (item.getDataType() == 4) {
						for (int j = 0; j < rowCount; j++) {
							if (m_billcardpanel
									.getBodyValueAt(j, item.getKey()) == null) {
								rslt[j] = nc.ui.ml.NCLangRes.getInstance()
										.getStrByID("uifactory",
												"UPPuifactory-000165")/* @res "否" */;
							} else {
								if (m_billcardpanel.getBodyValueAt(j,
										item.getKey()).toString().equals(
										"false")) {
									rslt[j] = nc.ui.ml.NCLangRes.getInstance()
											.getStrByID("uifactory",
													"UPPuifactory-000165")/*
																		 * @res
																		 * "否"
																		 */;
								} else {
									rslt[j] = nc.ui.ml.NCLangRes.getInstance()
											.getStrByID("uifactory",
													"UPPuifactory-000164")/*
																		 * @res
																		 * "是"
																		 */;
								}
							}
						}
					}
					// UIRefPane or UICombox
					else {
						for (int j = 0; j < rowCount; j++) {
							rslt[j] = m_billcardpanel.getBodyValueAt(j, item
									.getKey()) == null ? "" : m_billcardpanel
									.getBodyValueAt(j, item.getKey())
									.toString();
						}
					}
					if (itemExpress.equals("geb_pk")) {
						ArrayList<String>  list = new ArrayList<String>();
						Set<Integer> set = tpInfor.keySet();
						for(Integer key:set){
							list.addAll(tpInfor.get(key));
						}
						return list.toArray(new String[0]);	
					}
					// lyf begin 将托盘分担到表体对应行：果然该行有多个托盘，则要增加空行
					ArrayList<String> list = new ArrayList<String>();
					for (int row = 0; row < rslt.length; row++) {
						list.add(rslt[row]);
						ArrayList<String> tp = tpInfor.get(row);
						for (int m = 1; m < tp.size(); m++) {
							list.add("");
						}
					}
					return list.toArray(new String[0]);
					// lyf end
				}
			}

		}
		return null;
	}
}
