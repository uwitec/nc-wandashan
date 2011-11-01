package nc.ui.dm.so.order;

import javax.swing.ImageIcon;

import nc.bs.framework.common.NCLocator;
import nc.bs.framework.common.RuntimeEnv;
import nc.itf.uap.busibean.IFileManager;
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
			IFileManager fileManager = (IFileManager) NCLocator.getInstance().lookup(IFileManager.class.getName());
			String defaultDir = RuntimeEnv.getInstance()
			.getNCHome()
			+ "/webapps/nc_web/ncupload/printimage/"+imageName;
			Object[] pics = new Object[1];
			for(int i = 0; i < pics.length; i++){
				try {
					pics[0]= new ImageIcon(fileManager.readFile(defaultDir));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
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
