package nc.ui.zb.pub;

import nc.ui.pub.bill.BillCardPanel;
import nc.ui.trade.pub.CardPanelPRTS;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.zb.pub.ZbPubTool;

public class ZbPubPrintDataSou extends CardPanelPRTS{

	protected AggregatedValueObject m_data = null;
	
	public ZbPubPrintDataSou(String moduleName, BillCardPanel billcardpanel,AggregatedValueObject bills) {
		super(moduleName, billcardpanel);
		m_data = bills;
		dealData();
		// TODO Auto-generated constructor stub
	}
	
	public ZbPubPrintDataSou(String moduleName, BillCardPanel billcardpanel) {
		super(moduleName, billcardpanel);
//		m_datas = bills;
		// TODO Auto-generated constructor stub
	}
	
	public void setPrintDatas(AggregatedValueObject bill){
		m_data = bill;
		dealData();
	}
	
	protected void dealData(){		
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String[] getItemValuesByExpress(String itemExpress) {

		String key = itemExpress.substring(2);
		if(m_data == null)
			return super.getItemValuesByExpress(itemExpress);
		CircularlyAccessibleValueObject parent = m_data.getParentVO();
		//		if(parent ==)
		CircularlyAccessibleValueObject[] bodys = m_data.getChildrenVO();
		if(itemExpress.startsWith("h_")){
			return new String[]{ZbPubTool.getString_NullAsTrimZeroLen(parent.getAttributeValue(key))};
		}else if(itemExpress.startsWith("b_")){
			if(bodys == null || bodys.length == 0)
				return super.getItemValuesByExpress(itemExpress);
			String[] values = new String[bodys.length];
			int index = 0;
			for(CircularlyAccessibleValueObject body:bodys){

				if(key.equalsIgnoreCase("nwinpercent")){
					values[index] = ZbPubTool.getString_NullAsTrimZeroLen(body.getAttributeValue(key))+"%";
				}else
					values[index] = ZbPubTool.getString_NullAsTrimZeroLen(body.getAttributeValue(key));
				index++;
			}
			return values;
		}else		
			return super.getItemValuesByExpress(itemExpress);
	}
	
}
