package nc.ui.wds.ic.allocation.in;

import nc.ui.pub.bill.BillCardPanel;
import nc.ui.trade.pub.CardPanelPRTS;
import nc.ui.wl.pub.LongTimeTask;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.report.ReportBaseVO;
/**
 * 卡片打印
 * @author xjx  
 *
 */
public class MyDbTyDateSource extends CardPanelPRTS {
	private BillCardPanel m_billcardpanel = null;

	public MyDbTyDateSource(String moduleName, BillCardPanel billcardpanel) {
		super(moduleName, billcardpanel);
		this.m_billcardpanel = billcardpanel;
	}

	/**
	 * gebb_num      ninassistnum
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String[] getItemValuesByExpress(String itemExpress) {
		// 增加托盘中得一些必要字段字段。。。 t_cdt_pk general_b_pk
		if (itemExpress.equals("geb_pk")) {
			String[] general_b_pks = super
					.getItemValuesByExpress("geb_pk");
			String[] tpInfor = new String[general_b_pks.length];
			if (general_b_pks != null && general_b_pks.length > 0) {
				String classname = "nc.bs.wds.ic.allocation.in.AllocationInBO";
				String methodname = "getCorpTP";
				Class[] ParameterTypes = new Class[] { String.class };
				for (int i=0;i<general_b_pks.length;i++) {
					Object[] ParameterValues = new Object[] { general_b_pks[i] };
					try {
						ReportBaseVO[] list = (ReportBaseVO[]) LongTimeTask
								.callRemoteService(WdsWlPubConst.WDS_WL_MODULENAME,
										classname, methodname, ParameterTypes,
										ParameterValues, 2);
						StringBuffer bur = new StringBuffer();
						if(list != null && list.length>0){
							for(int j=0;j<list.length;j++){
								bur.append("托盘编号："+list[j].getAttributeValue("cdt_traycode")+", ");
						//		bur.append("实入数量："+list[j].getAttributeValue("gebb_num")+", ");
								bur.append("实入辅数量："+list[j].getAttributeValue("ninassistnum")+", ");
								bur.append("\n");
							}
						}
						tpInfor[i] = bur.toString();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			return tpInfor;

		}
		return super.getItemValuesByExpress(itemExpress);
	}
}
