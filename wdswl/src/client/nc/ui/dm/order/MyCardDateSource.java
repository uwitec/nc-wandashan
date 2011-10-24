package nc.ui.dm.order;

import javax.swing.ImageIcon;

import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.print.IExDataSource;
import nc.ui.trade.pub.CardPanelPRTS;

public class MyCardDateSource extends CardPanelPRTS implements IExDataSource {

	public MyCardDateSource(String moduleName, BillCardPanel billcardpanel) {
		super(moduleName, billcardpanel);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Object[] getObjectByExpress(String itemExpress){
		if(itemExpress.equals("reserve7")){
			Object[] pics = new Object[1];
			for(int i = 0; i < pics.length; i++)
				pics[i] = new ImageIcon("C:/images/11.gif");

			return pics;
		}
		return null;
	}

	public int getObjectTypeByExpress(String itemExpress) {
		if(itemExpress.equals("reserve7"))
			return IExDataSource.IMAGE_TYPE;
		
		return 0;
}

}
