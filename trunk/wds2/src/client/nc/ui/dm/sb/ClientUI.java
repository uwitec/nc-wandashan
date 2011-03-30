package nc.ui.dm.sb;

import javax.swing.ListSelectionModel;

import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.pub.lang.UFBoolean;


/**ÌØÊâÒµÎñ
 * @author Administrator
 * 
 */
public class ClientUI extends BillCardUI{

	private static final long serialVersionUID = 859146761348083688L;
	
	public ClientUI() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	public ClientUI(String pk_sbdoc,String vsbdocid,String vsbdocname,UFBoolean fisincity){
		super();
	}

	@Override
	protected CardEventHandler createEventHandler() {
		// TODO Auto-generated method stub
		return new ClientEventHandler(this, getUIControl());
	}
	
	@Override
	public Object getUserObject() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	protected ICardController createController() {
		// TODO Auto-generated method stub
		return new ClientController();
	}

	@Override
	public String getRefBillType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void initSelfData() {
		getBillCardPanel().getBillTable().setRowSelectionAllowed(true);
	    getBillCardPanel().getBillTable().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
	}
	
	@Override
	public void setDefaultData() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	public void afterEdit(nc.ui.pub.bill.BillEditEvent e) {
//		super.afterEdit(e);
//		String key= e.getKey();
//		int row = e.getRow();
//		if(e.getPos()==IBillItem.BODY){
//		if("invcode".equalsIgnoreCase(key)){
//			String invclass = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getBillModel().getValueAt(e.getRow(),"invclname"));
//			if(invclass==null){
//				String[] formulas =
//				    new String[] {
//						"pk_invcl->getColValue(bd_invbasdoc,pk_invcl,pk_invbasdoc,pk_invbasdoc)",
//						"invclname->getColValue(bd_invcl,invclassname,pk_invcl,pk_invcl)"};
//				getBillCardPanel().getBillModel().execFormulas(row,formulas);
//			}	
//		}else if("invclname".equalsIgnoreCase(key)){
//			getBillCardPanel().getBillModel().setValueAt(null,row,"pk_invmandoc");
//			getBillCardPanel().getBillModel().setValueAt(null,row,"pk_invbasdoc");
//			getBillCardPanel().getBillModel().setValueAt(null,row,"invcode");
//			getBillCardPanel().getBillModel().setValueAt(null,row,"invname");
//		}
//		}
	}

//	public boolean beforeEdit(nc.ui.pub.bill.BillEditEvent e) {
//		String key= e.getKey();
//		int row = e.getRow();
//		if(e.getPos()==IBillItem.BODY){
//			if("invcode".equalsIgnoreCase(key)){
//				String pk_invcl =SbDocVO.getString_TrimZeroLenAsNull(getBillCardPanel().getBodyValueAt(row,"pk_invcl"));
////				JComponent  jf = getBillCardPanel().getBodyItem("invcode").getComponent();
////				if(jf!=null && jf instanceof UIRefPane){
////				UIRefPane uf = (UIRefPane)jf;
////				AbstractRefModel refModel = (AbstractRefModel) uf.getRefModel();
////				if(pk_invcl == null){
////					refModel.addWherePart(" and 1=1");
////					return true;
////				}
////					refModel.addWherePart("  and bd_invbasdoc.pk_invcl = '" + pk_invcl + "'");
////					return true;
////			    }
//				
//				if(pk_invcl != null){
//					return false;
//				}
//		     }else if("invclname".equalsIgnoreCase(key)){
//		    	 String invcode = SbDocVO.getString_TrimZeroLenAsNull(getBillCardPanel().getBodyValueAt(row,"invcode"));
//		    	 if(invcode != null){
//		    		 return false;
//		    	 }
//		     }
//	     }
//		return super.beforeEdit(e);
//	}
	
}