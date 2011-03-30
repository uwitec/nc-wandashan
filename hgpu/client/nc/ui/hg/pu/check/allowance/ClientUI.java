package nc.ui.hg.pu.check.allowance;

import javax.swing.JComponent;
import javax.swing.ListSelectionModel;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.IBillItem;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.hg.pu.check.allowance.CritiCHK;
import nc.vo.scm.pu.PuPubVO;



/**
 * ÈÝ²î±ÈÀýÉèÖÃ
 * @author zhw
 *
 */
public class ClientUI extends BillCardUI {

	private static final long serialVersionUID = 859146761348083688L;

	public ClientUI() {
		super();
	}

	public ClientUI(String pk_corp, String pk_billType, String pk_busitype, String operater, String billId) {
		super(pk_corp, pk_billType, pk_busitype, operater, billId);
	}
	/**
	 * 
	 */
	@Override
	protected ICardController createController() {
		return new ClientUICtrl();
	}
	protected CardEventHandler createEventHandler() {
		return new ClientEventHandler(this, getUIControl());
	}
	@Override
	public String getRefBillType() {
		return null;
	}
	@Override
	
	protected void initSelfData() {
	    //
	    getBillCardPanel().getBillTable().setRowSelectionAllowed(true);
	    getBillCardPanel().getBillTable().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

	}

	@Override
	public void setDefaultData() throws Exception {
		
	}
	
	@Override
	public Object getUserObject() {
		return new CritiCHK();
	}

	public void afterEdit(nc.ui.pub.bill.BillEditEvent e) {
		super.afterEdit(e);
		String key= e.getKey();
		int row = e.getRow();
		if(e.getPos()==IBillItem.BODY){
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
		}
	}

	public boolean beforeEdit(nc.ui.pub.bill.BillEditEvent e) {
		String key= e.getKey();
		int row = e.getRow();
		if(e.getPos()==IBillItem.BODY){
			if("invcode".equalsIgnoreCase(key)){
				String pk_invcl =PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getBodyValueAt(row,"pk_invcl"));
//				JComponent  jf = getBillCardPanel().getBodyItem("invcode").getComponent();
//				if(jf!=null && jf instanceof UIRefPane){
//				UIRefPane uf = (UIRefPane)jf;
//				AbstractRefModel refModel = (AbstractRefModel) uf.getRefModel();
//				if(pk_invcl == null){
//					refModel.addWherePart(" and 1=1");
//					return true;
//				}
//					refModel.addWherePart("  and bd_invbasdoc.pk_invcl = '" + pk_invcl + "'");
//					return true;
//			    }
				
				if(pk_invcl != null){
					return false;
				}
		     }else if("invclname".equalsIgnoreCase(key)){
		    	 String invcode = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getBodyValueAt(row,"invcode"));
		    	 if(invcode != null){
		    		 return false;
		    	 }
		     }
	     }
		return super.beforeEdit(e);
	}
}
