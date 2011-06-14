package nc.ui.dm;

import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListPanel;

public class DealDataSource implements nc.ui.pub.print.IDataSource{

	private static final long serialVersionUID = 6867335405643319348L;
	
	private BillListPanel listPanel=null;
	private String modelname=null;
	private String[] keys=null;
	
	public DealDataSource(BillListPanel listPanel,String modelname ){
		this.listPanel=listPanel;
		this.modelname=modelname;
		keys=getAllDataItemExpress();
	}

	public String[] getAllDataItemExpress() {
	 BillItem[]  items=	listPanel.getBillListData().getHeadItems();
	 String [] strs=new String[items.length];
	 for(int i=0;i<items.length;i++){
		strs[i]=items[i].getKey(); 
		 
	 }
	
		return strs;
	}

	public String[] getAllDataItemNames() {
	  BillItem[]  items=	listPanel.getBillListData().getHeadItems();
	  String [] strs=new String[items.length];
	  for(int i=0;i<items.length;i++){
			strs[i]=items[i].getName(); 
			 
	 }
		
		return strs;
	}

	public String[] getDependentItemExpressByExpress(String itemExpress) {
		
		return null;
	}

	public String[] getItemValuesByExpress(String itemExpress) {
		
		int headCount = 0;
		String[] values=new String[listPanel.getHeadTable().getRowCount()];
//		if (m_billcardpanel.getHeadItems() != null){
//			headCount = m_billcardpanel.getHeadItems().length;
//		}
//		if (m_billcardpanel.getBillModel()!=null&&m_billcardpanel.getBillModel().getBodyItems() != null){
//			bodyCount = m_billcardpanel.getBillModel().getBodyItems().length;
//		}
//		if (m_billcardpanel.getTailItems() != null){
//			tailCount = m_billcardpanel.getTailItems().length;
//		}
//		int rowCount  = m_billcardpanel.getRowCount();
		if(listPanel !=null){
			headCount=listPanel.getHeadTable().getRowCount();
		}
	
		for(int i=0;i<headCount;i++){
			int col=0;
			for(int j=0;j<keys.length;j++){
				if(keys[j].equalsIgnoreCase(itemExpress.substring(2,itemExpress.length()))){
					col=j;
					break;
				}
			}
			Object value=listPanel.getHeadTable().getValueAt(i, col);
			
			if(value !=null){
			values[i]=value.toString();	
			}
		}	
		
//		try {
//			//表头
//			if(itemExpress.startsWith("h_")){
//				BillItem item = m_billcardpanel.getHeadItem(itemExpress.substring(2));
//				if(item==null) return null;
//				if (item.getKey().equals(itemExpress.substring(2))) {
//					//UICheckbox
//					if(item.getDataType()==4){
//						if(item.getValue()==null){
//							return new String[] {nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000165")/*@res "否"*/};
//						}else{
//							if(item.getValue().equals("false")){
//								return new String[] {nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000165")/*@res "否"*/};
//							}else{
//								return new String[] {nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000164")/*@res "是"*/};
//							}
//						}
//					}
//					//UICombox
//					else if(item.getDataType()==6){
//						String sc = ((UIComboBox)item.getComponent()).getSelectedItem().toString();
//						return new String[] { sc };
//					}
//					//UIRefPane
//					else if (item.getDataType() == 5){
//	  				    String sr = ((UIRefPane)item.getComponent()).isReturnCode() ? ((UIRefPane)item.getComponent()).getRefCode() : ((UIRefPane)item.getComponent()).getRefName();
//		  				return new String[] { sr };
//					}
//	               //	大文本
//					else if (item.getDataType() == 9) {
//					String wb = ((UITextAreaScrollPane)item.getComponent()).getText();
//					return new String[] { wb };
//					}
//
//					//其它文本
//					else{
//						String wb = ((UIRefPane)item.getComponent()).getText();
//					  	//cf add 如果是数植型则根据小数位数format
//					   	try{
//						   	if(item.getDataType() == 2){
//								UIRefPane item_h=(UIRefPane)item.getComponent();
//								nc.vo.pub.lang.UFDouble value=new nc.vo.pub.lang.UFDouble(wb);
//								value=value.setScale(item_h.getNumPoint(),4);
//								wb=value.toString();
//							}
//						}catch(Exception e){
//							System.out.println("如果是数植型则根据小数位数format出错:"+e);
//					  	}
//					   	//cf add
//					 	return new String[] { wb };
//					}
//				}
//
//			}
//			//表尾
//			else if(itemExpress.startsWith("t_")){
//				BillItem item = m_billcardpanel.getTailItem(itemExpress.substring(2));
//				if(item==null) return null;
//				if (item.getKey().equals(itemExpress.substring(2))) {
//					//UICheckbox
//					if(item.getDataType()==4){
//						if(item.getValue()==null){
//							return new String[] {nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000165")/*@res "否"*/};
//						}else{
//							if(item.getValue().equals("false")){
//								return new String[] {nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000165")/*@res "否"*/};
//							}else{
//								return new String[] {nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000164")/*@res "是"*/};
//							}
//						}
//					}
//					//UICombox
//					else if(item.getDataType()==6){
//						String sc = ((UIComboBox)item.getComponent()).getSelectedItem().toString();
//						return new String[] { sc };
//					}
//					//UIRefPane
//					else if (item.getDataType() == 5){
//	  				    String sr = ((UIRefPane)item.getComponent()).isReturnCode() ? ((UIRefPane)item.getComponent()).getRefCode() : ((UIRefPane)item.getComponent()).getRefName();
//		  				return new String[] { sr };
//					}
//					//其它文本
//					else{
//						String wb = ((UIRefPane)item.getComponent()).getText();
//					    return new String[] { wb };
//					}
//				}
//			}
//			//表体
//			else{
//				for (int i = 0; i < bodyCount; i++){
//					BillItem item = m_billcardpanel.getBillModel().getBodyItems()[i];
//					if(item==null) return null;
//					String[] rslt = new String[rowCount];
//					if (item.getKey().equals(itemExpress)) {
//						//UICheckbox
//						if(item.getDataType()==4){
//							for (int j = 0; j < rowCount; j++) {
//								if(m_billcardpanel.getBodyValueAt(j, item.getKey())==null){
//									rslt[j] = nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000165")/*@res "否"*/;
//								}else{
//									if(m_billcardpanel.getBodyValueAt(j, item.getKey()).toString().equals("false")){
//										rslt[j] = nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000165")/*@res "否"*/;
//									}else{
//										rslt[j] = nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000164")/*@res "是"*/;
//									}
//								}
//							}
//						}
//						//UIRefPane or UICombox
//						else{
//							for (int j = 0; j < rowCount; j++) {
//								rslt[j] = m_billcardpanel.getBodyValueAt(j, item.getKey()) == null ? "" : m_billcardpanel.getBodyValueAt(j, item.getKey()).toString();
//							}
//						}
//						return rslt;
//					}
//				}
//			}
//
//		} catch (Throwable e) {
//			e.printStackTrace();
//		    System.out.print("error at getItemValueByExpress()");
//		    return null;
//		}
		return values;
	}

	public String getModuleName() {
		
		return modelname;
	}

	public boolean isNumber(String itemExpress) {
		
		return false;
	}

}
