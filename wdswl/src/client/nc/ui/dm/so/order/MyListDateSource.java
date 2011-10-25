package nc.ui.dm.so.order;

import javax.swing.ImageIcon;

import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.print.IExDataSource;
import nc.ui.trade.pub.ListPanelPRTS;

public class MyListDateSource extends ListPanelPRTS implements IExDataSource {

	public MyListDateSource(String modulecode, BillListPanel bp) {
		super(modulecode, bp);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Object[] getObjectByExpress(String itemExpress){
		if(itemExpress.equals("h_reserve7")){
			String imageName = "";
			String [] h_reserve7 = super.getItemValuesByExpress("h_reserve7");
			if(h_reserve7 != null){
				imageName = h_reserve7[0];
			}
			Object[] pics = new Object[1];
			for(int i = 0; i < pics.length; i++)
				pics[i] = new ImageIcon("C:/images/"+imageName);
			return pics;
		}
		return null;
	}

	public int getObjectTypeByExpress(String itemExpress) {
		if(itemExpress.equals("h_reserve7"))
			return IExDataSource.IMAGE_TYPE;
		
		return 0;
}


}
