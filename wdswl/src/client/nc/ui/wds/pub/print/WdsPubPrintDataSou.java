package nc.ui.wds.pub.print;

import nc.ui.pub.bill.BillCardPanel;
import nc.ui.trade.pub.CardPanelPRTS;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.wl.pub.WdsWlPubTool;

public class WdsPubPrintDataSou extends CardPanelPRTS{

	protected AggregatedValueObject m_data = null;
	
	public WdsPubPrintDataSou(String moduleName, BillCardPanel billcardpanel,AggregatedValueObject bills) {
		super(moduleName, billcardpanel);
		m_data = bills;
		dealData();
	}
	
	public WdsPubPrintDataSou(String moduleName, BillCardPanel billcardpanel) {
		super(moduleName, billcardpanel);
	}
	
	public void setPrintDatas(AggregatedValueObject bill){
		m_data = bill;
		dealData();
	}
	
	protected void dealData(){		
	}
	
	public String[] getItemValuesByExpress(String itemExpress) {
		String key = itemExpress.substring(2);
		if(m_data == null)
			return super.getItemValuesByExpress(itemExpress);
		CircularlyAccessibleValueObject parent = m_data.getParentVO();
		CircularlyAccessibleValueObject[] bodys = m_data.getChildrenVO();
		if(itemExpress.startsWith("h_")){
			return new String[]{WdsWlPubTool.getString_NullAsTrimZeroLen(parent.getAttributeValue(key))};
		}else if(itemExpress.startsWith("e_")){//±íÎ²
			return super.getItemValuesByExpress(itemExpress);
		}
		else{
			if(bodys == null || bodys.length == 0)
				return super.getItemValuesByExpress(itemExpress);
			String[] values = new String[bodys.length];
			int index = 0;
			for(CircularlyAccessibleValueObject body:bodys){
				values[index] = WdsWlPubTool.getString_NullAsTrimZeroLen(body.getAttributeValue(itemExpress));
				index++;
			}
			return values;
		}
	}	
}
