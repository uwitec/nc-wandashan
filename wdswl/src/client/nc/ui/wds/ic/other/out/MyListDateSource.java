package nc.ui.wds.ic.other.out;

import nc.ui.pub.bill.BillListPanel;
import nc.ui.trade.pub.ListPanelPRTS;
import nc.ui.wl.pub.LongTimeTask;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.report.ReportBaseVO;

public class MyListDateSource extends ListPanelPRTS {
	private BillListPanel m_billcardpanel = null;

	public MyListDateSource(String moduleCode, BillListPanel billListPanel) {
		// TODO Auto-generated constructor stub
		super(moduleCode, billListPanel);
		this.m_billcardpanel = billListPanel;
	}

	/**
	 * noutnum noutassistnum
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String[] getItemValuesByExpress(String itemExpress) {
		// 增加托盘中得一些必要字段字段。。。 t_cdt_pk general_b_pk
		if (itemExpress.equals("general_b_pk")) {
			String[] general_b_pks = super
					.getItemValuesByExpress("general_b_pk");
			String[] tpInfor = new String[general_b_pks.length];
			if (general_b_pks != null && general_b_pks.length > 0) {
				String classname = "nc.bs.wds.ic.other.out.OtherOutBO";
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
								bur.append("托盘编号："+list[j].getAttributeValue("cdt_traycode")+":");
								bur.append("实出数量："+list[j].getAttributeValue("noutnum")+",");
								bur.append("实出辅数量："+list[j].getAttributeValue("noutassistnum")+",");
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
