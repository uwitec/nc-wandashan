package nc.ui.zb.bidding.make;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.IBillModelRowStateChangeEventListener;
import nc.ui.trade.business.HYPubBO_Client;
import nc.uif.pub.exception.UifException;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.zb.bidding.BiddingBillVO;
import nc.vo.zb.bidding.HistoryPriceVO;
import nc.vo.zb.parmset.ParamSetVO;
import nc.vo.zb.pub.ZbPubConst;
import nc.vo.zb.pub.ZbPubTool;

public class HistoryPriceDlg extends UIDialog implements ActionListener {	
	
	private javax.swing.JPanel ivjUIDialogContentPane = null;	
	private nc.ui.pub.beans.UIPanel ivjPnButton = null;
	private nc.ui.pub.beans.UIButton ivjBnCancel = null;
	private nc.ui.pub.beans.UIButton ivjBnOk = null;
	private nc.ui.pub.beans.UIButton ivjBnPrice = null;
	private nc.ui.pub.beans.UIButton ivjBnAllSel = null;
	private nc.ui.pub.beans.UIButton ivjBnCanAllSel = null;
	private BillCardPanel m_datapanel = null;
	
	private ToftPanel tp = null;	
//	数据
	private Map<String, HistoryPriceVO[]> historyPriceInfor = null;//key : 存货id
	
	public HistoryPriceVO[] getHistoryPrice(String invid){
		if(historyPriceInfor == null || historyPriceInfor.size() == 0)
			historyPriceInfor = new HashMap<String, HistoryPriceVO[]>();
		return historyPriceInfor.get(invid);
	}
	
	
	
	private String cinvid = null;
	private UFDate uLogdate = null;
	private UFDouble nprice = null;
	private UFDouble nnewprice = null;
	private String newts = null;
	private String cbiddingid = null;
	
	public void setHistoryPara(String cbiddingid,String invid,UFDate uLogDate,UFDouble nprice){
		cinvid = invid;
		uLogdate = uLogDate;
		this.nprice = nprice;
		this.cbiddingid = cbiddingid;
		this.nnewprice = null;
		this.newts = null;
		updateUI();
	}
	
	private void updateUI(){
		getDataPanel().getHeadItem("cinvid").setValue(cinvid);
		getDataPanel().getHeadItem("senddate").setValue(uLogdate.toString());
//		int ihistory = 6;
		String sstartdate = uLogdate.getDateBefore(ihistory * 30).toString();
		getDataPanel().getHeadItem("sstartdate").setValue(sstartdate);
		getDataPanel().getHeadItem("nprice").setValue(nprice);
//		getDataPanel().getHeadItem("nnewprice").setValue(null);
		
		HistoryPriceVO[] prices = getHistoryPrice(cinvid);
		if(prices == null || prices.length ==0){
			try {
				prices = MakeBiddingHelper.getHistoryPriceInfor(tp, cinvid,sstartdate, uLogdate.toString(), ClientEnvironment.getInstance().getCorporation().getPrimaryKey());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				tp.showErrorMessage(ZbPubTool.getString_NullAsTrimZeroLen(e.getMessage()));
			}
			historyPriceInfor.put(cinvid, prices);
		}
		getDataPanel().getBillModel().setBodyDataVO(prices);
		getDataPanel().execHeadTailLoadFormulas();
		getDataPanel().getBillModel().execLoadFormula();
	}

	
	public HistoryPriceDlg(ToftPanel parent){
		super(parent);
		tp = parent;
		init();
	}
	
	private int ihistory = 6;
	
	private void init() {
		setSize(810,400);
		setTitle("供货历史");
		setContentPane(getUIDialogContentPane());		
		
		getBnOk().addActionListener(this);
		getBnCancel().addActionListener(this);
		getBnPrice().addActionListener(this);
		getBnAllSel().addActionListener(this);
		getBnCanAllSel().addActionListener(this);
		getBnOk().setEnabled(false);
		initPara();
	}
	
	
	
	private void initPara(){
		ParamSetVO[] paras = null;
		try {
			paras = (ParamSetVO[])HYPubBO_Client.queryByCondition(ParamSetVO.class, " isnull(dr,0) = 0 and pk_corp = '"+ClientEnvironment.getInstance().getCorporation().getPrimaryKey()+"'");
		} catch (UifException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ihistory = 6;
		}
		if(paras == null || paras.length == 0 || paras.length>1)
			ihistory = 6;
		ihistory = PuPubVO.getInteger_NullAs(paras[0].getIreferencelimits(), 6);
	}
	
	private javax.swing.JPanel getUIDialogContentPane() {
		if (ivjUIDialogContentPane == null) {
			try {
				ivjUIDialogContentPane = new javax.swing.JPanel();
				ivjUIDialogContentPane.setName("UIDialogContentPane");
				ivjUIDialogContentPane.setLayout(new java.awt.BorderLayout());
				getUIDialogContentPane().add(getPnButton(), "South");
				getUIDialogContentPane().add(getDataPanel(), "Center");
			} catch (java.lang.Throwable ivjExc) {
			}
		}
		return ivjUIDialogContentPane;
	}
	private BillCardPanel getDataPanel(){
		if(m_datapanel == null){
			m_datapanel = new BillCardPanel();
			m_datapanel.loadTemplet(ZbPubConst.zb_historyprice, null,
					ClientEnvironment.getInstance().getCorporation().getPrimaryKey(), ClientEnvironment.getInstance().getUser().getPrimaryKey());
			m_datapanel.setEnabled(true);
			m_datapanel.setBodyMultiSelect(true);
		}
		return m_datapanel;
	}
	/**
	 * 返回 PnButton 特性值.
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* 警告:此方法将重新生成. */
	private nc.ui.pub.beans.UIPanel getPnButton() {
		if (ivjPnButton == null) {
			try {
				ivjPnButton = new nc.ui.pub.beans.UIPanel();
				ivjPnButton.setName("PnButton");
				getPnButton().add(getBnOk(), getBnOk().getName());	
				getPnButton().add(getBnCancel(), getBnCancel().getName());
				getPnButton().add(getBnPrice(), getBnPrice().getName());
				getPnButton().add(getBnAllSel(),getBnAllSel().getName());
				getPnButton().add(getBnCanAllSel(),getBnCanAllSel().getName());
			} catch (java.lang.Throwable ivjExc) {

			}
		}
		return ivjPnButton;
	}
	
	

	/**
	 * 返回 BnOk 特性值.
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告:此方法将重新生成. */
	nc.ui.pub.beans.UIButton getBnOk() {
		if (ivjBnOk == null) {
			try {
				ivjBnOk = new nc.ui.pub.beans.UIButton();
				ivjBnOk.setName("BnOk");
				ivjBnOk.setText("确定");

			} catch (java.lang.Throwable ivjExc) {

			}
		}
		return ivjBnOk;
	}
	
	/**
	 * 返回 BnOk 特性值.
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告:此方法将重新生成. */
	nc.ui.pub.beans.UIButton getBnAllSel() {
		if (ivjBnAllSel == null) {
			try {
				ivjBnAllSel = new nc.ui.pub.beans.UIButton();
				ivjBnAllSel.setName("ivjBnAllSel");
				ivjBnAllSel.setText("全选");

			} catch (java.lang.Throwable ivjExc) {

			}
		}
		return ivjBnAllSel;
	}
	
	nc.ui.pub.beans.UIButton getBnCanAllSel() {
		if (ivjBnCanAllSel == null) {
			try {
				ivjBnCanAllSel = new nc.ui.pub.beans.UIButton();
				ivjBnCanAllSel.setName("ivjBnCanAllSel");
				ivjBnCanAllSel.setText("全消");

			} catch (java.lang.Throwable ivjExc) {

			}
		}
		return ivjBnCanAllSel;
	}
	
	nc.ui.pub.beans.UIButton getBnPrice() {
		if (ivjBnPrice == null) {
			try {
				ivjBnPrice = new nc.ui.pub.beans.UIButton();
				ivjBnPrice.setName("ivjBnPrice");
				ivjBnPrice.setText("修订历史平均价");

			} catch (java.lang.Throwable ivjExc) {

			}
		}
		return ivjBnPrice;
	}
	
	/**
	 * 返回 BnCancel 特性值.
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告:此方法将重新生成. */
	nc.ui.pub.beans.UIButton getBnCancel() {
		if (ivjBnCancel == null) {
			try {
				ivjBnCancel = new nc.ui.pub.beans.UIButton();
				ivjBnCancel.setName("BnCancel");
				ivjBnCancel.setText("取消");

			} catch (java.lang.Throwable ivjExc) {

			}
		}
		return ivjBnCancel;
	}	

	
	
//	public void clear(){
////		m_datas = null;
////		lseldata.clear();
////		getDataPanel().getHeadBillModel().clearBodyData();
//	}
//	public void setInitDatas(PuPlanInvVO[] datas){
//		clear();
////		m_datas = datas;
////		lseldata.clear();
////		getDataPanel().getHeadBillModel().setBodyDataVO(m_datas);
////		getDataPanel().getHeadBillModel().execLoadFormula();
//	}
	private void onprice(){
		HistoryPriceVO[] vos =(HistoryPriceVO[])getDataPanel().getBillModel().getBodySelectedVOs(HistoryPriceVO.class.getName());
		
		if(vos ==null || vos.length==0){
			tp.showErrorMessage("请选中数据");
			return;
		}
		
		Map<String, UFDouble> dd = null;
		try {
			dd = BiddingBillVO.colHistoryPrice(vos);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(dd == null)
			return;
		nnewprice = dd.get(cinvid);
		getDataPanel().getHeadItem("nprice").setValue(nnewprice);
		getBnOk().setEnabled(true);
	}
	public String getNewTs(){
		return newts;
	}
	public UFDouble getNewPrice(){
		return nnewprice;
	}
	
	public boolean isUpdate(){
		return PuPubVO.getUFDouble_NullAsZero(nprice).equals(PuPubVO.getUFDouble_NullAsZero(nnewprice));
	}
	private boolean onOK(){
		getBnOk().setEnabled(false);
		if(PuPubVO.getUFDouble_NullAsZero(nprice).equals(PuPubVO.getUFDouble_NullAsZero(nnewprice)))
			return true;
		try {
			newts = MakeBiddingHelper.updateBiddingHistoryPrice(cbiddingid, cinvid, nnewprice);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			tp.showErrorMessage(ZbPubTool.getString_NullAsTrimZeroLen(e.getMessage()));
			return false;
		}
		return true;
	}
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == getBnOk()){		
			if(onOK())
			closeOK();
		}else if(e.getSource() == getBnCancel()){
			closeCancel();
		}else if(e.getSource() == getBnPrice()){
			onprice();
		}else if(e.getSource() == getBnAllSel()){
			onAllSel();
		}else if(e.getSource() == getBnCanAllSel()){
			onAllCancelSel();
		}
	}
	
	/**
	 * 全消按钮事件 作者：薛恩平 创建日期：2008-3-14 下午04:49:00
	 */
	public void onAllCancelSel() {

		BillModel bodyModel = getDataPanel().getBillModel();
		IBillModelRowStateChangeEventListener bodyListener = bodyModel
				.getRowStateChangeEventListener();
		bodyModel.removeRowStateChangeEventListener();

		int rowcount =getDataPanel().getBillModel().getRowCount();
		if (rowcount > 0) {
			for (int i = 0; i < rowcount; i++) {
				getDataPanel().getBillModel().setRowState(i,BillModel.UNSTATE);
			}
		}
		bodyModel.addRowStateChangeEventListener(bodyListener);
		getDataPanel().updateUI();
	}

	/**
	 * 全选按钮事件 作者：薛恩平 创建日期：2008-3-14 下午04:48:58
	 */
	public void onAllSel() {

		BillModel bodyModel = getDataPanel().getBillModel();
		IBillModelRowStateChangeEventListener bodyListener = bodyModel
				.getRowStateChangeEventListener();
		bodyModel.removeRowStateChangeEventListener();

		int rowcount =getDataPanel().getBillModel().getRowCount();
		if (rowcount > 0) {
			for (int i = 0; i < rowcount; i++) {
				getDataPanel().getBillModel().setRowState(i,BillModel.SELECTED);
			}
		}
		bodyModel.addRowStateChangeEventListener(bodyListener);
		getDataPanel().updateUI();
	}
}
