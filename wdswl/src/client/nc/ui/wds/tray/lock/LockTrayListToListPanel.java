package nc.ui.wds.tray.lock;

import javax.swing.JComponent;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.ValueChangedEvent;
import nc.ui.pub.beans.ValueChangedListener;
import nc.ui.pub.beans.textfield.UITextType;
import nc.ui.trade.listtolist.IListToListController;
import nc.ui.trade.pub.IVOTreeData;
import nc.ui.wds.ref.CargDocRefModel;
import nc.ui.wl.pub.UIListToListPanel2;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wl.pub.IVOTreeData2;

/**
 * 
 * @author zhf  托盘锁定 panel
 *
 */
public class LockTrayListToListPanel extends UIListToListPanel2 {
	private String  cwarehouseid = null;
	public void setWarehouseID(String wareid){
		cwarehouseid = wareid;
	}
	public LockTrayListToListPanel(){
		super();
	}
	
	public LockTrayListToListPanel(String warehouseid){
		super();
		this.cwarehouseid = warehouseid;
//		if(warehouseid != null)
		((CargDocRefModel)getLeftRefPane().getRefModel()).setStordocID(warehouseid);
	}

	@Override
	protected IVOTreeData createLeftData() {
		// TODO Auto-generated method stub
		return new LockTrayLeftTreeData();
	}

	@Override
	protected IListToListController createListController() {
		// TODO Auto-generated method stub
		return new LockTrayListToListCtrl();
	}

	@Override
	protected IVOTreeData createRightData() {
		// TODO Auto-generated method stub
		return new LockTrayRightTreeData();
	}

	@Override
	protected UIRefPane createUILiftContrlComponent() {
		// TODO Auto-generated method stub
		UIRefPane pane = createTrayRefPane();
		pane.addValueChangedListener(new leftValueChange());
		return pane;
	}

//	@Override
//	protected UIRefPane createUIRithtContrlComponent() {
//		// TODO Auto-generated method stub
//		UIRefPane pane = createTrayRefPane();
//		pane.addValueChangedListener(new rightValueChange());
//		return pane;
//	
//	}
	
	private UIRefPane createTrayRefPane(){
		UIRefPane ref = new UIRefPane();
		ref.setIsCustomDefined(true);
		AbstractRefModel refModel = null;
		if(PuPubVO.getString_TrimZeroLenAsNull(cwarehouseid)==null){
			refModel = new nc.ui.wds.ref.CargDocRefModel();
		}else{
			refModel = new nc.ui.wds.ref.CargDocRefModel(cwarehouseid);
		}
		ref.setRefNodeName("自定义参照");
		ref.setRefModel(refModel);
	
		ref.setTextType(UITextType.TextStr);
		ref.setReturnCode(false);
		ref.setMaxLength(30);
		ref.setMultiSelectedEnabled(false);
		ref.setSealedDataButtonShow(false);
		ref.getRefModel().setUseDataPower(true);
		ref.setAutoCheck(true);
		ref.setEnabled(true);
		ref.setEditable(true);
		return ref;
	}
	
	private UIRefPane getLeftRefPane(){
		return (UIRefPane)getUILiftContrlComponent();
	}
	
//	private UIRefPane getRightRefPane(){
//		return (UIRefPane)getUIRithtContrlComponent();
//	}
	
//	private String m_leftBiddingid = null;
//	private String m_rightBiddingid = null;
//	public String getLeftBiddingID(){
//		return m_leftBiddingid;
//	}
//	public String getRightBiddingID(){
//		return m_rightBiddingid;
//	}
//	/**
//	 * 
//	 * @author zhf
//	 * @说明：（鹤岗矿业）校验左右选择的标段不可以相等
//	 * 2011-6-7下午04:09:09
//	 * @param cnewbiddingid
//	 * @param isleft
//	 * @return
//	 */
//	private boolean checkBidding(String cnewbiddingid,boolean isleft){
//		if(PuPubVO.getString_TrimZeroLenAsNull(cnewbiddingid) == null)
//			return true;
//		if(PuPubVO.getString_TrimZeroLenAsNull(isleft?m_rightBiddingid:m_leftBiddingid)==null)
//			return true;
//		if((isleft?m_rightBiddingid:m_leftBiddingid).equalsIgnoreCase(cnewbiddingid)){
//			MessageDialog.showErrorDlg(parent, "警告", "选择为同一标段");
//			return false;
//		}
//		return true;
//	}

	class leftValueChange implements ValueChangedListener{

		public void valueChanged(ValueChangedEvent e) {
			// TODO Auto-generated method stub
			String cargdocid = PuPubVO.getString_TrimZeroLenAsNull(getLeftRefPane().getRefPK());
//			if(!checkBidding(invid,true)){
//				return;
//			}
			String whereSql = "";
			if(cargdocid != null){
//				m_leftBiddingid = invid;
				whereSql = " pk_cargdoc = '"+cargdocid+"'";
			}
			((IVOTreeData2)getLeftData()).setWherePart(whereSql);
			refreshData();
		}		
	}
	
	private void refreshData(){
		setLeftListDatas();
		if(getRightListItems() ==null || getRightListItems().length<=0)
		setRightListDatas();
	}
	
//	class rightValueChange implements ValueChangedListener{
//
//		public void valueChanged(ValueChangedEvent e) {
//			// TODO Auto-generated method stub
//			String invid = PuPubVO.getString_TrimZeroLenAsNull(getRightRefPane().getRefPK());
//			if(!checkBidding(invid,false))
//				return;
//			String whereSql = "";
//			if(invid != null){
//				m_rightBiddingid = invid;
//				whereSql = " cbiddingid = '"+invid+"'";
//			}
//			((IVOTreeData2)getRightData()).setWherePart(whereSql);
//			refreshData();
//		}
//		
//	}

	@Override
	protected boolean isExitLeftContrlCom() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected boolean isExitRightContrlCom() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected JComponent createUIRithtContrlComponent() {
		// TODO Auto-generated method stub
		return null;
	}
}
