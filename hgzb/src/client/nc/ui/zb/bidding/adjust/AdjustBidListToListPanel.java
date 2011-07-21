package nc.ui.zb.bidding.adjust;

import javax.swing.JComponent;

import nc.itf.zb.pub.IVOTreeData2;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.ValueChangedEvent;
import nc.ui.pub.beans.ValueChangedListener;
import nc.ui.pub.beans.textfield.UITextType;
import nc.ui.trade.listtolist.IListToListController;
import nc.ui.trade.pub.IVOTreeData;
import nc.ui.zb.pub.UIListToListPanel2;
import nc.ui.zb.pub.refmodel.BiddingRefModelFree;
import nc.vo.scm.pu.PuPubVO;

/**
 * 
 * @author zhf  议标数据准备 panel
 *
 */
public class AdjustBidListToListPanel extends UIListToListPanel2 {
	private JComponent parent = null;
	public AdjustBidListToListPanel(JComponent parent){
		super();
		this.parent = parent;
	}

	@Override
	protected IVOTreeData createLeftData() {
		// TODO Auto-generated method stub
		return new AjustBiddingLeftTreeData();
	}

	@Override
	protected IListToListController createListController() {
		// TODO Auto-generated method stub
		return new AdjustBidListToListCtrl();
	}

	@Override
	protected IVOTreeData createRightData() {
		// TODO Auto-generated method stub
		return new AdjustBiddingRightTreeData();
	}

	@Override
	protected UIRefPane createUILiftContrlComponent() {
		// TODO Auto-generated method stub
		UIRefPane pane = createBiddingRefPane();
		pane.addValueChangedListener(new leftValueChange());
		return pane;
	}

	@Override
	protected UIRefPane createUIRithtContrlComponent() {
		// TODO Auto-generated method stub
		UIRefPane pane = createBiddingRefPane();
		pane.addValueChangedListener(new rightValueChange());
		return pane;
	
	}
	
	private UIRefPane createBiddingRefPane(){
		UIRefPane ref = new UIRefPane();
		ref.setIsCustomDefined(true);
		AbstractRefModel refModel = new BiddingRefModelFree();
		ref.setRefNodeName("自定义参照");
		ref.setRefModel(refModel);
	
		ref.setTextType(UITextType.TextStr);
		ref.setReturnCode(false);
		ref.setMaxLength(30);
		ref.setMultiSelectedEnabled(false);
		ref.setSealedDataButtonShow(true);
		ref.getRefModel().setUseDataPower(true);
		ref.setAutoCheck(true);
		ref.setEnabled(true);
		ref.setEditable(true);
		return ref;
	}
	
	private UIRefPane getLeftRefPane(){
		return (UIRefPane)getUILiftContrlComponent();
	}
	
	private UIRefPane getRightRefPane(){
		return (UIRefPane)getUIRithtContrlComponent();
	}
	
	private String m_leftBiddingid = null;
	private String m_rightBiddingid = null;
	public String getLeftBiddingID(){
		return m_leftBiddingid;
	}
	public String getRightBiddingID(){
		return m_rightBiddingid;
	}
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）校验左右选择的标段不可以相等
	 * 2011-6-7下午04:09:09
	 * @param cnewbiddingid
	 * @param isleft
	 * @return
	 */
	private boolean checkBidding(String cnewbiddingid,boolean isleft){
		if(PuPubVO.getString_TrimZeroLenAsNull(cnewbiddingid) == null)
			return true;
		if(PuPubVO.getString_TrimZeroLenAsNull(isleft?m_rightBiddingid:m_leftBiddingid)==null)
			return true;
		if((isleft?m_rightBiddingid:m_leftBiddingid).equalsIgnoreCase(cnewbiddingid)){
			MessageDialog.showErrorDlg(parent, "警告", "选择为同一标段");
			return false;
		}
		return true;
	}

	class leftValueChange implements ValueChangedListener{

		public void valueChanged(ValueChangedEvent e) {
			// TODO Auto-generated method stub
			String invid = PuPubVO.getString_TrimZeroLenAsNull(getLeftRefPane().getRefPK());
			if(!checkBidding(invid,true)){
				return;
			}
			String whereSql = "";
			if(invid != null){
				m_leftBiddingid = invid;
				whereSql = " cbiddingid = '"+invid+"'";
			}
			((IVOTreeData2)getLeftData()).setWherePart(whereSql);
			refreshData();
		}		
	}
	
	private void refreshData(){
		setLeftListDatas();
		setRightListDatas();
	}
	
	class rightValueChange implements ValueChangedListener{

		public void valueChanged(ValueChangedEvent e) {
			// TODO Auto-generated method stub
			String invid = PuPubVO.getString_TrimZeroLenAsNull(getRightRefPane().getRefPK());
			if(!checkBidding(invid,false))
				return;
			String whereSql = "";
			if(invid != null){
				m_rightBiddingid = invid;
				whereSql = " cbiddingid = '"+invid+"'";
			}
			((IVOTreeData2)getRightData()).setWherePart(whereSql);
			refreshData();
		}
		
	}

}
