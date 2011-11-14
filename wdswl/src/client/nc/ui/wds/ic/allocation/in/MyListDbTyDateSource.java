package nc.ui.wds.ic.allocation.in;

import nc.ui.pub.bill.BillListPanel;
import nc.ui.trade.pub.ListPanelPRTS;
import nc.ui.wl.pub.LongTimeTask;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.report.ReportBaseVO;
/**
 * �б���ӡ
 * @author xjx  
 *
 */
public class MyListDbTyDateSource extends ListPanelPRTS {
	private BillListPanel m_billcardpanel = null;

	public MyListDbTyDateSource(String moduleCode, BillListPanel billListPanel) {
		// TODO Auto-generated constructor stub
		super(moduleCode, billListPanel);
		this.m_billcardpanel = billListPanel;
	}

	private static final long serialVersionUID = 1L;

	@Override
	public String[] getItemValuesByExpress(String itemExpress) {
		// ���������е�һЩ��Ҫ�ֶ��ֶΡ����� t_cdt_pk general_b_pk
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
								bur.append("���̱�ţ�"+list[j].getAttributeValue("cdt_traycode")+", ");
								bur.append("ʵ��������"+list[j].getAttributeValue("gebb_num")+", ");
								bur.append("ʵ�븨������"+list[j].getAttributeValue("ninassistnum")+", ");
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